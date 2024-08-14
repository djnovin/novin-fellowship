package edu.monash.domain.entities;

/**
 * Represents a generic creature in the game.
 * This is the base class for all enemy types that the Fellowship encounters.
 * 
 */
public abstract class Creature extends Character {

    /**
     * Constructs a Creature with the specified name and power.
     *
     * @param name  The name of the creature.
     * @param power The power level of the creature.
     */
    protected Creature(String name, int power) {
        super(name, power);
    }

    /**
     * Reset points when a new cave is entered.
     */
    public void resetPoints() {
        setDamagePoints(0);
    }
}
