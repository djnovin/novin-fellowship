package edu.monash.domain.entities;

/**
 * Represents a generic character in the game.
 * This is the base class for all characters, including both members of the
 * Fellowship and creatures.
 */
public abstract class Character {
    private final String name;
    private final int power;
    private final int maxHealth; // Maximum health the character can have
    private int health;
    private boolean hasCode;

    /**
     * Constructs a Character with the specified name, power, and health.
     *
     * @param name      The name of the character.
     * @param power     The power level of the character.
     * @param health    The initial health points of the character.
     * @param maxHealth The maximum health points of the character.
     */
    protected Character(String name, int power, int health, int maxHealth) {
        this.name = name;
        this.power = power;
        this.health = health;
        this.maxHealth = maxHealth;
        this.hasCode = false;
    }

    /**
     * Returns the name of the character.
     *
     * @return The name of the character.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the power level of the character.
     *
     * @return The power level of the character.
     */
    public int getPower() {
        return power;
    }

    /**
     * Returns the health points of the character.
     *
     * @return The health points of the character.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the maximum health points of the character.
     *
     * @return The maximum health points of the character.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the health points of the character.
     *
     * @param health The health points to set.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Reduces the character's health by the specified damage.
     *
     * @param damage The amount of damage to inflict on the character.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (damage < 0 && this.health > this.maxHealth) {
            this.health = this.maxHealth; // Ensure health does not exceed maximum
        } else if (this.health < 0) {
            this.health = 0; // Ensure health does not drop below zero
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean hasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }

    /**
     * Abstract method to be implemented by subclasses, representing an attack
     * action.
     *
     * @param target The character being attacked.
     * @return True if the attack was successful, false otherwise.
     */
    public abstract boolean attack(Character target);
}
