package controller;

import dao.*;
import dto.AppointmentResponse;
import dto.StudentProgressDTO;
import dto.StudentResponse;
import dto.ThesisHistoryResponse;
import dto.TopicResponseDTO;
import dto.UpdateGpaRequest;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.*;
import service.EmailService;

public class LecturerController extends HttpServlet {
   
    private static final Logger LOGGER = Logger.getLogger(LecturerController.class.getName());
    private final TopicDAO topicDAO = new TopicDAO();
    private final TopicRegistrationDAO registrationDAO = new TopicRegistrationDAO();
    private final ThesisDAO thesisDAO = new ThesisDAO();
    private final LecturerDAO lecturerDao = new LecturerDAO();
    private final StudentDAO studentDao = new StudentDAO();
    private final ThesisHistoryDAO thesisHistoryDAO = new ThesisHistoryDAO();
    private final EmailService emailService = new EmailService();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"LECTURER".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        try {
            switch (action != null ? action : "/") {
                case "/dashboard": 
                    showDashboard(request,response,user);
                    break;
                case "/topics/delete":
                    deleteTopic(request, response);
                    break;
                case "/topics":
                    listMyTopics(request,response,user);
                    break;
                case "/registrations": 
                    listRegistrations(request, response, user);
                    break;
                case "/logout":
                    logoutUser(request, response);
                    break;     
                case "/GuidedStudentList":
                    getGuidedStudent(request,response,user);
                    break; 
                case "/profile":
                    showLecturerProfile(request,response,user);
                    break;
                case "/thesis/history":
                    showStudentThesisHistory(request,response);
                    break;
                case "/appointment":
                    showAppointment(request,response,user);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in LecturerController GET", e);
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getPathInfo();
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"LECTURER".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        try {
            switch (action != null ? action : "") {
                case "/topic/add":    
                    addTopic(request, response);
                    break;
                case "/topics/update":
                    updateTopic(request, response);
                    break;
                case "/topic/approve":
                    acceptTopic(request,response,user);
                    break;
                case "/topic/reject":
                    rejectTopic(request,response,user);
                    break;                    
                case "/logout":
                    logoutUser(request,response);
                    break;        
                case "/update-student-gpa":
                    handleStudentUpdateGPA(request,response);
                    break;
                case "/send-email":
                    handleSendEmail(request,response);
                    break;
                case "/profile/update":
                    handleUpdateProfile(request,response,user);
                    break;
                case "/profile/deactivate":
                    handleDeactiveUser(request,response,user);
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in LecturerController POST", e);
        }
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        // Lấy thông tin chi tiết để hiển thị lên Dashboard
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
        List<TopicResponseDTO> pendingRequests = topicDAO.getPendingRegistrations(lecturer.getMscv());
        int approvedTopicsCount = registrationDAO.getAcceptedTopics(lecturer.getMscv());
        int activeStudentsCount = registrationDAO.studentRegistered(lecturer.getMscv()).size();
        int numberOfReport = thesisDAO.getNumberOfReport(lecturer.getMscv());
        List<StudentProgressDTO> studentProgress = thesisDAO.getStudentProgress(lecturer.getMscv());
        
        request.setAttribute("guidedStudentsProgress", studentProgress);
        request.setAttribute("numberOfReportCount",numberOfReport);
        request.setAttribute("activeStudentsCount",activeStudentsCount );
        request.setAttribute("approvedTopicsCount",approvedTopicsCount );
        request.setAttribute("pendingRequestsCount", pendingRequests.size());
        request.setAttribute("pendingRequests", pendingRequests);
        request.setAttribute("lecturer", lecturer);
        request.getRequestDispatcher("/jsp/lecturer/dashboard.jsp").forward(request, response);
    }
    private void showLecturerProfile(HttpServletRequest request, HttpServletResponse response,User user) throws ServletException, IOException {
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
        request.setAttribute("lecturer", lecturer);
        request.getRequestDispatcher("/jsp/lecturer/profile.jsp").forward(request, response);
    }    

    // --- CÁC HÀM XỬ LÝ NGHIỆP VỤ ---
    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login");    
    } 
    private void listMyTopics(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
        List<Topic> topics = topicDAO.getAllTopics();
        request.setAttribute("lecturer", lecturer);
        request.setAttribute("topics", topics);
        request.getRequestDispatcher("/jsp/lecturer/topic-manage.jsp").forward(request, response);
    }
    private void showStudentThesisHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mssv =request.getParameter("mssv");
            // Gọi DAO lấy danh sách lịch sử
        List<ThesisHistoryResponse> historyList = thesisHistoryDAO.getThesisHistoryByMssv(mssv);
        ThesisHistoryResponse th = thesisHistoryDAO.getNameAndtopicCodeByMssv(mssv);
        Student student = studentDao.getStudentByMssv(mssv);

        // Import java.time.format.DateTimeFormatter;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        request.setAttribute("dateFormatter", formatter); 
        request.setAttribute("historyList", historyList);
        request.setAttribute("student", student);
        request.setAttribute("thesisInfo", th);
        // Forward sang trang JSP mới (không phải Dashboard)
        request.getRequestDispatcher("/jsp/lecturer/thesis-history.jsp").forward(request, response);        
    }    

