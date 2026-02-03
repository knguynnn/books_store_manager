package Backend.DTO.SanPham_DanhMuc;

public class TheLoaiDTO {
    private String maTL;
    private String tenTL;

    public TheLoaiDTO() {}

    public TheLoaiDTO(String maTL, String tenTL) {
        this.maTL = maTL;
        this.tenTL = tenTL;
    }

    public String getMaTL() { return maTL; }
    public void setMaTL(String maTL) { this.maTL = maTL; }

    public String getTenTL() { return tenTL; }
    public void setTenTL(String tenTL) { this.tenTL = tenTL; }
}