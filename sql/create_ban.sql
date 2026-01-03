-- Run this script in phpMyAdmin or MySQL Workbench

CREATE TABLE IF NOT EXISTS Ban (
    MaBan INT PRIMARY KEY AUTO_INCREMENT,
    TenBan NVARCHAR(50) NOT NULL,
    TrangThai NVARCHAR(20) DEFAULT 'Trong',  -- Trong, DangSuDung, DaDat
    SoChoNgoi INT DEFAULT 4,
    ViTri NVARCHAR(50)
);

-- Insert sample data
INSERT INTO Ban (TenBan, TrangThai, SoChoNgoi, ViTri)
VALUES 
('Bàn 1', 'Trong', 4, 'Tầng 1'),
('Bàn 2', 'Trong', 4, 'Tầng 1'),
('Bàn 3', 'DangSuDung', 6, 'Tầng 1'),
('Bàn VIP 1', 'Trong', 8, 'Tầng 2'),
('Bàn VIP 2', 'DaDat', 8, 'Tầng 2'),
('Bàn Sân Vườn', 'Trong', 4, 'Sân vườn');
