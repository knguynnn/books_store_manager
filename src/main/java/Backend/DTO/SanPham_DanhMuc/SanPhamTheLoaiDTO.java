package Backend.DTO.SanPham_DanhMuc;

public class SanPhamTheLoaiDTO {
    private String maSP;
    private String maTL;

    public SanPhamTheLoaiDTO() {}

    public SanPhamTheLoaiDTO(String maSP, String maTL) {
        this.maSP = maSP;
        this.maTL = maTL;
    }

    // Getter và Setter
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getMaTL() { return maTL; }
    public void setMaTL(String maTL) { this.maTL = maTL; }
}