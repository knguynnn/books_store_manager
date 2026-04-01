<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Khách Hàng - Bookstore Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --sidebar-width: 260px; --primary-color: #2c3e50; --toolbar-height: 140px; }
        body { background-color: #f0f2f5; font-family: 'Inter', sans-serif; overflow: hidden; /* Chống cuộn toàn trang */ }
        
        .sidebar { width: var(--sidebar-width); height: 100vh; position: fixed; background: var(--primary-color); color: white; z-index: 1000; }
        
        /* Layout chính */
        .wrapper { margin-left: var(--sidebar-width); height: 100vh; display: flex; flex-direction: column; }

        /* Thanh cố định phía trên (Header + Toolbar) */
        .fixed-top-section { background: #f0f2f5; padding: 20px 30px 10px 30px; z-index: 999; }
        
        .header-bar { display: flex; justify-content: space-between; align-items: center; background: white; padding: 15px 25px; border-radius: 15px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 15px; }

        /* Khu vực cuộn của danh sách */
        .scrollable-content { flex: 1; overflow-y: auto; padding: 0 30px 30px 30px; }

        /* Style Table (Floating Rows) */
        .custom-table { border-collapse: separate; border-spacing: 0 12px; width: 100%; }
        .custom-table thead { position: sticky; top: 0; background: #f0f2f5; z-index: 10; }
        .custom-table thead th { border: none; color: #6c757d; font-size: 0.85rem; text-transform: uppercase; padding: 10px 20px; }
        .custom-table tbody tr { background: white; transition: all 0.3s; box-shadow: 0 2px 5px rgba(0,0,0,0.02); }
        .custom-table tbody tr:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0,0,0,0.08); }
        .custom-table tbody td { padding: 15px 20px; border: none; vertical-align: middle; }
        .custom-table tbody tr td:first-child { border-radius: 12px 0 0 12px; border-left: 4px solid #3498db; }
        .custom-table tbody tr td:last-child { border-radius: 0 12px 12px 0; }

        .id-badge { background: #e8f0fe; color: #1a73e8; padding: 4px 10px; border-radius: 6px; font-weight: bold; }
    </style>
</head>
<body>

    <div class="sidebar">
        <div class="p-4 text-center"><h4>BOOKSTORE</h4></div>
        <nav class="nav flex-column px-3">
            <a class="nav-link text-white py-3 active" href="#"><i class="fas fa-users me-2"></i> Khách hàng</a>
            </nav>
    </div>

    <div class="wrapper">
        <div class="fixed-top-section">
            <div class="header-bar">
                <h5 class="mb-0 fw-bold">QUẢN LÝ KHÁCH HÀNG</h5>
                <div class="d-flex align-items-center gap-3">
                    <form action="khachhang" method="get" class="d-flex gap-2">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="keyword" value="${keyword}" class="form-control form-control-sm" style="width: 250px;" placeholder="Tìm nhanh...">
                        <button class="btn btn-sm btn-primary"><i class="fas fa-search"></i></button>
                    </form>
                    <div class="vr mx-2"></div>
                    <button class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#addModal"><i class="fas fa-plus"></i> Thêm mới</button>
                    <div class="dropdown">
                        <button class="btn btn-sm btn-outline-secondary dropdown-toggle" data-bs-toggle="dropdown">Tiện ích</button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="khachhang?action=export-excel"><i class="fas fa-file-export me-2 text-warning"></i>Xuất Excel</a></li>
                            <li><a class="dropdown-item" href="#" data-bs-toggle="modal" data-bs-target="#importModal"><i class="fas fa-file-import me-2 text-info"></i>Nhập Excel</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="scrollable-content">
            <table class="custom-table">
                <thead>
                    <tr>
                        <th>Mã KH</th>
                        <th>Khách hàng</th>
                        <th>Liên hệ</th>
                        <th>Địa chỉ</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="kh" items="${dsKhachHang}">
                        <tr>
                            <td><span class="id-badge">${kh.maKH}</span></td>
                            <td>
                                <div class="fw-bold">${kh.hoKH} ${kh.tenKH}</div>
                                <small class="text-muted">${kh.email}</small>
                            </td>
                            <td>${kh.soDienThoai}</td>
                            <td>${kh.diaChi}</td>
                            <td class="text-center">
                                <button class="btn btn-sm text-warning btn-edit" data-bs-toggle="modal" data-bs-target="#editModal"
                                    data-makh="${kh.maKH}" data-ho="${kh.hoKH}" data-ten="${kh.tenKH}"
                                    data-sdt="${kh.soDienThoai}" data-email="${kh.email}" data-diachi="${kh.diaChi}">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <a href="khachhang?action=delete&maKH=${kh.maKH}" class="btn btn-sm text-danger" onclick="return confirm('Xóa khách hàng này?')"><i class="fas fa-trash"></i></a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal fade" id="importModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"><h5>Nhập dữ liệu từ Excel</h5><button class="btn-close" data-bs-dismiss="modal"></button></div>
                <form action="khachhang?action=import-excel" method="post" enctype="multipart/form-data">
                    <div class="modal-body">
                        <p class="text-muted small">Vui lòng chọn file .xlsx theo đúng định dạng cột (Mã, Họ, Tên, Địa chỉ, Email, SĐT).</p>
                        <input type="file" name="fileExcel" class="form-control" accept=".xlsx" required>
                    </div>
                    <div class="modal-footer"><button type="submit" class="btn btn-primary">Bắt đầu nhập</button></div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // JS cho nút Sửa (Giữ nguyên logic cũ)
        const editModal = document.getElementById('editModal');
        if(editModal) {
            editModal.addEventListener('show.bs.modal', function (event) {
                const btn = event.relatedTarget;
                document.getElementById('edit_maKH').value = btn.getAttribute('data-makh');
                document.getElementById('edit_hoKH').value = btn.getAttribute('data-ho');
                document.getElementById('edit_tenKH').value = btn.getAttribute('data-ten');
                document.getElementById('edit_sdt').value = btn.getAttribute('data-sdt');
                document.getElementById('edit_email').value = btn.getAttribute('data-email');
                document.getElementById('edit_diachi').value = btn.getAttribute('data-diachi');
            });
        }
    </script>
</body>
</html>