-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 03, 2026 lúc 02:02 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlybansach`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietnhanvien`
--

CREATE TABLE `chitietnhanvien` (
  `MaNV` varchar(50) NOT NULL,
  `NgSinh` date DEFAULT NULL,
  `GioiTinh` varchar(10) DEFAULT NULL,
  `SDT` varchar(20) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL,
  `CCCD` varchar(20) DEFAULT NULL,
  `NgayVaoLam` date DEFAULT NULL,
  `LuongCoBan` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietnhanvien`
--

INSERT INTO `chitietnhanvien` (`MaNV`, `NgSinh`, `GioiTinh`, `SDT`, `Email`, `DiaChi`, `CCCD`, `NgayVaoLam`, `LuongCoBan`) VALUES
('NV001', '1990-01-01', 'Nam', '0901234567', 'admin@bookstore.com', 'Quận 1, HCM', '123456789012', '2023-01-01', 15000000.00),
('NV002', '1995-05-20', 'Nữ', '0907654321', 'hoa.nt@bookstore.com', 'Quận 7, HCM', '987654321098', '2023-06-15', 7000000.00);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cthoadon`
--

CREATE TABLE `cthoadon` (
  `MaHD` varchar(50) NOT NULL,
  `MaSP` varchar(50) NOT NULL,
  `SoLuong` int(11) DEFAULT NULL,
  `DonGiaBan` decimal(18,2) DEFAULT NULL,
  `ThanhTien` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctkhuyenmai_sanpham`
--

CREATE TABLE `ctkhuyenmai_sanpham` (
  `MaKM` varchar(50) NOT NULL,
  `MaSP` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ctphieunhaphang`
--

CREATE TABLE `ctphieunhaphang` (
  `MaPhieuNhap` varchar(50) NOT NULL,
  `MaSP` varchar(50) NOT NULL,
  `SL` int(11) DEFAULT NULL,
  `DonGia` decimal(18,2) DEFAULT NULL,
  `ThanhTien` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `MaHD` varchar(50) NOT NULL,
  `ThoiGian` datetime DEFAULT current_timestamp(),
  `MaNV` varchar(50) DEFAULT NULL,
  `MaKH` varchar(50) DEFAULT NULL,
  `MaKM` varchar(50) DEFAULT NULL,
  `TongTienTruocKM` decimal(18,2) DEFAULT NULL,
  `GiamGiaHD` decimal(18,2) DEFAULT NULL,
  `TongTienThanhToan` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `MaKH` varchar(50) NOT NULL,
  `HoKH` varchar(100) DEFAULT NULL,
  `TenKH` varchar(100) DEFAULT NULL,
  `DChi` varchar(255) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `SDT` varchar(20) DEFAULT NULL,
  `TrangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`MaKH`, `HoKH`, `TenKH`, `DChi`, `Email`, `SDT`, `TrangThai`) VALUES
('KH001', 'Lê Văn', 'Tèo', 'Bình Thạnh, HCM', 'teo@gmail.com', '0912333444', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khuyenmai`
--

CREATE TABLE `khuyenmai` (
  `MaKM` varchar(50) NOT NULL,
  `TenKM` varchar(255) DEFAULT NULL,
  `NgayBatDau` datetime DEFAULT NULL,
  `NgayKetThuc` datetime DEFAULT NULL,
  `LoaiKM` varchar(50) DEFAULT NULL,
  `GiaTri` int(11) DEFAULT NULL,
  `DieuKien` decimal(18,2) DEFAULT NULL,
  `TrangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khuyenmai`
--

INSERT INTO `khuyenmai` (`MaKM`, `TenKM`, `NgayBatDau`, `NgayKetThuc`, `LoaiKM`, `GiaTri`, `DieuKien`, `TrangThai`) VALUES
('KM_HE', 'Chào Hè 2024', '2024-06-01 00:00:00', '2024-08-31 00:00:00', 'GIAM_HD', 10, 500000.00, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ncc`
--

CREATE TABLE `ncc` (
  `MaNCC` varchar(50) NOT NULL,
  `TenNCC` varchar(255) DEFAULT NULL,
  `SDT` varchar(20) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `ncc`
--

INSERT INTO `ncc` (`MaNCC`, `TenNCC`, `SDT`, `DiaChi`, `Email`) VALUES
('NCC001', 'Công ty Fahasa', '1900636467', 'Q1, TP.HCM', 'info@fahasa.com'),
('NCC002', 'Phuong Nam Book', '02838223244', 'Q10, TP.HCM', 'hotro@pnb.com.vn');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `MaNV` varchar(50) NOT NULL,
  `HoNV` varchar(100) DEFAULT NULL,
  `TenNV` varchar(100) DEFAULT NULL,
  `ChucVu` varchar(100) DEFAULT NULL,
  `TrangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`MaNV`, `HoNV`, `TenNV`, `ChucVu`, `TrangThai`) VALUES
('NV001', 'Trần Văn', 'Admin', 'Quản lý', 1),
('NV002', 'Nguyễn Thị', 'Hoa', 'Nhân viên bán hàng', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nxb`
--

CREATE TABLE `nxb` (
  `MaNXB` varchar(50) NOT NULL,
  `TenNXB` varchar(255) DEFAULT NULL,
  `SDT` varchar(20) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nxb`
--

INSERT INTO `nxb` (`MaNXB`, `TenNXB`, `SDT`, `DiaChi`, `Email`) VALUES
('NXB001', 'NXB Trẻ', '02839316289', '161 Lý Chính Thắng, Q3, HCM', 'hopthu@nxbtre.com.vn'),
('NXB002', 'NXB Kim Đồng', '02439434730', '55 Quang Trung, Hai Bà Trưng, HN', 'info@nxbkimdong.com.vn');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phieunhaphang`
--

CREATE TABLE `phieunhaphang` (
  `MaPhieuNhap` varchar(50) NOT NULL,
  `NgayNhap` datetime DEFAULT current_timestamp(),
  `MaNV` varchar(50) DEFAULT NULL,
  `MaNCC` varchar(50) DEFAULT NULL,
  `TongTienNhap` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `MaSP` varchar(50) NOT NULL,
  `Ten` varchar(255) DEFAULT NULL,
  `SLTonKho` int(11) DEFAULT 0,
  `DonViTinh` varchar(50) DEFAULT NULL,
  `DonGia` decimal(18,2) DEFAULT NULL,
  `NamXB` int(11) DEFAULT NULL,
  `MaNXB` varchar(50) DEFAULT NULL,
  `MaNCC` varchar(50) DEFAULT NULL,
  `MoTa` text DEFAULT NULL,
  `TrangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`MaSP`, `Ten`, `SLTonKho`, `DonViTinh`, `DonGia`, `NamXB`, `MaNXB`, `MaNCC`, `MoTa`, `TrangThai`) VALUES
('SP001', 'Mắt Biếc', 50, 'Cuốn', 110000.00, 2019, 'NXB001', 'NCC001', 'Tác phẩm nổi tiếng của Nguyễn Nhật Ánh', 1),
('SP002', 'Dế Mèn Phiêu Lưu Ký', 100, 'Cuốn', 55000.00, 2020, 'NXB002', 'NCC001', 'Truyện thiếu nhi kinh điển', 1),
('SP003', 'Đắc Nhân Tâm', 30, 'Cuốn', 86000.00, 2021, 'NXB001', 'NCC002', 'Sách kỹ năng hay nhất mọi thời đại', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham_tacgia`
--

CREATE TABLE `sanpham_tacgia` (
  `MaSP` varchar(50) NOT NULL,
  `MaTG` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham_tacgia`
--

INSERT INTO `sanpham_tacgia` (`MaSP`, `MaTG`) VALUES
('SP001', 'TG001'),
('SP002', 'TG002'),
('SP003', 'TG003');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham_theloai`
--

CREATE TABLE `sanpham_theloai` (
  `MaSP` varchar(50) NOT NULL,
  `MaTL` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham_theloai`
--

INSERT INTO `sanpham_theloai` (`MaSP`, `MaTL`) VALUES
('SP001', 'TL001'),
('SP002', 'TL002'),
('SP003', 'TL003');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tacgia`
--

CREATE TABLE `tacgia` (
  `MaTG` varchar(50) NOT NULL,
  `HoTG` varchar(100) DEFAULT NULL,
  `TenTG` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `tacgia`
--

INSERT INTO `tacgia` (`MaTG`, `HoTG`, `TenTG`) VALUES
('TG001', 'Nguyễn Nhật', 'Ánh'),
('TG002', 'Tô', 'Hoài'),
('TG003', 'Dale', 'Carnegie');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `TenDangNhap` varchar(100) NOT NULL,
  `MatKhau` varchar(255) DEFAULT NULL,
  `MaNV` varchar(50) DEFAULT NULL,
  `Quyen` varchar(50) DEFAULT NULL,
  `TrangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`TenDangNhap`, `MatKhau`, `MaNV`, `Quyen`, `TrangThai`) VALUES
('admin', '123456', 'NV001', 'ADMIN', 1),
('nv_hoa', '123456', 'NV002', 'STAFF', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `theloai`
--

CREATE TABLE `theloai` (
  `MaTL` varchar(50) NOT NULL,
  `TenTL` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `theloai`
--

INSERT INTO `theloai` (`MaTL`, `TenTL`) VALUES
('TL001', 'Văn học Việt Nam'),
('TL002', 'Thiếu nhi'),
('TL003', 'Kỹ năng sống');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitietnhanvien`
--
ALTER TABLE `chitietnhanvien`
  ADD PRIMARY KEY (`MaNV`);

--
-- Chỉ mục cho bảng `cthoadon`
--
ALTER TABLE `cthoadon`
  ADD PRIMARY KEY (`MaHD`,`MaSP`),
  ADD KEY `MaSP` (`MaSP`);

--
-- Chỉ mục cho bảng `ctkhuyenmai_sanpham`
--
ALTER TABLE `ctkhuyenmai_sanpham`
  ADD PRIMARY KEY (`MaKM`,`MaSP`),
  ADD KEY `MaSP` (`MaSP`);

--
-- Chỉ mục cho bảng `ctphieunhaphang`
--
ALTER TABLE `ctphieunhaphang`
  ADD PRIMARY KEY (`MaPhieuNhap`,`MaSP`),
  ADD KEY `MaSP` (`MaSP`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`MaHD`),
  ADD KEY `MaNV` (`MaNV`),
  ADD KEY `MaKH` (`MaKH`),
  ADD KEY `MaKM` (`MaKM`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MaKH`);

--
-- Chỉ mục cho bảng `khuyenmai`
--
ALTER TABLE `khuyenmai`
  ADD PRIMARY KEY (`MaKM`);

--
-- Chỉ mục cho bảng `ncc`
--
ALTER TABLE `ncc`
  ADD PRIMARY KEY (`MaNCC`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`MaNV`);

--
-- Chỉ mục cho bảng `nxb`
--
ALTER TABLE `nxb`
  ADD PRIMARY KEY (`MaNXB`);

--
-- Chỉ mục cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD PRIMARY KEY (`MaPhieuNhap`),
  ADD KEY `MaNV` (`MaNV`),
  ADD KEY `MaNCC` (`MaNCC`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`MaSP`),
  ADD KEY `MaNXB` (`MaNXB`),
  ADD KEY `MaNCC` (`MaNCC`);

--
-- Chỉ mục cho bảng `sanpham_tacgia`
--
ALTER TABLE `sanpham_tacgia`
  ADD PRIMARY KEY (`MaSP`,`MaTG`),
  ADD KEY `MaTG` (`MaTG`);

--
-- Chỉ mục cho bảng `sanpham_theloai`
--
ALTER TABLE `sanpham_theloai`
  ADD PRIMARY KEY (`MaSP`,`MaTL`),
  ADD KEY `MaTL` (`MaTL`);

--
-- Chỉ mục cho bảng `tacgia`
--
ALTER TABLE `tacgia`
  ADD PRIMARY KEY (`MaTG`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`TenDangNhap`),
  ADD UNIQUE KEY `MaNV` (`MaNV`);

--
-- Chỉ mục cho bảng `theloai`
--
ALTER TABLE `theloai`
  ADD PRIMARY KEY (`MaTL`);

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitietnhanvien`
--
ALTER TABLE `chitietnhanvien`
  ADD CONSTRAINT `chitietnhanvien_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `cthoadon`
--
ALTER TABLE `cthoadon`
  ADD CONSTRAINT `cthoadon_ibfk_1` FOREIGN KEY (`MaHD`) REFERENCES `hoadon` (`MaHD`) ON DELETE CASCADE,
  ADD CONSTRAINT `cthoadon_ibfk_2` FOREIGN KEY (`MaSP`) REFERENCES `sanpham` (`MaSP`);

--
-- Các ràng buộc cho bảng `ctkhuyenmai_sanpham`
--
ALTER TABLE `ctkhuyenmai_sanpham`
  ADD CONSTRAINT `ctkhuyenmai_sanpham_ibfk_1` FOREIGN KEY (`MaKM`) REFERENCES `khuyenmai` (`MaKM`) ON DELETE CASCADE,
  ADD CONSTRAINT `ctkhuyenmai_sanpham_ibfk_2` FOREIGN KEY (`MaSP`) REFERENCES `sanpham` (`MaSP`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `ctphieunhaphang`
--
ALTER TABLE `ctphieunhaphang`
  ADD CONSTRAINT `ctphieunhaphang_ibfk_1` FOREIGN KEY (`MaPhieuNhap`) REFERENCES `phieunhaphang` (`MaPhieuNhap`) ON DELETE CASCADE,
  ADD CONSTRAINT `ctphieunhaphang_ibfk_2` FOREIGN KEY (`MaSP`) REFERENCES `sanpham` (`MaSP`);

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  ADD CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`),
  ADD CONSTRAINT `hoadon_ibfk_3` FOREIGN KEY (`MaKM`) REFERENCES `khuyenmai` (`MaKM`);

--
-- Các ràng buộc cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD CONSTRAINT `phieunhaphang_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  ADD CONSTRAINT `phieunhaphang_ibfk_2` FOREIGN KEY (`MaNCC`) REFERENCES `ncc` (`MaNCC`);

--
-- Các ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `sanpham_ibfk_1` FOREIGN KEY (`MaNXB`) REFERENCES `nxb` (`MaNXB`),
  ADD CONSTRAINT `sanpham_ibfk_2` FOREIGN KEY (`MaNCC`) REFERENCES `ncc` (`MaNCC`);

--
-- Các ràng buộc cho bảng `sanpham_tacgia`
--
ALTER TABLE `sanpham_tacgia`
  ADD CONSTRAINT `sanpham_tacgia_ibfk_1` FOREIGN KEY (`MaSP`) REFERENCES `sanpham` (`MaSP`) ON DELETE CASCADE,
  ADD CONSTRAINT `sanpham_tacgia_ibfk_2` FOREIGN KEY (`MaTG`) REFERENCES `tacgia` (`MaTG`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `sanpham_theloai`
--
ALTER TABLE `sanpham_theloai`
  ADD CONSTRAINT `sanpham_theloai_ibfk_1` FOREIGN KEY (`MaSP`) REFERENCES `sanpham` (`MaSP`) ON DELETE CASCADE,
  ADD CONSTRAINT `sanpham_theloai_ibfk_2` FOREIGN KEY (`MaTL`) REFERENCES `theloai` (`MaTL`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD CONSTRAINT `taikhoan_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
