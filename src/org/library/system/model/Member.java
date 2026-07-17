package org.library.system.model;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private boolean isSuspended;

    // Constructor
    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.isSuspended = false; // Default to not suspended upon registration
    }

    // Getters and Setters
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getName() { return name; }
    public void setName() { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isSuspended() { return isSuspended; }
    public void setSuspensionStatus(boolean isSuspended) { this.isSuspended = isSuspended; }
}