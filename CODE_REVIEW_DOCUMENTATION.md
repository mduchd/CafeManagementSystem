# TÀI LIỆU GIẢI THÍCH CODE - CAFE MANAGEMENT SYSTEM
## Phần của Trưởng nhóm: SalesPanel, MainFrame và các Service/DAO liên quan

---

# 📋 MỤC LỤC

1. [Tổng quan Kiến trúc](#1-tổng-quan-kiến-trúc)
2. [MainFrame - Màn hình chính](#2-mainframe---màn-hình-chính)
3. [SalesPanel - Panel bán hàng](#3-salespanel---panel-bán-hàng)
4. [UserSession - Quản lý phiên đăng nhập](#4-usersession---quản-lý-phiên-đăng-nhập)
5. [ProductService & ProductDAO](#5-productservice--productdao)
6. [OrderService - Dịch vụ hóa đơn](#6-orderservice---dịch-vụ-hóa-đơn)
7. [Luồng hoạt động](#7-luồng-hoạt-động)

---

# 1. TỔNG QUAN KIẾN TRÚC

## 1.1 Mô hình MVC (Model - View - Controller)

```
┌─────────────────────────────────────────────────────────────────────┐
│                           VIEW LAYER                                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                  │
│  │ LoginFrame  │  │ MainFrame   │  │ SalesPanel  │                  │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘                  │
└─────────┼────────────────┼────────────────┼─────────────────────────┘
          │                │                │
          ▼                ▼                ▼
┌───────────────────────────────────────────────────────────────────── ┐
│                         SERVICE LAYER                                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                   │
│  │AuthService  │  │UserSession  │  │OrderService │  │ProductService│ │
│  └──────┬──────┘  └─────────────┘  └──────┬──────┘  └──────┬───────┘ │
└─────────┼──────────────────────────────────┼────────────────┼────────┘
          │                                  │                │
          ▼                                  ▼                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                           DAO LAYER                                 │
│  ┌─────────────┐                           ┌─────────────┐          │
│  │  AuthDAO    │                           │ ProductDAO  │          │
│  └──────┬──────┘                           └──────┬──────┘          │
└─────────┼──────────────────────────────────────────┼────────────────┘
          │                                          │
          ▼                                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    DATABASE (MySQL via XAMPP)                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌────────────┐ │
│  │  taikhoan   │  │  SanPham    │  │   HoaDon    │  │ChiTietHoaDon│ │
│  └─────────────┘  └─────────────┘  └─────────────┘  └────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

## 1.2 Giải thích từng Layer

| Layer | Mục đích | Files |
|-------|----------|-------|
| **View** | Giao diện người dùng (UI) | MainFrame.java, SalesPanel.java, LoginFrame.java |
| **Service** | Logic nghiệp vụ, validation | UserSession.java, ProductService.java, OrderService.java |
| **DAO** | Truy cập database | AuthDAO.java, ProductDAO.java |
| **Model** | Đối tượng dữ liệu | User.java, Product.java, Order.java |

---

# 2. MAINFRAME - MÀN HÌNH CHÍNH

## 2.1 Tổng quan

**File:** `src/com/cafe/view/main/MainFrame.java`

**Chức năng:**
- Màn hình chính của ứng dụng sau khi đăng nhập
- Chứa sidebar menu và khu vực content
- Phân quyền giao diện theo vai trò (MANAGER/STAFF)

## 2.2 Cấu trúc UI

```
┌────────────────────────────────────────────────────────────────────┐
│                         MainFrame                                   │
├──────────────┬─────────────────────────────────────────────────────┤
│   SIDEBAR    │              CONTENT AREA (CardLayout)              │
│   (pSidebar) │                    (pContent)                        │
│              │                                                      │
│  ┌────────┐  │  ┌───────────────────────────────────────────────┐  │
│  │  Logo  │  │  │                                               │  │
│  │Java    │  │  │    SalesPanel / ProductPanel / etc.          │  │
│  │Coffee  │  │  │    (chỉ 1 panel hiển thị tại 1 thời điểm)   │  │
│  └────────┘  │  │                                               │  │
│              │  │                                               │  │
│  [Bán hàng]  │  │                                               │  │
│  [Bàn]       │  │                                               │  │
│  [Sản phẩm]  │  │                                               │  │
│  [Kho]       │  │                                               │  │
│  [Thống kê]  │  │                                               │  │
│  [Nhân viên] │  │                                               │  │
│              │  │                                               │  │
│  ──────────  │  └───────────────────────────────────────────────┘  │
│  Nguyễn A    │                                                      │
│  (Quản lý)   │                                                      │
│ [Đăng xuất]  │                                                      │
└──────────────┴─────────────────────────────────────────────────────┘
```

## 2.3 Giải thích Code chi tiết

### 2.3.1 Khai báo biến

```java
// Bảng màu cho sidebar
private static final Color SIDEBAR_BG = new Color(30, 58, 95);      // Xanh đậm (nền)
private static final Color SIDEBAR_HOVER = new Color(41, 82, 130);  // Xanh nhạt (khi hover)
private static final Color SIDEBAR_ACTIVE = new Color(52, 152, 219); // Xanh sáng (đang chọn)

private JButton activeButton = null;  // Lưu nút menu đang được highlight
private CardLayout cardLayout;        // Layout để chuyển đổi giữa các panels
private SalesPanel salesPanel;        // Instance của SalesPanel
```

**Giải thích:**
- `SIDEBAR_BG/HOVER/ACTIVE`: 3 trạng thái màu của nút menu
- `activeButton`: Theo dõi nút nào đang active để highlight
- `cardLayout`: CardLayout cho phép hiển thị 1 trong nhiều panels

### 2.3.2 Constructor

```java
public MainFrame() {
    super("Hệ thống quản lý Cafe");  // Đặt title cho JFrame
    initComponents();                 // Tạo UI (NetBeans generate)
    initCustomLogic();                // Logic tùy chỉnh
    
    setDefaultCloseOperation(EXIT_ON_CLOSE);  // Đóng app khi đóng cửa sổ
    setSize(1400, 800);                       // Kích thước cửa sổ
    setLocationRelativeTo(null);              // Hiển thị giữa màn hình
}
```

### 2.3.3 initCustomLogic() - Phần quan trọng nhất

```java
private void initCustomLogic() {
    // 1. Lấy CardLayout từ pContent
    cardLayout = (CardLayout) pContent.getLayout();
    
    // 2. Tạo và thêm các panels vào CardLayout
    salesPanel = new SalesPanel();
    pContent.add(salesPanel, "SALES");           // Key = "SALES"
    pContent.add(new ProductPanel(), "PRODUCTS");// Key = "PRODUCTS"
    pContent.add(createPlaceholderPanel("Quản lý Bàn"), "TABLES");
    // ... các panel khác
    
    // 3. Style sidebar
    pSidebar.setBackground(SIDEBAR_BG);
    pLogo.setBackground(SIDEBAR_BG);
    pMenu.setBackground(SIDEBAR_BG);
    
    // 4. Setup từng nút menu
    setupMenuButton(btnSales, "Bán hàng", "SALES");
    setupMenuButton(btnProduct, "Sản phẩm", "PRODUCTS");
    // ... các nút khác
    
    // 5. Tạo panel hiển thị user info + logout
    setupRoleIndicatorPanel();
    
    // 6. Phân quyền theo vai trò
    applyRolePermissions();
    
    // 7. Hiển thị panel đầu tiên
    cardLayout.show(pContent, "SALES");
}
```

**Giải thích CardLayout:**
- CardLayout là layout manager cho phép "xếp chồng" nhiều panels
- Chỉ 1 panel được hiển thị tại 1 thời điểm
- Dùng `cardLayout.show(container, "KEY")` để chuyển panel

### 2.3.4 setupMenuButton() - Setup nút menu

```java
private void setupMenuButton(JButton btn, String text, String cardName) {
    // Style cơ bản
    btn.setText(text);                          // Đặt text
    btn.setForeground(Color.WHITE);             // Chữ màu trắng
    btn.setBackground(SIDEBAR_BG);              // Nền xanh đậm
    btn.setFocusPainted(false);                 // Không hiện viền focus
    btn.setBorderPainted(false);                // Không hiện border
    btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    btn.setHorizontalAlignment(SwingConstants.LEFT);  // Căn trái
    
    // Hiệu ứng hover (khi di chuột vào)
    btn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent evt) {
            if (btn != activeButton) {          // Nếu không phải nút đang active
                btn.setBackground(SIDEBAR_HOVER);  // Đổi màu hover
            }
        }
        
        @Override
        public void mouseExited(MouseEvent evt) {
            if (btn != activeButton) {
                btn.setBackground(SIDEBAR_BG);  // Trở về màu bình thường
            }
        }
    });
    
    // Xử lý khi click
    btn.addActionListener(e -> {
        setActiveButton(btn);                   // Đặt nút này là active
        cardLayout.show(pContent, cardName);    // Chuyển sang panel tương ứng
    });
}
```

### 2.3.5 applyRolePermissions() - Phân quyền

```java
private void applyRolePermissions() {
    if (UserSession.isStaff()) {
        // STAFF: Ẩn sidebar, chỉ hiển thị SalesPanel
        pSidebar.setVisible(false);         // Ẩn toàn bộ sidebar
        cardLayout.show(pContent, "SALES"); // Chỉ hiện SalesPanel
        addStaffLogoutButton();             // Thêm nút logout ở góc phải
        
    } else if (UserSession.isManager()) {
        // MANAGER: Hiển thị đầy đủ sidebar
        pSidebar.setVisible(true);
        btnSales.setVisible(true);
        btnTables.setVisible(true);
        btnProduct.setVisible(true);
        btnWarehouse.setVisible(true);
        btnStats.setVisible(true);
        btnEmployee.setVisible(true);
    }
}
```

**Giải thích phân quyền:**
- Kiểm tra vai trò qua `UserSession.isStaff()` hoặc `UserSession.isManager()`
- STAFF: Chỉ được dùng chức năng bán hàng
- MANAGER: Được dùng tất cả chức năng

---

# 3. SALESPANEL - PANEL BÁN HÀNG

## 3.1 Tổng quan

**File:** `src/com/cafe/view/sales/SalesPanel.java`

**Chức năng:**
- Hiển thị sơ đồ bàn
- Hiển thị menu món ăn/đồ uống
- Tạo hóa đơn và thanh toán

## 3.2 Cấu trúc UI

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              SalesPanel                                  │
├─────────────┬──────────────────────────────┬────────────────────────────┤
│   SƠ ĐỒ BÀN │         MENU MÓN             │         HÓA ĐƠN            │
│  (pTableArea)│       (pMenuArea)            │       (pBillArea)          │
│             │                               │                            │
│ ┌───┐ ┌───┐ │  [Tất cả][Cà phê][Trà][Nước] │  Bàn 01        Dùng tại bàn│
│ │B1 │ │B3 │ │  ┌─────────────────────────┐ │  ┌────────────────────────┐│
│ └───┘ └───┘ │  │ ┌─────┐ ┌─────┐ ┌─────┐ │ │  │Món    │SL│Đơn giá│Tiền││
│             │  │ │Café │ │Trà  │ │Nước │ │ │  ├───────┼──┼───────┼────┤│
│ ┌───┐ ┌───┐ │  │ │25k  │ │20k  │ │15k  │ │ │  │       │  │       │    ││
│ │B2 │ │B4 │ │  │ └─────┘ └─────┘ └─────┘ │ │  │       │  │       │    ││
│ └───┘ └───┘ │  │ ┌─────┐ ┌─────┐ ┌─────┐ │ │  └────────────────────────┘│
│             │  │ │Bánh │ │Sinh │ │Soda │ │ │                            │
│ ┌───┐ ┌───┐ │  │ │30k  │ │22k  │ │18k  │ │ │  Tạm tính:           100đ │
│ │B5 │ │B6 │ │  │ └─────┘ └─────┘ └─────┘ │ │  Giảm giá (%):      [  0] │
│ └───┘ └───┘ │  └─────────────────────────┘ │  Tổng cộng:          100đ │
│             │                               │                            │
│ ● Trống     │                               │  [  HỦY  ] [ THANH TOÁN ] │
│ ● Có khách  │                               │                            │
│ ● Đang chọn │                               │                            │
└─────────────┴──────────────────────────────┴────────────────────────────┘
```

## 3.3 Giải thích Code chi tiết

### 3.3.1 Khai báo biến

```java
// Màu sắc cho trạng thái bàn
private static final Color COLOR_EMPTY = new Color(46, 204, 113);    // Xanh lá: bàn trống
private static final Color COLOR_BUSY = new Color(231, 76, 60);      // Đỏ: có khách
private static final Color COLOR_SELECTED = new Color(52, 152, 219); // Xanh dương: đang chọn

// Map lưu trạng thái các bàn: key = số bàn, value = 0 (trống) hoặc 1 (có khách)
private final Map<Integer, Integer> tableStatus = new HashMap<>();

// Số bàn đang được chọn
private int selectedTableNo = 1;

// Services để tương tác với database
private final ProductService productService = new ProductService();
private final OrderService orderService = new OrderService();

// Panel chứa các nút món ăn
private JPanel pMenuItems;
```

### 3.3.2 Constructor

```java
public SalesPanel() {
    initComponents();  // Tạo UI (NetBeans generate)
    initLogic();       // Logic tùy chỉnh
}
```

### 3.3.3 initLogic() - Phần 1: Setup bàn

```java
private void initLogic() {
    // 1) Setup 8 nút bàn
    JButton[] tableBtns = { jButton1, jButton2, jButton3, jButton4, 
                            jButton5, jButton6, jButton7, jButton8 };

    for (int i = 0; i < tableBtns.length; i++) {
        int tableNo = i + 1;          // Số bàn: 1, 2, 3, ...
        JButton b = tableBtns[i];

        // Đặt text và style
        b.setText("Bàn " + tableNo);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);

        // Khởi tạo trạng thái: tất cả bàn đều trống
        tableStatus.put(tableNo, 0);
        
        // Đặt màu ban đầu
        setTableColor(b, 0, tableNo == selectedTableNo);

        // Xử lý khi click vào bàn
        b.addActionListener(e -> selectTable(tableNo));
    }

    // Demo: Bàn 2 có khách
    tableStatus.put(2, 1);
    refreshAllTableColors();  // Cập nhật lại màu tất cả bàn
```

### 3.3.4 initLogic() - Phần 2: Load menu từ database

```java
    // 10) Setup panel chứa menu items
    pMenuItems = new JPanel();
    pMenuItems.setLayout(new GridLayout(0, 3, 10, 10));  // 3 cột, tự động thêm hàng
    pMenuItems.setBackground(Color.WHITE);
    
    // Tìm JScrollPane trong pMenuArea và set pMenuItems làm viewport
    for (Component comp : pMenuArea.getComponents()) {
        if (comp instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) comp;
            scrollPane.setViewportView(pMenuItems);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            break;
        }
    }
    
    // Load menu từ database
    refreshMenu();
}
```

### 3.3.5 refreshMenu() - Tải menu từ database

```java
public void refreshMenu() {
    if (pMenuItems != null) {
        pMenuItems.removeAll();  // Xóa các món cũ
        
        // Lấy danh sách sản phẩm từ database qua ProductService
        List<Product> products = productService.getAllProducts();
        
        // Thêm button cho từng sản phẩm đang bán
        for (Product product : products) {
            String status = product.getStatus();
            
            // Chỉ hiển thị sản phẩm đang bán
            if ("DangBan".equals(status) || "Đang bán".equals(status)) {
                JButton btn = createMenuItemButton(
                    product.getName(),                           // Tên món
                    String.format("%.0fđ", product.getPrice()), // Giá
                    product.getCategory()                        // Loại
                );
                pMenuItems.add(btn);
            }
        }
        
        // Refresh giao diện
        pMenuItems.revalidate();
        pMenuItems.repaint();
    }
}
```

**Luồng dữ liệu:**
```
refreshMenu()
    ↓
productService.getAllProducts()
    ↓
productDAO.findAll()
    ↓
SELECT * FROM SanPham
    ↓
List<Product>
    ↓
Tạo JButton cho mỗi Product
```

### 3.3.6 createMenuItemButton() - Tạo nút món ăn

```java
private JButton createMenuItemButton(String name, String price, String category) {
    JButton btn = new JButton();
    btn.setLayout(new BorderLayout(5, 5));
    btn.setPreferredSize(new Dimension(140, 90));
    btn.setFocusPainted(false);
    
    // Label tên món
    JLabel lblName = new JLabel(name, SwingConstants.CENTER);
    lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
    
    // Label giá
    JLabel lblPrice = new JLabel(price, SwingConstants.CENTER);
    lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    lblPrice.setForeground(new Color(52, 152, 219));  // Màu xanh
    
    // Panel chứa text
    JPanel textPanel = new JPanel(new GridLayout(2, 1));
    textPanel.setOpaque(false);
    textPanel.add(lblName);
    textPanel.add(lblPrice);
    
    btn.add(textPanel, BorderLayout.CENTER);
    btn.setBackground(Color.WHITE);
    
    // Hiệu ứng hover
    btn.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent evt) {
            btn.setBackground(new Color(240, 240, 240));  // Sáng hơn khi hover
        }
        public void mouseExited(MouseEvent evt) {
            btn.setBackground(Color.WHITE);
        }
    });
    
    // Khi click: thêm món vào hóa đơn
    btn.addActionListener(e -> addItemToBill(name, price));
    
    return btn;
}
```

### 3.3.7 addItemToBill() - Thêm món vào hóa đơn

```java
private void addItemToBill(String itemName, String priceStr) {
    // Parse giá (loại bỏ ký tự 'đ' và dấu phẩy)
    String priceNumeric = priceStr.replace(",", "").replace("đ", "").trim();
    int price = Integer.parseInt(priceNumeric);
    
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    
    // Kiểm tra món đã có trong hóa đơn chưa
    boolean found = false;
    for (int i = 0; i < model.getRowCount(); i++) {
        String existingItem = (String) model.getValueAt(i, 0);
        if (existingItem.equals(itemName)) {
            // Món đã có → tăng số lượng
            int currentQty = (Integer) model.getValueAt(i, 1);
            int newQty = currentQty + 1;
            int total = newQty * price;
            
            model.setValueAt(newQty, i, 1);                    // Cập nhật SL
            model.setValueAt(formatCurrency(total), i, 3);     // Cập nhật thành tiền
            found = true;
            break;
        }
    }
    
    // Nếu món chưa có → thêm dòng mới
    if (!found) {
        model.addRow(new Object[]{
            itemName,              // Cột 0: Tên món
            1,                     // Cột 1: Số lượng
            formatCurrency(price), // Cột 2: Đơn giá
            formatCurrency(price)  // Cột 3: Thành tiền
        });
    }
    
    // Cập nhật tổng tiền
    updateTotalAmount();
}
```

### 3.3.8 handleCheckout() - Xử lý thanh toán

```java
private void handleCheckout() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    
    // Kiểm tra có món nào không
    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, 
            "Vui lòng chọn món trước khi thanh toán!",
            "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Tính tổng tiền
    double totalAmount = 0;
    // ... tính toán ...
    
    // Hiển thị dialog xác nhận
    int confirm = JOptionPane.showConfirmDialog(this,
        "Xác nhận thanh toán " + formatCurrency((int)totalAmount) + "?",
        "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        // Tạo Order object
        Order order = new Order();
        order.setTotalAmount(totalAmount);
        order.setCreatedBy(UserSession.getCurrentUser().getUsername());
        
        // Tạo danh sách chi tiết hóa đơn
        List<OrderDetail> details = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String productName = (String) model.getValueAt(i, 0);
            int quantity = (Integer) model.getValueAt(i, 1);
            // ... tạo OrderDetail ...
            details.add(detail);
        }
        order.setDetails(details);
        
        // Lưu vào database qua OrderService
        int orderId = orderService.createOrder(order);
        
        if (orderId > 0) {
            JOptionPane.showMessageDialog(this,
                "Thanh toán thành công!\nMã hóa đơn: " + orderId,
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearBill();  // Xóa hóa đơn
        }
    }
}
```

---

# 4. USERSESSION - QUẢN LÝ PHIÊN ĐĂNG NHẬP

## 4.1 Tổng quan

**File:** `src/com/cafe/service/UserSession.java`

**Chức năng:**
- Lưu trữ thông tin user đang đăng nhập
- Chia sẻ thông tin user cho toàn bộ ứng dụng
- Kiểm tra quyền hạn (Manager/Staff)

## 4.2 Code

```java
public class UserSession {
    // Biến static: chỉ có 1 instance duy nhất cho toàn app
    private static User currentUser;
    
    // Lấy user hiện tại
    public static User getCurrentUser() { 
        return currentUser; 
    }
    
    // Lưu user sau khi login thành công
    public static void setCurrentUser(User user) { 
        currentUser = user; 
    }
    
    // Xóa session (logout)
    public static void clear() { 
        currentUser = null; 
    }
    
    // Kiểm tra có phải Manager không
    public static boolean isManager() {
        if (currentUser == null) return false;
        String role = currentUser.getRole();
        return role != null && 
               (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("manager"));
    }
    
    // Kiểm tra có phải Staff không
    public static boolean isStaff() {
        return !isManager();
    }
}
```

## 4.3 Cách sử dụng

```java
// Khi login thành công (trong LoginFrame):
User user = authService.login(username, password);
if (user != null) {
    UserSession.setCurrentUser(user);  // Lưu vào session
    new MainFrame().setVisible(true);
}

// Trong MainFrame - kiểm tra quyền:
if (UserSession.isManager()) {
    pSidebar.setVisible(true);  // Hiện sidebar cho Manager
}

// Lấy thông tin user để hiển thị:
User currentUser = UserSession.getCurrentUser();
String name = currentUser.getFullname();  // "Nguyễn Văn A"

// Khi logout:
UserSession.clear();  // Xóa session
```

---

# 5. PRODUCTSERVICE & PRODUCTDAO

## 5.1 ProductDAO - Truy cập database

**File:** `src/com/cafe/dao/ProductDAO.java`

```java
public class ProductDAO {
    
    // Lấy tất cả sản phẩm
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("MaSP"));          // ID sản phẩm
                p.setName(rs.getString("TenSP"));     // Tên sản phẩm
                p.setCategory(rs.getString("LoaiSP")); // Loại: Cà phê, Trà, ...
                p.setPrice(rs.getDouble("GiaBan"));   // Giá bán
                p.setStatus(rs.getString("TrangThai")); // Trạng thái: DangBan, NgungBan
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Thêm sản phẩm mới
    public boolean insert(Product p) {
        String sql = "INSERT INTO SanPham (TenSP, LoaiSP, GiaBan, TrangThai) VALUES (?,?,?,?)";
        
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getStatus());
            
            return ps.executeUpdate() > 0;  // true nếu insert thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật sản phẩm
    public boolean update(Product p) {
        String sql = "UPDATE SanPham SET TenSP=?, LoaiSP=?, GiaBan=?, TrangThai=? WHERE MaSP=?";
        // ... tương tự insert ...
    }
    
    // Xóa sản phẩm
    public boolean delete(int id) {
        String sql = "DELETE FROM SanPham WHERE MaSP=?";
        // ...
    }
}
```

## 5.2 ProductService - Logic nghiệp vụ

**File:** `src/com/cafe/service/ProductService.java`

```java
public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    // Lấy danh sách sản phẩm
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    // Thêm sản phẩm với validation
    public boolean addProduct(Product p) {
        if (!validateProduct(p)) {  // Kiểm tra dữ liệu
            return false;
        }
        return productDAO.insert(p);
    }

    // Cập nhật sản phẩm với validation
    public boolean updateProduct(Product p) {
        if (p.getId() <= 0 || !validateProduct(p)) {
            return false;
        }
        return productDAO.update(p);
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int id) {
        if (id <= 0) {
            return false;
        }
        return productDAO.delete(id);
    }

    // Validation: Kiểm tra dữ liệu đầu vào
    private boolean validateProduct(Product p) {
        if (p == null) return false;
        if (p.getName() == null || p.getName().trim().isEmpty()) return false;
        if (p.getCategory() == null || p.getCategory().trim().isEmpty()) return false;
        if (p.getPrice() <= 0) return false;
        return true;
    }
}
```

## 5.3 So sánh DAO vs Service

| Đặc điểm | DAO | Service |
|----------|-----|---------|
| **Nhiệm vụ** | Truy cập database | Logic nghiệp vụ |
| **Chứa** | SQL queries | Validation, business rules |
| **Gọi bởi** | Service | View (UI) |
| **Ví dụ** | `SELECT * FROM SanPham` | Kiểm tra giá > 0 |

**Tại sao cần cả 2?**
- Tách biệt trách nhiệm (Separation of Concerns)
- Dễ bảo trì: thay đổi database không ảnh hưởng logic
- Dễ test: có thể test Service mà không cần database

---

# 6. ORDERSERVICE - DỊCH VỤ HÓA ĐƠN

## 6.1 Tổng quan

**File:** `src/com/cafe/service/OrderService.java`

**Chức năng:**
- Tạo hóa đơn mới (HoaDon + ChiTietHoaDon)
- Lấy danh sách hóa đơn (cho thống kê)
- Tính doanh thu

## 6.2 createOrder() - Tạo hóa đơn

```java
public int createOrder(Order order) {
    String sqlOrder = "INSERT INTO HoaDon (TongTien, NguoiTao) VALUES (?, ?)";
    String sqlDetail = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";
    
    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);  // BẮT ĐẦU TRANSACTION
        
        // 1. Insert vào bảng HoaDon
        PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
        psOrder.setDouble(1, order.getTotalAmount());
        psOrder.setString(2, order.getCreatedBy());
        psOrder.executeUpdate();
        
        // Lấy ID vừa tạo (auto-generated)
        ResultSet rs = psOrder.getGeneratedKeys();
        int orderId = -1;
        if (rs.next()) {
            orderId = rs.getInt(1);
        }
        
        // 2. Insert vào bảng ChiTietHoaDon
        if (orderId > 0 && order.getDetails() != null) {
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            for (OrderDetail detail : order.getDetails()) {
                psDetail.setInt(1, orderId);           // Mã hóa đơn
                psDetail.setInt(2, detail.getProductId()); // Mã sản phẩm
                psDetail.setInt(3, detail.getQuantity());  // Số lượng
                psDetail.setDouble(4, detail.getTotalPrice()); // Thành tiền
                psDetail.addBatch();  // Thêm vào batch
            }
            psDetail.executeBatch();  // Thực thi tất cả cùng lúc
        }
        
        conn.commit();  // COMMIT TRANSACTION (lưu tất cả)
        return orderId;
        
    } catch (Exception e) {
        try {
            if (conn != null) conn.rollback();  // ROLLBACK nếu lỗi
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
        return -1;
    } finally {
        // Đóng connection
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

**Giải thích Transaction:**
- `setAutoCommit(false)`: Bắt đầu transaction
- `commit()`: Lưu tất cả thay đổi
- `rollback()`: Hủy tất cả nếu có lỗi
- Đảm bảo HoaDon và ChiTietHoaDon được tạo đồng thời hoặc không tạo gì cả

---

# 7. LUỒNG HOẠT ĐỘNG

## 7.1 Luồng đăng nhập và hiển thị MainFrame

```
1. User mở ứng dụng
   → Main.java chạy
   → Hiển thị LoginFrame
   
2. User nhập username/password, click "Đăng nhập"
   → LoginFrame gọi AuthService.login(username, password)
   → AuthService gọi AuthDAO.login()
   → AuthDAO thực thi SQL: SELECT * FROM taikhoan WHERE ...
   → Trả về User object
   
3. Nếu login thành công:
   → UserSession.setCurrentUser(user)  // Lưu vào session
   → Tạo new MainFrame()
   → MainFrame.initCustomLogic() chạy:
      a) Tạo SalesPanel, ProductPanel, etc.
      b) Setup menu buttons
      c) Gọi applyRolePermissions():
         - Nếu Manager → Hiện sidebar
         - Nếu Staff → Ẩn sidebar, chỉ hiện SalesPanel
   → Hiển thị MainFrame
```

## 7.2 Luồng bán hàng trong SalesPanel

```
1. SalesPanel được tạo
   → initLogic() chạy:
      a) Setup 8 nút bàn với màu sắc
      b) Gọi refreshMenu():
         → ProductService.getAllProducts()
         → ProductDAO.findAll()
         → SELECT * FROM SanPham
         → Tạo button cho mỗi sản phẩm
         
