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
            // Member uses special weapon, creature is instantly killed
            member.useSpecialWeapon();
            creature.setDamagePoints(10); // Creature dies
            handleCombatOutcome(member, creature, memberHasCode, true);
            return true;
        }

        int powerDifference = member.getPower() - creature.getPower();
        int winProbability = calculateWinProbability(powerDifference);

        boolean memberWon = random.nextInt(100) < winProbability;

        if (memberWon) {
            // Member wins, creature accumulates damage points
            creature.setDamagePoints(creature.getDamagePoints() + 10); // Creature is defeated
        } else {
            // Creature wins, member accumulates damage points
            member.setDamagePoints(member.getDamagePoints() + 4);
        }

        handleCombatOutcome(member, creature, memberHasCode, memberWon);
        return memberWon;
    }

    private void handleCombatOutcome(Character winner, Character loser, boolean memberHadCode, boolean memberWon) {
        if (memberWon && memberHadCode) {
            // Member retains the code
            winner.setHasCode(true);
            loser.setHasCode(false);
        } else if (!memberWon && memberHadCode) {
            // Creature steals the code from the member
            loser.setHasCode(true);
            winner.setHasCode(false);
        }

        if (winner instanceof Member) {
            // Winning member recovers slightly
            winner.setDamagePoints(Math.max(winner.getDamagePoints() - 1, 0)); // Ensure no negative damage points
        }

        // If the loser has 10 or more damage points, they die
        if (loser.getDamagePoints() >= 10) {
            loser.setIsDead(true);
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
