package Backend.DAO.NCC_NhapHang;

import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;
import Backend.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCapDAO {

    public ArrayList<NhaCungCapDTO> getAll() {
        ArrayList<NhaCungCapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ncc"; 
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new NhaCungCapDTO(
                    rs.getString("MaNCC"),
                    rs.getString("TenNCC"),
                    rs.getString("SDT"),
                    rs.getString("DiaChi"),
                    rs.getString("Email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(NhaCungCapDTO ncc) {
        String sql = "INSERT INTO ncc (MaNCC, TenNCC, SDT, DiaChi, Email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(NhaCungCapDTO ncc) {
        String sql = "UPDATE ncc SET TenNCC=?, SDT=?, DiaChi=?, Email=? WHERE MaNCC=?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getSdt());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
            // Lỗi do dính khóa ngoại (đã từng nhập hàng thì ko được xóa)
            e.printStackTrace();
            return false; 
        }
    }

        // hàm tìm kiếm ở DAO
        
    public ArrayList<NhaCungCapDTO> search(String keyword) {
    ArrayList<NhaCungCapDTO> list = new ArrayList<>();
    // Dùng dấu % để tìm kiếm chứa từ khóa (tương tự contains trong Java)
    String sql = "SELECT * FROM ncc WHERE MaNCC LIKE ? OR TenNCC LIKE ? OR SDT LIKE ?";
    
    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        String value = "%" + keyword + "%";
        ps.setString(1, value);
        ps.setString(2, value);
        ps.setString(3, value);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new NhaCungCapDTO(
                    rs.getString("MaNCC"),
                    rs.getString("TenNCC"),
                    rs.getString("SDT"),
                    rs.getString("DiaChi"),
                    rs.getString("Email")
                ));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}


}



























