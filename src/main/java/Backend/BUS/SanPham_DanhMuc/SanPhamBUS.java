package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import java.util.ArrayList;

public class SanPhamBUS {
    private ArrayList<SanPhamDTO> listSP;
    private SanPhamDAO spDAO;

    public SanPhamBUS() {
        spDAO = new SanPhamDAO();
        refreshData(); // Tự động load dữ liệu khi khởi tạo giống KhachHangBUS
    }

    public void refreshData() {
        listSP = spDAO.getAll(); // Đồng bộ listSP từ Database
    }

    public ArrayList<SanPhamDTO> getAll() {
        return listSP;
    }

    // Lấy danh sách sản phẩm đang kinh doanh (TrangThai = 1)
    public ArrayList<SanPhamDTO> getActiveOnly() {
        ArrayList<SanPhamDTO> activeList = new ArrayList<>();
        for (SanPhamDTO sp : listSP) {
            if (sp.isTrangThai()) {
                activeList.add(sp);
            }
        }
        return activeList; // Logic lọc trên RAM giống KhachHangBUS
    }

    public boolean addSanPham(SanPhamDTO sp) {
        boolean check = spDAO.insert(sp); //
        if (check) {
            refreshData(); // Load lại toàn bộ danh sách để đồng bộ
        }
        return check;
    }

    public boolean updateSanPham(SanPhamDTO sp) {
        boolean check = spDAO.update(sp); //
        if (check) {
            refreshData(); // Load lại toàn bộ danh sách để đồng bộ
        }
        return check;
    }

    public boolean deleteSanPham(String maSP) {
        boolean check = spDAO.delete(maSP); 
        if (check) {
            refreshData(); 
        }
        return check;
    }

    // Hàm tạo mã mới tự động 
    public String generateNewMaSP() {
        if (listSP.isEmpty()) {
            return "SP001";
        }

        int maxNumber = 0;
        for (SanPhamDTO sp : listSP) {
            String maSP = sp.getMaSP();
            if (maSP.startsWith("SP")) {
                try {
                    int number = Integer.parseInt(maSP.substring(2));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu mã không đúng định dạng số
                }
            }
        }
        return String.format("SP%03d", maxNumber + 1);
    }

    public ArrayList<SanPhamDTO> search(String keyword) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (SanPhamDTO sp : listSP) {
            // Tìm kiếm trên RAM để đồng nhất với cách làm của KhachHangBUS
            if (sp.getTenSP().toLowerCase().contains(kw) || 
                sp.getMaSP().toLowerCase().contains(kw)) {
                result.add(sp);
            }
        }
        return result;
    }
    
    public SanPhamDTO getById(String maSP) {
        for (SanPhamDTO sp : listSP) {
            if (sp.getMaSP().equalsIgnoreCase(maSP)) {
                return sp;
            }
        }
        return null;
    }

    public boolean isMaSPExist(String maSP) {
        for (SanPhamDTO sp : listSP) {
            if (sp.getMaSP().equalsIgnoreCase(maSP)) {
                return true;
            }
        }
        return false;
    }
}