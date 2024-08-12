package edu.monash;
import java.util.ArrayList;
import java.util.List;

public class Fellowship {
    private final List<Member> members;
    private boolean hasCode;

    public Fellowship() {
        this.members = new ArrayList<>();
        this.hasCode = true;  // Initially, the Fellowship has the secret code.
    }

    public void addMember(Member member) {
        if (members.size() < 4) {
            members.add(member);
        } else {
            System.out.println("The Fellowship can only have up to 4 members.");
        }
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    public List<Member> getMembers() {
        return members;
    }

    public boolean hasCode() {
        return hasCode;
    }

    public void setHasCode(boolean hasCode) {
        this.hasCode = hasCode;
    }

    public void recover() {
        for (Member member : members) {
            member.takeDamage(-1);  // Assuming each member has a method to reduce damage points.
            System.out.println(member.getName() + " has recovered 1 health point.");
        }
    }

    public void showMembersStatus() {
        for (Member member : members) {
            System.out.println(member.getName() + " - Power: " + member.getPower() + ", Health: " + member.getHealth());
        }
    }
}