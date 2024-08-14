package edu.monash.domain.entities;

/**
 * Represents an Elf member of the Fellowship, who has a special weapon.
 * 
 */
public class Elf extends Member {
    private final SpecialWeapon specialWeapon;

    /**
     * Constructs an Elf with the specified name and power.
     *
     * @param name The name of the Elf.
     */
    public Elf(String name) {
        super(name, 5);
        this.specialWeapon = new SpecialWeapon();
    }

    /**
     * Returns true if the Elf has a special weapon, false otherwise.
     *
     * @return true if the Elf has a special weapon, false otherwise.
     */
    @Override
    public boolean hasSpecialWeapon() {
        return !specialWeapon.isUsed();
    }

    /**
     * Uses the special weapon of the Elf.
     *
     */
    @Override
    public void useSpecialWeapon() {
        specialWeapon.use();
    }
}
