package edu.monash;

public abstract class Member extends Character {
    private boolean hasSpecialWeapon;

    protected Member(String name, int power, int health, boolean hasSpecialWeapon) {
        super(name, power, health);
        this.hasSpecialWeapon = hasSpecialWeapon;
    }

    public boolean hasSpecialWeapon() {
        return hasSpecialWeapon;
    }

    public void useSpecialWeapon() {
        hasSpecialWeapon = false;
    }
}