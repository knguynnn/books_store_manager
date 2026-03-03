package Backend.DAO.SanPham_DanhMuc;

import Backend.DatabaseHelper;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import java.sql.*;
import java.util.ArrayList;

public class SanPhamDAO {
    public ArrayList<SanPhamDTO> getAll() {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO();
                sp.setMaSP(rs.getString("MaSP"));
                sp.setTenSP(rs.getString("Ten"));
                sp.setMaTG(rs.getString("MaTG")); 
                sp.setMaTL(rs.getString("MaTL")); 
                sp.setSoLuongTon(rs.getInt("SLTonKho"));
                sp.setDonViTinh(rs.getString("DonViTinh"));
                sp.setDonGia(rs.getLong("DonGia"));
                sp.setNamXuatBan(rs.getInt("NamXB"));
                sp.setMaNXB(rs.getString("MaNXB"));
                sp.setMaNCC(rs.getString("MaNCC"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy danh sách SP: " + e.getMessage());
        }
        return list;
    }

    public ArrayList<SanPhamDTO> getActiveOnly() {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        // Thêm điều kiện WHERE TrangThai = 1 để lọc các sản phẩm đang kinh doanh
        String sql = "SELECT * FROM sanpham WHERE TrangThai = 1 ORDER BY MaSP";

        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO(
                    rs.getString("MaSP"),
                    rs.getString("Ten"),
                    rs.getString("MaTG"),
                    rs.getString("MaTL"),
                    rs.getInt("SLTonKho"),      
                    rs.getString("DonViTinh"),
                    rs.getLong("DonGia"),       
                    rs.getInt("NamXB"),        
                    rs.getString("MaNXB"),
                    rs.getString("MaNCC"),
                    rs.getString("MoTa"),
                    rs.getBoolean("TrangThai")  
                );
                list.add(sp);
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getActiveOnly: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(SanPhamDTO sp) {
        String sql = "INSERT INTO sanpham (MaSP, Ten, MaTG, MaTL, MaNXB, MaNCC, SLTonKho, DonViTinh, DonGia, NamXB, MoTa, TrangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getMaTG()); 
            ps.setString(4, sp.getMaTL()); 
            ps.setString(5, sp.getMaNXB());
            ps.setString(6, sp.getMaNCC());
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
        String sql = "UPDATE sanpham SET Ten=?, MaTG=?, MaTL=?, MaNXB=?, MaNCC=?, SLTonKho=?, DonViTinh=?, DonGia=?, NamXB=?, MoTa=?, TrangThai=? "
                   + "WHERE MaSP=?";
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getMaTG()); 
            ps.setString(3, sp.getMaTL()); 
            ps.setString(4, sp.getMaNXB());
            ps.setString(5, sp.getMaNCC());
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
        // Câu lệnh SQL nối bảng để tìm theo Tên của Tác giả và Thể loại
        String sql = "SELECT sp.* FROM sanpham sp " +
                    "LEFT JOIN tacgia tg ON sp.MaTG = tg.MaTG " +
                    "LEFT JOIN theloai tl ON sp.MaTL = tl.MaTL " +
                    "WHERE sp.Ten LIKE ? " +
                    "OR tg.TenTG LIKE ? " +
                    "OR tl.TenTL LIKE ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchKey = "%" + keyword + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPhamDTO sp = new SanPhamDTO();
                    sp.setMaSP(rs.getString("MaSP"));
                    sp.setTenSP(rs.getString("Ten"));
                    sp.setMaTG(rs.getString("MaTG"));
                    sp.setMaTL(rs.getString("MaTL"));
                    sp.setSoLuongTon(rs.getInt("SLTonKho"));
                    sp.setDonViTinh(rs.getString("DonViTinh"));
                    sp.setDonGia(rs.getLong("DonGia"));
                    sp.setNamXuatBan(rs.getInt("NamXB"));
                    sp.setMaNXB(rs.getString("MaNXB"));
                    sp.setMaNCC(rs.getString("MaNCC"));
                    sp.setMoTa(rs.getString("MoTa"));
                    sp.setTrangThai(rs.getBoolean("TrangThai"));
                    list.add(sp);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm kiếm SP: " + e.getMessage());
        }
        return list;
    }

    public boolean delete(String maSP) {
        // Câu lệnh SQL cập nhật trạng thái về 0 (Ngừng bán) dựa trên MaSP
        String sql = "UPDATE sanpham SET TrangThai = 0 WHERE MaSP = ?";
        
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maSP); // Nạp mã sản phẩm vào dấu ?
            
            return ps.executeUpdate() > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            System.out.println("❌ Lỗi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}