package edu.monash;
public class Cave {
    private final int id;
    private Cave north;
    private Cave east;
    private Cave south;
    private Cave west;
    private Creature creature;

    public Cave(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Cave getNorth() {
        return north;
    }

    public void setNorth(Cave north) {
        this.north = north;
    }

    public Cave getEast() {
        return east;
    }

    public void setEast(Cave east) {
        this.east = east;
    }

    public Cave getSouth() {
        return south;
    }

    public void setSouth(Cave south) {
        this.south = south;
    }

    public Cave getWest() {
        return west;
    }

    public void setWest(Cave west) {
        this.west = west;
    }

    public Creature getCreature() {
        return creature;
    }

    public void setCreature(Creature creature) {
        this.creature = creature;
    }
}
