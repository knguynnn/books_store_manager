package Backend.DAO.KhuyenMai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.KhuyenMai.KhuyenMaiDTO;
import Backend.DatabaseHelper;

public class KhuyenMaiDAO {

    // Lấy tất cả khuyến mãi đang hoạt động
    public ArrayList<KhuyenMaiDTO> getAll() {
        ArrayList<KhuyenMaiDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM khuyenmai WHERE TrangThai = 1";
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

    // Lấy khuyến mãi đang còn hạn (hiện tại nằm giữa ngày bắt đầu và kết thúc)
    public ArrayList<KhuyenMaiDTO> getActive() {
        ArrayList<KhuyenMaiDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM khuyenmai WHERE TrangThai = 1 AND NgayBatDau <= NOW() AND NgayKetThuc >= NOW()";
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
    public KhuyenMaiDTO getById(String maKM) {
        String sql = "SELECT * FROM khuyenmai WHERE MaKM = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KhuyenMaiDTO mapRow(ResultSet rs) throws SQLException {
        KhuyenMaiDTO km = new KhuyenMaiDTO();
        km.setMaKM(rs.getString("MaKM"));
        km.setTenKM(rs.getString("TenKM"));
        km.setNgayBatDau(rs.getTimestamp("NgayBatDau"));
        km.setNgayKetThuc(rs.getTimestamp("NgayKetThuc"));
        km.setLoaiKM(rs.getString("LoaiKM"));
        km.setGiaTri(rs.getInt("GiaTri"));
        km.setDieuKien(rs.getLong("DieuKien"));
        km.setTrangThai(rs.getBoolean("TrangThai"));
        return km;
    }
}
