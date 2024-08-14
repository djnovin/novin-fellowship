package edu.monash.domain.services;

import java.util.Random;

import edu.monash.domain.entities.Character;
import edu.monash.domain.entities.Creature;
import edu.monash.domain.entities.Member;

public class CombatService {

    private final Random random;

    public CombatService() {
        this.random = new Random();
    }

    public boolean executeCombat(Member member, Creature creature, boolean hasCode) {
        if (member.hasSpecialWeapon()) {
            member.useSpecialWeapon();
            creature.takeDamage(creature.getHealth()); // Creature is instantly defeated
            return resolveFightOutcome(member, creature, hasCode, true);
        }

        int powerDifference = member.getPower() - creature.getPower();
        int winProbability = calculateWinProbability(powerDifference);

        if (random.nextInt(100) < winProbability) {
            creature.takeDamage(creature.getHealth()); // Creature is defeated
            return resolveFightOutcome(member, creature, hasCode, true);
        } else {
            member.takeDamage(4); // Member takes damage if they lose
            return resolveFightOutcome(member, creature, hasCode, false);
        }
    }

    private boolean resolveFightOutcome(Character winner, Character loser, boolean hadCode, boolean memberWon) {
        winner.takeDamage(-1); // Winner heals slightly

        if (loser.isAlive()) {
            loser.takeDamage(4); // Loser takes additional damage
        } else {
            loser.takeDamage(loser.getHealth()); // Ensure they are dead
        }

        // Handle code transfer
        if (hadCode) {
            winner.setHasCode(true);
            loser.setHasCode(false);
        }

        return memberWon;
    }

    private int calculateWinProbability(int powerDifference) {
        if (powerDifference >= 4) {
            return 90;
        } else if (powerDifference >= 3) {
            return 80;
        } else if (powerDifference >= 2) {
            return 70;
        } else if (powerDifference >= 1) {
            return 60;
        } else if (powerDifference == 0) {
            return 50;
        } else {
            return 0;
        }
    }
}
