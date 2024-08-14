package edu.monash.domain.entities;

/**
 * Represents a Dwarf member of the Fellowship, who has greater power but no special weapon.
 */
public class Dwarf extends Member {

    public Dwarf(String name) {
        super(name, 7, 12); // Power = 7, Health = 12
    }

    @Override
    public boolean attack(Character target) {
        if (target instanceof Creature creature) {
            return getPower() > creature.getPower();
        }
        return false;
    }

    @Override
    public boolean hasSpecialWeapon() {
        return false; // Dwarves do not have special weapons
    }

    @Override
    public void useSpecialWeapon() {
        // Dwarves do not have special weapons, so this can be left empty or throw an UnsupportedOperationException
        throw new UnsupportedOperationException("Dwarves do not have special weapons.");
    }
}
