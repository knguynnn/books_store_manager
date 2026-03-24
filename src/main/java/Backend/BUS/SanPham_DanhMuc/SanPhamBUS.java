package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Year;

public class SanPhamBUS {
    private ArrayList<SanPhamDTO> listSP;
    private SanPhamDAO spDAO;

    public SanPhamBUS() {
        spDAO = new SanPhamDAO();
        refreshData();
    }

    public ArrayList<SanPhamDTO> getActiveOnly() {
        ArrayList<SanPhamDTO> activeList = new ArrayList<>();
        for (SanPhamDTO sp : listSP) {
            if (sp.isTrangThai()) {
                activeList.add(sp);
            }
        }
        return activeList; 
    }

    public boolean isMaSPExist(String maSP) {
        for (SanPhamDTO sp : listSP) {
            if (sp.getMaSP().equalsIgnoreCase(maSP)) {
                return true;
            }
        }
        return false;
    }

    public void refreshData() {
        this.listSP = spDAO.getAll(); 
    }

    public ArrayList<SanPhamDTO> getAll() {
        return listSP;
    }

    public boolean deleteSanPham(String maSP) {
        if (spDAO.delete(maSP)) {
            refreshData(); // Load lại listSP (đã lọc TrangThai=1)
            return true;
        }
        return false;
    }

    /**
     * Logic Thêm: Tự động tạo mã, xử lý lưu file ảnh vật lý
     */
    public String validateAndAdd(SanPhamDTO sp, File selectedFile) {
        // 1. Validate dữ liệu cơ bản
        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty()) return "Tên sản phẩm không được để trống!";
        if (sp.getMaTL() == null || sp.getMaTL().isEmpty()) return "Vui lòng chọn Thể loại!";
        if (sp.getMaTG() == null || sp.getMaTG().isEmpty()) return "Vui lòng chọn Tác giả!";
        if (sp.getMaNXB() == null || sp.getMaNXB().isEmpty()) return "Vui lòng chọn Nhà xuất bản!";
        if (sp.getNamXuatBan() <= 0 || sp.getNamXuatBan() > Year.now().getValue()) return "Năm xuất bản không hợp lệ!";

        // 2. Ép buộc tạo mã mới từ hệ thống
        sp.setMaSP(generateNewMaSP());
        sp.setTrangThai(true); // Mặc định là đang kinh doanh
        
        // Giá và số lượng mặc định là 0 khi mới tạo (sẽ cập nhật qua phiếu nhập)
        sp.setSoLuongTon(0);
        sp.setDonGia(0);
        sp.setDonViTinh("Cuốn");
        // 3. Xử lý lưu ảnh
        if (selectedFile != null) {
            String newImageName = saveImage(selectedFile);
            sp.setHinhAnh(newImageName);
        } else {
            sp.setHinhAnh(""); 
        }

        if (spDAO.insert(sp)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể thêm sản phẩm!";
    }

    /**
     * Logic Sửa: Giữ nguyên mã, cập nhật thông tin và ảnh mới nếu có
     */
    public String validateAndUpdate(SanPhamDTO sp, File selectedFile) {
        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty()) return "Tên sản phẩm không được để trống!";

        // Nếu có chọn file ảnh mới thì mới lưu, không thì giữ tên ảnh cũ đã có trong DTO
        if (selectedFile != null) {
            String newImageName = saveImage(selectedFile);
            sp.setHinhAnh(newImageName);
        }

        if (spDAO.update(sp)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống: Không thể cập nhật sản phẩm!";
    }

    /**
     * Xử lý lưu file ảnh vào thư mục resources của dự án
     */
    private String saveImage(File sourceFile) {
        if (sourceFile == null) return "";
        try {
            String fileName = System.currentTimeMillis() + "_" + sourceFile.getName(); 
            File destFile = new File("src/main/resources/images/product/" + fileName);
            destFile.getParentFile().mkdirs();
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            System.err.println("❌ Lỗi lưu file ảnh: " + e.getMessage());
            return "";
        }
    }

    public String generateNewMaSP() {
        String maxMa = spDAO.getMaxMaSP();
        if (maxMa == null || maxMa.isEmpty() || !maxMa.startsWith("SP")) {
            return "SP001";
        }
        int num = Integer.parseInt(maxMa.substring(2)) + 1;
        return String.format("SP%03d", num);
    }

    public ArrayList<SanPhamDTO> search(String keyword) {
        return spDAO.search(keyword); 
    }
    
    public SanPhamDTO getById(String maSP) {
        for (SanPhamDTO sp : listSP) {
            if (sp.getMaSP().equalsIgnoreCase(maSP)) return sp;
        }
        return null;
    }

    public ArrayList<SanPhamDTO> searchAdvanced(String maTG, String maTL, int maxSoLuong, long maxGia) {
        return spDAO.searchAdvanced(maTG, maTL, maxSoLuong, maxGia);
    }

}