-- ========================================================
-- DATABASE3.SQL - DỮ LIỆU BỔ SUNG CHO HỆ THỐNG QUẢN LÝ BÁN SÁCH
-- Thêm: 10 NCC, 10 NXB, 10 Tác giả, 20 Thể loại, 17 Sản phẩm,
--        Phiếu nhập & Chi tiết phiếu nhập liên quan đến sản phẩm mới
-- ========================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
SET NAMES utf8mb4;

-- ========================================================
-- PHẦN 1: NHÀ CUNG CẤP (NCC004 → NCC010 | đã có NCC001, NCC002 + database2 thêm chưa có NCC003)
-- Thêm đủ để tổng = 10 NCC
-- ========================================================

INSERT INTO `ncc` (`MaNCC`, `TenNCC`, `SDT`, `DiaChi`, `Email`) VALUES
('NCC003', 'Alpha Books', '02422002468', '15 Ngô Quyền, Hoàn Kiếm, HN', 'contact@alphabooks.vn'),
('NCC004', 'Đinh Tị Books', '02839301009', '31 Hoàng Diệu, Q4, TP.HCM', 'info@dinhthibooks.vn'),
('NCC005', 'Thái Hà Books', '02462917979', 'Tầng 5, 20A Nguyễn Hữu Huân, HN', 'info@thaihabooks.vn'),
('NCC006', 'Nhà sách Cá Chép', '02838124563', '119 Pasteur, Q3, TP.HCM', 'cachepcorp@gmail.com'),
('NCC007', 'Minh Long Book', '02838294730', '65 Trần Nhân Tôn, Q5, TP.HCM', 'minhlongbook@gmail.com'),
('NCC008', 'Tân Việt Book', '02838126040', '12 Đinh Lễ, Hoàn Kiếm, HN', 'tanvietbook@gmail.com'),
('NCC009', 'Sbooks Vietnam', '02836368080', '284 Lý Thường Kiệt, Q10, TP.HCM', 'info@sbooks.vn'),
('NCC010', 'BookCity', '02838233236', '702 Sư Vạn Hạnh, Q10, TP.HCM', 'support@bookcity.vn');

-- ========================================================
-- PHẦN 2: NHÀ XUẤT BẢN (NXB003 → NXB010 | đã có NXB001, NXB002)
-- Thêm đủ để tổng = 10 NXB
-- ========================================================

INSERT INTO `nxb` (`MaNXB`, `TenNXB`, `SDT`, `DiaChi`, `Email`) VALUES
('NXB003', 'NXB Tổng hợp TP.HCM', '02838225340', '62 Nguyễn Thị Minh Khai, Q3, HCM', 'info@nxbhcm.com.vn'),
('NXB004', 'NXB Hội Nhà Văn', '02438253841', '65 Nguyễn Du, Hai Bà Trưng, HN', 'nxbhoinvn@gmail.com'),
('NXB005', 'NXB Dân Trí', '02437335283', '16 Hàng Chuối, Hai Bà Trưng, HN', 'info@nxbdantri.vn'),
('NXB006', 'NXB Lao Động', '02438515380', '175 Giảng Võ, Ba Đình, HN', 'nxbld@laodong.com.vn'),
('NXB007', 'NXB Văn Học', '02438253303', '18 Nguyễn Trường Tộ, Ba Đình, HN', 'nxbvanhoc@gmail.com'),
('NXB008', 'NXB Phụ Nữ Việt Nam', '02438222228', '39 Hàng Chuối, Hai Bà Trưng, HN', 'nxbpnvn@gmail.com'),
('NXB009', 'NXB Đại học Quốc gia HCM', '02837244555', 'Phòng 501, Nhà Điều hành, ĐHQG, HCM', 'dhqghcm@vnuhcm.edu.vn'),
('NXB010', 'NXB Giáo Dục Việt Nam', '02438214589', '81 Trần Hưng Đạo, Hoàn Kiếm, HN', 'info@nxbgd.vn');

-- ========================================================
-- PHẦN 3: TÁC GIẢ (TG004 → TG010 | đã có TG001, TG002, TG003)
-- Thêm đủ để tổng = 10 tác giả
-- ========================================================

INSERT INTO `tacgia` (`MaTG`, `HoTG`, `TenTG`) VALUES
('TG004', 'Nam', 'Quốc'),
('TG005', 'Haruki', 'Murakami'),
('TG006', 'Paulo', 'Coelho'),
('TG007', 'Nguyễn Du', ''),
('TG008', 'Tô Hoài', ''),
('TG009', 'Stephen', 'King'),
('TG010', 'J.K.', 'Rowling');

-- ========================================================
-- PHẦN 4: THỂ LOẠI (TL004 → TL020 | đã có TL001, TL002, TL003)
-- Thêm đủ để tổng = 20 thể loại
-- ========================================================

