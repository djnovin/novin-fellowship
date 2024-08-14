package edu.monash.domain.entities;

/**
 * Represents a Goblin creature in the game.
 */
public class Goblin extends Creature {

    /**
     * Constructs a Goblin with predefined power and health.
     */
    public Goblin() {
        super("Goblin", 3, 7); // Example power and health values
    }

    @Override
    public boolean attack(Character target) {
        if (target instanceof Creature creature) {
            return getPower() > creature.getPower();
        }
        return false;
    }
}