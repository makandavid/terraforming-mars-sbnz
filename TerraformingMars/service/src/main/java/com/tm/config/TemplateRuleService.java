package com.tm.config;

import com.tm.entity.CardSynergyEntity;
import com.tm.repository.CardSynergyRepository;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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
    @EventListener(ContextRefreshedEvent.class)
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
            System.out.println("[TEMPLATE] No synergies in database");
            return "";
        }

        try (InputStream templateStream = getClass().getResourceAsStream("/rules/card-synergy.drt")) {
            if (templateStream == null) {
                throw new RuntimeException("[TEMPLATE] Template rules not found at /rules/card-synergy.drt");
            }

            ObjectDataCompiler compiler = new ObjectDataCompiler();

            String generatedDrl = compiler.compile(synergies, templateStream);

            System.out.println("[TEMPLATE] Generated " + synergies.size() +
                    " rules from card-synergy.drt + database.");
            System.out.println("[TEMPLATE] Generated DRL preview (first 500 chars):\n" +
                    generatedDrl.substring(0, Math.min(500, generatedDrl.length())));

            return generatedDrl;
        } catch (Exception e) {
            throw new RuntimeException("[TEMPLATE] Failed to generate template rules: " + e.getMessage(), e);
        }
    }

}