package Backend.DAO.NCC_NhapHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.NCC_NhapHang.PhieuNhapHangDTO;
import Backend.DatabaseHelper;

public class PhieuNhapHangDAO {

    public ArrayList<PhieuNhapHangDTO> getAll() {
        ArrayList<PhieuNhapHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM phieunhaphang ORDER BY NgayNhap DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy danh sách phiếu nhập: " + e.getMessage());
        }
        return list;
    }

    public PhieuNhapHangDTO getById(String maPhieuNhap) {
        String sql = "SELECT * FROM phieunhaphang WHERE MaPhieuNhap = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm phiếu nhập: " + e.getMessage());
        }
        return null;
    }

    public boolean insert(PhieuNhapHangDTO pn, Connection conn) throws SQLException {
        String sql = "INSERT INTO phieunhaphang (MaPhieuNhap, NgayNhap, MaNV, MaNCC, TongTienNhap) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pn.getMaPhieuNhap());
            ps.setTimestamp(2, pn.getNgayNhap());
            ps.setString(3, pn.getMaNV());
            ps.setString(4, pn.getMaNCC());
            ps.setLong(5, pn.getTongTienNhap());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(PhieuNhapHangDTO pn, Connection conn) throws SQLException {
        String sql = "UPDATE phieunhaphang SET NgayNhap=?, MaNV=?, MaNCC=?, TongTienNhap=? WHERE MaPhieuNhap=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, pn.getNgayNhap());
            ps.setString(2, pn.getMaNV());
            ps.setString(3, pn.getMaNCC());
            ps.setLong(4, pn.getTongTienNhap());
            ps.setString(5, pn.getMaPhieuNhap());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maPhieuNhap, Connection conn) throws SQLException {
        String sql = "DELETE FROM phieunhaphang WHERE MaPhieuNhap = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuNhap);
            return ps.executeUpdate() > 0;
        }
    }

    public ArrayList<PhieuNhapHangDTO> search(String keyword) {
        ArrayList<PhieuNhapHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM phieunhaphang WHERE MaPhieuNhap LIKE ? OR MaNV LIKE ? OR MaNCC LIKE ? ORDER BY NgayNhap DESC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm kiếm phiếu nhập: " + e.getMessage());
        }
        return list;
    }

    public String generateId() {
        String sql = "SELECT MaPhieuNhap FROM phieunhaphang ORDER BY MaPhieuNhap DESC LIMIT 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String last = rs.getString("MaPhieuNhap");
                int num = Integer.parseInt(last.substring(2)) + 1;
                return String.format("PN%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PN001";
    }

    private PhieuNhapHangDTO mapRow(ResultSet rs) throws SQLException {
        return new PhieuNhapHangDTO(
            rs.getString("MaPhieuNhap"),
            rs.getTimestamp("NgayNhap"),
            rs.getString("MaNV"),
            rs.getString("MaNCC"),
            rs.getLong("TongTienNhap")
        );
    }
}
