<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch hẹn hướng dẫn | Quản lý Đồ án</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --primary-color: #0d6efd; --success-color: #198754; }
        .table-container { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
        .status-pill { font-size: 0.8rem; padding: 4px 12px; border-radius: 20px; font-weight: 500; }
        .bg-pending { background-color: #fff3cd; color: #856404; }
        .bg-confirmed { background-color: #d1e7dd; color: #0f5132; }
        .appointment-card { border-left: 4px solid var(--primary-color); }
    </style>
</head>
<body class="bg-light">

<div class="container py-5">
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/lecturer/dashboard" class="text-decoration-none text-muted">
            <i class="fas fa-arrow-left me-1"></i> Quay lại Dashboard
        </a>
    </div>

    <div class="row mb-4">
        <div class="col">
            <h2 class="fw-bold"><i class="fas fa-calendar-check me-2 text-primary"></i>Lịch hẹn hướng dẫn</h2>
            <p class="text-muted">Quản lý các buổi gặp mặt và thảo luận tiến độ với sinh viên.</p>
        </div>
    </div>

    <div class="table-container mb-5">
        <h5 class="fw-bold mb-4">
            <span class="badge bg-primary me-2">${pendingAppointments.size()}</span>Yêu cầu đang chờ xác nhận
        </h5>
        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Sinh viên</th>
                        <th>Thời gian đề xuất</th>
                        <th>Địa điểm / Hình thức</th>
                        <th>Nội dung thảo luận</th>
                        <th class="text-end">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="app" items="${pendingAppointments}">
                        <tr>
                            <td>
                                <div class="fw-bold">${app.fullName}</div>
                                <small class="text-muted">MSSV: ${app.mssv}</small>
                            </td>
                            <td>
                                <div class="text-primary fw-bold">
                                    <i class="far fa-clock me-1"></i>
                                    <fmt:formatDate value="${app.meetingDate}" pattern="HH:mm - dd/MM/yyyy"/>
                                </div>
                            </td>
                            <td>${app.location}</td>
                            <td><span class="text-truncate d-inline-block" style="max-width: 200px;">${app.purpose}</span></td>
                            <td class="text-end">
                                <form action="${pageContext.request.contextPath}/lecturer/appointment/confirm" method="POST" class="d-inline">
                                    <input type="hidden" name="appId" value="${app.appointmentId}">
                                    <button type="submit" class="btn btn-success btn-sm px-3">
                                        <i class="fas fa-check me-1"></i> Đồng ý
                                    </button>
                                </form>
                                <button class="btn btn-outline-danger btn-sm px-3 ms-1" data-bs-toggle="modal" data-bs-target="#rescheduleModal${app.appointmentId}">
                                    <i class="fas fa-calendar-alt me-1"></i> Đặt lịch lại
                                </button>
                            </td>
                        </tr>

                        <div class="modal fade" id="rescheduleModal${app.appointmentId}" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog">
                                <form action="${pageContext.request.contextPath}/lecturer/appointment/reschedule" method="POST" class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Phản hồi yêu cầu hẹn</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <input type="hidden" name="appId" value="${app.appointmentId}">
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Lý do/Ghi chú cho sinh viên</label>
                                            <textarea name="lecturerNote" class="form-control" rows="3" placeholder="Ví dụ: Thầy bận giờ này, dời sang 15:00 cùng ngày nhé..."></textarea>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                        <button type="submit" class="btn btn-danger">Gửi phản hồi</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty pendingAppointments}">
                        <tr>
                            <td colspan="5" class="text-center py-4 text-muted">Hiện không có yêu cầu hẹn mới.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="table-container shadow border-top border-success border-4">
        <div class="d-flex align-items-center mb-4">
            <div class="bg-success text-white rounded-circle p-2 me-3">
                <i class="fas fa-calendar-check fa-lg"></i>
            </div>
            <h5 class="fw-bold mb-0 text-dark" style="letter-spacing: -0.5px;">
                Lịch hẹn sắp tới
            </h5>
        </div>

        <div class="row g-4">
            <c:forEach var="app" items="${confirmedAppointments}">
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 appointment-card shadow-sm">
                        <div class="card-body p-4">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <span class="badge bg-success-subtle text-success border border-success-subtle px-3 py-2 rounded-pill">
                                    <i class="fas fa-check-circle me-1"></i> Đã xác nhận
                                </span>
                                <small class="location-text">
                                    <i class="fas fa-map-marker-alt text-danger me-1"></i> ${app.location}
                                </small>
                            </div>

                            <h6 class="student-name fw-bold text-uppercase">${app.fullName}</h6>
                            <div class="meeting-time mb-3">
                                <i class="far fa-clock me-1"></i>
                                <fmt:formatDate value="${app.meetingDate}" pattern="HH:mm, dd/MM/yyyy"/>
                            </div>

                            <div class="purpose-box">
                                <small class="d-block text-secondary mb-1 fw-bold">Nội dung thảo luận:</small>
                                <span class="small">"${app.purpose}"</span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty confirmedAppointments}">
                <div class="col-12">
                    <div class="text-center py-5 bg-light rounded-3 border border-dashed">
                        <i class="fas fa-calendar-day fa-3x text-muted mb-3"></i>
                        <p class="h6 text-muted">Chưa có lịch hẹn nào được xác nhận trong danh sách này.</p>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>