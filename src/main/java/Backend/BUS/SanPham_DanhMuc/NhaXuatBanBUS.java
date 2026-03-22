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

    /**
     * Thêm NXB: Tự động gán mã mới nhất từ hệ thống
     */
    public String validateAndAdd(NhaXuatBanDTO nxb) {
        if (nxb.getTenNXB() == null || nxb.getTenNXB().trim().isEmpty()) return "Tên nhà xuất bản không được để trống!";
        
        // Luôn tạo mã mới để tránh trùng lặp khi nhiều người cùng dùng
        nxb.setMaNXB(generateNewMaNXB());

        if (nxbDAO.insert(nxb)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể thêm nhà xuất bản!";
    }

    /**
     * Cập nhật NXB: Kiểm tra các trường thông tin cơ bản
     */
    public String validateAndUpdate(NhaXuatBanDTO nxb) {
        if (nxb.getTenNXB() == null || nxb.getTenNXB().trim().isEmpty()) return "Tên nhà xuất bản không được để trống!";
        
        if (nxbDAO.update(nxb)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể cập nhật thông tin!";
    }

    public boolean deleteNXB(String maNXB) {
        if (nxbDAO.delete(maNXB)) {
            refreshData();
            return true;
        }
        return false;
    }

    /**
     * Sinh mã NXB001, NXB002... 
     * Cắt chuỗi từ vị trí thứ 3 (sau chữ "NXB")
     */
    public String generateNewMaNXB() {
        if (listNXB == null || listNXB.isEmpty()) return "NXB001";
        
        int max = 0;
        for (NhaXuatBanDTO nxb : listNXB) {
            String ma = nxb.getMaNXB();
            if (ma != null && ma.startsWith("NXB")) {
                try {
                    int num = Integer.parseInt(ma.substring(3));
                    if (num > max) max = num;
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu định dạng mã không chuẩn
                }
            }
        }
        return String.format("NXB%03d", max + 1);
    }

    /**
     * Tìm kiếm theo Mã hoặc Tên NXB
     */
    public ArrayList<NhaXuatBanDTO> search(String keyword) {
        ArrayList<NhaXuatBanDTO> result = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        for (NhaXuatBanDTO nxb : listNXB) {
            if (nxb.getMaNXB().toLowerCase().contains(kw) || 
                nxb.getTenNXB().toLowerCase().contains(kw)) {
                result.add(nxb);
            }
        }
        return result;
    }

    public NhaXuatBanDTO getById(String id) {
        for (NhaXuatBanDTO nxb : listNXB) {
            if (nxb.getMaNXB().equalsIgnoreCase(id)) return nxb;
        }
        return null;
    }

    public String getTenById(String id) {
        NhaXuatBanDTO nxb = getById(id);
        return (nxb != null) ? nxb.getTenNXB() : "Chưa xác định";
    }

    public String getMaByTen(String ten) {
        for (NhaXuatBanDTO nxb : listNXB) {
            if (nxb.getTenNXB().equalsIgnoreCase(ten)) return nxb.getMaNXB();
        }
        return "";
    }
}