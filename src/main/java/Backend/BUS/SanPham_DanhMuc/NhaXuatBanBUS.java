package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.NhaXuatBanDAO;
import Backend.DTO.SanPham_DanhMuc.NhaXuatBanDTO;
import java.util.ArrayList;

public class NhaXuatBanBUS {
    private ArrayList<NhaXuatBanDTO> listNXB;
    private NhaXuatBanDAO nxbDAO;

    public NhaXuatBanBUS() {
        nxbDAO = new NhaXuatBanDAO();
        refreshData();
    }

    public void refreshData() {
        listNXB = nxbDAO.getAll();
    }

    public ArrayList<NhaXuatBanDTO> getAll() {
        return listNXB;
    }

    public boolean addNXB(NhaXuatBanDTO nxb) {
        if (nxbDAO.insert(nxb)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean updateNXB(NhaXuatBanDTO nxb) {
        if (nxbDAO.update(nxb)) {
            refreshData();
            return true;
        }
        return false;
    }

    public boolean deleteNXB(String maNXB) {
        if (nxbDAO.delete(maNXB)) {
            refreshData();
            return true;
        }
        return false;
    }

    public ArrayList<NhaXuatBanDTO> search(String keyword) {
        ArrayList<NhaXuatBanDTO> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (NhaXuatBanDTO nxb : listNXB) {
            if (nxb.getTenNXB().toLowerCase().contains(kw) || nxb.getMaNXB().toLowerCase().contains(kw)) {
                result.add(nxb);
            }
        }
        return result;
    }

    // Tự động tạo mã NXB mới (Định dạng NXB001, NXB002...)
    public String generateNewMaNXB() {
        if (listNXB.isEmpty()) return "NXB001";
        int max = 0;
        for (NhaXuatBanDTO nxb : listNXB) {
            try {
                // Lưu ý: Cắt từ vị trí thứ 3 (sau chữ "NXB")
                int num = Integer.parseInt(nxb.getMaNXB().substring(3));
                if (num > max) max = num;
            } catch (Exception e) {}
        }
        return String.format("NXB%03d", max + 1);
    }

    public String getTenById(String id) {
        for (NhaXuatBanDTO nxb : listNXB) {
            if (nxb.getMaNXB().equals(id)) return nxb.getTenNXB();
        }
        return "Chưa xác định";
    }

    public String getMaByTen(String ten) {
        for (NhaXuatBanDTO nxb : listNXB) {
            if (nxb.getTenNXB().equalsIgnoreCase(ten)) return nxb.getMaNXB();
        }
        return "";
    }
}