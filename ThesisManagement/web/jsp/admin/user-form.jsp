<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa người dùng | Thesis Management</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
    <style>
        :root {
            --sidebar-bg: #0f172a;
            --main-bg: #f8fafc;
            --accent: #6366f1;
            --text-main: #1e293b;
        }

        body { 
            background-color: var(--main-bg); 
            font-family: 'Inter', sans-serif; 
            color: var(--text-main);
        }

        .sidebar { 
            width: 260px; height: 100vh; position: fixed; 
            background: var(--sidebar-bg); color: white; 
            padding: 1.5rem 1rem;
        }

        .main-content { margin-left: 260px; padding: 3rem; }

        .card-edit {
            background: #fff;
            border: none;
            border-radius: 20px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.02);
            max-width: 600px;
            margin: 0 auto;
        }

        .form-label {
            font-weight: 600;
            font-size: 0.85rem;
            color: #64748b;
            margin-bottom: 0.5rem;
        }

        .form-control, .form-select {
            padding: 0.75rem 1rem;
            border-radius: 12px;
            border: 1px solid #e2e8f0;
            background-color: #fcfcfd;
        }

        .form-control:focus {
            border-color: var(--accent);
            box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.1);
        }

        .form-control:disabled {
            background-color: #f1f5f9;
            cursor: not-allowed;
        }

        .btn-update {
            background: var(--accent);
            color: white;
            border: none;
            padding: 0.8rem;
            border-radius: 12px;
            font-weight: 600;
            transition: 0.3s;
        }

        .btn-update:hover {
            background: #4f46e5;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(99, 102, 241, 0.3);
        }

        .header-icon {
            width: 50px;
            height: 50px;
            background: rgba(99, 102, 241, 0.1);
            color: var(--accent);
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="mb-4 px-3 fw-bold text-uppercase letter-spacing-1">Thesis Management</div>
        <nav class="nav flex-column">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link text-white-50 p-3"><i class="bi bi-grid-1x2 me-2"></i> Dashboard</a>
            <a href="list" class="nav-link text-white p-3 active bg-white bg-opacity-10 rounded-3"><i class="bi bi-people me-2"></i> Quản lý User</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="card-edit p-4 p-md-5">
            <div class="header-icon">
                <i class="bi bi-person-gear"></i>
            </div>
            
            <h4 class="fw-bold mb-1">Chỉnh sửa thông tin</h4>
            <p class="text-muted small mb-4">Cập nhật vai trò hoặc họ tên của người dùng hệ thống.</p>

            <form action="update" method="post">
                <input type="hidden" name="id" value="${user.id}">

                <div class="mb-4">
                    <label class="form-label text-uppercase">Tên đăng nhập</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0" style="border-radius: 12px 0 0 12px;"><i class="bi bi-lock-fill text-muted"></i></span>
                        <input type="text" class="form-control border-start-0" value="${user.username}" disabled style="border-radius: 0 12px 12px 0;">
                    </div>
                    <div class="form-text small mt-2">Định danh duy nhất, không thể thay đổi.</div>
                </div>

                <div class="mb-4">
                    <label class="form-label text-uppercase">Họ và Tên</label>
                    <input type="text" name="fullName" class="form-control" value="${user.fullName}" required placeholder="Nhập họ và tên đầy đủ">
                </div>

                <div class="mb-4">
                    <label class="form-label text-uppercase">Vai trò hệ thống</label>
                    <select name="roleId" class="form-select">
                        <option value="LECTURER" ${user.role == 'LECTURER' ? 'selected' : ''}>Giảng viên (LECTURER)</option>
                        <option value="STUDENT" ${user.role == 'STUDENT' ? 'selected' : ''}>Sinh viên (STUDENT)</option>
                        <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Quản trị viên (ADMIN)</option>
                    </select>
                </div>

                <div class="row g-3 mt-2">
                    <div class="col-sm-8">
                        <button type="submit" class="btn btn-update w-100">
                            <i class="bi bi-check2-circle me-2"></i> Lưu thay đổi
                        </button>
                    </div>
                    <div class="col-sm-4">
                        <a href="list" class="btn btn-light w-100" style="padding: 0.8rem; border-radius: 12px; font-weight: 600; color: #64748b;">
                            Hủy
                        </a>
                    </div>
                </div>
            </form>
        </div>
    </main>

</body>
</html>