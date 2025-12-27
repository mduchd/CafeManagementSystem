# Hướng dẫn chạy và test UI

## 🚀 Cách chạy ứng dụng

### 1. Compile
```bash
javac -encoding UTF-8 -d build -sourcepath src src\com\cafe\main\Main.java
```

### 2. Chạy
```bash
java -cp build com.cafe.main.Main
```

## 👤 Tài khoản đăng nhập

### ✅ Để thấy SIDEBAR (Full UI):
Đăng nhập với tài khoản có **Role = "admin"** hoặc **"manager"**

**Ví dụ trong database:**
```sql
-- Kiểm tra tài khoản admin/manager
SELECT Username, Role, TenHienThi FROM taikhoan WHERE Role IN ('admin', 'manager');
```

**Nếu chưa có, tạo tài khoản admin:**
```sql
INSERT INTO taikhoan (Username, Password, Role, TenHienThi) 
VALUES ('admin', '123', 'admin', 'Quản lý');
```

**Sau đó đăng nhập:**
- Username: `admin`
- Password: `123`

→ **Sẽ thấy sidebar với đầy đủ menu**

### ⚠️ Để thấy UI không có sidebar:
Đăng nhập với tài khoản có **Role = "staff"** hoặc bất kỳ role nào khác

**Ví dụ:**
```sql
INSERT INTO taikhoan (Username, Password, Role, TenHienThi) 
VALUES ('nhanvien', '123', 'staff', 'Nhân viên Demo');
```

**Đăng nhập:**
- Username: `nhanvien`
- Password: `123`

→ **Chỉ thấy SalesPanel, không có sidebar**

## 🎨 So sánh UI

### MANAGER/ADMIN (có sidebar):
```
┌────────────────────────────────────────────────────────┐
│  ┌──────────────┬──────────────────────────────────┐  │
│  │   SIDEBAR    │      CONTENT AREA                │  │
│  │              │                                   │  │
│  │  ☕ Logo     │                                   │  │
│  │              │      [Panel hiện tại]            │  │
│  │  📊 Bán hàng │                                   │  │
│  │  🪑 Bàn      │                                   │  │
│  │  🍔 Sản phẩm │                                   │  │
│  │  📦 Kho      │                                   │  │
│  │  📈 Thống kê │                                   │  │
│  │  👥 Nhân viên│                                   │  │
│  │              │                                   │  │
│  │  ─────────── │                                   │  │
│  │  Quản lý     │                                   │  │
│  │  [Đăng xuất] │                                   │  │
│  └──────────────┴──────────────────────────────────┘  │
└────────────────────────────────────────────────────────┘
```

### STAFF (không có sidebar):
```
┌────────────────────────────────────────────────────────┐
│  Nhân viên Demo (Nhân viên)         [Đăng xuất]        │
│  ┌──────────────────────────────────────────────────┐  │
│  │                                                   │  │
│  │              SalesPanel (toàn màn hình)          │  │
│  │                                                   │  │
│  └──────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────┘
```

## 🔍 Kiểm tra Role trong code

Code phân quyền trong `UserSession.java`:
```java
public static boolean isManager() {
    if (currentUser == null) return false;
    String role = currentUser.getRole();
    return role != null && 
           (role.equalsIgnoreCase("admin") || 
            role.equalsIgnoreCase("manager"));
}

public static boolean isStaff() {
    return !isManager();
}
```

**Quy tắc:**
- Role = "admin" hoặc "manager" → **isManager() = true** → Hiện sidebar
- Role = bất kỳ giá trị khác → **isStaff() = true** → Ẩn sidebar

## 📝 Checklist

- [ ] Database đã có bảng `taikhoan`
- [ ] Có ít nhất 1 tài khoản với Role = "admin" hoặc "manager"
- [ ] Compile thành công
- [ ] Chạy Main.java
- [ ] Màn hình LoginFrame hiện ra
- [ ] Đăng nhập với tài khoản admin
- [ ] **Thấy MainFrame với sidebar bên trái** ✅

## 🐛 Troubleshooting

### Không thấy sidebar?
1. Kiểm tra Role trong database: `SELECT Role FROM taikhoan WHERE Username = 'your_username'`
2. Đảm bảo Role = "admin" hoặc "manager" (không phân biệt hoa thường)
3. Kiểm tra console có lỗi gì không

### Lỗi kết nối database?
1. Kiểm tra `DatabaseConnection.java`
2. Đảm bảo SQL Server đang chạy
3. Kiểm tra connection string
