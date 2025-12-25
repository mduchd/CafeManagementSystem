package com.cafe.service;

import com.cafe.model.User;
import com.cafe.model.UserRole;

/**
 * Singleton class to manage user session
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;
    
    private UserSession() {
        // Private constructor for singleton
    }
    
    /**
     * Get singleton instance
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    /**
     * Login user and start session
     */
    public void login(User user) {
        this.currentUser = user;
    }
    
    /**
     * Logout and clear session
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Get current user's role
     */
    public UserRole getCurrentRole() {
        if (currentUser == null) {
            return UserRole.STAFF; // Default to STAFF if not logged in
        }
        return UserRole.fromString(currentUser.getRole());
    }
    
    /**
     * Check if current user is a manager
     */
    public boolean isManager() {
        return getCurrentRole() == UserRole.MANAGER;
    }
    
    /**
     * Check if current user is staff
     */
    public boolean isStaff() {
        return getCurrentRole() == UserRole.STAFF;
    }
    
    /**
     * Get current user's full name
     */
    public String getCurrentUserName() {
        if (currentUser == null) {
            return "Guest";
        }
        return currentUser.getFullName() != null ? currentUser.getFullName() : currentUser.getUserName();
    }
}
