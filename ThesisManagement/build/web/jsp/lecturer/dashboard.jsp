<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Giảng viên | Hệ thống Quản lý Đồ án</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root { --ai-purple: #6f42c1; --pending-orange: #fd7e14; }
        .card { border-radius: 12px; border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.05); transition: 0.3s; }
        .card:hover { transform: translateY(-5px); }
        .ai-card { border-left: 5px solid var(--ai-purple); background-color: #f8f5ff; }
        .status-badge { font-size: 0.85rem; padding: 0.4em 0.8em; border-radius: 20px; }
        .table-container { background: white; border-radius: 12px; padding: 20px; }
        .btn-approve { background-color: #198754; color: white; }
        .btn-approve:hover { background-color: #157347; color: white; }
    </style>
</head>
<body class="bg-light">

<div class="container py-4">
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm border-0 mb-4" role="alert">
            <div class="d-flex align-items-center">
                <i class="fas fa-exclamation-circle me-2 fs-4"></i>
                <div>
                    <strong>Thông báo lỗi:</strong> ${sessionScope.error}
                </div>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="error" scope="session" />
    </c:if>    
    <div class="mt-3">
        <c:choose>
            <c:when test="${param.msg == 'approved'}">
                <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    <strong>Thành công!</strong> Đã phê duyệt yêu cầu đăng ký đề tài của sinh viên.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:when>

            
            <c:when test="${param.msg == 'limit_reached'}">
                <div class="alert alert-warning alert-dismissible fade show border-0 shadow-sm" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <strong>Thông báo:</strong> Bạn đã đạt giới hạn tối đa (5 sinh viên). Không thể phê duyệt thêm.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:when>

            
            <c:when test="${param.msg == 'error' || param.msg == 'system_error'}">
                <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm" role="alert">
                    <i class="fas fa-times-circle me-2"></i>
                    <strong>Lỗi:</strong> Có lỗi xảy ra trong quá trình xử lý. Vui lòng thử lại sau.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:when>
        </c:choose>
    </div>       
    <div class="row mb-4 align-items-center">
        <div class="col-md-7">
            <h2 class="fw-bold">Xin chào, Giảng Viên. ${lecturer.fullName}! 👋</h2>
            <p class="text-muted mb-0">Mã giảng viên: <strong>${lecturer.mscv}</strong> | Khoa: CNTT</p>
        </div>
        <div class="col-md-5 text-md-end">
            <a href="${pageContext.request.contextPath}/lecturer/profile" class="btn btn-outline-primary btn-sm me-2">
                <i class="fas fa-user-circle"></i> Xem hồ sơ cá nhân
            </a>            
            <a href="${pageContext.request.contextPath}/lecturer/logout" class="btn btn-outline-danger btn-sm">
                <i class="fas fa-sign-out-alt me-1"></i> Đăng xuất
            </a>
        </div>
    </div>

    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="card p-3 h-100">
                <div class="d-flex align-items-center">
                    <div class="rounded-circle bg-primary bg-opacity-10 p-3 me-3">
                        <i class="fas fa-users text-primary"></i>
                    </div>
                    <div>
                        <small class="text-muted d-block">Đang hướng dẫn</small>
                        <span class="fw-bold fs-5">${activeStudentsCount} Sinh viên</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card p-3 h-100 border-warning" style="border-left: 5px solid orange !important;">
                <div class="d-flex align-items-center">
                    <div class="rounded-circle bg-warning bg-opacity-10 p-3 me-3">
                        <i class="fas fa-clock text-warning"></i>
                    </div>
                    <div>
                        <small class="text-muted d-block">Chờ phê duyệt</small>
                        <span class="fw-bold fs-5 text-warning">${pendingRequestsCount} Yêu cầu</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card p-3 h-100">
                <div class="d-flex align-items-center">
                    <div class="rounded-circle bg-success bg-opacity-10 p-3 me-3">
                        <i class="fas fa-check-circle text-success"></i>
                    </div>
                    <div>
                        <small class="text-muted d-block">Đề tài đã duyệt</small>
                        <span class="fw-bold fs-5">${approvedTopicsCount}</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card p-3 h-100">
                <div class="d-flex align-items-center">
                    <div class="rounded-circle bg-info bg-opacity-10 p-3 me-3">
                        <i class="fas fa-file-signature text-info"></i>
                    </div>
                    <div>
                        <small class="text-muted d-block">Số lượng báo cáo đã nộp</small>
                        <span class="fw-bold fs-5">${numberOfReportCount}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 mb-4">
            <div class="table-container shadow-sm">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0 text-dark"><i class="fas fa-user-clock me-2 text-warning"></i>Yêu cầu đăng ký mới</h5>
                    <span class="badge bg-warning text-dark">Chờ xác nhận</span>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Sinh viên</th>
                                <th>Mã SV</th>
                                <th>Tên đề tài</th>
                                <th>Ngày đăng ký</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="req" items="${pendingRequests}">
                                <tr>
                                    <td>
                                        <div class="fw-bold">${req.studentName}</div>
                                    </td>
                                    <td>${req.mssv}</td>
                                    <td style="max-width: 300px;">${req.topicTitle}</td>
                                    <td><fmt:formatDate value="${req.registeredAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/lecturer/topic/approve" method="POST" class="d-inline">
                                            <input type="hidden" name="topicId" value="${req.topicId}">
                                            <input type="hidden" name="studentId" value="${req.mssv}">
                                            <button type="submit" class="btn btn-approve btn-sm px-3 shadow-sm">
                                                <i class="fas fa-check me-1"></i> Chấp nhận
                                            </button>
                                        </form>
                                        <button class="btn btn-outline-danger btn-sm px-3 ms-1" data-bs-toggle="modal" data-bs-target="#rejectModal${req.topicId}">
                                            <i class="fas fa-times me-1"></i> Từ chối
                                        </button>
                                    </td>
                                </tr>
                                <div class="modal fade" id="rejectModal${req.topicId}" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content border-0 shadow">
                                            <form action="${pageContext.request.contextPath}/lecturer/topic/reject" method="POST">
                                                <div class="modal-header bg-danger text-white">
                                                    <h5 class="modal-title fw-bold"><i class="fas fa-times-circle me-2"></i>Từ chối đề tài</h5>
                                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body p-4">
                                                    <p>Bạn đang từ chối đề tài: <strong class="text-danger">${req.topicTitle}</strong></p>
                                                    <p>Sinh viên: <strong>${req.studentName} (${req.mssv})</strong></p>

                                                    <input type="hidden" name="topicId" value="${req.topicId}">
                                                    <input type="hidden" name="studentId" value="${req.mssv}">
                                                </div>
                                                <div class="modal-footer bg-light border-0">
                                                    <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">Hủy</button>
                                                    <button type="submit" class="btn btn-danger px-4 shadow-sm">Xác nhận từ chối</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>                                
                            </c:forEach>
                            <c:if test="${empty pendingRequests}">
                                <tr>
                                    <td colspan="5" class="text-center py-4 text-muted">Không có yêu cầu nào đang chờ xử lý.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card p-4 h-100">
                <h5 class="fw-bold mb-3">Quản lý Đề tài</h5>
                <div class="d-grid gap-2">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addTopicModal">
                        <i class="fas fa-plus me-2"></i> Thêm đề tài mới
                    </button>
                    <a href="${pageContext.request.contextPath}/lecturer/topics" class="btn btn-outline-secondary text-start">
                        <i class="fas fa-list me-2"></i> Danh sách đề tài của tôi
                    </a>
                    <a href="${pageContext.request.contextPath}/lecturer/GuidedStudentList" class="btn btn-outline-secondary text-start">
                        <i class="fas fa-users-cog me-2"></i> Quản lý sinh viên hướng dẫn
                    </a>
                </div>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="table-container shadow-sm">
                <h5 class="fw-bold mb-3"><i class="fas fa-tasks me-2 text-primary"></i>Theo dõi tiến độ sinh viên</h5>
                <div class="table-responsive text-nowrap">
                    <table class="table table-sm table-hover align-middle">
                        <thead>
                            <tr>
                                <th>Sinh viên</th>
                                <th>Báo cáo cuối/Source code link</th>
                                <th>Báo cáo AI(Về đạo văn)</th>
                                <th>Báo cáo AI(Về lạc đề)</th>
                                <th>Chấm điểm cho sinh viên</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${guidedStudentsProgress}">
                                <tr class="align-middle">
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="avatar-sm me-3 bg-light rounded-circle d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                                                <i class="fas fa-user text-secondary"></i>
                                            </div>
                                            <div>
                                                <h6 class="mb-0">${s.fullName}</h6>
                                                
                                            </div>
                                        </div>
                                    </td>

                                    <td>
                                        <div class="d-flex flex-column gap-2">
                                            <a href="${s.reportFile}" target="_blank" class="btn btn-sm btn-outline-success text-start">
                                                <i class="fas fa-file-pdf me-1"></i> Report File
                                            </a>
                                            <a href="${s.sourceCodeLink}" target="_blank" class="btn btn-sm btn-outline-dark text-start">
                                                <i class="fab fa-github me-1"></i> Source Code
                                            </a>
                                        </div>
                                    </td>

                                    <td style="min-width: 200px;">
                                        <div class="mb-1 d-flex justify-content-between">
                                            <small class="fw-bold"> ${s.similarityScore}%</small>
                                            <span class="badge ${s.similarityScore > 30 ? 'bg-danger' : (s.similarityScore > 15 ? 'bg-warning' : 'bg-success')}">
                                                ${s.plagiarismStatus}
                                            </span>
                                        </div>
                                        <div class="progress" style="height: 6px;">
                                            <div class="progress-bar ${s.similarityScore > 30 ? 'bg-danger' : (s.similarityScore > 15 ? 'bg-warning' : 'bg-success')}" 
                                                 role="progressbar" style="width: ${s.similarityScore}%"></div>
                                        </div>
                                        <c:if test="${not empty s.bestSource and s.bestSource != 'N/A'}">
                                            <div class="mt-2 text-truncate" style="max-width: 180px;">
                                                <a href="${s.bestSource}" target="_blank" class="text-decoration-none small text-muted">
                                                    <i class="fas fa-link me-1"></i> Nguồn: ${s.bestSource}
                                                </a>
                                            </div>
                                        </c:if>
                                    </td>

                                    <td style="min-width: 180px;">
                                        <div class="mb-1 d-flex justify-content-between">
                                            <small class="fw-bold">${s.relevantTopicScore}%</small>
                                            <span class="badge ${s.relevantTopicScore > 50 ? 'bg-danger' : 'bg-info text-dark'}">
                                                ${s.relevantTopicStatus}
                                            </span>
                                        </div>
                                        <div class="progress" style="height: 6px;">
                                            <div class="progress-bar ${s.relevantTopicScore > 50 ? 'bg-danger' : 'bg-info'}" 
                                                 role="progressbar" style="width: ${s.relevantTopicScore}%"></div>
                                        </div>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-warning" 
                                            title="Thêm điểm cho bài luận văn của sinh viên" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#ScoringModal" 
                                            onclick="prepareScoreModal('${s.mssv}', '${s.fullName}', '${s.thesisId}','${s.reportFile}','${s.sourceCodeLink}','${s.similarityScore}','${s.plagiarismStatus}','${s.relevantTopicScore}','${s.relevantTopicStatus}')">
                                            <i class="fas fa-edit"></i>
                                        </button>                                        
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
<div class="modal fade" id="addTopicModal" tabindex="-1" aria-labelledby="addTopicModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered"> 
        <div class="modal-content border-0 shadow-lg"> 
            <form action="${pageContext.request.contextPath}/lecturer/topic/add" method="POST">    
                <div class="modal-header bg-light">
                    <h5 class="modal-title fw-bold text-primary" id="addTopicModalLabel">
                        <i class="fas fa-plus-circle me-2"></i>Thêm Đề Tài Đồ Án Mới
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body p-4">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label fw-bold"><i class="fas fa-hashtag me-1"></i>Mã đề tài</label>
                            <input type="text" name="topicCode" class="form-control" placeholder="DT001..." required>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label fw-bold"><i class="fas fa-edit me-1"></i>Tên đề tài</label>
                            <input type="text" name="title" class="form-control" placeholder="Ví dụ: Xây dựng hệ thống quản lý..." required>
                        </div>

                        <div class="col-12">
                            <label class="form-label fw-bold"><i class="fas fa-align-left me-1"></i>Mô tả chi tiết</label>
                            <textarea name="description" class="form-control" rows="4" placeholder="Mô tả mục tiêu và phạm vi của đồ án..." required></textarea>
                        </div>

                        <div class="col-md-12">
                            <label class="form-label fw-bold"><i class="fas fa-tools me-1"></i>Yêu cầu kỹ thuật</label>
                            <input type="text" name="technicalRequirements" class="form-control" placeholder="Java, Spring Boot, MySQL, ReactJS...">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-bold"><i class="fas fa-layer-group me-1"></i>Độ khó (1-5)</label>
                            <div class="input-group">
                                <input type="number" name="difficultyScore" class="form-control" min="1" max="5" value="1" step="1">
                                <span class="input-group-text">/5</span>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-bold"><i class="fas fa-info-circle me-1"></i>Trạng thái</label>
                            <select name="status" class="form-select">
                                <option value="AVAILABLE" selected>Sẵn sàng (Available)</option>
                                <option value="END">Kết thúc (End)</option>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-bold"><i class="fas fa-user-tag me-1"></i>Nguồn đề xuất</label>
                            <select name="type" class="form-select">
                                <option value="LECTURER_SUGGESTED">Giảng viên đề xuất</option>
                                <option value="STUDENT_SUGGESTED">Sinh viên đề xuất</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="modal-footer bg-light border-0">
                    <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary px-4 shadow-sm">
                        <i class="fas fa-save me-2"></i>Lưu đề tài
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade" id="ScoringModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered"> <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title"><i class="fas fa-graduation-cap me-2"></i>Chấm điểm luận văn</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form action="${pageContext.request.contextPath}/lecturer/score" method="POST">
                <div class="modal-body p-4">
                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label class="small text-muted fw-bold">HỌ VÀ TÊN</label>
                            <input type="text" name="fullName" class="form-control bg-light border-0" id='modalStudentName' readonly>
                        </div>
                        <div class="col-md-6">
                            <label class="small text-muted fw-bold">MSSV</label>
                            <input type="text" name="mssv" class="form-control bg-light border-0" id='modalMssv' readonly>
                        </div>
                        <div class="col-12">
                            <label class="small text-muted fw-bold">FILE BÁO CÁO</label>
                            <div class="input-group">
                                <span class="input-group-text bg-white"><i class="far fa-file-pdf text-danger"></i></span>
                                <input type="text" name="reportFile" class="form-control bg-light border-start-0" id="modalReportFile" readonly>
                            </div>
                        </div>
                    </div>

                    <hr class="text-muted opacity-25">

                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <div class="p-3 border rounded bg-light-subtle">
                                <label class="d-block small text-muted fw-bold mb-2">PHÂN TÍCH ĐẠO VĂN</label>
                                <div class="d-flex align-items-center justify-content-between">
                                    <input type="text" name="similarityScore" class="form-control-plaintext fw-bold p-0" id='modalSimilarityScore' readonly>
                                    <span id="plagiarismBadge" class="badge rounded-pill"></span>
                                </div>
                                <input type="hidden" name="plagiarismStatus" id='modalPlagiarismStatus'>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="p-3 border rounded bg-light-subtle">
                                <label class="d-block small text-muted fw-bold mb-2">ĐỘ TƯƠNG QUAN ĐỀ TÀI</label>
                                <div class="d-flex align-items-center justify-content-between">
                                    <input type="text" name="relevantTopicScore" class="form-control-plaintext fw-bold p-0" id='modalRelevantTopicScore' readonly>
                                    <span id="relevantBadge" class="badge rounded-pill"></span>
                                </div>
                                <input type="hidden" name="relevantTopicStatus" id='modalRelevantTopicStatus'>
                            </div>
                        </div>
                    </div>

                    <div class="bg-primary bg-opacity-10 p-4 rounded-3 border border-primary border-opacity-25">
                        <h6 class="text-primary fw-bold mb-3"><i class="fas fa-pen-nib me-2"></i>Đánh giá của Giảng viên</h6>
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label fw-bold">Điểm số (thang 10)</label>
                                <div class="input-group input-group-lg">
                                    <input type="number" name="studentScore" class="form-control border-primary text-primary fw-bold" 
                                           min="0" max="10" value="0" step="0.1" value="${score}" required>
                                    <span class="input-group-text bg-primary text-white">/10</span>
                                </div>
                            </div>
                            <div class="col-md-8">
                                <label class="form-label fw-bold">Nhận xét & Phản hồi</label>
                                <textarea name="feedback" value="${feedback}" class="form-control border-primary" rows="2" placeholder="Nhập nhận xét cho sinh viên..."></textarea>
                            </div>
                        </div>
                    </div>

                    <input type="hidden" name="thesisId" id="modalThesis">
                    <input type="hidden" name="sourceCodeLink" id='modalSourceCodeLink'>
                </div>

                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary px-5 fw-bold">Xác nhận điểm</button>
                </div>
            </form>
        </div>
    </div>
</div>              
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script> 
function prepareScoreModal(mssv, fullName, thesisId, reportFile, sourceCodeLink, similarityScore, plagiarismStatus, relevantTopicScore, relevantTopicStatus) {
    document.getElementById('modalMssv').value = mssv;
    document.getElementById('modalStudentName').value = fullName;
    document.getElementById('modalThesis').value = thesisId;
    document.getElementById('modalReportFile').value = reportFile;
    document.getElementById('modalSourceCodeLink').value = sourceCodeLink;
    document.getElementById('modalSimilarityScore').value = similarityScore + "%";
    document.getElementById('modalRelevantTopicScore').value = relevantTopicScore + "/10";

    
    const plagBadge = document.getElementById('plagiarismBadge');
    plagBadge.innerText = plagiarismStatus;
    plagBadge.className = 'badge rounded-pill ' + 
        (plagiarismStatus === 'An toàn' ? 'bg-success' : 'bg-danger');

    // Cập nhật Badge Tương quan
    const relBadge = document.getElementById('relevantBadge');
    relBadge.innerText = relevantTopicStatus;
    relBadge.className = 'badge rounded-pill ' + 
        (relevantTopicStatus === 'Tốt' ? 'bg-success' : 'bg-warning text-dark');
}
</script>
</body>
</html>