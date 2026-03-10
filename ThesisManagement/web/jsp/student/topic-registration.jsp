<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký đề tài đồ án</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
    <div class="container py-5">
        <%-- Hiển thị lỗi từ Session (Ví dụ: Lỗi đăng ký trùng lặp) --%>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                <div class="d-flex align-items-center">
                    <i class="fas fa-exclamation-circle me-2 fs-4"></i>
                    <div>
                        <strong>Thông báo:</strong> ${sessionScope.error}
                    </div>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <%-- Xóa lỗi ngay sau khi hiển thị để không hiện lại khi F5 --%>
            <c:remove var="error" scope="session" />
        </c:if>

        <%-- Phần tiêu đề và bảng danh sách đề tài của bạn bên dưới... --%>
    </div>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow border-0">
                    <div class="card-header bg-primary text-white py-3">
                        <h4 class="mb-0 text-center"><i class="fas fa-edit me-2"></i>Đăng ký đề tài với giáo viên hướng dẫn</h4>
                    </div>
                    <div class="card-body p-4">

                        <c:if test="${not empty sessionScope.error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i> ${sessionScope.error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <% session.removeAttribute("error"); %>
                        </c:if>

                        <c:if test="${not empty sessionScope.success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i> ${sessionScope.success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <% session.removeAttribute("success"); %>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/student/topic/register" method="POST">

                            <input type="hidden" name="topicId" value="${selectedTopicId}" />

                            <div class="mb-3">
                                <label for="topicIdDisplay" class="form-label fw-bold">Mã đề tài (Code)</label>
                                <input type="text" class="form-control bg-light" id="topicIdDisplay" 
                                       name="topicCode" value="${param.topicCode}" readonly>
                                <div class="form-text">Mã đề tài đã được chọn từ danh sách.</div>
                            </div>

                            <div class="mb-3">
                                <label for="mscvHD" class="form-label fw-bold">
                                    <i class="fas fa-chalkboard-teacher me-1"></i>Giảng viên hướng dẫn
                                </label>
                                <select name="mscvHD" id="mscvHD" class="form-select" required>
                                    <option value="" selected disabled>-- Chọn giảng viên --</option>
                                    <c:forEach items="${lecturers}" var="l">
                                        <option value="${l.mscv}">
                                            ${l.fullName} (${l.mscv})
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="form-text italic">Giảng viên này sẽ chịu trách nhiệm duyệt đề tài.</div>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-paper-plane me-2"></i>Gửi yêu cầu đăng ký
                                </button>
                            </div>
                        </form>

                    </div>
                </div>

                <p class="text-center mt-4 text-muted small">
                    Hệ thống quản lý đồ án &copy; 2026 - Student Dashboard
                </p>
            </div>
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>