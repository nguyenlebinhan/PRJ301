/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import service.AIService;
import com.google.gson.JsonObject;
import dao.AppointmentDAO;
import dao.LecturerDAO;
import dao.StudentDAO;
import dao.ThesisDAO;
import dao.ThesisHistoryDAO;
import dao.TopicDAO;
import dao.TopicRegistrationDAO;
import dao.UserDAO;
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
import model.Appointment;

/**
 *
 * @author ADMIN
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
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
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        // 1. Kiểm tra bảo mật (Auth Check)
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null || !"STUDENT".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // 2. Điều hướng action
        switch (action != null ? action : "") {
            case "/dashboard":
                showDashboard(request, response, user);
                break;         
            case "/logout":
                logoutUser(request, response);
                break;  
            case "/topics":
                showTopicList(request,response);
                break;
            // Trong doGet, sửa case này:
            case "/topic/register":
                showTopicRegistration(request, response);
                break;
            case "/profile":
                showProfile(request,response);
                break;
            case "/thesis/history":
                showThesisHistory(request,response);
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
        
        // 1. Kiểm tra bảo mật (Auth Check)
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        
        if (user == null || !"STUDENT".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // 2. Điều hướng action
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
                handleUpdateProfile(request,response);
                break;
            case "/thesis/update":
                handleThesisUpdate(request,response,user);
                break;
            case "/profile/deactivate":
                handleDeactiveUser(request,response,user);
                break;
            case "/appointment/create":
                createAppointment(request,response,user);
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
        // Lấy thông tin chi tiết để hiển thị lên Dashboard
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

    private void showThesisHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");        
        Student student = studentDAO.getStudentByUserId(user.getId());
        String thesisIdParam = request.getParameter("thesisId");
    
        if (thesisIdParam != null && !thesisIdParam.isEmpty()) {
            int thesisId = Integer.parseInt(thesisIdParam);

            // Gọi DAO lấy danh sách lịch sử
            List<ThesisHistoryResponse> historyList = thesisHistoryDAO.getThesisHistory(student.getMssv(),thesisId);
            ThesisHistoryResponse th = thesisHistoryDAO.getNameAndtopicCode(student.getMssv(), thesisId);

            
            // Import java.time.format.DateTimeFormatter;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
            request.setAttribute("dateFormatter", formatter); 
            request.setAttribute("historyList", historyList);
            request.setAttribute("thesisInfo", th);
            // Forward sang trang JSP mới (không phải Dashboard)
            request.getRequestDispatcher("/jsp/student/thesis-history.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        }
    }    
    private void showTopicList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 1. Lấy thông tin sinh viên hiện tại từ Session
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        Student student = studentDAO.getStudentByUserId(user.getId());

        // 2. Lấy danh sách đề tài còn trống
        List<Topic> topics = topicDao.getAvailableTopics();

        // 3. Lấy danh sách ID các đề tài mà sinh viên này ĐÃ đăng ký
        // Chúng ta sẽ dùng hàm này để kiểm tra nhanh trong vòng lặp
        List<TopicRegistration> myRegistrations = topicRegistrationDao.getRegistrationsByStudent(student.getMssv());

        request.setAttribute("topics", topics);
        request.setAttribute("myRegistrations", myRegistrations);    
        request.getRequestDispatcher("/jsp/student/topic-list.jsp").forward(request, response);        

    }
    private void showProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy thông tin sinh viên hiện tại từ Session
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        Student student = studentDAO.getStudentByUserId(user.getId());

        request.setAttribute("student", student);
        request.getRequestDispatcher("/jsp/student/profile.jsp").forward(request, response);         
    }
    
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String redirectPath = request.getContextPath() + "/student/profile";

        try {
            User user = (User) session.getAttribute("user");
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

            boolean isUpdated = studentDAO.updateProfileStudent(updatedStudent);

            if (isUpdated) {
                session.setAttribute("success", "Cập nhật thành công profile.");
            } else {
                session.setAttribute("error", "Không thể cập nhật profile.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xử lý cập nhật Profile", e);
            session.setAttribute("error", "Hệ thống gặp sự cố kỹ thuật.");
        } finally {
            // Chỉ gọi Redirect duy nhất MỘT LẦN ở đây
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

            // 2. Tạo URL nội bộ để Python có thể truy cập vào file vừa upload
            String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
                             + request.getContextPath() + "/uploads/" + fileName;

            // 3. Gọi AI bằng URL của file vừa nộp
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

            thesisDAO.updateThesis(newThesis);

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

            // 1. Xử lý Upload File
            String topicName = topicDao.getTopicNameById(Integer.parseInt(request.getParameter("topicId")));
            Part filePart = request.getPart("reportFile");
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

            // Đường dẫn lưu file trong thư mục 'uploads' của project
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            filePart.write(uploadPath + File.separator + fileName);

            // 2. Tạo URL nội bộ để Python có thể truy cập vào file vừa upload
            String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
                             + request.getContextPath() + "/uploads/" + fileName;

            // 3. Gọi AI bằng URL của file vừa nộp
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
    private void createAppointment(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        // Log bắt đầu luồng xử lý
        LOGGER.log(Level.INFO, "Bắt đầu yêu cầu tạo lịch hẹn cho User ID: {0}", user.getId());

        try {
            // 1. Lấy thông tin sinh viên
            Student student = studentDAO.getStudentByUserId(user.getId());
            if (student == null) {
                LOGGER.log(Level.WARNING, "Không tìm thấy Student cho User ID: {0}", user.getId());
                throw new Exception("Không tìm thấy thông tin sinh viên.");
            }

            // 2. Lấy giảng viên và đề tài
            Lecturer supervisor = topicRegistrationDao.getLecturerByMssv(student.getMssv());
            TopicThesisDTO topicThesis = topicDao.getAcceptedTopic(student.getMssv());

            if (supervisor == null || topicThesis == null) {
                LOGGER.log(Level.WARNING, "Yêu cầu bị từ chối: MSSV {0} thiếu GVHD hoặc Đề tài được duyệt.", student.getMssv());
                request.setAttribute("error", "Bạn chưa có giảng viên hướng dẫn hoặc đề tài được duyệt!");
                showDashboard(request, response, user);
                return;
            }

            // 3. Nhận dữ liệu từ form
            String purpose = request.getParameter("purpose");
            String location = request.getParameter("location");
            String dateStr = request.getParameter("appointmentDate");
            String timeStr = request.getParameter("appointmentTime");

            // Log dữ liệu đầu vào (không log password hay thông tin nhạy cảm)
            LOGGER.log(Level.INFO, "Dữ liệu nhận được: MSSV={0}, Date={1}, Time={2}, Location={3}", 
                      new Object[]{student.getMssv(), dateStr, timeStr, location});

            
            java.sql.Timestamp meetingTimestamp = null;
            try {
                
                String fullDateTimeStr = dateStr + " " + timeStr + ":00"; 
                meetingTimestamp = java.sql.Timestamp.valueOf(fullDateTimeStr);

                LOGGER.log(Level.INFO, "Converted Timestamp: {0}", meetingTimestamp);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Lỗi định dạng DateTime: " + dateStr + " " + timeStr, e);
                throw new Exception("Định dạng ngày giờ không hợp lệ (Yêu cầu yyyy-MM-dd HH:mm).");
            }

            
            Appointment app = new Appointment();

            app.setMssv(student.getMssv());

            app.setMscv(supervisor.getMscv());


            // KIỂM TRA CHẶN LỖI TẠI ĐÂY
            if (topicThesis == null || topicThesis.getThesisId() == null) {
                LOGGER.log(Level.WARNING, "Không thể tạo lịch hẹn. MSSV {0} chưa có bản ghi trong bảng Theses.", student.getMssv());

                // Gửi thông báo lỗi về cho người dùng thay vì để chương trình bị Crash
                request.getSession().setAttribute("error", "Lỗi: Đồ án của bạn chưa được khởi tạo chính thức trên hệ thống. Vui lòng liên hệ Giảng viên hướng dẫn!");
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
                return;
            }

            // Nếu đã có thesisId hợp lệ, tiến hành set vào Appointment
            app.setThesisId(topicThesis.getThesisId());
            app.setThesisId(topicThesis.getThesisId());

            app.setPurpose(purpose);

            app.setMeetingDate(meetingTimestamp); 

            app.setLocation(location);

            app.setStatus("Pending");

            boolean isSuccess = appointmentDAO.createAppointment(app);

            if (isSuccess) {
                LOGGER.log(Level.INFO, "Tạo lịch hẹn thành công trong Database cho MSSV: {0}", student.getMssv());
                request.getSession().setAttribute("success", "Đặt lịch hẹn thành công!");
                response.sendRedirect(request.getContextPath() + "/student/dashboard?msg=success");
            } else {
                LOGGER.log(Level.SEVERE, "Database trả về false khi lưu Appointment cho MSSV: {0}", student.getMssv());
                request.setAttribute("error", "Không thể lưu lịch hẹn vào cơ sở dữ liệu.");
                showDashboard(request, response, user);
            }

        } catch (Exception e) {
            // Log chi tiết StackTrace để biết chính xác dòng nào lỗi
            LOGGER.log(Level.SEVERE, "Ngoại lệ xảy ra trong createAppointment: " + e.getMessage(), e);
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            showDashboard(request, response, user);
        }
    }
    private void handleRegisterTopic(HttpServletRequest request, HttpServletResponse response, User user) 
        throws IOException {
        try {
            // A. Lấy dữ liệu từ Form
            String topicCode = request.getParameter("topicCode");
            String mscvHD = request.getParameter("mscvHD");
            int topicIdRaw = Integer.parseInt(request.getParameter("topicId"));
                 

            // B. Lấy thông tin sinh viên hiện tại
            Student student = studentDAO.getStudentByUserId(user.getId());

            // C. Kiểm tra xem SV đã đăng ký đề tài này (hoặc đề tài khác) chưa
            if (topicRegistrationDao.hasAlreadyRegistered(student.getMssv(), topicIdRaw)) {
                request.getSession().setAttribute("error", "Bạn đã gửi yêu cầu đăng ký cho đề tài này rồi!");
                response.sendRedirect(request.getContextPath() + "/student/topics");
                return;
            }
            

            // D. Tạo đối tượng đăng ký
            TopicRegistration reg = new TopicRegistration(topicIdRaw,topicCode,"PENDING", student.getMssv(),mscvHD);

            // E. Lưu vào Database
            if (topicRegistrationDao.createRegistration(reg)) {
                // Đăng ký thành công, đẩy thông báo vào session để hiển thị sau khi redirect
                request.getSession().setAttribute("success", "Gửi yêu cầu đăng ký thành công! Đang chờ giảng viên phê duyệt.");
            } else {
                request.getSession().setAttribute("error", "Có lỗi xảy ra trong quá trình đăng ký.");
            }
            

            // Quay về Dashboard để SV theo dõi trạng thái
            response.sendRedirect(request.getContextPath() + "/student/dashboard");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/student/topics?error=system");
        }
        
    }    
// Cập nhật phương thức xử lý hiển thị form
    private void showTopicRegistration(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // 1. Lấy topicId từ URL (ví dụ: /topic/register?topicId=5)
            String idParam = request.getParameter("topicId");
            if (idParam != null) {
                int topicId = Integer.parseInt(idParam); 
                List<Lecturer> lecturers = lecturerDao.getAvailableLecturers();
       
                request.setAttribute("lecturers", lecturers);
                request.setAttribute("selectedTopicId", topicId);
                request.getRequestDispatcher("/jsp/student/topic-registration.jsp").forward(request, response);
                return;
                
            }
            // Nếu không có ID hoặc không tìm thấy, quay về danh sách
            response.sendRedirect(request.getContextPath() + "/student/topics");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/student/topics?error=invalid_id");
        }
    }

}