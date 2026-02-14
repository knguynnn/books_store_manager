package Backend.BUS.NhanVien_TaiKhoan;

import Backend.DAO.NhanVien_TaiKhoan.NhanVienDAO;
import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;
import java.util.ArrayList;

public class NhanVienBUS {
    private ArrayList<NhanVienDTO> listNhanVien;
    private NhanVienDAO dao = new NhanVienDAO();

    public NhanVienBUS() {
        // Khi khởi tạo BUS thì load dữ liệu từ DB lên luôn
        refreshData();
    }

    // Load lại dữ liệu từ Database vào List
    public void refreshData() {
        listNhanVien = dao.getAll();
    }

    // Lấy toàn bộ danh sách (cả nhân viên nghỉ việc)
    public ArrayList<NhanVienDTO> getAll() {
        if (listNhanVien == null) refreshData();
        return listNhanVien;
    }

    // Lấy nhân viên theo Mã NV
    public NhanVienDTO getNhanVienByID(String maNV) {
        for (NhanVienDTO nv : listNhanVien) {
            if (nv.getMaNV().equals(maNV)) {
                return nv;
            }
        }
        return null;
    }

    // Thêm nhân viên
    public boolean addNhanVien(NhanVienDTO nv) {
        // Kiểm tra mã trùng (dù đã tự sinh nhưng vẫn nên check)
        if (getNhanVienByID(nv.getMaNV()) != null) {
            return false;
        }
        
        // Gọi DAO thêm vào DB
        if (dao.insert(nv)) {
            listNhanVien.add(nv); // Thêm thành công thì cập nhật list local
            return true;
        }
        return false;
    }

    // Cập nhật nhân viên
    public boolean updateNhanVien(NhanVienDTO nv) {
        if (dao.update(nv)) {
            // Cập nhật lại list local
            for (int i = 0; i < listNhanVien.size(); i++) {
                if (listNhanVien.get(i).getMaNV().equals(nv.getMaNV())) {
                    listNhanVien.set(i, nv);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    // Xóa nhân viên (Khóa tài khoản)
    public boolean deleteNhanVien(String maNV) {
        if (dao.delete(maNV)) {
            // Cập nhật trạng thái trong list local thay vì xóa hẳn
            for (NhanVienDTO nv : listNhanVien) {
                if (nv.getMaNV().equals(maNV)) {
                    nv.setTrangThai(false); // Đổi trạng thái thành Đã nghỉ
                    break;
                }
            }
            return true;
        }
        return false;
    }

    // Tìm kiếm nhân viên (Theo Mã, Tên, SĐT)
    public ArrayList<NhanVienDTO> search(String keyword) {
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        keyword = keyword.toLowerCase();
        
        for (NhanVienDTO nv : listNhanVien) {
            // Tìm theo Mã NV, Tên NV hoặc SĐT
            if (nv.getMaNV().toLowerCase().contains(keyword) || 
                (nv.getHoNV() + " " + nv.getTenNV()).toLowerCase().contains(keyword) ||
                nv.getSoDienThoai().contains(keyword)) {
                result.add(nv);
            }
        }
        return result;
    }

    // Hàm tự động sinh mã NV mới (NV001, NV002...)
    public String generateNewMaNV() {
        int maxId = 0;
        for (NhanVienDTO nv : listNhanVien) {
            // Chỉ xét các mã đúng định dạng NV + Số (VD: NV001)
            if (nv.getMaNV().matches("^NV\\d+$")) {
                try {
                    // Lấy phần số: NV005 -> 5
                    int id = Integer.parseInt(nv.getMaNV().replace("NV", ""));
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua mã lỗi
                }
            }
        }
        // Trả về mã mới tăng thêm 1, format 3 chữ số (NV006)
        return "NV" + String.format("%03d", maxId + 1);
    }
}