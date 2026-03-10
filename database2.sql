-- 1. Tạo bảng Phân Quyền (phanquyen) dựa trên cấu trúc của PhanQuyenDTO

CREATE TABLE `phanquyen` (

  `MaQuyen` varchar(50) NOT NULL,

  `TenQuyen` varchar(100) NOT NULL,

  `QLKhachHang` int(11) DEFAULT 0,

  `QLNhanVien` int(11) DEFAULT 0,

  `QLSanPham` int(11) DEFAULT 0,

  `QLNhapHang` int(11) DEFAULT 0,

  `QLBanHang` int(11) DEFAULT 0,

  `QLNhaCungCap` int(11) DEFAULT 0,

  `QLThongKe` int(11) DEFAULT 0,

  `QLPhanQuyen` int(11) DEFAULT 0,

  PRIMARY KEY (`MaQuyen`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- 2. Đổ dữ liệu mẫu cho bảng Phân Quyền (3 nhóm quyền cơ bản)

INSERT INTO `phanquyen` (`MaQuyen`, `TenQuyen`, `QLKhachHang`, `QLNhanVien`, `QLSanPham`, `QLNhapHang`, `QLBanHang`, `QLNhaCungCap`, `QLThongKe`, `QLPhanQuyen`) VALUES

('ADMIN', 'Quản trị viên', 1, 1, 1, 1, 1, 1, 1, 1),

('KHO', 'Quản lý kho', 0, 0, 1, 1, 0, 1, 1, 0),

('STAFF', 'Nhân viên bán hàng', 1, 0, 0, 0, 1, 0, 0, 0),

('SECURITY', 'Bảo vệ', 0, 0, 0, 0, 1, 0, 0, 0);



-- 3. Đổi tên cột 'Quyen' thành 'MaQuyen' trong bảng Tài Khoản

ALTER TABLE `taikhoan` CHANGE `Quyen` `MaQuyen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL;



-- 4. Thêm khóa ngoại kết nối bảng Tài Khoản và Phân Quyền

ALTER TABLE `taikhoan` ADD CONSTRAINT `taikhoan_ibfk_2` FOREIGN KEY (`MaQuyen`) REFERENCES `phanquyen` (`MaQuyen`) ON DELETE SET NULL;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;

-- 1. Bổ sung Khách hàng mẫu
INSERT INTO `khachhang` (`MaKH`, `HoKH`, `TenKH`, `DChi`, `Email`, `SDT`, `TrangThai`) VALUES
('KH002', 'Nguyễn Minh', 'Anh', 'Quận 7, TP.HCM', 'minhanh@gmail.com', '0901222333', 1),
('KH003', 'Hoàng Thanh', 'Tùng', 'Hoàn Kiếm, Hà Nội', 'tunght@yahoo.com', '0988777666', 1),
('KH004', 'Phạm Thị', 'Mai', 'Ninh Kiều, Cần Thơ', 'maipham@gmail.com', '0933444555', 1);

-- 2. Bổ sung Nhân viên & Chi tiết nhân viên
INSERT INTO `nhanvien` (`MaNV`, `HoNV`, `TenNV`, `ChucVu`, `TrangThai`) VALUES
('NV003', 'Lê Minh', 'Tuấn', 'Thủ kho', 1),
('NV004', 'Võ Thị', 'Lan', 'Bảo vệ', 1);

INSERT INTO `chitietnhanvien` (`MaNV`, `NgSinh`, `GioiTinh`, `SDT`, `Email`, `DiaChi`, `CCCD`, `NgayVaoLam`, `LuongCoBan`) VALUES
('NV003', '1992-11-12', 'Nam', '0901112223', 'tuan.lm@bookstore.com', 'Quận 12, HCM', '079123456789', '2023-08-01', 9000000.00),
('NV004', '1988-04-05', 'Nữ', '0905556667', 'lan.vt@bookstore.com', 'Hóc Môn, HCM', '079987654321', '2024-01-10', 6500000.00);

-- 3. Cấp Tài khoản cho nhân viên mới (liên kết với bảng phanquyen)
INSERT INTO `taikhoan` (`TenDangNhap`, `MatKhau`, `MaNV`, `MaQuyen`, `TrangThai`) VALUES
('nv_tuan', '123456', 'NV003', 'KHO', 1),
('nv_lan', '123456', 'NV004', 'SECURITY', 1);

-- 4. Bổ sung Sản phẩm (Sách)
INSERT INTO `sanpham` (`MaSP`, `Ten`, `MaTG`, `MaTL`, `MaNXB`, `MaNCC`, `SLTonKho`, `DonViTinh`, `DonGia`, `NamXB`, `MoTa`, `TrangThai`) VALUES
('SP004', 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 'TG001', 'TL001', 'NXB001', 'NCC001', 30, 'Cuốn', 85000.00, 2018, 'Ký ức tuổi thơ đầy xúc động', 1),
('SP005', 'Quẳng Gánh Lo Đi Và Vui Sống', 'TG003', 'TL003', 'NXB001', 'NCC002', 45, 'Cuốn', 92000.00, 2020, 'Sách truyền cảm hứng sống tích cực', 1);

-- Liên kết Tác giả và Thể loại cho sản phẩm mới
INSERT INTO `sanpham_tacgia` (`MaSP`, `MaTG`) VALUES ('SP004', 'TG001'), ('SP005', 'TG003');
INSERT INTO `sanpham_theloai` (`MaSP`, `MaTL`) VALUES ('SP004', 'TL001'), ('SP005', 'TL003');

-- 5. Tạo một Phiếu nhập hàng mẫu (Nhập thêm sách SP001 và SP002)
INSERT INTO `phieunhaphang` (`MaPhieuNhap`, `NgayNhap`, `MaNV`, `MaNCC`, `TongTienNhap`) VALUES
('PN001', '2024-03-01 09:00:00', 'NV003', 'NCC001', 2500000.00);

INSERT INTO `ctphieunhaphang` (`MaPhieuNhap`, `MaSP`, `SL`, `DonGia`, `ThanhTien`) VALUES
('PN001', 'SP001', 20, 80000.00, 1600000.00),
('PN001', 'SP002', 15, 60000.00, 900000.00);

-- 6. Tạo một Hóa đơn mẫu (Bán hàng cho KH001)
INSERT INTO `hoadon` (`MaHD`, `ThoiGian`, `MaNV`, `MaKH`, `MaKM`, `TongTienTruocKM`, `GiamGiaHD`, `TongTienThanhToan`) VALUES
('HD001', '2024-03-05 14:30:00', 'NV002', 'KH001', NULL, 165000.00, 0.00, 165000.00);

INSERT INTO `cthoadon` (`MaHD`, `MaSP`, `SoLuong`, `DonGiaBan`, `ThanhTien`) VALUES
('HD001', 'SP001', 1, 110000.00, 110000.00),
('HD001', 'SP002', 1, 55000.00, 55000.00);

COMMIT;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;

-- --------------------------------------------------------
-- 1. THÊM KHÁCH HÀNG VÀ SẢN PHẨM MỚI
-- --------------------------------------------------------

INSERT INTO `khachhang` (`MaKH`, `HoKH`, `TenKH`, `DChi`, `Email`, `SDT`, `TrangThai`) VALUES
('KH005', 'Phan Văn', 'Trị', 'Quận 5, TP.HCM', 'triphan@gmail.com', '0944555666', 1),
('KH006', 'Trịnh Công', 'Sơn', 'Quận 1, TP.HCM', 'sontc@gmail.com', '0911222888', 1);

INSERT INTO `sanpham` (`MaSP`, `Ten`, `MaTG`, `MaTL`, `MaNXB`, `MaNCC`, `SLTonKho`, `DonViTinh`, `DonGia`, `NamXB`, `MoTa`, `TrangThai`) VALUES
('SP006', 'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 'TG001', 'TL001', 'NXB001', 'NCC001', 80, 'Cuốn', 125000.00, 2022, 'Truyện dài của Nguyễn Nhật Ánh', 1),
('SP007', 'Doraemon tập 1', 'TG002', 'TL002', 'NXB002', 'NCC002', 200, 'Cuốn', 25000.00, 2023, 'Manga kinh điển', 1);

-- Cập nhật bảng trung gian cho sản phẩm mới
INSERT INTO `sanpham_tacgia` (`MaSP`, `MaTG`) VALUES ('SP006', 'TG001'), ('SP007', 'TG002');
INSERT INTO `sanpham_theloai` (`MaSP`, `MaTL`) VALUES ('SP006', 'TL001'), ('SP007', 'TL002');

-- --------------------------------------------------------
-- 2. THÊM PHIẾU NHẬP HÀNG (Dữ liệu đầu vào để tính chi phí)
-- --------------------------------------------------------

INSERT INTO `phieunhaphang` (`MaPhieuNhap`, `NgayNhap`, `MaNV`, `MaNCC`, `TongTienNhap`) VALUES
('PN002', '2026-02-10 10:00:00', 'NV003', 'NCC001', 4000000.00),
('PN003', '2026-02-15 15:30:00', 'NV003', 'NCC002', 1500000.00);

INSERT INTO `ctphieunhaphang` (`MaPhieuNhap`, `MaSP`, `SL`, `DonGia`, `ThanhTien`) VALUES
('PN002', 'SP006', 40, 100000.00, 4000000.00),
('PN003', 'SP007', 100, 15000.00, 1500000.00);

-- --------------------------------------------------------
-- 3. THÊM HÓA ĐƠN BÁN HÀNG (Để thống kê Doanh thu)
-- --------------------------------------------------------

-- Hóa đơn 002: Bán trong tháng 2
INSERT INTO `hoadon` (`MaHD`, `ThoiGian`, `MaNV`, `MaKH`, `MaKM`, `TongTienTruocKM`, `GiamGiaHD`, `TongTienThanhToan`) VALUES
('HD002', '2026-02-20 08:15:00', 'NV002', 'KH005', NULL, 375000.00, 0.00, 375000.00);

INSERT INTO `cthoadon` (`MaHD`, `MaSP`, `SoLuong`, `DonGiaBan`, `ThanhTien`) VALUES
('HD002', 'SP006', 3, 125000.00, 375000.00);

-- Hóa đơn 003: Bán đầu tháng 3 (Có áp dụng khuyến mãi nếu muốn)
INSERT INTO `hoadon` (`MaHD`, `ThoiGian`, `MaNV`, `MaKH`, `MaKM`, `TongTienTruocKM`, `GiamGiaHD`, `TongTienThanhToan`) VALUES
('HD003', '2026-03-01 19:45:00', 'NV002', 'KH006', NULL, 300000.00, 0.00, 300000.00);

INSERT INTO `cthoadon` (`MaHD`, `MaSP`, `SoLuong`, `DonGiaBan`, `ThanhTien`) VALUES
('HD003', 'SP006', 2, 125000.00, 250000.00),
('HD003', 'SP007', 2, 25000.00, 50000.00);

COMMIT;