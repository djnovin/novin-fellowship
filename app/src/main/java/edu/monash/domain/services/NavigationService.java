package edu.monash.domain.services;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.monash.domain.entities.Cave;
import edu.monash.domain.entities.Creature;
import edu.monash.domain.entities.Fellowship;
import edu.monash.domain.entities.Labyrinth;
import edu.monash.domain.entities.Member;

public class NavigationService {

    private static final Logger logger = Logger.getLogger(NavigationService.class.getName());
    private final CombatService combatService;
    private final Labyrinth labyrinth;

    public NavigationService(Labyrinth labyrinth) {
        this.combatService = new CombatService();
        this.labyrinth = labyrinth;
    }

    /**
     * Returns the starting cave of the labyrinth.
     * 
     * @return the starting cave
     */
    public Cave getStartedInCave() {
        return labyrinth.getStartingCave();
    }

    public Optional<Cave> move(Cave currentCave, String direction) {
        Optional<Cave> nextCave = getNextCave(currentCave, direction);

        if (nextCave.isPresent()) {
            logger.log(Level.INFO, "Fellowship moved {0} to Cave {1}",
                    new Object[] { direction, nextCave.get().getId() });
            return nextCave;
        } else {
            logger.log(Level.WARNING, "Invalid move. There''s no cave in the {0} direction.", direction);
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

        if (creature != null) {
            logger.log(Level.INFO, "A {0} has appeared in Cave {1}!",
                    new Object[] { creature.getName(), currentCave.getId() });

            // Choose a member to fight
            Member chosenMember = selectMemberForFight(fellowship);
            boolean memberHasCode = chosenMember.hasCode();

            // Execute the combat
            boolean memberWon = combatService.executeCombat(chosenMember, creature, memberHasCode);

            if (memberWon) {
                logger.log(Level.INFO, "{0} defeated the {1}!",
                        new Object[] { chosenMember.getName(), creature.getName() });
            } else {
                logger.log(Level.INFO, "{0} was defeated by the {1}.",
                        new Object[] { chosenMember.getName(), creature.getName() });
            }

            return true;
        } else {
            logger.info("No creatures in this cave.");

            return false;
        }
    }

    private Member selectMemberForFight(Fellowship fellowship) {
        // Logic to choose which member will fight, for example, the first member:
        return fellowship.getMembers().get(0); // Example: just return the first member
    }
}
