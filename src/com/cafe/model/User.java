
package com.cafe.model;

public class User {
    private String userName;
    private String passWord; 
    private String role; // admin or employee
    private String fullName;
    
    public User() {
    }
    
    public User(String usernName, String passWord, String role, String fullName) {
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
        this.fullName = fullName;
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
    
    
}
