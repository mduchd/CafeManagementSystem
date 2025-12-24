package com.cafe.dao;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {

    public User login(String userName, String passWord) {
        String sql = "SELECT Username, Role, TenHienThi FROM Taikhoan WHERE Username = ? AND Password = ?";
        User user = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);
            stmt.setString(2, passWord);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserName(rs.getString("Username"));
                    user.setRole(rs.getString("Role"));
                    user.setFullName(rs.getString("TenHienThi"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
