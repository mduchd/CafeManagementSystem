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

```
View (Java Swing UI)
        |
Service (Business Logic)
        |
DAO (SQL, JDBC)
        |
Database (MySQL)
```

**Nguyên tắc:**
- Không viết SQL trong View
- Không xử lý UI trong Service
- Không đặt nghiệp vụ trong DAO

## Cấu trúc dự án

```
CafeManagementSystem/
├── src/
│   ├── database/
│   │   └── database.sql
│   ├── icon/
│   └── com/cafe/
│       ├── config/
│       ├── model/
│       ├── khoản |