    private void listRegistrations(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
        List<TopicRegistration> registrations = registrationDAO.getRegistrationsByLecturer(lecturer.getMscv());
        request.setAttribute("registrations", registrations);
        request.getRequestDispatcher("/jsp/lecturer/registration-list.jsp").forward(request, response);
    }
    
    
    public void handleSendEmail(HttpServletRequest request, HttpServletResponse response) throws IOException{
//        try{
//            String email = request.getParameter("email");
//            String message = request.getParameter("message-email");
//            String subject = ;
//            boolean isSuccess = emailService.sendEmail(email, subject, email);
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendRedirect(request.getContextPath() + "/lecturer/GuidedStudentList?msg=system_error");
//            return;
//        }
    }
    private void handleStudentUpdateGPA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String mssv = request.getParameter("mssv");
            double gpa= Double.parseDouble(request.getParameter("gpa"));
            UpdateGpaRequest updateGpaRequest = new UpdateGpaRequest(mssv,gpa);
            boolean updateGPA = studentDao.updateGpaStudent(updateGpaRequest);
            if (updateGPA) {
                response.sendRedirect(request.getContextPath() + "/lecturer/GuidedStudentList?msg=update_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/lecturer/GuidedStudentList?msg=update_error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lecturer/GuidedStudentList?msg=system_error");
            return;
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

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response,User user) throws ServletException, IOException {
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String academicTitle = request.getParameter("academicTitle");
        String researchField = request.getParameter("researchField");
        Lecturer updatedLecturer = new Lecturer(lecturer.getMscv(),fullName,academicTitle,researchField,email);
        boolean isUpdated = lecturerDao.updateLecturer(updatedLecturer);
        if(isUpdated){
            request.getSession().setAttribute("success", "Cập nhật người dùng thành công");
        }else{
            request.getSession().setAttribute("error", "Cập nhật thất bại");
        }
        response.sendRedirect(request.getContextPath()+"/lecturer/profile");
    }
    
    
    private void handleAddTopic(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());

            // 1. Thu thập dữ liệu từ request
            String topicCode = request.getParameter("topicCode");
            if(topicDAO.checkTopicExist(topicCode)){
                request.getSession().setAttribute("error", "Mã đề tài [" + topicCode + "] đã tồn tại trong hệ thống. Vui lòng chọn mã khác.");
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard");
                return;
            }
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String technicalRequirements = request.getParameter("techStack"); // Khớp với name trong Modal trước đó

            // 2. Chuyển đổi BigDecimal an toàn
            String difficultyStr = request.getParameter("difficulty"); // Khớp với name="difficulty" trong Modal
            BigDecimal difficultyScore = (difficultyStr != null) ? new BigDecimal(difficultyStr) : BigDecimal.ZERO;

            String status = request.getParameter("status");
            String type = request.getParameter("type");
            String createdBy = lecturer.getMscv();

            // 3. Đóng gói đối tượng
            Topic requestTopic = new Topic(topicCode, title, description, technicalRequirements, status, type, createdBy, difficultyScore);

            // 4. Gọi DAO lưu vào DB
            boolean isSuccess = topicDAO.createTopic(requestTopic);

