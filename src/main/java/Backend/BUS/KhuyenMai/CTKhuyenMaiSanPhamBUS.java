package Backend.BUS.KhuyenMai;

import java.util.ArrayList;

import Backend.DAO.KhuyenMai.CTKhuyenMaiSanPhamDAO;
import Backend.DTO.KhuyenMai.CTKhuyenMaiSanPhamDTO;

public class CTKhuyenMaiSanPhamBUS {
    private final CTKhuyenMaiSanPhamDAO dao = new CTKhuyenMaiSanPhamDAO();

    public ArrayList<CTKhuyenMaiSanPhamDTO> getByMaKM(String maKM) {
        return dao.getByMaKM(maKM);
    }

    public boolean isProductEligible(String maKM, String maSP) {
        return dao.isProductEligible(maKM, maSP);
    }

    public ArrayList<String> getEligibleProductIds(String maKM) {
        return dao.getEligibleProductIds(maKM);
    }
}
