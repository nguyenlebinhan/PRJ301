<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Lịch sử chỉnh sửa | ${thesisInfo.topicCode}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold text-primary">Nhật ký thay đổi đồ án</h2>
            <c:choose>
                <c:when test = "${not empty thesisInfo.title}">
                 <p class="text-muted">Đề tài: <strong>${thesisInfo.title}</strong> (${thesisInfo.topicCode})</p>
                </c:when> 
                <c:otherwise>
                    <span class="text-muted small">Đề tài: <strong>Chưa có </strong> </span>
                </c:otherwise>    
            </c:choose>
            <p class="text-muted">Sinh viên: <strong>${student.fullName}</strong> (${student.mssv})</p>
        </div>
        <a href="${pageContext.request.contextPath}/lecturer/GuidedStudentList" class="btn btn-secondary">
            <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách sinh viên
        </a>
    </div>

    <div class="card border-0 shadow-sm table-container">
        <div class="card-body p-0">
            <table class="table table-hover align-middle mb-0">
                <thead class="bg-dark text-white">
                    <tr>
                        <th class="ps-4 py-3">Thời điểm</th>
                        <th>Tài liệu & Mã nguồn</th>
                        <th style="width: 200px;">Độ tương đồng đạo văn</th>
                        <th>Trạng thái đạo văn</th>
                        <th>Điểm tương quan giữa đề tài với bài luận án</th>
                        <th>Tình trạng lạc đề</th>
                        <th>Điểm đề tài</th>
                        <th>Feedback cho sinh viên</th>
                        <th>Chi tiết AI</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="h" items="${historyList}" varStatus="loop">
                        <tr>
                            <td class="ps-4">
                                <div class="d-flex flex-column">
                                    <span class="fw-bold text-dark">${h.createdAt.format(dateFormatter)}</span>
                                </div>
                            </td>

                            <td>
                                <div class="mb-1">
                                    <c:if test="${not empty h.reportFile}">
                                        <a href="${h.reportFile}" class="text-decoration-none me-3">
                                            <i class="fas fa-file-word text-primary me-1"></i> <small>Báo cáo</small>
                                        </a>
                                    </c:if>
                                    <c:if test="${not empty h.sourceCodeLink}">
                                        <a href="${h.sourceCodeLink}" target="_blank" class="text-decoration-none text-success">
                                            <i class="fab fa-github me-1"></i> <small>Source Code</small>
                                        </a>
                                    </c:if>
                                </div>
                                <div class="small text-muted text-truncate" style="max-width: 250px;">
                                    <i class="fas fa-link me-1"></i> ${h.bestSource != null ? h.bestSource : 'Chưa xác định nguồn'}
                                </div>
                            </td>

                            <td>
                                <div class="d-flex align-items-center">
                                    <span class="me-2 fw-bold">${h.similarityScore}%</span>
                                    <div class="progress flex-grow-1 similarity-bar">
                                        <div class="progress-bar ${h.similarityScore > 50 ? 'bg-danger' : (h.similarityScore > 20 ? 'bg-warning' : 'bg-success')}" 
                                             role="progressbar" style="width: ${h.similarityScore}%"></div>
                                    </div>
                                </div>
                            </td>

                            <td>
                                <c:set var="statusClass" value="${h.plagiarismStatus == 'An toàn' ? 'bg-success' : (h.plagiarismStatus == 'Cần kiểm tra lại' ? 'bg-warning text-dark' : 'bg-danger')}" />
                                <span class="badge rounded-pill status-badge ${statusClass}">
                                    ${h.plagiarismStatus}
                                </span>
                            </td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <span class="me-2 fw-bold">${h.relevantTopicScore}%</span>
                                    <div class="progress flex-grow-1 similarity-bar">
                                        <div class="progress-bar ${h.relevantTopicScore > 50 ? 'bg-danger' : (h.relevantTopicScore > 20 ? 'bg-warning' : 'bg-success')}" 
                                             role="progressbar" style="width: ${h.relevantTopicScore}%"></div>
                                    </div>
                                </div>
                            </td>

                            <td>
                                <c:set var="statusClass" value="${h.relevantTopicStatus == 'Đúng chủ đề' ? 'bg-success' : (h.relevantTopicStatus == 'Cần điều chỉnh' ? 'bg-warning text-dark' : 'bg-danger')}" />
                                <span class="badge rounded-pill status-badge ${statusClass}">
                                    ${h.relevantTopicStatus}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty h.score}">
                                        <span class="badge bg-primary fs-6">${h.score}/10</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted small italic">Chưa chấm</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <c:choose>
                                        <c:when test="${not empty h.feedback}">
                                            <button type="button" 
                                                    class="btn btn-sm btn-outline-info border-0 rounded-circle"
                                                    data-bs-toggle="popover" 
                                                    data-bs-trigger="focus" 
                                                    data-bs-placement="top"
                                                    title="<i class='fas fa-comment-dots me-2'></i>Nhận xét của giảng viên"
                                                    data-bs-html="true"
                                                    data-bs-content="<div class='small text-dark'>${h.feedback}</div>">
                                                <i class="fas fa-comment-medical fs-5"></i>
                                            </button>
                                            <span class="ms-2 small text-muted d-none d-xl-inline">Có phản hồi</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted opacity-50 small"><em>Chưa có</em></span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td>
                                <button class="btn btn-sm btn-light border btn-analysis" 
                                        data-bs-toggle="modal" data-bs-target="#analysisModal${loop.index}">
                                    <i class="fas fa-brain text-info"></i>
                                </button>

                                <div class="modal fade" id="analysisModal${loop.index}" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered modal-lg">
                                        <div class="modal-content border-0 shadow-lg">
                                            <div class="modal-header border-0 bg-light">
                                                <h5 class="modal-title fw-bold">
                                                    <i class="fas fa-robot text-primary me-2"></i>Kết quả phân tích từ Setence Embedding models
                                                </h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body p-4">
                                                <div class="alert alert-info border-0 mb-4">
                                                    <strong>Nguồn đối chiếu chính:</strong><br>
                                                    <a href="${h.bestSource}" target="_blank" class="alert-link small">${h.bestSource}</a>
                                                </div>
                                                <h6 class="fw-bold mb-3 text-secondary uppercase"><i class="fas fa-comment-dots me-2"></i>Nhận xét chuyên sâu(Đạo văn):</h6>
                                                <div class="bg-white p-3 rounded border" style="white-space: pre-wrap; line-height: 1.8; color: #444;">${h.plagiarismAnalysis}</div>
                                                <br/>
                                                <h6 class="fw-bold mb-3 text-secondary uppercase"><i class="fas fa-comment-dots me-2"></i>Nhận xét chuyên sâu(Lạc đề):</h6>
                                                <div class="bg-white p-3 rounded border" style="white-space: pre-wrap; line-height: 1.8; color: #444;">${h.relevanceAnalysis}</div>                                                
                                            </div>
                                            <div class="modal-footer border-0">
                                                <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Đóng</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty historyList}">
                        <tr>
                            <td colspan="9" class="text-center py-5">
                                <img src="https://cdn-icons-png.flaticon.com/512/7486/7486744.png" alt="Empty" style="width: 80px;" class="mb-3 opacity-50">
                                <p class="text-muted">Chưa có bản ghi nộp bài nào được tìm thấy.</p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function () {
       
        var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
        var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl, {
                sanitize: false 
            })
        })
    });
</script>         
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>