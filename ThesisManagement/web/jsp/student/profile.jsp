<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân | ${student.fullName}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .profile-header { background: linear-gradient(135deg, #6f42c1 0%, #4e73df 100%); color: white; border-radius: 15px; }
        .avatar-wrapper { width: 120px; height: 120px; border: 4px solid white; box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        .card { border: none; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
    </style>
</head>
<body class="bg-light">

<div class="container py-5">
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/student/dashboard" class="text-decoration-none text-muted">
            <i class="fas fa-arrow-left me-2"></i>Quay lại Dashboard
        </a>
    </div>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4">
            <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="success" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm border-0 mb-4" role="alert">
            <div class="d-flex align-items-center">
                <i class="fas fa-exclamation-triangle me-2 fs-4"></i>
                <div>
                    <strong>Lỗi:</strong> ${sessionScope.error}
                </div>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="error" scope="session" />
    </c:if>              

    <div class="row g-4">
        <div class="col-lg-4">
            <div class="card text-center p-4 h-100">
                <div class="d-flex justify-content-center mb-3">
                    <div class="avatar-wrapper rounded-circle bg-light d-flex align-items-center justify-content-center">
                        <i class="fas fa-user fa-4x text-secondary"></i>
                    </div>
                </div>
                <h4 class="fw-bold mb-1">${student.fullName}</h4>
                <div class="d-grid gap-2">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                        <i class="fas fa-edit me-2"></i>Chỉnh sửa hồ sơ
                    </button>
                    <button class="btn btn-outline-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteConfirmModal">
                        Vô hiệu hóa tài khoản
                    </button>
                </div>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="card p-4 h-100">
                <h5 class="fw-bold text-primary mb-4 border-bottom pb-2">Thông tin chi tiết</h5>
                <div class="row gy-4">
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Mã số sinh viên (MSSV)</label>
                        <span class="fw-bold">${student.mssv}</span>
                    </div>
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Lớp chuyên ngành</label>
                        <span class="fw-bold">${student.className}</span>
                    </div>
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Chuyên ngành</label>
                        <span class="fw-bold">${student.major}</span>
                    </div>                    
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Địa chỉ Email</label>
                        <span class="fw-bold">${student.email}</span>
                    </div>
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Số điện thoại</label>
                        <span class="fw-bold">${not empty student.phone ? student.phone : 'Chưa cập nhật'}</span>
                    </div>
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Chuyên ngành</label>
                        <span class="fw-bold">Kỹ thuật phần mềm (SE)</span>
                    </div>
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Chuyên ngành</label>
                        <span class="fw-bold">Kỹ thuật phần mềm (SE)</span>
                    </div> 
                    <div class="col-md-6">
                        <label class="text-muted small d-block">Các kĩ năng</label>
                        <span class="fw-bold">${student.skills}</span>
                    </div>                     
                    <div class="col-md-6">
                        <label class="text-muted small d-block">GPA Tích lũy</label>
                        <span class="badge bg-success">${student.gpa} / 4.0</span>
                    </div>                   
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="editProfileModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content border-0 shadow">
            <form action="${pageContext.request.contextPath}/student/profile/update" method="POST">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold">Cập nhật thông tin</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Họ và tên</label>
                        <input type="text" name="fullName" class="form-control" value="${student.fullName}" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Số điện thoại</label>
                        <input type="text" name="phone" class="form-control" value="${student.phone}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Email cá nhân</label>
                        <input type="email" name="email" class="form-control" value="${student.email}" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên lớp</label>
                        <input type="text" name="className" class="form-control" value="${student.className}" required>
                    </div>   
                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên ngành</label>
                        <input type="text" name="major" class="form-control" value="${student.major}" required>
                    </div>   
                    <div class="mb-3">
                        <label class="form-label fw-bold">Các kĩ năng</label>
                        <input type="text" name="skills" class="form-control" value="${student.skills}" required>
                    </div>                       
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-body text-center p-4">
                <i class="fas fa-exclamation-triangle fa-3x text-danger mb-3"></i>
                <h5 class="fw-bold">Xác nhận?</h5>
                <p class="text-muted small">Tài khoản của bạn sẽ không thể đăng nhập cho đến khi được Admin mở lại.</p>
                <form action="${pageContext.request.contextPath}/student/profile/deactivate" method="POST">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">Vô hiệu hóa</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>