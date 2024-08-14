package edu.monash.domain.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * The Fellowship class represents the group of members on the quest.
 * It manages the members, checks their status, and selects members for actions.
 */
public class Fellowship {

    private final List<Member> members;

    /**
     * Constructs an empty Fellowship.
     */
    public Fellowship() {
        this.members = new ArrayList<>();
    }

    /**
     * Adds a member to the Fellowship.
     * 
     * @param member The member to be added.
     * @return true if the member was added, false if the Fellowship is full.
     */
    public boolean addMember(Member member) {
        if (members.size() < 4) {
            members.add(member);
            return true;
        } else {
            System.out.println("The Fellowship can only have up to 4 members.");
            return false;
        }
    }

    /**
     * Removes a member from the Fellowship.
     * 
     * @param member The member to be removed.
     */
    public void removeMember(Member member) {
        members.remove(member);
    }

    /**
     * Gets the list of members in the Fellowship.
     * 
     * @return A list of members.
     */
    public List<Member> getMembers() {
        return new ArrayList<>(members); // Return a copy to avoid external modification
    }

    /**
     * Checks if the Fellowship is still alive.
     * 
     * @return true if at least one member is alive, false otherwise.
     */
    public boolean isFellowshipAlive() {
        return members.stream().anyMatch(member -> !member.getIsDead());
    }

    /**
     * Selects a member for a fight.
     * 
     * @param memberName The name of the member to be selected.
     * @return The selected member if found, otherwise null.
     */
    public Member selectMemberForFight(String memberName) {
        return members.stream()
                .filter(member -> member.getName().equalsIgnoreCase(memberName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Recovers damage points for all members in the Fellowship when no creatures
     * are present.
     */
    public void recoverDamagePoints() {
        members.forEach(Member::recoverPoints);
    }

    /**
     * Displays the current status of the Fellowship.
     */
    public void displayStatus() {
        System.out.println("Fellowship status:");
        members.forEach(member -> System.out.println(member.getName() + " - Power: " + member.getPower() +
                ", Damage Points: " + member.getDamagePoints() + ", Has Code: " + member.getHasCode()));
    }
}
