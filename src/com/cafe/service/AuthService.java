
package com.cafe.service;


import com.cafe.dao.AuthDAO;
import com.cafe.model.User;

public class AuthService {
    private final AuthDAO authDAO = new AuthDAO();
    public User login(String userName, String passWord) {
        return authDAO.login(userName, passWord);
    }
}
