package Backend.controller.KhachHang;

import Backend.BUS.KhachHang_BanHang.KhachHangBUS;
import Backend.DTO.KhachHang_BanHang.KhachHangDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/khachhang")
@jakarta.servlet.annotation.MultipartConfig
public class KhachHangServlet extends HttpServlet {
    private KhachHangBUS bus = new KhachHangBUS();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                showList(request, response);
                break;
            case "search":
                searchKhachHang(request, response);
                break;
            case "delete":
                deleteKhachHang(request, response);
                break;
            case "add-form":
                showAddForm(request, response);
                break;
            case "edit-form":
                showEditForm(request, response);
                break;
            case "export-excel":
                exportExcel(request, response);
                break;
            default:
                showList(request, response);
                break;
        }
    }

    // 1. Hiển thị danh sách
    private void showList(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
            ArrayList<KhachHangDTO> list = bus.getActiveOnly(); 
            
            // Thêm dòng này để chuẩn bị sẵn mã mới cho cái Modal thêm nhanh
            String autoMaKH = bus.generateNewMaKH(); 
            
            request.setAttribute("dsKhachHang", list);
            request.setAttribute("autoMaKH", autoMaKH); // Gửi mã này sang trang list
            
            request.getRequestDispatcher("/WEB-INF/KhachHang/khachhang-list.jsp").forward(request, response);
    }

    // 2. Tìm kiếm
    private void searchKhachHang(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        ArrayList<KhachHangDTO> list;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = bus.search(keyword); // Sửa lại đúng tên hàm searchInDB trong DAO của bạn
        } else {
            list = bus.getActiveOnly();
        }
        
        request.setAttribute("dsKhachHang", list);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/WEB-INF/KhachHang/khachhang-list.jsp").forward(request, response);
    }

    // 3. Xóa
    private void deleteKhachHang(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String maKH = request.getParameter("maKH");
        if (maKH != null) {
            bus.deleteKhachHang(maKH); 
        }
        response.sendRedirect("khachhang?action=list");
    }

    // 4. Mở form Thêm (Tự động sinh mã mới)
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String autoMaKH = bus.generateNewMaKH(); 
        request.setAttribute("autoMaKH", autoMaKH);
        request.getRequestDispatcher("/WEB-INF/KhachHang/khachhang-form.jsp").forward(request, response);
    }

    // 5. Mở form Sửa (Lấy dữ liệu cũ đổ vào form)
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String maKH = request.getParameter("maKH");
        KhachHangDTO existingKH = null;
        
        for (KhachHangDTO kh : bus.getAll()) {
            if (kh.getMaKH().equals(maKH)) {
                existingKH = kh;
                break;
            }
        }
        
        request.setAttribute("khachHang", existingKH);
        request.getRequestDispatcher("/WEB-INF/KhachHang/khachhang-form.jsp").forward(request, response);
    }

    // 6. Nơi tiếp nhận dữ liệu từ Form gửi lên (Thêm/Sửa vào DB)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Thiết lập bảng mã UTF-8 để không bị lỗi font tiếng Việt khi nhận dữ liệu từ Form
        request.setCharacterEncoding("UTF-8"); 
        String action = request.getParameter("action");

        // TRƯỜNG HỢP A: XỬ LÝ NHẬP DỮ LIỆU TỪ EXCEL (IMPORT)
        if ("import-excel".equals(action)) {
            try {
                // Lấy phần dữ liệu file từ input có tên là "fileExcel" trong Modal Import
                var filePart = request.getPart("fileExcel"); 
                if (filePart != null && filePart.getSize() > 0) {
                    // Gọi hàm xử lý logic đọc file Excel từ lớp BUS
                    bus.importFromExcel(filePart.getInputStream());
                }
            } catch (Exception e) {
                System.out.println("❌ Lỗi khi nhập file Excel trong Servlet: " + e.getMessage());
                e.printStackTrace();
            }
            // Sau khi nhập xong, quay về trang danh sách để thấy dữ liệu mới
            response.sendRedirect("khachhang?action=list");
            return;
        }

        // TRƯỜNG HỢP B: XỬ LÝ THÊM HOẶC SỬA THÔNG TIN KHÁCH HÀNG
        // Lấy thông tin từ các ô nhập liệu (dùng chung cho cả Form Add Modal và Edit Modal)
        String maKH = request.getParameter("maKH");
        String hoKH = request.getParameter("hoKH");
        String tenKH = request.getParameter("tenKH");
        String soDienThoai = request.getParameter("soDienThoai");
        String email = request.getParameter("email");
        String diaChi = request.getParameter("diaChi");

        // Đóng gói dữ liệu vào đối tượng DTO
        KhachHangDTO kh = new KhachHangDTO(maKH, hoKH, tenKH, diaChi, email, soDienThoai, true);

        if ("insert".equals(action)) {
            // Thực hiện thêm mới vào Database qua lớp BUS
            bus.addKhachHang(kh); 
        } else if ("update".equals(action)) {
            // Thực hiện cập nhật thông tin dựa trên mã khách hàng
            bus.updateKhachHang(kh); 
        }

        // Sau khi xử lý xong, điều hướng trình duyệt quay lại trang danh sách
        response.sendRedirect("khachhang?action=list");
    }

    private void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=DanhSachKhachHang.xlsx");
        
        try {
            bus.exportToExcel(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}