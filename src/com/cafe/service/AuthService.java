
package com.cafe.service;


import com.cafe.dao.AuthDAO;
import com.cafe.model.User;

public class AuthService {
    private final AuthDAO authDAO = new AuthDAO();
    
    public User login(String username, String password) {
        if (username == null || username.isBlank()) return null;
        if (password == null || password.isBlank()) return null;
        
        return authDAO.login(username.trim(), password);
    }
    
    
    //  Doi mat khau
    public boolean changePassword(String username, String oldPass, String newPass) {
        if (newPass == null || newPass.length() < 6) return false;
        
        String current = authDAO.getPasswordByUserName(username);
        if (current == null) return false;
        
        if (!current.equals(oldPass)) return false; 
        return authDAO.updatePassword(username, newPass);
    }
}
