package com.cafe.service;

import com.cafe.model.User;

/**
 * Simple class to manage user session
 */
public class UserSession {
    private static User currentUser;
    private static final UserSession INSTANCE = new UserSession();

    // Singleton accessor for compatibility
    public static UserSession getInstance() {
        return INSTANCE;
    }

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
        if (currentUser == null)
            return false;
        String role = currentUser.getRole();
        return role != null && (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("manager"));
    }

    public static boolean isStaff() {
        return !isManager();
    }

    // Instance methods for MainFrame compatibility
    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getUserName() : "Guest";
    }

    public UserRole getCurrentRole() {
        if (currentUser == null)
            return UserRole.STAFF;
        String role = currentUser.getRole();
        if (role != null && (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("manager"))) {
            return UserRole.MANAGER;
        }
        return UserRole.STAFF;
    }

    public void logout() {
        clear();
    }

    // Role enum for display
    public enum UserRole {
        MANAGER("Quản lý"),
        STAFF("Nhân viên");

        private final String displayName;

        UserRole(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
