package Backend.DAO.SanPham_DanhMuc;

import Backend.DTO.SanPham_DanhMuc.TacGiaDTO;
import Backend.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;

public class TacGiaDAO {

    // 1. Lấy tất cả tác giả
    public ArrayList<TacGiaDTO> getAll() {
        ArrayList<TacGiaDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tacgia ORDER BY MaTG";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TacGiaDTO tg = new TacGiaDTO(
                    rs.getString("MaTG"),
                    rs.getString("HoTG"),
                    rs.getString("TenTG")
                );
                list.add(tg);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getAll tác giả: " + e.getMessage());
        }
        return list;
    }

    // 2. Thêm tác giả mới
    public boolean insert(TacGiaDTO tg) {
        String sql = "INSERT INTO tacgia (MaTG, HoTG, TenTG) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tg.getMaTG());
            ps.setString(2, tg.getHoTG());
            ps.setString(3, tg.getTenTG());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi thêm tác giả: " + e.getMessage());
            return false;
        }
    }

    // 3. Cập nhật thông tin tác giả
    public boolean update(TacGiaDTO tg) {
        String sql = "UPDATE tacgia SET HoTG = ?, TenTG = ? WHERE MaTG = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tg.getHoTG());
            ps.setString(2, tg.getTenTG());
            ps.setString(3, tg.getMaTG());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi cập nhật tác giả: " + e.getMessage());
            return false;
        }
    }

    // 4. Xóa tác giả 
    public boolean delete(String maTG) {
        String sql = "DELETE FROM tacgia WHERE MaTG = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maTG);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lỗi này thường xảy ra nếu Tác giả đã có sách trong bảng sanpham (Ràng buộc khóa ngoại)
            System.out.println("❌ Lỗi xóa tác giả (Có thể do đang được liên kết với sản phẩm): " + e.getMessage());
            return false;
        }
    }

    // 5. Tìm kiếm tác giả theo tên hoặc mã
    public ArrayList<TacGiaDTO> search(String keyword) {
        ArrayList<TacGiaDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tacgia WHERE MaTG LIKE ? OR CONCAT(HoTG,' ',TenTG) LIKE ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new TacGiaDTO(
                        rs.getString("MaTG"),
                        rs.getString("HoTG"),
                        rs.getString("TenTG")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi tìm kiếm tác giả: " + e.getMessage());
        }
        return list;
    }
}