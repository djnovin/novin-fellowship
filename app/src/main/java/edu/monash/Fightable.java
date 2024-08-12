package edu.monash;
public interface Fightable {
    void attack(Fightable opponent);
    int getPower();
    int getHealth();
    void takeDamage(int amount);
}