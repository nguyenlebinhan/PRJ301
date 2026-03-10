<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý người dùng | Thesis Management</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
    <style>
        :root {
            --sidebar-bg: #0f172a;
            --main-bg: #f8fafc;
            --accent: #6366f1;
            --text-main: #1e293b;
            --text-muted: #64748b;
        }

        body { 
            background-color: var(--main-bg); 
            font-family: 'Inter', sans-serif; 
            color: var(--text-main);
        }

        /* Sidebar kế thừa từ dashboard */
        .sidebar { 
            width: 260px; height: 100vh; position: fixed; 
            background: var(--sidebar-bg); color: white; 
            padding: 1.5rem 1rem; z-index: 1000;
        }
        .sidebar-brand { font-size: 1.25rem; font-weight: 700; padding: 0 1rem 2rem; display: flex; align-items: center; }
        .nav-link { color: #94a3b8; padding: 0.8rem 1rem; border-radius: 10px; transition: 0.2s; display: flex; align-items: center; font-weight: 500; text-decoration: none; }
        .nav-link i { margin-right: 12px; font-size: 1.2rem; }
        .nav-link:hover { background: rgba(255,255,255,0.05); color: #fff; }
        .nav-link.active { background: var(--accent); color: white; }

        /* Main Content */
        .main-content { margin-left: 260px; padding: 2rem; }
        
        /* Table Card */
        .card-custom {
            background: #fff; border-radius: 16px; border: none;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
            overflow: hidden;
        }
        
        .table thead th {
            background: #f8fafc; color: var(--text-muted);
            font-weight: 600; font-size: 0.75rem;
            text-transform: uppercase; letter-spacing: 0.05em;
            padding: 1rem; border: none;
        }

        .table tbody td { padding: 1.2rem 1rem; vertical-align: middle; border-bottom: 1px solid #f1f5f9; }

        /* User UI Components */
        .user-avatar {
            width: 40px; height: 40px; background: #e2e8f0;
            border-radius: 12px; display: flex; align-items: center;
            justify-content: center; font-weight: 600; color: var(--accent);
            margin-right: 15px;
        }

        .badge-role {
            padding: 0.4rem 0.8rem; border-radius: 8px; font-size: 0.75rem; font-weight: 600;
        }

        .status-pill {
            display: inline-flex; align-items: center; gap: 6px;
            font-size: 0.85rem; font-weight: 500;
        }

        .btn-action {
            width: 32px; height: 32px; padding: 0; display: inline-flex;
            align-items: center; justify-content: center; border-radius: 8px;
        }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand"><i class="bi bi-mortarboard-fill me-2 text-primary"></i> Thesis Management</div>
        <nav class="nav flex-column">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i class="bi bi-grid-1x2"></i> Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/list" class="nav-link"><i class="bi bi-people"></i> Quản lý User</a>
            <a href="${pageContext.request.contextPath}/admin/topic" class="nav-link"><i class="bi bi-journal-text"></i> Đề tài</a>
            <a href="${pageContext.request.contextPath}/admin/thesis" class="nav-link"><i class="bi bi-layers"></i> Đồ án</a>
            <hr class="text-secondary mx-2">
            <a href="${pageContext.request.contextPath}/admin/logout" class="nav-link text-danger"><i class="bi bi-box-arrow-right"></i> Đăng xuất</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-end mb-4">
            <div>
                <h3 class="fw-bold mb-1">Danh sách người dùng</h3>
                <p class="text-muted mb-0">Quản lý tài khoản sinh viên, giảng viên và quản trị viên.</p>
            </div>
            <button class="btn btn-primary shadow-sm" style="border-radius: 10px;" data-bs-toggle="modal" data-bs-target="#addUserModal">
                <i class="bi bi-person-plus-fill me-2"></i> Thêm mới
            </button>
        </div>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success border-0 shadow-sm rounded-4 d-flex align-items-center" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <div>
                    <c:choose>
                        <c:when test="${param.msg == 'update_success'}">Cập nhật thông tin thành công!</c:when>
                        <c:when test="${param.msg == 'delete_success'}">Đã xóa người dùng khỏi hệ thống.</c:when>
                    </c:choose>
                </div>
            </div>
        </c:if>

        <div class="card-custom">
            <div class="table-responsive">
                <table class="table mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Thông tin người dùng</th>
                            <th>Vai trò</th>
                            <th>Trạng thái</th>
                            <th class="text-end">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${listUser}">
                            <tr>
                                <td class="text-muted fw-medium">#${u.id}</td>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <div class="user-avatar text-uppercase">${u.username.substring(0,1)}</div>
                                        <div>
                                            <div class="fw-bold text-dark">${u.fullName}</div>
                                            <div class="text-muted small">@${u.username} • ${u.email}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge-role ${u.role == 'ADMIN' ? 'bg-primary-subtle text-primary' : 'bg-light text-dark border'}">
                                        ${u.role}
                                    </span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.isActive}">
                                            <span class="status-pill text-success"><i class="bi bi-circle-fill fs-6" style="font-size: 8px !important;"></i> Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-pill text-danger"><i class="bi bi-circle-fill fs-6" style="font-size: 8px !important;"></i> Khóa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <button type="button" 
                                            class="btn btn-action btn-light text-primary me-1" 
                                            onclick="openEditModal('${u.id}', '${u.username}', '${u.fullName}', '${u.role}', '${u.mssv}', '${u.mscv}')"
                                            title="Sửa thông tin">
                                        <i class="bi bi-pencil-fill"></i>
                                    </button>
                                    <a href="delete?id=${u.id}" class="btn btn-action btn-light text-danger" 
                                       onclick="return confirm('Xác nhận xóa tài khoản: ${u.fullName}?\nThao tác này không thể hoàn tác!')" title="Xóa người dùng">
                                        <i class="bi bi-trash3-fill"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="p-3 bg-light border-top d-flex justify-content-between align-items-center">
                <span class="small text-muted">Hiển thị ${listUser.size()} kết quả</span>
            </div>
        </div>
    </main>
    <div class="modal fade" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg"> <div class="modal-content border-0 shadow-lg" style="border-radius: 20px;">
                <div class="modal-header border-0 pt-4 px-4">
                    <h5 class="modal-title fw-bold" id="addUserModalLabel">
                        <i class="bi bi-person-plus text-primary me-2"></i>Tạo tài khoản mới
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form action="${pageContext.request.contextPath}/admin/addUser" method="POST">
                    <div class="modal-body p-4">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-muted text-uppercase">Họ và tên</label>
                                <input type="text" name="fullName" class="form-control bg-light border-0" placeholder="Nguyễn Văn A" required style="padding: 0.7rem 1rem; border-radius: 10px;">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-muted text-uppercase">Tên đăng nhập</label>
                                <input type="text" name="username" class="form-control bg-light border-0" placeholder="username123" required style="padding: 0.7rem 1rem; border-radius: 10px;">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-muted text-uppercase">Email</label>
                                <input type="email" name="email" class="form-control bg-light border-0" placeholder="example@gmail.com" required style="padding: 0.7rem 1rem; border-radius: 10px;">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-muted text-uppercase">Mật khẩu</label>
                                <input type="password" name="password" class="form-control bg-light border-0" placeholder="••••••••" required style="padding: 0.7rem 1rem; border-radius: 10px;">
                            </div>

                            <div class="col-12">
                                <label class="form-label small fw-bold text-muted text-uppercase">Vai trò hệ thống</label>
                                <select id="roleSelect" name="role" class="form-select bg-light border-0" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                    <option value="STUDENT" selected>Sinh viên</option>
                                    <option value="TEACHER">Giảng viên</option>
                                    <option value="ADMIN">Quản trị viên</option>
                                </select>
                            </div>

                            <div id="studentFields" class="row g-3 m-0 p-0">
                                <div class="col-md-4 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">MSSV</label>
                                    <input type="text" name="mssv" class="form-control bg-light border-0" placeholder="20216001" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                                <div class="col-md-4 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">Lớp</label>
                                    <input type="text" name="class" class="form-control bg-light border-0" placeholder="KTPM01" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                                <div class="col-md-4 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">Ngành</label>
                                    <input type="text" name="major" class="form-control bg-light border-0" placeholder="Công nghệ phần mềm" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                            </div>

                            <div id="teacherFields" class="row g-3 m-0 p-0" style="display: none;">
                                <div class="col-md-4 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">MSCV</label>
                                    <input type="text" name="mscb" class="form-control bg-light border-0" placeholder="GV123" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                                <div class="col-md-4 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">Học hàm/Học vị</label>
                                    <input type="text" name="mscb" class="form-control bg-light border-0" placeholder="Giáo sư ..." style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                                <div class="col-md-4 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">Lĩnh vực nghiên cứu</label>
                                    <input type="text" name="mscb" class="form-control bg-light border-0" placeholder="Trí tuệ nhân tạo ..." style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer border-0 p-4 pt-0">
                        <button type="button" class="btn btn-light fw-semibold px-4" data-bs-dismiss="modal" style="border-radius: 10px; color: var(--text-muted);">Hủy</button>
                        <button type="submit" class="btn btn-primary fw-semibold px-4" style="border-radius: 10px; background: var(--accent);">Xác nhận thêm</button>
                    </div>
                </form>
            </div>
        </div>
    </div>    
    <div class="modal fade" id="editUserModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content border-0 shadow-lg" style="border-radius: 20px;">
                <div class="modal-header border-0 pt-4 px-4">
                    <h5 class="modal-title fw-bold">
                        <i class="bi bi-pencil-square text-primary me-2"></i>Chỉnh sửa thông tin
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form action="${pageContext.request.contextPath}/admin/update" method="POST">
                    <input type="hidden" name="id" id="edit_id">

                    <div class="modal-body p-4">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-muted text-uppercase">Tên đăng nhập</label>
                                <input type="text" id="edit_username" class="form-control bg-light border-0" disabled style="border-radius: 10px;">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-muted text-uppercase">Họ và tên</label>
                                <input type="text" name="fullName" id="edit_fullName" class="form-control bg-light border-0" required style="padding: 0.7rem 1rem; border-radius: 10px;">
                            </div>
                            <div class="col-md-12">
                                <label class="form-label small fw-bold text-muted text-uppercase">Vai trò hệ thống</label>
                                <select id="edit_roleSelect" name="role" class="form-select bg-light border-0" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                    <option value="STUDENT">Sinh viên</option>
                                    <option value="TEACHER">Giảng viên</option>
                                    <option value="ADMIN">Quản trị viên</option>
                                </select>
                            </div>

                            <div id="edit_studentFields" class="row g-3 m-0 p-0">
                                <div class="col-md-6 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">MSSV</label>
                                    <input type="text" name="mssv" id="edit_mssv" class="form-control bg-light border-0" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>                                
                            </div>

                            <div id="edit_teacherFields" class="row g-3 m-0 p-0" style="display: none;">
                                <div class="col-md-6 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">MSCV</label>
                                    <input type="text" name="mscb" id="edit_mscb" class="form-control bg-light border-0" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                                <div class="col-md-6 mt-3">
                                    <label class="form-label small fw-bold text-muted text-uppercase">Học vị</label>
                                    <input type="text" name="degree" id="edit_degree" class="form-control bg-light border-0" style="padding: 0.7rem 1rem; border-radius: 10px;">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer border-0 p-4 pt-0">
                        <button type="button" class="btn btn-light fw-semibold px-4" data-bs-dismiss="modal" style="border-radius: 10px;">Hủy</button>
                        <button type="submit" class="btn btn-primary fw-semibold px-4" style="border-radius: 10px; background: var(--accent);">Cập nhật</button>
                    </div>
                </form>
            </div>
        </div>
    </div>                    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const roleSelect = document.getElementById('roleSelect');
            const studentFields = document.getElementById('studentFields');
            const teacherFields = document.getElementById('teacherFields');

            roleSelect.addEventListener('change', function() {
                const role = this.value;

                if (role === 'STUDENT') {
                    studentFields.style.display = 'flex';
                    teacherFields.style.display = 'none';
                    // Toggle thuộc tính required nếu cần
                } else if (role === 'TEACHER') {
                    studentFields.style.display = 'none';
                    teacherFields.style.display = 'flex';
                } else {
                    // Nếu là ADMIN thì ẩn cả hai
                    studentFields.style.display = 'none';
                    teacherFields.style.display = 'none';
                }
            });
        });
    </script>  
    <script>
        // Hàm mở modal và đổ dữ liệu
        function openEditModal(id, username, fullName, role, mssv,, mscb) {
            // 1. Gán giá trị vào các ô input
            document.getElementById('edit_id').value = id;
            document.getElementById('edit_username').value = username;
            document.getElementById('edit_fullName').value = fullName;
            document.getElementById('edit_roleSelect').value = role;

            // Gán dữ liệu riêng (nếu có)
            if(document.getElementById('edit_mssv')) document.getElementById('edit_mssv').value = mssv || '';
            if(document.getElementById('edit_mscb')) document.getElementById('edit_mscb').value = mscb || '';

            // 2. Chạy logic ẩn hiện dựa trên role của user được sửa
            toggleEditFields(role);

            // 3. Hiển thị modal
            var myModal = new bootstrap.Modal(document.getElementById('editUserModal'));
            myModal.show();
        }

        // Hàm xử lý ẩn hiện cho Modal Sửa
        function toggleEditFields(role) {
            const studentFields = document.getElementById('edit_studentFields');
            const teacherFields = document.getElementById('edit_teacherFields');

            if (role === 'STUDENT') {
                studentFields.style.display = 'flex';
                teacherFields.style.display = 'none';
            } else if (role === 'TEACHER') {
                studentFields.style.display = 'none';
                teacherFields.style.display = 'flex';
            } else {
                studentFields.style.display = 'none';
                teacherFields.style.display = 'none';
            }
        }

        // Lắng nghe sự kiện thay đổi role ngay trong Modal Sửa
        document.getElementById('edit_roleSelect').addEventListener('change', function() {
            toggleEditFields(this.value);
        });
    </script>    

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>