INSERT INTO `theloai` (`MaTL`, `TenTL`) VALUES
('TL004', 'Tâm lý - Triết học'),
('TL005', 'Lịch sử - Địa lý'),
('TL006', 'Khoa học - Công nghệ'),
('TL007', 'Kinh tế - Kinh doanh'),
('TL008', 'Ngoại ngữ'),
('TL009', 'Manga - Comic'),
('TL010', 'Văn học nước ngoài'),
('TL011', 'Tiểu thuyết'),
('TL012', 'Truyện ngắn'),
('TL013', 'Thơ ca - Tản văn'),
('TL014', 'Hồi ký - Ký sự'),
('TL015', 'Giáo khoa - Tham khảo'),
('TL016', 'Nuôi dạy con'),
('TL017', 'Sức khỏe - Làm đẹp'),
('TL018', 'Nấu ăn'),
('TL019', 'Du lịch - Phiêu lưu'),
('TL020', 'Kinh dị - Hồi hộp');

-- ========================================================
-- PHẦN 5: 17 SẢN PHẨM MỚI (SP008 → SP024)
-- MaNCC thêm vào cột mới từ database2
-- ========================================================

INSERT INTO `sanpham` (`MaSP`, `HinhAnh`, `Ten`, `MaTG`, `MaTL`, `MaNXB`, `MaNCC`, `SLTonKho`, `DonViTinh`, `DonGia`, `PhanTram`, `NamXB`, `MoTa`, `TrangThai`) VALUES
('SP008', 'rung-na-uy.jpg',            'Rừng Na Uy',                    'TG005', 'TL010', 'NXB003', 'NCC003', 30,  'Cuốn', 135000.00, 10, 2022, 'Tiểu thuyết nổi tiếng của Haruki Murakami', 1),
('SP009', 'nha-gia-kim.jpg',           'Nhà Giả Kim',                   'TG006', 'TL010', 'NXB003', 'NCC003', 45,  'Cuốn', 120000.00, 10, 2021, 'Hành trình theo đuổi giấc mơ của Paulo Coelho', 1),
('SP010', 'truyen-kieu.jpg',           'Truyện Kiều',                   'TG007', 'TL013', 'NXB004', 'NCC004', 25,  'Cuốn',  95000.00,  5, 2020, 'Kiệt tác thơ chữ Nôm của Nguyễn Du', 1),
('SP011', 'so-do.jpg',                 'Số Đỏ',                         'TG004', 'TL001', 'NXB004', 'NCC004', 20,  'Cuốn',  89000.00, 10, 2019, 'Tiểu thuyết trào phúng kinh điển của Vũ Trọng Phụng', 1),
('SP012', 'vo-chong-a-phu.jpg',        'Vợ Chồng A Phủ',                'TG008', 'TL012', 'NXB007', 'NCC007', 18,  'Cuốn',  75000.00, 10, 2023, 'Tập truyện ngắn nổi tiếng của Tô Hoài', 1),
('SP013', 'it.jpg',                    'IT - Chương 1',                 'TG009', 'TL020', 'NXB006', 'NCC006', 15,  'Cuốn', 199000.00, 10, 2022, 'Kinh dị bậc thầy của Stephen King', 1),
('SP014', 'harry-potter-1.jpg',        'Harry Potter và Hòn đá Phù thủy','TG010', 'TL002', 'NXB005', 'NCC005', 50,  'Cuốn', 189000.00, 10, 2023, 'Tập 1 bộ truyện Harry Potter huyền thoại', 1),
('SP015', 'nghin-mat-troi-rang-ro.jpg','Ngàn Mặt Trời Rực Rỡ',         'TG006', 'TL011', 'NXB003', 'NCC003', 22,  'Cuốn', 145000.00, 10, 2022, 'Câu chuyện cảm động về phụ nữ Afghanistan', 1),
('SP016', 'think-and-grow-rich.jpg',   'Nghĩ Giàu Làm Giàu',           'TG004', 'TL007', 'NXB005', 'NCC005', 35,  'Cuốn', 165000.00, 15, 2021, 'Sách kinh điển về tư duy làm giàu', 1),
('SP017', 'sapiens.jpg',               'Sapiens: Lược sử loài người',   'TG005', 'TL005', 'NXB003', 'NCC009', 28,  'Cuốn', 249000.00, 10, 2022, 'Tác phẩm nổi tiếng của Yuval Noah Harari', 1),
('SP018', 'atomic-habits.jpg',         'Atomic Habits',                  'TG004', 'TL004', 'NXB006', 'NCC006', 40,  'Cuốn', 179000.00, 10, 2023, 'Thay đổi tí hon, hiệu quả bất ngờ của James Clear', 1),
('SP019', 'doraemon-tap2.jpg',         'Doraemon tập 2',                'TG002', 'TL009', 'NXB002', 'NCC008', 80,  'Cuốn',  22000.00, 10, 2023, 'Manga kinh điển - tập 2', 1),
('SP020', 'conan-tap1.jpg',            'Thám tử Conan tập 1',           'TG004', 'TL009', 'NXB002', 'NCC008', 60,  'Cuốn',  25000.00, 10, 2022, 'Manga thám tử nổi tiếng', 1),
('SP021', 'dau-bep-vu-tru.jpg',        'Đầu Bếp Vũ Trụ',               'TG004', 'TL018', 'NXB008', 'NCC007', 12,  'Cuốn',  98000.00, 10, 2023, 'Sách nấu ăn sáng tạo và thú vị', 1),
('SP022', 'khoa-hoc-ve-giac-ngu.jpg',  'Khoa Học Về Giấc Ngủ',         'TG005', 'TL017', 'NXB009', 'NCC009', 20,  'Cuốn', 155000.00, 10, 2023, 'Giải mã bí ẩn giấc ngủ của con người', 1),
('SP023', 'lich-su-viet-nam.jpg',      'Lịch Sử Việt Nam Bằng Tranh',  'TG007', 'TL005', 'NXB010', 'NCC010', 15,  'Cuốn', 210000.00,  5, 2022, 'Bộ sách lịch sử tranh cho mọi lứa tuổi', 1),
('SP024', 'tieng-anh-giao-tiep.jpg',   'Tiếng Anh Giao Tiếp Thực Hành','TG004', 'TL008', 'NXB010', 'NCC010', 35,  'Cuốn', 128000.00, 10, 2023, 'Sách học tiếng Anh giao tiếp hiệu quả', 1);

