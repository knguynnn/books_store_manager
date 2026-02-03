package Backend.DTO.SanPham_DanhMuc;

public class SanPhamDTO {
    private String maSP;
    private String tenSP;
    private int soLuongTon;
    private String donViTinh;
    private long donGia;
    private int namXuatBan;
    private String maNXB;
    private String maNCC;
    private String moTa;
    private boolean trangThai;

    public SanPhamDTO() {}

    public SanPhamDTO(String maSP, String tenSP, int soLuongTon, String donViTinh, long donGia, 
                      int namXuatBan, String maNXB, String maNCC, String moTa, boolean trangThai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuongTon = soLuongTon;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.namXuatBan = namXuatBan;
        this.maNXB = maNXB;
        this.maNCC = maNCC;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    // Getters và Setters
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public long getDonGia() { return donGia; }
    public void setDonGia(long donGia) { this.donGia = donGia; }

    public int getNamXuatBan() { return namXuatBan; }
    public void setNamXuatBan(int namXuatBan) { this.namXuatBan = namXuatBan; }

    public String getMaNXB() { return maNXB; }
    public void setMaNXB(String maNXB) { this.maNXB = maNXB; }

    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}