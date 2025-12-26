package com.cafe.model;

public class Account {

    private int id;
    private int employeeId;
    private String username;
    private String password;
    private String role;
    private String displayName;

    public Account() {
    }

    // Constructor for full account details
    public Account(int id, int employeeId, String username, String password, String role, String displayName) {
        this.id = id;
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = displayName;
    }

    // Constructor for creating a new account   
    public Account(int employeeId, String username, String password, String role, String displayName) {
        this(0, employeeId, username, password, role, displayName);
    }

    // Getters and Setters  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
 
    // toString method for easy debugging   
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
