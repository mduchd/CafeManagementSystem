-- =============================================
-- CafeDB - Complete Database Schema
-- Run this script in phpMyAdmin or MySQL Workbench
-- =============================================

-- 1. Create database
CREATE DATABASE IF NOT EXISTS cafedb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cafedb;

-- =============================================
-- 2. BẢNG TÀI KHOẢN (TaiKhoan)
-- =============================================
CREATE TABLE IF NOT EXISTS TaiKhoan (
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(20) DEFAULT 'STAFF',  -- MANAGER, STAFF
    TenHienThi NVARCHAR(100)
);

-- Sample accounts (password = '123456')
INSERT INTO TaiKhoan (Username, Password, Role, TenHienThi) VALUES 
('admin', '123456', 'MANAGER', 'Quản lý'),
('staff1', '123456', 'STAFF', 'Nhân viên 1'),
('staff2', '123456', 'STAFF', 'Nhân viên 2');

-- =============================================
-- 3. BẢNG NHÂN VIÊN (NhanVien)
-- =============================================
CREATE TABLE IF NOT EXISTS NhanVien (
    MaNV INT PRIMARY KEY AUTO_INCREMENT,
    HoTen NVARCHAR(100) NOT NULL,
    NgaySinh DATE,
    GioiTinh NVARCHAR(10),
    SoDT VARCHAR(15),
    Email VARCHAR(100),
    DiaChi NVARCHAR(255),
    NgayVaoLam DATE,
    Luong DECIMAL(15,2) DEFAULT 0,
    ChucVu NVARCHAR(50)
);

-- Sample employees
INSERT INTO NhanVien (HoTen, NgaySinh, GioiTinh, SoDT, Email, DiaChi, NgayVaoLam, Luong, ChucVu) VALUES 
('Nguyễn Văn A', '1995-05-15', 'Nam', '0901234567', 'nva@cafe.com', 'Quận 1, TP.HCM', '2023-01-10', 8000000, 'Nhân viên'),
('Trần Thị B', '1998-08-20', 'Nữ', '0912345678', 'ttb@cafe.com', 'Quận 3, TP.HCM', '2023-03-15', 10000000, 'Quản lý'),
('Lê Văn C', '2000-12-01', 'Nam', '0923456789', 'lvc@cafe.com', 'Quận 7, TP.HCM', '2024-06-01', 7000000, 'Nhân viên');

-- =============================================
-- 4. BẢNG BÀN (Ban)
-- =============================================
CREATE TABLE IF NOT EXISTS Ban (
    MaBan INT PRIMARY KEY AUTO_INCREMENT,
    TenBan NVARCHAR(50) NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT 'Trong',  -- Trong, DangSuDung, DaDat
    SoChoNgoi INT DEFAULT 4,
    ViTri NVARCHAR(50)
);

-- Sample tables
INSERT INTO Ban (TenBan, TrangThai, SoChoNgoi, ViTri) VALUES 
('Bàn 1', 'Trong', 4, 'Tầng 1'),
('Bàn 2', 'Trong', 4, 'Tầng 1'),
('Bàn 3', 'Trong', 6, 'Tầng 1'),
('Bàn 4', 'Trong', 4, 'Tầng 1'),
('Bàn VIP 1', 'Trong', 8, 'Tầng 2'),
('Bàn VIP 2', 'Trong', 8, 'Tầng 2'),
('Bàn Sân Vườn 1', 'Trong', 4, 'Sân vườn'),
('Bàn Sân Vườn 2', 'Trong', 4, 'Sân vườn');

-- =============================================
-- 5. BẢNG SẢN PHẨM (sanpham)
-- =============================================
CREATE TABLE IF NOT EXISTS sanpham (
    MaSP INT PRIMARY KEY AUTO_INCREMENT,
    TenSP NVARCHAR(100) NOT NULL,
    LoaiSP NVARCHAR(50),       -- Cà phê, Trà, Nước ngọt, Bánh
    GiaBan DECIMAL(10,2) DEFAULT 0,
    TrangThai NVARCHAR(20) DEFAULT 'Đang bán',  -- Đang bán, Ngừng bán
    HinhAnh VARCHAR(255)
);

