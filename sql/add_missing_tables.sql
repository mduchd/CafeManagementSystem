-- =============================================
-- CafeDB - Thêm các bảng còn thiếu
-- Chạy file này trong phpMyAdmin
-- =============================================

USE cafedb;

-- =============================================
-- 1. BẢNG SẢN PHẨM (sanpham)
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
-- 2. BẢNG HÓA ĐƠN (HoaDon)
-- =============================================
CREATE TABLE IF NOT EXISTS HoaDon (
    MaHD INT PRIMARY KEY AUTO_INCREMENT,
    MaBan INT,
    NgayTao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    TongTien DECIMAL(15,2) DEFAULT 0,
    NguoiTao NVARCHAR(100),
    TrangThai NVARCHAR(20) DEFAULT 'Da thanh toan',
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan)
);

-- Sample orders
INSERT INTO HoaDon (MaBan, NgayTao, TongTien, NguoiTao, TrangThai) VALUES 
(1, NOW() - INTERVAL 2 DAY, 125000, 'admin', 'Da thanh toan'),
(2, NOW() - INTERVAL 1 DAY, 95000, 'staff1', 'Da thanh toan'),
(3, NOW(), 150000, 'admin', 'Da thanh toan');

-- =============================================
-- 3. BẢNG CHI TIẾT HÓA ĐƠN (ChiTietHoaDon)
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

-- Sample order details
INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, DonGia, ThanhTien) VALUES 
(1, 1, 2, 25000, 50000),
(1, 6, 1, 35000, 35000),
(1, 8, 1, 40000, 40000),
(2, 2, 2, 30000, 60000),
(2, 7, 1, 35000, 35000),
(3, 4, 2, 45000, 90000),
(3, 12, 1, 55000, 55000);

-- =============================================
-- DONE!
-- =============================================
SELECT 'Đã thêm các bảng: sanpham, HoaDon, ChiTietHoaDon' AS Status;
