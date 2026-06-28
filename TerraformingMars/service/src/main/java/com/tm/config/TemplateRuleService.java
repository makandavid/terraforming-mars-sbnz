package com.tm.config;

import com.tm.entity.CardSynergyEntity;
import com.tm.repository.CardSynergyRepository;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.*;

@Service
public class TemplateRuleService {

    private final CardSynergyRepository cardSynergyRepository;

    private volatile KieContainer templateKieContainer = null;

    private static final List<String> CARD_TAG_NAMES = Arrays.asList(
            "SCIENCE", "BUILDING", "POWER", "EARTH", "JOVIAN",
            "SPACE", "CITY", "ANIMAL", "MICROBE", "PLANT", "EVENT"
    );

    public TemplateRuleService(CardSynergyRepository cardSynergyRepository) {
        this.cardSynergyRepository = cardSynergyRepository;
    }

    // Fires AFTER the application context is fully started
    // and AFTER data.sql has been executed.
    // This pre-builds the template container so the first
    // analyze() request is not slow.
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("[TEMPLATE] ApplicationReadyEvent — " +
                "starting pre-compiling template rules...");
        try {
            buildContainerWithTemplates();
            System.out.println("[TEMPLATE] Pre-compiling template rules finished");
            System.out.println("=================================================");
        } catch (Exception e) {
            System.err.println("[TEMPLATE] Error while pre-compiling: " + e.getMessage());
            // Don't crash the application — lazy fallback still works
        }
    }

    public KieSession newSessionWithTemplates() {
        if (templateKieContainer == null) {
            synchronized (this) {
                if (templateKieContainer == null) {
                    System.out.println("[TEMPLATE] Lazy build — " +
                            "container is not ready yet...");
                    buildContainerWithTemplates();
                }
            }
        }
        return templateKieContainer.newKieSession("tm-session");
    }

    private synchronized void buildContainerWithTemplates() {
        // Double-check inside synchronized block
        if (templateKieContainer != null) {
            return;
        }

        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();

        kfs.write(ks.getResources()
                .newClassPathResource("rules/rules.drl")
                .setResourceType(ResourceType.DRL));
        kfs.write(ks.getResources()
                .newClassPathResource("rules/bc-rules.drl")
                .setResourceType(ResourceType.DRL));
        kfs.write(ks.getResources()
                .newClassPathResource("rules/cep-rules.drl")
                .setResourceType(ResourceType.DRL));

        kfs.writeKModuleXML(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<kmodule xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                        "    <kbase name=\"tm-rules\" packages=\"rules\" " +
                        "eventProcessingMode=\"stream\">\n" +
                        "        <ksession name=\"tm-session\" type=\"stateful\" " +
                        "clockType=\"pseudo\"/>\n" +
                        "    </kbase>\n" +
                        "</kmodule>"
        );

        String generatedDrl = generateTemplateRules();
        if (!generatedDrl.isBlank()) {
            kfs.write(
                    "src/main/resources/rules/card-synergy-generated.drl",
                    ks.getResources()
                            .newReaderResource(new StringReader(generatedDrl))
                            .setResourceType(ResourceType.DRL)
            );
        }

        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        kieBuilder.buildAll();

        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException(
                    "Error while compiling template rules: " +
                            kieBuilder.getResults().toString());
        }

        templateKieContainer = ks.newKieContainer(
                ks.getRepository().getDefaultReleaseId());

        System.out.println("[TEMPLATE] KieContainer with template rules is ready");
    }

    private String generateTemplateRules() {
        List<CardSynergyEntity> synergies =
                cardSynergyRepository.findByRequiredTagIn(CARD_TAG_NAMES);

        if (synergies.isEmpty()) {
            System.out.println("[TEMPLATE] Not synergies in database");
            return "";
        }

        StringBuilder drl = new StringBuilder();
        drl.append("package rules;\n\n");
        drl.append("import com.tm.facts.PlayerState;\n");
        drl.append("import com.tm.facts.PlayedCard;\n");
        drl.append("import com.tm.facts.CardInHand;\n");
        drl.append("import com.tm.enums.CardTag;\n");
        drl.append("import com.tm.enums.RecommendationType;\n");
        drl.append("import com.tm.enums.Priority;\n");
        drl.append("import com.tm.output.Recommendation;\n\n");

        for (CardSynergyEntity s : synergies) {
            String ruleName = "Template-Synergy-"
                    + s.getCardName().replaceAll("[^a-zA-Z0-9]", "-")
                    + "-" + s.getRequiredTag();

            drl.append("rule \"").append(ruleName).append("\"\n");
            drl.append("    when\n");
            drl.append("        $player: PlayerState(currentPlayer == true, $pid: id)\n");
            drl.append("        CardInHand(playerId == $pid, name == \"")
                    .append(s.getCardName()).append("\")\n");
            drl.append("        $count: Number(intValue >= ").append(s.getThreshold())
                    .append(") from accumulate(\n");
            drl.append("            PlayedCard(\n");
            drl.append("                playerId == $pid,\n");
            drl.append("                tags contains CardTag.").append(s.getRequiredTag())
                    .append("\n");
            drl.append("            ),\n");
            drl.append("            count(1)\n");
            drl.append("        )\n");
            drl.append("        not Recommendation(\n");
            drl.append("            playerId == $pid,\n");
            drl.append("            type == RecommendationType.ACTION_ENABLED_BY_CARD_CONDITION,\n");
            drl.append("            subject == \"").append(s.getCardName()).append("\"\n");
            drl.append("        )\n");
            drl.append("    then\n");
            drl.append("        insert(new Recommendation(\n");
            drl.append("            $pid,\n");
            drl.append("            RecommendationType.ACTION_ENABLED_BY_CARD_CONDITION,\n");
            drl.append("            Priority.").append(s.getPriority()).append(",\n");
            drl.append("            \"").append(s.getCardName()).append("\",\n");
            drl.append("            \"").append(s.getEffectText())
                    .append(" (currently you have \" + $count.intValue() + \" ")
                    .append(s.getRequiredTag()).append(" tags)\"\n");
            drl.append("        ));\n");
            drl.append("        System.out.println(\"[TEMPLATE] Synergy: ")
                    .append(ruleName)
                    .append(", count=\" + $count.intValue() + \")\");\n");
            drl.append("end\n\n");
        }

        System.out.println("[TEMPLATE] Generated " + synergies.size() +
                " rules from database");
        return drl.toString();
    }
}