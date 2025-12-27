package com.cafe.service;

import com.cafe.model.User;

/**
 * Singleton class to manage user session
 */
public class UserSession {
    private static User currentUser;
    public static User getCurrentUser() { return currentUser; }
    public static void setCurrentUser(User u) { currentUser = u; }
    public static void clear() { currentUser = null; }
    }    

    
