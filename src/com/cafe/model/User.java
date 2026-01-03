package com.cafe.model;

public class User {
    private int id;
    private int employeeId;
    private String userName;
    private String passWord;
    private String role; // admin hoặc employee
    private String fullName;

    public User() {
    }

    // Constructor cho login/session (không cần id, employeeId)
    public User(String userName, String passWord, String role, String fullName) {
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
        this.fullName = fullName;
    }

    // Constructor đầy đủ (từ database)
    public User(int id, int employeeId, String userName, String passWord, String role, String fullName) {
        this.id = id;
        this.employeeId = employeeId;
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
        this.fullName = fullName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String password) {
        this.passWord = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
