package Backend.DAO.NCC_NhapHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;
import Backend.DatabaseHelper;

public class NhaCungCapDAO {

    public ArrayList<NhaCungCapDTO> getAll() {
        ArrayList<NhaCungCapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ncc ORDER BY MaNCC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy danh sách NCC: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(NhaCungCapDTO ncc) {
        String sql = "INSERT INTO ncc (MaNCC, TenNCC, SDT, DiaChi, Email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getSoDienThoai());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi thêm NCC: " + e.getMessage());
            return false;
        }
    }

    public boolean update(NhaCungCapDTO ncc) {
        String sql = "UPDATE ncc SET TenNCC=?, SDT=?, DiaChi=?, Email=? WHERE MaNCC=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getSoDienThoai());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi cập nhật NCC: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String maNCC) {
        String sql = "DELETE FROM ncc WHERE MaNCC = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi xóa NCC: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<NhaCungCapDTO> search(String keyword) {
        ArrayList<NhaCungCapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ncc WHERE MaNCC LIKE ? OR TenNCC LIKE ? OR SDT LIKE ? ORDER BY MaNCC";
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
            System.err.println("❌ Lỗi tìm kiếm NCC: " + e.getMessage());
        }
        return list;
    }

    private NhaCungCapDTO mapRow(ResultSet rs) throws SQLException {
        return new NhaCungCapDTO(
            rs.getString("MaNCC"),
            rs.getString("TenNCC"),
            rs.getString("SDT"),
            rs.getString("DiaChi"),
            rs.getString("Email")
        );
    }
}
