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

    public boolean executeCombat(Member member, Creature creature, boolean memberHasCode) {
        if (member.hasSpecialWeapon()) {
            member.useSpecialWeapon();
            creature.takeDamage(creature.getHealth()); // Creature is instantly defeated
            handleCombatOutcome(member, creature, memberHasCode, true);
            return true;
        }

        int powerDifference = member.getPower() - creature.getPower();
        int winProbability = calculateWinProbability(powerDifference);

        boolean memberWon = random.nextInt(100) < winProbability;
        if (memberWon) {
            creature.takeDamage(creature.getHealth()); // Creature is defeated
        } else {
            member.takeDamage(4); // Member takes damage if they lose
        }

        handleCombatOutcome(member, creature, memberHasCode, memberWon);
        return memberWon;
    }

    private void handleCombatOutcome(Character winner, Character loser, boolean memberHadCode, boolean memberWon) {
        if (memberWon && memberHadCode) {
            winner.setHasCode(true);
            loser.setHasCode(false);
        } else if (!memberWon && memberHadCode) {
            loser.setHasCode(true);
            winner.setHasCode(false);
        }

        if (winner instanceof Member) {
            winner.takeDamage(-1); // Winner heals slightly (gains 1 health point)
        }

        if (!loser.isAlive()) {
            loser.takeDamage(loser.getHealth()); // Ensure the loser is marked as dead
        }
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
            return 10; // Assume 10% chance if powerDifference is negative
        }
    }
}
