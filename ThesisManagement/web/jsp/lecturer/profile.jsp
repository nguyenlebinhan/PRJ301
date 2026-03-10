<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ Giảng viên | ${lecturer.fullName}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .profile-header {
            background: linear-gradient(to right, #004e92, #000428);
            color: white;
            padding: 60px 0;
            border-bottom-left-radius: 50px;
            border-bottom-right-radius: 50px;
        }
        .profile-img {
            width: 150px;
            height: 150px;
            object-fit: cover;
            border: 5px solid rgba(255,255,255,0.2);
        }
        .stats-card {
            border: none;
            border-radius: 15px;
            transition: transform 0.3s;
        }
        .stats-card:hover {
            transform: translateY(-5px);
        }
    </style>
</head>
<body class="bg-light">

<header class="profile-header text-center mb-5 shadow">
    <div class="container">
        <img src="${not empty lecturer.avatar ? lecturer.avatar : 'https://via.placeholder.com/150'}" 
             class="rounded-circle profile-img mb-3" alt="Avatar">
        <h1 class="fw-bold">${lecturer.fullName}</h1>
        <p class="lead"><i class="fas fa-briefcase me-2"></i>Giảng viên Khoa Công nghệ thông tin</p>
        <div class="d-flex justify-content-center gap-3">
            <span class="badge bg-light text-dark"><i class="fas fa-envelope me-1"></i> ${lecturer.email}</span>
            <span class="badge bg-light text-dark"><i class="fas fa-phone me-1"></i> ${lecturer.phone}</span>
        </div>
    </div>
</header>

<div class="container">
    <div class="row">
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm mb-4 p-3">
                <h5 class="fw-bold border-bottom pb-2"><i class="fas fa-info-circle me-2 text-primary"></i>Thông tin cá nhân</h5>
                <ul class="list-unstyled mt-3">
                    <li class="mb-3"><strong>Mã giảng viên:</strong> <span class="text-muted">${lecturer.lecturerCode}</span></li>
                    <li class="mb-3"><strong>Trình độ:</strong> <span class="text-muted">${lecturer.degree} (Thạc sĩ/Tiến sĩ)</span></li>
                    <li class="mb-3"><strong>Phòng làm việc:</strong> <span class="text-muted">Phòng 204, Tòa Alpha</span></li>
                    <li class="mb-3"><strong>Lĩnh vực nghiên cứu:</strong> 
                        <div class="mt-2">
                            <span class="badge btn-outline-primary border text-primary">Software Engineering</span>
                            <span class="badge btn-outline-primary border text-primary">Blockchain</span>
                        </div>
                    </li>
                </ul>
                <button class="btn btn-primary w-100 mt-2" data-bs-toggle="modal" data-bs-target="#editProfile">
                    <i class="fas fa-user-edit me-2"></i>Chỉnh sửa hồ sơ
                </button>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="row mb-4">
                <div class="col-md-4">
                    <div class="card stats-card bg-white text-center p-3 shadow-sm">
                        <h3 class="fw-bold text-primary">${thesisCount}</h3>
                        <p class="text-muted mb-0 small">Đồ án đang hướng dẫn</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stats-card bg-white text-center p-3 shadow-sm">
                        <h3 class="fw-bold text-success">${completedCount}</h3>
                        <p class="text-muted mb-0 small">Đồ án đã hoàn thành</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stats-card bg-white text-center p-3 shadow-sm">
                        <h3 class="fw-bold text-warning">4.8</h3>
                        <p class="text-muted mb-0 small">Đánh giá trung bình</p>
                    </div>
                </div>
            </div>

            <div class="card border-0 shadow-sm">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <h5 class="mb-0 fw-bold">Đồ án đang quản lý</h5>
                    <a href="#" class="btn btn-sm btn-link text-decoration-none">Xem tất cả</a>
                </div>
                <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-3">Mã đề tài</th>
                                <th>Tên đồ án</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${thesisList}">
                                <tr>
                                    <td class="ps-3 fw-bold text-primary">${t.topicCode}</td>
                                    <td>${t.title}</td>
                                    <td>
                                        <span class="badge rounded-pill ${t.status == 'COMPLETED' ? 'bg-success' : 'bg-info'}">
                                            ${t.status}
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/lecturer/thesis/history?id=${t.id}" 
                                           class="btn btn-sm btn-outline-secondary">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editProfile" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Cập nhật thông tin</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="update-profile" method="POST">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" name="phone" class="form-control" value="${lecturer.phone}">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Lĩnh vực nghiên cứu</label>
                        <textarea name="research" class="form-control" rows="3">${lecturer.researchArea}</textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>