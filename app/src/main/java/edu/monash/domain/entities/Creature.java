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
        super(name, power, health, health);
    }

    // Additional methods or attributes specific to creatures can be added here.
    public boolean isDead() {
        return getHealth() <= 0; // The creature is dead if health is 0 or less
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        // Check if the creature has 10 or more damage points
        if (getHealth() <= 0) {
            setHealth(0); // Ensure the health is 0 and mark the creature as dead
        }
    }
}
