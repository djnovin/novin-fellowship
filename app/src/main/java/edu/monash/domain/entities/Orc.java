package edu.monash.domain.entities;

/**
 * Represents an Orc creature in the game.
 */
public class Orc extends Creature {

    /**
     * Constructs an Orc with predefined power and health.
     */
    public Orc() {
        super("Orc", 5, 10); // Example power and health values
    }

    @Override
    public boolean attack(Character target) {
        if (target instanceof Creature creature) {
            return getPower() > creature.getPower();
        }
        return false;
    }
}