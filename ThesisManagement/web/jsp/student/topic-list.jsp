<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách đề tài đồ án</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container py-5">

        
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
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold"><i class="fas fa-list-ul me-2 text-primary"></i>Danh sách đề tài mở</h2>
            <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-1"></i> Quay lại Dashboard
            </a>
        </div>

        <c:set var="isAlreadyAccepted" value="false" />
        <c:set var="isAlreadyRejected" value="false" />
        <c:set var="hasPendingReq" value="false" />

        <c:forEach var="reg" items="${myRegistrations}">
            <c:if test="${reg.status == 'ACCEPTED' }">
                <c:set var="isAlreadyAccepted" value="true" />
            </c:if>
            <c:if test="${reg.status == 'PENDING'}">
                <c:set var="hasPendingReq" value="true" />
            </c:if>
            <c:if test="${reg.status == 'REJECTED'}">
                <c:set var="isAlreadyRejected" value="true" />
            </c:if>            
        </c:forEach>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <table class="table table-hover mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-4">Mã số</th>
                            <th>Tên đề tài</th>
                            <th>Giảng viên</th>
                            <th>Độ khó</th>
                            <th class="text-center">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="topic" items="${topics}">
                            <tr>
                                <td class="ps-4 fw-bold">${topic.topicCode}</td>
                                <td>
                                    <div class="fw-bold">${topic.title}</div>
                                    <small class="text-muted text-truncate d-inline-block" style="max-width: 400px;">
                                        ${topic.description}
                                    </small>
                                </td>
                                <td><span class="badge bg-light text-dark border">${topic.lecturer.fullName}</span></td>
                                <td>
                                    <c:set var="stars" value="${topic.difficultyScore.intValue()}" />
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="fas fa-star ${i <= stars ? 'text-warning' : 'text-muted'}"></i>
                                    </c:forEach>
                                </td>
                                <td class="text-center">
                                    <%-- TÌM ĐĂNG KÝ CỦA SV CHO ĐỀ TÀI HIỆN TẠI --%>
                                    <c:set var="currentReg" value="${null}" />
                                    <c:forEach var="reg" items="${myRegistrations}">
                                        <c:if test="${reg.topicId == topic.topicId}">
                                            <c:set var="currentReg" value="${reg}" />
                                        </c:if>
                                    </c:forEach>

                                    <c:choose>
                                        <%-- TRƯỜNG HỢP 1: Đây là đề tài sinh viên ĐÃ từng đăng ký --%>
                                        <c:when test="${not empty currentReg}">
                                            <c:choose>
                                                <%-- 1.1. Nếu đã được duyệt --%>
                                                <c:when test="${currentReg.status == 'ACCEPTED'}">
                                                    <span class="badge bg-success shadow-sm px-3 py-2">
                                                        <i class="fas fa-check-circle me-1"></i>Đề tài của bạn
                                                    </span>
                                                </c:when>

                                                <%-- 1.2. Nếu đang chờ duyệt --%>
                                                <c:when test="${currentReg.status == 'PENDING'}">
                                                    <span class="badge bg-warning text-dark border border-warning px-3 py-2">
                                                        <i class="fas fa-clock me-1"></i>Đang chờ duyệt
                                                    </span>
                                                </c:when>

                                                <%-- 1.3. QUAN TRỌNG: Nếu bị từ chối -> Khóa vĩnh viễn đề tài này --%>
                                                <c:when test="${currentReg.status == 'REJECTED'}">
                                                    <span class="badge bg-secondary shadow-sm px-3 py-2 text-white" title="Bạn đã bị từ chối đề tài này và không thể đăng ký lại">
                                                        <i class="fas fa-times-circle me-1"></i>Đã bị từ chối
                                                    </span>
                                                </c:when>
                                            </c:choose>
                                        </c:when>

                                        <%-- TRƯỜNG HỢP 2: Đây là các đề tài khác mà SV chưa từng đăng ký --%>
                                        <c:otherwise>
                                            <c:choose>
                                                <%-- Nếu đã có 1 đề tài khác ĐÃ ĐƯỢC DUYỆT -> Khóa tất cả --%>
                                                <c:when test="${isAlreadyAccepted == 'true'}">
                                                    <span class="text-muted small italic">
                                                        <i class="fas fa-lock me-1"></i>Đã có đề tài chính thức
                                                    </span>
                                                </c:when>

                                                <%-- Nếu đang có 1 đề tài khác ĐANG CHỜ DUYỆT -> Không cho đăng ký thêm --%>
                                                <c:when test="${hasPendingReq == 'true'}">
                                                    <span class="text-muted small">
                                                        <i class="fas fa-ban me-1"></i>Đang chờ duyệt đề tài khác
                                                    </span>
                                                </c:when>

                                                <%-- Nếu SV hoàn toàn trống (không pending, không accepted) -> Cho phép đăng ký đề tài này --%>
                                                <c:when test="${topic.status == 'AVAILABLE'}">
                                                    <button class="btn btn-sm btn-primary px-4 shadow-sm" 
                                                            onclick="registerTopic(${topic.topicId}, '${topic.topicCode}')">
                                                        Đăng ký
                                                    </button>
                                                </c:when>

                                                <c:otherwise>
                                                    <span class="badge bg-secondary">Hết chỗ</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr> 
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <%-- Form ẩn phục vụ chuyển hướng sang trang chi tiết đăng ký --%>
    <form id="registerForm" action="${pageContext.request.contextPath}/student/topic/register" method="GET" style="display: none;">
        <input type="hidden" name="topicId" id="formTopicId">
        <input type="hidden" name="topicCode" id="formTopicCode">
    </form>

    <script>
        function registerTopic(topicId, topicCode) {
            if(confirm('Bạn có chắc chắn muốn đăng ký đề tài mã số ' + topicCode + '?')) {
                document.getElementById('formTopicId').value = topicId;
                document.getElementById('formTopicCode').value = topicCode;
                document.getElementById('registerForm').submit(); 
            }
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>