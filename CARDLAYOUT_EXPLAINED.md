# 🎴 CARDLAYOUT TRONG DỰ ÁN - HƯỚNG DẪN CHI TIẾT

## 📚 CARDLAYOUT LÀ GÌ?

**CardLayout** là một Layout Manager trong Java Swing cho phép **xếp chồng nhiều panel** lên nhau và **chỉ hiển thị 1 panel tại một thời điểm**.

**Ví dụ thực tế:** Giống như một bộ bài - bạn chỉ nhìn thấy lá bài trên cùng, nhưng có nhiều lá bài bên dưới.

---

## 🎯 VỊ TRÍ SỬ DỤNG CARDLAYOUT TRONG DỰ ÁN

### **File:** `src/com/cafe/view/main/MainFrame.java`

### **Vị trí 1: Khai báo biến CardLayout (Dòng 20)**
```java
private java.awt.CardLayout cardLayout;
```
**Giải thích:** Biến này lưu trữ instance của CardLayout để có thể gọi method `show()` sau này.

---

### **Vị trí 2: Lấy CardLayout từ pContent (Dòng 38)**
```java
// Trong method initCustomLogic()
cardLayout = (java.awt.CardLayout) pContent.getLayout();
```

**Giải thích:**
- `pContent` là JPanel chứa tất cả các panel (Sales, Product, Employee, v.v.)
- `pContent` đã được set layout là CardLayout trong NetBeans Design view
- Dòng này lấy CardLayout từ `pContent` để sử dụng

**Tương đương:**
```java
// pContent đã được tạo trong initComponents() như sau:
pContent = new JPanel();
pContent.setLayout(new CardLayout());  // ← CardLayout được set ở đây
```

---

### **Vị trí 3: Thêm Panel vào CardLayout (Dòng 44-49)**
```java
// Add panels to content area
pContent.add(salesPanel, "SALES");                              // ← Card 1
pContent.add(createPlaceholderPanel("Quản lý Bàn"), "TABLES");  // ← Card 2
pContent.add(createPlaceholderPanel("Quản lý Sản phẩm"), "PRODUCTS");  // ← Card 3
pContent.add(createPlaceholderPanel("Quản lý Kho"), "WAREHOUSE");      // ← Card 4
pContent.add(createPlaceholderPanel("Thống kê"), "STATS");             // ← Card 5
pContent.add(createPlaceholderPanel("Quản lý Nhân viên"), "EMPLOYEES");// ← Card 6
```

**Cú pháp:**
```java
pContent.add(panel, "CARD_NAME");
//           ^       ^
//           Panel   Tên card (dùng để show sau này)
```

**Giải thích:**
- Mỗi panel được thêm vào với 1 **tên card duy nhất** (String)
- Tên card này sẽ dùng để **chuyển đổi** giữa các panel
- Tất cả panel được **xếp chồng** lên nhau, chỉ 1 panel hiển thị

**Hình ảnh:**
```
pContent (CardLayout)
├── [SALES] SalesPanel          ← Lá bài 1
├── [TABLES] PlaceholderPanel   ← Lá bài 2
├── [PRODUCTS] PlaceholderPanel ← Lá bài 3
├── [WAREHOUSE] PlaceholderPanel← Lá bài 4
├── [STATS] PlaceholderPanel    ← Lá bài 5
└── [EMPLOYEES] PlaceholderPanel← Lá bài 6
     ^
     Chỉ 1 panel hiển thị tại 1 thời điểm
```

---

### **Vị trí 4: Hiển thị Panel ban đầu (Dòng 79)**
```java
cardLayout.show(pContent, "SALES");
```

**Giải thích:**
- Khi mở MainFrame, hiển thị panel "SALES" (Bán hàng) đầu tiên
- `show(container, cardName)` = Hiển thị card có tên "SALES"

---

### **Vị trí 5: Chuyển đổi Panel khi click menu (Dòng 257-260)**
```java
// Trong method setupMenuButton()
btn.addActionListener(e -> {
    setActiveButton(btn);
    cardLayout.show(pContent, cardName);  // ← CHUYỂN ĐỔI PANEL Ở ĐÂY
});
```

