package edu.monash.domain.entities;

/**
 * Represents an Elf member of the Fellowship, who has a special weapon.
 */
public class Elf extends Member {
    private final SpecialWeapon specialWeapon;

    public Elf(String name) {
        super(name, 5);
        this.specialWeapon = new SpecialWeapon();
    }

    @Override
    public boolean hasSpecialWeapon() {
        return !specialWeapon.isUsed();
    }

    @Override
    public void useSpecialWeapon() {
        specialWeapon.use();
    }
}
