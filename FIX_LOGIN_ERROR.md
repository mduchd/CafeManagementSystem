# ❌ LỖI: Không đăng nhập được

## 🔍 Nguyên nhân

**THIẾU MySQL Connector JAR** - Thư viện để Java kết nối với MySQL

## ✅ Cách sửa

### Bước 1: Tải MySQL Connector

**Cách 1: Tải từ trang chính thức**
1. Truy cập: https://dev.mysql.com/downloads/connector/j/
2. Chọn "Platform Independent"
3. Tải file ZIP
4. Giải nén và lấy file `mysql-connector-j-x.x.x.jar`

**Cách 2: Tải trực tiếp (nhanh hơn)**
- Link trực tiếp: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar

### Bước 2: Thêm JAR vào project

**Nếu dùng NetBeans:**
1. Tạo thư mục `lib` trong project: `d:\CafeManagementSystem\lib`
2. Copy file `mysql-connector-j-x.x.x.jar` vào thư mục `lib`
3. Trong NetBeans:
   - Right-click vào project → Properties
   - Chọn Libraries
   - Click "Add JAR/Folder"
   - Chọn file JAR vừa copy
   - Click OK

**Nếu compile bằng command line:**
1. Tạo thư mục `lib`:
   ```bash
   mkdir lib
   ```

2. Copy file JAR vào thư mục `lib`

3. Compile với classpath:
   ```bash
   javac -encoding UTF-8 -cp "lib/*" -d build -sourcepath src src\com\cafe\main\Main.java
   ```

4. Chạy với classpath:
   ```bash
   java -cp "build;lib/*" com.cafe.main.Main
   ```

### Bước 3: Kiểm tra lại

Sau khi thêm JAR, chạy test:
```bash
javac -encoding UTF-8 -cp "lib/*" -d build -sourcepath src src\com\cafe\main\TestLogin.java
java -cp "build;lib/*" com.cafe.main.TestLogin
```

Nếu thành công, bạn sẽ thấy:
```
✅ Kết nối database thành công!
✅ Bảng 'taikhoan' tồn tại!
✅ Tìm thấy X tài khoản
✅ ĐĂNG NHẬP THÀNH CÔNG!
```

### Bước 4: Chạy ứng dụng

```bash
java -cp "build;lib/*" com.cafe.main.Main
```

## 📋 Checklist

- [ ] XAMPP MySQL đang chạy
- [ ] Database `CafeDB` đã tạo
- [ ] Bảng `taikhoan` đã tạo với cột: Username, Password, Role, TenHienThi
- [ ] Có tài khoản admin trong database
- [ ] **MySQL Connector JAR đã thêm vào project** ← QUAN TRỌNG!
- [ ] Compile với `-cp "lib/*"`
- [ ] Chạy với `-cp "build;lib/*"`

## 🗄️ SQL để tạo database và tài khoản

```sql
-- 1. Tạo database
CREATE DATABASE IF NOT EXISTS CafeDB;
USE CafeDB;

-- 2. Tạo bảng taikhoan
CREATE TABLE IF NOT EXISTS taikhoan (
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(20) NOT NULL,
    TenHienThi VARCHAR(100)
);

-- 3. Thêm tài khoản admin
INSERT INTO taikhoan (Username, Password, Role, TenHienThi) 
VALUES ('admin', '123', 'admin', 'Quản lý');

-- 4. Thêm tài khoản nhân viên (optional)
INSERT INTO taikhoan (Username, Password, Role, TenHienThi) 
VALUES ('nhanvien', '123', 'staff', 'Nhân viên Demo');

-- 5. Kiểm tra
SELECT * FROM taikhoan;
```

## 🎯 Sau khi sửa xong

1. Chạy `TestLogin.java` để kiểm tra kết nối
2. Nếu OK, chạy `Main.java`
3. Đăng nhập với admin/123
4. Sẽ thấy MainFrame với sidebar đầy đủ! ✅
