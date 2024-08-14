package edu.monash.domain.entities;

/**
 * Represents a member of the Fellowship in the game.
 * This is the base class for all member types, such as Hobbit, Elf, and Dwarf.
 */
public abstract class Member extends Character {
    /**
     * Constructs a Member with the specified name, power, and health.
     *
     * @param name  The name of the member.
     * @param power The power level of the member.
     */
    protected Member(String name, int power) {
        super(name, power);
    }

    /**
     * Abstract method to check if the member has a special weapon.
     * Subclasses that have special weapons will implement this method.
     *
     * @return True if the member has a special weapon, false otherwise.
     */
    public abstract boolean hasSpecialWeapon();

    /**
     * Abstract method to use the special weapon.
     * Subclasses that have special weapons will implement this method.
     */
    public abstract void useSpecialWeapon();

    /**
     * Recover points when no aggressive creatures are present.
     */
    public void recoverPoints() {
        if (getDamagePoints() > 0) {
            int damagePoints = getDamagePoints();
            damagePoints -= 1;
            setDamagePoints(damagePoints);
        }
    }
}
