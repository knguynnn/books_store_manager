package Backend.DAO.SanPham_DanhMuc;

import Backend.DatabaseHelper;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import java.sql.*;
import java.util.ArrayList;

public class SanPhamDAO {
    private SanPhamDTO mapRow(ResultSet rs) throws SQLException {
        SanPhamDTO sp = new SanPhamDTO();
        sp.setMaSP(rs.getString("MaSP"));
        sp.setTenSP(rs.getString("Ten"));
        sp.setSoLuongTon(rs.getInt("SLTonKho"));
        sp.setDonViTinh(rs.getString("DonViTinh"));
        sp.setDonGia(rs.getLong("DonGia"));
        sp.setNamXuatBan(rs.getInt("NamXB"));
        sp.setMaNXB(rs.getString("MaNXB"));
        sp.setMaNCC(rs.getString("MaNCC"));
        sp.setMoTa(rs.getString("MoTa"));
        sp.setTrangThai(rs.getBoolean("TrangThai"));
        try { sp.setMaTG(rs.getString("MaTG")); } catch (SQLException ignored) {}
        try { sp.setMaTL(rs.getString("MaTL")); } catch (SQLException ignored) {}
        return sp;
    }

    private static final String SELECT_WITH_JOIN =
        "SELECT sp.*, stg.MaTG, stl.MaTL FROM sanpham sp " +
        "LEFT JOIN sanpham_tacgia stg ON sp.MaSP = stg.MaSP " +
        "LEFT JOIN sanpham_theloai stl ON sp.MaSP = stl.MaSP";

    public ArrayList<SanPhamDTO> getAll() {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = SELECT_WITH_JOIN + " GROUP BY sp.MaSP";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy danh sách SP: " + e.getMessage());
        }
        return list;
    }

    public ArrayList<SanPhamDTO> getActiveOnly() {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = SELECT_WITH_JOIN + " WHERE sp.TrangThai = 1 GROUP BY sp.MaSP ORDER BY sp.MaSP";

        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Lỗi truy vấn getActiveOnly: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(SanPhamDTO sp) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO sanpham (MaSP, Ten, MaNXB, MaNCC, SLTonKho, DonViTinh, DonGia, NamXB, MoTa, TrangThai) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sp.getMaSP());
                ps.setString(2, sp.getTenSP());
                ps.setString(3, sp.getMaNXB());
                ps.setString(4, sp.getMaNCC());
                ps.setInt(5, sp.getSoLuongTon());
                ps.setString(6, sp.getDonViTinh());
                ps.setLong(7, sp.getDonGia());
                ps.setInt(8, sp.getNamXuatBan());
                ps.setString(9, sp.getMoTa());
                ps.setBoolean(10, sp.isTrangThai());
                ps.executeUpdate();
            }

            // Insert vào bảng trung gian nếu có MaTG
            if (sp.getMaTG() != null && !sp.getMaTG().isEmpty()) {
                try (PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT IGNORE INTO sanpham_tacgia (MaSP, MaTG) VALUES (?, ?)")) {
                    ps2.setString(1, sp.getMaSP());
                    ps2.setString(2, sp.getMaTG());
                    ps2.executeUpdate();
                }
            }
            // Insert vào bảng trung gian nếu có MaTL
            if (sp.getMaTL() != null && !sp.getMaTL().isEmpty()) {
                try (PreparedStatement ps3 = conn.prepareStatement(
                        "INSERT IGNORE INTO sanpham_theloai (MaSP, MaTL) VALUES (?, ?)")) {
                    ps3.setString(1, sp.getMaSP());
                    ps3.setString(2, sp.getMaTL());
                    ps3.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi thêm sản phẩm: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) {}
        }
    }

    public boolean update(SanPhamDTO sp) {
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false);

            String sql = "UPDATE sanpham SET Ten=?, MaNXB=?, MaNCC=?, SLTonKho=?, DonViTinh=?, DonGia=?, NamXB=?, MoTa=?, TrangThai=? WHERE MaSP=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sp.getTenSP());
                ps.setString(2, sp.getMaNXB());
                ps.setString(3, sp.getMaNCC());
                ps.setInt(4, sp.getSoLuongTon());
                ps.setString(5, sp.getDonViTinh());
                ps.setLong(6, sp.getDonGia());
                ps.setInt(7, sp.getNamXuatBan());
                ps.setString(8, sp.getMoTa());
                ps.setBoolean(9, sp.isTrangThai());
                ps.setString(10, sp.getMaSP());
                ps.executeUpdate();
            }

            // Cập nhật bảng trung gian tác giả
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM sanpham_tacgia WHERE MaSP = ?")) {
                del.setString(1, sp.getMaSP()); del.executeUpdate();
            }
            if (sp.getMaTG() != null && !sp.getMaTG().isEmpty()) {
                try (PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO sanpham_tacgia (MaSP, MaTG) VALUES (?, ?)")) {
                    ins.setString(1, sp.getMaSP());
                    ins.setString(2, sp.getMaTG());
                    ins.executeUpdate();
                }
            }

            // Cập nhật bảng trung gian thể loại
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM sanpham_theloai WHERE MaSP = ?")) {
                del.setString(1, sp.getMaSP()); del.executeUpdate();
            }
            if (sp.getMaTL() != null && !sp.getMaTL().isEmpty()) {
                try (PreparedStatement ins = conn.prepareStatement(
                        "INSERT INTO sanpham_theloai (MaSP, MaTL) VALUES (?, ?)")) {
                    ins.setString(1, sp.getMaSP());
                    ins.setString(2, sp.getMaTL());
                    ins.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi cập nhật sản phẩm: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) {}
        }
    }

    public ArrayList<SanPhamDTO> search(String keyword) {
        ArrayList<SanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT sp.*, stg.MaTG, stl.MaTL FROM sanpham sp " +
                    "LEFT JOIN sanpham_tacgia stg ON sp.MaSP = stg.MaSP " +
                    "LEFT JOIN sanpham_theloai stl ON sp.MaSP = stl.MaSP " +
                    "LEFT JOIN tacgia tg ON stg.MaTG = tg.MaTG " +
                    "LEFT JOIN theloai tl ON stl.MaTL = tl.MaTL " +
                    "WHERE sp.Ten LIKE ? " +
                    "OR sp.MaSP LIKE ? " +
                    "OR tg.TenTG LIKE ? " +
                    "OR tl.TenTL LIKE ? " +
                    "GROUP BY sp.MaSP";
        
        try (Connection conn = DatabaseHelper.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchKey = "%" + keyword + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
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

    // Trừ số lượng tồn kho (dùng trong transaction khi tạo/sửa hóa đơn)
    public boolean updateSoLuongTon(String maSP, int soLuongBan, Connection conn) throws SQLException {
        String sql = "UPDATE sanpham SET SLTonKho = SLTonKho - ? WHERE MaSP = ? AND SLTonKho >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongBan);
            ps.setString(2, maSP);
            ps.setInt(3, soLuongBan);
            return ps.executeUpdate() > 0;
        }
    }

    // Hoàn lại số lượng tồn kho (dùng khi xóa/sửa hóa đơn)
    public boolean hoanSoLuongTon(String maSP, int soLuongHoan, Connection conn) throws SQLException {
        String sql = "UPDATE sanpham SET SLTonKho = SLTonKho + ? WHERE MaSP = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuongHoan);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        }
    }
}