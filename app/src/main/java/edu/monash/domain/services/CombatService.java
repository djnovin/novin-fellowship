package edu.monash.domain.services;

import java.util.Random;

import edu.monash.domain.entities.Character;
import edu.monash.domain.entities.Creature;
import edu.monash.domain.entities.Member;

public class CombatService {
    private final Random random;
    private int totalFights = 0;
    private int fightsWon = 0;

    /**
     * Constructs a CombatService.
     */
    public CombatService() {
        this.random = new Random();
    }

    /**
     * Returns the number of fights participated by the member.
     * 
     * @return
     */
    public int getTotalFights() {
        return totalFights;
    }

    /**
     * Returns the number of fights won by the member.
     * 
     * @return
     */
    public int getFightsWon() {
        return fightsWon;
    }

    /**
     * Executes a combat between a member and a creature.
     * 
     * @param member
     * @param creature
     * @param memberHasCode
     * @return
     */
    public boolean executeCombat(Member member, Creature creature, boolean memberHasCode) {

        totalFights++;

        if (member.hasSpecialWeapon()) {
            member.useSpecialWeapon();
            creature.setDamagePoints(10);
            handleCombatOutcome(member, creature, memberHasCode, true);
            return true;
        }

        int powerDifference = member.getPower() - creature.getPower();
        int winProbability = calculateWinProbability(powerDifference);

        boolean memberWon = random.nextInt(100) < winProbability;

        if (memberWon) {
            fightsWon++;
        }

        if (memberWon) {
            creature.setDamagePoints(creature.getDamagePoints() + 10);
        } else {
            member.setDamagePoints(member.getDamagePoints() + 4);
        }

        handleCombatOutcome(member, creature, memberHasCode, memberWon);

        return memberWon;
    }

    /**
     * Handles the outcome of the combat.
     * 
     * @param winner
     * @param loser
     * @param memberHadCode
     * @param memberWon
     */
    private void handleCombatOutcome(Character winner, Character loser, boolean memberHadCode, boolean memberWon) {
        if (memberWon && memberHadCode) {
            winner.setHasCode(true);
            loser.setHasCode(false);
        } else if (!memberWon && memberHadCode) {
            loser.setHasCode(true);
            winner.setHasCode(false);
        }

        if (winner instanceof Member) {
            winner.setDamagePoints(Math.max(winner.getDamagePoints() - 1, 0));
        }

        if (loser.getDamagePoints() >= 10) {
            loser.setIsDead(true);
        }
    }

    /**
     * Calculates the win probability based on the power difference between the
     * member and the creature.
     * 
     * @param powerDifference
     * @return
     */
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
            return 10;
        }
    }
}
