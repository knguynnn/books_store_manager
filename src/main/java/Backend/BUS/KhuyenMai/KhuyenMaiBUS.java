package Backend.BUS.KhuyenMai;

import java.util.ArrayList;

import Backend.DAO.KhuyenMai.CTKhuyenMaiSanPhamDAO;
import Backend.DAO.KhuyenMai.KhuyenMaiDAO;
import Backend.DTO.KhachHang_BanHang.CTHoaDonDTO;
import Backend.DTO.KhuyenMai.KhuyenMaiDTO;

public class KhuyenMaiBUS {
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final CTKhuyenMaiSanPhamDAO ctKmSpDAO = new CTKhuyenMaiSanPhamDAO();

    public ArrayList<KhuyenMaiDTO> getAll() {
        return khuyenMaiDAO.getAll();
    }

    // Lấy khuyến mãi đang còn hạn
    public ArrayList<KhuyenMaiDTO> getActive() {
        return khuyenMaiDAO.getActive();
    }

    public KhuyenMaiDTO getById(String maKM) {
        return khuyenMaiDAO.getById(maKM);
    }

    /**
     * Tính giảm giá cho hóa đơn (phiên bản đơn giản - không kiểm tra SP)
     */
    public long tinhGiamGia(String maKM, long tongTienTruocKM) {
        return tinhGiamGiaChiTiet(maKM, tongTienTruocKM, null);
    }

    /**
     * Tính giảm giá chi tiết, hỗ trợ cả GIAM_HD và GIAM_SP
     * @param maKM mã khuyến mãi
     * @param tongTienTruocKM tổng tiền trước khuyến mãi
     * @param dsCTHD danh sách chi tiết hóa đơn (cần cho GIAM_SP)
     * @return số tiền được giảm
     */
    public long tinhGiamGiaChiTiet(String maKM, long tongTienTruocKM, ArrayList<CTHoaDonDTO> dsCTHD) {
        if (maKM == null || maKM.isEmpty()) return 0;
        KhuyenMaiDTO km = khuyenMaiDAO.getById(maKM);
        if (km == null) return 0;

        // kiểm tra điều kiện tối thiểu
        if (tongTienTruocKM < km.getDieuKien()) return 0;

        // GIAM_HD: giảm theo % trên tổng hóa đơn
        if ("GIAM_HD".equals(km.getLoaiKM())) {
            return tongTienTruocKM * km.getGiaTri() / 100;
        }

        // GIAM_SP: giảm giá trị cố định cho mỗi SP đủ điều kiện
        if ("GIAM_SP".equals(km.getLoaiKM())) {
            if (dsCTHD == null || dsCTHD.isEmpty()) {
                // Fallback: trả giá trị cố định nếu không có chi tiết
                return km.getGiaTri();
            }
            // Lấy danh sách SP đủ điều kiện khuyến mãi
            ArrayList<String> eligibleProducts = ctKmSpDAO.getEligibleProductIds(maKM);
            long tongGiam = 0;
            for (CTHoaDonDTO ct : dsCTHD) {
                if (eligibleProducts.contains(ct.getMaSP())) {
                    tongGiam += (long) km.getGiaTri() * ct.getSoLuong();
                }
            }
            return tongGiam;
        }
        return 0;
    }
}