**Giải thích:**
- Khi user click vào menu button (VD: "Sản phẩm")
- `cardLayout.show(pContent, "PRODUCTS")` được gọi
- CardLayout ẩn panel hiện tại và hiển thị panel "PRODUCTS"

**Ví dụ cụ thể:**
```java
// Setup button "Sản phẩm"
setupMenuButton(btnProduct, "Sản phẩm", "PRODUCTS");
//                                       ^
//                                       Card name

// Khi user click btnProduct:
// → cardLayout.show(pContent, "PRODUCTS")
// → Panel "PRODUCTS" được hiển thị
```

---

## 🔄 LUỒNG HOẠT ĐỘNG CARDLAYOUT

### **Khi khởi động MainFrame:**
```
1. initComponents() được gọi
   → pContent được tạo với CardLayout

2. initCustomLogic() được gọi
   → Lấy CardLayout: cardLayout = pContent.getLayout()
   → Thêm các panel: pContent.add(panel, "NAME")
   → Hiển thị panel đầu tiên: cardLayout.show(pContent, "SALES")

3. MainFrame hiển thị với SalesPanel
```

### **Khi user click menu button:**
```
User click "Sản phẩm"
   ↓
btnProduct.actionListener được trigger
   ↓
setActiveButton(btnProduct)  ← Đổi màu button
   ↓
cardLayout.show(pContent, "PRODUCTS")  ← CHUYỂN PANEL
   ↓
ProductPanel hiển thị, các panel khác ẩn
```

---

## 📊 MAPPING: BUTTON → CARD NAME → PANEL

```java
// Dòng 61-66: Setup menu buttons
setupMenuButton(btnSales,     "Bán hàng",  "SALES");      // ← Button 1
setupMenuButton(btnTables,    "Bàn",       "TABLES");     // ← Button 2
setupMenuButton(btnProduct,   "Sản phẩm",  "PRODUCTS");   // ← Button 3
setupMenuButton(btnWarehouse, "Kho",       "WAREHOUSE");  // ← Button 4
setupMenuButton(btnStats,     "Thống kê",  "STATS");      // ← Button 5
setupMenuButton(btnEmployee,  "Nhân viên", "EMPLOYEES");  // ← Button 6
```

**Bảng mapping:**
```
┌──────────────┬─────────────┬──────────────┬─────────────────┐
│ Menu Button  │ Text hiển thị│ Card Name   │ Panel           │
├──────────────┼─────────────┼──────────────┼─────────────────┤
│ btnSales     │ "Bán hàng"  │ "SALES"      │ SalesPanel      │
│ btnTables    │ "Bàn"       │ "TABLES"     │ Placeholder     │
│ btnProduct   │ "Sản phẩm"  │ "PRODUCTS"   │ Placeholder     │
│ btnWarehouse │ "Kho"       │ "WAREHOUSE"  │ Placeholder     │
│ btnStats     │ "Thống kê"  │ "STATS"      │ Placeholder     │
│ btnEmployee  │ "Nhân viên" │ "EMPLOYEES"  │ Placeholder     │
└──────────────┴─────────────┴──────────────┴─────────────────┘
```

---

## 💻 CODE CHI TIẾT: setupMenuButton()

```java
private void setupMenuButton(JButton btn, String text, String cardName) {
    // 1. Set text và style cho button
    btn.setText(text);
    btn.setForeground(Color.WHITE);
    btn.setBackground(SIDEBAR_BG);
    // ... các style khác ...
    
    // 2. Thêm hover effect
    btn.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent evt) {
            if (btn != activeButton) {
                btn.setBackground(SIDEBAR_HOVER);  // Đổi màu khi hover
            }
        }
        
        public void mouseExited(MouseEvent evt) {
            if (btn != activeButton) {
                btn.setBackground(SIDEBAR_BG);  // Trở về màu gốc
            }
        }
    });
    
    // 3. QUAN TRỌNG: Click handler để chuyển panel
    btn.addActionListener(e -> {
        setActiveButton(btn);                    // Đổi màu button active
        cardLayout.show(pContent, cardName);     // ← CHUYỂN ĐỔI PANEL
    });
}
```

