package edu.monash.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.monash.domain.entities.Cave;
import edu.monash.domain.entities.Creature;
import edu.monash.domain.entities.Fellowship;
import edu.monash.domain.entities.Goblin;
import edu.monash.domain.entities.Labyrinth;
import edu.monash.domain.entities.Member;
import edu.monash.domain.entities.Orc;
import edu.monash.domain.entities.Troll;

/**
 * Service class to handle navigation logic in the labyrinth.
 */
public class NavigationService {
    private static final Logger logger = Logger.getLogger(NavigationService.class.getName());
    private final CombatService combatService;
    private final Random random;
    private final Labyrinth labyrinth;
    private final List<Cave> visitedCaves;
    private boolean allCreaturesKilled;

    /**
     * Constructs a NavigationService with the specified labyrinth.
     *
     * @param labyrinth The labyrinth to navigate.
     */
    public NavigationService(Labyrinth labyrinth) {
        this.combatService = new CombatService();
        this.random = new Random();
        this.labyrinth = labyrinth;
        this.visitedCaves = new ArrayList<>();
        this.allCreaturesKilled = false;
    }

    /**
     * Adds a cave to the list of visited caves.
     *
     * @param cave The cave to add.
     */
    public void addVisitedCave(Cave cave) {
        if (!visitedCaves.contains(cave)) {
            visitedCaves.add(cave);
        }
    }

    /**
     * Moves the Fellowship to the next cave in the specified direction.
     *
     * @param currentCave The current cave.
     * @param direction   The direction to move in (north, east, south, west).
     * @return The next cave if the move is valid, or an empty Optional if the move
     *         is invalid.
     */
    public Optional<Cave> move(Cave currentCave, String direction) {
        Optional<Cave> nextCave = getNextCave(currentCave, direction);

        if (nextCave.isPresent()) {
            logger.log(Level.INFO, "Fellowship moved {0} to Cave {1}",
                    new Object[] { direction, nextCave.get().getId() });
            visitedCaves.add(nextCave.get()); // Add the cave to the list of visited caves
            return nextCave;
        } else {
            logger.log(Level.WARNING, "Invalid move. There is no cave in the {0} direction.", direction);
            return Optional.empty();
        }
    }

    public Cave getCaveById(int id) {
        return labyrinth.getCaves().get(id);
    }

    /**
     * Returns the cave in the specified direction from the current cave.
     *
     * @param currentCave The current cave.
     * @param direction   The direction to check (north, east, south, west).
     * @return The cave in the specified direction, or an empty Optional if there is
     *         no cave in that direction.
     */
    private Optional<Cave> getNextCave(Cave currentCave, String direction) {
        return switch (direction.toLowerCase()) {
            case "north" -> Optional.ofNullable(currentCave.getNorth());
            case "east" -> Optional.ofNullable(currentCave.getEast());
            case "south" -> Optional.ofNullable(currentCave.getSouth());
            case "west" -> Optional.ofNullable(currentCave.getWest());
            default -> Optional.empty();
        };
    }

    /**
     * Returns the number of code changes made by the Fellowship.
     *
     * @return The number of code changes made by the Fellowship.
     */
    public int getCodeChangeCount() {
        return combatService.getFightsWon();
    }

    /**
     * Returns the total number of fights that have occurred in the labyrinth.
     *
     * @return The total number of fights that have occurred in the labyrinth.
     */
    public int getTotalFights() {
        return combatService.getTotalFights();
    }

    /**
     * Returns the number of caves visited by the Fellowship.
     *
     * @return The number of caves visited by the Fellowship.
     */
    public int getVisitedCavesCount() {
        return visitedCaves.size();
    }

    /**
     * Returns the current cave of the Fellowship.
     *
     * @return The current cave of the Fellowship.
     */
    public Cave getCurrentCave() {
        return visitedCaves.isEmpty() ? getStartingCave() : visitedCaves.get(visitedCaves.size() - 1);
    }

    public List<String> getDeadCreatures() {
        return labyrinth.getCaves().values().stream()
                .map(Cave::getCreature)
                .filter(Objects::nonNull)
                .filter(Creature::getIsDead)
                .map(Creature::getName)
                .toList();
    }

