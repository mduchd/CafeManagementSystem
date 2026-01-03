# IMPLEMENTATION: CHỨC NĂNG THANH TOÁN VÀ IN HÓA ĐƠN

## 🎯 MỤC TIÊU
Hoàn thiện chức năng thanh toán trong SalesPanel:
1. Lưu hóa đơn vào database (HoaDon + ChiTietHoaDon)
2. In hóa đơn
3. Cập nhật trạng thái bàn
4. Làm mới bill

## 📊 DATABASE STRUCTURE (Đã có)

### Table HoaDon:
```sql
CREATE TABLE HoaDon (
    MaHD INT AUTO_INCREMENT PRIMARY KEY,
    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    TongTien DOUBLE,
    NguoiTao VARCHAR(50),  -- Username
    FOREIGN KEY (NguoiTao) REFERENCES TaiKhoan(Username)
);
```

### Table ChiTietHoaDon:
```sql
CREATE TABLE ChiTietHoaDon (
    MaHD INT,
    MaSP INT,
    SoLuong INT,
    ThanhTien DOUBLE,
    PRIMARY KEY (MaHD, MaSP),
    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);
```

---

## 📝 BƯỚC 1: TẠO MODEL ORDER

### File: `src/com/cafe/model/Order.java`

```java
package com.cafe.model;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private Date createdDate;
    private double totalAmount;
    private String createdBy;
    private List<OrderDetail> details;
    
    // Constructors
    public Order() {
        this.createdDate = new Date();
    }
    
    public Order(int id, Date createdDate, double totalAmount, String createdBy) {
        this.id = id;
        this.createdDate = createdDate;
        this.totalAmount = totalAmount;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public List<OrderDetail> getDetails() { return details; }
    public void setDetails(List<OrderDetail> details) { this.details = details; }
}
```

### File: `src/com/cafe/model/OrderDetail.java`

```java
package com.cafe.model;

public class OrderDetail {
    private int orderId;
    private int productId;
    private String productName;  // For display
    private int quantity;
    private double unitPrice;    // For display
    private double totalPrice;
    
    public OrderDetail() {}
    
    public OrderDetail(int orderId, int productId, String productName, 
                       int quantity, double unitPrice, double totalPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
```

---

## 📝 BƯỚC 2: TẠO ORDERSERVICE

### File: `src/com/cafe/service/OrderService.java`

```java
package com.cafe.service;

import com.cafe.config.DatabaseConnection;
import com.cafe.model.Order;
import com.cafe.model.OrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    
    /**
     * Tạo hóa đơn mới
     * @return ID của hóa đơn vừa tạo, hoặc -1 nếu lỗi
     */
    public int createOrder(Order order) {
        String sqlOrder = "INSERT INTO HoaDon (TongTien, NguoiTao) VALUES (?, ?)";
        String sqlDetail = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // Begin transaction
            
            // 1. Insert HoaDon
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setDouble(1, order.getTotalAmount());
            psOrder.setString(2, order.getCreatedBy());
            psOrder.executeUpdate();
            
            // Get generated ID
            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            
            // 2. Insert ChiTietHoaDon
            if (orderId > 0 && order.getDetails() != null) {
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                for (OrderDetail detail : order.getDetails()) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, detail.getProductId());
                    psDetail.setInt(3, detail.getQuantity());
                    psDetail.setDouble(4, detail.getTotalPrice());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }
            
            conn.commit();  // Commit transaction
            return orderId;
            
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return -1;
        } finally {
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
    
    /**
     * Lấy tất cả hóa đơn (để thống kê)
     */
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY NgayTao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("MaHD"),
                    rs.getTimestamp("NgayTao"),
                    rs.getDouble("TongTien"),
                    rs.getString("NguoiTao")
                );
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Lấy chi tiết hóa đơn
     */
    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSP, sp.GiaBan " +
                    "FROM ChiTietHoaDon ct " +
                    "JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                    "WHERE ct.MaHD = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderDetail detail = new OrderDetail(
                    rs.getInt("MaHD"),
                    rs.getInt("MaSP"),
                    rs.getString("TenSP"),
                    rs.getInt("SoLuong"),
                    rs.getDouble("GiaBan"),
                    rs.getDouble("ThanhTien")
                );
                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
```

