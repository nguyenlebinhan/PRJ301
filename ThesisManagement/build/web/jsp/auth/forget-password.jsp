<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quên mật khẩu - Hệ thống Quản lý Đồ án</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
            }
            .container {
                background: white;
                padding: 40px;
                border-radius: 10px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.3);
                width: 100%;
                max-width: 400px;
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
            input[type="email"] {
                width: 100%;
                padding: 12px;
                border: 1px solid #ddd;
                border-radius: 5px;
                font-size: 14px;
            }
            input[type="email"]:focus {
                outline: none;
                border-color: #667eea;
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
            .success {
                color: #27ae60;
                margin-bottom: 15px;
                padding: 10px;
                background: #eafaf1;
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
            .links a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>   
        <div class="container">
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="success">${success}</div>
            </c:if>              
            <h2>Quên mật khẩu</h2>
            
            <form method="POST" action="${pageContext.request.contextPath}/auth/forget-password">
                <div class="form-group">
                    <label for="email">Email đăng ký:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <button type="submit" class="btn">Gửi link đặt lại mật khẩu</button>
            </form>
            <div class="links">
                <a href="${pageContext.request.contextPath}/auth/login">Quay lại đăng nhập</a> | 
                <a href="${pageContext.request.contextPath}/auth/register">Đăng ký tài khoản mới</a>
            </div>
        </div>
    </body>
</html>

