package com.cafe.dao;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account (TaiKhoan table)
 * Handles CRUD operations for user accounts
 */
public class AccountDAO {

    // Get all accounts
    public List<Account> getAll() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT Username, Password, Role, TenHienThi FROM TaiKhoan ORDER BY Username";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Account acc = mapResultSetToAccount(rs);
                list.add(acc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get account by username
    public Account getByUsername(String username) {
        String sql = "SELECT Username, Password, Role, TenHienThi FROM TaiKhoan WHERE Username = ?";
        Account acc = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    acc = mapResultSetToAccount(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return acc;
    }

    // Check if username exists
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM TaiKhoan WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add new account
    public boolean add(Account acc) {
        String sql = "INSERT INTO TaiKhoan (Username, Password, Role, TenHienThi) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, acc.getUsername());
            stmt.setString(2, acc.getPassword());
            stmt.setString(3, acc.getRole());
            stmt.setString(4, acc.getDisplayName());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update existing account (password, role, displayName)
    public boolean update(Account acc) {
        String sql = "UPDATE TaiKhoan SET Password = ?, Role = ?, TenHienThi = ? WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, acc.getPassword());
            stmt.setString(2, acc.getRole());
            stmt.setString(3, acc.getDisplayName());
            stmt.setString(4, acc.getUsername());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update account without changing password
    public boolean updateWithoutPassword(Account acc) {
        String sql = "UPDATE TaiKhoan SET Role = ?, TenHienThi = ? WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, acc.getRole());
            stmt.setString(2, acc.getDisplayName());
            stmt.setString(3, acc.getUsername());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete account by username
    public boolean delete(String username) {
        String sql = "DELETE FROM TaiKhoan WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Search accounts by username or display name
    public List<Account> search(String keyword) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT Username, Password, Role, TenHienThi FROM TaiKhoan " +
                "WHERE Username LIKE ? OR TenHienThi LIKE ? ORDER BY Username";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account acc = mapResultSetToAccount(rs);
                    list.add(acc);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper method to map ResultSet to Account object
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account acc = new Account();
        acc.setUsername(rs.getString("Username"));
        acc.setPassword(rs.getString("Password"));
        acc.setRole(rs.getString("Role"));
        acc.setDisplayName(rs.getString("TenHienThi"));
        return acc;
    }
}
