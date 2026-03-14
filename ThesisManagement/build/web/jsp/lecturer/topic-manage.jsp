<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Đề tài | Giảng viên</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold text-primary"><i class="fas fa-book-open me-2"></i>Kho Đề tài của Tôi</h2>
            <a href="${pageContext.request.contextPath}/lecturer/dashboard" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-arrow-left me-1"></i> Quay lại Dashboard
            </a>            
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <table class="table table-hover mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th class="ps-4">Mã số</th>
                            <th>Tên đề tài</th>
                            <th>Mô tả đề tài</th>
                            <th>Mô tả kĩ thuật</th>
                            <th>Trạng thái</th>
                            <th>Điểm độ khó</th>
                            <th class="text-center">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="t" items="${topics}">
                            <c:if test="${t.status != 'HIDDEN'}"> <tr>
                                    <td class="ps-4 fw-bold">${t.topicCode}</td>
                                    <td>${t.title}</td>
                                    <td>${t.description}</td>
                                    <td>${t.technicalRequirements}</td>
                                    <td>
                                        <span class="badge ${t.status == 'AVAILABLE' ? 'bg-success' : 'bg-secondary'}">
                                            ${t.status}
                                        </span>
                                    </td>
                                    <td>${t.difficultyScore}</td>
                                    <td class="text-center">
                                        <button class="btn btn-sm btn-outline-warning me-1" 
                                                onclick="editTopic('${t.topicId}', '${t.topicCode}','${t.description}','${t.technicalRequirements}' ,'${t.title}', '${t.status}', '${t.difficultyScore}')">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <a href="${pageContext.request.contextPath}/lecturer/topics/delete?id=${t.topicId}" 
                                           class="btn btn-sm btn-outline-danger" 
                                           onclick="return confirm('Bạn có chắc muốn xóa đề tài này khỏi danh sách?')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                           
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="modal fade" id="editTopicModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/lecturer/topics/update" method="POST">
                    <div class="modal-header">
                        <h5 class="modal-title">Chỉnh sửa đề tài</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="topicId" id="editTopicId">
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
    function editTopic(id, code, description , technicalRequirements , title, status, difficultyScore) {
        // 1. Đổ dữ liệu vào các input trong Modal
        document.getElementById('editTopicId').value = id;
        document.getElementById('editTopicCode').value = code;
        document.getElementById('editDescription').value = description;
        document.getElementById('editTechnicalRequirements').value=technicalRequirements;
        document.getElementById('editTitle').value = title;
        document.getElementById('editStatus').value = status;
        document.getElementById('editDifficultyScore').value = difficultyScore;

        // 2. Hiển thị Modal
        var myModal = new bootstrap.Modal(document.getElementById('editTopicModal'));
        myModal.show();
    }
    </script>
</body>
</html>