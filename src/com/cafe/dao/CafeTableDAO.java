package com.cafe.dao;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.CafeTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CafeTableDAO {

    // Get all tables
    public List<CafeTable> getAll() {
        List<CafeTable> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban ORDER BY MaBan";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CafeTable table = mapResultSetToTable(rs);
                list.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get table by ID
    public CafeTable getById(int id) {
        String sql = "SELECT * FROM Ban WHERE MaBan = ?";
        CafeTable table = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    table = mapResultSetToTable(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }

    // Add new table
    public boolean add(CafeTable table) {
        String sql = "INSERT INTO Ban (TenBan, TrangThai, SoChoNgoi, ViTri) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, table.getName());
            stmt.setString(2, table.getStatus());
            stmt.setInt(3, table.getCapacity());
            stmt.setString(4, table.getLocation());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update table
    public boolean update(CafeTable table) {
        String sql = "UPDATE Ban SET TenBan=?, TrangThai=?, SoChoNgoi=?, ViTri=? WHERE MaBan=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, table.getName());
            stmt.setString(2, table.getStatus());
            stmt.setInt(3, table.getCapacity());
            stmt.setString(4, table.getLocation());
            stmt.setInt(5, table.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update table status only
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE Ban SET TrangThai=? WHERE MaBan=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete table by ID
    public boolean delete(int id) {
        String sql = "DELETE FROM Ban WHERE MaBan = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Search tables by name or location
    public List<CafeTable> search(String keyword) {
        List<CafeTable> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban WHERE TenBan LIKE ? OR ViTri LIKE ? ORDER BY MaBan";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CafeTable table = mapResultSetToTable(rs);
                    list.add(table);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get tables by status
    public List<CafeTable> getByStatus(String status) {
        List<CafeTable> list = new ArrayList<>();
        String sql = "SELECT * FROM Ban WHERE TrangThai = ? ORDER BY MaBan";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CafeTable table = mapResultSetToTable(rs);
                    list.add(table);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper method to map ResultSet to CafeTable
    private CafeTable mapResultSetToTable(ResultSet rs) throws SQLException {
        CafeTable table = new CafeTable();
        table.setId(rs.getInt("MaBan"));
        table.setName(rs.getString("TenBan"));
        table.setStatus(rs.getString("TrangThai"));
        table.setCapacity(rs.getInt("SoChoNgoi"));
        table.setLocation(rs.getString("ViTri"));
        return table;
    }
}
