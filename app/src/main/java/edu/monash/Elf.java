package edu.monash;
public class Elf extends Member implements MagicUser {

    public Elf(String name) {
        super(name, 5, 10, true); // Power = 5, Health = 10, has a special weapon
    }

    @Override
    public void castSpell(Fightable target) {
        target.takeDamage(3);  // Example spell logic
    }

    @Override
    public int getMana() {
        return 10;  // Example mana level
    }
}