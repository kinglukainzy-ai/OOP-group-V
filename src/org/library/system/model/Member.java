package org.library.system.model;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private boolean isSuspended;

    // 3-Argument Constructor (For registering a brand new member)
    public Member(String memberId, String name, String email) {
        validateInput(memberId, "Member ID cannot be null or empty.");
        validateInput(name, "Name cannot be null or empty.");
        validateInput(email, "Email cannot be null or empty.");
        
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.isSuspended = false; 
    }

    // 4-Argument Constructor (Required by FileIOHelper to load CSV data)
    public Member(String memberId, String name, String email, boolean isSuspended) {
        validateInput(memberId, "Member ID cannot be null or empty.");
        validateInput(name, "Name cannot be null or empty.");
        validateInput(email, "Email cannot be null or empty.");
        
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.isSuspended = isSuspended;
    }

    // Input Validation Helper Method
    private void validateInput(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // Getters and Setters with Validation
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { 
        validateInput(memberId, "Member ID cannot be null or empty.");
        this.memberId = memberId; 
    }

    public String getName() { return name; }
    public void setName(String name) { 
        validateInput(name, "Name cannot be null or empty.");
        this.name = name; 
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { 
        validateInput(email, "Email cannot be null or empty.");
        this.email = email; 
    }

    public boolean isSuspended() { return isSuspended; }
    public void setSuspensionStatus(boolean isSuspended) { this.isSuspended = isSuspended; }

    // Restored toString() Method
    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isSuspended=" + isSuspended +
                '}';
    }
}