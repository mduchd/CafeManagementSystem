# CardLayout - Giải thích

## CardLayout là gì?

CardLayout là một Layout Manager trong Java Swing cho phép chứa nhiều component nhưng chỉ hiển thị 1 component tại một thời điểm.

## Tại sao dùng CardLayout?

- Chuyển đổi giữa các màn hình mà không cần tạo nhiều JFrame
- Tiết kiệm tài nguyên hệ thống
- Dễ quản lý và maintain code

## Cách hoạt động

1. Tạo CardLayout
2. Thêm các panel với tên định danh
3. Chuyển đổi giữa các panel bằng cardLayout.show()

## Trong MainFrame

pContent (CardLayout) chứa các panel: SALES, PRODUCTS, WAREHOUSE, STATS, EMPLOYEES

## Ưu điểm

- Chỉ có 1 JFrame duy nhất
- Chuyển đổi nhanh giữa các panel
- Không tốn bộ nhớ cho các window ẩn
- Dễ thêm panel mới

## Thêm panel mới

1. Tạo class panel mới trong view/
2. Add vào CardLayout trong MainFrame
3. Thêm button navigation trong sidebar