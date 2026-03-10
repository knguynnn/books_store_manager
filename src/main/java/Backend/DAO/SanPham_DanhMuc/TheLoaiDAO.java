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
                // Khởi tạo DTO khớp với constructor (String maTL, String tenTL)
                TheLoaiDTO tl = new TheLoaiDTO(
                    rs.getString("MaTL"),
                    rs.getString("TenTL")
                );
                list.add(tl);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getAll thể loại: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm thể loại mới
    public boolean insert(TheLoaiDTO tl) {
        String sql = "INSERT INTO theloai (MaTL, TenTL) VALUES (?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tl.getMaTL()); // Sử dụng getMaTL() từ DTO
            ps.setString(2, tl.getTenTL()); // Sử dụng getTenTL() từ DTO

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
            ps.setString(2, tl.getMaTL()); // MaTL dùng cho điều kiện WHERE

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật thể loại: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String maTL) {
        boolean result = false;
        // Đồng bộ hóa bản chất xóa mềm
        String sql = "UPDATE theloai SET TrangThai = 0 WHERE MaTL = ?";
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maTL);
            if (ps.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi xóa thể loại: " + e.getMessage());
        }
        return result;
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