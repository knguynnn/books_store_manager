package Frontend.GUI.NhapHangGUI;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import Backend.BUS.NCC_NhapHang.CTPhieuNhapHangBUS;
import Backend.BUS.NCC_NhapHang.NhaCungCapBUS;
import Backend.BUS.NCC_NhapHang.PhieuNhapHangBUS;
import Backend.BUS.NhanVien_TaiKhoan.NhanVienBUS;
import Backend.BUS.SanPham_DanhMuc.SanPhamBUS;
import Backend.BUS.SharedData;
import Backend.DTO.NCC_NhapHang.CTPhieuNhapHangDTO;
import Backend.DTO.NCC_NhapHang.NhaCungCapDTO;
import Backend.DTO.NCC_NhapHang.PhieuNhapHangDTO;
import Backend.DTO.NhanVien_TaiKhoan.NhanVienDTO;
import Backend.DTO.SanPham_DanhMuc.SanPhamDTO;
import Frontend.Compoent.XuatExcel;
import Frontend.Compoent.XuatPDF;

public class NhapHangController {
    private final NhapHangPanel view;

    private final PhieuNhapHangBUS phieuNhapBUS = new PhieuNhapHangBUS();
    private final CTPhieuNhapHangBUS ctPhieuNhapBUS = new CTPhieuNhapHangBUS();
    private final NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    private final NhanVienBUS nhanVienBUS = new NhanVienBUS();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();

    private final ArrayList<CTPhieuNhapHangDTO> chiTietTam = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private boolean isEditing = false;

    public NhapHangController(NhapHangPanel view) {
        this.view = view;
        initData();
        bindActions();
    }

    private void initData() {
        loadNhanVienCombo();
        loadNCCCombo();
        loadSanPhamCombo();
        loadPhieuNhapTable(phieuNhapBUS.getAll());
        resetForNewPhieu();
    }

