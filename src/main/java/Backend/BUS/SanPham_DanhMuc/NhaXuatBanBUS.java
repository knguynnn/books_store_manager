package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.NhaXuatBanDAO;
import Backend.DTO.SanPham_DanhMuc.NhaXuatBanDTO;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

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

    private String validateCommon(NhaXuatBanDTO nxb) {
        if (nxb.getTenNXB() == null || nxb.getTenNXB().trim().isEmpty())
            return "Tên nhà xuất bản không được để trống!";
        if (nxb.getDiaChi() == null || nxb.getDiaChi().isEmpty())
            return "Địa chỉ nhà xuất bản không được để trống!";
        if (nxb.getEmail() == null || nxb.getEmail().isEmpty())
            return "Email nhà xuất bản không được để trống!";
        if (nxb.getSoDienThoai() == null || nxb.getSoDienThoai().isEmpty())
            return "Số điện thoại nhà xuất bản không được để trống!";
        return null;
    }

    /**
     * Thêm NXB: Tự động gán mã mới nhất từ hệ thống
     */
    public String validateAndAdd(NhaXuatBanDTO nxb) {
        String error = validateCommon(nxb);
        if (error != null) return error;

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
        String error = validateCommon(nxb);
        if (error != null) return error;

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

    public String nhapExcel(String filePath) {
        int count = 0;
        int skip = 0;
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    skip++;
                    continue;
                }

                // Đọc dữ liệu từ các cột (0: Tên, 1: Địa chỉ, 2: Email, 3: SĐT)
                Cell cellTen = row.getCell(0);
                Cell cellDiaChi = row.getCell(1);
                Cell cellEmail = row.getCell(2);
                Cell cellSdt = row.getCell(3);

                if (cellTen == null || cellDiaChi == null || cellEmail == null || cellSdt == null) {
                    skip++;
                    continue;
                }

                String tenNXB = cellTen.getStringCellValue().trim();
                String diaChi = cellDiaChi.getStringCellValue().trim();
                String email = cellEmail.getStringCellValue().trim();
                String sdt = "";
                
                // Xử lý cột SĐT (tránh lỗi nếu định dạng trong Excel là số)
                if (cellSdt.getCellType() == CellType.NUMERIC) {
                    sdt = String.valueOf((long) cellSdt.getNumericCellValue());
                } else {
                    sdt = cellSdt.getStringCellValue().trim();
                }

                if (tenNXB.isEmpty() || diaChi.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
                    skip++;
                    continue;
                }

                // Kiểm tra trùng tên Nhà xuất bản
                boolean isDuplicate = false;
                for (NhaXuatBanDTO existing : listNXB) {
                    if (existing.getTenNXB().equalsIgnoreCase(tenNXB)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    NhaXuatBanDTO nxb = new NhaXuatBanDTO();
                    nxb.setTenNXB(tenNXB);
                    nxb.setDiaChi(diaChi);
                    nxb.setEmail(email);
                    nxb.setSoDienThoai(sdt);
                    nxb.setMaNXB(generateNewMaNXB());

                    if (nxbDAO.insert(nxb)) {
                        listNXB.add(nxb); // Cập nhật list tạm để sinh mã cho dòng sau
                        count++;
                    } else {
                        skip++;
                    }
                } else {
                    skip++;
                }
            }
            refreshData();
            return "SUCCESS:" + count + ":" + skip;
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}