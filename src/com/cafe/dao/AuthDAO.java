package com.cafe.dao;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Nên bắt SQLException thay vì Exception chung chung

public class AuthDAO {

    public User login(String username, String password) {
        String sql = "SELECT Username, Role, TenHienThi FROM Taikhoan WHERE Username = ? AND PASSWORD = ?";
        
        // SỬA LỖI 1: Đưa cả conn và stmt vào trong ngoặc tròn của try
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUsername(rs.getString("Username"));
                    u.setRole(rs.getString("Role")); // Đảm bảo cột Role trong DB khớp tên
                    u.setFullname(rs.getString("TenHienThi"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getPasswordByUserName(String username) {
        String sql = "SELECT Password FROM Taikhoan WHERE Username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("Password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    

    // Update mật khẩu
    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE Taikhoan SET Password = ? WHERE Username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}