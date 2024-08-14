package edu.monash.domain.entities;

/**
 * Represents an abstract character in the game.
 * 
 * This class provides a blueprint for creating different types of characters in
 * the game.
 * It contains information about the character's name, power level, points,
 * hasCode status, and life status.
 * 
 * The name and power level are set during character creation and cannot be
 * changed afterwards.
 * The points, hasCode status, and life status can be modified during the game.
 * 
 * This class is intended to be extended by concrete character classes that
 * define specific behavior and abilities.
 */
public abstract class Character {
    private final String name;
    private final int power;
    private int points;
    private boolean hasCode;
    private boolean isDead;

    /**
     * Constructs a Character with the specified name and power.
     *
     * @param name  The name of the character.
     * @param power The power level of the character.
     */
    protected Character(String name, int power) {
        this.name = name;
        this.power = power;
        this.points = 0;
        this.hasCode = true;
        this.isDead = false;
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
     * Checks if the character has the code.
     * 
     * @return true if the character has the code, false otherwise.
     */
    public boolean getHasCode() {
        return hasCode;
    }

    /**
     * Sets the code status of the character.
     * 
     * @param hasCode true if the character has the code, false otherwise.
     */
    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }

    /**
     * Returns the number of points accumulated by the character.
     *
     * @return The number of points accumulated by the character.
     */
    public int getDamagePoints() {
        return points;
    }

    /**
     * Sets the number of points accumulated by the character.
     *
     * @param points The number of points to set.
     */
    public void setDamagePoints(int points) {
        this.points = points;
    }

    /**
     * Checks if the character is dead.
     * 
     * @return true if the character is dead, false otherwise.
     */
    public boolean getIsDead() {
        return isDead;
    }

    /**
     * Sets the life status of the character.
     * 
     * @param isDead true if the character is dead, false otherwise.
     */
    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }
}
