package com.tm.runner;

import com.tm.enums.*;
import com.tm.facts.*;
import com.tm.output.Alert;
import com.tm.output.Insight;
import com.tm.output.Recommendation;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RulesRunner implements CommandLineRunner {

    private final KieSession kieSession;

    public RulesRunner(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    @Override
    public void run(String... args) {
        System.out.println("TERRAFORMING MARS — STRATESKI SAVETNIK");

        // Scenario
        // Generacija 6. Trenutni igrac ima 4 science taga odigrana,
        // energetsku produkciju 1 (ispod 2 — aktivira FC-2),
        // Quantum Extractor u ruci (blokiran — aktivira FC-11),
        // kartu za grad u ruci (aktivira FC-17).
        // Protivnik ima 2 grada (aktivira FC-4 i FC-12).
        // Mayor milestone nije osvijen.

        // 1. Globalno stanje igre
        GameState gameState = new GameState(6, 6.0, -12.0, 4, 4);

        // 2. Trenutni igrac (id = 1.0)
        // @AllArgsConstructor redosled:
        // id, isCurrentPlayer, terraformRating, megacredits, mcProduction,
        // steel, steelProduction, titanium, titaniumProduction,
        // energy, energyProduction, heat, heatProduction,
        // plants, plantProduction, cityCount, greeneryCount,
        // scienceTagCount, buildingTagCount
        PlayerState currentPlayer = new PlayerState(
                1.0,    // id
                true,   // isCurrentPlayer
                22,     // terraformRating
                9,      // megacredits
                6,      // mcProduction
                4,      // steel
                1,      // steelProduction
                2,      // titanium
                0,      // titaniumProduction
                2,      // energy
                1,      // energyProduction  <-- ispod 2, aktivira FC-2
                4,      // heat
                1,      // heatProduction
                3,      // plants
                1,      // plantProduction
                0,      // cityCount         <-- ispod 2, doprinosi FC-12
                1,      // greeneryCount
                4,      // scienceTagCount
                2       // buildingTagCount
        );

        // 3. Protivnik (id = 2.0)
        PlayerState opponent = new PlayerState(
                2.0,    // id
                false,  // isCurrentPlayer
                20,     // terraformRating
                14,     // megacredits
                8,      // mcProduction
                6,      // steel
                2,      // steelProduction
                3,      // titanium
                1,      // titaniumProduction
                3,      // energy
                3,      // energyProduction
                2,      // heat
                1,      // heatProduction
                2,      // plants
                1,      // plantProduction
                2,      // cityCount         <-- protivnik ima 2 grada
                1,      // greeneryCount
                2,      // scienceTagCount
                3       // buildingTagCount
        );

        // 4. Odigrane karte trenutnog igraca — 4 science taga ukupno
        PlayedCard research = new PlayedCard(
                1.0, "Research",
                List.of(CardTag.SCIENCE), 11, 0, "Draw 2 cards",
                1.0, 3);

        PlayedCard filterers = new PlayedCard(
                2.0, "Filterers",
                List.of(CardTag.SCIENCE), 4, 0, "+1 TR when oxygen raised",
                1.0, 4);

        PlayedCard olympus = new PlayedCard(
                3.0, "Olympus Conference",
                Arrays.asList(CardTag.SCIENCE, CardTag.EARTH, CardTag.BUILDING),
                10, 3, "Resource every 3rd science card",
                1.0, 5);

        PlayedCard viralEnhancers = new PlayedCard(
                4.0, "Viral Enhancers",
                Arrays.asList(CardTag.SCIENCE, CardTag.MICROBE),
                9, 0, "When playing plant/microbe/animal add resource",
                1.0, 6);

        // 5. Karte u ruci trenutnog igraca
        // Quantum Extractor — blokiran (treba energiju + science sinergiju)
        CardInHand quantumExtractor = new CardInHand(
                1.0, "Quantum Extractor",
                Arrays.asList(CardTag.POWER, CardTag.SCIENCE),
                13, 0, "Doubles energy production with 4+ science tags",
                1.0);
        quantumExtractor.setRequiresEnergy(true);
        quantumExtractor.setScienceSynergyBonus(true);

        // Urbanized Area — karta koja postavlja grad
        CardInHand urbanized = new CardInHand(
                2.0, "Urbanized Area",
                Arrays.asList(CardTag.BUILDING, CardTag.CITY),
                10, 0, "Place a city. -1 plant prod, +2 energy prod",
                1.0);
        urbanized.setPlacesCity(true);

        // 6. Plocice protivnika — 2 postavljena grada
        TilePlaced opponentCity1 = new TilePlaced(2.0, TileType.CITY, 4);
        TilePlaced opponentCity2 = new TilePlaced(2.0, TileType.CITY, 5);

        // 7. Milestoneovi — Mayor slobodan (nije osvijen)
        Milestone mayor = new Milestone(MilestoneType.MAYOR, 0.0, 0);
        Milestone builder = new Milestone(MilestoneType.BUILDER, 0.0, 0);
        Milestone gardener = new Milestone(MilestoneType.GARDENER, 0.0, 0);

        // Insertovanje fakata
        System.out.println("\n--- Insertovanje fakata u radnu memoriju ---");
        kieSession.insert(gameState);
        kieSession.insert(currentPlayer);
        kieSession.insert(opponent);
        kieSession.insert(research);
        kieSession.insert(filterers);
        kieSession.insert(olympus);
        kieSession.insert(viralEnhancers);
        kieSession.insert(quantumExtractor);
        kieSession.insert(urbanized);
        kieSession.insert(opponentCity1);
        kieSession.insert(opponentCity2);
        kieSession.insert(mayor);
        kieSession.insert(builder);
        kieSession.insert(gardener);
        System.out.println("Fakti insertovani: " + kieSession.getFactCount());

        // Pokretanje pravila
        System.out.println("\n--- Pokretanje pravila ---");
        int rulesFired = kieSession.fireAllRules();
        System.out.println("Pravila aktivirana: " + rulesFired);

        // Prikaz rezultata
        System.out.println("REZULTATI — INSIGHTS (nivo 1)");
        kieSession.getObjects(obj -> obj instanceof Insight)
                .stream()
                .map(obj -> (Insight) obj)
                .forEach(i -> System.out.println("  " + i));

        System.out.println("REZULTATI — ALERTS (nivo 2)");
        kieSession.getObjects(obj -> obj instanceof Alert)
                .stream()
                .map(obj -> (Alert) obj)
                .sorted((a, b) -> a.getPriority().compareTo(b.getPriority()))
                .forEach(a -> System.out.println("  " + a));

        System.out.println("REZULTATI — PREPORUKE (nivo 3)");
        List<Recommendation> recommendations = kieSession.getObjects(obj -> obj instanceof Recommendation)
                .stream()
                .map(obj -> (Recommendation) obj)
                .sorted((a, b) -> a.getPriority().compareTo(b.getPriority()))
                .toList();

        if (recommendations.isEmpty()) {
            System.out.println("Nema preporuka.");
        } else {
            recommendations.forEach(r -> System.out.println("  " + r));
        }

        kieSession.dispose();
    }
}
