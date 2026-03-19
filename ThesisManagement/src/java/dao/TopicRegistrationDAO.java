package dao;

import dal.DBContext;
import dto.StudentResponse;
import java.math.BigDecimal;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object cho TopicRegistration
 */
public class TopicRegistrationDAO {
    private static final Logger LOGGER = Logger.getLogger(TopicRegistrationDAO.class.getName());
    private final DBContext dbContext;
    private final TopicDAO topicDAO;
    private final StudentDAO studentDAO;
    private final LecturerDAO lecturerDAO;

    public TopicRegistrationDAO() {
        this.dbContext = new DBContext();
        this.topicDAO = new TopicDAO();
        this.studentDAO = new StudentDAO();
        this.lecturerDAO = new LecturerDAO();
    }

    public TopicRegistration getRegistrationById(int registrationId) {
        String sql = "SELECT * FROM TopicRegistrations WHERE registrationId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, registrationId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRegistration(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting registration by id: " + registrationId, e);
        }
        return null;
    }
    
    public Lecturer getLecturerByMssv(String mssv) {
        Lecturer lecturer = null; 
        String sql = "SELECT l.* FROM TopicRegistrations tr " +
                     "INNER JOIN Lecturers l ON l.mscv = tr.mscvHD " +
                     "WHERE tr.mssv = ? AND tr.status = 'ACCEPTED'";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // QUAN TRỌNG: Phải tạo đối tượng mới trước khi set dữ liệu
                    lecturer = new Lecturer(); 

                    lecturer.setMscv(rs.getString("mscv"));
                    lecturer.setUserId(rs.getInt("userId"));
                    lecturer.setFullName(rs.getNString("fullName"));
                    lecturer.setAcademicTitle(rs.getNString("academicTitle"));
                    lecturer.setResearchField(rs.getNString("researchField"));
                    lecturer.setMaxStudents(rs.getInt("maxStudents"));
                    lecturer.setCurrentStudents(rs.getInt("currentStudents"));
                    lecturer.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting Lecturer by mssv: " + mssv, e);
        }
        return lecturer; 
    }
    
    public List<StudentResponse> studentRegistered (String mscv){
        List<StudentResponse> studentList = new ArrayList<>();
        String sql = "SELECT DISTINCT tr.mssv, s.fullName, s.gpa, s.major, s.className, s.email,s.phone,s.skills FROM TopicRegistrations tr INNER JOIN Students s ON s.mssv = tr.mssv WHERE tr.mscvHD = ? AND tr.status = 'ACCEPTED'";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mscv);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String mssv = rs.getString("mssv");
                    String fullName = rs.getString("fullName");
                    BigDecimal gpa = rs.getBigDecimal("gpa");
                    String major =rs.getString("major");
                    String className = rs.getString("className");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String skills = rs.getString("skills");
                    studentList.add(new StudentResponse(mssv,fullName,email,gpa,phone,major,className,skills));
                    
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting list Student by mcv: " + mscv, e);
        }
        return studentList;        
    }
    
    public List<TopicRegistration> getRegistrationsByTopic(int topicId) {
        List<TopicRegistration> registrations = new ArrayList<>();
        String sql = "SELECT * FROM TopicRegistrations WHERE topicId = ? ORDER BY priority DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, topicId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registrations.add(mapRegistration(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting registrations by topic: " + topicId, e);
        }
        return registrations;
    }
    
    public int getAcceptedTopics(String mscv) {
       
        String sql = "SELECT COUNT(*) as NumberOfAcceptedTopics  FROM TopicRegistrations WHERE mscvHD = ? AND status = 'ACCEPTED'";
        int count = 0;
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mscv);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                   count = rs.getInt("NumberOfAcceptedTopics");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting accepted registrations for mscv: " + mscv, e);
        }
        return count;
    }
    
    
    public boolean acceptedTopicRegistration(String mssv,String mscv, int topicId) {
        String sql = "UPDATE TopicRegistrations SET status = 'ACCEPTED' " +
                     "WHERE mssv = ? AND topicId = ? AND mscvHD = ? AND status = 'PENDING'";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);
            ps.setInt(2, topicId);
            ps.setString(3,mscv);

            int rowsAffected = ps.executeUpdate();

            // Nếu rowsAffected > 0 tức là đã update thành công
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi duyệt đăng ký đề tài cho SV: " + mssv, e);
            return false;
        }
    }
    public boolean rejectTopicRegistration(String mssv,String mscv, int topicId) {
        // Câu lệnh SQL cập nhật trạng thái từ 'PENDING' sang 'ACCEPTED'
        String sql = "UPDATE TopicRegistrations SET status = 'REJECTED' " +
                     "WHERE mssv = ? AND topicId = ? AND mscvHD = ? AND status = 'PENDING'";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);
            ps.setInt(2, topicId);
            ps.setString(3,mscv);

            int rowsAffected = ps.executeUpdate();

            // Nếu rowsAffected > 0 tức là đã update thành công
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi duyệt từ chối đề tài cho SV: " + mssv, e);
            return false;
        }
    }      

    public List<TopicRegistration> getRegistrationsByStudent(String mssv) {
        List<TopicRegistration> registrations = new ArrayList<>();
        String sql = "SELECT * FROM TopicRegistrations WHERE mssv = ? ORDER BY registeredAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mssv);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registrations.add(mapRegistration(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting registrations by student: " + mssv, e);
        }
        return registrations;
    }

    

    public boolean createRegistration(TopicRegistration registration) {
        String sql = "INSERT INTO TopicRegistrations (topicId,topicCode,status, mssv, mscvHD) "
                + "VALUES (?, ?,?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, registration.getTopicId());
            ps.setString(2, registration.getTopicCode());
            ps.setString(3, registration.getStatus());
            ps.setString(4, registration.getMssv());
            ps.setString(5, registration.getMscvHD());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating registration", e);
            return false;
        }
    }

    public boolean updateRegistration(TopicRegistration registration) {
        String sql = "UPDATE TopicRegistrations SET status, processedAt = GETDATE() "
                + "WHERE registrationId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, registration.getStatus());
            ps.setInt(2,registration.getRegistrationId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating registration", e);
            return false;
        }
    }

    private TopicRegistration mapRegistration(ResultSet rs) throws SQLException {
        TopicRegistration registration = new TopicRegistration();
        registration.setRegistrationId(rs.getInt("registrationId"));
        registration.setTopicId(rs.getInt("topicId"));
        registration.setTopicCode(rs.getString("topicCode"));
        registration.setStatus(rs.getString("status"));
        registration.setMssv(rs.getString("mssv"));
        registration.setMscvHD(rs.getString("mscvHD"));
        
        Timestamp registeredAt = rs.getTimestamp("registeredAt");
        if (registeredAt != null) {
            registration.setRegisteredAt(registeredAt);
        }
        
        Timestamp processedAt = rs.getTimestamp("processedAt");
        if (processedAt != null) {
            registration.setProcessedAt(processedAt);
        }
        
        // Load related entities
        registration.setTopic(topicDAO.getTopicById(registration.getTopicId()));
        registration.setStudent(studentDAO.getStudentByMssv(registration.getMssv()));
        if (registration.getMscvHD() != null) {
            registration.setSupervisor(lecturerDAO.getLecturerByMscv(registration.getMscvHD()));
        }
        
        return registration;
    }
    /**
     * Kiểm tra xem sinh viên đã đăng ký đề tài này chưa (tránh đăng ký trùng).
     * Chỉ tính các bản ghi có trạng thái PENDING hoặc APPROVED.
     */
    public boolean hasAlreadyRegistered(String mssv, int topicId) {
        String sql = "SELECT COUNT(*) FROM TopicRegistrations "
                   + "WHERE mssv = ? AND topicId = ? AND status != 'REJECTED'";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);
            ps.setInt(2, topicId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra trạng thái đăng ký của SV: " + mssv, e);
        }
        return false;
    }    

    /**
     * Lấy danh sách tất cả sinh viên đăng ký đề tài do giảng viên này hướng dẫn
     */
    public List<TopicRegistration> getRegistrationsByLecturer(String mscv) {
        List<TopicRegistration> registrations = new ArrayList<>();
        // Sắp xếp theo thứ tự: Ưu tiên cao trước, sau đó đến thời gian đăng ký mới nhất
        String sql = "SELECT * FROM TopicRegistrations WHERE mscvHD = ? "
                   + "ORDER BY priority DESC, registeredAt DESC";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mscv);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tái sử dụng hàm mapRegistration mà bạn đã viết sẵn trong DAO
                    registrations.add(mapRegistration(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đăng ký của giảng viên: " + mscv, e);
        }
        return registrations;
    }    
}

