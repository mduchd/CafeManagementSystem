# Kien truc Cafe Management System

## Nguyen tac thiet ke

- 1 MainFrame duy nhat - Su dung CardLayout de chuyen doi giua cac panel
- Phan quyen bang UI - An/hien menu dua tren role, khong tao frame moi
- Code don gian - Phu hop voi du an nho, tranh over-engineering

## Cau truc thu muc

`
src/com/cafe/
├── main/
│   └── Main.java              - Entry point
├── model/
│   ├── User.java              - User entity
│   ├── Product.java           - Product entity  
│   └── UserRole.java          - Enum: MANAGER, STAFF
├── service/
│   ├── UserSession.java       - Quan ly session
│   ├── AuthService.java       - Xac thuc dang nhap
│   └── ProductService.java    - Business logic
├── dao/
│   ├── AuthDAO.java           - Database access cho User
│   └── ProductDAO.java        - Database access cho Product
└── view/
    ├── login/
    │   └── LoginFrame.java    - Man hinh dang nhap
    ├── main/
    │   └── MainFrame.java     - Main window (CardLayout)
    ├── sales/
    │   └── SalesPanel.java    - Panel ban hang
    └── warehouse/
        └── WarehousePanel.java - Panel quan ly kho
`

## Luong hoat dong

`
1. Main.java
   |
2. LoginFrame (dang nhap)
   |
3. UserSession.setCurrentUser(user)
   |
4. MainFrame (hien thi theo role)
   ├─ MANAGER - Sidebar + All Panels
   └─ STAFF   - Chi SalesPanel
`

## MainFrame - CardLayout Architecture

### Cau truc MainFrame

`
MainFrame (BorderLayout)
├── pSidebar (WEST) - Chi hien voi MANAGER
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
`

### Phan quyen trong MainFrame

MANAGER Role:
`java
if (UserSession.isManager()) {
    pSidebar.setVisible(true);  // Hien sidebar
    // Hien tat ca menu buttons
}
`

STAFF Role:
`java
if (UserSession.isStaff()) {
    pSidebar.setVisible(false);  // An sidebar
    cardLayout.show(pContent, "SALES");  // Chi hien SalesPanel
}
`

## UserSession - Simple Static Class

`java
public class UserSession {
    private static User currentUser;
    
    public static User getCurrentUser() { ... }
    public static void setCurrentUser(User user) { ... }
    public static void clear() { ... }
    public static boolean isManager() { ... }
    public static boolean isStaff() { ... }
}
`

## Quy tac phat trien

1. Them panel moi: Tao panel trong view/ va add vao CardLayout
2. Phan quyen: Chinh sua MainFrame.applyRolePermissions()
3. Khong tao JFrame moi: Moi man hinh deu la JPanel trong CardLayout
4. Giu code don gian

## Uu diem cua kien truc nay

- Don gian - De hieu, de maintain
- Linh hoat - De them panel moi
- Hieu qua - Chi 1 window
- Ro rang - Phan quyen tap trung
- Mo rong - De them role moi
