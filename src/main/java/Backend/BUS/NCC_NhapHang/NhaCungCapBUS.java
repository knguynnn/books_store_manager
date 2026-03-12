package Backend.BUS.NCC_NhapHang;

import java.util.ArrayList;

import Backend.DAO.NCC_NhapHang.NhaCungCapDAO;
import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;

public class NhaCungCapBUS {
    private ArrayList<NhaCungCapDTO> listNCC;
    private NhaCungCapDAO dao = new NhaCungCapDAO();

    public NhaCungCapBUS() {
        refreshData();
    }

    public void refreshData() {
        listNCC = dao.getAll();
    }

    public ArrayList<NhaCungCapDTO> getAll() {
        return listNCC;
    }

    public String generateNewMaNCC() {
        if (listNCC == null || listNCC.isEmpty()) {
            return "NCC001";
        }
        int maxNumber = 0;
        for (NhaCungCapDTO ncc : listNCC) {
            String ma = ncc.getMaNCC();
            if (ma.startsWith("NCC")) {
                try {
                    int number = Integer.parseInt(ma.substring(3));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // bỏ qua
                }
            }
        }
        return String.format("NCC%03d", maxNumber + 1);
    }

    public ArrayList<NhaCungCapDTO> search(String keyword) {
        ArrayList<NhaCungCapDTO> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (NhaCungCapDTO ncc : listNCC) {
            if (ncc.getMaNCC().toLowerCase().contains(kw) ||
                ncc.getTenNCC().toLowerCase().contains(kw) ||
                (ncc.getSoDienThoai() != null && ncc.getSoDienThoai().contains(keyword))) {
                result.add(ncc);
            }
        }
        return result;
    }

    public boolean addNhaCungCap(NhaCungCapDTO ncc) {
        boolean result = dao.insert(ncc);
        if (result) refreshData();
        return result;
    }

    public boolean updateNhaCungCap(NhaCungCapDTO ncc) {
        boolean result = dao.update(ncc);
        if (result) refreshData();
        return result;
    }

    public boolean deleteNhaCungCap(String maNCC) {
        boolean result = dao.delete(maNCC);
        if (result) refreshData();
        return result;
    }

    public boolean isMaNCCExist(String maNCC) {
        for (NhaCungCapDTO ncc : listNCC) {
            if (ncc.getMaNCC().equals(maNCC)) {
                return true;
            }
        }
        return false;
    }

    public NhaCungCapDTO getById(String maNCC) {
        for (NhaCungCapDTO ncc : listNCC) {
            if (ncc.getMaNCC().equals(maNCC)) {
                return ncc;
            }
        }
        return null;
    }
}