    private void bindActions() {
        view.getBtnThemSP().addActionListener(e -> handleThemSanPham());
        view.getBtnXoaSP().addActionListener(e -> handleXoaSanPhamKhoiPhieu());

        view.getBtnLuu().addActionListener(e -> {
            if (isEditing) {
                handleSuaPhieuNhap();
            } else {
                handleLuuPhieuNhap();
            }
        });
        view.getBtnSua().addActionListener(e -> handleSuaPhieuNhap());
        view.getBtnXoa().addActionListener(e -> handleXoaPhieuNhap());
        view.getBtnMoi().addActionListener(e -> resetForNewPhieu());

        view.getBtnTimKiem().addActionListener(e -> handleTimKiem());
        view.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { handleTimKiem(); }
            @Override
            public void removeUpdate(DocumentEvent e) { handleTimKiem(); }
            @Override
            public void changedUpdate(DocumentEvent e) { handleTimKiem(); }
        });

        view.getBtnChonNV().addActionListener(e -> NhapHangActions.chooseFromCombo(view, view.getCboMaNV(), "nhân viên"));
        view.getBtnChonNCC().addActionListener(e -> NhapHangActions.chooseFromCombo(view, view.getCboMaNCC(), "nhà cung cấp"));
        view.getBtnChonSP().addActionListener(e -> NhapHangActions.chooseFromCombo(view, view.getCboMaSP(), "sản phẩm"));

        view.getTablePhieuNhap().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedPhieu();
            }
        });

        view.getTableChiTiet().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillSPFromSelectedChiTiet();
            }
        });

        view.getBtnXuat().addActionListener(e -> XuatExcel.xuat(view.getTablePhieuNhap(), "DanhSachPhieuNhap"));
        view.getBtnXuatPDF().addActionListener(e -> XuatPDF.xuat(view.getTablePhieuNhap(), "Danh sách phiếu nhập"));
        view.getBtnNhapExcel().addActionListener(e -> handleNhapExcel());
    }

    private void handleNhapExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn file Excel chi tiết nhập hàng");
        chooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx, *.xls)", "xlsx", "xls"));

        if (chooser.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File excelFile = chooser.getSelectedFile();
        importChiTietFromExcel(excelFile);
    }

    private void importChiTietFromExcel(File excelFile) {
        int importedRows = 0;
        int skippedRows = 0;
        List<String> errorRows = new ArrayList<>();
        String maPhieu = view.getTxtMaPhieuNhap().getText().trim();

        try (FileInputStream fis = new FileInputStream(excelFile);
                Workbook workbook = WorkbookFactory.create(fis)) {

            if (workbook.getNumberOfSheets() == 0) {
                JOptionPane.showMessageDialog(view, "File Excel không có sheet dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();

            for (int rowIndex = firstRow; rowIndex <= lastRow; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String maSP = readTextCell(row.getCell(0), formatter);
                String soLuongText = readTextCell(row.getCell(1), formatter);
                String donGiaText = readTextCell(row.getCell(2), formatter);

                if (maSP.isEmpty() && soLuongText.isEmpty() && donGiaText.isEmpty()) {
                    continue;
                }

                if (isLikelyHeaderRow(rowIndex, firstRow, maSP, soLuongText, donGiaText)) {
                    continue;
                }

                if (maSP.isEmpty()) {
                    skippedRows++;
                    collectError(errorRows, rowIndex, "Mã sản phẩm trống");
                    continue;
                }

                maSP = maSP.toUpperCase();

                if (!sanPhamBUS.isMaSPExist(maSP)) {
                    skippedRows++;
                    collectError(errorRows, rowIndex, "Không tìm thấy mã sản phẩm " + maSP);
                    continue;
                }

                int soLuong;
                long donGia;
                try {
                    soLuong = parseSoLuong(soLuongText);
                    donGia = parseDonGia(donGiaText);
                } catch (NumberFormatException ex) {
                    skippedRows++;
                    collectError(errorRows, rowIndex, ex.getMessage());
                    continue;
                }

                mergeChiTiet(maPhieu, maSP, soLuong, donGia);
                importedRows++;
            }

            loadChiTietTable();

            StringBuilder message = new StringBuilder();
            message.append("Nhập Excel hoàn tất.\n")
                    .append("- Dòng hợp lệ: ").append(importedRows).append("\n")
                    .append("- Dòng bỏ qua: ").append(skippedRows);

            if (!errorRows.isEmpty()) {
                message.append("\n\nChi tiết lỗi (tối đa 10 dòng):\n");
                for (String err : errorRows) {
                    message.append("- ").append(err).append("\n");
                }
            }

            JOptionPane.showMessageDialog(view, message.toString(), "Kết quả nhập Excel", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Không thể đọc file Excel: " + ex.getMessage(),
                    "Lỗi nhập Excel",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mergeChiTiet(String maPhieu, String maSP, int soLuong, long donGia) {
        CTPhieuNhapHangDTO existing = null;
        for (CTPhieuNhapHangDTO ct : chiTietTam) {
            if (ct.getMaSP().equalsIgnoreCase(maSP)) {
                existing = ct;
                break;
            }
        }

        if (existing != null) {
            existing.setSoLuong(existing.getSoLuong() + soLuong);
            existing.setDonGiaNhap(donGia);
            existing.setThanhTien(existing.getSoLuong() * existing.getDonGiaNhap());
            return;
        }

        CTPhieuNhapHangDTO ct = new CTPhieuNhapHangDTO();
        ct.setMaPhieuNhap(maPhieu);
        ct.setMaSP(maSP);
        ct.setSoLuong(soLuong);
        ct.setDonGiaNhap(donGia);
        ct.setThanhTien((long) soLuong * donGia);
        chiTietTam.add(ct);
    }

    private String readTextCell(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        String value = formatter.formatCellValue(cell);
        return value == null ? "" : value.trim();
    }

    private boolean isLikelyHeaderRow(int rowIndex, int firstRow, String maSP, String soLuong, String donGia) {
        if (rowIndex != firstRow) {
            return false;
        }
        String text = (maSP + " " + soLuong + " " + donGia).toLowerCase();
        return text.contains("mã") || text.contains("ma") || text.contains("số lượng") || text.contains("don gia") || text.contains("đơn giá");
    }

    private int parseSoLuong(String text) {
        if (text == null || text.isBlank()) {
            throw new NumberFormatException("Số lượng trống");
        }
        String digits = text.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            throw new NumberFormatException("Số lượng không hợp lệ");
        }
        int soLuong = Integer.parseInt(digits);
        if (soLuong <= 0) {
            throw new NumberFormatException("Số lượng phải > 0");
        }
        return soLuong;
    }

    private long parseDonGia(String text) {
        if (text == null || text.isBlank()) {
            throw new NumberFormatException("Đơn giá trống");
        }
        long donGia = NhapHangActions.parseMoney(text);
        if (donGia <= 0) {
            throw new NumberFormatException("Đơn giá phải > 0");
        }
        return donGia;
    }

    private void collectError(List<String> errorRows, int rowIndex, String reason) {
        if (errorRows.size() >= 10) {
            return;
        }
        errorRows.add("Dòng " + (rowIndex + 1) + ": " + reason);
    }

    private void loadNhanVienCombo() {
        view.getCboMaNV().removeAllItems();
        for (NhanVienDTO nv : nhanVienBUS.getAll()) {
            if (nv.isTrangThai()) {
                String hoTen = (nv.getHoNV() == null ? "" : nv.getHoNV().trim()) + " "
                        + (nv.getTenNV() == null ? "" : nv.getTenNV().trim());
                view.getCboMaNV().addItem(nv.getMaNV() + " - " + hoTen.trim());
            }
        }
    }

    private void loadNCCCombo() {
        view.getCboMaNCC().removeAllItems();
        for (NhaCungCapDTO ncc : nccBUS.getAll()) {
            view.getCboMaNCC().addItem(ncc.getMaNCC() + " - " + ncc.getTenNCC());
        }
    }

    private void loadSanPhamCombo() {
        sanPhamBUS.refreshData();
        view.getCboMaSP().removeAllItems();
        for (SanPhamDTO sp : sanPhamBUS.getActiveOnly()) {
            String item = sp.getMaSP() + " - " + sp.getTenSP() + " (Tồn: " + sp.getSoLuongTon() + ")";
            view.getCboMaSP().addItem(item);
        }
    }

    private void loadPhieuNhapTable(ArrayList<PhieuNhapHangDTO> dsPhieu) {
        view.getModelPhieuNhap().setRowCount(0);
        for (PhieuNhapHangDTO pn : dsPhieu) {
            view.getModelPhieuNhap().addRow(new Object[] {
                    pn.getMaPhieuNhap(),
                    pn.getNgayNhap() == null ? "" : dateFormat.format(pn.getNgayNhap()),
                    pn.getMaNV(),
                    pn.getMaNCC(),
                    NhapHangActions.formatMoney(pn.getTongTienNhap())
            });
        }
    }

    private void loadChiTietTable() {
        view.getModelChiTiet().setRowCount(0);
        for (CTPhieuNhapHangDTO ct : chiTietTam) {
            view.getModelChiTiet().addRow(new Object[] {
                    ct.getMaPhieuNhap(),
                    ct.getMaSP(),
                    ct.getSoLuong(),
                    NhapHangActions.formatMoney(ct.getDonGiaNhap()),
                    NhapHangActions.formatMoney(ct.getThanhTien())
            });
        }
        view.updateTongTienNhap();
    }

    private void fillFormFromSelectedPhieu() {
        int row = view.getTablePhieuNhap().getSelectedRow();
        if (row < 0) {
            return;
        }

        String maPhieu = String.valueOf(view.getModelPhieuNhap().getValueAt(row, 0));
        String ngayNhap = String.valueOf(view.getModelPhieuNhap().getValueAt(row, 1));
        String maNV = String.valueOf(view.getModelPhieuNhap().getValueAt(row, 2));
        String maNCC = String.valueOf(view.getModelPhieuNhap().getValueAt(row, 3));
        String tongTien = String.valueOf(view.getModelPhieuNhap().getValueAt(row, 4));

        view.getTxtMaPhieuNhap().setText(maPhieu);
        view.getTxtNgayNhap().setText(ngayNhap);
        NhapHangActions.selectComboByCode(view.getCboMaNV(), maNV);
        NhapHangActions.selectComboByCode(view.getCboMaNCC(), maNCC);
        view.getTxtTongTienNhap().setText(tongTien);

        chiTietTam.clear();
        chiTietTam.addAll(ctPhieuNhapBUS.getByMaPhieuNhap(maPhieu));
        loadChiTietTable();
        enterEditMode();
    }

    private void fillSPFromSelectedChiTiet() {
        int row = view.getTableChiTiet().getSelectedRow();
        if (row < 0) {
            return;
        }

        String maSP = String.valueOf(view.getModelChiTiet().getValueAt(row, 1));
        String soLuong = String.valueOf(view.getModelChiTiet().getValueAt(row, 2));
        String donGia = String.valueOf(view.getModelChiTiet().getValueAt(row, 3));

        NhapHangActions.selectComboByCode(view.getCboMaSP(), maSP);
        try {
            view.getSpinSoLuong().setValue(Integer.parseInt(soLuong.replaceAll("[^0-9]", "")));
        } catch (NumberFormatException ex) {
            view.getSpinSoLuong().setValue(1);
        }
        view.getTxtDonGia().setText(donGia.replaceAll("[^0-9]", ""));
    }

    private void handleThemSanPham() {
        if (!view.validateChiTiet()) {
            return;
        }

        String maSP = NhapHangActions.extractCode(String.valueOf(view.getCboMaSP().getSelectedItem()));
        int soLuong = (int) view.getSpinSoLuong().getValue();
        long donGia;

        try {
            donGia = NhapHangActions.parseMoney(view.getTxtDonGia().getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Đơn giá không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CTPhieuNhapHangDTO existing = null;
        for (CTPhieuNhapHangDTO ct : chiTietTam) {
            if (ct.getMaSP().equalsIgnoreCase(maSP)) {
                existing = ct;
                break;
            }
        }

        if (existing != null) {
            existing.setSoLuong(existing.getSoLuong() + soLuong);
            existing.setDonGiaNhap(donGia);
            existing.setThanhTien(existing.getSoLuong() * existing.getDonGiaNhap());
        } else {
            CTPhieuNhapHangDTO ct = new CTPhieuNhapHangDTO();
            ct.setMaPhieuNhap(view.getTxtMaPhieuNhap().getText().trim());
            ct.setMaSP(maSP);
            ct.setSoLuong(soLuong);
            ct.setDonGiaNhap(donGia);
            ct.setThanhTien(soLuong * donGia);
            chiTietTam.add(ct);
        }

        loadChiTietTable();
        view.getSpinSoLuong().setValue(1);
        view.getTxtDonGia().setText("");
    }

    private void handleXoaSanPhamKhoiPhieu() {
        int row = view.getTableChiTiet().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm cần xóa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (row < chiTietTam.size()) {
            chiTietTam.remove(row);
            loadChiTietTable();
        }
    }

    private void handleLuuPhieuNhap() {
        if (isEditing) {
            JOptionPane.showMessageDialog(view,
                    "Bạn đang ở chế độ sửa. Vui lòng bấm 'Cập nhật phiếu' để lưu thay đổi hoặc 'Làm mới' để tạo phiếu mới.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!view.validatePhieuNhap()) {
            return;
        }
        if (chiTietTam.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Phiếu nhập phải có ít nhất 1 sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
        pn.setMaNV(NhapHangActions.extractCode(String.valueOf(view.getCboMaNV().getSelectedItem())));
        pn.setMaNCC(NhapHangActions.extractCode(String.valueOf(view.getCboMaNCC().getSelectedItem())));

        boolean ok = phieuNhapBUS.taoPhieuNhap(pn, new ArrayList<>(chiTietTam));
        if (ok) {
            JOptionPane.showMessageDialog(view, "Lưu phiếu nhập thành công");
            Backend.BUS.EventBus.publish("SAN_PHAM_CHANGED");
            loadSanPhamCombo();
            loadPhieuNhapTable(phieuNhapBUS.getAll());
            resetForNewPhieu();
        } else {
            JOptionPane.showMessageDialog(view, "Lưu phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSuaPhieuNhap() {
        if (!isEditing) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu nhập cần sửa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String maPhieu = view.getTxtMaPhieuNhap().getText().trim();
        if (maPhieu.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu nhập để sửa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!view.validatePhieuNhap() || chiTietTam.isEmpty()) {
            return;
        }

        PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
        pn.setMaPhieuNhap(maPhieu);
        pn.setNgayNhap(new Timestamp(System.currentTimeMillis()));
        pn.setMaNV(NhapHangActions.extractCode(String.valueOf(view.getCboMaNV().getSelectedItem())));
        pn.setMaNCC(NhapHangActions.extractCode(String.valueOf(view.getCboMaNCC().getSelectedItem())));

        boolean ok = phieuNhapBUS.suaPhieuNhap(pn, new ArrayList<>(chiTietTam));
        if (ok) {
            JOptionPane.showMessageDialog(view, "Cập nhật phiếu nhập thành công");
            Backend.BUS.EventBus.publish("SAN_PHAM_CHANGED");
            loadSanPhamCombo();
            loadPhieuNhapTable(phieuNhapBUS.getAll());
            resetForNewPhieu();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleXoaPhieuNhap() {
        String maPhieu = view.getTxtMaPhieuNhap().getText().trim();
        if (maPhieu.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu nhập để xóa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa phiếu nhập " + maPhieu + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = phieuNhapBUS.xoaPhieuNhap(maPhieu);
        if (ok) {
            JOptionPane.showMessageDialog(view, "Xóa phiếu nhập thành công");
            Backend.BUS.EventBus.publish("SAN_PHAM_CHANGED");
            loadSanPhamCombo();
            loadPhieuNhapTable(phieuNhapBUS.getAll());
            resetForNewPhieu();
        } else {
            JOptionPane.showMessageDialog(view, "Xóa phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleTimKiem() {
        String keyword = view.getSearchField().getText().trim();
        if (keyword.isEmpty()) {
            loadPhieuNhapTable(phieuNhapBUS.getAll());
            return;
        }
        loadPhieuNhapTable(phieuNhapBUS.search(keyword));
    }

    private void resetForNewPhieu() {
        exitEditMode();
        view.resetForm();
        view.getTxtMaPhieuNhap().setText(phieuNhapBUS.generateId());
        view.getTxtNgayNhap().setText(dateFormat.format(new Timestamp(System.currentTimeMillis())));

        chiTietTam.clear();
        loadChiTietTable();

        if (SharedData.taiKhoanHienTai != null) {
            NhapHangActions.selectComboByCode(view.getCboMaNV(), SharedData.taiKhoanHienTai.getMaNV());
        }
    }

    private void enterEditMode() {
        isEditing = true;
        view.getBtnLuu().setText("Cập nhật phiếu");
    }

    private void exitEditMode() {
        isEditing = false;
        view.getBtnLuu().setText("Lưu phiếu");
    }
}