**Giải thích dòng quan trọng:**
```java
cardLayout.show(pContent, cardName);
//         ^     ^         ^
//         |     |         Tên card (VD: "PRODUCTS")
//         |     Container chứa các panel
//         Method để hiển thị card
```

---

## 🎯 VÍ DỤ CỤ THỂ: CHUYỂN TỪ SALES → PRODUCTS

### **Bước 1: User click button "Sản phẩm"**
```java
// btnProduct được click
```

### **Bước 2: ActionListener được trigger**
```java
btn.addActionListener(e -> {
    setActiveButton(btnProduct);              // Đổi màu button
    cardLayout.show(pContent, "PRODUCTS");    // Chuyển panel
});
```

### **Bước 3: CardLayout xử lý**
```java
// CardLayout tìm panel có tên "PRODUCTS"
// Ẩn panel hiện tại (SALES)
// Hiển thị panel "PRODUCTS"
```

### **Kết quả:**
```
TRƯỚC:
pContent hiển thị: [SALES] SalesPanel

SAU:
pContent hiển thị: [PRODUCTS] PlaceholderPanel
```

---

## 🔧 CÁCH THÊM PANEL MỚI VÀO CARDLAYOUT

### **Bước 1: Tạo panel**
```java
EmployeePanel employeePanel = new EmployeePanel();
```

### **Bước 2: Thêm vào CardLayout**
```java
pContent.add(employeePanel, "EMPLOYEES");
//           ^               ^
//           Panel instance  Card name
```

### **Bước 3: Setup button (đã có sẵn)**
```java
setupMenuButton(btnEmployee, "Nhân viên", "EMPLOYEES");
//                                         ^
//                                         Phải khớp với card name
```

### **Hoàn thành!**
Khi click button "Nhân viên" → EmployeePanel hiển thị

---

## ⚠️ LƯU Ý QUAN TRỌNG

### **1. Card Name phải CHÍNH XÁC:**
```java
// ✅ ĐÚNG:
pContent.add(employeePanel, "EMPLOYEES");
setupMenuButton(btnEmployee, "Nhân viên", "EMPLOYEES");  // Khớp

// ❌ SAI:
pContent.add(employeePanel, "EMPLOYEE");   // Thiếu 'S'
setupMenuButton(btnEmployee, "Nhân viên", "EMPLOYEES");  // Không khớp
```

### **2. Card Name là String, phân biệt HOA/thường:**
```java
"EMPLOYEES" ≠ "employees" ≠ "Employees"
```

### **3. Mỗi panel chỉ add 1 lần:**
```java
// ✅ ĐÚNG:
pContent.add(salesPanel, "SALES");

// ❌ SAI (add 2 lần):
pContent.add(salesPanel, "SALES");
pContent.add(salesPanel, "SALES2");  // Lỗi!
```

---

## 📋 TÓM TẮT

### **CardLayout trong dự án:**
1. **Khai báo:** `private CardLayout cardLayout;` (dòng 20)
2. **Lấy instance:** `cardLayout = pContent.getLayout();` (dòng 38)
3. **Thêm panel:** `pContent.add(panel, "NAME");` (dòng 44-49)
4. **Hiển thị panel:** `cardLayout.show(pContent, "NAME");` (dòng 79, 259)

### **Cách chuyển đổi panel:**
```java
cardLayout.show(pContent, "CARD_NAME");
```

### **Vị trí quan trọng:**
- **Dòng 38:** Lấy CardLayout
- **Dòng 44-49:** Thêm panels
- **Dòng 259:** Chuyển đổi panel khi click menu

---

## 🎉 KẾT LUẬN

**CardLayout** là cơ chế chính để **chuyển đổi giữa các panel** trong MainFrame.

**Nguyên tắc:**
- 1 container (pContent)
- Nhiều panel (xếp chồng)
- 1 panel hiển thị tại 1 thời điểm
- Chuyển đổi bằng `cardLayout.show()`

**Đơn giản như:** Lật bài - chỉ nhìn thấy 1 lá bài, nhưng có nhiều lá bên dưới! 🎴
