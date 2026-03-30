package Backend.BUS.SanPham_DanhMuc;

import Backend.DAO.SanPham_DanhMuc.SanPhamDAO;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import Backend.BUS.NCC_NhapHang.CTPhieuNhapHangBUS;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class SanPhamBUS {
    private ArrayList<SanPhamDTO> listSP;
    private SanPhamDAO spDAO;
    private CTPhieuNhapHangBUS ctNhapBUS = new CTPhieuNhapHangBUS();
    private TacGiaBUS tgBUS = new TacGiaBUS();
    private TheLoaiBUS tlBUS = new TheLoaiBUS();
    private NhaXuatBanBUS nxbBUS = new NhaXuatBanBUS();

    public SanPhamBUS() {
        spDAO = new SanPhamDAO();
        refreshData();
    }

    public void refreshData() {
        this.listSP = spDAO.getAll();
    }

    public ArrayList<SanPhamDTO> getAll() {
        return listSP;
    }

    public ArrayList<SanPhamDTO> getActiveOnly() {
        ArrayList<SanPhamDTO> activeList = new ArrayList<>();
        for (SanPhamDTO sp : listSP) {
            if (sp.isTrangThai()) activeList.add(sp);
        }
        return activeList;
    }

    public boolean isMaSPExist(String maSP) {
        for (SanPhamDTO sp : listSP) {
            if (sp.getMaSP().equalsIgnoreCase(maSP)) return true;
        }
        return false;
    }

    public SanPhamDTO getById(String maSP) {
        for (SanPhamDTO sp : listSP) {
            if (sp.getMaSP().equalsIgnoreCase(maSP)) return sp;
        }
        return null;
    }

    public boolean deleteSanPham(String maSP) {
        if (spDAO.delete(maSP)) {
            refreshData();
            return true;
        }
        return false;
    }

    private String validateCommon(SanPhamDTO sp) {
        if (sp.getTenSP() == null || sp.getTenSP().trim().isEmpty())
            return "Tên sản phẩm không được để trống!";
        if (sp.getMaTL() == null || sp.getMaTL().isEmpty())
            return "Vui lòng chọn Thể loại!";
        if (sp.getMaTG() == null || sp.getMaTG().isEmpty())
            return "Vui lòng chọn Tác giả!";
        if (sp.getMaNXB() == null || sp.getMaNXB().isEmpty())
            return "Vui lòng chọn Nhà xuất bản!";
        if (sp.getPhanTram() == 0)
            return "Mức lợi nhuận chưa được điều chỉnh!";
        return null;
    }

    public long tinhGiaBan(long giaNhap, int phanTram) {
        if (phanTram == 0) return 0;
        return Math.round(giaNhap * (1 + phanTram / 100.0) / 100.0) * 100;
    }

    public long getGiaNhapMoiNhat(String maSP) {
        return ctNhapBUS.getLatestGiaNhapBySP(maSP);
    }

    public String validateAndAdd(SanPhamDTO sp, File selectedFile) {
        String error = validateCommon(sp);
        if (error != null) return error;

        sp.setMaSP(generateNewMaSP());
        sp.setTrangThai(true);
        sp.setDonViTinh("Cuốn");
        sp.setSoLuongTon(0);
        sp.setDonGia(0);

        // Ưu tiên: selectedFile (chọn từ GUI) > hinhAnh đã set (từ Excel) > ""
        if (selectedFile != null) {
            sp.setHinhAnh(saveImage(selectedFile));
        } else if (sp.getHinhAnh() == null || sp.getHinhAnh().isEmpty()) {
            sp.setHinhAnh("");
        }
        // Nếu sp.getHinhAnh() đã có giá trị (từ Excel) thì giữ nguyên, không set lại

        if (spDAO.insert(sp)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống!";
    }

    public String validateAndUpdate(SanPhamDTO sp, File selectedFile) {
        String error = validateCommon(sp);
        if (error != null) return error;
        
        sp.setDonViTinh("Cuốn");
        SanPhamDTO current = getById(sp.getMaSP());
        if (current == null) return "Không tìm thấy sản phẩm!";

        long giaNhap = getGiaNhapMoiNhat(sp.getMaSP());
        if (giaNhap <= 0) giaNhap = current.getDonGia(); 
        
        sp.setDonGia(tinhGiaBan(giaNhap, sp.getPhanTram()));
        sp.setSoLuongTon(current.getSoLuongTon());
        
        if (selectedFile != null) {
            sp.setHinhAnh(saveImage(selectedFile));
        } else if (sp.getHinhAnh() == null || sp.getHinhAnh().isEmpty()) {
            sp.setHinhAnh(current.getHinhAnh());
        }

        if (spDAO.update(sp)) {
            refreshData();
            return "SUCCESS";
        }
        return "Lỗi hệ thống!";
    }

    public ArrayList<SanPhamDTO> search(String keyword) {
        return spDAO.search(keyword);
    }

    public ArrayList<SanPhamDTO> searchAdvanced(String maTG, String maTL, int maxSoLuong, long maxGia) {
        return spDAO.searchAdvanced(maTG, maTL, maxSoLuong, maxGia);
    }

    public String generateNewMaSP() {
        String maxMa = spDAO.getMaxMaSP();
        if (maxMa == null || maxMa.isEmpty() || !maxMa.startsWith("SP")) return "SP001";
        int num = Integer.parseInt(maxMa.substring(2)) + 1;
        return String.format("SP%03d", num);
    }

    private String saveImage(File sourceFile) {
        if (sourceFile == null) return "";
        try {
            String fileName = System.currentTimeMillis() + "_" + sourceFile.getName();
            File destFile = new File("src/main/resources/images/product/" + fileName);
            destFile.getParentFile().mkdirs();
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            return "";
        }
    }

    public String nhapExcel(String filePath) {
        int success = 0, skip = 0;
        StringBuilder skippedReasons = new StringBuilder();

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath))) {
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) { skip++; continue; }

                String tenSP  = getCellString(row, 0);
                String maTG   = getCellString(row, 1);
                String maTL   = getCellString(row, 2);
                String maNXB  = getCellString(row, 3);
                int    namXB  = (int) getNumericCell(row, 4);
                String moTa   = getCellString(row, 5);
                int    phanTram = (int) getNumericCell(row, 6);
                String hinhAnh = getCellString(row, 7); 

                // 1. Kiểm tra thiếu dữ liệu bắt buộc
                if (tenSP.isEmpty() || maTG.isEmpty() || maTL.isEmpty()
                        || maNXB.isEmpty()) {
                    skip++;
                    skippedReasons.append("Dòng ").append(i + 1).append(": Thiếu dữ liệu bắt buộc\n");
                    continue;
                }

                // 3. Kiểm tra mã TG có tồn tại không
                if (tgBUS.getById(maTG) == null) {
                    skip++;
                    skippedReasons.append("Dòng ").append(i + 1)
                        .append(": Mã tác giả \"").append(maTG).append("\" không tồn tại\n");
                    continue;
                }

                // 4. Kiểm tra mã TL có tồn tại không
                if (tlBUS.getById(maTL) == null) {
                    skip++;
                    skippedReasons.append("Dòng ").append(i + 1)
                        .append(": Mã thể loại \"").append(maTL).append("\" không tồn tại\n");
                    continue;
                }

                // 5. Kiểm tra mã NXB có tồn tại không
                if (nxbBUS.getById(maNXB) == null) {
                    skip++;
                    skippedReasons.append("Dòng ").append(i + 1)
                        .append(": Mã NXB \"").append(maNXB).append("\" không tồn tại\n");
                    continue;
                }

                // Tất cả hợp lệ → thêm mới
                SanPhamDTO sp = new SanPhamDTO();
                sp.setTenSP(tenSP);
                sp.setMaTG(maTG);
                sp.setMaTL(maTL);
                sp.setMaNXB(maNXB);
                sp.setNamXuatBan(namXB);
                sp.setMoTa(moTa);
                sp.setPhanTram(phanTram);
                sp.setHinhAnh(hinhAnh);
                sp.setDonViTinh("Cuốn");
                sp.setDonGia(0);
                sp.setSoLuongTon(0);
                sp.setTrangThai(true);

                String result = validateAndAdd(sp, null);
                if (result.equals("SUCCESS")) {
                    success++;
                } else {
                    skip++;
                    skippedReasons.append("Dòng ").append(i + 1)
                        .append(": ").append(result).append("\n");
                }
            }
            refreshData();

            String summary = "SUCCESS:" + success + ":" + skip;
            if (skippedReasons.length() > 0)
                summary += ":" + skippedReasons.toString();
            return summary;

        } catch (Exception e) {
            return "ERROR:" + e.getMessage();
        }
    }

    private String getCellString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int)cell.getNumericCellValue());
        return cell.getStringCellValue().trim();
    }

    private double getNumericCell(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return 0;
        try { return cell.getNumericCellValue(); } catch (Exception e) { return 0; }
    }
}