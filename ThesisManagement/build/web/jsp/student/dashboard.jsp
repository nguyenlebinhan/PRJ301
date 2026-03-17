<!DOCTYPE html>
<html lang="vi">
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Sinh viên | Hệ thống Quản lý Đồ án</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="today" />
    <style>
        :root { --ai-purple: #6f42c1; }
        .card { border-radius: 12px; border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
        .progress { height: 8px; border-radius: 10px; }
        .bg-info-soft { background-color: #e0f7fa; color: #00acc1; }
        .status-badge { font-size: 0.9rem; padding: 0.5em 1em; }
        .ai-option { transition: all 0.3s ease; cursor: pointer; border: 2px solid transparent; }
        .ai-option:hover { transform: translateY(-5px); border-color: #0d6efd; }
        .ai-option.active { border-color: #0d6efd; background-color: #f0f7ff; }       
    </style>
</head>
<body class="bg-light">

    <div class="container py-4">
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                <i class="fas fa-check-circle me-2"></i> ${sessionScope.success}
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
        <div class="row mb-4 align-items-center">
            <div class="col-md-7">
                <h2 class="fw-bold">Chào buổi sáng, ${student.fullName}! 👋</h2>
                <p class="text-muted mb-0">MSSV: <strong>${student.mssv}</strong> | Lớp: ${student.className}</p>
            </div>        
            <div class="col-md-5 text-md-end">
                <a href="${pageContext.request.contextPath}/student/profile" class="btn btn-outline-primary btn-sm me-2">
                    <i class="fas fa-user-circle"></i> Xem hồ sơ cá nhân
                </a>

                <a href="${pageContext.request.contextPath}/student/logout" class="btn btn-outline-danger btn-sm">
                    <i class="fas fa-sign-out-alt"></i> Đăng xuất
                </a>     
            </div>
        </div>

        <div class="row g-3 mb-4">
            <div class="col-md-4">
                <div class="card p-3 h-100">
                    <div class="d-flex align-items-center">
                        <div class="rounded-circle bg-primary bg-opacity-10 p-3 me-3">
                            <i class="fas fa-chalkboard-teacher text-primary"></i>
                        </div>
                        <div>
                            <small class="text-muted d-block">Giảng viên hướng dẫn</small>
                            <span class="fw-bold">${not empty supervisorName ? supervisorName : 'Chưa có'}</span>
                        </div>
                    </div>                  
                </div>
            </div>
            <div class="col-md-4">
                <div class="card p-3 h-100">
                    <div class="d-flex align-items-center">
                        <div class="rounded-circle bg-success bg-opacity-10 p-3 me-3">
                            <i class="fas fa-file-alt text-success"></i>
                        </div>
                        <div>
                            <small class="text-muted d-block">Báo cáo đã nộp</small>
                            <span class="fw-bold">${reportCount} Bản báo cáo</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card p-3 h-100">
                    <div class="d-flex align-items-center">
                        <div class="rounded-circle bg-info bg-opacity-10 p-3 me-3">
                            <i class="fas fa-chart-line text-info"></i>
                        </div>
                        <div>
                            <small class="text-muted d-block">GPA Tích lũy</small>
                            <span class="fw-bold">${student.gpa}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <c:choose>
            
            <c:when test="${not empty acceptedTopics}">
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                        <h5 class="fw-bold mb-0 text-primary"><i class="fas fa-list-check me-2"></i>Quản lý Thesis</h5>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th class="ps-4">Mã số</th>
                                        <th>Tên đề tài</th>
                                        <th class="text-center">Quản lý luận án</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    
                                        <tr>
                                            <td class="ps-4"><strong>${acceptedTopics.topicCode}</strong></td>
                                            <td class="ps-4"><strong>${acceptedTopics.topicTitle}</strong></td>
                                            <td class="text-center">
                                                <c:choose>          
                                                    <c:when test="${acceptedTopics.thesisId == null || acceptedTopics.thesisId == 0}">
                                                        <button class="btn btn-sm btn-primary px-3 shadow-sm" 
                                                                data-bs-toggle="modal" 
                                                                data-bs-target="#addThesisModal${acceptedTopics.topicId}">
                                                            <i class="fas fa-plus-circle me-1"></i> Thêm đề tài mới
                                                        </button>
                                                    </c:when>                  
                                                    <c:otherwise>
                                                        <div class="btn-group shadow-sm">
                                                            <a href="${pageContext.request.contextPath}/student/thesis/history?thesisId=${acceptedTopics.thesisId}&mssv=${acceptedTopics.mssv}" 
                                                               class="btn btn-sm btn-outline-info shadow-sm">
                                                                <i class="fas fa-history"></i> Xem lịch sử chi tiết
                                                            </a>                                                                                                       
                                                            <button class="btn btn-sm btn-outline-warning" 
                                                                    data-bs-toggle="modal" 
                                                                    data-bs-target="#editThesisModal${acceptedTopics.thesisId}">
                                                                <i class="fas fa-edit"></i> Sửa
                                                            </button>
                                                            <button class="btn btn-sm btn-outline-primary" 
                                                                    data-bs-toggle="modal" 
                                                                    data-bs-target="#aiSuggestionModal${acceptedTopics.thesisId}">
                                                                <i class="fas fa-robot"></i> Gợi ý AI
                                                            </button>                                                                 
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>

                                        <div class="modal fade" id="addThesisModal${acceptedTopics.topicId}" tabindex="-1" aria-hidden="true">
                                           <div class="modal-dialog">
                                               <div class="modal-content border-0 shadow">
                                                   <form id="uploadForm" action="${pageContext.request.contextPath}/student/thesis/add" method="POST" enctype="multipart/form-data">
                                                       <input type="hidden" name="topicId" value="${acceptedTopics.topicId}">
                                                       <div class="modal-header bg-primary text-white">
                                                           <h5 class="modal-title"><i class="fas fa-upload me-2"></i>Nộp Luận án mới</h5>
                                                           <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                       </div>
                                                       <div class="modal-body">
                                                           <p class="text-muted small">Đề tài: <strong>${acceptedTopics.topicTitle}</strong></p>

                                                           <div class="mb-3">
                                                               <label class="form-label fw-bold text-primary">Tải lên file báo cáo (PDF/Docx)</label>
                                                               <div class="input-group">
                                                                   <span class="input-group-text"><i class="fas fa-file-pdf"></i></span>
                                                                   <input type="file" name="reportFile" class="form-control" accept=".pdf,.docx,.doc" required>
                                                               </div>
                                                               <div class="form-text text-danger mt-1">
                                                                   <small><i class="fas fa-info-circle"></i> AI sẽ tự động quét nội dung file để kiểm tra đạo văn.</small>
                                                               </div>
                                                           </div>                    

                                                           <div class="mb-3">
                                                               <label class="form-label fw-bold">Link Git Repository</label>
                                                               <div class="input-group">
                                                                   <span class="input-group-text"><i class="fab fa-github"></i></span>
                                                                   <input type="url" name="sourceCodeLink" class="form-control" placeholder="https://github.com/..." required>
                                                               </div>
                                                           </div>
                                                       </div>
                                                       <div class="modal-footer bg-light">
                                                           <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                           <button type="submit" class="btn btn-primary px-4">
                                                               <i class="fas fa-paper-plane me-1"></i> Nộp bài & Check AI
                                                           </button>
                                                       </div>
                                                   </form>
                                               </div>
                                           </div>
                                        </div>
                                        <div class="modal fade" id="aiSuggestionModal${acceptedTopics.thesisId}" tabindex="-1" aria-hidden="true">
                                            <div class="modal-dialog modal-lg">
                                                <div class="modal-content border-0 shadow-lg">
                                                    <form action="${pageContext.request.contextPath}/student/thesis/ai-advice" method="POST" id="aiRequestForm${acceptedTopics.thesisId}">
                                                        <div class="modal-header bg-dark text-white">
                                                            <h5 class="modal-title"><i class="fas fa-robot me-2 text-warning"></i>Yêu cầu AI Phân tích</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>

                                                        <div class="modal-body">
                                                            <input type="hidden" name="thesisId" value="${acceptedTopics.thesisId}">
                                                            <input type="hidden" name="reportFile" value="${acceptedTopics.reportFile}">
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold">
                                                                    <i class="fas fa-comment-dots me-2 text-primary"></i>
                                                                    Mô tả hướng suy luận bạn muốn AI tập trung:
                                                                </label>
                                                                <textarea class="form-control border-primary shadow-sm" 
                                                                          name="userPrompt" 
                                                                          rows="5" 
                                                                          placeholder="Ví dụ: Kiểm tra tính logic giữa chương 1 và chương 2; hoặc: Soát lỗi chính tả và văn phong khoa học cho mục 3.2..." 
                                                                          required></textarea>
                                                                <div class="form-text mt-2">
                                                                    <i class="fas fa-info-circle me-1"></i>
                                                                    Mô tả càng chi tiết, AI sẽ phản hồi càng chính xác cho bản báo cáo của bạn.
                                                                </div>
                                                            </div>

                                                            <div id="aiResponseArea${acceptedTopics.thesisId}" class="d-none mt-4">
                                                                <hr>
                                                                <h6 class="fw-bold text-success"><i class="fas fa-magic me-2"></i>Phản hồi từ AI:</h6>
                                                                <div class="bg-light p-3 rounded border border-success shadow-inner" 
                                                                     id="aiContent${acceptedTopics.thesisId}" 
                                                                     style="white-space: pre-line; max-height: 300px; overflow-y: auto;">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="modal-footer bg-light">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                            <button type="submit" class="btn btn-primary px-4" id="btnSubmitAI${acceptedTopics.thesisId}">
                                                                <i class="fas fa-paper-plane me-1"></i> Gửi yêu cầu
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>


                                        <c:if test="${not empty acceptedTopics.thesisId && acceptedTopics.thesisId != 0}">
                                            <div class="modal fade" id="editThesisModal${acceptedTopics.thesisId}" tabindex="-1" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <div class="modal-content border-0 shadow">
                                                        <form class="plagiarism-form" action="${pageContext.request.contextPath}/student/thesis/update" method="POST" enctype="multipart/form-data">
                                                            <input type="hidden" name="thesisId" value="${acceptedTopics.thesisId}">
                                                            <input type="hidden" name="topicId" value="${acceptedTopics.topicId}">
                                                            <div class="modal-header bg-warning text-dark">
                                                                <h5 class="modal-title"><i class="fas fa-sync-alt me-2"></i>Cập nhật bản báo cáo mới</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <div class="mb-3">
                                                                    <label class="form-label fw-bold">Chọn file báo cáo thay thế</label>
                                                                    <input type="file" name="reportFile" class="form-control" accept=".pdf,.docx">
                                                                    <div class="form-text">Bản cũ: <a href="${acceptedTopics.reportFile}" target="_blank" class="text-truncate d-inline-block shadow-none" style="max-width: 200px;">Xem lại file cũ</a></div>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label class="form-label fw-bold">Cập nhật Link Git</label>
                                                                    <input type="url" name="sourceCodeLink" class="form-control" value="${acceptedTopics.sourceCodeLink}">
                                                                </div>                                            
                                                            </div>
                                                            <div class="modal-footer bg-light">
                                                                <button type="submit" class="btn btn-warning px-4">Lưu phiên bản mới</button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>                                                                                   
                                        </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:when>

            
            <c:otherwise>
                <div class="card p-5 text-center shadow-sm border-0">
                    <img src="https://cdn-icons-png.flaticon.com/512/7486/7486744.png" style="width: 100px;" class="mx-auto mb-4 opacity-50">
                    <h3 class="fw-bold">Chưa có đề tài chính thức</h3>
                    <p class="text-muted">Hãy chờ giảng viên phê duyệt đề tài hoặc đăng ký đề tài mới.</p>
                    <div class="mt-3">
                        <a href="${pageContext.request.contextPath}/student/topics" class="btn btn-primary btn-lg px-5">Đến danh sách đề tài</a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>      
</div>  
                            
<script>
    document.getElementById('uploadForm').onsubmit = function() {
        
        var myModalEl = document.querySelector('.modal.show');
        var modal = bootstrap.Modal.getInstance(myModalEl);
        if (modal) modal.hide();


        Swal.fire({
            title: 'Đang phân tích AI...',
            html: 'Hệ thống đang quét <b>các nguồn dữ liệu</b> trên Internet.<br/>Vui lòng chờ trong giây lát (có thể mất 30-45 giây).',
            allowOutsideClick: false,
            showConfirmButton: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });
    };
    
    document.querySelectorAll('[id^="aiRequestForm"]').forEach(form => {
        form.onsubmit = function() {
            
            const openModal = document.querySelector('.modal.show');
            if (openModal) {
                const modalInstance = bootstrap.Modal.getInstance(openModal);
                if (modalInstance) modalInstance.hide();
            }

            
            Swal.fire({
                title: 'AI đang phân tích...',
                html: 'Hệ thống đang đọc nội dung và xử lý yêu cầu của bạn.<br/>Vui lòng đợi trong giây lát (có thể mất 30-45 giây).',
                allowOutsideClick: false,
                showConfirmButton: false,
                didOpen: () => {
                    Swal.showLoading();
                }
            });
        };
    });  

    
    document.querySelectorAll('.plagiarism-form').forEach(form => {
        form.onsubmit = function() {
            
            const openModal = document.querySelector('.modal.show');
            if (openModal) {
                const modalInstance = bootstrap.Modal.getInstance(openModal);
                if (modalInstance) modalInstance.hide();
            }

           
            Swal.fire({
                title: 'Đang cập nhật & Quét AI...',
                html: 'Hệ thống đang phân tích bản báo cáo mới của bạn.<br/>' + 
                     'Quá trình <b>so khớp ngữ nghĩa</b> đang diễn ra...',
                allowOutsideClick: false,
                showConfirmButton: false,
                timerProgressBar: true,
                didOpen: () => {
                    Swal.showLoading();
                }
            });
        };
    });
    
    let selectedMode = 'cautruc'; 

    function selectReasoning(element, mode) {
        document.querySelectorAll('.ai-option').forEach(el => {
            el.classList.remove('active', 'border-primary');
            el.classList.add('border-light');
        });
        element.classList.add('active', 'border-primary');
        element.classList.remove('border-light');
        selectedMode = mode;
    }

</script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>