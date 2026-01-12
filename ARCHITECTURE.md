# Kiến trúc Cafe Management System

## Nguyên tắc thiết kế

- 1 MainFrame duy nhất - Sử dụng CardLayout để chuyển đổi giữa các panel
- Phân quyền bằng UI - Ẩn/hiện menu dựa trên role, không tạo frame mới
- Code đơn giản - Phù hợp với dự án nhỏ, tránh over-engineering

## Cấu trúc thư mục

```
src/com/cafe/
├── main/
│   └── Main.java              - Entry point
├── model/
│   ├── User.java              - User entity
│   ├── Product.java           - Product entity
│   └── UserRole.java          - Enum: MANAGER, STAFF
├── service/
│   ├── UserSession.java       - Quản lý session
│   ├── AuthService.java       - Xác thực đăng nhập
│   └── ProductService.java    - Business logic
├── dao/
│   ├── AuthDAO.java           - Database access cho User
│   └── ProductDAO.java        - Database access cho Product
└── view/
    ├── login/
    │   └── LoginFrame.java    - Màn hình đăng nhập
    ├── main/
    │   └── MainFrame.java     - Main window (CardLayout)
    ├── sales/
    │   └── SalesPanel.java    - Panel bán hàng
    └── warehouse/
        └── WarehousePanel.java - Panel quản lý kho
```

## Luồng hoạt động

```
1. Main.java
   |
2. LoginFrame (đăng nhập)
   |
3. UserSession.setCurrentUser(user)
   |
4. MainFrame (hiển thị theo role)
   ├─ MANAGER - Sidebar + All Panels
   └─ STAFF   - Chỉ SalesPanel
```

## MainFrame - CardLayout Architecture

### Cấu trúc MainFrame

```
MainFrame (BorderLayout)
├── pSidebar (WEST) - Chỉ hiện với MANAGER
│   ├── pLogo (TOP)
│   ├── pMenu (CENTER)
│   │   ├── btnSales
│   │   ├── btnTables
│   │   ├── btnProduct
│   │   ├── btnWarehouse
│   │   ├── btnStats
│   │   └── btnEmployee
│   └── pRoleIndicator (BOTTOM)
│       ├── lblRole
│       └── btnLogout
└── pContent (CENTER) - CardLayout
    ├── SALES -> SalesPanel
    ├── PRODUCTS -> ProductPanel
    ├── WAREHOUSE -> WarehousePanel
    ├── STATS -> StatsPanel
    └── EMPLOYEES -> EmployeePanel
```

## Phân quyền

### MANAGER Role
- Hiển thị sidebar với tất cả menu buttons
- Có thể truy cập tất cả chức năng

### STAFF Role
- Ẩn sidebar
- Chỉ hiển thị SalesPanel
- Có nút đăng xuất ở góc phải

## Quy tắc phát triển

1. Thêm panel mới: Tạo panel trong view/ và add vào CardLayout
2. Phân quyền: Chỉnh sửa MainFrame.applyRolePermissions()
3. Không tạo JFrame mới: Mọi màn hình đều là JPanel trong CardLayout
4. Giữ code đơn giản

## Ưu điểm của kiến trúc này

- Đơn giản - Dễ hiểu, dễ maintain
- Linh hoạt - Dễ thêm panel mới
- Hiệu quả - Chỉ 1 window
- Rõ ràng - Phân quyền tập trung
- Mở rộng - Dễ thêm role mới