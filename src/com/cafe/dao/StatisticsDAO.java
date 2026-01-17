package com.cafe.dao;

import com.cafe.config.DatabaseConnection;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class StatisticsDAO {

    // 1. Tính tổng doanh thu (Sửa thành TongTien, NgayTao)
    public double getTotalRevenue(Timestamp fromDate, Timestamp toDate) {
        double total = 0;
        String sql = "SELECT SUM(TongTien) FROM hoadon WHERE NgayTao BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    // 2. Đếm tổng số đơn hàng
    public int getTotalOrders(Timestamp fromDate, Timestamp toDate) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM hoadon WHERE NgayTao BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 3. Tìm món bán chạy nhất
   
    public String getBestSellingProduct(Timestamp fromDate, Timestamp toDate) {
        String bestSeller = "Chưa có liệu";
        String sql = "SELECT s.TenSP, SUM(c.SoLuong) as qty " +
                     "FROM chitiethoadon c " +
                     "JOIN hoadon h ON c.MaHD = h.MaHD " +
                     "JOIN sanpham s ON c.MaSP = s.MaSP " +
                     "WHERE h.NgayTao BETWEEN ? AND ? " +
                     "GROUP BY s.TenSP " +
                     "ORDER BY qty DESC LIMIT 1";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bestSeller = rs.getString("TenSP");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestSeller;
    }

    // 4. Lấy danh sách đơn hàng đổ vào Bảng (QUAN TRỌNG NHẤT)
    public DefaultTableModel getOrdersModel(Timestamp fromDate, Timestamp toDate) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("STT");
        columnNames.add("Mã đơn hàng");
        columnNames.add("Người lập"); 
        columnNames.add("Thời gian");
        columnNames.add("Tổng tiền");

        Vector<Vector<Object>> data = new Vector<>();
        
        // Cập nhật đúng tên cột theo ảnh: MaHD, TongTien, NgayTao, NguoiTao
        String sql = "SELECT MaHD, TongTien, NgayTao, NguoiTao FROM hoadon WHERE NgayTao BETWEEN ? AND ? ORDER BY NgayTao DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            

            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            
            int stt = 1;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(stt++);
                
                // Lấy MaHD (trong DB có thể là int hoặc String)
                row.add(rs.getString("MaHD")); 
                
                // Lấy NguoiTao
                row.add(rs.getString("NguoiTao"));

                // Lấy NgayTao và format lại
                try {
                    row.add(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(rs.getTimestamp("NgayTao")));
                } catch (Exception e) { row.add(""); }

                // Lấy TongTien và format tiền tệ
                try {
                    row.add(String.format("%,.0f đ", rs.getDouble("TongTien")));
                } catch (Exception e) { row.add("0 đ"); }
                
                data.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultTableModel(data, columnNames);
    }
}