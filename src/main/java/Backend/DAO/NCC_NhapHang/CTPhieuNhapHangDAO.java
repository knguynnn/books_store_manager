package Backend.DAO.NCC_NhapHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.NCC_NhapHang.CTPhieuNhapHangDTO;
import Backend.DatabaseHelper;

public class CTPhieuNhapHangDAO {

    public ArrayList<CTPhieuNhapHangDTO> getByMaPhieuNhap(String maPhieuNhap) {
        ArrayList<CTPhieuNhapHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ctphieunhaphang WHERE MaPhieuNhap = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy chi tiết phiếu nhập: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(CTPhieuNhapHangDTO ct, Connection conn) throws SQLException {
        String sql = "INSERT INTO ctphieunhaphang (MaPhieuNhap, MaSP, SL, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPhieuNhap());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setLong(4, ct.getDonGiaNhap());
            ps.setLong(5, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByMaPhieuNhap(String maPhieuNhap, Connection conn) throws SQLException {
        String sql = "DELETE FROM ctphieunhaphang WHERE MaPhieuNhap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            return ps.executeUpdate() >= 0;
        }
    }

    private CTPhieuNhapHangDTO mapRow(ResultSet rs) throws SQLException {
        return new CTPhieuNhapHangDTO(
            rs.getString("MaPhieuNhap"),
            rs.getString("MaSP"),
            rs.getInt("SL"),
            rs.getLong("DonGia"),
            rs.getLong("ThanhTien")
        );
    }
}
