<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký - Hệ thống Quản lý Đồ án</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {

                padding: 20px;
            }
            .container {
                max-width: 800px;
                margin: 0 auto;
                background: white;
                padding: 40px;
                border-radius: 10px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            }
            h2 {
                text-align: center;
                color: #333;
                margin-bottom: 30px;
            }
            .form-group {
                margin-bottom: 20px;
            }
            label {
                display: block;
                margin-bottom: 5px;
                color: #555;
                font-weight: 500;
            }
            input[type="text"],
            input[type="email"],
            input[type="password"],
            select,
            textarea {
                width: 100%;
                padding: 12px;
                border: 1px solid #ddd;
                border-radius: 5px;
                font-size: 14px;
            }
            input:focus, select:focus, textarea:focus {
                outline: none;
                border-color: #667eea;
            }
            .row {
                display: flex;
                gap: 15px;
            }
            .row .form-group {
                flex: 1;
            }
            .role-specific {
                display: none;
                margin-top: 20px;
                padding: 20px;
                background: #f5f5f5;
                border-radius: 5px;
            }
            .role-specific.active {
                display: block;
            }
            .btn {
                width: 100%;
                padding: 12px;
                background: #667eea;
                color: white;
                border: none;
                border-radius: 5px;
                font-size: 16px;
                cursor: pointer;
                transition: background 0.3s;
            }
            .btn:hover {
                background: #5568d3;
            }
            .error {
                color: #e74c3c;
                margin-bottom: 15px;
                padding: 10px;
                background: #ffeaea;
                border-radius: 5px;
                text-align: center;
            }
            .links {
                text-align: center;
                margin-top: 20px;
            }
            .links a {
                color: #667eea;
                text-decoration: none;
            }
        </style>
        <script>
            function showRoleFields() {
                var role = document.getElementById('role').value;
                document.getElementById('studentFields').classList.remove('active');
                document.getElementById('lecturerFields').classList.remove('active');
                
                if (role === 'STUDENT') {
                    document.getElementById('studentFields').classList.add('active');
                } else if (role === 'LECTURER') {
                    document.getElementById('lecturerFields').classList.add('active');
                }
            }
        </script>
    </head>
    <body>
        <div class="container">
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="success">${success}</div>
            </c:if>              
            <h2>Đăng ký tài khoản</h2>
            <form method="POST" action="${pageContext.request.contextPath}/auth/register" accept-charset="UTF-8">
                <div class="form-group">
                    <label for="username">Tên đăng nhập *:</label>
                    <input type="text" id="username" name="username" required minlength="3" 
                           value="${registerRequest != null ? registerRequest.username : ''}">
                </div>
                
                <div class="row">
                    <div class="form-group">
                        <label for="password">Mật khẩu *:</label>
                        <input type="password" id="password" name="password" required minlength="6">
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">Xác nhận mật khẩu *:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="email">Email *:</label>
                    <input type="email" id="email" name="email" required 
                           value="${registerRequest != null ? registerRequest.email : ''}">
                </div>
                
                <div class="form-group">
                    <label for="fullName">Họ và tên *:</label>
                    <input type="text" id="fullName" name="fullName" required 
                           value="${registerRequest != null ? registerRequest.fullName : ''}">
                </div>
                
                <div class="form-group">
                    <label for="role">Vai trò *:</label>
                    <select id="role" name="role" required onchange="showRoleFields()">
                        <option value="">-- Chọn vai trò --</option>
                        <option value="STUDENT" ${registerRequest != null && registerRequest.role == 'STUDENT' ? 'selected' : ''}>Sinh viên</option>
                        <option value="LECTURER" ${registerRequest != null && registerRequest.role == 'LECTURER' ? 'selected' : ''}>Giảng viên hướng dẫn</option>
                    </select>
                </div>
                
                <!-- Student specific fields -->
                <div id="studentFields" class="role-specific">
                    <h3>Thông tin sinh viên</h3>
                    <div class="form-group">
                        <label for="mssv">MSSV *:</label>
                        <input type="text" id="mssv" name="mssv" 
                               value="${registerRequest != null ? registerRequest.mssv : ''}">
                    </div>
                    <div class="form-group">
                        <label for="mssv">Số điện thoại :</label>
                        <input type="text" id="phone" name="phone" 
                               value="${registerRequest != null ? registerRequest.phone : ''}">
                    </div>                    
                    <div class="row">
                        <div class="form-group">
                            <label for="className">Lớp:</label>
                            <input type="text" id="className" name="className" 
                                   value="${registerRequest != null ? registerRequest.className : ''}">
                        </div>
                        <div class="form-group">
                            <label for="major">Ngành:</label>
                            <input type="text" id="major" name="major" 
                                   value="${registerRequest != null ? registerRequest.major : ''}">
                        </div>
                    </div>
                </div>
                
                <!-- Lecturer specific fields -->
                <div id="lecturerFields" class="role-specific">
                    <h3>Thông tin giảng viên</h3>
                    <div class="form-group">
                        <label for="mscv">MSCV *:</label>
                        <input type="text" id="mscv" name="mscv" 
                               value="${registerRequest != null ? registerRequest.mscv : ''}">
                    </div>
                    <div class="form-group">
                        <label for="academicTitle">Học hàm/Học vị:</label>
                        <input type="text" id="academicTitle" name="academicTitle" 
                               value="${registerRequest != null ? registerRequest.academicTitle : ''}">
                    </div>
                    <div class="form-group">
                        <label for="researchField">Lĩnh vực nghiên cứu:</label>
                        <textarea id="researchField" name="researchField" rows="3" 
                                  placeholder="Ví dụ: Trí tuệ nhân tạo, Machine Learning, Deep Learning">${registerRequest != null ? registerRequest.researchField : ''}</textarea>
                    </div>
                </div>
                
                <button type="submit" class="btn">Đăng ký</button>
            </form>
            
            <div class="links">
                <a href="${pageContext.request.contextPath}/auth/login">Đã có tài khoản? Đăng nhập</a> | 
                <a href="${pageContext.request.contextPath}/auth/forget-password">Quên mật khẩu?</a>
            </div>
        </div>
        
        <script>
            // Show fields if role is already selected
            showRoleFields();
        </script>
    </body>
</html>

