package com.cafe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Đảm bảo sử dụng thư viện chuẩn này
import java.util.ArrayList;
import java.util.List;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.Product;

public class ProductDAO {

    private Product readFromResultSet(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("MaSP"));
        p.setName(rs.getString("TenSP"));
        p.setCategory(rs.getString("LoaiSP"));
        p.setPrice(rs.getDouble("GiaBan"));
        p.setStatus(rs.getString("TrangThai"));
        p.setImage(rs.getString("HinhAnh")); 
        return p;
    }

    // Lấy toàn bộ danh sách sản phẩm
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(readFromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm sản phẩm mới (Bao gồm cả hình ảnh)
    public boolean insert(Product p) {
        String sql = "INSERT INTO SanPham (TenSP, LoaiSP, GiaBan, TrangThai, HinhAnh) VALUES (?,?,?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getStatus());
            ps.setString(5, p.getImage());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật sản phẩm (Bao gồm cả hình ảnh)
    public boolean update(Product p) {
        String sql = "UPDATE SanPham SET TenSP=?, LoaiSP=?, GiaBan=?, TrangThai=?, HinhAnh=? WHERE MaSP=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getStatus());
            ps.setString(5, p.getImage());
            ps.setInt(6, p.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa sản phẩm theo mã
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

    // Lọc theo loại sản phẩm
    public List<Product> findByCategory(String category) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE LoaiSP=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readFromResultSet(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Tìm kiếm mã sản phẩm theo tên
    public int getProductIdByName(String name) {
        String sql = "SELECT MaSP FROM SanPham WHERE TenSP = ?";
        
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("MaSP");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; 
    }
}