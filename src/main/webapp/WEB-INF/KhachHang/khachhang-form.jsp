<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${khachHang != null ? 'Sửa Khách Hàng' : 'Thêm Khách Hàng'}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .form-container { width: 400px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 5px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="email"] { width: 100%; padding: 8px; box-sizing: border-box; }
        .btn { padding: 10px 15px; border: none; border-radius: 3px; cursor: pointer; color: white; }
        .btn-submit { background-color: #4CAF50; }
        .btn-back { background-color: #aaa; text-decoration: none; display: inline-block; text-align: center; }
    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <div class="form-container">
        <h2>${khachHang != null ? 'SỬA THÔNG TIN' : 'THÊM KHÁCH HÀNG MỚI'}</h2>
        
        <form action="khachhang" method="post">
            <input type="hidden" name="action" value="${khachHang != null ? 'update' : 'insert'}">

            <div class="form-group">
                <label>Mã Khách Hàng:</label>
                <input type="text" name="maKH" value="${khachHang != null ? khachHang.maKH : autoMaKH}" ${khachHang != null ? 'readonly style="background-color:#eee;"' : 'readonly'}>
            </div>

            <div class="form-group">
                <label>Họ Khách Hàng:</label>
                <input type="text" name="hoKH" value="${khachHang != null ? khachHang.hoKH : ''}" required>
            </div>

            <div class="form-group">
                <label>Tên Khách Hàng:</label>
                <input type="text" name="tenKH" value="${khachHang != null ? khachHang.tenKH : ''}" required>
            </div>

            <div class="form-group">
                <label>Số Điện Thoại:</label>
                <input type="text" name="soDienThoai" value="${khachHang != null ? khachHang.soDienThoai : ''}" required>
            </div>

            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" value="${khachHang != null ? khachHang.email : ''}">
            </div>

            <div class="form-group">
                <label>Địa Chỉ:</label>
                <input type="text" name="diaChi" value="${khachHang != null ? khachHang.diaChi : ''}">
            </div>

            <button type="submit" class="btn btn-submit">Lưu Dữ Liệu</button>
            <a href="khachhang?action=list" class="btn btn-back">Quay lại</a>
        </form>
    </div>

</body>
</html>