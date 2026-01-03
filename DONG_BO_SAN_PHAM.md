# HƯỚNG DẪN: ĐỒNG BỘ SẢN PHẨM GIỮA PRODUCTPANEL VÀ SALESPANEL

## 🎯 VẤN ĐỀ
Khi thêm/xóa sản phẩm ở ProductPanel → SalesPanel cần tự động cập nhật menu

## 💡 GIẢI PHÁP: REFRESH KHI SWITCH PANEL

### **Ý tưởng:**
1. SalesPanel có method `refreshMenu()` để load lại menu từ database
2. Khi switch từ ProductPanel → SalesPanel → Gọi `refreshMenu()`
3. Menu tự động cập nhật

---

## 📝 BƯỚC 1: THÊM METHOD refreshMenu() VÀO SALESPANEL

### **File:** `src/com/cafe/view/sales/SalesPanel.java`

### **Thêm import:**
```java
import com.cafe.service.ProductService;
import com.cafe.model.Product;
import java.util.List;
```

### **Thêm field:**
```java
private final ProductService productService = new ProductService();
private JPanel pMenuItems;  // Panel chứa menu items
```

### **Thêm method refreshMenu():**
```java
/**
 * Refresh menu items from database
 */
public void refreshMenu() {
    // Clear existing menu items
    if (pMenuItems != null) {
        pMenuItems.removeAll();
        
        // Load products from database
        List<Product> products = productService.getAllProducts();
        
        // Add menu item buttons
        for (Product product : products) {
            if (product.getStatus() == 1) {  // Only active products
                JButton btn = createMenuItemButton(
                    product.getName(),
                    String.format("%,dđ", product.getPrice()),
                    product.getCategory()
                );
                pMenuItems.add(btn);
            }
        }
        
        // Refresh UI
        pMenuItems.revalidate();
        pMenuItems.repaint();
    }
}
```

---

## 📝 BƯỚC 2: TẠO pMenuItems PANEL

### **Trong initLogic() của SalesPanel:**

```java
private void initLogic() {
    // ... code hiện tại ...
    
    // Create menu items panel
    pMenuItems = new JPanel();
    pMenuItems.setLayout(new GridLayout(0, 3, 10, 10));  // 3 columns
    pMenuItems.setBackground(Color.WHITE);
    
    // Add to scroll pane
    jScrollPane1.setViewportView(pMenuItems);
    
    // Load initial menu
    refreshMenu();
    
    // ... code còn lại ...
}
```

---

## 📝 BƯỚC 3: GỌI refreshMenu() KHI SWITCH PANEL

### **File:** `src/com/cafe/view/main/MainFrame.java`

### **Sửa method setupMenuButton():**

```java
private void setupMenuButton(JButton btn, String text, String cardName) {
    btn.setText(text);
    // ... các style khác ...
    
    // Click handler
    btn.addActionListener(e -> {
        setActiveButton(btn);
        
        // QUAN TRỌNG: Refresh SalesPanel khi switch về
        if (cardName.equals("SALES")) {
            salesPanel.refreshMenu();  // ← THÊM DÒNG NÀY
        }
        
        cardLayout.show(pContent, cardName);
    });
}
```

---

## 📝 BƯỚC 4: GỌI refreshMenu() SAU KHI THÊM/XÓA SẢN PHẨM

### **File:** `src/com/cafe/view/product/ProductPanel.java`

### **Không cần sửa gì!** 
Vì khi user thêm/xóa xong, họ sẽ switch về SalesPanel → refreshMenu() tự động được gọi

---

## 🎯 LUỒNG HOẠT ĐỘNG

```
1. User ở SalesPanel
   ↓
2. Click "Sản phẩm" → Chuyển sang ProductPanel
   ↓
3. Thêm sản phẩm mới (VD: "Cà phê sữa")
   ↓
4. Click "Bán hàng" → Chuyển về SalesPanel
   ↓
5. setupMenuButton() detect cardName = "SALES"
   ↓
6. Gọi salesPanel.refreshMenu()
   ↓
7. refreshMenu() load lại products từ database
   ↓
8. Menu hiển thị "Cà phê sữa" mới
```

