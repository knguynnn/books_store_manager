package Backend.DAO.KhachHang_BanHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.KhachHang_BanHang.KhachHangDTO;
import Backend.DatabaseHelper;

public class KhachHangDAO {

    // Lấy tất cả khách hàng đang hoạt động
    public ArrayList<KhachHangDTO> getAll() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang WHERE TrangThai = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm theo mã
    public KhachHangDTO getById(String maKH) {
        String sql = "SELECT * FROM khachhang WHERE MaKH = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm theo tên hoặc mã
    public ArrayList<KhachHangDTO> search(String keyword) {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang WHERE TrangThai = 1 AND (MaKH LIKE ? OR CONCAT(HoKH,' ',TenKH) LIKE ? OR SDT LIKE ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private KhachHangDTO mapRow(ResultSet rs) throws SQLException {
        KhachHangDTO kh = new KhachHangDTO();
        kh.setMaKH(rs.getString("MaKH"));
        kh.setHoKH(rs.getString("HoKH"));
        kh.setTenKH(rs.getString("TenKH"));
        kh.setDiaChi(rs.getString("DChi"));
        kh.setEmail(rs.getString("Email"));
        kh.setSoDienThoai(rs.getString("SDT"));
        kh.setTrangThai(rs.getBoolean("TrangThai"));
        return kh;
    }
}
