//package servlet;
//
//import dao.ProgressDAO;
//import dao.ThesisDAO;
//import model.Progress;
//import model.User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.List;
//import java.util.logging.Logger;
//
///**
// * Servlet quản lý tiến độ đồ án
// */
//@WebServlet(name = "ProgressServlet", urlPatterns = {"/progress", "/progress/*"})
//public class ProgressServlet extends HttpServlet {
//    private static final Logger LOGGER = Logger.getLogger(ProgressServlet.class.getName());
//    private final ProgressDAO progressDAO;
//    private final ThesisDAO thesisDAO;
//
//    public ProgressServlet() {
//        this.progressDAO = new ProgressDAO();
//        this.thesisDAO = new ThesisDAO();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String pathInfo = request.getPathInfo();
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        String role = (String) session.getAttribute("role");
//        
//        if (pathInfo == null || pathInfo.equals("/")) {
//            // List progress reports
//            String thesisIdParam = request.getParameter("thesisId");
//            if (thesisIdParam != null) {
//                int thesisId = Integer.parseInt(thesisIdParam);
//                List<Progress> progressList = progressDAO.getProgressByThesis(thesisId);
//                request.setAttribute("progressList", progressList);
//                request.setAttribute("thesis", thesisDAO.getThesisById(thesisId));
//            }
//            request.getRequestDispatcher("/WEB-INF/jsp/progress/list.jsp").forward(request, response);
//        } else if (pathInfo.equals("/create")) {
//            // Show create form
//            int thesisId = Integer.parseInt(request.getParameter("thesisId"));
//            request.setAttribute("thesis", thesisDAO.getThesisById(thesisId));
//            request.getRequestDispatcher("/WEB-INF/jsp/progress/create.jsp").forward(request, response);
//        } else if (pathInfo.startsWith("/")) {
//            // View progress
//            String id = pathInfo.substring(1);
//            try {
//                int progressId = Integer.parseInt(id);
//                Progress progress = progressDAO.getProgressById(progressId);
//                if (progress != null) {
//                    request.setAttribute("progress", progress);
//                    request.getRequestDispatcher("/WEB-INF/jsp/progress/view.jsp").forward(request, response);
//                } else {
//                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
//                }
//            } catch (NumberFormatException e) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            }
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String pathInfo = request.getPathInfo();
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        String role = (String) session.getAttribute("role");
//        
//        if (pathInfo == null || pathInfo.equals("/")) {
//            // Create new progress report
//            int thesisId = Integer.parseInt(request.getParameter("thesisId"));
//            int week = Integer.parseInt(request.getParameter("week"));
//            String reportContent = request.getParameter("reportContent");
//            String attachments = request.getParameter("attachments");
//            
//            Progress progress = new Progress();
//            progress.setThesisId(thesisId);
//            progress.setWeek(week);
//            progress.setReportContent(reportContent);
//            progress.setAttachments(attachments);
//            progress.setStatus("PENDING");
//            progress.setAiStatus("NORMAL");
//            
//            // TODO: AI analysis here
//            
//            if (progressDAO.createProgress(progress)) {
//                response.sendRedirect(request.getContextPath() + "/progress?thesisId=" + thesisId);
//            } else {
//                request.setAttribute("error", "Không thể tạo báo cáo tiến độ");
//                request.getRequestDispatcher("/WEB-INF/jsp/progress/create.jsp").forward(request, response);
//            }
//        } else if (pathInfo.equals("/review")) {
//            // Lecturer reviewing progress
//            int progressId = Integer.parseInt(request.getParameter("progressId"));
//            String comment = request.getParameter("comment");
//            String status = request.getParameter("status");
//            
//            Progress progress = progressDAO.getProgressById(progressId);
//            if (progress != null) {
//                progress.setLecturerComment(comment);
//                progress.setStatus(status);
//                if (progressDAO.updateProgress(progress)) {
//                    response.sendRedirect(request.getContextPath() + "/progress?thesisId=" + progress.getThesisId());
//                } else {
//                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                }
//            }
//        }
//    }
//}
//
