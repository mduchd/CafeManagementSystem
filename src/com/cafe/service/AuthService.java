/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.service;

import com.cafe.dao.AuthDAO;
import com.cafe.model.User;

/**
 *
 * @author Owner
 */
public class AuthService {
    private AuthDAO  authDAO = new AuthDAO();
    public User login (String username, String password) {
        // Ktra rong
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui long nhap ten dang nhap!");
        }
        if (password == null || password.trim().isEmpty()) {
                        throw new IllegalArgumentException("Vui long nhap mat khau!");
        }
        
        // Goi DAO de kiem tra trong database
        User user = authDAO.login(username, password);
        
        // ktra ket qua 
        if (user == null) {
            throw new IllegalArgumentException("Sai ten dang nhap hoac mat khau!");
        }
        return user;
    }
    
}