---

## 🔧 CODE MẪU HOÀN CHỈNH

### **SalesPanel.java - Thêm vào class:**

```java
public class SalesPanel extends JPanel {
    
    // Existing fields
    private static final Color COLOR_EMPTY = new Color(46, 204, 113);
    private static final Color COLOR_BUSY = new Color(231, 76, 60);
    private static final Color COLOR_SELECTED = new Color(52, 152, 219);
    private final Map<Integer, Integer> tableStatus = new HashMap<>();
    private int selectedTableNo = 1;
    
    // NEW: Add these fields
    private final ProductService productService = new ProductService();
    private JPanel pMenuItems;
    
    public SalesPanel() {
        initComponents();
        initLogic();
    }
    
    private void initLogic() {
        // ... existing code ...
        
        // NEW: Setup menu items panel
        pMenuItems = new JPanel();
        pMenuItems.setLayout(new GridLayout(0, 3, 10, 10));
        pMenuItems.setBackground(Color.WHITE);
        jScrollPane1.setViewportView(pMenuItems);
        
        // Load initial menu
        refreshMenu();
        
        // ... rest of existing code ...
    }
    
    // NEW: Add this method
    /**
     * Refresh menu items from database
     */
    public void refreshMenu() {
        if (pMenuItems != null) {
            pMenuItems.removeAll();
            
            List<Product> products = productService.getAllProducts();
            
            for (Product product : products) {
                if (product.getStatus() == 1) {
                    JButton btn = createMenuItemButton(
                        product.getName(),
                        String.format("%,dđ", product.getPrice()),
                        product.getCategory()
                    );
                    pMenuItems.add(btn);
                }
            }
            
            pMenuItems.revalidate();
            pMenuItems.repaint();
        }
    }
    
    // Existing createMenuItemButton() method
    private JButton createMenuItemButton(String name, String price, String category) {
        // ... existing code ...
    }
}
```

### **MainFrame.java - Sửa setupMenuButton():**

```java
private void setupMenuButton(JButton btn, String text, String cardName) {
    btn.setText(text);
    btn.setForeground(Color.WHITE);
    btn.setBackground(SIDEBAR_BG);
    // ... other styles ...
    
    btn.addActionListener(e -> {
        setActiveButton(btn);
        
        // Refresh SalesPanel menu when switching to SALES
        if (cardName.equals("SALES")) {
            salesPanel.refreshMenu();
        }
        
        cardLayout.show(pContent, cardName);
    });
}
```

---

## ✅ CHECKLIST IMPLEMENTATION

- [ ] Thêm import vào SalesPanel (ProductService, Product, List)
- [ ] Thêm field `productService` và `pMenuItems` vào SalesPanel
- [ ] Tạo `pMenuItems` panel trong `initLogic()`
- [ ] Thêm method `refreshMenu()` vào SalesPanel
- [ ] Gọi `refreshMenu()` trong `initLogic()` (load ban đầu)
- [ ] Sửa `setupMenuButton()` trong MainFrame để gọi `refreshMenu()`
- [ ] Test: Thêm sản phẩm → Switch về Sales → Kiểm tra menu

---

## 🎉 KẾT QUẢ

Sau khi implement:
1. ✅ Thêm sản phẩm ở ProductPanel → Menu tự động cập nhật
2. ✅ Xóa sản phẩm ở ProductPanel → Menu tự động cập nhật
3. ✅ Sửa sản phẩm (tên, giá) → Menu tự động cập nhật
4. ✅ Disable sản phẩm (status=0) → Sản phẩm biến mất khỏi menu

---

## 🚀 NÂNG CAO (TÙY CHỌN)

### **Cách 1: Auto-refresh mỗi 5 giây**
```java
// Trong SalesPanel constructor
Timer refreshTimer = new Timer(5000, e -> refreshMenu());
refreshTimer.start();
```

### **Cách 2: Observer Pattern (Chuyên nghiệp hơn)**
Tạo `ProductChangeListener` interface để notify khi có thay đổi

---

Bạn muốn tôi implement code này vào dự án không? 😊
