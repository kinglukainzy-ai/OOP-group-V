package org.library.system.service;

import java.util.List;
import org.library.system.model.Member;
import org.library.system.util.LibraryData;

public class MemberServiceImpl implements MemberService {

    private final LibraryData libraryData;

    public MemberServiceImpl(LibraryData libraryData) {
        this.libraryData = libraryData;
    }

    @Override
    public boolean registerMember(Member member) {
        try {
            libraryData.getLibrary().registerMember(member);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Member getMemberById(String memberId) {
        List<Member> members = libraryData.getLibrary().getMembers();

        for (Member member : members) {
            if (member.getMemberId().equals(memberId)) {
                return member;
            }
        }

        return null;
    }

    @Override
    public void setSuspensionStatus(String memberId, boolean status) {
        Member member = getMemberById(memberId);

        if (member != null) {
            member.setSuspended(status);
        }
    }
}
