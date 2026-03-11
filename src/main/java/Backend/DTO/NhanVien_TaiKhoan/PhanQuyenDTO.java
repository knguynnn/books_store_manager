package Backend.DTO.NhanVien_TaiKhoan;

public class PhanQuyenDTO {
    private String maQuyen;
    private String tenQuyen;

    // Chi tiết quyền (1: Có quyền, 0: Không có quyền)
    // Bạn có thể dùng boolean, nhưng dùng int (0/1) sẽ khớp với kiểu TinyInt trong MySQL hơn
    private int qlKhachHang;
    private int qlNhanVien;
    private int qlSanPham;
    private int qlNhapHang;
    private int qlBanHang;
    private int qlNhaCungCap;
    private int qlThongKe;
    private int qlPhanQuyen;

    // Constructor không tham số
    public PhanQuyenDTO() {
    }

    // Constructor đầy đủ tham số
    public PhanQuyenDTO(String maQuyen, String tenQuyen, int qlKhachHang, int qlNhanVien, 
                        int qlSanPham, int qlNhapHang, int qlBanHang, int qlNhaCungCap, 
                        int qlThongKe, int qlPhanQuyen) {
        this.maQuyen = maQuyen;
        this.tenQuyen = tenQuyen;
        this.qlKhachHang = qlKhachHang;
        this.qlNhanVien = qlNhanVien;
        this.qlSanPham = qlSanPham;
        this.qlNhapHang = qlNhapHang;
        this.qlBanHang = qlBanHang;
        this.qlNhaCungCap = qlNhaCungCap;
        this.qlThongKe = qlThongKe;
        this.qlPhanQuyen = qlPhanQuyen;
    }

    // --- Getter và Setter ---
    public String getMaQuyen() { return maQuyen; }
    public void setMaQuyen(String maQuyen) { this.maQuyen = maQuyen; }

    public String getTenQuyen() { return tenQuyen; }
    public void setTenQuyen(String tenQuyen) { this.tenQuyen = tenQuyen; }

    public int getQlKhachHang() { return qlKhachHang; }
    public void setQlKhachHang(int qlKhachHang) { this.qlKhachHang = qlKhachHang; }

    public int getQlNhanVien() { return qlNhanVien; }
    public void setQlNhanVien(int qlNhanVien) { this.qlNhanVien = qlNhanVien; }

    public int getQlSanPham() { return qlSanPham; }
    public void setQlSanPham(int qlSanPham) { this.qlSanPham = qlSanPham; }

    public int getQlNhapHang() { return qlNhapHang; }
    public void setQlNhapHang(int qlNhapHang) { this.qlNhapHang = qlNhapHang; }

    public int getQlBanHang() { return qlBanHang; }
    public void setQlBanHang(int qlBanHang) { this.qlBanHang = qlBanHang; }

    public int getQlNhaCungCap() { return qlNhaCungCap; }
    public void setQlNhaCungCap(int qlNhaCungCap) { this.qlNhaCungCap = qlNhaCungCap; }

    public int getQlThongKe() { return qlThongKe; }
    public void setQlThongKe(int qlThongKe) { this.qlThongKe = qlThongKe; }

    public int getQlPhanQuyen() { return qlPhanQuyen; }
    public void setQlPhanQuyen(int qlPhanQuyen) { this.qlPhanQuyen = qlPhanQuyen; }
}