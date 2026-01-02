# BƯỚC CUỐI: GẮN EVENT CHO NÚT THANH TOÁN

## ✅ CODE ĐÃ HOÀN THÀNH:

1. ✅ Order và OrderDetail models
2. ✅ OrderService với method createOrder()
3. ✅ ProductService.getProductIdByName()
4. ✅ ProductDAO.getProductIdByName()
5. ✅ SalesPanel.handleCheckout() - Logic thanh toán
6. ✅ SalesPanel.parseCurrency() - Parse tiền tệ
7. ✅ SalesPanel.printInvoice() - In hóa đơn

---

## ⚠️ CÒN THIẾU 1 BƯỚC: GẮN EVENT

### **Cần tìm button thanh toán và gắn sự kiện**

Trong `initLogic()` của SalesPanel, thêm:

```java
// Tìm button thanh toán (có thể tên: btnPay, btnCheckout, jButton_thanhtoan, ...)
// Thêm ActionListener:

btnCheckout.addActionListener(e -> handleCheckout());
```

hoặc:

```java
btnPay.addActionListener(e -> handleCheckout());
```

---

## 🔍 CÁCH TÌM TÊN BUTTON:

### **Option 1: Tìm trong code**
```java
// Tìm các dòng có chữ "THANH TOÁN" hoặc "PAY"
// VD: btnCheckout.setText("THANH TOÁN");
```

### **Option 2: Xem trong NetBeans Design**
1. Mở `SalesPanel.java` trong NetBeans
2. Click tab **Design**
3. Click vào button thanh toán
4. Xem **Properties** → Tên biến (variable name)

### **Option 3: Tìm trong variables declaration**
Xem cuối file SalesPanel.java:
```java
// Variables declaration
private javax.swing.JButton btnCheckout;  // ← Tên button
private javax.swing.JButton btnCancel;
private javax.swing.JButton btnPay;
// ...
```

---

## 📝 THÊM EVENT VÀO initLogic()

### **Ví dụ hoàn chỉnh:**

```java
private void initLogic() {
    // ... code hiện tại ...
    
    // 11) Gắn sự kiện cho button Hủy
    btnCancel.addActionListener(e -> clearBill());
    
    // 12) Gắn sự kiện cho button THANH TOÁN - THÊM DÒNG NÀY
    btnCheckout.addActionListener(e -> handleCheckout());
    
    // ... code khác ...
}
```

---

## ✅ SAU KHI THÊM EVENT:

### **Test:**
1. Chạy ứng dụng
2. Login (admin/123)
3. Vào "Bán hàng"
4. Chọn bàn
5. Click sản phẩm → Thêm vào bill
6. Click nút "THANH TOÁN"
7. Xác nhận
8. **Kết quả:**
   - ✅ Hóa đơn được lưu vào database
   - ✅ Dialog in hóa đơn hiển thị
   - ✅ Bàn chuyển về trống (màu xanh)
   - ✅ Bill được clear

---

## 🎯 KIỂM TRA DATABASE:

```sql
SELECT * FROM HoaDon ORDER BY NgayTao DESC;
SELECT * FROM ChiTietHoaDon WHERE MaHD = 1;
```

---

Bạn cần:
1. Tìm tên button thanh toán trong SalesPanel
2. Thêm dòng: `btnTenButton.addActionListener(e -> handleCheckout());`
3. Vào đúng vị trí trong `initLogic()`
4. Test!

🎉 **XONG!**
