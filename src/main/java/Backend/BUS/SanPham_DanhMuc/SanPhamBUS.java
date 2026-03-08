package Backend.BUS.SanPham_DanhMuc;

import java.util.ArrayList;

import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;

public class SanPhamBUS {
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    public ArrayList<SanPhamDTO> getAll() {
        return sanPhamDAO.getAll();
    }

    public SanPhamDTO getById(String maSP) {
        return sanPhamDAO.getById(maSP);
    }

    public ArrayList<SanPhamDTO> search(String keyword) {
        return sanPhamDAO.search(keyword);
    }
}
