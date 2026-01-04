-- Run this script in phpMyAdmin or MySQL Workbench

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

-- Insert sample data
INSERT INTO NhanVien (HoTen, NgaySinh, GioiTinh, SoDT, Email, DiaChi, NgayVaoLam, Luong, ChucVu)
VALUES 
('Nguyễn Văn A', '1995-05-15', 'Nam', '0901234567', 'nva@cafe.com', 'Quận 1, TP.HCM', '2023-01-10', 8000000, 'Nhân viên'),
('Trần Thị B', '1998-08-20', 'Nữ', '0912345678', 'ttb@cafe.com', 'Quận 3, TP.HCM', '2023-03-15', 10000000, 'Quản lý');
