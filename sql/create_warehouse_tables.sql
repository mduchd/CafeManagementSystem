-- =============================================
-- CafeDB - Bảng Quản lý Kho (Warehouse)
-- Chạy file này trong phpMyAdmin
-- =============================================

USE cafedb;

-- =============================================
-- 1. BẢNG NHÀ CUNG CẤP (tbl_supplier)
-- =============================================
CREATE TABLE IF NOT EXISTS tbl_supplier (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name NVARCHAR(100) NOT NULL,
    address NVARCHAR(255),
    phone VARCHAR(20)
);

-- Sample suppliers
INSERT INTO tbl_supplier (supplier_name, address, phone) VALUES 
('Công ty Cà phê Trung Nguyên', 'Buôn Ma Thuột, Đắk Lắk', '0262123456'),
('Đại lý Sữa Vinamilk', 'Quận 7, TP.HCM', '02854001260'),
('Nhà phân phối Đường Biên Hòa', 'Biên Hòa, Đồng Nai', '02513823478'),
('Công ty Kem Rich', 'Quận Bình Tân, TP.HCM', '0283751234');

-- =============================================
-- 2. BẢNG NGUYÊN LIỆU (tbl_ingredient)
-- =============================================
CREATE TABLE IF NOT EXISTS tbl_ingredient (
    ingredient_id INT PRIMARY KEY AUTO_INCREMENT,
    ingredient_name NVARCHAR(100) NOT NULL,
    unit NVARCHAR(20) DEFAULT 'kg',
    quantity INT DEFAULT 0
);

-- Sample ingredients (matching cbIngredient in ImportPanel)
INSERT INTO tbl_ingredient (ingredient_name, unit, quantity) VALUES 
('Cà phê bột', 'kg', 50),
('Cà phê hạt', 'kg', 30),
('Đường', 'kg', 100),
('Bột Béo', 'kg', 20),
('Sữa tươi', 'lít', 50),
('Sữa đặc', 'hộp', 100),
('Kem béo(Rich)', 'hộp', 30),
('Siro', 'chai', 20),
('Mứt/sốt trái cây', 'hũ', 25);

-- =============================================
-- 3. BẢNG NHẬP KHO (tbl_import)
-- =============================================
CREATE TABLE IF NOT EXISTS tbl_import (
    import_id INT PRIMARY KEY AUTO_INCREMENT,
    ingredient_id INT,
    ingredient_name NVARCHAR(100),
    quantity INT DEFAULT 0,
    price DECIMAL(15,2) DEFAULT 0,
    supplier_id INT,
    import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES tbl_supplier(supplier_id)
);

-- Sample import records
INSERT INTO tbl_import (ingredient_name, quantity, price, supplier_id, import_date) VALUES 
('Cà phê bột', 20, 500000, 1, NOW() - INTERVAL 5 DAY),
('Sữa tươi', 30, 150000, 2, NOW() - INTERVAL 3 DAY),
('Đường', 50, 100000, 3, NOW() - INTERVAL 2 DAY),
('Kem béo(Rich)', 10, 200000, 4, NOW() - INTERVAL 1 DAY);

-- =============================================
-- 4. BẢNG XUẤT KHO (tbl_export)
-- =============================================
CREATE TABLE IF NOT EXISTS tbl_export (
    export_id INT PRIMARY KEY AUTO_INCREMENT,
    ingredient_id INT,
    ingredient_name NVARCHAR(100),
    quantity INT DEFAULT 0,
    export_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample export records
INSERT INTO tbl_export (ingredient_name, quantity, export_date) VALUES 
('Cà phê bột', 5, NOW() - INTERVAL 2 DAY),
('Sữa tươi', 10, NOW() - INTERVAL 1 DAY),
('Đường', 15, NOW());

-- =============================================
-- DONE!
-- =============================================
SELECT 'Đã tạo các bảng kho: tbl_supplier, tbl_ingredient, tbl_import, tbl_export' AS Status;
