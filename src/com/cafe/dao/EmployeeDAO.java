package com.cafe.dao;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.Employee;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Get employee list
    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY MaNV";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee emp = mapResultSetToEmployee(rs);
                list.add(emp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get employee by ID
    public Employee getById(int id) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";
        Employee emp = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    emp = mapResultSetToEmployee(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }

    // Add new employee
    public boolean add(Employee emp) {
        String sql = "INSERT INTO NhanVien (HoTen, NgaySinh, GioiTinh, SoDT, Email, DiaChi, NgayVaoLam, Luong, ChucVu) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emp.getFullName());
            stmt.setDate(2, emp.getDateOfBirth() != null ? Date.valueOf(emp.getDateOfBirth()) : null);
            stmt.setString(3, emp.getGender());
            stmt.setString(4, emp.getPhoneNumber());
            stmt.setString(5, emp.getEmail());
            stmt.setString(6, emp.getAddress());
            stmt.setDate(7, emp.getStartedDate() != null ? Date.valueOf(emp.getStartedDate()) : null);
            stmt.setDouble(8, emp.getSalary());
            stmt.setString(9, emp.getPosition());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update existing employee
    public boolean update(Employee emp) {
        String sql = "UPDATE NhanVien SET HoTen=?, NgaySinh=?, GioiTinh=?, SoDT=?, Email=?, DiaChi=?, NgayVaoLam=?, Luong=?, ChucVu=? "
                +
                "WHERE MaNV=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emp.getFullName());
            stmt.setDate(2, emp.getDateOfBirth() != null ? Date.valueOf(emp.getDateOfBirth()) : null);
            stmt.setString(3, emp.getGender());
            stmt.setString(4, emp.getPhoneNumber());
            stmt.setString(5, emp.getEmail());
            stmt.setString(6, emp.getAddress());
            stmt.setDate(7, emp.getStartedDate() != null ? Date.valueOf(emp.getStartedDate()) : null);
            stmt.setDouble(8, emp.getSalary());
            stmt.setString(9, emp.getPosition());
            stmt.setInt(10, emp.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete employee by ID
    public boolean delete(int id) {
        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Search employees by name or phone
    public List<Employee> search(String keyword) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE HoTen LIKE ? OR SoDT LIKE ? ORDER BY MaNV";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = mapResultSetToEmployee(rs);
                    list.add(emp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper method to map ResultSet to Employee object
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("MaNV"));
        emp.setFullName(rs.getString("HoTen"));

        Date dob = rs.getDate("NgaySinh");
        emp.setDateOfBirth(dob != null ? dob.toLocalDate() : null);

        emp.setGender(rs.getString("GioiTinh"));
        emp.setPhoneNumber(rs.getString("SoDT"));
        emp.setEmail(rs.getString("Email"));
        emp.setAddress(rs.getString("DiaChi"));

        Date startDate = rs.getDate("NgayVaoLam");
        emp.setStartedDate(startDate != null ? startDate.toLocalDate() : null);

        emp.setSalary(rs.getDouble("Luong"));
        emp.setPosition(rs.getString("ChucVu"));

        return emp;
    }
}
