package edu.monash;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = LoggerSingleton.getLogger();
    private final Fellowship fellowship;
    private final Labyrinth labyrinth;

    public Main(Fellowship fellowship, Labyrinth labyrinth) {
        this.fellowship = fellowship;
        this.labyrinth = labyrinth;
    }

    public void run() {
        showWelcomeMessage();
        selectFellowshipMembers();
        try {
            loadLabyrinth();
            navigateLabyrinth();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading labyrinth: {0}", e.getMessage());
        }
    }

    private void showWelcomeMessage() {
        logger.info("Welcome to Fellowship of Code: A Java Adventure in Middle Earth!");
        logger.info("--------------------------------------------------------------");
        logger.info("In this game, you lead a group of adventurers known as the Fellowship.");
        logger.info("Your mission is to deliver a secret code to a Java wizard on Mount Api,");
        logger.info("navigating through a labyrinth of caves and fighting off evil creatures.");
        logger.info("");
        logger.info("You will choose up to 4 members for your Fellowship, led by a hobbit.");
        logger.info("The other members can be elves or dwarves. Each member has unique abilities.");
        logger.info("Beware of the orcs, trolls, and goblins that inhabit the caves!");
        logger.info("");
        logger.info("The game ends successfully when you deliver the code to Mount Api,");
        logger.info("or if the Fellowship perishes along the way.");
        logger.info("");
        logger.info("Good luck on your quest!");
        logger.info("--------------------------------------------------------------");
    }

    private void selectFellowshipMembers() {
        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Please select up to 4 members for your Fellowship.");
            logger.info("The first member must be a Hobbit (leader). The rest can be Elves or Dwarves.");

            logger.info("Enter the name of your Hobbit leader:");
            if (scanner.hasNextLine()) {
                String hobbitName = scanner.nextLine();
                Member hobbit = new Hobbit(hobbitName);
                fellowship.addMember(hobbit);
            } else {
                throw new NoSuchElementException("No input provided for Hobbit leader.");
            }

            int i = 1;
            boolean done = false;
            while (i < 4 && !done) {
                logger.info("Choose a member type (Elf/Dwarf) or type 'done' to finish selection:");
                if (!scanner.hasNextLine()) {
                    throw new NoSuchElementException("No input provided for member type.");
                }
                String type = scanner.nextLine().toLowerCase();

                if (type.equals("done")) {
                    done = true;
                    continue;  // Continue to the end of the loop
                }

                logger.info("Enter the name of your " + type + ":");
                if (!scanner.hasNextLine()) {
                    throw new NoSuchElementException("No input provided for member name.");
                }
                String name = scanner.nextLine();
                Member member = null;

                switch (type) {
                    case "elf" -> member = new Elf(name);
                    case "dwarf" -> member = new Dwarf(name);
                    default -> {
                        logger.warning("Invalid member type. Please choose 'Elf' or 'Dwarf'.");
                        continue;  // Skip incrementing i and retry the loop
                    }
                }

                if (member != null) {
                    fellowship.addMember(member);
                    i++;
                }
            }
        } catch (NoSuchElementException e) {
            logger.severe("Input error: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void loadLabyrinth() throws IOException {
        try {
            logger.info("Loading labyrinth...");
            
            String path = System.getProperty("user.dir");

            String filePath = path + "/app/src/main/resources/labyrinth.txt";

            labyrinth.loadFromFile(filePath);
            logger.info("Labyrinth loaded successfully.");
            labyrinth.logLabyrinthStructure();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading labyrinth: {0}", e.getMessage());
            throw e;
        }
    }

    private void navigateLabyrinth() {
        try (Scanner scanner = new Scanner(System.in)) {
            Cave currentCave = labyrinth.getStartingCave();

            while (true) {
                logger.log(Level.INFO, "You are currently in cave {0}", currentCave.getId());
                logger.info("Choose a direction to move (north, east, south, west):");

                if (!scanner.hasNextLine()) {
                    throw new NoSuchElementException("No input available for direction.");
                }

                String direction = scanner.nextLine().toLowerCase();
                Cave nextCave = null;

                switch (direction) {
                    case "north" -> nextCave = currentCave.getNorth();
                    case "east" -> nextCave = currentCave.getEast();
                    case "south" -> nextCave = currentCave.getSouth();
                    case "west" -> nextCave = currentCave.getWest();
                    default -> {
                        logger.warning("Invalid direction. Please choose 'north', 'east', 'south', or 'west'.");
                        continue;
                    }
                }

                if (nextCave == null) {
                    logger.warning("You cannot move in that direction.");
                } else {
                    currentCave = nextCave;
                    if (currentCave.getCreature() != null) {
                        Creature creature = currentCave.getCreature();
                        logger.log(Level.INFO, "You encounter a {0}!", creature.getName());

                        Member member = fellowship.getMembers().get(0);
                        member.attack(creature);

                        if (!creature.isAlive()) {
                            logger.log(Level.INFO, "The {0} has been defeated!", creature.getName());
                            currentCave.setCreature(null);
                        } else {
                            logger.log(Level.INFO, "The {0} is still alive!", creature.getName());
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        LoggerSingleton.getLogger();

        Fellowship fellowship = new Fellowship();
        Labyrinth labyrinth = new Labyrinth();

        Main game = new Main(fellowship, labyrinth);

        game.run();
    }
}
