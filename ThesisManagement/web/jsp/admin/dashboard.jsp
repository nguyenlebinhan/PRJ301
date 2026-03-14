<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | Thesis Management</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
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
            width: 260px; 
            height: 100vh; 
            position: fixed; 
            background: var(--sidebar-bg); 
            color: white; 
            padding: 1.5rem 1rem;
            z-index: 1000;
        }
        
        .sidebar-brand {
            font-size: 1.25rem;
            font-weight: 700;
            padding: 0 1rem 2rem;
            color: #fff;
            display: flex;
            align-items: center;
        }

        .nav-link { 
            color: #94a3b8; 
            padding: 0.8rem 1rem;
            margin-bottom: 0.3rem;
            border-radius: 10px;
            transition: 0.2s ease;
            display: flex;
            align-items: center;
            font-weight: 500;
        }

        .nav-link i { font-size: 1.2rem; margin-right: 12px; }

        .nav-link:hover { 
            background: rgba(255,255,255,0.05); 
            color: #fff; 
        }

        .nav-link.active { 
            background: var(--accent); 
            color: white; 
            box-shadow: 0 10px 15px -3px rgba(99, 102, 241, 0.3);
        }

        
        .main-content { margin-left: 260px; padding: 2rem; }

        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
        }

        /* Stats Cards */
        .stat-card {
            background: #fff;
            border: none;
            border-radius: 16px;
            padding: 1.5rem;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
            transition: transform 0.2s ease;
        }
        
        .stat-card:hover { transform: translateY(-4px); }
        
        .icon-box {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
        }

        
        .card-custom {
            background: #fff;
            border-radius: 16px;
            border: none;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
        }

        .table thead th {
            background: #f8fafc;
            color: var(--text-muted);
            font-weight: 600;
            font-size: 0.75rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            border-top: none;
            padding: 1rem;
        }

        .table tbody td { padding: 1rem; border-bottom: 1px solid #f1f5f9; }

        .user-avatar {
            width: 38px;
            height: 38px;
            background: #e2e8f0;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            color: var(--accent);
            margin-right: 12px;
        }

        .badge-role {
            padding: 0.4rem 0.8rem;
            border-radius: 8px;
            font-size: 0.75rem;
            font-weight: 600;
        }
    </style>
</head>
<body>
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
    <aside class="sidebar">
        <div class="sidebar-brand">
            <i class="bi bi-mortarboard-fill me-2 text-primary"></i> Thesis Management
        </div>
        <nav class="nav flex-column">
            <a href="#" class="nav-link active"><i class="bi bi-grid-1x2"></i> Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/list" class="nav-link"><i class="bi bi-people"></i> Quản lý User</a>
            <a href="${pageContext.request.contextPath}/admin/topic" class="nav-link"><i class="bi bi-journal-text"></i> Đề tài</a>
            <a href="${pageContext.request.contextPath}/admin/thesis" class="nav-link"><i class="bi bi-layers"></i> Đồ án</a>
            
        </nav>
        <div style="position: absolute; bottom: 20px; width: calc(100% - 2rem);">
            <hr class="text-secondary">
            <a href="${pageContext.request.contextPath}/admin/logout" class="nav-link text-danger"><i class="bi bi-box-arrow-right"></i> Đăng xuất</a>
        </div>
    </aside>

    <main class="main-content">
        <div class="header-section">
            <div>
                <h4 class="fw-bold mb-1">Tổng quan hệ thống</h4>
                <p class="text-muted small">Chào mừng trở lại, ${sessionScope.user.fullName}!</p>
            </div>
            <div class="d-flex align-items-center bg-white p-2 px-3 rounded-pill shadow-sm">
                <div class="user-avatar text-uppercase">${sessionScope.user.fullName.substring(0,1)}</div>
                <span class="fw-semibold small">Administrator</span>
            </div>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <p class="text-muted small fw-medium mb-1">Sinh viên</p>
                            <h3 class="fw-bold mb-0">${totalStudents}</h3>
                        </div>
                        <div class="icon-box" style="background: rgba(99, 102, 241, 0.1); color: var(--accent);">
                            <i class="bi bi-people"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <p class="text-muted small fw-medium mb-1">Giảng viên</p>
                            <h3 class="fw-bold mb-0">${totalLecturers}</h3>
                        </div>
                        <div class="icon-box" style="background: rgba(16, 185, 129, 0.1); color: #10b981;">
                            <i class="bi bi-person-badge"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <p class="text-muted small fw-medium mb-1">Đề tài trống</p>
                            <h3 class="fw-bold mb-0">${availableTopics}</h3>
                        </div>
                        <div class="icon-box" style="background: rgba(245, 158, 11, 0.1); color: #f59e0b;">
                            <i class="bi bi-lightbulb"></i>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <p class="text-muted small fw-medium mb-1">Đang thực hiện</p>
                            <h3 class="fw-bold mb-0">${activeTheses}</h3>
                        </div>
                        <div class="icon-box" style="background: rgba(239, 68, 68, 0.1); color: #ef4444;">
                            <i class="bi bi-check2-circle"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>



        <div class="card-custom overflow-hidden">
            <div class="table-responsive">
                <table class="table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Thành viên</th>
                            <th>Vai trò</th>
                            <th>Ngày tham gia</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${listUser}" end="4">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <div class="user-avatar text-uppercase">${u.username.substring(0,1)}</div>
                                        <div>
                                            <div class="fw-semibold text-dark">${u.fullName}</div>
                                            <div class="text-muted small">${u.email}</div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge-role bg-light text-dark border">${u.role}</span>
                                </td>
                                <td class="text-muted small">${u.createdAt}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</body>
</html>