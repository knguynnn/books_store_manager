package Backend.DAO.SanPham_DanhMuc;

import Backend.DTO.SanPham_DanhMuc.NhaXuatBanDTO;
import Backend.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;

public class NhaXuatBanDAO {

    // 1. Lấy tất cả Nhà xuất bản
    public ArrayList<NhaXuatBanDTO> getAll() {
        ArrayList<NhaXuatBanDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nxb ORDER BY MaNXB";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhaXuatBanDTO nxb = new NhaXuatBanDTO(
                    rs.getString("MaNXB"),
                    rs.getString("TenNXB"),
                    rs.getString("SDT"),
                    rs.getString("DiaChi"),
                    rs.getString("Email")
                );
                list.add(nxb);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getAll NXB: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm Nhà xuất bản mới
    public boolean insert(NhaXuatBanDTO nxb) {
        String sql = "INSERT INTO nxb (MaNXB, TenNXB, SDT, DiaChi, Email) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nxb.getMaNXB());
            ps.setString(2, nxb.getTenNXB());
            ps.setString(3, nxb.getSoDienThoai());
            ps.setString(4, nxb.getDiaChi());
            ps.setString(5, nxb.getEmail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm NXB: " + e.getMessage());
            return false;
        }
    }

    // 3. Cập nhật Nhà xuất bản
    public boolean update(NhaXuatBanDTO nxb) {
        String sql = "UPDATE nxb SET TenNXB=?, SDT=?, DiaChi=?, Email=? WHERE MaNXB=?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nxb.getTenNXB());
            ps.setString(2, nxb.getSoDienThoai());
            ps.setString(3, nxb.getDiaChi());
            ps.setString(4, nxb.getEmail());
            ps.setString(5, nxb.getMaNXB()); // MaNXB nằm cuối cho WHERE

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật NXB: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String maNXB) {
        String sql = "UPDATE nxb SET TrangThai = 0 WHERE MaNXB = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNXB);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa NXB: " + e.getMessage());
            return false;
        }
    }

    // 5. Tìm kiếm NXB theo Tên hoặc Mã
    public ArrayList<NhaXuatBanDTO> search(String keyword) {
        ArrayList<NhaXuatBanDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nxb WHERE MaNXB LIKE ? OR TenNXB LIKE ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new NhaXuatBanDTO(
                        rs.getString("MaNXB"),
                        rs.getString("TenNXB"),
                        rs.getString("SDT"),
                        rs.getString("DiaChi"),
                        rs.getString("Email")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm NXB: " + e.getMessage());
        }
        return list;
    }
}