    /**
     * Finds the cave containing the code.
     *
     * @return The cave containing the code, or null if not found.
     */
    public Cave findCaveWithCode() {
        return labyrinth.getCaves().values().stream()
                .filter(cave -> cave.getCreature() != null && cave.getCreature().getHasCode())
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks for a creature encounter in the current cave.
     *
     * @param fellowship  The Fellowship to check for a creature encounter.
     * @param currentCave The current cave of the Fellowship.
     * @return true if a creature encounter occurred, false otherwise.
     */
    public boolean checkForCreature(Fellowship fellowship, Cave currentCave) {
        if (allCreaturesKilled) {
            logger.info("All creatures are dead. The Fellowship can navigate safely.");
            return false; // No combat if all creatures are dead
        }

        Creature creature = currentCave.getCreature();

        // If no creature is in the cave, we may randomly assign one
        if (creature == null && random.nextFloat() <= 0.75f) { // 75% chance of encountering a creature
            creature = createRandomCreature();
            currentCave.setCreature(creature);
            logger.log(Level.INFO, "A {0} has appeared in Cave {1}!",
                    new Object[] { creature.getName(), currentCave.getId() });
        }

        if (creature != null) {
            logger.log(Level.INFO, "A {0} has appeared in Cave {1}!",
                    new Object[] { creature.getName(), currentCave.getId() });

            // Handle the creature encounter, initiate combat
            Member chosenMember = selectMemberForFight(fellowship);
            boolean memberHasCode = chosenMember.getHasCode();
            boolean memberWon = combatService.executeCombat(chosenMember, creature, memberHasCode);

            if (memberWon) {
                logger.log(Level.INFO, "{0} defeated the {1}!",
                        new Object[] { chosenMember.getName(), creature.getName() });
                checkAllCreaturesKilled(); // Check if all creatures are killed after each combat
            } else {
                logger.log(Level.INFO, "{0} was defeated by the {1}.",
                        new Object[] { chosenMember.getName(), creature.getName() });
            }
            return true; // A creature encounter occurred
        } else {
            logger.info("No creatures in this cave.");

            // Fellowship members recover if no creatures are present
            fellowship.getMembers().forEach(Member::recoverPoints);

            return false; // No creature encounter
        }
    }

    /**
     * Checks if all creatures in the labyrinth have been defeated.
     */
    private void checkAllCreaturesKilled() {
        boolean allDead = labyrinth.getCaves().values().stream()
                .map(Cave::getCreature)
                .filter(Objects::nonNull)
                .allMatch(Creature::getIsDead);

        if (allDead) {
            allCreaturesKilled = true;
            logger.info("All creatures in the labyrinth have been defeated.");
        }
    }

    /**
     * Creates a random creature (Orc, Troll, or Goblin).
     *
     * @return A random creature.
     */
    private Creature createRandomCreature() {
        int choice = random.nextInt(3);
        return switch (choice) {
            case 0 -> new Orc();
            case 1 -> new Troll();
            case 2 -> new Goblin();
            default -> null;
        };
    }

    /**
     * Returns the list of caves visited by the Fellowship.
     *
     * @return A list of caves visited by the Fellowship.
     */
    public List<Cave> getVisitedCaves() {
        return new ArrayList<>(visitedCaves);
    }

    /**
     * Selects a member for a fight.
     *
     * @param fellowship The Fellowship to select a member from.
     * @return The selected member for the fight.
     */

    private Member selectMemberForFight(Fellowship fellowship) {
        return fellowship.getMembers().get(0);
    }

    /**
     * Retrieves the starting cave of the labyrinth.
     *
     * @return The starting cave of the labyrinth.
     */
    public Cave getStartingCave() {
        return labyrinth.getStartingCave();
    }

    /**
     * Checks if all creatures in the labyrinth have been defeated.
     *
     * @return true if all creatures have been defeated, false otherwise.
     */
    public boolean allCreaturesKilled() {
        return allCreaturesKilled;
    }

    /**
     * Returns a list of caves that are available to move to from the current cave.
     *
     * @param currentCave The current cave.
     * @return A list of caves available to move to from the current cave.
     */
    public List<Cave> getAvailableCaves(Cave currentCave) {
        List<Cave> availableCaves = new ArrayList<>();
        if (currentCave.getNorth() != null) {
            availableCaves.add(currentCave.getNorth());
        }
        if (currentCave.getEast() != null) {
            availableCaves.add(currentCave.getEast());
        }
        if (currentCave.getSouth() != null) {
            availableCaves.add(currentCave.getSouth());
        }
        if (currentCave.getWest() != null) {
            availableCaves.add(currentCave.getWest());
        }
        return availableCaves;
    }
}
