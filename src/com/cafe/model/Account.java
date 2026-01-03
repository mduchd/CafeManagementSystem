package com.cafe.model;

/**
 * Model class for Account (TaiKhoan table)
 * Represents a user account in the system
 */
public class Account {
    private String username;
    private String password;
    private String role; // 'Admin' or 'NhanVien'
    private String displayName; // TenHienThi

    // Default constructor
    public Account() {
    }

    // Full constructor with validation
    public Account(String username, String password, String role, String displayName) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (!role.equalsIgnoreCase("Admin") && !role.equalsIgnoreCase("NhanVien")) {
            throw new IllegalArgumentException("Role must be 'Admin' or 'NhanVien'");
        }
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = displayName;
    }

    // Getters and Setters
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

    // Helper method to check if account is admin
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
