package edu.monash.domain.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a labyrinth consisting of interconnected caves.
 * This class is part of the domain layer and contains the core logic for managing the labyrinth's structure.
 */
public class Labyrinth {
    private final Map<Integer, Cave> caves;

    /**
     * Constructs a new Labyrinth.
     * Initializes the collection of caves within the labyrinth.
     */
    public Labyrinth() {
        this.caves = new HashMap<>();
    }

    /**
     * Adds a cave to the labyrinth and connects it to its neighboring caves.
     *
     * @param caveId  The identifier of the cave being added.
     * @param northId The identifier of the cave to the north, or 0 if none.
     * @param eastId  The identifier of the cave to the east, or 0 if none.
     * @param southId The identifier of the cave to the south, or 0 if none.
     * @param westId  The identifier of the cave to the west, or 0 if none.
     */
    public void addCave(int caveId, int northId, int eastId, int southId, int westId) {
        Cave cave = caves.computeIfAbsent(caveId, Cave::new);
        if (northId != 0) {
            cave.setNorth(caves.computeIfAbsent(northId, Cave::new));
        }
        if (eastId != 0) {
            cave.setEast(caves.computeIfAbsent(eastId, Cave::new));
        }
        if (southId != 0) {
            cave.setSouth(caves.computeIfAbsent(southId, Cave::new));
        }
        if (westId != 0) {
            cave.setWest(caves.computeIfAbsent(westId, Cave::new));
        }
    }

    /**
     * Retrieves a cave from the labyrinth by its identifier.
     *
     * @param id The identifier of the cave to retrieve.
     * @return The cave with the specified identifier, or null if not found.
     */
    public Cave getCave(int id) {
        return caves.get(id);
    }

    /**
     * Retrieves the starting cave of the labyrinth.
     *
     * @return The starting cave, assumed to be the cave with ID 1.
     */
    public Cave getStartingCave() {
        return caves.get(1);
    }

    /**
     * Sets the starting cave of the labyrinth.
     *
     * @param cave The cave to set as the starting cave.
     */
    public void setStartingCave(Cave cave) {
        caves.put(1, cave);
    }

    /**
     * Retrieves the entire collection of caves within the labyrinth.
     *
     * @return A map containing all caves, keyed by their identifiers.
     */
    public Map<Integer, Cave> getCaves() {
        return caves;
    }
}
