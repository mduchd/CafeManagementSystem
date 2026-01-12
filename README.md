# Cafe Management System

He thong quan ly quan Cafe (Desktop Application) duoc xay dung bang Java Swing, ap dung kien truc 3-Tier ket hop Service Layer.

## Muc luc
- [Gioi thieu](#gioi-thieu)
- [Kien truc](#kien-truc)
- [Cau truc du an](#cau-truc-du-an)
- [Cong nghe su dung](#cong-nghe-su-dung)
- [Huong dan cai dat](#huong-dan-cai-dat)
- [Thanh vien nhom](#thanh-vien-nhom)

## Gioi thieu

Du an cung cap giai phap quan ly toan dien cho quan Cafe:

- **Ban hang (POS):** Goi mon, tao hoa don, tinh tien
- **Kho:** Quan ly nhap/xuat kho, theo doi ton kho
- **Thong ke:** Doanh thu, mon ban chay
- **Nhan su:** Quan ly nhan vien, phan quyen
- **Xac thuc:** Dang nhap, doi mat khau

## Kien truc

Du an ap dung kien truc 3-Tier + Service Layer:

```
View (Java Swing UI)
        |
Service (Business Logic)
        |
DAO (SQL, JDBC)
        |
Database (MySQL)
```

**Nguyen tac:**
- Khong viet SQL trong View
- Khong xu ly UI trong Service
- Khong dat nghiep vu trong DAO

## Cau truc du an

```
CafeManagementSystem/
+-- src/
|   +-- database/
|   |   +-- database.sql
|   +-- icon/
|   +-- com/cafe/
|       +-- config/
|       +-- model/
|       +-- dao/
|       +-- service/
|       +-- view/
|           +-- main/
|           +-- login/
|           +-- sales/
|           +-- product/
|           +-- warehouse/
|           +-- statistics/
|           +-- employee/
+-- lib/
|   +-- mysql-connector-java-8.0.27.jar
+-- README.md
```

## Cong nghe su dung

| Thanh phan | Cong nghe |
|------------|-----------|
| Ngon ngu | Java (JDK 8+) |
| Giao dien | Java Swing |
| Database | MySQL |
| Ket noi | JDBC |
| IDE | NetBeans |

## Huong dan cai dat

### Buoc 1: Chuan bi Database

1. Cai dat XAMPP
2. Start Apache va MySQL
3. Truy cap: http://localhost/phpmyadmin
4. Tao database: cafedb
5. Import file: src/database/database.sql

### Buoc 2: Clone project

```bash
git clone https://github.com/mduchd/CafeManagementSystem.git
```

### Buoc 3: Mo project

1. Mo NetBeans
2. Chon Open Project
3. Tro den thu muc vua clone

### Buoc 4: Them JDBC Driver

1. Chuot phai Libraries
2. Chon Add JAR/Folder
3. Them file lib/mysql-connector-java-8.0.27.jar

### Buoc 5: Chay chuong trinh

1. Mo src/com/cafe/main/Main.java
2. Nhan Shift + F6

**Tai khoan mac dinh:**
- Username: admin
- Password: 123

## Thanh vien nhom

| Ho ten | Vai tro |
|--------|---------|
| Nguyen Minh Duc | Quan ly ban hang, dang nhap |
| Nguyen Thuy Hien | Quan ly san pham |
| Le Tien Quan | Bao cao va thong ke |
| Le Thi Diu | Quan ly kho |
| Vu Ngoc Son | Quan ly nhan su, quan ly ban |