            // 5. Điều hướng và ngắt luồng (QUAN TRỌNG)
            if (isSuccess) {
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=add_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=add_error");
            }
            return; // Luôn return sau khi redirect để tránh lỗi IllegalStateException

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=system_error");
            return;
        }
    }
    private void getGuidedStudent(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
        List<StudentResponse> guidedStudents = registrationDAO.studentRegistered(lecturer.getMscv());
        LOGGER.log(Level.INFO, "Lecturer {0} enter his/her guided student list. Number: {1}", 
               new Object[]{lecturer.getMscv(), guidedStudents.size()});
        request.setAttribute("guidedStudents", guidedStudents);
        
        request.getRequestDispatcher("/jsp/lecturer/guided-student-list.jsp").forward(request, response);        
    }    


   
    private void acceptTopic(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            // 1. Lấy thông tin cơ bản
            Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
            String studentId = request.getParameter("studentId");
            int topicId = Integer.parseInt(request.getParameter("topicId"));

            // 2. KIỂM TRA GIỚI HẠN TRƯỚC (Pre-check)
            // Giả sử class Lecturer có thuộc tính currentStudents
            if (lecturer.getCurrentStudents() >= 5) {
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=limit_reached");
                return;
            }

            // 3. Thực hiện chấp nhận đăng ký
            boolean isSuccess = registrationDAO.acceptedTopicRegistration(studentId, lecturer.getMscv(), topicId);

            if (isSuccess) {
                // 4. Cập nhật lại số lượng vào bảng Lecturers
                // Hàm này đã có điều kiện "AND currentStudents < 5" trong SQL nên rất an toàn
                boolean updateCount = lecturerDao.updateNumberOfStudents(lecturer.getMscv());

                if (updateCount) {
                    response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=approved");
                } else {
                    // Trường hợp hy hữu: Bị tranh chấp dữ liệu (Race condition)
                    response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=limit_reached");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=error");
            }
            return;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi duyệt đề tài", e);
            response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=system_error");
        }
    }
    private void rejectTopic(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException,IOException {
        try {
            // 1. Lấy thông tin từ Request và Session
            Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
            String studentId = request.getParameter("studentId");
            int topicId = Integer.parseInt(request.getParameter("topicId"));

            // 2. Gọi DAO để xử lý logic (Nên trả về boolean để kiểm tra thành công/thất bại)
            boolean isSuccess = registrationDAO.rejectTopicRegistration(studentId, lecturer.getMscv(), topicId);

            // 3. Điều hướng người dùng kèm theo thông báo trạng thái
            if (isSuccess) {
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=approved");
            } else {
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=error");
            }
        } catch (Exception e) {
            // Xử lý lỗi parse ID hoặc lỗi hệ thống
            response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=system_error");
        }
    }    
    private void addTopic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        try {
            Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
            // Log thông tin bắt đầu hành động
            LOGGER.log(Level.INFO,"Lecturer MSCV: {1} is attempting to create a new topic.",lecturer.getMscv());
            Topic topic = new Topic();
            topic.setTopicCode(request.getParameter("topicCode"));
            topic.setTitle(request.getParameter("title"));
            topic.setDescription(request.getParameter("description"));
            topic.setTechnicalRequirements(request.getParameter("technicalRequirements"));

            // Chú ý: Cần kiểm tra null/format cho BigDecimal để tránh Crash
            String diffScore = request.getParameter("difficultyScore");
            topic.setDifficultyScore(new java.math.BigDecimal(diffScore));

            topic.setStatus(request.getParameter("status"));
            topic.setType(request.getParameter("type"));
            topic.setCreatedBy(lecturer.getMscv());

            // Log dữ liệu chuẩn bị insert (Dùng debug để tránh làm nặng log file ở môi trường Production)
            LOGGER.log(Level.INFO,"Topic details: Code={1}, Title={2}", new Object[]{topic.getTopicCode(), topic.getTitle()});

            if (topicDAO.createTopic(topic)) {
                LOGGER.log(Level.INFO,"Successfully created topic: {} by Lecturer: {}", new Object[]{topic.getTopicCode(), lecturer.getMscv()});
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?msg=success");
            } else {
                LOGGER.log(Level.WARNING,"Failed to create topic: {} (Database constraint or error)", topic.getTopicCode());
                response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?error=fail");
            }

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING,"Invalid difficulty score format: {}", request.getParameter("difficultyScore"));
            response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?error=invalid_input");
        } catch (Exception e) {
            // Log lỗi hệ thống kèm StackTrace để debug
            LOGGER.log(Level.WARNING,"Unexpected error occurred while adding topic", e);
            response.sendRedirect(request.getContextPath() + "/lecturer/dashboard?error=system_error");
        }
    }

    private void updateTopic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());

        Topic topic = new Topic();
        topic.setTopicId(Integer.parseInt(request.getParameter("topicId")));
        topic.setTitle(request.getParameter("title"));
        topic.setDescription(request.getParameter("description"));
        topic.setTechnicalRequirements(request.getParameter("technicalRequirements"));
        topic.setStatus(request.getParameter("status"));
        topic.setDifficultyScore(new java.math.BigDecimal(request.getParameter("difficultyScore")));
        topic.setCreatedBy(lecturer.getMscv()); // Để verify quyền sở hữu

        if (topicDAO.updateTopic(topic)) {
            response.sendRedirect(request.getContextPath() + "/lecturer/topics?msg=updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/lecturer/topics?error=update_fail");
        }
    }

    private void deleteTopic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int topicId = Integer.parseInt(request.getParameter("id"));

            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");

            // Lấy thông tin giảng viên để đảm bảo họ chỉ ẩn được đề tài của chính mình
            Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());

            if (topicDAO.deleteTopic(topicId, lecturer.getMscv())) {
                response.sendRedirect(request.getContextPath() + "/lecturer/topics?msg=delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/lecturer/topics?error=delete_failed");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/lecturer/topics?error=invalid_id");
        }
    } 

    private void showAppointment(HttpServletRequest request, HttpServletResponse response,User user) throws ServletException, IOException {
        Lecturer lecturer = lecturerDao.getLecturerByUserId(user.getId());
        List<AppointmentResponse> appointments = appointmentDAO.getAppointmentByMscv(lecturer.getMscv());
        request.setAttribute("pendingAppointments", appointments);
        request.getRequestDispatcher("/jsp/lecturer/appointment.jsp").forward(request, response);
    }

}