package Backend.BUS.NhanVien_TaiKhoan;

import java.util.ArrayList;

import Backend.DAO.NhanVien_TaiKhoan.NhanVienDAO;
import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;

public class NhanVienBUS {
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public ArrayList<NhanVienDTO> getAll() {
        return nhanVienDAO.getAll();
    }

    public NhanVienDTO getById(String maNV) {
        return nhanVienDAO.getById(maNV);
    }

    public ArrayList<NhanVienDTO> search(String keyword) {
        return nhanVienDAO.search(keyword);
    }
}
