package Backend.DAO.SanPham_DanhMuc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import Backend.DatabaseHelper;

public class SanPhamDAO {

    // Lấy tất cả sản phẩm đang hoạt động
    public ArrayList<SanPhamDTO> getAll() {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE TrangThai = 1";
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
    public SanPhamDTO getById(String maSP) {
        String sql = "SELECT * FROM sanpham WHERE MaSP = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm kiếm theo mã hoặc tên
    public ArrayList<SanPhamDTO> search(String keyword) {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE TrangThai = 1 AND (MaSP LIKE ? OR Ten LIKE ?)";
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

    // Cập nhật số lượng tồn kho (dùng trong transaction)
    public boolean updateSoLuongTon(String maSP, int soLuongGiam, Connection conn) throws SQLException {
        String sql = "UPDATE sanpham SET SLTonKho = SLTonKho - ? WHERE MaSP = ? AND SLTonKho >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongGiam);
            ps.setString(2, maSP);
            ps.setInt(3, soLuongGiam);
            return ps.executeUpdate() > 0;
        }
    }

    // Hoàn lại số lượng tồn kho khi xóa hóa đơn (dùng trong transaction)
    public boolean hoanSoLuongTon(String maSP, int soLuongHoan, Connection conn) throws SQLException {
        String sql = "UPDATE sanpham SET SLTonKho = SLTonKho + ? WHERE MaSP = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongHoan);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        }
    }

    private SanPhamDTO mapRow(ResultSet rs) throws SQLException {
        SanPhamDTO sp = new SanPhamDTO();
        sp.setMaSP(rs.getString("MaSP"));
        sp.setTenSP(rs.getString("Ten"));
        sp.setSoLuongTon(rs.getInt("SLTonKho"));
        sp.setDonViTinh(rs.getString("DonViTinh"));
        sp.setDonGia(rs.getLong("DonGia"));
        sp.setNamXuatBan(rs.getInt("NamXB"));
        sp.setMaNXB(rs.getString("MaNXB"));
        sp.setMaNCC(rs.getString("MaNCC"));
        sp.setMoTa(rs.getString("MoTa"));
        sp.setTrangThai(rs.getBoolean("TrangThai"));
        return sp;
    }
}
