package com.cafe.main;

import com.cafe.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * File test để debug login
 */
public class TestLogin {
    public static void main(String[] args) {
        System.out.println("=== TEST KẾT NỐI VÀ ĐĂNG NHẬP ===\n");
        
        // Test 1: Kết nối database
        System.out.println("1. Kiểm tra kết nối database...");
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("❌ KHÔNG KẾT NỐI ĐƯỢC DATABASE!");
            System.out.println("   Kiểm tra:");
            System.out.println("   - XAMPP MySQL đã start chưa?");
            System.out.println("   - Database 'CafeDB' đã tạo chưa?");
            System.out.println("   - MySQL Connector JAR đã add vào project chưa?");
            return;
        }
        System.out.println("✅ Kết nối database thành công!\n");
        
        // Test 2: Kiểm tra bảng taikhoan
        System.out.println("2. Kiểm tra bảng 'taikhoan'...");
        try {
            String checkTable = "SELECT * FROM taikhoan LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(checkTable);
            ResultSet rs = stmt.executeQuery();
            System.out.println("✅ Bảng 'taikhoan' tồn tại!\n");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("❌ KHÔNG TÌM THẤY BẢNG 'taikhoan'!");
            System.out.println("   Lỗi: " + e.getMessage());
            return;
        }
        
        // Test 3: Liệt kê tất cả tài khoản
        System.out.println("3. Danh sách tài khoản trong database:");
        System.out.println("   ----------------------------------------");
        try {
            String sql = "SELECT Username, Password, Role, TenHienThi FROM taikhoan";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("   Tài khoản #" + count + ":");
                System.out.println("   - Username: " + rs.getString("Username"));
                System.out.println("   - Password: " + rs.getString("Password"));
                System.out.println("   - Role: " + rs.getString("Role"));
                System.out.println("   - TenHienThi: " + rs.getString("TenHienThi"));
                System.out.println("   ----------------------------------------");
            }
            
            if (count == 0) {
                System.out.println("   ⚠️ KHÔNG CÓ TÀI KHOẢN NÀO!");
            } else {
                System.out.println("   ✅ Tìm thấy " + count + " tài khoản\n");
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("❌ LỖI khi đọc dữ liệu!");
            System.out.println("   Lỗi: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Test 4: Thử đăng nhập với admin/123
        System.out.println("4. Test đăng nhập với admin/123...");
        try {
            String sql = "SELECT Username, Role, TenHienThi FROM taikhoan WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "admin");
            stmt.setString(2, "123");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("✅ ĐĂNG NHẬP THÀNH CÔNG!");
                System.out.println("   - Username: " + rs.getString("Username"));
                System.out.println("   - Role: " + rs.getString("Role"));
                System.out.println("   - TenHienThi: " + rs.getString("TenHienThi"));
            } else {
                System.out.println("❌ ĐĂNG NHẬP THẤT BẠI!");
                System.out.println("   Không tìm thấy tài khoản admin/123");
                System.out.println("   Kiểm tra lại Username và Password trong database");
            }
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("❌ LỖI khi test đăng nhập!");
            System.out.println("   Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Đóng kết nối
        DatabaseConnection.closeConnection(conn);
        System.out.println("\n=== KẾT THÚC TEST ===");
    }
}
