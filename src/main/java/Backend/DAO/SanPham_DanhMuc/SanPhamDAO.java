package Backend.DAO.SanPham_DanhMuc;

import Backend.DatabaseHelper;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import java.sql.*;
import java.util.ArrayList;

public class SanPhamDAO {
    
    // Hàm phụ để tránh lặp code khi đọc ResultSet
    private SanPhamDTO readResultSet(ResultSet rs) throws SQLException {
        SanPhamDTO sp = new SanPhamDTO();
        sp.setMaSP(rs.getString("MaSP"));
        sp.setHinhAnh(rs.getString("HinhAnh")); // Thêm mới
        sp.setTenSP(rs.getString("Ten"));
        sp.setMaTG(rs.getString("MaTG")); 
        sp.setMaTL(rs.getString("MaTL")); 
        sp.setSoLuongTon(rs.getInt("SLTonKho"));
        sp.setDonViTinh(rs.getString("DonViTinh"));
        sp.setDonGia(rs.getLong("DonGia"));
        sp.setNamXuatBan(rs.getInt("NamXB"));
        sp.setMaNXB(rs.getString("MaNXB"));
        sp.setMoTa(rs.getString("MoTa"));
        sp.setTrangThai(rs.getBoolean("TrangThai"));
        return sp;
    }

    // SanPhamDAO.java
    public ArrayList<SanPhamDTO> getAll() {
        ArrayList<SanPhamDTO> ds = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE TrangThai = 1"; 
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(readResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean insert(SanPhamDTO sp) {
        // Thêm HinhAnh vào câu lệnh SQL
        String sql = "INSERT INTO sanpham (MaSP, HinhAnh, Ten, MaTG, MaTL, MaNXB, SLTonKho, DonViTinh, DonGia, NamXB, MoTa, TrangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getHinhAnh()); 
            ps.setString(3, sp.getTenSP());
            ps.setString(4, sp.getMaTG()); 
            ps.setString(5, sp.getMaTL()); 
            ps.setString(6, sp.getMaNXB());
            ps.setInt(7, sp.getSoLuongTon());
            ps.setString(8, sp.getDonViTinh());
            ps.setLong(9, sp.getDonGia());
            ps.setInt(10, sp.getNamXuatBan());
            ps.setString(11, sp.getMoTa());
            ps.setBoolean(12, sp.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi thêm sản phẩm: " + e.getMessage());
            return false;
        }
    }

    public boolean update(SanPhamDTO sp) {
        // Thêm HinhAnh=? vào câu lệnh SQL
        String sql = "UPDATE sanpham SET HinhAnh=?, Ten=?, MaTG=?, MaTL=?, MaNXB=?, SLTonKho=?, DonViTinh=?, DonGia=?, NamXB=?, MoTa=?, TrangThai=? "
                   + "WHERE MaSP=?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sp.getHinhAnh()); // Thêm mới
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getMaTG()); 
            ps.setString(4, sp.getMaTL()); 
            ps.setString(5, sp.getMaNXB());
            ps.setInt(6, sp.getSoLuongTon());
            ps.setString(7, sp.getDonViTinh());
            ps.setLong(8, sp.getDonGia());
            ps.setInt(9, sp.getNamXuatBan());
            ps.setString(10, sp.getMoTa());
            ps.setBoolean(11, sp.isTrangThai());
            ps.setString(12, sp.getMaSP()); 
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi cập nhật sản phẩm: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<SanPhamDTO> search(String keyword) {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT sp.* FROM sanpham sp " +
            "LEFT JOIN tacgia tg ON sp.MaTG = tg.MaTG " +
            "LEFT JOIN theloai tl ON sp.MaTL = tl.MaTL " +
            "WHERE sp.TrangThai = 1 " +          // ← Đưa lên đầu
            "AND (sp.Ten LIKE ? " +              // ← Bọc trong ngoặc
            "OR sp.MaSP LIKE ? " +
            "OR CONCAT(tg.HoTG, ' ', tg.TenTG) LIKE ? " +
            "OR tl.TenTL LIKE ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchKey = "%" + keyword + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(readResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm kiếm SP: " + e.getMessage());
        }
        return list;
    }

    public boolean delete(String maSP) {
        String sql = "UPDATE sanpham SET TrangThai = 0 WHERE MaSP = ?";
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa sản phẩm: " + e.getMessage());
        }
        return false;
    }

    public String getMaxMaSP() {
        String maxMa = "SP000";
        // Bỏ điều kiện TrangThai để luôn lấy mã lớn nhất kể cả đã xóa
        String sql = "SELECT MaSP FROM sanpham ORDER BY MaSP DESC LIMIT 1";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maxMa = rs.getString("MaSP");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxMa;
    }

    public ArrayList<SanPhamDTO> searchAdvanced(String maTG, String maTL, int maxSoLuong, long maxGia) {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM sanpham WHERE TrangThai = 1");
        
        if (maTG != null && !maTG.isEmpty()) sql.append(" AND MaTG = ?");
        if (maTL != null && !maTL.isEmpty()) sql.append(" AND MaTL = ?");
        if (maxSoLuong >= 0) sql.append(" AND SLTonKho <= ?");
        if (maxGia >= 0) sql.append(" AND DonGia <= ?");
        sql.append(" ORDER BY MaSP ASC");

        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (maTG != null && !maTG.isEmpty()) ps.setString(idx++, maTG);
            if (maTL != null && !maTL.isEmpty()) ps.setString(idx++, maTL);
            if (maxSoLuong >= 0) ps.setInt(idx++, maxSoLuong);
            if (maxGia >= 0) ps.setLong(idx++, maxGia);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(readResultSet(rs));
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm kiếm nâng cao: " + e.getMessage());
        }
        return list;
    }
}