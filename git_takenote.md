# 📌 GIT TAKE NOTE – NHÓM BOOK STORE MANAGER

Tài liệu này dùng để **thống nhất cách làm việc với Git** cho toàn bộ thành viên trong nhóm.

---

## 🌱 1. Cấu trúc nhánh (Git Flow)

| Nhánh | Mục đích |
|------|---------|
| `main` | Phiên bản ổn định, dùng để nộp bài |
| `develop` | Nhánh tích hợp toàn bộ chức năng |
| `feature/*` | Nhánh phát triển chức năng riêng |

Các nhánh chức năng hiện có:
- `feature/ban-hang`
- `feature/nhap-hang`
- `feature/san-pham`
- `feature/khach-hang`
- `feature/nhan-vien`
- `feature/thong-ke`

---

## 👤 2. Quy tắc cho thành viên

- ❌ **Không commit trực tiếp vào `main`**
- ❌ **Không merge thẳng `feature/*` vào `main`**
- ✅ Mỗi người **chỉ làm trong nhánh được phân công**
- ✅ Code xong phải **tạo Pull Request vào `develop`**

---

## 🚀 3. Quy trình làm việc chuẩn

### Bước 1: Clone project
```bash
git clone https://github.com/<TEN_GITHUB>/books_store_manager.git
cd books_store_manager
```

### Bước 2: Chuyển sang nhánh của mình
```bash
git checkout feature/ten-nhanh
```

### Bước 3: Trước khi code (rất quan trọng)
```bash
git pull origin develop
```

### Bước 4: Code đúng phần được phân công
Cấu trúc chuẩn:
```text
src/
 ├─ dto/
 ├─ dao/
 ├─ bus/
 └─ gui/
```

---

## 💾 4. Commit & Push

```bash
git add .
git commit -m "Mô tả ngắn gọn chức năng đã làm"
git push
```

⚠️ Commit message phải **rõ ràng, không ghi chung chung**.

---

## 🔀 5. Pull Request (Bắt buộc)

1. Lên GitHub repository
2. Chọn **Compare & Pull Request**
3. Chọn:
   - **Base**: `develop`
   - **Compare**: `feature/ten-nhanh`
4. Create Pull Request

❌ Không tự merge nếu chưa được trưởng nhóm duyệt.

---

## 👑 6. Nhiệm vụ trưởng nhóm

- Review code các Pull Request
- Merge `feature/*` → `develop`
- Khi hoàn tất project:
```bash
git checkout main
git merge develop
git push
```

---

## 🛑 7. Các lỗi thường gặp & cách xử lý

### 🔴 Lỗi conflict
- Không tự ý xoá code người khác
- Báo nhóm/trưởng nhóm để cùng xử lý

### 🔴 Push bị từ chối
```bash
git pull origin develop
```
Sau đó fix conflict rồi commit lại.

---

## 🎯 8. Ghi nhớ

> "Mỗi người một nhánh – Mọi thứ qua develop – main chỉ để nộp bài"

📌 **Tuân thủ tài liệu này để tránh lỗi và mất thời gian khi làm nhóm.**

