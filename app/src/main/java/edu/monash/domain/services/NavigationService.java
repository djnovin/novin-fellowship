package edu.monash.domain.services;

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

public class NavigationService {
    private static final Logger logger = Logger.getLogger(NavigationService.class.getName());
    private final CombatService combatService;
    private final Random random;
    private final Labyrinth labyrinth;

    public NavigationService(Labyrinth labyrinth) {
        this.combatService = new CombatService();
        this.random = new Random();
        this.labyrinth = labyrinth;
    }

    public Optional<Cave> move(Cave currentCave, String direction) {
        Optional<Cave> nextCave = getNextCave(currentCave, direction);

        if (nextCave.isPresent()) {
            logger.log(Level.INFO, "Fellowship moved {0} to Cave {1}",
                    new Object[] { direction, nextCave.get().getId() });
            return nextCave;
        } else {
            logger.log(Level.WARNING, "Invalid move. There is no cave in the {0} direction.", direction);
            return Optional.empty();
        }
    }

    private Optional<Cave> getNextCave(Cave currentCave, String direction) {
        return switch (direction.toLowerCase()) {
            case "north" -> Optional.ofNullable(currentCave.getNorth());
            case "east" -> Optional.ofNullable(currentCave.getEast());
            case "south" -> Optional.ofNullable(currentCave.getSouth());
            case "west" -> Optional.ofNullable(currentCave.getWest());
            default -> Optional.empty();
        };
    }

    public boolean checkForCreature(Fellowship fellowship, Cave currentCave) {
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
            boolean memberHasCode = chosenMember.hasCode();
            boolean memberWon = combatService.executeCombat(chosenMember, creature, memberHasCode);

            if (memberWon) {
                logger.log(Level.INFO, "{0} defeated the {1}!",
                        new Object[] { chosenMember.getName(), creature.getName() });
            } else {
                logger.log(Level.INFO, "{0} was defeated by the {1}.",
                        new Object[] { chosenMember.getName(), creature.getName() });
            }
            return true; // A creature encounter occurred
        } else {
            logger.info("No creatures in this cave.");

            // Fellowship members recover if no creatures are present
            fellowship.getMembers().forEach(member -> {
                if (member.getHealth() > 0) {
                    member.takeDamage(-1); // Decrease damage, effectively healing the member
                }
            });

            return false; // No creature encounter
        }
    }

    public boolean handleCreatureEncounter(Fellowship fellowship, Cave currentCave) {
        Creature creature = currentCave.getCreature();
        if (creature != null) {
            // Handle the creature encounter, initiate combat
            Member chosenMember = selectMemberForFight(fellowship);
            boolean memberHasCode = chosenMember.hasCode();
            boolean memberWon = combatService.executeCombat(chosenMember, creature, memberHasCode);

            if (memberWon) {
                logger.log(Level.INFO, "{0} defeated the {1}!",
                        new Object[] { chosenMember.getName(), creature.getName() });
            } else {
                logger.log(Level.INFO, "{0} was defeated by the {1}.",
                        new Object[] { chosenMember.getName(), creature.getName() });
            }
            return memberWon;
        }
        return false; // No encounter to handle
    }

    private Creature createRandomCreature() {
        // Create a random creature (Orc, Troll, or Goblin)
        int choice = random.nextInt(3);
        return switch (choice) {
            case 0 -> new Orc();
            case 1 -> new Troll();
            case 2 -> new Goblin();
            default -> null;
        };
    }

    private Member selectMemberForFight(Fellowship fellowship) {
        // Logic to choose which member will fight, e.g., the first member:
        return fellowship.getMembers().get(0); // Example: just return the first member
    }

    public Cave getStartingCave() {
        return labyrinth.getStartingCave();
    }
}