-- ========================================================
-- PHẦN 6: LIÊN KẾT BẢNG sanpham_tacgia (SP008 → SP024)
-- ========================================================

INSERT INTO `sanpham_tacgia` (`MaSP`, `MaTG`) VALUES
('SP008', 'TG005'),
('SP009', 'TG006'),
('SP010', 'TG007'),
('SP011', 'TG004'),
('SP012', 'TG008'),
('SP013', 'TG009'),
('SP014', 'TG010'),
('SP015', 'TG006'),
('SP016', 'TG004'),
('SP017', 'TG005'),
('SP018', 'TG004'),
('SP019', 'TG002'),
('SP020', 'TG004'),
('SP021', 'TG004'),
('SP022', 'TG005'),
('SP023', 'TG007'),
('SP024', 'TG004');

-- ========================================================
-- PHẦN 7: LIÊN KẾT BẢNG sanpham_theloai (SP008 → SP024)
-- ========================================================

INSERT INTO `sanpham_theloai` (`MaSP`, `MaTL`) VALUES
('SP008', 'TL010'),
('SP008', 'TL011'),
('SP009', 'TL010'),
('SP009', 'TL004'),
('SP010', 'TL013'),
('SP010', 'TL001'),
('SP011', 'TL001'),
('SP011', 'TL012'),
('SP012', 'TL012'),
('SP012', 'TL001'),
('SP013', 'TL020'),
('SP013', 'TL011'),
('SP014', 'TL002'),
('SP014', 'TL011'),
('SP015', 'TL011'),
('SP015', 'TL010'),
('SP016', 'TL007'),
('SP016', 'TL004'),
('SP017', 'TL005'),
('SP017', 'TL006'),
('SP018', 'TL004'),
('SP018', 'TL007'),
('SP019', 'TL009'),
('SP020', 'TL009'),
('SP021', 'TL018'),
('SP022', 'TL017'),
('SP023', 'TL005'),
('SP024', 'TL008');

-- ========================================================
-- PHẦN 8: PHIẾU NHẬP HÀNG (PN004 → PN008)
-- Mỗi phiếu nhập từ các NCC mới, nhập sản phẩm mới
-- ========================================================

INSERT INTO `phieunhaphang` (`MaPhieuNhap`, `NgayNhap`, `MaNV`, `MaNCC`, `TongTienNhap`) VALUES
('PN004', '2026-01-05 08:30:00', 'NV003', 'NCC003', 8100000.00),
('PN005', '2026-01-15 09:00:00', 'NV003', 'NCC004', 3320000.00),
('PN006', '2026-02-01 10:30:00', 'NV003', 'NCC005', 12700000.00),
('PN007', '2026-02-20 14:00:00', 'NV003', 'NCC008',  3340000.00),
('PN008', '2026-03-10 08:00:00', 'NV003', 'NCC009',  7770000.00);

-- ========================================================
-- PHẦN 9: CHI TIẾT PHIẾU NHẬP HÀNG
-- Liên kết trực tiếp với sản phẩm mới (SP008 → SP024)
-- ========================================================

