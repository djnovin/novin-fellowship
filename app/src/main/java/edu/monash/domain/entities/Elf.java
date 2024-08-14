package edu.monash.domain.entities;

/**
 * Represents an Elf member of the Fellowship, who has a special weapon.
 */
public class Elf extends Member {
    private final SpecialWeapon specialWeapon;

    public Elf(String name) {
        super(name, 5, 10, 10);
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

                // Check if the creature was defeated after taking damage
                if (!creature.isAlive()) {
                    return true; // Return true if the creature was defeated
                }
            }
        }
        return false; // Return false if the creature was not defeated
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
