<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Đề tài | TMS</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    
    <style>
        :root {
            --sidebar-bg: #0f172a;
            --main-bg: #f8fafc;
            --accent: #6366f1;
            --text-main: #1e293b;
            --text-muted: #64748b;
        }

        body { 
            background-color: var(--main-bg); 
            font-family: 'Inter', sans-serif; 
            color: var(--text-main);
        }

        .sidebar { 
            width: 260px; height: 100vh; position: fixed; 
            background: var(--sidebar-bg); color: white; 
            padding: 1.5rem 1rem; z-index: 1000;
        }
        .sidebar-brand { font-size: 1.25rem; font-weight: 700; padding: 0 1rem 2rem; display: flex; align-items: center; }
        .nav-link { color: #94a3b8; padding: 0.8rem 1rem; border-radius: 10px; transition: 0.2s; display: flex; align-items: center; font-weight: 500; text-decoration: none; }
        .nav-link i { margin-right: 12px; font-size: 1.2rem; }
        .nav-link:hover { background: rgba(255,255,255,0.05); color: #fff; }
        .nav-link.active { background: var(--accent); color: white; box-shadow: 0 10px 15px -3px rgba(99, 102, 241, 0.3); }

        .main-content { margin-left: 260px; padding: 2rem; }
        
        .card-custom {
            background: #fff; border-radius: 16px; border: none;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
            overflow: hidden;
        }
        
        .table thead th {
            background: #f8fafc; color: var(--text-muted);
            font-weight: 600; font-size: 0.75rem;
            text-transform: uppercase; letter-spacing: 0.05em;
            padding: 1rem; border: none;
        }

        .table tbody td { padding: 1.2rem 1rem; vertical-align: middle; border-bottom: 1px solid #f1f5f9; }

        .topic-title {
            font-weight: 600; color: var(--text-main);
            display: block; max-width: 400px;
            white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
        }

        .badge-status {
            padding: 0.4rem 0.8rem; border-radius: 8px; font-size: 0.7rem; font-weight: 700; text-transform: uppercase;
        }

        .btn-action {
            width: 32px; height: 32px; padding: 0; display: inline-flex;
            align-items: center; justify-content: center; border-radius: 8px;
            transition: 0.2s;
        }
        
        .search-box {
            background: #fff; border: 1px solid #e2e8f0; border-radius: 12px;
            padding: 0.5rem 1rem; display: flex; align-items: center; width: 300px;
        }
        .search-box input { border: none; outline: none; margin-left: 10px; width: 100%; font-size: 0.9rem; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand"><i class="bi bi-mortarboard-fill me-2 text-primary"></i> TMS ADMIN</div>
        <nav class="nav flex-column">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i class="bi bi-grid-1x2"></i> Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/list" class="nav-link"><i class="bi bi-people"></i> Quản lý User</a>
            <a href="${pageContext.request.contextPath}/admin/topic" class="nav-link"><i class="bi bi-journal-text"></i> Đề tài</a>
            <a href="${pageContext.request.contextPath}/admin/thesis" class="nav-link"><i class="bi bi-layers"></i> Đồ án</a>
            <hr class="text-secondary mx-2">
            <a href="${pageContext.request.contextPath}/admin/logout" class="nav-link text-danger"><i class="bi bi-box-arrow-right"></i> Đăng xuất</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Danh mục đề tài</h3>
                <p class="text-muted mb-0">Quản lý kho đề tài nghiên cứu và đồ án tốt nghiệp.</p>
            </div>
            <div class="d-flex gap-2">
                <form action="${pageContext.request.contextPath}/admin/topic" method="GET" class="search-box d-none d-md-flex">
                    <i class="bi bi-search text-muted"></i>
                    <input type="text" name="query" value="${param.query}" placeholder="Tìm tên đề tài ...">
                    <button type="submit" class="d-none"></button> </form>
            </div>
        </div>

        <div class="card-custom">
            <div class="table-responsive">
                <table class="table mb-0">
                    <thead>
                        <tr>
                            <th>Mã ĐT</th>
                            <th>Tên đề tài</th>
                            <th>Giảng viên</th>
                            
                            <th>Trạng thái</th>
                            <th class="text-end">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="t" items="${listTopic}">
                            <tr>
                                <td class="text-muted fw-medium small">#${t.topicId}</td>
                                <td>
                                    <span class="topic-title" title="${t.title}">${t.title}</span>
                                   
                                </td>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <div class="text-dark fw-medium small">${t.lecturer.fullName}</div>
                                    </div>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${t.status == 'AVAILABLE'}">
                                            <span class="badge-status bg-success-subtle text-success">Đang mở</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status bg-secondary-subtle text-secondary">Đã đóng</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <button class="btn btn-sm btn-outline-warning me-1" 
                                            onclick="editTopic('${t.topicId}', '${t.topicCode}','${t.description}','${t.technicalRequirements}' ,'${t.title}', '${t.status}', '${t.difficultyScore}','${t.lecturer.mscv}')">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <a href="${pageContext.request.contextPath}/admin/topic/delete?id=${t.topicId}" 
                                       class="btn btn-sm btn-outline-danger" 
                                       onclick="return confirm('Bạn có chắc muốn xóa đề tài này khỏi danh sách?')">
                                        <i class="fas fa-trash"></i>
                                    </a>                                  
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <%-- Trường hợp danh sách trống --%>
                        <c:if test="${empty listTopic}">
                            <tr>
                                <td colspan="6" class="text-center py-5">
                                    <i class="bi bi-inbox fs-1 text-muted d-block mb-2"></i>
                                    <span class="text-muted">Chưa có đề tài nào được đăng ký.</span>
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <div class="p-3 bg-light border-top d-flex justify-content-between align-items-center">
                <span class="small text-muted">Tổng cộng: <strong>${listTopic.size()}</strong> đề tài</span>
            </div>
        </div>
    </main>
    <div class="modal fade" id="editTopicModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/admin/topic/update" method="POST">
                    <div class="modal-header">
                        <h5 class="modal-title">Chỉnh sửa đề tài</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="topicId" id="editTopicId">
                        <input type="hidden" name="mscv" id="editMscv">
                        <div class="mb-3">
                            <label class="form-label">Mã số</label>
                            <input type="text" class="form-control" id="editTopicCode" readonly>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Tên đề tài</label>
                            <input type="text" name="title" class="form-control" id="editTitle" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mô tả đề tài</label>
                            <input type="text" name="description" class="form-control" id="editDescription" required>
                        </div>    
                        <div class="mb-3">
                            <label class="form-label">Yêu cầu kỹ thuật</label>
                            <input type="text" name="technicalRequirements" class="form-control" id="editTechnicalRequirements" required>
                        </div>                              
                        
                        <div class="mb-3">
                            <label class="form-label">Trạng thái</label>
                            <select name="status" class="form-select" id="editStatus">
                                <option value="AVAILABLE">AVAILABLE</option>
                                <option value="END">END</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Điểm độ khó</label>
                            <input type="number" name="difficultyScore" min="1" max="5" value="5" step="1" class="form-control" id="editDifficultyScore" required >
                        </div>                        
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>                
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    function editTopic(id, code, description , technicalRequirements , title, status, difficultyScore,mscv) {
        // 1. Đổ dữ liệu vào các input trong Modal
        document.getElementById('editTopicId').value = id;
        document.getElementById('editTopicCode').value = code;
        document.getElementById('editDescription').value = description;
        document.getElementById('editTechnicalRequirements').value=technicalRequirements;
        document.getElementById('editTitle').value = title;
        document.getElementById('editStatus').value = status;
        document.getElementById('editDifficultyScore').value = difficultyScore;
        document.getElementById('editMscv').value = mscv;
        // 2. Hiển thị Modal
        var myModal = new bootstrap.Modal(document.getElementById('editTopicModal'));
        myModal.show();
    }
    </script>            

</body>
</html>