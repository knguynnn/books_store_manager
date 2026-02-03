# 📚 BOOKS STORE MANAGER - HỆ THỐNG QUẢN LÝ CỬA HÀNG BÁN SÁCH

Đồ án môn Lập trình Java - Hệ thống quản lý toàn diện từ kho hàng, nhân viên đến hóa đơn bán hàng và khuyến mãi.

## 📖 Giới thiệu dự án
[cite_start]Dự án được xây dựng trên mô hình **3-Layer Architecture** (Presentation - Business Logic - Data Access) giúp quản lý cửa hàng sách một cách tối ưu.

- [cite_start]**Tên đồ án:** Quản lý cửa hàng sách.
- [cite_start]**Công nghệ:** Java Swing, JDBC, MySQL[cite: 1, 4].
- [cite_start]**Kiến trúc:** BUS (Business Logic), DAO (Data Access), DTO (Data Transfer Object).

## 🛠 Cấu hình hệ thống
[cite_start]Hệ thống sử dụng các thông số kết nối sau (theo file `db.properties` ):
- [cite_start]**Driver:** MySQL Connector J.
- [cite_start]**URL:** `jdbc:mysql://localhost:3306/quanlybansach`.
- [cite_start]**User:** `root`.
- [cite_start]**Password:** (Trống - mặc định XAMPP).

## 🚀 Các tính năng chính
[cite_start]Hệ thống bao gồm các phân hệ quản lý quan trọng[cite: 1, 3]:
- [cite_start]**Bán hàng & Khách hàng:** Lập hóa đơn, chi tiết hóa đơn, quản lý thông tin khách hàng[cite: 1, 3].
- [cite_start]**Sản phẩm & Danh mục:** Quản lý sách, nhà xuất bản, tác giả, thể loại[cite: 1, 3].
- [cite_start]**Nhân viên & Tài khoản:** Quản lý hồ sơ nhân viên, phân quyền truy cập hệ thống.
- [cite_start]**Nhập hàng:** Quản lý nhà cung cấp và phiếu nhập kho[cite: 1, 3].
- [cite_start]**Khuyến mãi:** Thiết lập các chương trình giảm giá cho sản phẩm và hóa đơn.

## 🔑 Tài khoản đăng nhập mẫu
[cite_start]Bạn có thể sử dụng các tài khoản sau để test hệ thống (dữ liệu từ `database.sql` ):
| Quyền | Tên đăng nhập | Mật khẩu |
| :--- | :--- | :--- |
| **Admin** | `admin` | `123456` |
| **Nhân viên** | `nv_hoa` | `123456` |

## 📂 Cấu trúc thư mục
- [cite_start]`/src/main/java/Backend`: Chứa logic xử lý (BUS, DAO, DTO).
- [cite_start]`/src/main/java/Frontend`: Chứa giao diện người dùng (GUI, Component).
- [cite_start]`/database`: Chứa file script `database.sql` để khởi tạo hệ thống.
- [cite_start]`/.doc`: Tài liệu báo cáo chi tiết dự án.

## 🛠 Hướng dẫn cài đặt
1. Clone dự án: `git clone https://github.com/knguynnn/books_store_manager.git`.
2. [cite_start]Import database: Sử dụng file `database.sql` trong thư mục `/sql` hoặc `/database` và import vào **phpMyAdmin**.
3. Mở project trong IntelliJ/Eclipse, đợi Maven tải các dependencies trong `pom.xml`.
4. Chạy file `App.java` để bắt đầu.
