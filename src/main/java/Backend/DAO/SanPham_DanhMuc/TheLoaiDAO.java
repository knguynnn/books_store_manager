package Backend.DAO.SanPham_DanhMuc;

import Backend.DTO.SanPham_DanhMuc.TheLoaiDTO;
import Backend.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;

public class TheLoaiDAO {

    // 1. Lấy tất cả thể loại
    public ArrayList<TheLoaiDTO> getAll() {
        ArrayList<TheLoaiDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM theloai ORDER BY MaTL";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TheLoaiDTO tl = new TheLoaiDTO(
                    rs.getString("MaTL"),
                    rs.getString("TenTL")
                );
                list.add(tl);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getAll thể loại: " + e.getMessage());
        }
        return list;
    }

    // 2. Thêm thể loại mới
    public boolean insert(TheLoaiDTO tl) {
        String sql = "INSERT INTO theloai (MaTL, TenTL) VALUES (?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tl.getMaTL());
            ps.setString(2, tl.getTenTL());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm thể loại: " + e.getMessage());
            return false;
        }
    }

    // 3. Cập nhật thông tin thể loại
    public boolean update(TheLoaiDTO tl) {
        String sql = "UPDATE theloai SET TenTL = ? WHERE MaTL = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tl.getTenTL());
            ps.setString(2, tl.getMaTL());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật thể loại: " + e.getMessage());
            return false;
        }
    }

    // 4. Xóa thể loại 
    public boolean delete(String maTL) {
        String sql = "DELETE FROM theloai WHERE MaTL = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maTL);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lỗi này thường xảy ra nếu có sản phẩm đang thuộc thể loại này (Khóa ngoại)
            System.out.println("❌ Lỗi xóa thể loại (Có thể do ràng buộc khóa ngoại): " + e.getMessage());
            return false;
        }
    }

    // 5. Tìm kiếm thể loại theo tên hoặc mã
    public ArrayList<TheLoaiDTO> search(String keyword) {
        ArrayList<TheLoaiDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM theloai WHERE MaTL LIKE ? OR TenTL LIKE ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TheLoaiDTO(
                        rs.getString("MaTL"),
                        rs.getString("TenTL")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm thể loại: " + e.getMessage());
        }
        return list;
    }
}