# Hướng dẫn Quản lý Tài khoản - Cafe Management System

## 📋 Tổng quan

Hệ thống quản lý tài khoản với phân quyền rõ ràng:
- **Admin/Manager**: Có quyền đổi mật khẩu
- **Nhân viên/Staff**: Không có quyền đổi mật khẩu

---

## 🔐 Tài khoản mẫu

### Admin
- **Username**: `admin`
- **Password**: `123`
- **Quyền**: Quản lý (có nút "Đổi mật khẩu")

### Nhân viên
- **Username**: `nhanvien`
- **Password**: `123`
- **Quyền**: Nhân viên (không có nút "Đổi mật khẩu")

---

## 🎯 Cách sử dụng

### 1. Đăng nhập với tài khoản Admin
```
Username: admin
Password: 123
```

### 2. Tìm nút "Đổi mật khẩu"
- Sau khi đăng nhập, nhìn vào **sidebar bên trái**
- Kéo xuống **cuối sidebar**
- Bạn sẽ thấy:
  - Tên người dùng
  - Vai trò (Quản lý)
  - **Nút "Đổi mật khẩu"** (màu xanh dương)
  - Nút "Đăng xuất" (màu đỏ)

### 3. Đổi mật khẩu
1. Click nút **"Đổi mật khẩu"**
2. Nhập:
   - **Mật khẩu hiện tại**: Mật khẩu cũ của bạn
   - **Mật khẩu mới**: Mật khẩu mới (tối thiểu 3 ký tự)
   - **Nhập lại mật khẩu**: Nhập lại mật khẩu mới
3. Click **"Thay đổi mật khẩu"**

### 4. Validation
Hệ thống sẽ kiểm tra:
- ✅ Tất cả các trường đã được điền
- ✅ Mật khẩu mới có ít nhất 3 ký tự
- ✅ Mật khẩu mới và xác nhận khớp nhau
- ✅ Mật khẩu hiện tại đúng

---

## 🚫 Phân quyền

### Admin/Manager
- ✅ Có nút "Đổi mật khẩu" ở sidebar
- ✅ Có thể đổi mật khẩu của chính mình
- ✅ Truy cập tất cả các menu

### Nhân viên/Staff
- ❌ **KHÔNG** có nút "Đổi mật khẩu"
- ❌ Không thể đổi mật khẩu
- ✅ Chỉ truy cập được màn hình "Bán hàng"

---

## 🔧 Cấu trúc Code

### 1. ChangePassDialog.java
- Giao diện đổi mật khẩu
- Validation đầy đủ
- Kết nối với `AuthDAO` để kiểm tra và cập nhật

### 2. MainFrame.java
- Thêm nút "Đổi mật khẩu" trong `setupRoleIndicatorPanel()`
- Chỉ hiện nút khi `UserSession.isManager() == true`
- Mở `ChangePassDialog` khi click

### 3. AuthDAO.java
- `getPasswordByUserName()`: Lấy mật khẩu hiện tại
- `updatePassword()`: Cập nhật mật khẩu mới

### 4. database.sql
- Bảng `TaiKhoan` với các trường:
  - `Username` (PRIMARY KEY)
  - `Password`
  - `Role` ('Admin' hoặc 'NhanVien')
  - `TenHienThi`

---

## 📝 Lưu ý

1. **Bảo mật**: Mật khẩu hiện tại được lưu dạng plain text (nên mã hóa trong production)
2. **Phân quyền**: Chỉ Admin mới thấy và sử dụng được chức năng đổi mật khẩu
3. **Tạo tài khoản mới**: Hiện tại chỉ có thể tạo bằng cách INSERT trực tiếp vào database
4. **Quên mật khẩu**: Cần Admin reset trực tiếp trong database

---

## 🎨 Giao diện

### Sidebar Admin (có nút Đổi mật khẩu)
```
┌─────────────────┐
│   Java Coffee   │
├─────────────────┤
│   Bán hàng      │
│   Bàn           │
│   Sản phẩm      │
│   Kho           │
│   Thống kê      │
│   Nhân viên     │
├─────────────────┤
│   Quản Lý       │
│   (Quản lý)     │
├─────────────────┤
│ Đổi mật khẩu    │ ← Chỉ Admin mới có
├─────────────────┤
│   Đăng xuất     │
└─────────────────┘
```

### Nhân viên (KHÔNG có nút Đổi mật khẩu)
```
┌──────────────────────────┐
│  Nhân Viên A (Nhân viên) │
│      [Đăng xuất]         │
└──────────────────────────┘
│                          │
│   Màn hình Bán hàng      │
│                          │
```

---

## ✅ Checklist Hoàn thành

- [x] Tạo `ChangePassDialog` với validation đầy đủ
- [x] Thêm nút "Đổi mật khẩu" vào `MainFrame`
- [x] Phân quyền: Chỉ Admin mới thấy nút
- [x] Kết nối với `AuthDAO` để kiểm tra và cập nhật
- [x] Thêm tài khoản mẫu vào database
- [x] Tạo tài liệu hướng dẫn

---

**Tác giả**: AI Assistant  
**Ngày tạo**: 2025-12-28
