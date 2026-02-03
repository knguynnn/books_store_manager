package Backend.DTO.NhanVien_TaiKhoan;

public class TaiKhoanDTO {
    private String tenDangNhap;
    private String matKhau;
    private String maNV;
    private String quyen;
    private boolean trangThai;

    public TaiKhoanDTO() {}

    public TaiKhoanDTO(String tenDangNhap, String matKhau, String maNV, String quyen, boolean trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
        this.quyen = quyen;
        this.trangThai = trangThai;
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}