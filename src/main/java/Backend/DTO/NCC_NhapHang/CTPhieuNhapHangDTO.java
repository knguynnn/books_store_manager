package Backend.DTO.NCC_NhapHang;

public class CTPhieuNhapHangDTO {
    private String maPhieuNhap;
    private String maSP;
    private int soLuong;
    private long donGiaNhap;
    private long thanhTien;

    public CTPhieuNhapHangDTO() {}

    public CTPhieuNhapHangDTO(String maPhieuNhap, String maSP, int soLuong, long donGiaNhap, long thanhTien) {
        this.maPhieuNhap = maPhieuNhap;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGiaNhap = donGiaNhap;
        this.thanhTien = thanhTien;
    }

    // Getter và Setter
    public String getMaPhieuNhap() { return maPhieuNhap; }
    public void setMaPhieuNhap(String maPhieuNhap) { this.maPhieuNhap = maPhieuNhap; }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public long getDonGiaNhap() { return donGiaNhap; }
    public void setDonGiaNhap(long donGiaNhap) { this.donGiaNhap = donGiaNhap; }

    public long getThanhTien() { return thanhTien; }
    public void setThanhTien(long thanhTien) { this.thanhTien = thanhTien; }
}