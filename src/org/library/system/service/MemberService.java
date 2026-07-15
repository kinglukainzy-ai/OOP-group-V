package org.library.system.service;

import org.library.system.model.Member;

public interface MemberService {

    boolean registerMember(Member member);

    Member getMemberById(String memberId);

    void setSuspensionStatus(String memberId, boolean status);
}
