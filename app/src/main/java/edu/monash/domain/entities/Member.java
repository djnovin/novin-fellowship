package edu.monash.domain.entities;

/**
 * Represents a member of the Fellowship in the game.
 * This is the base class for all member types, such as Hobbit, Elf, and Dwarf.
 */
public abstract class Member extends Character {

    /**
     * Constructs a Member with the specified name, power, and health.
     *
     * @param name      The name of the member.
     * @param power     The power level of the member.
     * @param health    The health points of the member.
     * @param maxHealth The maximum health points of the member.
     */
    protected Member(String name, int power, int health, int maxHealth) {
        super(name, power, health, maxHealth);
    }

    /**
     * Abstract method to perform an attack on a target.
     * Each subclass of Member will implement its own attack behavior.
     *
     * @param target The character being attacked (could be another member or
     *               creature).
     * @return True if the attack was successful, false otherwise.
     */
    @Override
    public abstract boolean attack(Character target);

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
}
