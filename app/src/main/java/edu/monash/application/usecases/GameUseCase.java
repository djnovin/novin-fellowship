package edu.monash.application.usecases;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import edu.monash.adapters.ConsoleOutputAdapter;
import edu.monash.domain.entities.Cave;
import edu.monash.domain.entities.Dwarf;
import edu.monash.domain.entities.Elf;
import edu.monash.domain.entities.Fellowship;
import edu.monash.domain.entities.Hobbit;
import edu.monash.domain.entities.Member;
import edu.monash.domain.services.CombatService;
import edu.monash.domain.services.NavigationService;

public class GameUseCase {

    private static final String PROMPT_MEMBER_TYPE = "Choose a member type (Elf/Dwarf) or type 'done' to finish selection:";
    private static final String ERROR_INVALID_MEMBER_TYPE = "Invalid member type. Please choose 'Elf' or 'Dwarf' or type 'done' to finish selection:";
    private static final String ERROR_EMPTY_NAME = "Name cannot be empty. Please enter a name:";
    private static final String ERROR_DUPLICATE_NAME = "A member with that name already exists. Please choose a different name.";
    private static final int MAX_MEMBERS = 4;

    private final ConsoleOutputAdapter outputAdapter;
    private final NavigationService navigationService;
    private final CombatService combatService;
    private final Scanner scanner;

    public GameUseCase(ConsoleOutputAdapter outputAdapter, NavigationService navigationService,
            CombatService combatService, Scanner scanner) {
        this.outputAdapter = outputAdapter;
        this.navigationService = navigationService;
        this.combatService = combatService;
        this.scanner = scanner;
    }

    public void startGame() {
        outputAdapter.displayWelcomeMessage();
        Fellowship fellowship = selectFellowshipMembers();
        playGame(fellowship);
    }

    private Fellowship selectFellowshipMembers() {
        Fellowship fellowship = new Fellowship();
        addHobbitLeader(fellowship);
        addOtherMembers(fellowship);
        outputAdapter.displayMessage("Fellowship members selected:");
        fellowship.displayStatus();
        return fellowship;
    }

    private void addHobbitLeader(Fellowship fellowship) {
        String hobbitName = promptForValidInput("Enter the name of your Hobbit leader:",
                "Hobbit name cannot be empty. Please enter a name:");
        Member hobbit = new Hobbit(hobbitName);
        fellowship.addMember(hobbit);
    }

    private void addOtherMembers(Fellowship fellowship) {
        int memberCount = 1; // 1 because Hobbit leader is already added
        while (memberCount < MAX_MEMBERS) {
            String type = promptForValidInput(PROMPT_MEMBER_TYPE, ERROR_INVALID_MEMBER_TYPE);

            if (isDoneSelection(type, memberCount)) {
                break;
            }

            if (isInvalidMemberType(type)) {
                outputAdapter.displayMessage(ERROR_INVALID_MEMBER_TYPE);
                continue;
            }

            String name = promptForValidInput("Enter the name of your " + type + ":", ERROR_EMPTY_NAME);

            if (isDuplicateName(fellowship, name)) {
                outputAdapter.displayMessage(ERROR_DUPLICATE_NAME);
                continue;
            }

            Member member = createMember(type, name);
            if (member != null) {
                fellowship.addMember(member);
                memberCount++;
            }
        }
    }

    private boolean isDoneSelection(String type, int memberCount) {
        return type.equalsIgnoreCase("done") && memberCount >= 2;
    }

    private boolean isInvalidMemberType(String type) {
        return !type.equalsIgnoreCase("elf") && !type.equalsIgnoreCase("dwarf");
    }

