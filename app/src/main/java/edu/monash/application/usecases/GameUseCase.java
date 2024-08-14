package edu.monash.application.usecases;

import java.util.Scanner;

import edu.monash.adapters.presenters.ConsoleOutputAdapter;
import edu.monash.domain.entities.Dwarf;
import edu.monash.domain.entities.Elf;
import edu.monash.domain.entities.Fellowship;
import edu.monash.domain.entities.Hobbit;
import edu.monash.domain.entities.Member;
import edu.monash.domain.services.CombatService;

public class GameUseCase {

    private final ConsoleOutputAdapter outputAdapter;
    private final CombatService combatService;

    public GameUseCase(ConsoleOutputAdapter outputAdapter) {
        this.outputAdapter = outputAdapter;
        this.combatService = new CombatService();
    }

    public Fellowship startGame() {
        outputAdapter.displayWelcomeMessage();
        return selectFellowshipMembers();
    }

    private Fellowship selectFellowshipMembers() {
        try (Scanner scanner = new Scanner(System.in)) {
            Fellowship fellowship = new Fellowship();

            outputAdapter.displayMessage("Enter the name of your Hobbit leader:");
            String hobbitName = scanner.nextLine();
            Member hobbit = new Hobbit(hobbitName);
            fellowship.addMember(hobbit);

            for (int i = 1; i < 4; i++) {
                outputAdapter.displayMessage("Choose a member type (Elf/Dwarf) or type 'done' to finish selection:");
                String type = scanner.nextLine().toLowerCase();

                if (type.equals("done")) {
                    break;
                }

                outputAdapter.displayMessage("Enter the name of your " + type + ":");
                String name = scanner.nextLine();
                Member member;

                switch (type) {
                    case "elf" -> member = new Elf(name);
                    case "dwarf" -> member = new Dwarf(name);
                    default -> {
                        outputAdapter.displayMessage("Invalid member type. Please choose 'Elf' or 'Dwarf'.");
                        i--; // Re-prompt for the same member
                        continue;
                    }
                }

                fellowship.addMember(member);
            }

            outputAdapter.displayMessage("Fellowship members selected:");
            fellowship.displayStatus();
            return fellowship;
        }
    }
}
