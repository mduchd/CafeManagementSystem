# Hướng dẫn Custom UI trong NetBeans GUI Builder

## ✅ ĐÃ CLEAN UP CODE

File `SalesPanel.java` đã được clean up:
- ❌ XÓA: Tất cả code set màu, text, font trong `initLogic()`
- ✅ GIỮ: Chỉ logic và event handlers

**Bây giờ bạn cần custom UI trong NetBeans GUI Builder!**

---

## 📝 HƯỚNG DẪN CHI TIẾT

### **Bước 1: Mở Design View**

1. Trong NetBeans, tìm file: `SalesPanel.java`
2. Click chuột phải → **Open**
3. Click tab **Design** ở dưới cùng
4. Bạn sẽ thấy giao diện kéo thả

### **Bước 2: Custom Nút Bàn (jButton1 → jButton8)**

**Cho mỗi button (jButton1, jButton2, ..., jButton8):**

1. Click vào button
2. Bên phải, panel **Properties**
3. Set các giá trị:

#### **jButton1 (Bàn 1 - Đang chọn):**
```
text: Bàn 1
background: 52, 152, 219 (xanh dương)
foreground: 255, 255, 255 (trắng)
font: Segoe UI, Bold, 14
focusPainted: false
opaque: true
```

#### **jButton2 (Bàn 2 - Có khách):**
```
text: Bàn 2
background: 231, 76, 60 (đỏ)
foreground: 255, 255, 255 (trắng)
font: Segoe UI, Bold, 14
focusPainted: false
opaque: true
```

#### **jButton3-8 (Bàn 3-8 - Trống):**
```
text: Bàn 3 (đổi số cho từng button)
background: 46, 204, 113 (xanh lá)
foreground: 255, 255, 255 (trắng)
font: Segoe UI, Bold, 14
focusPainted: false
opaque: true
```

### **Bước 3: Custom Filter Buttons**

#### **btnAll:**
```
text: Tất cả
```

#### **btnCoffee:**
```
text: Cà phê
```

#### **btnTea:**
```
text: Trà
```

#### **btnJuice:**
```
text: Nước
```

#### **btnCake:**
```
text: Bánh
```

### **Bước 4: Custom Labels**

#### **jLabel1:**
```
text: Bàn 01
font: Segoe UI, Bold, 14
```

#### **jLabel2:**
```
text: Dùng tại bàn
font: Segoe UI, Plain, 12
```

#### **lblSubtotalLabel:**
```
text: Tạm tính:
font: Segoe UI, Plain, 14
```

#### **lblSubtotalValue:**
```
text: 0đ
font: Segoe UI, Bold, 14
horizontalAlignment: RIGHT
```

#### **lblDiscountLabel:**
```
text: Giảm giá (%):
font: Segoe UI, Plain, 14
```

#### **lblTotalLabel:**
```
text: Tổng cộng:
font: Segoe UI, Bold, 16
```

#### **lblTotalValue:**
```
text: 0đ
font: Segoe UI, Bold, 18
foreground: 52, 152, 219 (xanh dương)
horizontalAlignment: RIGHT
```

### **Bước 5: Custom Action Buttons**

#### **btnCancel (Nút HỦY):**
```
text: HỦY
background: 231, 76, 60 (đỏ)
foreground: 255, 255, 255 (trắng)
font: Segoe UI, Bold, 14
focusPainted: false
preferredSize: 120, 40
```

#### **btnCheckout (Nút THANH TOÁN):**
```
text: THANH TOÁN
background: 46, 204, 113 (xanh lá)
foreground: 255, 255, 255 (trắng)
font: Segoe UI, Bold, 14
focusPainted: false
preferredSize: 150, 40
```

### **Bước 6: Custom TextField**

#### **txtDiscountPercent:**
```
text: 0
horizontalAlignment: RIGHT
preferredSize: 80, 25
```

### **Bước 7: Custom Table**

#### **jTable1:**
- Không cần custom (đã set model trong code)

---

## 🎨 Bảng Màu Sắc

| Màu | RGB | Dùng cho |
|-----|-----|----------|
| **Xanh lá** | 46, 204, 113 | Bàn trống, nút THANH TOÁN |
| **Đỏ** | 231, 76, 60 | Bàn có khách, nút HỦY |
| **Xanh dương** | 52, 152, 219 | Bàn đang chọn, tổng tiền |
| **Trắng** | 255, 255, 255 | Chữ trên nút |

---

## 💡 Tips

### **Cách set màu RGB trong NetBeans:**

1. Click vào property **background** hoặc **foreground**
2. Click nút **...** bên cạnh
3. Chọn tab **RGB**
4. Nhập 3 số: R, G, B
5. Click **OK**

### **Cách set font:**

1. Click vào property **font**
2. Click nút **...**
3. Chọn:
   - Font Name: Segoe UI
   - Font Style: Bold hoặc Plain
   - Size: 12, 14, 16, 18
4. Click **OK**

---

## ✅ Sau khi set xong

1. **Save** file (Ctrl+S)
2. NetBeans tự động generate code vào `initComponents()`
3. **Compile lại:**
   ```bash
   javac -encoding UTF-8 -cp "lib/*" -d build -sourcepath src src\com\cafe\main\Main.java
   ```
4. **Chạy:**
   ```bash
   java -cp "build;lib/*" com.cafe.main.Main
   ```

**Giao diện sẽ giống CHÍNH XÁC với design của bạn!** 🎉

---

## 🔧 Nếu muốn thay đổi sau này

**ĐỪNG sửa code trong `initLogic()`!**

✅ **ĐÚNG:** Mở Design view → Click component → Đổi properties  
❌ **SAI:** Thêm `setText()`, `setBackground()` vào `initLogic()`

**Nguyên tắc:**
- **NetBeans GUI Builder:** Static UI (màu, text, font, size)
- **Code (initLogic):** Dynamic logic (event handlers, business logic)
