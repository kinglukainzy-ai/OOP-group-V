package org.library.system.model;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private boolean isSuspended;

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.isSuspended = false;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isSuspended() { return isSuspended; }

    public void setSuspended(boolean status) { this.isSuspended = status; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}