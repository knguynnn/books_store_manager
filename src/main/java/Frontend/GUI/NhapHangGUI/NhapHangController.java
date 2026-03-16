package Frontend.GUI.NhapHangGUI;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
