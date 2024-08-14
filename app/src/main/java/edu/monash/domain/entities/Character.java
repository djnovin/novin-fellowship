package edu.monash.domain.entities;

/**
 * Represents a generic character in the game.
 * This is the base class for all characters, including both members of the
 * Fellowship and creatures.
 */
public abstract class Character {
    private final String name;
    private final int power;
    private int points;
    private boolean hasCode;
    private boolean isDead;

    /**
     * Constructs a Character with the specified name, power, and health.
     *
     * @param name    The name of the character.
     * @param power   The power level of the character.
     * @param points  The points of the character.
     * @param hasCode The hasCode of the character.
     * @param isDead  The isDead of the character.
     */
    protected Character(String name, int power) {
        this.name = name;
        this.power = power;
        this.points = 0;
        this.hasCode = false;
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

    public boolean getHasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }

    public int getDamagePoints() {
        return points;
    }

    public void setDamagePoints(int points) {
        this.points = points;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }
}