---

## 📝 BƯỚC 3: IMPLEMENT THANH TOÁN TRONG SALESPANEL

### Thêm vào `SalesPanel.java`:

```java
// Import thêm
import com.cafe.model.Order;
import com.cafe.model.OrderDetail;
import com.cafe.service.OrderService;
import com.cafe.service.UserSession;
import java.util.ArrayList;

// Field mới
private final OrderService orderService = new OrderService();

// Method thanh toán
private void handleCheckout() {
    // 1. Validate
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong hóa đơn!");
        return;
    }
    
    // 2. Confirm
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Xác nhận thanh toán cho " + jLabel1.getText() + "?",
        "Xác nhận thanh toán",
        JOptionPane.YES_NO_OPTION);
    
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }
    
    // 3. Tạo Order object
    Order order = new Order();
    order.setTotalAmount(parseCurrency(lblTotalValue.getText()));
    order.setCreatedBy(UserSession.getCurrentUser().getUsername());
    
    // 4. Tạo OrderDetails từ bill table
    List<OrderDetail> details = new ArrayList<>();
    for (int i = 0; i < model.getRowCount(); i++) {
        String productName = model.getValueAt(i, 0).toString();
        int quantity = Integer.parseInt(model.getValueAt(i, 1).toString());
        double unitPrice = parseCurrency(model.getValueAt(i, 2).toString());
        double totalPrice = parseCurrency(model.getValueAt(i, 3).toString());
        
        // Get product ID from name (need ProductService method)
        int productId = productService.getProductIdByName(productName);
        
        OrderDetail detail = new OrderDetail();
        detail.setProductId(productId);
        detail.setProductName(productName);
        detail.setQuantity(quantity);
        detail.setUnitPrice(unitPrice);
        detail.setTotalPrice(totalPrice);
        
        details.add(detail);
    }
    order.setDetails(details);
    
    // 5. Lưu vào database
    int orderId = orderService.createOrder(order);
    
    if (orderId > 0) {
        // 6. In hóa đơn
        printInvoice(orderId, order);
        
        // 7. Cập nhật trạng thái bàn về trống
        tableStatus.put(selectedTableNo, 0);
        refreshAllTableColors();
        
        // 8. Clear bill
        clearBill();
        
        JOptionPane.showMessageDialog(this, 
            "Thanh toán thành công!\nMã hóa đơn: " + orderId);
    } else {
        JOptionPane.showMessageDialog(this, 
            "Lỗi khi lưu hóa đơn!", 
            "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
}

// Helper method: Parse currency string to double
private double parseCurrency(String currencyStr) {
    return Double.parseDouble(currencyStr.replace("đ", "").replace(",", "").trim());
}

// Method in hóa đơn
private void printInvoice(int orderId, Order order) {
    // Tạo dialog hiển thị hóa đơn
    JDialog invoiceDialog = new JDialog();
    invoiceDialog.setTitle("Hóa đơn #" + orderId);
    invoiceDialog.setSize(400, 600);
    invoiceDialog.setLocationRelativeTo(this);
    
    // Tạo nội dung hóa đơn
    StringBuilder invoice = new StringBuilder();
    invoice.append("===========================================\n");
    invoice.append("           QUÁN CAFE ABC\n");
    invoice.append("       Địa chỉ: 123 Đường ABC\n");
    invoice.append("         ĐT: 0123456789\n");
    invoice.append("===========================================\n\n");
    invoice.append("Hóa đơn số: ").append(orderId).append("\n");
    invoice.append("Ngày: ").append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(order.getCreatedDate())).append("\n");
    invoice.append(jLabel1.getText()).append(" - ").append(jLabel2.getText()).append("\n");
    invoice.append("Nhân viên: ").append(UserSession.getCurrentUser().getTenHienThi()).append("\n");
    invoice.append("-------------------------------------------\n\n");
    
    // Chi tiết sản phẩm
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        String name = model.getValueAt(i, 0).toString();
        String qty = model.getValueAt(i, 1).toString();
        String price = model.getValueAt(i, 2).toString();
        String total = model.getValueAt(i, 3).toString();
        
        invoice.append(String.format("%-20s x%2s  %10s\n", name, qty, total));
    }
    
    invoice.append("\n-------------------------------------------\n");
    invoice.append(String.format("%-20s %15s\n", lblSubtotalLabel.getText(), lblSubtotalValue.getText()));
    invoice.append(String.format("%-20s %15s\n", lblDiscountLabel.getText(), txtDiscountPercent.getText() + "%"));
    invoice.append(String.format("%-20s %15s\n", lblTotalLabel.getText(), lblTotalValue.getText()));
    invoice.append("-------------------------------------------\n\n");
    invoice.append("     Cảm ơn quý khách! Hẹn gặp lại!\n");
    invoice.append("===========================================\n");
    
    // Hiển thị trong JTextArea
    JTextArea textArea = new JTextArea(invoice.toString());
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    textArea.setEditable(false);
    
    JScrollPane scrollPane = new JScrollPane(textArea);
    invoiceDialog.add(scrollPane, BorderLayout.CENTER);
    
    // Nút in (có thể implement in thật sau)
    JButton btnPrint = new JButton("In");
    btnPrint.addActionListener(e -> {
        try {
            textArea.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(invoiceDialog, "Lỗi khi in: " + ex.getMessage());
        }
    });
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(btnPrint);
    invoiceDialog.add(buttonPanel, BorderLayout.SOUTH);
    
    invoiceDialog.setVisible(true);
}
```

