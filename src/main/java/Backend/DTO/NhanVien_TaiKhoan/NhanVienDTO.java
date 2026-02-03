package Backend.DTO.NhanVien_TaiKhoan;


// Mối quan hệ 1-1: Bạn thấy NhanVien, ChiTietNhanVien và TaiKhoan đều dùng chung maNV. 
// Khi code giao diện Thêm nhân viên, bạn sẽ phải gọi DAO của cả 3 bảng này để lưu dữ liệu cùng lúc.


public class NhanVienDTO {
    private String maNV;
    private String hoNV;
    private String tenNV;
    private String chucVu;
    private boolean trangThai;

    public NhanVienDTO() {}

    public NhanVienDTO(String maNV, String hoNV, String tenNV, String chucVu, boolean trangThai) {
        this.maNV = maNV;
        this.hoNV = hoNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.trangThai = trangThai;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoNV() { return hoNV; }
    public void setHoNV(String hoNV) { this.hoNV = hoNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
