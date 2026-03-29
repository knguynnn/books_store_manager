package Backend.DTO.SanPham_DanhMuc;

public class SanPhamDTO {
    private String maSP;
    private String hinhAnh;
    private String tenSP;
    private String maTG; 
    private String maTL; 
    private int soLuongTon;
    private String donViTinh;
    private long donGia;
    private int phanTram;
    private int namXuatBan;
    private String maNXB;
    private String moTa;
    private boolean trangThai;

    public SanPhamDTO() {}

    // Cập nhật Constructor đầy đủ tham số
    public SanPhamDTO(String maSP, String hinhAnh, String tenSP, String maTG, String maTL, int soLuongTon, 
                      String donViTinh, long donGia, int phanTram, int namXuatBan, String maNXB, 
                      String moTa, boolean trangThai) {
        this.maSP = maSP;
        this.hinhAnh = hinhAnh; 
        this.tenSP = tenSP;
        this.maTG = maTG;
        this.maTL = maTL;
        this.soLuongTon = soLuongTon;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.phanTram = phanTram;
        this.namXuatBan = namXuatBan;
        this.maNXB = maNXB;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    // --- Getters và Setters cho thuộc tính mới ---
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    // --- Các Getters và Setters cũ ---
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getMaTG() { return maTG; }
    public void setMaTG(String maTG) { this.maTG = maTG; }

    public String getMaTL() { return maTL; }
    public void setMaTL(String maTL) { this.maTL = maTL; }

    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public long getDonGia() { return donGia; }
    public void setDonGia(long donGia) { this.donGia = donGia; }

    public int getPhanTram() { return phanTram; }
    public void setPhanTram(int phanTram) { this.phanTram = phanTram; }

    public int getNamXuatBan() { return namXuatBan; }
    public void setNamXuatBan(int namXuatBan) { this.namXuatBan = namXuatBan; }

    public String getMaNXB() { return maNXB; }
    public void setMaNXB(String maNXB) { this.maNXB = maNXB; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}