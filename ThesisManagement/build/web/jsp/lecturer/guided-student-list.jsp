<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Sinh viên Hướng dẫn</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .card { border-radius: 12px; border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
        .table-container { background: white; border-radius: 12px; padding: 20px; }
    </style>
</head>
<body class="bg-light">

<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold"><i class="fas fa-user-graduate me-2 text-primary"></i>Sinh viên hướng dẫn</h2>
        <a href="${pageContext.request.contextPath}/lecturer/dashboard" class="btn btn-outline-secondary btn-sm">
            <i class="fas fa-arrow-left me-1"></i> Quay lại Dashboard
        </a>
    </div>

    <div class="table-container shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4">MSSV</th>
                        <th>Họ và Tên</th>
                        <th>Chuyên ngành</th>
                        <th>Lớp</th>
                        <th class="text-center">GPA</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${guidedStudents}">
                        <tr>
                            <td class="ps-4 fw-bold text-primary">${s.mssv}</td>
                            <td>${s.fullName}</td>
                            <td>${s.major}</td>
                            <td>${s.className}</td>
                            <td class="text-center">
                                <c:set var="gpaValue" value="${s.gpa.doubleValue()}" />
                                <span class="badge ${gpaValue >= 3.2 ? 'bg-success' : (gpaValue >= 2.5 ? 'bg-warning text-dark' : 'bg-danger')}">
                                    <fmt:formatNumber value="${s.gpa}" minFractionDigits="2" maxFractionDigits="2"/>
                                </span>
                            </td>
                            <td class="text-center">
                                <div class="btn-group">
                                    <a href="${pageContext.request.contextPath}/lecturer/thesis/history?mssv=${s.mssv}" class="btn btn-sm btn-outline-info" title="Xem tiến độ">
                                        <i class="fas fa-chart-line"></i>
                                    </a>
                                    <button class="btn btn-sm btn-outline-primary" 
                                            title="Gửi thông báo" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#emailModal"
                                            onclick="prepareEmailModal('${s.mssv}', '${s.fullName}', '${s.email}')">
                                        <i class="fas fa-envelope"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-primary" 
                                            title="Xem thông tin sinh viên" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#viewStudentModal"
                                            onclick="prepareViewStudentModal('${s.mssv}', '${s.fullName}', '${s.email}', '${s.className}', '${s.major}', '${s.gpa}', '${s.phone}', '${s.skills}')">
                                        <i class="fas fa-eye"></i>
                                    </button>                                      
                                    <button class="btn btn-sm btn-outline-warning" 
                                        title="Chỉnh sửa GPA" 
                                        data-bs-toggle="modal" 
                                        data-bs-target="#editGpaModal" 
                                        onclick="prepareGpaModal('${s.mssv}', '${s.fullName}', '${s.gpa}')">
                                        <i class="fas fa-edit"></i>
                                    </button>    
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty guidedStudents}">
                        <tr>
                            <td colspan="6" class="text-center py-4 text-muted">Hiện tại chưa có sinh viên nào trong danh sách hướng dẫn chính thức.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <div class="modal fade" id="viewStudentModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg"> <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold"><i class="fas fa-user-graduate me-2"></i>Chi tiết hồ sơ sinh viên</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="row g-4">
                            <div class="col-md-6 border-end">
                                <div class="mb-3">
                                    <label class="text-muted small d-block">Họ và Tên</label>
                                    <span id="m-name" class="fw-bold fs-5 text-primary"></span>
                                </div>
                                <div class="mb-3">
                                    <label class="text-muted small d-block">Mã số sinh viên (MSSV)</label>
                                    <span id="m-mssv" class="fw-bold"></span>
                                </div>
                                <div class="mb-3">
                                    <label class="text-muted small d-block">Lớp chuyên ngành</label>
                                    <span id="m-class" class="fw-bold text-secondary"></span>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label class="text-muted small d-block">Chuyên ngành</label>
                                    <span id="m-major" class="fw-bold text-info"></span>
                                </div>
                                <div class="mb-3">
                                    <label class="text-muted small d-block">Điểm GPA tích lũy</label>
                                    <span id="m-gpa" class="badge bg-success"></span>
                                </div>
                                <div class="mb-3">
                                    <label class="text-muted small d-block">Số điện thoại</label>
                                    <span id="m-phone" class="fw-bold"></span>
                                </div>
                            </div>

                            <hr class="my-2">

                            <div class="col-12">
                                <div class="mb-3">
                                    <label class="text-muted small d-block">Địa chỉ Email</label>
                                    <span id="m-email" class="fw-bold"></span>
                                </div>
                                <div class="mb-1">
                                    <label class="text-muted small d-block">Kỹ năng chuyên môn</label>
                                    <div id="m-skills" class="mt-2"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>        
        <div class="modal fade" id="editGpaModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/lecturer/update-student-gpa" method="POST">
                        <div class="modal-header">
                            <h5 class="modal-title">Cập nhật điểm GPA</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <p>Sinh viên: <strong id="modalStudentName"></strong> (<span id="modalMssvText"></span>)</p>
                            <input type="hidden" name="mssv" id="modalMssv">
                            <div class="mb-3">
                                <label for="gpa" class="form-label">Điểm GPA mới</label>
                                <input type="number" step="0.01" min="0" max="4" class="form-control" name="gpa" id="modalGpa" required>
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
        <div class="modal fade" id="emailModal" tabindex="-1" aria-labelledby="emailModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="emailModalLabel">Gửi tin nhắn đến sinh viên</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="emailForm" action="${pageContext.request.contextPath}/lecturer/send-email" method="POST">
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Thông tin người nhận:</label>
                                <div class="p-3 bg-light border rounded">
                                    <div class="mb-1">
                                        <i class="fas fa-user me-2 text-secondary"></i>
                                        <strong id="emailStudentNameDisplay"></strong> 
                                    </div>
                                    <div class="mb-1">
                                        <i class="fas fa-id-card me-2 text-secondary"></i>
                                        <span>MSSV: </span><span id="emailMssvText" class="fw-bold"></span>
                                    </div>
                                    <div>
                                        <i class="fas fa-envelope me-2 text-secondary"></i>
                                        <span>Email: </span><span id="emailDisplay" class="text-primary"></span>
                                    </div>
                                </div>
                                <input type="hidden" id="recipient-mssv" name="mssv">
                                <input type="hidden" id="recipient-email" name="email">
                            </div>

                            <div class="mb-3">
                                <label for="message-text" class="form-label text-muted small fw-bold">Nội dung thông báo(Khuyến khích bằng tiếng anh):</label>
                                <textarea class="form-control" name ="message-email" id="message-text" rows="4" placeholder="Nhập nội dung gửi đến sinh viên..." required></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" form="emailForm" class="btn btn-primary">
                            <i class="fas fa-paper-plane me-1"></i> Gửi Mail
                        </button>
                    </div>
                </div>
            </div>
        </div>                       
    </div>