2. User chọn bàn (click nút bàn)
   → selectTable(tableNo)
   → refreshAllTableColors()
   → Cập nhật header hóa đơn "Bàn 01"
   
3. User chọn món (click button món)
   → addItemToBill(name, price)
   → Kiểm tra món đã có → tăng SL hoặc thêm dòng mới
   → updateTotalAmount()
   
4. User click "THANH TOÁN"
   → handleCheckout()
   → Tạo Order + List<OrderDetail>
   → OrderService.createOrder(order)
   → Insert HoaDon + ChiTietHoaDon vào database
   → Hiển thị thông báo thành công
   → clearBill()
```

---

# TÓM TẮT

## Các file quan trọng

| File | Vai trò |
|------|---------|
| **MainFrame.java** | Màn hình chính, phân quyền UI |
| **SalesPanel.java** | Panel bán hàng, tạo hóa đơn |
| **UserSession.java** | Quản lý phiên đăng nhập |
| **ProductService.java** | Logic nghiệp vụ sản phẩm |
| **ProductDAO.java** | Truy cập database sản phẩm |
| **OrderService.java** | Logic nghiệp vụ hóa đơn |

## Nguyên tắc thiết kế

1. **MVC Pattern**: View → Service → DAO → Database
2. **Separation of Concerns**: Mỗi class có 1 nhiệm vụ
3. **CardLayout**: Chuyển đổi giữa các panels trong MainFrame
4. **UserSession**: Chia sẻ thông tin user toàn app
5. **Transaction**: Đảm bảo tính toàn vẹn dữ liệu khi tạo hóa đơn
