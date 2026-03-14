package dao;

import dal.DBContext;
import dto.StudentProfileRequestDTO;
import model.Lecturer;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LecturerDAO {
    private static final Logger LOGGER = Logger.getLogger(LecturerDAO.class.getName());
    private final DBContext dbContext;
    public LecturerDAO() {
        this.dbContext = new DBContext();
    }

    public Lecturer getLecturerByMscv(String mscv) {
        String sql = "SELECT l.*, u.username, u.email as userEmail "
                + "FROM Lecturers l "
                + "INNER JOIN Users u ON l.userId = u.id "
                + "WHERE l.mscv = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mscv);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapLecturer(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting lecturer by mscv: " + mscv, e);
        }
        return null;
    }

    public Lecturer getLecturerByUserId(int userId) {
        String sql = "SELECT l.*, u.username, u.email as userEmail "
                + "FROM Lecturers l "
                + "INNER JOIN Users u ON l.userId = u.id "
                + "WHERE l.userId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapLecturer(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting lecturer by userId: " + userId, e);
        }
        return null;
    }
    public boolean updateNumberOfStudents(String mscv) {
        String SQL = "UPDATE Lecturers " +
                     "SET currentStudents = (SELECT COUNT(*) FROM TopicRegistrations WHERE mscvHD = ? AND status = 'ACCEPTED') " +
                     "WHERE mscv = ? AND currentStudents < 5";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            // Vì mscv xuất hiện 2 lần trong câu SQL (1 ở Subquery, 1 ở điều kiện WHERE)
            ps.setString(1, mscv);
            ps.setString(2, mscv);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Đã đồng bộ số lượng sinh viên cho giảng viên {0}", mscv);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Không thể cập nhật: Giảng viên {0} đã đầy hoặc không tồn tại", mscv);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL khi đồng bộ số lượng sinh viên", e);
            return false;
        }
    }
    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT l.*, u.username, u.email as userEmail "
                + "FROM Lecturers l "
                + "INNER JOIN Users u ON l.userId = u.id "
                + "ORDER BY l.fullName";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lecturers.add(mapLecturer(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all lecturers", e);
        }
        return lecturers;
    }

    public List<Lecturer> getAvailableLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT l.*, u.username, u.email as userEmail "
                + "FROM Lecturers l "
                + "INNER JOIN Users u ON l.userId = u.id "
                + "WHERE l.currentStudents < l.maxStudents "
                + "ORDER BY l.fullName";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lecturers.add(mapLecturer(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting available lecturers", e);
        }
        return lecturers;
    }

    public boolean createLecturer(Lecturer lecturer) {
        LOGGER.log(Level.INFO, "Creating lecturer: mscv={0}, userId={1}, name={2}", 
                new Object[]{lecturer.getMscv(), lecturer.getUserId(), lecturer.getFullName()});
        
        String sql = "INSERT INTO Lecturers (mscv, userId, fullName, academicTitle, researchField, maxStudents, email) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lecturer.getMscv());
            ps.setInt(2, lecturer.getUserId());
            ps.setNString(3, lecturer.getFullName());
            ps.setNString(4, lecturer.getAcademicTitle());
            ps.setNString(5, lecturer.getResearchField());
            ps.setInt(6, lecturer.getMaxStudents());
            ps.setString(7, lecturer.getEmail());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Lecturer created successfully: mscv={0}", lecturer.getMscv());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Lecturer creation failed: no rows affected for mscv={0}", lecturer.getMscv());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating lecturer: mscv=" + lecturer.getMscv(), e);
            return false;
        }
    }


    public boolean updateLecturer(Lecturer lecturer) {
        String sqlUser = "UPDATE Users SET fullName = ? WHERE id = (SELECT userId FROM Lecturers WHERE mscv = ?)";
        String sqlLecturer = "UPDATE Lecturers SET fullName = ?, academicTitle = ?, researchField = ?, "
                + "maxStudents = ?, email = ? WHERE mscv = ?";

        Connection conn = null;
        try {
            conn = dbContext.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
                psUser.setString(1, lecturer.getFullName());
                psUser.setString(2, lecturer.getMscv());
                psUser.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlLecturer)) {
                ps.setNString(1, lecturer.getFullName());
                ps.setNString(2, lecturer.getAcademicTitle());
                ps.setNString(3, lecturer.getResearchField());
                ps.setInt(4, 5);
                ps.setString(5, lecturer.getEmail());
                ps.setString(6, lecturer.getMscv());
                ps.executeUpdate();
            }

            conn.commit(); 
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        }
    }        
    
    public boolean updateLecturerByUserId(Lecturer lecturer) {
        String sql = "UPDATE Lecturers SET mscv = ?, fullName = ?, academicTitle = ?, researchField = ?, "
                + "email = ? WHERE userId =?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecturer.getMscv());
            ps.setNString(2, lecturer.getFullName());
            ps.setNString(3, lecturer.getAcademicTitle());
            ps.setNString(4, lecturer.getResearchField());
            ps.setString(5, lecturer.getEmail());
            ps.setInt(6, lecturer.getUserId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating lecturer", e);
            return false;
        }
    }

    private Lecturer mapLecturer(ResultSet rs) throws SQLException {
        Lecturer lecturer = new Lecturer();
        lecturer.setMscv(rs.getString("mscv"));
        lecturer.setUserId(rs.getInt("userId"));
        lecturer.setFullName(rs.getNString("fullName"));
        lecturer.setAcademicTitle(rs.getNString("academicTitle"));
        lecturer.setResearchField(rs.getNString("researchField"));
        lecturer.setMaxStudents(rs.getInt("maxStudents"));
        lecturer.setCurrentStudents(rs.getInt("currentStudents"));
        lecturer.setEmail(rs.getString("email"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            lecturer.setCreatedAt(createdAt);
        }
        
        // Set user info if available
        try {
            String username = rs.getString("username");
            if (username != null) {
                User user = new User();
                user.setId(rs.getInt("userId"));
                user.setUsername(username);
                user.setEmail(rs.getString("userEmail"));
                lecturer.setUser(user);
            }
        } catch (SQLException e) {
            // Ignore if columns don't exist
        }
        
        return lecturer;
    }
}

