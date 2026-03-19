/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.google.gson.JsonElement;
import service.AIService;
import com.google.gson.JsonObject;
import dao.LecturerDAO;
import dao.StudentDAO;
import dao.ThesisDAO;
import dao.ThesisHistoryDAO;
import dao.TopicDAO;
import dao.TopicRegistrationDAO;
import dao.UserDAO;
import dto.ImprovementRequest;
import dto.StudentProfileRequestDTO;
import dto.ThesisHistoryResponse;
import dto.ThesisUpdateRequest;
import dto.TopicThesisDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.Lecturer;
import model.Student;
import model.Thesis;
import model.Topic;
import model.TopicRegistration;
import model.User;
import java.util.logging.*;


/**
 *
 * @author ADMIN
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, 
    maxFileSize = 1024 * 1024 * 10,      
    maxRequestSize = 1024 * 1024 * 50    
)

public class StudentController extends HttpServlet {
    private final Logger LOGGER =Logger.getLogger(StudentController.class.getName());
    private final UserDAO userDAO = new UserDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final LecturerDAO lecturerDao = new LecturerDAO();
    private final ThesisDAO thesisDAO = new ThesisDAO();
    private final TopicDAO topicDao = new TopicDAO();
    private final TopicRegistrationDAO topicRegistrationDao = new TopicRegistrationDAO();
    private final ThesisHistoryDAO thesisHistoryDAO = new ThesisHistoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || !"STUDENT".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        
        switch (action != null ? action : "") {
            case "/dashboard":
                showDashboard(request, response, user);
                break;         
            case "/logout":
                logoutUser(request, response);
                break;  
            case "/topics":
                showTopicList(request,response,user);
                break;
            
            case "/topic/register":
                showTopicRegistration(request, response);
                break;
            case "/profile":
                showProfile(request,response,user);
                break;
            case "/thesis/history":
                showThesisHistory(request,response,user);
                break;
            default:
                response.sendError(404);
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null || !"STUDENT".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        
        switch (action != null ? action : "") {
            case "/dashboard":
                showDashboard(request, response, user);
                break;
            case "/logout":
                logoutUser(request, response);
                break;    
            case "/topic/register":
                handleRegisterTopic(request, response, user);
                break;
            case "/thesis/add":
                handleAddThesis(request,response,user);
                break;
            case "/profile/update":
                handleUpdateProfile(request,response,user);
                break;
            case "/thesis/update":
                handleThesisUpdate(request,response,user);
                break;
            case "/profile/deactivate":
                handleDeactiveUser(request,response,user);
                break;
            case "/thesis/ai-advice":
                handleImprovementThesis(request,response,user);
                break;
            default:
                //response.sendError(404);
                break;
        }
    }    

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login");    
    }    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        
        Student student = studentDAO.getStudentByUserId(user.getId());
        //List<Topic> topics = topicDao.
        TopicThesisDTO topicThesis = topicDao.getAcceptedTopic(student.getMssv());
        Lecturer supervisor = topicRegistrationDao.getLecturerByMssv(student.getMssv());
        String supervisorName = null;

        if (supervisor != null) {
            supervisorName = supervisor.getFullName();
        }
        request.setAttribute("supervisorName", supervisorName);
        request.setAttribute("student", student);
        request.setAttribute("acceptedTopics", topicThesis);
        request.getRequestDispatcher("/jsp/student/dashboard.jsp").forward(request, response);
    }

    private void showThesisHistory(HttpServletRequest request, HttpServletResponse response,User user) throws ServletException, IOException {      
        Student student = studentDAO.getStudentByUserId(user.getId());
        String thesisIdParam = request.getParameter("thesisId");
    
        if (thesisIdParam != null && !thesisIdParam.isEmpty()) {
            int thesisId = Integer.parseInt(thesisIdParam);
            List<ThesisHistoryResponse> historyList = thesisHistoryDAO.getThesisHistory(student.getMssv(),thesisId);
            ThesisHistoryResponse th = thesisHistoryDAO.getNameAndtopicCode(student.getMssv(), thesisId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
            request.setAttribute("dateFormatter", formatter); 
            request.setAttribute("historyList", historyList);
            request.setAttribute("thesisInfo", th);
            request.getRequestDispatcher("/jsp/student/thesis-history.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        }
    }    
    private void showTopicList(HttpServletRequest request, HttpServletResponse response,User user) 
            throws ServletException, IOException {
        Student student = studentDAO.getStudentByUserId(user.getId());
        List<TopicRegistration> myRegistrations = topicRegistrationDao.getRegistrationsByStudent(student.getMssv());
        String query = request.getParameter("query");
        List<Topic> list;

        if (query != null && !query.trim().isEmpty()) {
            list = topicDao.searchTopics(query.trim());
        } else {
            list = topicDao.getAllTopics();
        }
        request.setAttribute("topics", list);
        request.setAttribute("myRegistrations", myRegistrations);    
        request.getRequestDispatcher("/jsp/student/topic-list.jsp").forward(request, response);        

    }
    private void showProfile(HttpServletRequest request, HttpServletResponse response,User user) throws ServletException, IOException {

        Student student = studentDAO.getStudentByUserId(user.getId());

        request.setAttribute("student", student);
        request.getRequestDispatcher("/jsp/student/profile.jsp").forward(request, response);         
    }

  
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response,User user) throws IOException {
        String redirectPath = request.getContextPath() + "/student/profile";

        try {
            Student student = studentDAO.getStudentByUserId(user.getId());

            // Lấy dữ liệu và thực hiện logic
            String fullName = request.getParameter("fullName");
            String className = request.getParameter("className");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String skills = request.getParameter("skills");
            String major = request.getParameter("major");

            StudentProfileRequestDTO updatedStudent = new StudentProfileRequestDTO(
                student.getMssv(), fullName, className, major, skills, email, phone
            );

            boolean isUpdated = studentDAO.updateFullProfile(updatedStudent);

            if (isUpdated) {
                request.getSession().setAttribute("success", "Cập nhật thành công profile.");
            } else {
                request.getSession().setAttribute("error", "Không thể cập nhật profile.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xử lý cập nhật Profile", e);
            request.getSession().setAttribute("error", "Hệ thống gặp sự cố kỹ thuật.");
        } finally {
            if (!response.isCommitted()) {
                response.sendRedirect(redirectPath);
            }
        }
    }    
    private void handleThesisUpdate(HttpServletRequest request, HttpServletResponse response,User user) throws IOException {          
        try {
            Student student = studentDAO.getStudentByUserId(user.getId());
            Lecturer supervisor = topicRegistrationDao.getLecturerByMssv(student.getMssv());
            String topicName = topicDao.getTopicNameById(Integer.parseInt(request.getParameter("topicId")));
            // 1. Xử lý Upload File
            int thesisId = Integer.parseInt(request.getParameter("thesisId"));
            Part filePart = request.getPart("reportFile");
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

            // Đường dẫn lưu file trong thư mục 'uploads' của project
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            filePart.write(uploadPath + File.separator + fileName);

            String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
                             + request.getContextPath() + "/uploads/" + fileName;
            
            AIService aiService = new AIService();
            JsonObject plagiarismResult = aiService.checkPlagiarismAuto(fileUrl);
            JsonObject relevanceResult = aiService.checkTopicRelevant(fileUrl,topicName );
            double similarityScore = plagiarismResult.get("similarity_score").getAsDouble();
            String plagiarismStatus = plagiarismResult.get("plagiarism_status").getAsString();
            String bestSource = plagiarismResult.get("best_source").getAsString();
            String plagiarismAnalysis = plagiarismResult.get("plagiarism_analysis").getAsString();
            double relevantTopicScore = relevanceResult.get("relevance_score").getAsDouble();
            String relevanceStatus = relevanceResult.get("relevance_status").getAsString();
            String relevanceAnalysis = relevanceResult.get("relevance_analysis").getAsString();
            // 4. Lưu vào Database (Lưu fileUrl vào cột reportFile)
            ThesisUpdateRequest newThesis = new ThesisUpdateRequest(
                thesisId,fileUrl, request.getParameter("sourceCodeLink"),similarityScore,plagiarismStatus,bestSource,plagiarismAnalysis,relevantTopicScore,relevanceStatus,relevanceAnalysis
            );

            boolean isUpdatedSucessfully = thesisDAO.updateThesis(newThesis);
            if(isUpdatedSucessfully){
                thesisHistoryDAO.addToHistory(new Thesis(
                thesisId, 
                student.getMssv(), 
                fileUrl, 
                request.getParameter("sourceCodeLink"), 
                similarityScore, 
                plagiarismStatus, 
                bestSource, 
                plagiarismAnalysis, 
                relevantTopicScore, 
                relevanceStatus, 
                relevanceAnalysis
                ));
            }

            request.getSession().setAttribute("success", "Upload và kiểm tra AI thành công!");
            response.sendRedirect(request.getContextPath() + "/student/dashboard");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi upload file", e);
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=upload");
        }     
    }
    private void handleDeactiveUser(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            boolean isDeactivated = userDAO.deactivateUser(user.getId());
            if(isDeactivated){
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate(); 
                }
                request.getSession(true).setAttribute("success", "Tài khoản của bạn đã được vô hiệu hóa thành công.");

                response.sendRedirect(request.getContextPath() + "/auth/login");                
            } else {
                request.getSession().setAttribute("error", "Không thể vô hiệu hóa tài khoản lúc này.");
                response.sendRedirect(request.getHeader("Referer"));
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi vô hiệu hóa user id: " + user.getId(), e);
            response.sendRedirect(request.getContextPath() + "/auth/login?error=system_error");
        }          
    }    
    private void handleAddThesis(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        try {
            Student student = studentDAO.getStudentByUserId(user.getId());
            Lecturer supervisor = topicRegistrationDao.getLecturerByMssv(student.getMssv());
            String topicName = topicDao.getTopicNameById(Integer.parseInt(request.getParameter("topicId")));
            
            
            Part filePart = request.getPart("reportFile");
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            filePart.write(uploadPath + File.separator + fileName); 
            String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
                             + request.getContextPath() + "/uploads/" + fileName;
            
            
            AIService aiService = new AIService();
            JsonObject plagiarismResult = aiService.checkPlagiarismAuto(fileUrl);
            JsonObject relevanceResult = aiService.checkTopicRelevant(fileUrl,topicName );
            double similarityScore = plagiarismResult.get("similarity_score").getAsDouble();
            String plagiarismStatus = plagiarismResult.get("plagiarism_status").getAsString();
            String bestSource = plagiarismResult.get("best_source").getAsString();
            String plagiarismAnalysis = plagiarismResult.get("plagiarism_analysis").getAsString();
            double relevantTopicScore = relevanceResult.get("relevance_score").getAsDouble();
            String relevanceStatus = relevanceResult.get("relevance_status").getAsString();
            String relevanceAnalysis = relevanceResult.get("relevance_analysis").getAsString();
            Thesis newThesis = new Thesis(
                Integer.parseInt(request.getParameter("topicId")),
                student.getMssv(), supervisor.getMscv(),
                fileUrl, request.getParameter("sourceCodeLink"), "IN_PROGRESS"
            );
            newThesis.setSimilarityScore(similarityScore);
            newThesis.setPlagiarismStatus(plagiarismStatus);
            newThesis.setBestSource(bestSource);
            newThesis.setPlagiarismAnalysis(plagiarismAnalysis);
            newThesis.setRelevantTopicScore(relevantTopicScore);
            newThesis.setRelevantTopicStatus(relevanceStatus);
            newThesis.setRelevanceAnalysis(relevanceAnalysis);
            thesisDAO.createThesis(newThesis);
            request.getSession().setAttribute("success", "Upload và kiểm tra AI thành công!");
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi upload file", e);
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=upload");
        }
    }
    private void handleImprovementThesis(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            String reportFile = request.getParameter("reportFile");
            String userPrompt = request.getParameter("userPrompt");
            int thesisId = Integer.parseInt(request.getParameter("thesisId"));
            AIService aiService = new AIService();
            
            JsonObject improvementResult = aiService.suggestImprovements(reportFile,userPrompt);
            String focusAnalysis = improvementResult.get("focus_analysis").getAsString();
            String generalObservation = improvementResult.get("general_observations").getAsString();
            JsonElement topPriorElement = improvementResult.get("top_3_priorities");
            String topPrior;
            if (topPriorElement.isJsonArray()) {
                StringBuilder sb = new StringBuilder();
                int count = 1;
                for (JsonElement e : topPriorElement.getAsJsonArray()) {
                    sb.append(count++).append(". ").append(e.getAsString()).append("; ");
                }
                topPrior = sb.toString();
            } else {
                topPrior = topPriorElement.getAsString();
            }
            ImprovementRequest Iprequest = new ImprovementRequest(focusAnalysis,generalObservation,topPrior,userPrompt,thesisId);
            boolean isUpdated = thesisDAO.updateAnalysis(Iprequest);
            if(isUpdated){
                thesisHistoryDAO.updateImprovementInThesisHistory(Iprequest);
            }
            request.getSession().setAttribute("success", "AI cải tiến thành công!");
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi upload file", e);
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=upload");
        }        
    }    
    private void handleRegisterTopic(HttpServletRequest request, HttpServletResponse response, User user) 
        throws IOException {
        try {
            
            String topicCode = request.getParameter("topicCode");
            String mscvHD = request.getParameter("mscvHD");
            int topicIdRaw = Integer.parseInt(request.getParameter("topicId"));  
            Student student = studentDAO.getStudentByUserId(user.getId());
            if (topicRegistrationDao.hasAlreadyRegistered(student.getMssv(), topicIdRaw)) {
                request.getSession().setAttribute("error", "Bạn đã gửi yêu cầu đăng ký cho đề tài này rồi!");
                response.sendRedirect(request.getContextPath() + "/student/topics");
                return;
            }
            TopicRegistration reg = new TopicRegistration(topicIdRaw,topicCode,"PENDING", student.getMssv(),mscvHD);
            if (topicRegistrationDao.createRegistration(reg)) {     
                request.getSession().setAttribute("success", "Gửi yêu cầu đăng ký thành công! Đang chờ giảng viên phê duyệt.");
            } else {
                request.getSession().setAttribute("error", "Có lỗi xảy ra trong quá trình đăng ký.");
            }
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/student/topics?error=system");
        }
        
    }    
    private void showTopicRegistration(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {            
            String idParam = request.getParameter("topicId");
            if (idParam != null) {
                int topicId = Integer.parseInt(idParam); 
                List<Lecturer> lecturers = lecturerDao.getAvailableLecturers();
                request.setAttribute("lecturers", lecturers);
                request.setAttribute("selectedTopicId", topicId);
                request.getRequestDispatcher("/jsp/student/topic-registration.jsp").forward(request, response);
                return;
                
            }
            response.sendRedirect(request.getContextPath() + "/student/topics");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/student/topics?error=invalid_id");
        }
    }


}