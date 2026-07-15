package org.library.system.model;

/**
 * Class representing a library member.
 * Demonstrates basic encapsulation.
 */
public class Member {
    private final String memberId;
    private final String name;
    private final String email;
    private boolean isSuspended;

    public Member(String memberId, String name, String email) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        this.memberId = memberId.trim();
        this.name = name.trim();
        this.email = email.trim();
        this.isSuspended = false;
    }

    public Member(String memberId, String name, String email, boolean isSuspended) {
        this(memberId, name, email);
        this.isSuspended = isSuspended;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        this.isSuspended = suspended;
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s)%s", name, memberId, isSuspended ? " [SUSPENDED]" : "");
    }
}
