
package com.cafe.config;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/CafeDB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "";
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy thư viện MySQL Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể kết nối tới Cơ sở dữ liệu (Hãy kiểm tra XAMPP)!");
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("Đang kiểm tra kết nối...");
        Connection testConn = getConnection();
        
        if (testConn != null) {
            System.out.println("KẾT NỐI THÀNH CÔNG!");
            System.out.println("Dự án của bạn đã sẵn sàng để truy vấn dữ liệu.");
            closeConnection(testConn);
        } else {
            System.out.println("KẾT NỐI THẤT BẠI!");
            System.out.println("Hãy đảm bảo bạn đã:");
            System.out.println("1. Start MySQL trong XAMPP.");
            System.out.println("2. Tạo Database tên 'CafeDB'.");
            System.out.println("3. Add file mysql-connector-j-xxx.jar vào thư mục Libraries.");
        }
    }
}

