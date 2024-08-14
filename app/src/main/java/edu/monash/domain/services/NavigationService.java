package edu.monash.domain.services;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.monash.domain.entities.Cave;
import edu.monash.domain.entities.Fellowship;
import edu.monash.domain.entities.Labyrinth;

public class NavigationService {

    private static final Logger logger = Logger.getLogger(NavigationService.class.getName());
    private final Labyrinth labyrinth;

    public NavigationService(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }

    public Optional<Cave> move(Fellowship fellowship, Cave currentCave, String direction) {
        Optional<Cave> nextCave = getNextCave(currentCave, direction);

        if (nextCave.isPresent()) {
            logger.log(Level.INFO, "Fellowship moved {0} to Cave {1}", new Object[]{direction, nextCave.get().getId()});
            return nextCave;
        } else {
            logger.warning("Invalid move. There's no cave in the " + direction + " direction.");
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

    public void checkForCreature(Fellowship fellowship, Cave currentCave) {
        if (currentCave.getCreature() != null) {
            logger.log(Level.INFO, "A {0} has appeared in Cave {1}!", new Object[]{currentCave.getCreature().getName(), currentCave.getId()});
            // Handle the creature encounter here, e.g., initiate combat
        } else {
            logger.info("No creatures in this cave.");
        }
    }
}
