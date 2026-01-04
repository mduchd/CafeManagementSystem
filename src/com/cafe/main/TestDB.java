package com.cafe.main;

import com.cafe.config.DatabaseConnection;
import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA KẾT NỐI DATABASE ===\n");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✓ Kết nối database thành công!\n");
                
                // Kiểm tra bảng hoadon
                System.out.println("=== BẢNG HOADON ===");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM hoadon");
                if (rs.next()) {
                    System.out.println("Số hóa đơn trong DB: " + rs.getInt(1));
                }
                
                // Hiển thị 5 hóa đơn gần nhất
                rs = stmt.executeQuery("SELECT * FROM hoadon ORDER BY NgayTao DESC LIMIT 5");
                System.out.println("\n5 hóa đơn gần nhất:");
                System.out.println("MaHD | TongTien | NgayTao | NguoiTao");
                System.out.println("------------------------------------");
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%d | %.0f | %s | %s%n",
                        rs.getInt("MaHD"),
                        rs.getDouble("TongTien"),
                        rs.getTimestamp("NgayTao"),
                        rs.getString("NguoiTao"));
                }
                if (!hasData) {
                    System.out.println("⚠ KHÔNG CÓ DỮ LIỆU TRONG BẢNG HOADON!");
                }
                
                // Kiểm tra chitiethoadon
                System.out.println("\n=== BẢNG CHITIETHOADON ===");
                rs = stmt.executeQuery("SELECT COUNT(*) FROM chitiethoadon");
                if (rs.next()) {
                    System.out.println("Số chi tiết trong DB: " + rs.getInt(1));
                }
                
            }
        } catch (Exception e) {
            System.out.println("✗ Lỗi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
