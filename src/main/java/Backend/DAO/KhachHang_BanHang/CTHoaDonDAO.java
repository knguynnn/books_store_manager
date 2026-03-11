package Backend.DAO.KhachHang_BanHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.KhachHang_BanHang.CTHoaDonDTO;
import Backend.DatabaseHelper;

public class CTHoaDonDAO {

    // Lấy chi tiết theo mã hóa đơn
    public ArrayList<CTHoaDonDTO> getByMaHD(String maHD) {
        ArrayList<CTHoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.Ten AS TenSP FROM cthoadon ct "
                   + "LEFT JOIN sanpham sp ON ct.MaSP = sp.MaSP "
                   + "WHERE ct.MaHD = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CTHoaDonDTO ct = new CTHoaDonDTO();
                    ct.setMaHD(rs.getString("MaHD"));
                    ct.setMaSP(rs.getString("MaSP"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGiaBan(rs.getLong("DonGiaBan"));
                    ct.setThanhTien(rs.getLong("ThanhTien"));
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tên SP theo mã (dùng để hiển thị ở bảng chi tiết)
    public String getTenSP(String maSP) {
        String sql = "SELECT Ten FROM sanpham WHERE MaSP = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("Ten");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Thêm chi tiết hóa đơn (dùng trong transaction)
    public boolean insert(CTHoaDonDTO ct, Connection conn) throws SQLException {
        String sql = "INSERT INTO cthoadon (MaHD, MaSP, SoLuong, DonGiaBan, ThanhTien) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaHD());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setLong(4, ct.getDonGiaBan());
            ps.setLong(5, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        }
    }

    // Xóa tất cả chi tiết theo mã HĐ (dùng khi update HĐ)
    public boolean deleteByMaHD(String maHD, Connection conn) throws SQLException {
        String sql = "DELETE FROM cthoadon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() >= 0;
        }
    }
}
