package edu.monash.domain.entities;

/**
 * Represents a Dwarf member of the Fellowship, who has greater power but no
 * special weapon.
 */
public class Dwarf extends Member {

    /**
     * Constructs a Dwarf with the specified name.
     *
     * @param name The name of the Dwarf.
     */
    public Dwarf(String name) {
        super(name, 7);
    }

    /**
     * Returns true if the Dwarf has a special weapon, false otherwise.
     *
     * @return true if the Dwarf has a special weapon, false otherwise.
     */
    @Override
    public boolean hasSpecialWeapon() {
        return false; // Dwarves do not have special weapons
    }

    /**
     * Uses the special weapon of the Dwarf.
     *
     * @throws UnsupportedOperationException if the Dwarf does not have a special
     *                                       weapon.
     */
    @Override
    public void useSpecialWeapon() {
        throw new UnsupportedOperationException("Dwarves do not have special weapons.");
    }
}
