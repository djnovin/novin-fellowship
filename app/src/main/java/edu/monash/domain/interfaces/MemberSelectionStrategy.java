package edu.monash.domain.interfaces;

import java.util.List;

import edu.monash.domain.entities.Member;

public interface MemberSelectionStrategy {
    Member selectMember(List<Member> members);
}
