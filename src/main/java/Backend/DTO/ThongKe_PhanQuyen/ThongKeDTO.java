package Backend.DTO.ThongKe_PhanQuyen;

public class ThongKeDTO {
    
    // --- PHẦN 1: Dành cho 5 thẻ Dashboard ở phía trên ---
    public static class TongQuan {
        private double doanhThu;
        private double tongChi;
        private int soSachDaBan;
        private int soDonHang;
        private int soKhachHang;

        // Constructor mặc định
        public TongQuan() {}

        // Getter và Setter
        public double getDoanhThu() { return doanhThu; }
        public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }

        public double getTongChi() { return tongChi; }
        public void setTongChi(double tongChi) { this.tongChi = tongChi; }

        public int getSoSachDaBan() { return soSachDaBan; }
        public void setSoSachDaBan(int soSachDaBan) { this.soSachDaBan = soSachDaBan; }

        public int getSoDonHang() { return soDonHang; }
        public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }

        public int getSoKhachHang() { return soKhachHang; }
        public void setSoKhachHang(int soKhachHang) { this.soKhachHang = soKhachHang; }
    }

    // --- PHẦN 2: Dành cho bảng thống kê Quý ở phía dưới ---
    public static class ChiTiet {
        private String tenDoiTuong; // Tên NV, KH hoặc SP
        private double quy1;
        private double quy2;
        private double quy3;
        private double quy4;

        public ChiTiet(String ten, double q1, double q2, double q3, double q4) {
            this.tenDoiTuong = ten;
            this.quy1 = q1;
            this.quy2 = q2;
            this.quy3 = q3;
            this.quy4 = q4;
        }

        // Tính tổng cộng cho cột cuối cùng của bảng
        public double getTongCong() {
            return quy1 + quy2 + quy3 + quy4;
        }

        // Getters
        public String getTenDoiTuong() { return tenDoiTuong; }
        public double getQuy1() { return quy1; }
        public double getQuy2() { return quy2; }
        public double getQuy3() { return quy3; }
        public double getQuy4() { return quy4; }
    }
}