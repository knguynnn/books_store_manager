package Backend.DAO.NhanVien_TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;
import Backend.DatabaseHelper;

public class NhanVienDAO {

    // Lấy tất cả nhân viên đang hoạt động
    public ArrayList<NhanVienDTO> getAll() {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE TrangThai = 1";
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
    public NhanVienDTO getById(String maNV) {
        String sql = "SELECT * FROM nhanvien WHERE MaNV = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm theo mã hoặc tên
    public ArrayList<NhanVienDTO> search(String keyword) {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE TrangThai = 1 AND (MaNV LIKE ? OR CONCAT(HoNV,' ',TenNV) LIKE ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
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

    private NhanVienDTO mapRow(ResultSet rs) throws SQLException {
        NhanVienDTO nv = new NhanVienDTO();
        nv.setMaNV(rs.getString("MaNV"));
        nv.setHoNV(rs.getString("HoNV"));
        nv.setTenNV(rs.getString("TenNV"));
        nv.setChucVu(rs.getString("ChucVu"));
        nv.setTrangThai(rs.getBoolean("TrangThai"));
        return nv;
    }
}
