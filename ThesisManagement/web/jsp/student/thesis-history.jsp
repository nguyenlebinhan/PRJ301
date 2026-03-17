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
    <style>
        body { background-color: #f4f7f6; }
        .table-container { border-radius: 15px; overflow: hidden; }
        .similarity-bar { height: 8px; border-radius: 4px; }
        .btn-analysis { transition: all 0.3s; }
        .btn-analysis:hover { transform: translateY(-2px); box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .status-badge { font-size: 0.85rem; padding: 0.5em 1em; }
    </style>
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="d-flex justify-content-between align-items-end mb-4">
        <div>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-2">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/student/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item active">Lịch sử nộp bài</li>
                </ol>
            </nav>
            <h2 class="fw-bold text-dark mb-0">Nhật ký thay đổi đồ án</h2>
            <p class="text-muted mb-0 mt-1">
                <i class="fas fa-book-open me-2"></i>Đề tài: <span class="text-primary fw-semibold">${thesisInfo.title}</span> 
                <span class="badge bg-secondary ms-2">${thesisInfo.topicCode}</span>
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-outline-secondary px-4 shadow-sm">
            <i class="fas fa-undo me-2"></i>Quay lại
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
                        <th>AI(Check đạo văn/Lạc đề)</th>
                        <th>AI(Gợi ý cải thiện)</th>
                        
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
                                        </div>
                                    </div>
                                </div>
                            </td>

                            <td>
                                <c:if test="${not empty h.aiRequestPrompt}">
                                    <button class="btn btn-sm btn-light border btn-analysis" 
                                            data-bs-toggle="modal" data-bs-target="#improvementModal${loop.index}"
                                            title="Xem đề xuất cải thiện">
                                        <i class="fas fa-rocket text-warning"></i>
                                    </button>

                                    <div class="modal fade" id="improvementModal${loop.index}" tabindex="-1" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered modal-xl">
                                            <div class="modal-content border-0 shadow-lg">
                                                <div class="modal-header border-0 bg-warning text-dark">
                                                    <h5 class="modal-title fw-bold">
                                                        <i class="fas fa-magic me-2"></i>Chiến lược cải thiện đồ án từ AI
                                                    </h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body p-4 bg-light">
                                                    <div class="row g-4">
                                                        <div class="col-md-7">
                                                            <div class="card border-0 shadow-sm mb-4">
                                                                <div class="card-body">
                                                                    <h6 class="fw-bold text-primary text-uppercase small"><i class="fas fa-search-plus me-2"></i>Phân tích trọng tâm</h6>
                                                                    <hr>
                                                                    <div class="text-secondary" style="white-space: pre-wrap; line-height: 1.6;">${h.focusAnalysis}</div>
                                                                </div>
                                                            </div>
                                                            <div class="card border-0 shadow-sm">
                                                                <div class="card-body">
                                                                    <h6 class="fw-bold text-success text-uppercase small"><i class="fas fa-clipboard-list me-2"></i>Quan sát chung</h6>
                                                                    <hr>
                                                                    <div class="text-secondary" style="white-space: pre-wrap; line-height: 1.6;">${h.generalObservations}</div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-5">
                                                            <div class="card border-0 shadow-sm mb-4 border-top border-4 border-danger">
                                                                <div class="card-body">
                                                                    <h6 class="fw-bold text-danger text-uppercase small"><i class="fas fa-star me-2"></i>Ưu tiên hàng đầu</h6>
                                                                    <div class="p-3 bg-danger bg-opacity-10 rounded mt-2 text-danger fw-bold shadow-sm">
                                                                        ${h.topPrior}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="card border-0 shadow-sm bg-dark">
                                                                <div class="card-body">
                                                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                                                        <h6 class="fw-bold text-light text-uppercase small mb-0 font-monospace">AI Request Prompt</h6>
                                                                    </div>
                                                                    <div class="text-info small p-2" style="max-height: 150px; overflow-y: auto; font-family: 'Courier New', monospace; background: #1a1a1a;">
                                                                        ${h.aiRequestPrompt}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>                                    
                                </c:if>
                            </td>                            
                        </tr>
                    </c:forEach>

                    <c:if test="${empty historyList}">
                        <tr>
                            <td colspan="5" class="text-center py-5">
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
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>