    private String promptForValidInput(String prompt, String errorMessage) {
        outputAdapter.displayMessage(prompt);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            outputAdapter.displayMessage(errorMessage);
            outputAdapter.displayMessage(prompt);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private boolean isDuplicateName(Fellowship fellowship, String name) {
        return fellowship.getMembers().stream().anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    private Member createMember(String type, String name) {
        return switch (type.toLowerCase()) {
            case "elf" -> new Elf(name);
            case "dwarf" -> new Dwarf(name);
            default -> null;
        };
    }

    private void playGame(Fellowship fellowship) {
        Cave currentCave = navigationService.getStartingCave();

        while (true) {
            outputAdapter.displayMessage("You are currently in cave " + currentCave.getId());

            // Check if the Fellowship has reached Mount Api
            if (checkWinCondition(currentCave)) {
                outputAdapter.displayMessage(
                        "Congratulations! The Fellowship has reached Mount Api and delivered the secret code!");
                break;
            }

            // Check if the Fellowship has perished
            if (checkLossCondition(fellowship)) {
                outputAdapter.displayMessage("Game over. The Fellowship has perished.");
                break;
            }

            // Check for creatures and handle combat if necessary
            if (navigationService.checkForCreature(fellowship, currentCave)) {
                handleCombat(fellowship, currentCave);
            }

            // After combat or if no creatures, handle navigation to the next cave
            currentCave = handlePostCombatNavigation(fellowship, currentCave);

            // Display cave status after events
            displayCaveStatus();
        }

        displayFinalSummary(fellowship);
    }

    private void handleCombat(Fellowship fellowship, Cave currentCave) {
        Member selectedMember = selectMemberForCombat(fellowship);

        // Prompt to use special weapon if available
        if (selectedMember.hasSpecialWeapon()) {
            outputAdapter.displayMessage(
                    selectedMember.getName() + " has a special weapon! Do you want to use it? (yes/no)");

            String useWeapon = scanner.nextLine().trim().toLowerCase();

            if (!useWeapon.equals("yes") && !useWeapon.equals("no")) {
                outputAdapter.displayMessage("Invalid input. Please enter 'yes' or 'no'.");
                return;
            }

            if (useWeapon.equals("yes")) {
                selectedMember.useSpecialWeapon();
                outputAdapter.displayMessage(selectedMember.getName() + " used the special weapon and defeated the "
                        + currentCave.getCreature().getName() + "!");
                currentCave.getCreature().setIsDead(true); // Creature is defeated
                return;
            }
        }

        // Regular combat if no weapon used
        boolean memberWins = combatService.executeCombat(selectedMember, currentCave.getCreature(),
                selectedMember.getHasCode());

        if (memberWins) {
            outputAdapter.displayMessage(
                    selectedMember.getName() + " defeated the " + currentCave.getCreature().getName() + "!");
            selectedMember.setDamagePoints(selectedMember.getDamagePoints() + 1); // Increase damage points for the
                                                                                  // winner
        } else {
            outputAdapter.displayMessage(
                    selectedMember.getName() + " was defeated by the " + currentCave.getCreature().getName() + "!");
            selectedMember.setDamagePoints(selectedMember.getDamagePoints() + 4); // Increase damage points for the
                                                                                  // loser
        }
    }

    private Member selectMemberForCombat(Fellowship fellowship) {
        while (true) {
            outputAdapter.displayMessage("Choose a member to fight (enter the member's name):");
            String memberName = scanner.nextLine().trim();
            Optional<Member> memberOpt = fellowship.getMembers().stream()
                    .filter(m -> m.getName().equalsIgnoreCase(memberName)).findFirst();

            if (memberOpt.isPresent()) {
                return memberOpt.get();
            } else {
                outputAdapter.displayMessage("Invalid member name. Please choose a member from the Fellowship.");
            }
        }
    }

    private Cave handlePostCombatNavigation(Fellowship fellowship, Cave currentCave) {
        // Determine if the Fellowship has the code
        boolean fellowshipHasCode = fellowship.getMembers().stream().anyMatch(Member::getHasCode);

        // Allow the player to choose the next cave
        if (fellowshipHasCode) {
            outputAdapter.displayMessage("You have the secret code. You may proceed to Mount Api.");
        } else {
            outputAdapter.displayMessage("You must retrieve the secret code before proceeding to Mount Api.");

            Cave caveWithCode = navigationService.findCaveWithCode();

            outputAdapter.displayMessage("Cave with code: " + caveWithCode);

            if (caveWithCode != null) {
                outputAdapter
                        .displayMessage("Navigating back to cave " + caveWithCode.getId() + " to retrieve the code.");
                return caveWithCode;
            }
        }

        if (fellowshipHasCode) {
            return navigationService.getCaveById(100);
        }

        return chooseNextCave(currentCave);
    }

    private Optional<Cave> getNextCave(Cave currentCave, String direction) {
        return navigationService.getAvailableCaves(currentCave).stream()
                .filter(c -> direction.equalsIgnoreCase(currentCave.getDirectionTo(c)))
                .findFirst();
    }

    private Cave chooseNextCave(Cave currentCave) {
        List<Cave> availableCaves = navigationService.getAvailableCaves(currentCave);

        // Check if Mount Api (cave ID 100) is an available option
        Optional<Cave> mountApiCave = availableCaves.stream().filter(c -> c.getId() == 100).findFirst();
        if (mountApiCave.isPresent()) {
            outputAdapter.displayMessage("Mount Api is available. Automatically proceeding to Mount Api (Cave 100).");
            return mountApiCave.get();
        }

        if (availableCaves.size() == 1) {
            outputAdapter.displayMessage(
                    "Only one cave available. Automatically entering cave " + availableCaves.get(0).getId());
            return availableCaves.get(0);
        }

        // Prompt the user to choose a direction if there are multiple options
        while (true) {
            outputAdapter.displayMessage("Choose a direction to move (north, east, south, west):");
            String direction = scanner.nextLine().trim().toLowerCase();

            Optional<Cave> nextCave = getNextCave(currentCave, direction);
            if (nextCave.isPresent()) {
                return nextCave.get();
            } else {
                outputAdapter.displayMessage("Invalid direction. Please choose 'north', 'east', 'south', or 'west'.");
            }
        }
    }

    private void displayCaveStatus() {
        outputAdapter.displayMessage("Caves visited:");
        List<Cave> visitedCaves = navigationService.getVisitedCaves();
        for (Cave cave : visitedCaves) {
            String creatureStatus = cave.getCreature() != null ? "Creature present: " + cave.getCreature().getName()
                    : "No creature";
            outputAdapter.displayMessage("Cave " + cave.getId() + ": " + creatureStatus);
        }

        outputAdapter.displayMessage("Current creature status:");
        visitedCaves.stream()
                .filter(cave -> cave.getCreature() != null)
                .forEach(cave -> outputAdapter.displayMessage("Creature " + cave.getCreature().getName()
                        + " holding the code: " + cave.getCreature().getHasCode()
                        + " Damage points: " + cave.getCreature().getDamagePoints()));

        outputAdapter.displayMessage("Next cave to enter: ");
    }

    private boolean checkWinCondition(Cave currentCave) {
        return currentCave.getId() == 100;
    }

    private boolean checkLossCondition(Fellowship fellowship) {
        return !fellowship.isFellowshipAlive();
    }

    private void displayFinalSummary(Fellowship fellowship) {
        outputAdapter.displayMessage("Game Over!");

        boolean questSuccessful = checkWinCondition(navigationService.getCurrentCave());

        if (questSuccessful) {
            outputAdapter.displayMessage("Outcome: The quest was successful! The Fellowship delivered the code.");
        } else {
            outputAdapter.displayMessage("Outcome: The quest failed. The Fellowship did not deliver the code.");
        }

        // Display the creature that delivered the code, if any
        Member codeBearer = fellowship.getMembers().stream()
                .filter(Member::getHasCode)
                .findFirst()
                .orElse(null);

        if (codeBearer != null) {
            outputAdapter.displayMessage("The code was delivered by: " + codeBearer.getName());
        } else {
            outputAdapter.displayMessage("No creature delivered the code.");
        }

        List<Cave> visitedCaves = navigationService.getVisitedCaves();
        outputAdapter.displayMessage("Number of caves visited: " + visitedCaves.size());

        int codeChanges = navigationService.getCodeChangeCount();
        outputAdapter.displayMessage("Number of times the secret code changed hands: " + codeChanges);

        List<String> deadCreatures = navigationService.getDeadCreatures();

        if (deadCreatures.isEmpty()) {
            outputAdapter.displayMessage("No creatures died during the quest.");
        } else {
            outputAdapter.displayMessage("List of creatures that died:");
            deadCreatures.forEach(creatureName -> outputAdapter.displayMessage(" - " + creatureName));
        }

        int totalFights = combatService.getTotalFights();
        int fightsWon = combatService.getFightsWon();

        double successRate = totalFights > 0 ? (double) fightsWon * 100 / totalFights : 0;

        outputAdapter.displayMessage("Fellowship fight success rate: " + String.format("%.2f", successRate) + "%");

        outputAdapter.displayMessage("Final Status of Fellowship:");
        fellowship.displayStatus();

        outputAdapter.displayMessage("Thank you for playing!");
    }
}
