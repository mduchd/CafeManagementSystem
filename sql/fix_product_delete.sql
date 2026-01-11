-- =============================================
-- Sửa Foreign Key để cho phép xóa sản phẩm
-- Đơn hàng cũ vẫn giữ được (MaSP thành NULL)
-- =============================================

USE cafedb;

-- 1. Cho phép MaSP có thể NULL
ALTER TABLE chitiethoadon MODIFY MaSP INT NULL;

-- 2. Xóa FK cũ (có thể báo lỗi nếu tên FK khác)
ALTER TABLE chitiethoadon DROP FOREIGN KEY chitiethoadon_ibfk_2;

-- 3. Tạo FK mới với ON DELETE SET NULL
ALTER TABLE chitiethoadon 
ADD CONSTRAINT fk_chitiethoadon_sanpham 
FOREIGN KEY (MaSP) REFERENCES sanpham(MaSP) ON DELETE SET NULL;

SELECT 'Done! Bây giờ có thể xóa sản phẩm, đơn hàng cũ vẫn giữ được.' AS Status;
