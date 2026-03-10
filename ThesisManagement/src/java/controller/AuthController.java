/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.LecturerDAO;
import dao.ResetTokenDAO;
import dao.StudentDAO;
import dao.UserDAO;
import dto.RegisterRequest;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.*;
import model.Lecturer;
import model.Student;
import model.User;
import service.EmailService;

/**
 *
 * @author ADMIN
 */
public class AuthController extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final LecturerDAO lecturerDAO =new LecturerDAO();
    private final EmailService emailService =new EmailService();
    private final ResetTokenDAO resetTokenDao = new ResetTokenDAO();
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        LOGGER.log(Level.INFO, "Action received in AuthController (GET): {0}", action);
        switch (action != null ? action : "") {
            case "/register":
                displayRegisterForm(request, response);
                break;
            case "/login":
                displayLoginForm(request, response);
                break;
            case "/forget-password":
                forgotPasswordForm(request, response);
                break;
            case "/reset-password":
                resetPasswordForm(request,response);
                break;
            default:
                // Nếu không có action cụ thể, chuyển hướng về trang đăng nhập
                response.sendRedirect(request.getContextPath() + "/auth/login");
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        LOGGER.log(Level.INFO, "Action received in AuthController (POST): {0}", action);
        request.setCharacterEncoding("UTF-8"); // Đảm bảo nhận tiếng Việt
        response.setCharacterEncoding("UTF-8"); // Đảm bảo gửi tiếng Việt

        switch (action != null ? action : "") {
            case "/register":
                addRegisterUser(request, response);
                break;
            case "/login":
                authenticateUser(request, response);
                break;
            case "/forget-password":
                handleForgetPassword(request, response);
                break;
            case "/reset-password":
                handleResetPassword(request,response);
                break;

            default:
                // Nếu POST đến URL không xác định, có thể chuyển hướng về trang đăng nhập hoặc báo lỗi
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action không hợp lệ");
                break;
        }
    }
    private void displayRegisterForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "GET /register - Displaying registration form");
        request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
    }
    private void displayLoginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "GET /login - Displaying login page");
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // User already logged in, redirect to dashboard
            String role = (String) session.getAttribute("role");
            String username = (String) session.getAttribute("username");
            LOGGER.log(Level.INFO, "User already logged in: {0}, role: {1}, redirecting to dashboard", 
                    new Object[]{username, role});
            response.sendRedirect(getDashboardUrl(role, request));
            return;
        }
        LOGGER.log(Level.FINE, "Forwarding to login.jsp");
        request.getRequestDispatcher("/jsp/auth/login.jsp").forward(request, response);        
    }
    private void forgotPasswordForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị form nhập email
       LOGGER.log(Level.FINE, "Displaying forget password form");
       request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
    }   
    private void resetPasswordForm(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException{
        LOGGER.log(Level.INFO,"Show reset passwordn form");
        String token = request.getParameter("token");
        LOGGER.log(Level.INFO, "Validating reset token: {0}", token != null ? "provided" : "null");
        if (token != null && resetTokenDao.isValidToken(token)) {
            LOGGER.log(Level.INFO, "Token is valid, displaying reset password form");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/jsp/auth/reset-password.jsp").forward(request, response);
        } else {
            LOGGER.log(Level.WARNING, "Invalid or expired token");
            request.setAttribute("error", "Invalid token or token is out-of-time");
            request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
        }
    }
    private void handleForgetPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        LOGGER.log(Level.INFO,"Processing request for email : {0}",email);
        if(email == null || email.isEmpty()){        
            LOGGER.log(Level.WARNING, "Reset request failed: Email is empty");
            request.setAttribute("error", "Please enter an email");
            request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
            return;                
        }
        
        User user = userDAO.getUserByEmail(email);
        if(user == null){
            LOGGER.log(Level.INFO, "User not found for email: {0} (not revealing to user)", email);
            request.setAttribute("success", "If email exists, you can get a link to reset");
            request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
            return;            
        }
        
        LOGGER.log(Level.INFO,"User found with userId: {0} and username : {1}",new Object[]{user.getId(),user.getFullName()});
        
        String token = resetTokenDao.createResetToken(user.getId(), user.getEmail());
        if (token != null) {
            LOGGER.log(Level.INFO, "Reset token created successfully for user id: {0}", user.getId());
            
            // Gửi email với reset link
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String resetUrl = baseUrl + "/auth/reset-password?token=" + token;
            LOGGER.log(Level.INFO, "Sending reset email to: {0}, resetUrl: {1}", new Object[]{user.getEmail(), resetUrl});
            
            emailService.sendResetPasswordEmailAsync(user.getEmail(), resetUrl);
            request.setAttribute("success", "A reset link has been sent to your email for a few second.");       
        } else {
            LOGGER.log(Level.SEVERE, "Failed to create reset token for user id: {0}", user.getId());
            request.setAttribute("error", "Error");
        }
        
        request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
    }    
    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        LOGGER.log(Level.INFO, "Processing password reset - token: {0}", token != null ? "provided" : "null");
        
        if (token == null || !resetTokenDao.isValidToken(token)) {
            LOGGER.log(Level.WARNING, "Password reset failed: Invalid or expired token");
            request.setAttribute("error", "Token không hợp lệ hoặc đã hết hạn");
            request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword == null || newPassword.isEmpty() || newPassword.length() < 6) {
            LOGGER.log(Level.WARNING, "Password reset failed: Password too short or empty");
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/jsp/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            LOGGER.log(Level.WARNING, "Password reset failed: Passwords do not match");
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/jsp/auth/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Lấy userId từ token
        Integer userId = resetTokenDao.getUserIdByToken(token);
        if (userId != null) {
            LOGGER.log(Level.INFO, "Resetting password for user id: {0}", userId);
            
            // Cập nhật mật khẩu
            if (userDAO.updatePassword(userId, newPassword)) {
                // Đánh dấu token đã sử dụng
                resetTokenDao.markTokenAsUsed(token);
                LOGGER.log(Level.INFO, "Password reset successful for user id: {0}", userId);
                
                request.setAttribute("success", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
                request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
                return;
            } else {
                LOGGER.log(Level.SEVERE, "Password reset failed: Could not update password for user id: {0}", userId);
            }
        } else {
            LOGGER.log(Level.WARNING, "Password reset failed: Could not get userId from token");
        }
        
        request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
        request.setAttribute("token", token);
        request.getRequestDispatcher("/jsp/auth/forget-password.jsp").forward(request, response);
            
    }
    private void addRegisterUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "POST /register - Processing registration request");
        LOGGER.log(Level.INFO, "Request method: {0}, Content-Type: {1}", 
                new Object[]{request.getMethod(), request.getContentType()});
        
        // Log all parameters received
        LOGGER.log(Level.INFO, "=== ALL REQUEST PARAMETERS ===");
        request.getParameterMap().forEach((key, values) -> {
            LOGGER.log(Level.INFO, "Parameter: {0} = {1}", new Object[]{key, String.join(", ", values)});
        });
        LOGGER.log(Level.INFO, "=== END PARAMETERS ===");
        
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(request.getParameter("username"));
        registerRequest.setPassword(request.getParameter("password"));
        registerRequest.setConfirmPassword(request.getParameter("confirmPassword"));
        registerRequest.setEmail(request.getParameter("email"));
        registerRequest.setFullName(request.getParameter("fullName"));
        registerRequest.setRole(request.getParameter("role"));
        
        // BUG FIX: Set MSSV and MSCV BEFORE validation
        registerRequest.setMssv(request.getParameter("mssv"));
        registerRequest.setClassName(request.getParameter("className"));
        registerRequest.setMajor(request.getParameter("major"));
        registerRequest.setMscv(request.getParameter("mscv"));
        registerRequest.setAcademicTitle(request.getParameter("academicTitle"));
        registerRequest.setResearchField(request.getParameter("researchField"));
        
        LOGGER.log(Level.INFO, "=== REGISTER REQUEST DATA ===");
        LOGGER.log(Level.INFO, "Username: {0}", registerRequest.getUsername());
        LOGGER.log(Level.INFO, "Email: {0}", registerRequest.getEmail());
        LOGGER.log(Level.INFO, "FullName: {0}", registerRequest.getFullName());
        LOGGER.log(Level.INFO, "Role: {0}", registerRequest.getRole());
        LOGGER.log(Level.INFO, "MSSV: {0}", registerRequest.getMssv());
        LOGGER.log(Level.INFO, "ClassName: {0}", registerRequest.getClassName());
        LOGGER.log(Level.INFO, "Major: {0}", registerRequest.getMajor());
        LOGGER.log(Level.INFO, "MSCV: {0}", registerRequest.getMscv());
        LOGGER.log(Level.INFO, "AcademicTitle: {0}", registerRequest.getAcademicTitle());
        LOGGER.log(Level.INFO, "ResearchField: {0}", registerRequest.getResearchField());
        LOGGER.log(Level.INFO, "=== END REGISTER REQUEST DATA ===");
        
        // Validate input
        LOGGER.log(Level.INFO, "Starting validation...");
        String error = validateRegisterRequest(registerRequest);
        if (error != null) {
            LOGGER.log(Level.WARNING, "Registration validation failed: {0}", error);
            request.setAttribute("error", error);
            request.setAttribute("registerRequest", registerRequest);
            request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
            return;
        }
        LOGGER.log(Level.INFO, "Validation passed successfully");
        
        
        // Create user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword()); // TODO: Hash với BCrypt
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setRole(registerRequest.getRole());
        user.setIsActive(true);
        
        LOGGER.log(Level.INFO, "Creating user account: username={0}, roleId={1}", 
                new Object[]{user.getUsername(), user.getRole()});
        
        if (!userDAO.createUser(user)) {
            LOGGER.log(Level.WARNING, "User creation failed: username or email already exists");
            request.setAttribute("error", "Tên đăng nhập hoặc email đã tồn tại");
            request.setAttribute("registerRequest", registerRequest);
            request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Get created user to get ID
        User createdUser = userDAO.getUserByUsername(registerRequest.getUsername());
        if (createdUser == null) {
            LOGGER.log(Level.SEVERE, "User created but could not retrieve: username={0}", registerRequest.getUsername());
            request.setAttribute("error", "Có lỗi xảy ra khi tạo tài khoản");
            request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
            return;
        }
        
        LOGGER.log(Level.INFO, "User created successfully: id={0}, username={1}", 
                new Object[]{createdUser.getId(), createdUser.getUsername()});
        
        // Create student or lecturer based on role
        if ("STUDENT".equals(registerRequest.getRole())) {
            LOGGER.log(Level.INFO, "Creating student record for userId: {0}", createdUser.getId());
            LOGGER.log(Level.INFO, "Student data - MSSV: {0}, ClassName: {1}, Major: {2}", 
                    new Object[]{registerRequest.getMssv(), registerRequest.getClassName(), registerRequest.getMajor()});
            
            Student student = new Student();
            student.setMssv(registerRequest.getMssv());
            student.setUserId(createdUser.getId());
            student.setFullName(registerRequest.getFullName());
            student.setClassName(registerRequest.getClassName());
            student.setMajor(registerRequest.getMajor());
            student.setEmail(registerRequest.getEmail());
            
            if (!studentDAO.createStudent(student)) {
                LOGGER.log(Level.WARNING, "Student creation failed: MSSV already exists: {0}", registerRequest.getMssv());
                // Rollback: delete user
                // userDAO.deleteUser(createdUser.getId());
                request.setAttribute("error", "MSSV đã tồn tại");
                request.setAttribute("registerRequest", registerRequest);
                request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
                return;
            }
            
            LOGGER.log(Level.INFO, "Student record created successfully: mssv={0}", registerRequest.getMssv());
        } else if ("LECTURER".equals(registerRequest.getRole())) {
            LOGGER.log(Level.INFO, "Creating lecturer record for userId: {0}", createdUser.getId());
            LOGGER.log(Level.INFO, "Lecturer data - MSCV: {0}, AcademicTitle: {1}, ResearchField: {2}", 
                    new Object[]{registerRequest.getMscv(), registerRequest.getAcademicTitle(), registerRequest.getResearchField()});
            
            Lecturer lecturer = new Lecturer();
            lecturer.setMscv(registerRequest.getMscv());
            lecturer.setUserId(createdUser.getId());
            lecturer.setFullName(registerRequest.getFullName());
            lecturer.setAcademicTitle(registerRequest.getAcademicTitle());
            lecturer.setResearchField(registerRequest.getResearchField());
            lecturer.setEmail(registerRequest.getEmail());
            lecturer.setMaxStudents(5);
            
            if (!lecturerDAO.createLecturer(lecturer)) {
                LOGGER.log(Level.WARNING, "Lecturer creation failed: MSCV already exists: {0}", registerRequest.getMscv());
                request.setAttribute("error", "MSCV đã tồn tại");
                request.setAttribute("registerRequest", registerRequest);
                request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
                return;
            }
            
            LOGGER.log(Level.INFO, "Lecturer record created successfully: mscv={0}", registerRequest.getMscv());
        }
        
        // Send welcome email
        LOGGER.log(Level.INFO, "Sending welcome email to: {0}", registerRequest.getEmail());
        emailService.sendWelcomeEmailAsync(registerRequest.getEmail(), registerRequest.getUsername(), registerRequest.getFullName());
        
        // Redirect to login with success message
        LOGGER.log(Level.INFO, "Registration completed successfully for username: {0}", registerRequest.getUsername());
        request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }
    
    
    
    private String validateRegisterRequest(RegisterRequest request) {
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
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            LOGGER.log(Level.WARNING, "Validation failed: Passwords do not match");
            return "Mật khẩu xác nhận không khớp";
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
    private void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOGGER.log(Level.INFO, "POST /login - Processing login request");
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        LOGGER.log(Level.FINE, "Login attempt - username: {0}", username != null ? username : "null");
        
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            LOGGER.log(Level.WARNING, "Login failed: Missing username or password");
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/jsp/auth/login.jsp").forward(request, response);
            return;
        }
        
        User user = userDAO.login(username, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            session.setAttribute("fullName", user.getFullName());
            
            LOGGER.log(Level.INFO, "Login successful - username: {0}, userId: {1}, role: {2}", 
                    new Object[]{username, user.getId(), user.getRole()});
            
            // Redirect to appropriate dashboard
            String role = user.getRole();
            String redirectUrl = getDashboardUrl(role, request);
            LOGGER.log(Level.INFO, "Redirecting to: {0}", redirectUrl);
            response.sendRedirect(redirectUrl);
        } else {
            LOGGER.log(Level.WARNING, "Login failed: Invalid credentials for username: {0}", username);
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            request.getRequestDispatcher("/jsp/auth/login.jsp").forward(request, response);
        }        
    }    

    private String getDashboardUrl(String role, HttpServletRequest request) {
        LOGGER.log(Level.FINE, "Getting dashboard URL for role: {0}", role);
        
        String url;
        switch (role.toUpperCase()) {
            case "STUDENT":
                url = request.getContextPath() + "/student/dashboard";
                break;
            case "LECTURER":
                url = request.getContextPath() + "/lecturer/dashboard";
                break;
            case "ADMIN":
                url = request.getContextPath() + "/admin/dashboard";
                break;
            default:
                LOGGER.log(Level.WARNING, "Unknown role: {0}, redirecting to home", role);
                url = request.getContextPath() + "/";
                break;
        }
        
        LOGGER.log(Level.FINE, "Dashboard URL: {0}", url);
        return url;
    }    


    
}
