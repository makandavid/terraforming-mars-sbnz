package com.tm.runner;

import com.tm.CEP.CardPlayedEvent;
import com.tm.CEP.TilePlayedEvent;
import com.tm.enums.*;
import com.tm.facts.*;
import com.tm.output.*;
import com.tm.config.TemplateRuleService;
import org.kie.api.time.SessionPseudoClock;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Component
public class RulesRunner implements CommandLineRunner {

    private final TemplateRuleService templateRuleService;

    public RulesRunner(TemplateRuleService templateRuleService) {
        this.templateRuleService = templateRuleService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=================================================");
            System.out.println("  TERRAFORMING MARS — STRATEGIC ADVISOR SYSTEM  ");
            System.out.println("=================================================");
            System.out.println("1. Run whole simulation (FC + BC)");
            System.out.println("2. Run just Forward Chaining (Levels 1-4)");
            System.out.println("3. Test Backward Chaining");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

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
                    System.out.println("System shut down.");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void runSimulation(boolean runFC, boolean runBC) {
        KieSession kieSession = templateRuleService.newSessionWithTemplates();

        try {
            System.out.println("=================================================");
            System.out.println("\nInitializing test scenario...");

            // GLOBAL STATE
            GameState gameState = new GameState(6, 6.0, -14.0, 3, 4);

            // CURRENT PLAYER (id=1)
            PlayerState currentPlayer = new PlayerState(
                    1L, true,
                    22,   // terraformRating
                    35,   // megacredits
                    5,    // mcProduction
                    4, 1, // steel, steelProduction
                    2, 0, // titanium, titaniumProduction
                    2, 1, // energy, energyProduction → FC-2
                    4, 1, // heat, heatProduction
                    16, 2,// plants, plantProduction → BC-3 via 2x standard project
                    2,    // cityCount → FC-7b, BC-1
                    2,    // greeneryCount → FC-7d, BC-3
                    3,    // scienceTagCount
                    7     // buildingTagCount → FC-7c
            );

            // OPPONENT (id=2)
            PlayerState opponent = new PlayerState(
                    2L, false,
                    20,   // terraformRating
                    14,   // megacredits
                    5,    // mcProduction → equal to player's, BC-5 recurse
                    6, 2, // steel, steelProduction
                    3, 1, // titanium, titaniumProduction
                    3, 3, // energy, energyProduction
                    2, 1, // heat, heatProduction
                    2, 1, // plants, plantProduction
                    2,    // cityCount → FC-4 OPPONENT_NEAR_MAYOR
                    2,    // greeneryCount
                    2,    // scienceTagCount → less than player's (3), BC-4 base case
                    3     // buildingTagCount
            );

            // PLAYED CARDS
            // 3x SCIENCE → FC-1 (SCIENCE_ENGINE_ACTIVE)
            PlayedCard research = new PlayedCard(
                    1L, "Research",
                    List.of(CardTag.SCIENCE), 11, 0, "", 1L, 2);
            PlayedCard filterers = new PlayedCard(
                    2L, "Filterers",
                    List.of(CardTag.SCIENCE), 4, 0, "", 1L, 3);
            PlayedCard olympus = new PlayedCard(
                    3L, "Olympus Conference",
                    Arrays.asList(CardTag.SCIENCE, CardTag.EARTH, CardTag.BUILDING),
                    10, 3, "", 1L, 4);

            // BUILDING tags → FC-10 (TagCount BUILDING), FC-7c
            PlayedCard steelWorks = new PlayedCard(
                    4L, "Steel Works",
                    List.of(CardTag.BUILDING), 15, 0, "", 1L, 3);
            PlayedCard miningArea = new PlayedCard(
                    5L, "Mining Area",
                    List.of(CardTag.BUILDING), 4, 1, "", 1L, 3);
            PlayedCard earthOffice = new PlayedCard(
                    6L, "Earth Office",
                    Arrays.asList(CardTag.EARTH, CardTag.BUILDING), 1, 0, "", 1L, 2);
            PlayedCard caretaker = new PlayedCard(
                    7L, "Caretaker Contract",
                    List.of(CardTag.BUILDING), 3, 0, "", 1L, 3);

            // OPPONENT'S PLAYED CARDS
            // SCIENCE tags — less than player's → BC-4 base case
            PlayedCard physicsComplex = new PlayedCard(
                    10L, "Physics Complex",
                    Arrays.asList(CardTag.SCIENCE, CardTag.BUILDING), 12, 4, "", 2L, 3);
            PlayedCard inventionContest = new PlayedCard(
                    11L, "Invention Contest",
                    List.of(CardTag.SCIENCE), 2, 0, "", 2L, 4);

            // CARDS IN HAND
            // Urbanized Area: places_city=true → BC-1 via card
            // cost=10, player mc=35 → can be played
            CardInHand cityCard = new CardInHand(
                    101L, "Urbanized Area",
                    Arrays.asList(CardTag.BUILDING, CardTag.CITY),
                    10, 0, "", 1L);
            cityCard.setPlacesCity(true);
            cityCard.setEnergyProductionIncrease(1);

            // Fusion Power: requires_energy=true, science_synergy_bonus=true
            // → FC-11 (BLOCKED_CARD jer energy production < 2 but science engine active)
            // → Template rule: requests 2+ SCIENCE tags, player has 3 → FIRES
            CardInHand fusionPower = new CardInHand(
                    102L, "Fusion Power",
                    Arrays.asList(CardTag.BUILDING, CardTag.POWER, CardTag.SCIENCE),
                    14, 0, "", 1L);
            fusionPower.setRequiresEnergy(true);
            fusionPower.setScienceSynergyBonus(true);
            fusionPower.setEnergyProductionIncrease(3);

            CardInHand solarPower = new CardInHand(
                    105L, "Solar Power",
                    Arrays.asList(CardTag.BUILDING, CardTag.POWER),
                    11, 1, "", 1L);
            solarPower.setEnergyProductionIncrease(1);

            CardInHand geothermalPower = new CardInHand(
                    106L, "Geothermal Power",
                    Arrays.asList(CardTag.BUILDING, CardTag.POWER),
                    11, 0, "", 1L);
            geothermalPower.setEnergyProductionIncrease(2);

            CardInHand nuclearPower = new CardInHand(
                    107L, "Nuclear Power",
                    Arrays.asList(CardTag.BUILDING, CardTag.POWER),
                    10, 0, "", 1L);
            nuclearPower.setEnergyProductionIncrease(3);

            // Acquired Company: mc production increase → za BC-5 recurse test
            CardInHand acquiredCompany = new CardInHand(
                    103L, "Acquired Company",
                    List.of(CardTag.BUILDING), 8, 0, "", 1L);
            acquiredCompany.setMcProductionIncrease(2);

            // TILES
            // player: 2 cities, 2 greeneries
            TilePlaced city1 = new TilePlaced(1L, TileType.CITY, 3);
            TilePlaced city2 = new TilePlaced(1L, TileType.CITY, 4);
            TilePlaced green1 = new TilePlaced(1L, TileType.GREENERY, 4);
            TilePlaced green2 = new TilePlaced(1L, TileType.GREENERY, 5);
            // opponent: 2 cities → FC-4
            TilePlaced oppCity1 = new TilePlaced(2L, TileType.CITY, 4);
            TilePlaced oppCity2 = new TilePlaced(2L, TileType.CITY, 5);

            // MILESTONES
            // Not claimed → BC-1 Mayor, BC-3 Gardener, BC-4 Builder
            Milestone mayor     = new Milestone(MilestoneType.MAYOR,      false, 0L, 0);
            Milestone builder   = new Milestone(MilestoneType.BUILDER,    false, 0L, 0);
            Milestone gardener  = new Milestone(MilestoneType.GARDENER,   false, 0L, 0);
            Milestone terraformer = new Milestone(MilestoneType.TERRAFORMER, false, 0L, 0);
            Milestone planner   = new Milestone(MilestoneType.PLANNER,    false, 0L, 0);

            // AWARDS
            // Not funded → BC-4 Scientist, BC-5 Banker
            Award scientist  = new Award(AwardType.SCIENTIST,  false, 0L, 8);
            Award banker     = new Award(AwardType.BANKER,     false, 0L, 8);
            Award landlord   = new Award(AwardType.LANDLORD,   false, 0L, 8);
            Award thermalist = new Award(AwardType.THERMALIST, false, 0L, 8);
            Award miner      = new Award(AwardType.MINER,      false, 0L, 8);

            // CEP EVENTS
            // CEP-1: opponent played 3 SCIENCE cards in last 2 generations
            // generation 5 and 6 are in sliding window (clock is on gen 6 = 6000ms,
            // window is 2000ms → covers gen 4,5,6)
            CardPlayedEvent sciEvent1 = new CardPlayedEvent(2L, 5, CardTag.SCIENCE);
            CardPlayedEvent sciEvent2 = new CardPlayedEvent(2L, 5,  CardTag.SCIENCE);
            CardPlayedEvent sciEvent3 = new CardPlayedEvent(2L, 6, CardTag.SCIENCE);

            // CEP-2: opponent placed 2 cities in gap <= 2 generations
            // generations 4 and 5 → gap = 1 <= 2 → MAYOR_RUSH
            TilePlayedEvent tileEvent1 = new TilePlayedEvent(2L, 4, TileType.CITY);
            TilePlayedEvent tileEvent2 = new TilePlayedEvent(2L, 5, TileType.CITY);

            // CEP-3: player did NOT raise temperature in last 2 generations
            // (we don't insert TemperatureRaisedEvent for player 1)
            // + player has raises_temperature card in hand
            CardInHand heatCard = new CardInHand(
                    104L, "Nuclear Zone",
                    List.of(CardTag.EARTH), 10, -2, "", 1L);
            heatCard.setRaisesTemperature(true);

            // INSERT ALL FACTS
            kieSession.insert(gameState);
            kieSession.insert(currentPlayer);
            kieSession.insert(opponent);

            // played cards
            kieSession.insert(research);
            kieSession.insert(filterers);
            kieSession.insert(olympus);
            kieSession.insert(steelWorks);
            kieSession.insert(miningArea);
            kieSession.insert(earthOffice);
            kieSession.insert(caretaker);
            kieSession.insert(physicsComplex);
            kieSession.insert(inventionContest);

            // cards in hand
            kieSession.insert(cityCard);
            kieSession.insert(fusionPower);
            kieSession.insert(acquiredCompany);
            kieSession.insert(heatCard);
            kieSession.insert(solarPower);
            kieSession.insert(geothermalPower);
            kieSession.insert(nuclearPower);

            // tiles
            kieSession.insert(city1);
            kieSession.insert(city2);
            kieSession.insert(green1);
            kieSession.insert(green2);
            kieSession.insert(oppCity1);
            kieSession.insert(oppCity2);

            // Milestones and awards
            kieSession.insert(mayor);
            kieSession.insert(builder);
            kieSession.insert(gardener);
            kieSession.insert(terraformer);
            kieSession.insert(planner);
            kieSession.insert(scientist);
            kieSession.insert(banker);
            kieSession.insert(landlord);
            kieSession.insert(thermalist);
            kieSession.insert(miner);

            // CEP events — must be inserted AS EVENTS (inserted in stream)
            // advance pseudo clock to current generation
            SessionPseudoClock clock = kieSession.getSessionClock();
            clock.advanceTime(gameState.getGeneration() * 1000L, TimeUnit.MILLISECONDS);

            kieSession.insert(sciEvent1);
            kieSession.insert(sciEvent2);
            kieSession.insert(sciEvent3);
            kieSession.insert(tileEvent1);
            kieSession.insert(tileEvent2);

            System.out.println("Facts successfully inserted. Count: "
                    + kieSession.getFactCount());

            // ── FIRE RULES ───────────────────────────────────────────────
            int fired = kieSession.fireAllRules(match -> {
                String name = match.getRule().getName();
                if (!runFC && (name.startsWith("FC-") || name.startsWith("ACC-"))) return false;
                if (!runBC && name.startsWith("BC-")) return false;
                if (!runBC && name.startsWith("Template-")) return false;
                return true;
            });

            System.out.println("\n--- Evaluation done. Activated rules: "
                    + fired + " ---");
            showResults(kieSession);
            System.out.println("=================================================");

        } finally {
            kieSession.dispose();
        }
    }

    private void showResults(KieSession kieSession) {
        System.out.println("\n>>> STRATEGIC REVIEW <<<");

        System.out.println("\n[INSIGHTS LEVEL 1]:");
        kieSession.getObjects(o -> o instanceof Insight).forEach(System.out::println);

        System.out.println("\n[ALERTS LEVEL 2]:");
        kieSession.getObjects(o -> o instanceof Alert).forEach(System.out::println);

        System.out.println("\n[MILESTONE REPORTS BC]:");
        kieSession.getObjects(o -> o instanceof MilestoneReport).forEach(System.out::println);

        System.out.println("\n[RECOMMENDATIONS]:");
        kieSession.getObjects(o -> o instanceof Recommendation).forEach(System.out::println);

        System.out.println("\n[STRATEGIC ADVICES]:");
        kieSession.getObjects(o -> o instanceof StrategicAdvice).forEach(System.out::println);

        System.out.println("\n[THREAT ALERTS]:");
        kieSession.getObjects(o -> o instanceof ThreatAlert).forEach(System.out::println);

        System.out.println("\n[SCORE PROJECTION ACC]:");
        kieSession.getObjects(o -> o instanceof ScoreProjection).forEach(System.out::println);
    }
}