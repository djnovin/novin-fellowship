package edu.monash.application.usecases;

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
    private static final String MESSAGE_SELECT_AT_LEAST_ONE = "At least one member must be selected. Please continue selecting members.";

    private final ConsoleOutputAdapter outputAdapter;
    private final NavigationService navigationService;
    private final CombatService combatService;
    private final Scanner scanner; // Keep a single Scanner instance

    public GameUseCase(ConsoleOutputAdapter outputAdapter, NavigationService navigationService,
            CombatService combatService, Scanner scanner) { // Pass the Scanner to the constructor
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
        int memberCount = 0;
        while (true) {
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
        if (type.equalsIgnoreCase("done")) {
            if (memberCount > 0) {
                return true;
            } else {
                outputAdapter.displayMessage(MESSAGE_SELECT_AT_LEAST_ONE);
            }
        }
        return false;
    }

    private boolean isInvalidMemberType(String type) {
        return !type.equalsIgnoreCase("elf") && !type.equalsIgnoreCase("dwarf");
    }

    private String promptForValidInput(String prompt, String errorMessage) {
        outputAdapter.displayMessage(prompt);
        String input = scanner.nextLine().trim(); // Use the shared Scanner
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

            // Ask the user to choose a direction to move
            outputAdapter.displayMessage("Choose a direction to move (north, east, south, west):");
            String direction = promptForValidInput("",
                    "Invalid direction. Please choose 'north', 'east', 'south', or 'west'.");

            Optional<Cave> nextCave = navigationService.move(currentCave, direction);

            if (nextCave.isPresent()) {
                currentCave = nextCave.get();

                // Only check for a creature after a successful move
                if (navigationService.checkForCreature(fellowship, currentCave)) {
                    handleCombat(fellowship, currentCave);
                }
            } else {
                outputAdapter.displayMessage("You can't move in that direction. Try again.");
            }
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
                currentCave.getCreature().takeDamage(currentCave.getCreature().getHealth()); // Creature is defeated
                return;
            }
        }

        // Regular combat if no weapon used
        boolean memberWins = combatService.executeCombat(selectedMember, currentCave.getCreature(),
                selectedMember.hasCode());

        if (memberWins) {
            outputAdapter.displayMessage(
                    selectedMember.getName() + " defeated the " + currentCave.getCreature().getName() + "!");
        } else {
            outputAdapter.displayMessage(
                    selectedMember.getName() + " was defeated by the " + currentCave.getCreature().getName() + "!");
        }
    }

    private Member selectMemberForCombat(Fellowship fellowship) {
        while (true) {
            outputAdapter.displayMessage("Choose a member to fight (enter the member's name):");
            String memberName = scanner.nextLine().trim(); // Use the shared Scanner
            Optional<Member> memberOpt = fellowship.getMembers().stream()
                    .filter(m -> m.getName().equalsIgnoreCase(memberName)).findFirst();

            if (memberOpt.isPresent()) {
                return memberOpt.get();
            } else {
                outputAdapter.displayMessage("Invalid member name. Please choose a member from the Fellowship.");
            }
        }
    }

    private boolean checkWinCondition(Cave currentCave) {
        return currentCave.getId() == 100;
    }

    private boolean checkLossCondition(Fellowship fellowship) {
        return !fellowship.isFellowshipAlive();
    }

    private void displayFinalSummary(Fellowship fellowship) {
        outputAdapter.displayMessage("Game Over!");
        outputAdapter.displayMessage("Final Status of Fellowship:");
        fellowship.displayStatus();

        outputAdapter.displayMessage("Thank you for playing!");
    }
}
