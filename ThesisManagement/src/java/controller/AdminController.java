package controller;

import dao.LecturerDAO;
import dao.StudentDAO;
import dao.ThesisDAO;
import dao.ThesisHistoryDAO;
import dao.TopicDAO;
import dao.TopicRegistrationDAO;
import dao.UserDAO;
import dto.AddingUserRequest;
import dto.AdminInformationRequest;
import dto.RegisterRequest;

import model.User;
//import model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Lecturer;
import model.Student;
import model.Thesis;
import model.Topic;
import service.EmailService;

public class AdminController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());
    private final UserDAO userDAO = new UserDAO();
    private final TopicDAO topicDAO = new TopicDAO();
    private final TopicRegistrationDAO registrationDAO = new TopicRegistrationDAO();
    private final ThesisDAO thesisDAO = new ThesisDAO();
    private final LecturerDAO lecturerDao = new LecturerDAO();
    private final StudentDAO studentDao = new StudentDAO();
    private final ThesisHistoryDAO thesisHistoryDAO = new ThesisHistoryDAO();    
    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        // 1. Kiểm tra quyền Admin (Security Check)
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || user.getRole() == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            switch (action != null ? action : "/") {
                case "/dashboard":
                    displayAdminDashboard(request,response,user);
                    break;
                case "/list":
                    displayUsers(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/topic":
                    showAdminTopic(request,response);
                    break;
                case "/logout":
                    logoutUser(request,response);
                    break;                    
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in UserController GET", e);
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getPathInfo();

        try {
            switch (action != null ? action : "") {
                case "/update":
                    updateUser(request, response);
                    break;
                case "/logout":
                    logoutUser(request,response);
                    break;
                case "/addUser":
                    addUser(request,response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in UserController POST", e);
        }
    }

    // --- CÁC HÀM XỬ LÝ CHI TIẾT ---

    private void displayAdminDashboard(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        List<Student> student = studentDao.getAllStudents();
        List<Lecturer> lecturers = lecturerDao.getAllLecturers();
        List<Topic> availableTopics = topicDAO.getAvailableTopics();
        List<Thesis> theses = thesisDAO.getAllTheses();
        List<User> listUser = userDAO.getAllNewUsers();
        
        request.setAttribute("listUser", listUser);        
        request.setAttribute("totalStudents", student.size());
        request.setAttribute("totalLecturers",lecturers.size());
        request.setAttribute("availableTopics", availableTopics.size());
        request.setAttribute("activeTheses", theses.size());
        
        request.getRequestDispatcher("/jsp/admin/dashboard.jsp").forward(request, response);
    }
    
   private void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "POST /register - Processing add user request");
        LOGGER.log(Level.INFO, "Request method: {0}, Content-Type: {1}", 
                new Object[]{request.getMethod(), request.getContentType()});
        
        // Log all parameters received
        LOGGER.log(Level.INFO, "=== ALL REQUEST PARAMETERS ===");
        request.getParameterMap().forEach((key, values) -> {
            LOGGER.log(Level.INFO, "Parameter: {0} = {1}", new Object[]{key, String.join(", ", values)});
        });
        LOGGER.log(Level.INFO, "=== END PARAMETERS ===");
        
        AddingUserRequest addingUserRequest = new AddingUserRequest();
        addingUserRequest.setFullName(request.getParameter("fullName"));        
        addingUserRequest.setUsername(request.getParameter("username"));
        addingUserRequest.setEmail(request.getParameter("email"));
        
        addingUserRequest.setPassword(request.getParameter("password"));
        addingUserRequest.setRole(request.getParameter("role"));
        
        // BUG FIX: Set MSSV and MSCV BEFORE validation
        addingUserRequest.setMssv(request.getParameter("mssv"));
        addingUserRequest.setClassName(request.getParameter("className"));
        addingUserRequest.setMajor(request.getParameter("major"));
        addingUserRequest.setMscv(request.getParameter("mscv"));
        addingUserRequest.setAcademicTitle(request.getParameter("academicTitle"));
        addingUserRequest.setResearchField(request.getParameter("researchField"));
        
        LOGGER.log(Level.INFO, "=== REGISTER REQUEST DATA ===");
        LOGGER.log(Level.INFO, "Username: {0}", addingUserRequest.getUsername());
        LOGGER.log(Level.INFO, "Email: {0}", addingUserRequest.getEmail());
        LOGGER.log(Level.INFO, "FullName: {0}", addingUserRequest.getFullName());
        LOGGER.log(Level.INFO, "Role: {0}", addingUserRequest.getRole());
        LOGGER.log(Level.INFO, "MSSV: {0}", addingUserRequest.getMssv());
        LOGGER.log(Level.INFO, "ClassName: {0}", addingUserRequest.getClassName());
        LOGGER.log(Level.INFO, "Major: {0}", addingUserRequest.getMajor());
        LOGGER.log(Level.INFO, "MSCV: {0}", addingUserRequest.getMscv());
        LOGGER.log(Level.INFO, "AcademicTitle: {0}", addingUserRequest.getAcademicTitle());
        LOGGER.log(Level.INFO, "ResearchField: {0}", addingUserRequest.getResearchField());
        LOGGER.log(Level.INFO, "=== END REGISTER REQUEST DATA ===");
        
        // Validate input
        LOGGER.log(Level.INFO, "Starting validation...");
        String error = validateAddingUserRequest(addingUserRequest);
        if (error != null) {
            LOGGER.log(Level.WARNING, "Registration validation failed: {0}", error);
            request.getSession().setAttribute("error", error);
            response.sendRedirect(request.getContextPath() + "/admin/list");
            return;
        }     
        LOGGER.log(Level.INFO, "Validation passed successfully");
        
        
        // Create user
        User user = new User();
        user.setUsername(addingUserRequest.getUsername());
        user.setPassword(addingUserRequest.getPassword());
        user.setEmail(addingUserRequest.getEmail());
        user.setFullName(addingUserRequest.getFullName());
        user.setRole(addingUserRequest.getRole());
        user.setIsActive(true);
        
        LOGGER.log(Level.INFO, "Creating user account: username={0}, roleId={1}", 
                new Object[]{user.getUsername(), user.getRole()});
        
        if (!userDAO.createUser(user)) {
            LOGGER.log(Level.WARNING, "User creation failed: username or email already exists");
            request.getSession().setAttribute("error", "Tên đăng nhập hoặc email đã tồn tại");
            
            response.sendRedirect(request.getContextPath() + "/admin/list");
            return;
        }
        
        // Get created user to get ID
        User createdUser = userDAO.getUserByUsername(addingUserRequest.getUsername());
        if (createdUser == null) {
            LOGGER.log(Level.SEVERE, "User created but could not retrieve: username={0}", addingUserRequest.getUsername());
            request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản");
            request.getRequestDispatcher("/jsp/admin/user-list.jsp").forward(request, response);
            return;
        }
        
        LOGGER.log(Level.INFO, "User created successfully: id={0}, username={1}", 
                new Object[]{createdUser.getId(), createdUser.getUsername()});
        
        // Create student or lecturer based on role
        if ("STUDENT".equals(addingUserRequest.getRole())) {
            LOGGER.log(Level.INFO, "Creating student record for userId: {0}", createdUser.getId());
            LOGGER.log(Level.INFO, "Student data - MSSV: {0}, ClassName: {1}, Major: {2}", 
                    new Object[]{addingUserRequest.getMssv(), addingUserRequest.getClassName(), addingUserRequest.getMajor()});
            
            Student student = new Student();
            student.setMssv(addingUserRequest.getMssv());
            student.setUserId(createdUser.getId());
            student.setFullName(addingUserRequest.getFullName());
            student.setClassName(addingUserRequest.getClassName());
            student.setMajor(addingUserRequest.getMajor());
            student.setEmail(addingUserRequest.getEmail());
            
            if (!studentDao.createStudent(student)) {
                LOGGER.log(Level.WARNING, "Student creation failed: MSSV already exists: {0}", addingUserRequest.getMssv());
                // Rollback: delete user
                // userDAO.deleteUser(createdUser.getId());
                request.getSession().setAttribute("error", "MSSV đã tồn tại");
                
                request.getRequestDispatcher("/jsp/admin/user-list.jsp").forward(request, response);
                return;
            }
            
            LOGGER.log(Level.INFO, "Student record created successfully: mssv={0}", addingUserRequest.getMssv());
        } else if ("LECTURER".equals(addingUserRequest.getRole())) {
            LOGGER.log(Level.INFO, "Creating lecturer record for userId: {0}", createdUser.getId());
            LOGGER.log(Level.INFO, "Lecturer data - MSCV: {0}, AcademicTitle: {1}, ResearchField: {2}", 
                    new Object[]{addingUserRequest.getMscv(), addingUserRequest.getAcademicTitle(), addingUserRequest.getResearchField()});
            
            Lecturer lecturer = new Lecturer();
            lecturer.setMscv(addingUserRequest.getMscv());
            lecturer.setUserId(createdUser.getId());
            lecturer.setFullName(addingUserRequest.getFullName());
            lecturer.setAcademicTitle(addingUserRequest.getAcademicTitle());
            lecturer.setResearchField(addingUserRequest.getResearchField());
            lecturer.setEmail(addingUserRequest.getEmail());
            lecturer.setMaxStudents(5);
            
            if (!lecturerDao.createLecturer(lecturer)) {
                LOGGER.log(Level.WARNING, "Lecturer creation failed: MSCV already exists: {0}", addingUserRequest.getMscv());
                request.getSession().setAttribute("error", "MSCV đã tồn tại");
                
                request.getRequestDispatcher("/jsp/admin/user-list.jsp").forward(request, response);
                return;
            }
            
            LOGGER.log(Level.INFO, "Lecturer record created successfully: mscv={0}", addingUserRequest.getMscv());
        }
        
        // Send welcome email
        LOGGER.log(Level.INFO, "Sending welcome email to: {0}", addingUserRequest.getEmail());
        emailService.sendWelcomeEmailAsync(addingUserRequest.getEmail(), addingUserRequest.getUsername(), addingUserRequest.getFullName());
        
        // Redirect to login with success message
        LOGGER.log(Level.INFO, "Add completed successfully for username: {0}", addingUserRequest.getUsername());
        request.getSession().setAttribute("success", "Thêm thành công!");
        response.sendRedirect(request.getContextPath() + "/admin/list");
    }    
    private void showAdminTopic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        List<Topic> list;

        if (query != null && !query.trim().isEmpty()) {
            list = topicDAO.searchTopics(query.trim());
        } else {
            list = topicDAO.getAllTopics();
        }

        request.setAttribute("listTopic", list);
        request.getRequestDispatcher("/jsp/admin/topic-list.jsp").forward(request, response);

    }    
    
    private void displayUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
      
        // Gọi DAO lấy danh sách (giả sử dùng các giá trị mặc định cho phân trang)
        List<AdminInformationRequest> listUser = userDAO.getAllInformation();
        request.setAttribute("listUser", listUser);
        request.getRequestDispatcher("/jsp/admin/user-list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.getUserById(id);
         // Để hiển thị dropdown chọn quyền
        
        request.setAttribute("user", existingUser);
        request.getRequestDispatcher("/jsp/admin/user-form.jsp").forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");

        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setRole(role);
        
        if (userDAO.updateUser(user)) {
            response.sendRedirect("list?msg=update_success");
        } else {
            response.sendRedirect("edit?id=" + id + "&error=1");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (userDAO.deleteUser(id)) {
            response.sendRedirect("list?msg=delete_success");
        } else {
            response.sendRedirect("list?error=delete_fail");
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session =request.getSession(false);
        if(session!= null){
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath()+"/auth/login");
    }
    private String validateAddingUserRequest(AddingUserRequest request) {
        LOGGER.log(Level.INFO, "=== VALIDATION START ===");
        LOGGER.log(Level.INFO, "Validating username...");
        
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            LOGGER.log(Level.WARNING, "Validation failed: Username is null or empty");
            return "Vui lòng nhập tên đăng nhập";
        }
        LOGGER.log(Level.FINE, "Username check passed: {0}", request.getUsername());
        
        if (request.getUsername().length() < 3) {
            LOGGER.log(Level.WARNING, "Validation failed: Username too short: {0}", request.getUsername().length());
            return "Tên đăng nhập phải có ít nhất 3 ký tự";
        }
        
        LOGGER.log(Level.INFO, "Validating password...");
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            LOGGER.log(Level.WARNING, "Validation failed: Password is null or empty");
            return "Vui lòng nhập mật khẩu";
        }
        if (request.getPassword().length() < 6) {
            LOGGER.log(Level.WARNING, "Validation failed: Password too short: {0}", request.getPassword().length());
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }
        LOGGER.log(Level.FINE, "Password check passed");
        
        LOGGER.log(Level.INFO, "Validating email...");
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            LOGGER.log(Level.WARNING, "Validation failed: Email is null or empty");
            return "Vui lòng nhập email";
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            LOGGER.log(Level.WARNING, "Validation failed: Invalid email format: {0}", request.getEmail());
            return "Email không hợp lệ";
        }
        LOGGER.log(Level.FINE, "Email check passed: {0}", request.getEmail());
        
        LOGGER.log(Level.INFO, "Validating fullName...");
        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            LOGGER.log(Level.WARNING, "Validation failed: FullName is null or empty");
            return "Vui lòng nhập họ tên";
        }
        LOGGER.log(Level.FINE, "FullName check passed: {0}", request.getFullName());
        
        LOGGER.log(Level.INFO, "Validating role...");
        if (request.getRole() == null || request.getRole().isEmpty()) {
            LOGGER.log(Level.WARNING, "Validation failed: Role is null or empty");
            return "Vui lòng chọn vai trò";
        }
        LOGGER.log(Level.INFO, "Role: {0}", request.getRole());
        
        if ("STUDENT".equals(request.getRole())) {
            LOGGER.log(Level.INFO, "Validating STUDENT specific fields...");
            LOGGER.log(Level.INFO, "MSSV value: {0} (null: {1}, empty: {2})", 
                    new Object[]{
                        request.getMssv(), 
                        request.getMssv() == null,
                        request.getMssv() != null && request.getMssv().isEmpty()
                    });
            
            if (request.getMssv() == null || request.getMssv().isEmpty()) {
                LOGGER.log(Level.WARNING, "Validation failed: MSSV is null or empty for STUDENT role");
                return "Vui lòng nhập MSSV";
            }
            LOGGER.log(Level.FINE, "MSSV check passed: {0}", request.getMssv());
        }
        
        if ("LECTURER".equals(request.getRole())) {
            LOGGER.log(Level.INFO, "Validating SUPERVISOR specific fields...");
            LOGGER.log(Level.INFO, "MSCV value: {0} (null: {1}, empty: {2})", 
                    new Object[]{
                        request.getMscv(), 
                        request.getMscv() == null,
                        request.getMscv() != null && request.getMscv().isEmpty()
                    });
            
            if (request.getMscv() == null || request.getMscv().isEmpty()) {
                LOGGER.log(Level.WARNING, "Validation failed: MSCV is null or empty for SUPERVISOR role");
                return "Vui lòng nhập MSCV";
            }
            LOGGER.log(Level.FINE, "MSCV check passed: {0}", request.getMscv());
        }
        
        LOGGER.log(Level.INFO, "=== VALIDATION PASSED ===");
        return null;
    } 

}