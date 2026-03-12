package Backend.DAO.ThongKe_PhanQuyen;

import Backend.DatabaseHelper;
import Backend.DTO.ThongKe_PhanQuyen.ThongKeDTO;
import java.sql.*;
import java.util.ArrayList;

public class ThongKeDAO {

    // =============================================================
    // 1. LẤY DỮ LIỆU TỔNG QUAN (CHO 5 THẺ DASHBOARD)
    // =============================================================
    public ThongKeDTO.TongQuan getTongQuan(String tuNgay, String denNgay) {
        ThongKeDTO.TongQuan data = new ThongKeDTO.TongQuan();

        try (Connection conn = DatabaseHelper.getConnection()) {
            // Sử dụng COALESCE để đảm bảo nếu không có dữ liệu sẽ trả về 0 thay vì NULL
            
            // Lấy Doanh thu, Số đơn hàng, Số khách hàng từ bảng hoadon
            String sqlHD = "SELECT COALESCE(SUM(TongTienThanhToan), 0) as DT, " +
                           "COUNT(MaHD) as SLHD, " +
                           "COUNT(DISTINCT MaKH) as SLKH " +
                           "FROM hoadon WHERE ThoiGian BETWEEN ? AND ?";
            PreparedStatement psHD = conn.prepareStatement(sqlHD);
            psHD.setString(1, tuNgay + " 00:00:00");
            psHD.setString(2, denNgay + " 23:59:59");
            ResultSet rsHD = psHD.executeQuery();
            if (rsHD.next()) {
                data.setDoanhThu(rsHD.getDouble("DT"));
                data.setSoDonHang(rsHD.getInt("SLHD"));
                data.setSoKhachHang(rsHD.getInt("SLKH"));
            }

            // Lấy Tổng chi từ bảng phieunhaphang
            String sqlPN = "SELECT COALESCE(SUM(TongTienNhap), 0) as TC FROM phieunhaphang WHERE NgayNhap BETWEEN ? AND ?";
            PreparedStatement psPN = conn.prepareStatement(sqlPN);
            psPN.setString(1, tuNgay + " 00:00:00");
            psPN.setString(2, denNgay + " 23:59:59");
            ResultSet rsPN = psPN.executeQuery();
            if (rsPN.next()) {
                data.setTongChi(rsPN.getDouble("TC"));
            }

            // Lấy Số sách đã bán từ bảng cthoadon (liên kết hoadon)
            String sqlSach = "SELECT COALESCE(SUM(ct.SoLuong), 0) as SLS FROM cthoadon ct " +
                             "JOIN hoadon hd ON ct.MaHD = hd.MaHD " +
                             "WHERE hd.ThoiGian BETWEEN ? AND ?";
            PreparedStatement psSach = conn.prepareStatement(sqlSach);
            psSach.setString(1, tuNgay + " 00:00:00");
            psSach.setString(2, denNgay + " 23:59:59");
            ResultSet rsSach = psSach.executeQuery();
            if (rsSach.next()) {
                data.setSoSachDaBan(rsSach.getInt("SLS"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // =============================================================
    // 2. LẤY CHI TIẾT THEO QUÝ (CHO BẢNG CHI TIẾT)
    // =============================================================

    // Thống kê theo Nhân viên
    public ArrayList<ThongKeDTO.ChiTiet> getDoanhThuNhanVienTheoQuy(int nam) {
        String sql = "SELECT nv.TenNV, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 1 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q1, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 2 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q2, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 3 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q3, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 4 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q4 " +
                "FROM nhanvien nv " +
                "LEFT JOIN hoadon hd ON nv.MaNV = hd.MaNV AND YEAR(hd.ThoiGian) = ? " +
                "GROUP BY nv.MaNV, nv.TenNV";
        return fetchChiTietData(sql, nam);
            }

    // Thống kê theo Khách hàng
    public ArrayList<ThongKeDTO.ChiTiet> getDoanhThuKhachHangTheoQuy(int nam) {
        String sql = "SELECT kh.TenKH, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 1 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q1, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 2 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q2, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 3 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q3, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 4 THEN COALESCE(hd.TongTienThanhToan, 0) ELSE 0 END) AS Q4 " +
                "FROM khachhang kh " +
                "LEFT JOIN hoadon hd ON kh.MaKH = hd.MaKH AND YEAR(hd.ThoiGian) = ? " +
                "GROUP BY kh.MaKH, kh.TenKH";
        return fetchChiTietData(sql, nam);
    }

    // Thống kê theo Sản phẩm
    public ArrayList<ThongKeDTO.ChiTiet> getDoanhThuSanPhamTheoQuy(int nam) {
        String sql = "SELECT sp.Ten, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 1 THEN COALESCE(ct.ThanhTien, 0) ELSE 0 END) AS Q1, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 2 THEN COALESCE(ct.ThanhTien, 0) ELSE 0 END) AS Q2, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 3 THEN COALESCE(ct.ThanhTien, 0) ELSE 0 END) AS Q3, " +
                "SUM(CASE WHEN QUARTER(hd.ThoiGian) = 4 THEN COALESCE(ct.ThanhTien, 0) ELSE 0 END) AS Q4 " +
                "FROM sanpham sp " +
                "LEFT JOIN cthoadon ct ON sp.MaSP = ct.MaSP " +
                "LEFT JOIN hoadon hd ON ct.MaHD = hd.MaHD AND YEAR(hd.ThoiGian) = ? " +
                "GROUP BY sp.MaSP, sp.Ten";
        return fetchChiTietData(sql, nam);
    }

    // Hàm dùng chung để thực thi truy vấn và đóng gói vào ArrayList<ChiTiet>
    private ArrayList<ThongKeDTO.ChiTiet> fetchChiTietData(String sql, int nam) {
        ArrayList<ThongKeDTO.ChiTiet> list = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ThongKeDTO.ChiTiet(
                    rs.getString(1),    // tenDoiTuong
                    rs.getDouble(2),    // quy1
                    rs.getDouble(3),    // quy2
                    rs.getDouble(4),    // quy3
                    rs.getDouble(5)     // quy4
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}   