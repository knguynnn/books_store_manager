package Backend.DAO.KhuyenMai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Backend.DatabaseHelper;
import Backend.DTO.KhuyenMai.CTKhuyenMaiSanPhamDTO;

public class CTKhuyenMaiSanPhamDAO {

    public ArrayList<CTKhuyenMaiSanPhamDTO> getByMaKM(String maKM) {
        ArrayList<CTKhuyenMaiSanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT MaKM, MaSP FROM ctkhuyenmai_sanpham WHERE MaKM = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CTKhuyenMaiSanPhamDTO(
                            rs.getString("MaKM"),
                            rs.getString("MaSP")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isProductEligible(String maKM, String maSP) {
        String sql = "SELECT 1 FROM ctkhuyenmai_sanpham WHERE MaKM = ? AND MaSP = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            ps.setString(2, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getEligibleProductIds(String maKM) {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT MaSP FROM ctkhuyenmai_sanpham WHERE MaKM = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("MaSP"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
