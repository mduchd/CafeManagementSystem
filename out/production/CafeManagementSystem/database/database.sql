CREATE DATABASE IF NOT EXISTS CafeDB;
USE CafeDB;

-- 1. Bảng Tài khoản (Khớp với login)
CREATE TABLE TaiKhoan (
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(50) NOT NULL,
    Role VARCHAR(20) NOT NULL, -- 'Admin' hoặc 'NhanVien'
    TenHienThi NVARCHAR(50)
);

-- 2. Bảng Sản phẩm (Khớp với model.Product)
CREATE TABLE SanPham (
    MaSP INT AUTO_INCREMENT PRIMARY KEY,
    TenSP NVARCHAR(100) NOT NULL,
    GiaBan DOUBLE NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT 'DangBan'
);

-- 3. Bảng Nguyên liệu (Khớp với model.Ingredient)
CREATE TABLE NguyenLieu (
    MaNL INT AUTO_INCREMENT PRIMARY KEY,
    TenNL NVARCHAR(100) NOT NULL,
    DonViTinh NVARCHAR(20),
    SoLuongTon DOUBLE DEFAULT 0,
    GiaNhap DOUBLE DEFAULT 0
);

-- 4. Bảng Phiếu nhập (Khớp với model.ImportNote)
CREATE TABLE PhieuNhap (
    MaPN INT AUTO_INCREMENT PRIMARY KEY,
    NgayNhap DATETIME DEFAULT CURRENT_TIMESTAMP,
    TongTien DOUBLE,
    NguoiNhap VARCHAR(50),
    FOREIGN KEY (NguoiNhap) REFERENCES TaiKhoan(Username)
);

-- 5. Bảng Hóa đơn & Chi tiết (Khớp với sales)
CREATE TABLE HoaDon (
    MaHD INT AUTO_INCREMENT PRIMARY KEY,
    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    TongTien DOUBLE,
    NguoiTao VARCHAR(50),
    FOREIGN KEY (NguoiTao) REFERENCES TaiKhoan(Username)
);

CREATE TABLE ChiTietHoaDon (
    MaHD INT,
    MaSP INT,
    SoLuong INT,
    ThanhTien DOUBLE,
    PRIMARY KEY (MaHD, MaSP),
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);

-- Thêm dữ liệu mẫu
INSERT INTO TaiKhoan VALUES ('admin', '123', 'Admin', N'Quản Lý');
INSERT INTO SanPham (TenSP, GiaBan) VALUES (N'Cafe Đen', 20000), (N'Cafe Sữa', 25000);