package edu.monash.domain.entities;

/**
 * Represents a Hobbit member of the Fellowship, who has a special weapon.
 */
public class Hobbit extends Member {
    private static final int HOBBIT_POWER = 3;

    private final SpecialWeapon specialWeapon;

    /**
     * Constructs a Hobbit with the specified name.
     *
     * @param name The name of the Hobbit.
     */
    public Hobbit(String name) {
        super(name, HOBBIT_POWER);
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
