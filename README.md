# Cafe Management System

Hệ thống quản lý quán Cafe (Desktop Application) được xây dựng bằng Java Swing, áp dụng kiến trúc 3-Tier kết hợp Service Layer.

## Mục lục
- [Giới thiệu](#giới-thiệu)
- [Kiến trúc](#kiến-trúc)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Hướng dẫn cài đặt](#hướng-dẫn-cài-đặt)
- [Thành viên nhóm](#thành-viên-nhóm)

## Giới thiệu

Dự án cung cấp giải pháp quản lý toàn diện cho quán Cafe:

- **Bán hàng (POS):** Gọi món, tạo hóa đơn, tính tiền
- **Kho:** Quản lý nhập/xuất kho, theo dõi tồn kho
- **Thống kê:** Doanh thu, món bán chạy
- **Nhân sự:** Quản lý nhân viên, phân quyền
- **Xác thực:** Đăng nhập, đổi mật khẩu

## Kiến trúc

Dự án áp dụng kiến trúc 3-Tier + Service Layer:

`
View (Java Swing UI)
        |
Service (Business Logic)
        |
DAO (SQL, JDBC)
        |
Database (MySQL)
`

**Nguyên tắc:**
- Không viết SQL trong View
- Không xử lý UI trong Service
- Không đặt nghiệp vụ trong DAO

## Cấu trúc dự án

`
CafeManagementSystem/
├── src/
│   ├── database/
│   │   └── database.sql
│   ├── icon/
│   └── com/cafe/
│       ├── config/
│       ├── model/
│       ├── dao/
│       ├── service/
│       └── view/
│           ├── main/
│           ├── login/
│           ├── sales/
│           ├── product/
│           ├── warehouse/
│           ├── statistics/
│           └── employee/
├── lib/
│   └── mysql-connector-java-8.0.27.jar
└── README.md
`

## Công nghệ sử dụng

| Thành phần | Công nghệ |
|------------|-----------|
| Ngôn ngữ | Java (JDK 8+) |
| Giao diện | Java Swing |
| Database | MySQL |
| Kết nối | JDBC |
| IDE | NetBeans |

## Hướng dẫn cài đặt

### Bước 1: Chuẩn bị Database

1. Cài đặt XAMPP
2. Start Apache và MySQL
3. Truy cập: http://localhost/phpmyadmin
4. Tạo database: `cafedb`
5. Import file: `src/database/database.sql`

### Bước 2: Clone project

`ash
git clone https://github.com/mduchd/CafeManagementSystem.git
`

### Bước 3: Mở project

1. Mở NetBeans
2. Chọn Open Project
3. Trỏ đến thư mục vừa clone

### Bước 4: Thêm JDBC Driver

1. Chuột phải Libraries
2. Chọn Add JAR/Folder
3. Thêm file `lib/mysql-connector-java-8.0.27.jar`

### Bước 5: Chạy chương trình

1. Mở `src/com/cafe/main/Main.java`
2. Nhấn Shift + F6

**Tài khoản mặc định:**
- Username: `admin`
- Password: `123`

## Thành viên nhóm

| Họ tên | Vai trò |
|--------|---------|
| Nguyễn Minh Đức | Quản lý bán hàng, đăng nhập |
| Nguyễn Thúy Hiền | Quản lý sản phẩm |
| Lê Tiến Quân | Báo cáo và thống kê |
| Lê Thị Dịu | Quản lý kho |
| Vũ Ngọc Sơn | Quản lý nhân sự, quản lý bàn |
