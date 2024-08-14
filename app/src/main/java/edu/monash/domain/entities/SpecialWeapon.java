package edu.monash.domain.entities;

/**
 * Represents a special weapon that can be used once by a member of the Fellowship.
 * When used, this weapon will guarantee a win in combat.
 */
public class SpecialWeapon {
    private boolean used;

    /**
     * Constructs a new SpecialWeapon that has not been used yet.
     */
    public SpecialWeapon() {
        this.used = false;
    }

    /**
     * Returns whether the special weapon has been used.
     *
     * @return True if the weapon has been used, false otherwise.
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Uses the special weapon, marking it as used.
     * This method should only be called if the weapon has not been used yet.
     */
    public void use() {
        if (!used) {
            used = true;
        } else {
            throw new IllegalStateException("Special weapon has already been used.");
        }
    }
}
