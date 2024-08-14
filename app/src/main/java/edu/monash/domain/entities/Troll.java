package edu.monash.domain.entities;

/**
 * Represents a Troll creature in the game.
 */
public class Troll extends Creature {

    /**
     * Constructs a Troll with predefined power and health.
     */
    public Troll() {
        super("Troll", 9, 15); // Example power and health values
    }

    @Override
    public boolean attack(Character target) {
        if (target instanceof Creature creature) {
            return getPower() > creature.getPower();
        }
        return false;
    }
}