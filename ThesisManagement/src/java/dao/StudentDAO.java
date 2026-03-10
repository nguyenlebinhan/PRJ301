package dao;

import dal.DBContext;
import dto.StudentProfileRequestDTO;
import dto.UpdateGpaRequest;
import model.Student;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object cho Student
 */
public class StudentDAO {
    private static final Logger LOGGER = Logger.getLogger(StudentDAO.class.getName());
    private final DBContext dbContext;

    public StudentDAO() {
        this.dbContext = new DBContext();
    }

    public Student getStudentByMssv(String mssv) {
        LOGGER.log(Level.INFO, "Getting student by mssv: {0}", mssv);
        
        String sql = "SELECT s.*, u.username, u.email as userEmail "
                + "FROM Students s "
                + "INNER JOIN Users u ON s.userId = u.id "
                + "WHERE s.mssv = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mssv);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student student = mapStudent(rs);
                    LOGGER.log(Level.INFO, "Student found: mssv={0}, name={1}", 
                            new Object[]{mssv, student.getFullName()});
                    return student;
                } else {
                    LOGGER.log(Level.WARNING, "Student not found with mssv: {0}", mssv);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting student by mssv: " + mssv, e);
        }
        return null;
    }
    
    
    public Student getStudentByUserId(int userId) {
        String sql = "SELECT s.*, u.username, u.email as userEmail "
                + "FROM Students s "
                + "INNER JOIN Users u ON s.userId = u.id "
                + "WHERE s.userId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting student by userId: " + userId, e);
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, u.username, u.email as userEmail "
                + "FROM Students s "
                + "INNER JOIN Users u ON s.userId = u.id "
                + "ORDER BY s.mssv";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                students.add(mapStudent(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all students", e);
        }
        return students;
    }

    public boolean createStudent(Student student) {
        LOGGER.log(Level.INFO, "Creating student: mssv={0}, userId={1}, name={2}", 
                new Object[]{student.getMssv(), student.getUserId(), student.getFullName()});
        
        String sql = "INSERT INTO Students (mssv, userId, fullName, className, major, gpa, skills, email, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, student.getMssv());
            ps.setInt(2, student.getUserId());
            ps.setNString(3, student.getFullName());
            ps.setNString(4, student.getClassName());
            ps.setNString(5, student.getMajor());
            ps.setBigDecimal(6, student.getGpa());
            ps.setNString(7, student.getSkills());
            ps.setString(8, student.getEmail());
            ps.setString(9, student.getPhone());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Student created successfully: mssv={0}", student.getMssv());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Student creation failed: no rows affected for mssv={0}", student.getMssv());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating student: mssv=" + student.getMssv(), e);
            return false;
        }
    }
    
    public boolean updateGpaStudent(UpdateGpaRequest request){
        LOGGER.log(Level.INFO, "Updating student's gpa: mssv={0}, gpa={1}", 
                new Object[]{request.getMssv(), request.getGpa()});        
        String sql = "UPDATE Students SET gpa =? "
                + " WHERE mssv = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, request.getGpa());
            ps.setString(2,request.getMssv());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating student's gpa", e);
            return false;
        }                
    }
    
    public boolean updateProfileStudent(StudentProfileRequestDTO student) {
        String sql = "UPDATE Students SET fullName = ?, className = ?, major = ?, "
                + "skills = ?, email = ?, phone = ? WHERE mssv = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, student.getFullName());
            ps.setNString(2, student.getClassName());
            ps.setNString(3, student.getMajor());
            ps.setNString(4, student.getSkills());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhone());
            ps.setString(7, student.getMssv());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating student", e);
            return false;
        }
    }    

    public boolean updateStudent(Student student) {
        String sql = "UPDATE Students SET fullName = ?, className = ?, major = ?, gpa = ?, "
                + "skills = ?, email = ?, phone = ? WHERE mssv = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, student.getFullName());
            ps.setNString(2, student.getClassName());
            ps.setNString(3, student.getMajor());
            ps.setBigDecimal(4, student.getGpa());
            ps.setNString(5, student.getSkills());
            ps.setString(6, student.getEmail());
            ps.setString(7, student.getPhone());
            ps.setString(8, student.getMssv());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating student", e);
            return false;
        }
    }
    
    public boolean updateProfileUser(Student student) {
        String sql = "UPDATE Students SET fullName = ?,className = ?,major = ?,"
                + "email = ?, phone = ? WHERE mssv = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, student.getFullName());
            ps.setNString(2, student.getClassName());
            ps.setNString(3, student.getMajor());
            ps.setBigDecimal(4, student.getGpa());
            ps.setNString(5, student.getSkills());
            ps.setString(6, student.getEmail());
            ps.setString(7, student.getPhone());
            ps.setString(8, student.getMssv());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating student", e);
            return false;
        }
    }    

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setMssv(rs.getString("mssv"));
        student.setUserId(rs.getInt("userId"));
        student.setFullName(rs.getNString("fullName"));
        student.setClassName(rs.getNString("className"));
        student.setMajor(rs.getNString("major"));
        student.setGpa(rs.getBigDecimal("gpa"));
        student.setSkills(rs.getNString("skills"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            student.setCreatedAt(createdAt);
        }
        
        // Set user info if available
        try {
            String username = rs.getString("username");
            if (username != null) {
                User user = new User();
                user.setId(rs.getInt("userId"));
                user.setUsername(username);
                user.setEmail(rs.getString("userEmail"));
                student.setUser(user);
            }
        } catch (SQLException e) {
            // Ignore if columns don't exist
        }
        
        return student;
    }
}

