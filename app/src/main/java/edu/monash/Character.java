package edu.monash;
public abstract class Character implements Fightable {
    protected String name;
    protected int power;
    protected int health;

    protected Character(String name, int power, int health) {
        this.name = name;
        this.power = power;
        this.health = health;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void takeDamage(int amount) {
        this.health = Math.max(0, this.health - amount);
    }

    @Override
    public void attack(Fightable opponent) {
        if (this.power > opponent.getPower()) {
            opponent.takeDamage(2);  // Example logic: if stronger, deal more damage
        } else {
            this.takeDamage(2);  // Otherwise, take damage
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public String getName() {
        return name;
    }
}