-- Sample products
INSERT INTO sanpham (TenSP, LoaiSP, GiaBan, TrangThai, HinhAnh) VALUES 
('Cà phê đen', 'Cà phê', 25000, 'Đang bán', 'cafe-den.jpg'),
('Cà phê sữa', 'Cà phê', 30000, 'Đang bán', 'cafe-sua.jpg'),
('Bạc xỉu', 'Cà phê', 32000, 'Đang bán', 'bac-xiu.jpg'),
('Cappuccino', 'Cà phê', 45000, 'Đang bán', 'cappuccino.jpg'),
('Latte', 'Cà phê', 48000, 'Đang bán', 'latte.jpg'),
('Trà đào', 'Trà', 35000, 'Đang bán', 'tra-dao.jpg'),
('Trà vải', 'Trà', 35000, 'Đang bán', 'tra-vai.jpg'),
('Trà sữa trân châu', 'Trà', 40000, 'Đang bán', 'tra-sua.jpg'),
('Coca Cola', 'Nước ngọt', 20000, 'Đang bán', 'coca.jpg'),
('Pepsi', 'Nước ngọt', 20000, 'Đang bán', 'pepsi.jpg'),
('Sting đỏ', 'Nước ngọt', 18000, 'Đang bán', 'sting-do.png'),
('Bánh tiramisu', 'Bánh', 55000, 'Đang bán', 'tiramisu.jpg'),
('Bánh croissant', 'Bánh', 35000, 'Đang bán', 'croissant.jpg');

-- =============================================
-- 6. BẢNG HÓA ĐƠN (HoaDon)
-- =============================================
CREATE TABLE IF NOT EXISTS HoaDon (
    MaHD INT PRIMARY KEY AUTO_INCREMENT,
    MaBan INT,
    NgayTao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TongTien DECIMAL(15,2) DEFAULT 0,
    NguoiTao NVARCHAR(100),
    TrangThai NVARCHAR(20) DEFAULT 'Da thanh toan',  -- Chua thanh toan, Da thanh toan
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan)
);

-- Sample orders
INSERT INTO HoaDon (MaBan, NgayTao, TongTien, NguoiTao, TrangThai) VALUES 
(1, NOW() - INTERVAL 2 DAY, 125000, 'admin', 'Da thanh toan'),
(2, NOW() - INTERVAL 1 DAY, 95000, 'staff1', 'Da thanh toan'),
(3, NOW(), 150000, 'admin', 'Da thanh toan');

-- =============================================
-- 7. BẢNG CHI TIẾT HÓA ĐƠN (ChiTietHoaDon)
-- =============================================
CREATE TABLE IF NOT EXISTS ChiTietHoaDon (
    MaCT INT PRIMARY KEY AUTO_INCREMENT,
    MaHD INT NOT NULL,
    MaSP INT NOT NULL,
    SoLuong INT DEFAULT 1,
    DonGia DECIMAL(10,2),
    ThanhTien DECIMAL(15,2),
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
    FOREIGN KEY (MaSP) REFERENCES sanpham(MaSP)
);

-- Sample order details (linking to orders above)
INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, DonGia, ThanhTien) VALUES 
(1, 1, 2, 25000, 50000),  -- 2x Cà phê đen
(1, 6, 1, 35000, 35000),  -- 1x Trà đào
(1, 8, 1, 40000, 40000),  -- 1x Trà sữa trân châu
(2, 2, 2, 30000, 60000),  -- 2x Cà phê sữa
(2, 7, 1, 35000, 35000),  -- 1x Trà vải
(3, 4, 2, 45000, 90000),  -- 2x Cappuccino
(3, 12, 1, 55000, 55000); -- 1x Bánh tiramisu

-- =============================================
-- DONE! Database is ready to use.
-- =============================================
SELECT 'Database cafedb created successfully!' AS Status;
