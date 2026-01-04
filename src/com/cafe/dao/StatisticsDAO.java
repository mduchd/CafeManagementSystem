package com.cafe.dao;

import com.cafe.config.DatabaseConnection;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class StatisticsDAO {

    // 1. Tính tổng doanh thu trong khoảng thời gian
    public double getTotalRevenue(Timestamp fromDate, Timestamp toDate) {
        double total = 0;
        // Giả sử bảng hóa đơn là 'orders' và cột tổng tiền là 'total_price'
        String sql = "SELECT SUM(total_price) FROM orders WHERE created_at BETWEEN ? AND ?";
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
        String sql = "SELECT COUNT(*) FROM orders WHERE created_at BETWEEN ? AND ?";
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
        // Cần bảng 'order_details' (lưu món ăn) kết nối với 'orders'
        // Logic: Cộng tổng số lượng (quantity) theo tên món, sắp xếp giảm dần, lấy top 1
        String sql = "SELECT d.product_name, SUM(d.quantity) as qty " +
                     "FROM order_details d " +
                     "JOIN orders o ON d.order_id = o.id " +
                     "WHERE o.created_at BETWEEN ? AND ? " +
                     "GROUP BY d.product_name " +
                     "ORDER BY qty DESC LIMIT 1";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bestSeller = rs.getString("product_name");
            }
        } catch (Exception e) {
            // Nếu bảng chưa có dữ liệu hoặc tên bảng khác, trả về mặc định
            System.out.println("Lỗi lấy món bán chạy (Kiểm tra lại tên bảng trong DB): " + e.getMessage());
        }
        return bestSeller;
    }

    // 4. Lấy danh sách đơn hàng để hiển thị lên bảng
    public DefaultTableModel getOrdersModel(Timestamp fromDate, Timestamp toDate) {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("STT");
        columnNames.add("Mã đơn hàng");
        columnNames.add("Thành tiền");
        columnNames.add("Thời gian");

        Vector<Vector<Object>> data = new Vector<>();
        String sql = "SELECT id, total_price, created_at FROM orders WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ResultSet rs = ps.executeQuery();
            
            int stt = 1;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(stt++);
                row.add(rs.getInt("id"));
                row.add(String.format("%,.0f đ", rs.getDouble("total_price"))); // Format tiền: 50.000 đ
                row.add(rs.getTimestamp("created_at").toString());
                data.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultTableModel(data, columnNames);
    }
}