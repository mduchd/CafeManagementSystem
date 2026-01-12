# Hướng dẫn chạy ứng dụng

## Yêu cầu hệ thống

- JDK 8 hoặc cao hơn
- MySQL (XAMPP)
- NetBeans IDE

## Bước 1: Cài đặt Database

1. Cài đặt XAMPP
2. Start Apache và MySQL
3. Truy cập http://localhost/phpmyadmin
4. Tạo database: cafedb
5. Import file: src/database/database.sql

## Bước 2: Mở project

1. Mở NetBeans
2. File -> Open Project
3. Chọn thư mục CafeManagementSystem

## Bước 3: Thêm JDBC Driver

1. Click phải vào Libraries
2. Chọn Add JAR/Folder
3. Chọn file lib/mysql-connector-java-8.0.27.jar

## Bước 4: Chạy ứng dụng

1. Mở src/com/cafe/main/Main.java
2. Nhấn Shift + F6

## Tài khoản mặc định

- Username: admin
- Password: 123

## Khắc phục lỗi thường gặp

### Lỗi kết nối database
- Kiểm tra MySQL đã start trong XAMPP
- Kiểm tra database cafedb tồn tại
- Kiểm tra file DatabaseConnection.java có đúng thông tin

### Lỗi thiếu thư viện
- Kiểm tra đã add mysql-connector-java vào Libraries