</div>
<script>
    function prepareEmailModal(mssv, name, email) {
        document.getElementById('emailStudentNameDisplay').innerText = name;
        document.getElementById('emailMssvText').innerText = mssv;
        document.getElementById('emailDisplay').innerText = email;
        
        document.getElementById('recipient-mssv').value = mssv;
        document.getElementById('recipient-email').value = email;
        
        document.getElementById('message-text').value = '';
    }

    // Giữ nguyên hàm GPA của bạn
    function prepareGpaModal(mssv, name, currentGpa) {
        document.getElementById('modalMssv').value = mssv;
        document.getElementById('modalMssvText').innerText = mssv;
        document.getElementById('modalStudentName').innerText = name;
        document.getElementById('modalGpa').value = currentGpa;
    }
    function prepareViewStudentModal(mssv, name, email, className, major, gpa, phone, skills) {
        document.getElementById('m-mssv').innerText = mssv;
        document.getElementById('m-name').innerText = name;
        document.getElementById('m-email').innerText = email;
        document.getElementById('m-class').innerText = className;
        document.getElementById('m-major').innerText = major;
        document.getElementById('m-gpa').innerText = gpa + " / 4.0";
        document.getElementById('m-phone').innerText = phone || 'Chưa cập nhật';

        // Xử lý hiển thị kỹ năng dạng Badge cho đẹp
        const skillContainer = document.getElementById('m-skills');
        skillContainer.innerHTML = ''; // Reset cũ
        if (skills && skills.trim() !== "") {
            skills.split(',').forEach(skill => {
                let span = document.createElement('span');
                span.className = "badge bg-info text-dark me-1";
                span.innerText = skill.trim();
                skillContainer.appendChild(span);
            });
        } else {
            skillContainer.innerHTML = '<span class="text-muted small">Chưa có dữ liệu</span>';
        }
    }    
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>