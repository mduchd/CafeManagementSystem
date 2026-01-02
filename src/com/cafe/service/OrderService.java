package com.cafe.service;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.Order;
import com.cafe.model.OrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Order operations
 */
public class OrderService {
    
    /**
     * Tạo hóa đơn mới (insert vào HoaDon và ChiTietHoaDon)
     * @param order Order object với details
     * @return ID của hóa đơn vừa tạo, hoặc -1 nếu lỗi
     */
    public int createOrder(Order order) {
        String sqlOrder = "INSERT INTO HoaDon (TongTien, NguoiTao) VALUES (?, ?)";
        String sqlDetail = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // Begin transaction
            
            // 1. Insert HoaDon
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setDouble(1, order.getTotalAmount());
            psOrder.setString(2, order.getCreatedBy());
            psOrder.executeUpdate();
            
            // Get generated ID
            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            
            // 2. Insert ChiTietHoaDon
            if (orderId > 0 && order.getDetails() != null) {
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                for (OrderDetail detail : order.getDetails()) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, detail.getProductId());
                    psDetail.setInt(3, detail.getQuantity());
                    psDetail.setDouble(4, detail.getTotalPrice());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }
            
            conn.commit();  // Commit transaction
            return orderId;
            
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Lấy tất cả hóa đơn (để thống kê)
     */
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY NgayTao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("MaHD"),
                    rs.getTimestamp("NgayTao"),
                    rs.getDouble("TongTien"),
                    rs.getString("NguoiTao")
                );
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Lấy chi tiết hóa đơn
     */
    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSP, sp.GiaBan " +
                    "FROM ChiTietHoaDon ct " +
                    "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                    "WHERE ct.MaHD = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderDetail detail = new OrderDetail(
                    rs.getInt("MaHD"),
                    rs.getInt("MaSP"),
                    rs.getString("TenSP"),
                    rs.getInt("SoLuong"),
                    rs.getDouble("GiaBan"),
                    rs.getDouble("ThanhTien")
                );
                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Lấy tổng doanh thu theo ngày
     */
    public double getTotalRevenueByDate(java.sql.Date date) {
        String sql = "SELECT SUM(TongTien) as total FROM HoaDon WHERE DATE(NgayTao) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
