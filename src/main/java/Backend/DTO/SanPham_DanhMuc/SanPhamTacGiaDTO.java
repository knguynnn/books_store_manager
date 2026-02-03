package Backend.DTO.SanPham_DanhMuc;

public class SanPhamTacGiaDTO {
    private String maSP;
    private String maTG;

    public SanPhamTacGiaDTO() {}

    public SanPhamTacGiaDTO(String maSP, String maTG) {
        this.maSP = maSP;
        this.maTG = maTG;
    }

    // Getter và Setter
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getMaTG() { return maTG; }
    public void setMaTG(String maTG) { this.maTG = maTG; }
}