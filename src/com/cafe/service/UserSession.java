package com.cafe.service;

import com.cafe.model.User;

/**
 * Simple class to manage user session
 */
public class UserSession {
    private static User currentUser;
    
    public static User getCurrentUser() { 
        return currentUser; 
    }
    
    public static void setCurrentUser(User user) { 
        currentUser = user; 
    }
    
    public static void clear() { 
        currentUser = null; 
    }
    
    public static boolean isManager() {
        if (currentUser == null) return false;
        String role = currentUser.getRole();
        return role != null && (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("manager"));
    }
    
    public static boolean isStaff() {
        return !isManager();
    }
}
