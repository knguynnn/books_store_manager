package Backend.BUS.NCC_NhapHang;

import Backend.DAO.NCC_NhapHang.NhaCungCapDAO;
import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;
import java.util.ArrayList;

public class NhaCungCapBUS {
    private NhaCungCapDAO dao = new NhaCungCapDAO();
    private ArrayList<NhaCungCapDTO> listNCC;

    public NhaCungCapBUS() {
        refreshData();
    }

    public void refreshData() {
        listNCC = dao.getAll();
    }

    public ArrayList<NhaCungCapDTO> getAll() {
        return listNCC;
    }

    public NhaCungCapDTO getNCCByID(String maNCC) {
        for (NhaCungCapDTO ncc : listNCC) {
            if (ncc.getMaNCC().equals(maNCC)) return ncc;
        }
        return null;
    }

    public boolean addNCC(NhaCungCapDTO ncc) {
        if (dao.insert(ncc)) {
            listNCC.add(ncc);
            return true;
        }
        return false;
    }

    public boolean updateNCC(NhaCungCapDTO ncc) {
        if (dao.update(ncc)) {
            for (int i = 0; i < listNCC.size(); i++) {
                if (listNCC.get(i).getMaNCC().equals(ncc.getMaNCC())) {
                    listNCC.set(i, ncc);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteNCC(String maNCC) {
        if (dao.delete(maNCC)) {
            listNCC.removeIf(ncc -> ncc.getMaNCC().equals(maNCC));
            return true;
        }
        return false;
    }

    public ArrayList<NhaCungCapDTO> search(String keyword) {
        ArrayList<NhaCungCapDTO> result = new ArrayList<>();
        keyword = keyword.toLowerCase();
        for (NhaCungCapDTO ncc : listNCC) {
            if (ncc.getMaNCC().toLowerCase().contains(keyword) || 
                ncc.getTenNCC().toLowerCase().contains(keyword) ||
                ncc.getSdt().contains(keyword)) {
                result.add(ncc);
            }
        }
        return result;
    }

    public String generateNewMaNCC() {
        int maxId = 0;
        for (NhaCungCapDTO ncc : listNCC) {
            if (ncc.getMaNCC().matches("^NCC\\d+$")) {
                try {
                    int id = Integer.parseInt(ncc.getMaNCC().replace("NCC", ""));
                    if (id > maxId) maxId = id;
                } catch (NumberFormatException e) { }
            }
        }
        return "NCC" + String.format("%03d", maxId + 1);
    }
}
