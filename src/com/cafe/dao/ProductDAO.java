package com.cafe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.Product;

public class ProductDAO {

    // Lấy danh sách sản phẩm
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("MaSP"));
                p.setName(rs.getString("TenSP"));
                p.setCategory(rs.getString("LoaiSP"));
                p.setPrice(rs.getDouble("GiaBan"));
                p.setStatus(rs.getString("TrangThai"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm sản phẩm
    public boolean insert(Product p) {
        String sql = "INSERT INTO SanPham (TenSP, LoaiSP, GiaBan, TrangThai) VALUES (?,?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getStatus());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật sản phẩm
    public boolean update(Product p) {
        String sql = "UPDATE SanPham SET TenSP=?, LoaiSP=?, GiaBan=?, TrangThai=? WHERE MaSP=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getStatus());
            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa sản phẩm
    public boolean delete(int id) {
        String sql = "DELETE FROM SanPham WHERE MaSP=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // (Optional) Lọc theo loại sản phẩm
    public List<Product> findByCategory(String category) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE LoaiSP=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("MaSP"));
                p.setName(rs.getString("TenSP"));
                p.setCategory(rs.getString("LoaiSP"));
                p.setPrice(rs.getDouble("GiaBan"));
                p.setStatus(rs.getString("TrangThai"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Lấy ID sản phẩm theo tên
     */
    public int getProductIdByName(String name) {
        String sql = "SELECT MaSP FROM SanPham WHERE TenSP = ?";
        
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("MaSP");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;  // Not found
    }
}