-- PN004: nhập từ NCC003 (Alpha Books) - sách văn học nước ngoài
INSERT INTO `ctphieunhaphang` (`MaPhieuNhap`, `MaSP`, `SL`, `DonGia`, `ThanhTien`) VALUES
('PN004', 'SP008', 30, 100000.00, 3000000.00),  -- Rừng Na Uy
('PN004', 'SP009', 45,  90000.00, 4050000.00),  -- Nhà Giả Kim
('PN004', 'SP015', 15, 105000.00, 1575000.00);  -- Ngàn Mặt Trời Rực Rỡ (thêm lô bổ sung)

-- PN005: nhập từ NCC004 (Đinh Tị Books) - văn học Việt
INSERT INTO `ctphieunhaphang` (`MaPhieuNhap`, `MaSP`, `SL`, `DonGia`, `ThanhTien`) VALUES
('PN005', 'SP010', 25,  70000.00, 1750000.00),  -- Truyện Kiều
('PN005', 'SP011', 20,  65000.00, 1300000.00),  -- Số Đỏ
('PN005', 'SP012', 18,  55000.00,  990000.00);  -- Vợ Chồng A Phủ

-- PN006: nhập từ NCC005 (Thái Hà Books) - sách kỹ năng & trẻ em
INSERT INTO `ctphieunhaphang` (`MaPhieuNhap`, `MaSP`, `SL`, `DonGia`, `ThanhTien`) VALUES
('PN006', 'SP014', 50, 140000.00, 7000000.00),  -- Harry Potter
('PN006', 'SP016', 35, 125000.00, 4375000.00),  -- Nghĩ Giàu Làm Giàu
('PN006', 'SP018', 40, 133000.00, 5320000.00);  -- Atomic Habits

-- PN007: nhập từ NCC008 (BookCity) - manga
INSERT INTO `ctphieunhaphang` (`MaPhieuNhap`, `MaSP`, `SL`, `DonGia`, `ThanhTien`) VALUES
('PN007', 'SP019', 80,  16000.00, 1280000.00),  -- Doraemon tập 2
('PN007', 'SP020', 80,  18000.00, 1440000.00),  -- Conan tập 1
('PN007', 'SP013', 15, 148000.00, 2220000.00);  -- IT Chương 1

-- PN008: nhập từ NCC009 (Sbooks Vietnam) - sách khoa học & cuộc sống
INSERT INTO `ctphieunhaphang` (`MaPhieuNhap`, `MaSP`, `SL`, `DonGia`, `ThanhTien`) VALUES
('PN008', 'SP017', 28, 185000.00, 5180000.00),  -- Sapiens
('PN008', 'SP022', 20, 115000.00, 2300000.00),  -- Khoa Học Giấc Ngủ
('PN008', 'SP021', 12,  74000.00,  888000.00),  -- Đầu Bếp Vũ Trụ
('PN008', 'SP024', 35,  96000.00, 3360000.00);  -- Tiếng Anh Giao Tiếp

INSERT INTO `khuyenmai` (`MaKM`, `TenKM`, `NgayBatDau`, `NgayKetThuc`, `LoaiKM`, `GiaTri`, `DieuKien`, `TrangThai`) VALUES
('KM002', 'Khuyến mãi Tựu Trường', '2026-08-15 00:00:00', '2026-09-15 23:59:59', 'GIAM_HD', 15, 300000.00, 1),
('KM003', 'Siêu Sale Ngày Đôi 10/10', '2026-10-10 00:00:00', '2026-10-10 23:59:59', 'GIAM_HD', 20, 1000000.00, 1),
('KM004', 'Ưu đãi Black Friday', '2026-11-20 00:00:00', '2026-11-30 23:59:59', 'GIAM_HD', 25, 500000.00, 1),
('KM005', 'Mừng Giáng Sinh', '2026-12-20 00:00:00', '2026-12-26 23:59:59', 'GIAM_SP', 10, 0.00, 1),
('KM006', 'Lì xì Năm Mới', '2027-01-01 00:00:00', '2027-01-10 23:59:59', 'GIAM_HD', 50000, 500000.00, 1);

-- 2. THIẾT LẬP CHI TIẾT KHUYẾN MÃI CHO SẢN PHẨM (Áp dụng cho loại GIAM_SP)
-- Ví dụ: KM005 áp dụng cho các sản phẩm từ SP008 đến SP012
INSERT INTO `ctkhuyenmai_sanpham` (`MaKM`, `MaSP`) VALUES
('KM005', 'SP008'),
('KM005', 'SP009'),
('KM005', 'SP010'),
('KM005', 'SP011'),
('KM005', 'SP012');

COMMIT;