### Trong initLogic(), sửa btnCheckout:

```java
// Thay vì:
btnCheckout.setText("THANH TOÁN");

// Thành:
btnCheckout.setText("THANH TOÁN");
btnCheckout.addActionListener(e -> handleCheckout());
```

---

## 📝 BƯỚC 4: THÊM METHOD VÀO PRODUCTSERVICE

### Trong `ProductService.java`, thêm:

```java
/**
 * Lấy ID sản phẩm theo tên
 */
public int getProductIdByName(String name) {
    String sql = "SELECT MaSP FROM SanPham WHERE TenSP = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("MaSP");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;  // Not found
}
```

---

## ✅ CHECKLIST IMPLEMENTATION

- [ ] Tạo `Order.java` model
- [ ] Tạo `OrderDetail.java` model
- [ ] Tạo `OrderService.java`
- [ ] Thêm method `getProductIdByName()` vào ProductService
- [ ] Thêm field `orderService` vào SalesPanel
- [ ] Implement method `handleCheckout()` trong SalesPanel
- [ ] Implement method `parseCurrency()` trong SalesPanel
- [ ] Implement method `printInvoice()` trong SalesPanel
- [ ] Gắn event `btnCheckout.addActionListener()`
- [ ] Test: Thêm sản phẩm → Thanh toán → Kiểm tra database
- [ ] Test: In hóa đơn

---

## 🚀 KẾT QUẢ

Sau khi implement:
1. ✅ User thêm sản phẩm vào bill
2. ✅ Ấn "THANH TOÁN"
3. ✅ Hệ thống lưu hóa đơn vào database
4. ✅ Hiển thị dialog in hóa đơn
5. ✅ Bàn chuyển về trạng thái trống
6. ✅ Bill được clear
7. ✅ Dữ liệu hóa đơn sẵn sàng cho thống kê

---

Bạn muốn tôi implement từng bước luôn không? 😊
