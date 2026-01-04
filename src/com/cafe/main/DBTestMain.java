package com.cafe.main;

import com.cafe.dao.AuthDAO;
import com.cafe.model.User;

public class DBTestMain {
    public static void main(String[] args) {
        AuthDAO dao = new AuthDAO();
        User u = dao.login("admin", "123");
        System.out.println(u == null ? "FAIL" : "OK" + u.getUserName() + " " + u.getRole());
    }
}
