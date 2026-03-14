<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Đồ án & AI Check | TMS</title>
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

        /* Sidebar kế thừa từ topic.jsp */
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
        
        /* Card & Table Style */
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

        /* Progress & Badge AI */
        .progress-thin { height: 6px; border-radius: 10px; background-color: #f1f5f9; margin-top: 5px; }
        .badge-ai { padding: 0.35rem 0.6rem; border-radius: 6px; font-size: 0.65rem; font-weight: 700; }
        
        .search-box {
            background: #fff; border: 1px solid #e2e8f0; border-radius: 12px;
            padding: 0.5rem 1rem; display: flex; align-items: center; width: 320px;
        }
        .search-box input { border: none; outline: none; margin-left: 10px; width: 100%; font-size: 0.9rem; }

        .student-info { font-size: 0.85rem; color: var(--text-muted); }
        .thesis-title { font-weight: 600; color: var(--text-main); line-height: 1.4; display: block; margin-bottom: 2px; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand"><i class="bi bi-mortarboard-fill me-2 text-primary"></i> TMS ADMIN</div>
        <nav class="nav flex-column">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i class="bi bi-grid-1x2"></i> Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/list" class="nav-link"><i class="bi bi-people"></i> Quản lý User</a>
            <a href="${pageContext.request.contextPath}/admin/topic" class="nav-link"><i class="bi bi-journal-text"></i> Đề tài</a>
            <a href="${pageContext.request.contextPath}/admin/thesis" class="nav-link active"><i class="bi bi-layers"></i> Đồ án</a>
            <hr class="text-secondary mx-2">
            <a href="${pageContext.request.contextPath}/admin/logout" class="nav-link text-danger"><i class="bi bi-box-arrow-right"></i> Đăng xuất</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">Giám sát Đồ án nộp</h3>
                <p class="text-muted mb-0">Kiểm tra tài liệu, source code và phân tích đạo văn bằng AI.</p>
            </div>
            <form action="${pageContext.request.contextPath}/admin/thesis" method="GET" class="search-box">
                <i class="bi bi-search text-muted"></i>
                <input type="text" name="query" value="${param.query}" placeholder="Tìm mã SV, tên đồ án...">
                <button type="submit" class="d-none"></button>
            </form>
        </div>

        <div class="card-custom">
            <div class="table-responsive">
                <table class="table mb-0">
                    <thead>
                        <tr>
                            <th style="width: 22%">Sinh viên thực hiện</th>
                            <th style="width: 25%">Thông tin Đồ án</th>
                            <th style="width: 18%">Tài liệu nộp</th>
                            <th style="width: 18%">Đạo văn (AI)</th>
                            <th style="width: 17%">Phù hợp (AI)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${listThesis}">
                            <tr>
                                <td>
                                    <div class="fw-bold text-dark">${s.fullName}</div>
                                    <div class="student-info">${s.mssv} • ${s.className}</div>
                                </td>
                                <td>
                                    <span class="thesis-title">${s.title}</span>
                                    <small class="text-primary fw-medium"><i class="bi bi-person-badge me-1"></i>${s.lecturerName}</small>
                                </td>
                                <td>
                                    <div class="d-flex flex-column gap-1">
                                        <c:if test="${not empty s.reportFile}">
                                            <a href="${s.reportFile}" target="_blank" class="btn btn-sm btn-outline-success py-0 px-2" style="font-size: 0.75rem;">
                                                <i class="bi bi-file-earmark-pdf"></i> Báo cáo
                                            </a>
                                        </c:if>
                                        <c:if test="${not empty s.sourceCodeLink}">
                                            <a href="${s.sourceCodeLink}" target="_blank" class="btn btn-sm btn-outline-dark py-0 px-2" style="font-size: 0.75rem;">
                                                <i class="bi bi-github"></i> Source
                                            </a>
                                        </c:if>
                                    </div>
                                </td>
                                <td>
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <span class="small fw-bold">${s.similarityScore}%</span>
                                        <span class="badge-ai ${s.similarityScore > 30 ? 'bg-danger text-white' : (s.similarityScore > 15 ? 'bg-warning text-dark' : 'bg-success text-white')}">
                                            ${s.plagiarismStatus}
                                        </span>
                                    </div>
                                    <div class="progress progress-thin">
                                        <div class="progress-bar ${s.similarityScore > 30 ? 'bg-danger' : (s.similarityScore > 15 ? 'bg-warning' : 'bg-success')}" 
                                             role="progressbar" style="width: ${s.similarityScore}%"></div>
                                    </div>
                                </td>
                                <td>
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <span class="small fw-bold">${s.relevantTopicScore}%</span>
                                        <span class="badge-ai ${s.relevantTopicScore < 50 ? 'bg-danger text-white' : 'bg-info text-white'}">
                                            ${s.relevantTopicStatus}
                                        </span>
                                    </div>
                                    <div class="progress progress-thin">
                                        <div class="progress-bar ${s.relevantTopicScore < 50 ? 'bg-danger' : 'bg-info'}" 
                                             role="progressbar" style="width: ${s.relevantTopicScore}%"></div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty listThesis}">
                            <tr>
                                <td colspan="5" class="text-center py-5 text-muted">
                                    <i class="bi bi-clipboard-x fs-1 d-block mb-2"></i>
                                    Chưa có đồ án nào được nộp lên hệ thống.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <div class="p-3 bg-light border-top d-flex justify-content-between align-items-center">
                <span class="small text-muted">Số lượng đồ án: <strong>${listThesis.size()}</strong></span>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>