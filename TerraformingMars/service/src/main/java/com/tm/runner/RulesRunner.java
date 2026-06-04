package com.tm.runner;

import com.tm.enums.*;
import com.tm.facts.*;
import com.tm.output.*;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class RulesRunner implements CommandLineRunner {

    private final KieSession kieSession;

    public RulesRunner(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=================================================");
            System.out.println("  TERRAFORMING MARS — STRATESKI SAVETNIK SYSTEM  ");
            System.out.println("=================================================");
            System.out.println("1. Pokreni kompletnu simulaciju (FC + BC)");
            System.out.println("2. Pokreni samo Forward Chaining analizu (Nivoi 1-4)");
            System.out.println("3. Testiraj Backward Chaining Milestone dohvativost");
            System.out.println("0. Izlaz");
            System.out.print("Izaberite opciju: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    runSimulation(true, true);
                    break;
                case "2":
                    runSimulation(true, false);
                    break;
                case "3":
                    runSimulation(false, true);
                    break;
                case "0":
                    running = false;
                    System.out.println("Sistem ugasen.");
                    break;
                default:
                    System.out.println("Nevalidna opcija. Pokusajte ponovo.");
            }
        }
    }

    private void runSimulation(boolean runFC, boolean runBC) {
        System.out.println("\nInicijalizacija radne memorije i priprema stanja...");

        // 1. Globalno stanje igre
        GameState gameState = new GameState(6, 6.0, -12.0, 4, 4);

        // 2. Trenutni igrac (id=1.0)
        PlayerState currentPlayer = new PlayerState(
                1.0, true, 22, 15, 5,
                4, 1, 2, 0,
                2, 1, 4, 1,
                8, 1, 2, 2,
                3, 7
        );

        // 3. Protivnik (id=2.0)
        PlayerState opponent = new PlayerState(
                2.0, false, 20, 14, 5,
                6, 2, 3, 1,
                3, 3, 2, 1,
                2, 1, 2, 1,
                3, 3
        );

        // ── INSERT FACT BLOCKS ──
        kieSession.insert(gameState);
        kieSession.insert(currentPlayer);
        kieSession.insert(opponent);

        // Odigrane karte trenutnog igraca (Za FC nivo 1)
        kieSession.insert(new PlayedCard(1, "Research", List.of(CardTag.SCIENCE), 11, 0, "", 1.0, 3));
        kieSession.insert(new PlayedCard(2, "Filterers", List.of(CardTag.SCIENCE), 4, 0, "", 1.0, 4));
        kieSession.insert(new PlayedCard(3, "Olympus Conference", Arrays.asList(CardTag.SCIENCE, CardTag.EARTH, CardTag.BUILDING), 10, 3, "", 1.0, 5));
        kieSession.insert(new PlayedCard(4, "Steel Works", List.of(CardTag.BUILDING), 15, 0, "", 1.0, 4));
        kieSession.insert(new PlayedCard(5, "Mining Area", List.of(CardTag.BUILDING), 4, 1, "", 1.0, 3));
        kieSession.insert(new PlayedCard(6, "Earth Office", Arrays.asList(CardTag.EARTH, CardTag.BUILDING), 1, 0, "", 1.0, 2));
        kieSession.insert(new PlayedCard(7, "Caretaker Contract", List.of(CardTag.BUILDING), 3, 0, "", 1.0, 3));
        kieSession.insert(new PlayedCard(8, "Lava Flows", List.of(CardTag.BUILDING), 18, 0, "", 1.0, 4));
        kieSession.insert(new PlayedCard(9, "Mining Guild", Arrays.asList(CardTag.BUILDING, CardTag.BUILDING), 10, 0, "", 1.0, 5));

        // Protivnikove karte
        kieSession.insert(new PlayedCard(10, "Physics Complex", Arrays.asList(CardTag.SCIENCE, CardTag.BUILDING), 12, 4, "", 2.0, 3));
        kieSession.insert(new PlayedCard(11, "Invention Contest", List.of(CardTag.SCIENCE), 2, 0, "", 2.0, 4));
        kieSession.insert(new PlayedCard(12, "Search For Life", List.of(CardTag.SCIENCE), 3, 0, "", 2.0, 5));

        // Karte u ruci
        CardInHand cityCard = new CardInHand(101.0, "Urbanized Area", Arrays.asList(CardTag.BUILDING, CardTag.CITY), 10, 0, "", 1.0);
        cityCard.setPlacesCity(true);
        cityCard.setEnergyProductionIncrease(1);
        kieSession.insert(cityCard);

        CardInHand buildingCard = new CardInHand(102.0, "Acquired Company", List.of(CardTag.BUILDING), 8, 0, "", 1.0);
        buildingCard.setMcProductionIncrease(2);
        kieSession.insert(buildingCard);

        CardInHand scienceCard = new CardInHand(103.0, "Quantum Extractor", Arrays.asList(CardTag.SCIENCE, CardTag.POWER), 8, 0, "", 1.0);
        scienceCard.setRequiresEnergy(true);
        scienceCard.setScienceSynergyBonus(true);
        kieSession.insert(scienceCard);

        CardInHand productionCard = new CardInHand(104.0, "Earth Office Hand", Arrays.asList(CardTag.EARTH, CardTag.BUILDING), 8, 0, "", 1.0);
        productionCard.setMcProductionIncrease(2);
        kieSession.insert(productionCard);

        // Plocice i Milestone-ovi
        kieSession.insert(new TilePlaced(1.0, TileType.CITY, 3));
        kieSession.insert(new TilePlaced(1.0, TileType.CITY, 4));
        kieSession.insert(new TilePlaced(1.0, TileType.GREENERY, 4));
        kieSession.insert(new TilePlaced(1.0, TileType.GREENERY, 5));
        kieSession.insert(new TilePlaced(2.0, TileType.CITY, 4));
        kieSession.insert(new TilePlaced(2.0, TileType.CITY, 5));

        kieSession.insert(new Milestone(MilestoneType.MAYOR, false, 0.0, 0));
        kieSession.insert(new Milestone(MilestoneType.BUILDER, false, 0.0, 0));
        kieSession.insert(new Milestone(MilestoneType.GARDENER, false, 0.0, 0));
        kieSession.insert(new Milestone(MilestoneType.TERRAFORMER, false, 0.0, 0));
        kieSession.insert(new Milestone(MilestoneType.PLANNER, false, 0.0, 0));

        System.out.println("Fakti uspesno uvezeni. Ukupan broj objekata: " + kieSession.getFactCount());

        // Koriscenje Drools Agenda filtera za kontrolu izvrsavanja
        int fired = kieSession.fireAllRules(match -> {
            String name = match.getRule().getName();
            if (!runFC && name.startsWith("FC-")) return false;
            if (!runBC && name.startsWith("BC-")) return false;
            return true;
        });

        System.out.println("--- Evaluacija zavrsena. Broj aktiviranih pravila: " + fired + " ---");
        showResults();

        // Ciscenje radne memorije za sledecu CLI iteraciju
        kieSession.getFactHandles().forEach(kieSession::delete);
    }

    private void showResults() {
        System.out.println("\n>>> STRATESKI PREGLED REZULTATA <<<");

        System.out.println("\n[INSIGHTS NIVO 1]:");
        kieSession.getObjects(o -> o instanceof Insight).forEach(System.out::println);

        System.out.println("\n[ALERTS NIVO 2]:");
        kieSession.getObjects(o -> o instanceof Alert).forEach(System.out::println);

        System.out.println("\n[MILESTONE REPORTS BC]:");
        kieSession.getObjects(o -> o instanceof MilestoneReport).forEach(System.out::println);

        System.out.println("\n[PREPORUKE]:");
        kieSession.getObjects(o -> o instanceof Recommendation).forEach(System.out::println);

        System.out.println("\n[STRATESKI SAVETI]:");
        kieSession.getObjects(o -> o instanceof StrategicAdvice).forEach(System.out::println);
    }
}