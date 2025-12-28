# Kiến trúc Cafe Management System

## 🎯 Nguyên tắc thiết kế

✅ **1 MainFrame duy nhất** - Sử dụng CardLayout để chuyển đổi giữa các panel  
✅ **Phân quyền bằng UI** - Ẩn/hiện menu dựa trên role, không tạo frame mới  
✅ **Code đơn giản** - Phù hợp với dự án nhỏ, tránh over-engineering  

## 📁 Cấu trúc thư mục

```
src/com/cafe/
├── main/
│   ├── Main.java              ← Entry point (khởi chạy LoginFrame)
│   └── DBTestMain.java         ← Test database connection
├── model/
│   ├── User.java              ← User entity
│   ├── Product.java           ← Product entity
│   └── UserRole.java          ← Enum: MANAGER, STAFF
├── service/
│   ├── UserSession.java       ← Quản lý session (static methods)
│   ├── AuthService.java       ← Xác thực đăng nhập
│   └── ProductService.java    ← Business logic cho Product
├── dao/
│   ├── AuthDAO.java           ← Database access cho User
│   └── ProductDAO.java        ← Database access cho Product
└── view/
    ├── login/
    │   ├── LoginFrame.java    ← Màn hình đăng nhập
    │   └── ChangePassDialog.java
    ├── main/
    │   └── MainFrame.java     ← Main window DUY NHẤT (CardLayout)
    └── sales/
        └── SalesPanel.java    ← Panel bán hàng
```

## 🔄 Luồng hoạt động

```
1. Main.java
   ↓
2. LoginFrame (đăng nhập)
   ↓
3. UserSession.setCurrentUser(user)
   ↓
4. MainFrame (hiển thị theo role)
   ├─ MANAGER → Sidebar + All Panels
   └─ STAFF   → Chỉ SalesPanel (no sidebar)
```

## 🎨 MainFrame - CardLayout Architecture

### Cấu trúc MainFrame

```
MainFrame (BorderLayout)
├── pSidebar (WEST) - Chỉ hiện với MANAGER
│   ├── pLogo (TOP)
│   ├── pMenu (CENTER) - BoxLayout
│   │   ├── btnSales
│   │   ├── btnTables
│   │   ├── btnProduct
│   │   ├── btnWarehouse
│   │   ├── btnStats
│   │   └── btnEmployee
│   └── pRoleIndicator (BOTTOM)
│       ├── lblRole (username + role)
│       └── btnLogout
└── pContent (CENTER) - CardLayout
    ├── "SALES" → SalesPanel
    ├── "TABLES" → TablesPanel (placeholder)
    ├── "PRODUCTS" → ProductsPanel (placeholder)
    ├── "WAREHOUSE" → WarehousePanel (placeholder)
    ├── "STATS" → StatsPanel (placeholder)
    └── "EMPLOYEES" → EmployeesPanel (placeholder)
```

### Phân quyền trong MainFrame

#### MANAGER Role
```java
if (UserSession.isManager()) {
    pSidebar.setVisible(true);  // Hiện sidebar
    // Hiện tất cả menu buttons
    btnSales.setVisible(true);
    btnTables.setVisible(true);
    btnProduct.setVisible(true);
    // ... tất cả buttons
}
```

#### STAFF Role
```java
if (UserSession.isStaff()) {
    pSidebar.setVisible(false);  // Ẩn sidebar
    cardLayout.show(pContent, "SALES");  // Chỉ hiện SalesPanel
    addStaffLogoutButton();  // Thêm nút logout ở góc phải
}
```

## 🔐 UserSession - Simple Static Class

```java
public class UserSession {
    private static User currentUser;
    
    public static User getCurrentUser() { ... }
    public static void setCurrentUser(User user) { ... }
    public static void clear() { ... }
    public static boolean isManager() { ... }
    public static boolean isStaff() { ... }
}
```

**Không dùng Singleton pattern** - Giữ đơn giản với static methods

## ✅ Những gì ĐÃ XÓA (không cần thiết)

❌ MainManager.java - Thay bằng phân quyền trong MainFrame  
❌ MainStaff.java - Thay bằng phân quyền trong MainFrame  
❌ Nhiều JFrame riêng biệt - Chỉ dùng 1 MainFrame + CardLayout  

## 🚀 Cách chạy ứng dụng

```bash
# Compile
javac -encoding UTF-8 -d build -sourcepath src src/com/cafe/main/Main.java

# Run
java -cp build com.cafe.main.Main
```

## 📝 Quy tắc phát triển

1. **Thêm panel mới**: Tạo panel trong `view/` và add vào CardLayout trong `MainFrame.initCustomLogic()`
2. **Phân quyền**: Chỉnh sửa `MainFrame.applyRolePermissions()` để ẩn/hiện menu
3. **Không tạo JFrame mới**: Mọi màn hình đều là JPanel trong CardLayout
4. **Giữ code đơn giản**: Đây là dự án nhỏ, tránh over-engineering

## 🎯 Ưu điểm của kiến trúc này

✅ **Đơn giản** - Dễ hiểu, dễ maintain  
✅ **Linh hoạt** - Dễ thêm panel mới  
✅ **Hiệu quả** - Chỉ 1 window, không tốn tài nguyên  
✅ **Rõ ràng** - Phân quyền tập trung tại 1 chỗ  
✅ **Mở rộng** - Dễ thêm role mới hoặc panel mới  
