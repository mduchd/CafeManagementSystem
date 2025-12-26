package com.cafe.model;

/**
 * Enum defining user roles in the system
 */
public enum UserRole {
    STAFF("Nhân viên"),      // Employee - limited access (only Sales)
    MANAGER("Quản lý");      // Manager - full access
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Convert string role to UserRole enum
     */
    public static UserRole fromString(String role) {
        if (role == null) return STAFF;
        
        switch (role.toLowerCase()) {
            case "manager":
            case "admin":
            case "quản lý":
                return MANAGER;
            case "staff":
            case "employee":
            case "nhân viên":
            default:
                return STAFF;
        }
    }
}
