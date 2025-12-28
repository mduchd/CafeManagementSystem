#  Cafe Management System

![Java](https://img.shields.io/badge/Language-Java-orange)
![NetBeans](https://img.shields.io/badge/IDE-NetBeans-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-lightgrey)
![Architecture](https://img.shields.io/badge/Architecture-3--Tier%20%2B%20Service-green)

Hệ thống quản lý quán Cafe (**Desktop Application**) được xây dựng bằng **Java Swing**, áp dụng kiến trúc **3-Tier (Layered Architecture)** kết hợp **Service Layer**, giúp dễ bảo trì, mở rộng và làm việc nhóm hiệu quả.

---

## 📖 Mục lục
- [Giới thiệu](#-giới-thiệu)
- [Kiến trúc & Luồng dữ liệu](#-kiến-trúc--luồng-dữ-liệu)
- [Cấu trúc dự án](#-cấu-trúc-dự-án)
- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Hướng dẫn cài đặt](#️-hướng-dẫn-cài-đặt-setup)
- [Quy chuẩn code](#-quy-chuẩn-code-coding-convention)
- [Quy trình Git](#-quy-trình-làm-việc-git-workflow)
- [Thành viên nhóm](#-thành-viên-nhóm)

---

## 🚀 Giới thiệu
Dự án cung cấp giải pháp quản lý toàn diện cho một quán Cafe vừa và nhỏ:

- 🛒 **Bán hàng (POS):** Gọi món, tạo hóa đơn, tính tiền
- 📦 **Kho:** Quản lý nhập kho, theo dõi tồn kho
- 📊 **Thống kê:** Doanh thu, món bán chạy
- 👥 **Nhân sự:** Quản lý nhân viên, phân quyền
- 🔐 **Xác thực:** Đăng nhập, đổi mật khẩu

---

## 🧱 Kiến trúc & Luồng dữ liệu

Dự án áp dụng kiến trúc **3-Tier + Service Layer**:

```text
View (Java Swing UI)
        ↓
Service (Business Logic)
        ↓
DAO (SQL, JDBC)
        ↓
Database (MySQL)
Nguyên tắc bắt buộc
❌ Không viết SQL trong View

❌ Không xử lý UI trong Service

❌ Không đặt nghiệp vụ trong DAO

📂 Cấu trúc dự án
CafeManagementSystem
├── database/
│   └── database.sql            # Script khởi tạo CSDL (chạy đầu tiên)
├── src/com/cafe/
│   ├── config/                 # JDBC config (DatabaseConnection)
│   ├── model/                  # Entity: Product, Employee, Order...
│   ├── dao/                    # DAO: thao tác SQL
│   ├── service/                # Business Logic
│   └── view/                   # Java Swing UI
│       ├── main/               # MainFrame
│       ├── login/              # Đăng nhập
│       ├── sales/              # Bán hàng
│       ├── warehouse/          # Kho
│       ├── statistics/         # Thống kê
│       └── employee/           # Nhân sự
├── .gitignore
├── build.xml
├── manifest.mf
└── README.md
 ```
## 🛠 Công nghệ sử dụng
Thành phần	Công nghệ / Công cụ	Ghi chú
Ngôn ngữ	Java (JDK 8+)	Core Java
Giao diện	Java Swing	NetBeans GUI Builder
Database	MySQL	Chạy trên XAMPP
Kết nối	JDBC	mysql-connector-j
IDE	NetBeans	Khuyên dùng 8.2+
Quản lý mã nguồn	Git + GitHub	Làm việc nhóm
⚙️ Hướng dẫn cài đặt (Setup)
Bước 1: Chuẩn bị Database

Cài đặt XAMPP

Start Apache và MySQL

Truy cập: http://localhost/phpmyadmin

Tạo database: CafeDB

Import file: database/database.sql

Bước 2: Clone & mở project
git clone https://github.com/mduchd/CafeManagementSystem.git


Mở NetBeans

Chọn Open Project

Trỏ đến thư mục vừa clone

Bước 3: Thêm JDBC Driver

Chuột phải Libraries

Chọn Add JAR/Folder

Thêm file mysql-connector-j-x.x.x.jar

Bước 4: Chạy chương trình

Mở src/com/cafe/main/Main.java

Nhấn Shift + F6

Tài khoản Admin mặc định

Username: admin
Password: 123456

## 📜 Quy chuẩn Code (Coding Convention)

### 1️⃣ Đặt tên UI Component (BẮT BUỘC)

| Component | Prefix | Ví dụ |
|----------|--------|-------|
| Button   | btn    | btnLogin, btnSave |
| TextField| txt    | txtUsername |
| Label    | lbl    | lblTitle |
| Table    | tbl    | tblProduct |
| ComboBox | cbo    | cboCategory |

### 2️⃣ Quy tắc Java
- Tên biến & hàm: **camelCase**
- Tên class: **PascalCase**
- Không hard-code SQL trong View
- Logic xử lý phải nằm trong Service


Tên biến & hàm: camelCaseên class: PascalCase

Không hard-code SQL trong View

Logic xử lý phải nằm trong Service

🤝 Quy trình làm việc (Git Workflow)

❌ Không commit trực tiếp vào main

Mỗi thành viên tạo branch riêng:

feature-sales
feature-warehouse
feature-statistics


Luôn git pull trước khi code

Hoàn thành → Push branch → tạo Pull Request

Trưởng nhóm review trước khi merge

## 👥 Thành viên nhóm

| Họ tên | Vai trò |
|------|--------|
| Nguyễn Minh Đức | Quản lý bán hàng + đăng nhập, đăng xuất |
| Nguyễn Thúy Hiền | Quản lý sản phẩm |
| Lê Tiến Quân | Báo cáo và thống kê |
| Lê Thị Dịu | Quản lý kho |
| Vũ Ngọc Sơn | Quản lý nhân sự + Quản lý bàn |
