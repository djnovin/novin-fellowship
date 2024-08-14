package edu.monash.domain.entities;

/**
 * Represents a generic creature in the game.
 * This is the base class for all enemy types that the Fellowship encounters.
 */
public abstract class Creature extends Character {

    /**
     * Constructs a Creature with the specified name, power, and health.
     *
     * @param name   The name of the creature.
     * @param power  The power level of the creature.
     * @param health The health points of the creature.
     */
    protected Creature(String name, int power, int health) {
        super(name, power, health);
    }

    // Additional methods or attributes specific to creatures can be added here.
}
