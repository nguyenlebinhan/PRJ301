<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Phê duyệt Đăng ký</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container py-5">
        <h2 class="mb-4 fw-bold"><i class="fas fa-user-check me-2"></i>Yêu cầu Đăng ký Đồ án</h2>
        
        <div class="card border-0 shadow-sm">
            <div class="card-body">
                <table class="table align-middle">
                    <thead>
                        <tr>
                            <th>Sinh viên</th>
                            <th>Đề tài đăng ký</th>
                            <th>Ngày gửi</th>
                            <th>Trạng thái</th>
                            <th class="text-end">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="reg" items="${registrations}">
                            <tr>
                                <td>
                                    <div class="fw-bold">${reg.student.fullName}</div>
                                    <small class="text-muted">${reg.mssv}</small>
                                </td>
                                <td>${reg.topic.title}</td>
                                <td><small>${reg.registeredAt}</small></td>
                                <td>
                                    <span class="badge ${reg.status == 'PENDING' ? 'bg-warning' : 'bg-success'} text-dark">
                                        ${reg.status}
                                    </span>
                                </td>
                                <td class="text-end">
                                    <c:if test="${reg.status == 'PENDING'}">
                                        <form action="${pageContext.request.contextPath}/lecturer/registrations/approve" method="POST" class="d-inline">
                                            <input type="hidden" name="registrationId" value="${reg.registrationId}">
                                            <button type="submit" class="btn btn-success btn-sm px-3">
                                                <i class="fas fa-check me-1"></i> Chấp nhận
                                            </button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>