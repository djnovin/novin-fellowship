package edu.monash.domain.entities;

/**
 * Represents a cave in the labyrinth.
 * A cave can have connections to neighboring caves (north, east, south, west) and may contain a creature.
 */
public class Cave {
    private final int id;
    private Cave north;
    private Cave east;
    private Cave south;
    private Cave west;
    private Creature creature;

    /**
     * Constructs a Cave with the specified ID.
     *
     * @param id The unique identifier of the cave.
     */
    public Cave(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the cave.
     *
     * @return The ID of the cave.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the cave to the north, or null if there is none.
     *
     * @return The cave to the north, or null if there is none.
     */
    public Cave getNorth() {
        return north;
    }

    /**
     * Sets the cave to the north.
     *
     * @param north The cave to the north.
     */
    public void setNorth(Cave north) {
        this.north = north;
    }

    /**
     * Returns the cave to the east, or null if there is none.
     *
     * @return The cave to the east, or null if there is none.
     */
    public Cave getEast() {
        return east;
    }

    /**
     * Sets the cave to the east.
     *
     * @param east The cave to the east.
     */
    public void setEast(Cave east) {
        this.east = east;
    }

    /**
     * Returns the cave to the south, or null if there is none.
     *
     * @return The cave to the south, or null if there is none.
     */
    public Cave getSouth() {
        return south;
    }

    /**
     * Sets the cave to the south.
     *
     * @param south The cave to the south.
     */
    public void setSouth(Cave south) {
        this.south = south;
    }

    /**
     * Returns the cave to the west, or null if there is none.
     *
     * @return The cave to the west, or null if there is none.
     */
    public Cave getWest() {
        return west;
    }

    /**
     * Sets the cave to the west.
     *
     * @param west The cave to the west.
     */
    public void setWest(Cave west) {
        this.west = west;
    }

    /**
     * Returns the creature currently in the cave, or null if there is none.
     *
     * @return The creature in the cave, or null if there is none.
     */
    public Creature getCreature() {
        return creature;
    }

    /**
     * Sets the creature in the cave.
     *
     * @param creature The creature to place in the cave.
     */
    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    /**
     * Checks if the cave is connected to another cave.
     *
     * @param cave The cave to check the connection with.
     * @return true if the cave is connected to the given cave, false otherwise.
     */
    public boolean isConnectedTo(Cave cave) {
        return north == cave || east == cave || south == cave || west == cave;
    }

    /**
     * Checks if the cave contains a creature.
     *
     * @return true if there is a creature in the cave, false otherwise.
     */
    public boolean hasCreature() {
        return creature != null;
    }
}
