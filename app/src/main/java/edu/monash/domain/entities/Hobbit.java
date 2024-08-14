package edu.monash.domain.entities;

/**
 * Represents a Hobbit member of the Fellowship, who has a special weapon.
 */
public class Hobbit extends Member {
    private static final int HOBBIT_POWER = 3;
    private static final int HOBBIT_HEALTH = 10;
    private static final int HOBBIT_MAX_HEALTH = 10;

    private final SpecialWeapon specialWeapon;

    public Hobbit(String name) {
        super(name, HOBBIT_POWER, HOBBIT_HEALTH, HOBBIT_MAX_HEALTH);
        this.specialWeapon = new SpecialWeapon();
    }

    @Override
    public boolean attack(Character target) {
        if (target instanceof Creature creature) {
            if (!specialWeapon.isUsed()) {
                specialWeapon.use();
                creature.takeDamage(creature.getHealth()); // Always wins the fight
                return true;
            } else {

                int damage = getPower();

                creature.takeDamage(damage);
            }
        }
        return false;
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
