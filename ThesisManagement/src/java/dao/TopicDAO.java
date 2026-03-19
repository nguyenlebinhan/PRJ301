package dao;

import dal.DBContext;
import dto.TopicResponseDTO;
import dto.TopicThesisDTO;
import model.Lecturer;
import model.Topic;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TopicDAO {
    private static final Logger LOGGER = Logger.getLogger(TopicDAO.class.getName());
    private final DBContext dbContext;

    public TopicDAO() {
        this.dbContext = new DBContext();
    }

    public Topic getTopicById(int topicId) {
        String sql = "SELECT t.*, l.fullName as lecturerName, l.researchField "
                + "FROM Topics t "
                + "LEFT JOIN Lecturers l ON t.createdBy = l.mscv "
                + "WHERE t.topicId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, topicId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTopic(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting topic by id: " + topicId, e);
        }
        return null;
    }
    public String getTopicNameById(int topicId) {
        String sql = "SELECT title FROM Topics WHERE topicId = ?";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, topicId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getNString("title");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving topic name for ID: " + topicId, e);
        }
        return null; 
    }

    public Topic getTopicByCode(String topicCode) {
        String sql = "SELECT t.*, l.fullName as lecturerName, l.researchField "
                + "FROM Topics t "
                + "LEFT JOIN Lecturers l ON t.createdBy = l.mscv "
                + "WHERE t.topicCode = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, topicCode);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTopic(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting topic by code: " + topicCode, e);
        }
        return null;
    }
    
    public boolean checkTopicExist(String topicCode) {
        // Sử dụng COUNT để kiểm tra sự tồn tại (hiệu năng tốt hơn SELECT *)
        String sql = "SELECT COUNT(*) FROM Topics WHERE topicCode = ?";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, topicCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Nếu count > 0 nghĩa là topicCode đã tồn tại
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error when checking the topic by topicCode: " + topicCode, e);
        }
        return false; 
    }

    public List<Topic> getAvailableTopics() {
        List<Topic> topics = new ArrayList<>();
        String sql = "SELECT t.*, l.fullName as lecturerName, l.researchField "
                + "FROM Topics t "
                + "LEFT JOIN Lecturers l ON t.createdBy = l.mscv "
                + "WHERE t.status = 'AVAILABLE' "
                + "ORDER BY t.createdAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                topics.add(mapTopic(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting available topics", e);
        }
        return topics;
    }

    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();
        String sql = "SELECT t.*, l.fullName as lecturerName, l.researchField "
                + "FROM Topics t "
                + "LEFT JOIN Lecturers l ON t.createdBy = l.mscv "
                + "ORDER BY t.createdAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                topics.add(mapTopic(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all topics", e);
        }
        return topics;
    }

    public List<TopicResponseDTO> getPendingRegistrations(String mscv) {
        List<TopicResponseDTO> list = new ArrayList<>();

        
        String sql = "SELECT s.fullName, s.mssv, t.topicId, t.title, tr.registeredAt " +
                     "FROM TopicRegistrations tr " +
                     "INNER JOIN Topics t ON t.topicId = tr.topicId " +
                     "INNER JOIN Students s ON tr.mssv = s.mssv " +
                     "WHERE tr.mscvHD = ? AND tr.status = 'PENDING' " + 
                     "ORDER BY tr.registeredAt DESC"; 

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mscv);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TopicResponseDTO dto = new TopicResponseDTO();
                    dto.setStudentName(rs.getString("fullName"));
                    dto.setMssv(rs.getString("mssv"));
                    dto.setTopicId(rs.getInt("topicId"));
                    dto.setTopicTitle(rs.getString("title"));
                    dto.setRegisteredAt(rs.getTimestamp("registeredAt"));
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đăng ký cho giảng viên: " + mscv, e);
        }
        return list;
    }

    public boolean createTopic(Topic topic) {
        LOGGER.log(Level.INFO, "Creating topic: code={0}, title={1}, description={2}, technicalRequirements={3}, status={4}, type={5},createdBy={6},difficultyScore ={7}", 
                new Object[]{topic.getTopicCode(), topic.getTitle(),topic.getDescription(),topic.getTechnicalRequirements(),topic.getStatus(),topic.getType(), topic.getCreatedBy(),topic.getDifficultyScore()});
        
        String sql = "INSERT INTO Topics (topicCode, title, description, technicalRequirements, status, type, createdBy,difficultyScore) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, topic.getTopicCode());
            ps.setNString(2, topic.getTitle());
            ps.setNString(3, topic.getDescription());
            ps.setNString(4, topic.getTechnicalRequirements());
            ps.setString(5, topic.getStatus());
            ps.setString(6, topic.getType());
            ps.setString(7, topic.getCreatedBy());
            ps.setBigDecimal(8, topic.getDifficultyScore());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Topic created successfully: code={0}", topic.getTopicCode());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Topic creation failed: no rows affected for code={0}", topic.getTopicCode());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating topic: code=" + topic.getTopicCode(), e);
            return false;
        }
    }

    public boolean updateTopic(Topic topic) {
        String sql = "UPDATE Topics SET title = ?, description = ?, technicalRequirements = ?, "
                + "status = ?, difficultyScore = ? WHERE topicId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, topic.getTitle());
            ps.setNString(2, topic.getDescription());
            ps.setNString(3, topic.getTechnicalRequirements());
            ps.setString(4, topic.getStatus());
            ps.setBigDecimal(5, topic.getDifficultyScore());
            ps.setInt(6, topic.getTopicId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating topic", e);
            return false;
        }
    }

    public boolean approveTopic(int topicId, int approvedBy) {
        String sql = "UPDATE Topics SET status = 'APPROVED', approvedAt = GETDATE(), approvedBy = ? WHERE topicId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, approvedBy);
            ps.setInt(2, topicId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error approving topic", e);
            return false;
        }
    }
    
    public TopicThesisDTO getAcceptedTopic(String mssv){
        
        String sql = "select t.topicId,t.title,t.topicCode,tr.mssv,t.description,t.technicalRequirements,t.difficultyScore,th.thesisId,th.reportFile,th.sourceCodeLink from TopicRegistrations tr INNER JOIN Topics t on tr.topicId = t.topicId LEFT JOIN Theses th on th.topicId=t.topicId WHERE tr.mssv = ? AND tr.status = 'ACCEPTED'";
        
        try (Connection conn = dbContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TopicThesisDTO topic = new TopicThesisDTO();
                    topic.setTopicId(rs.getInt("topicId"));
                    topic.setTopicTitle(rs.getString("title"));
                    topic.setTopicCode(rs.getString("topicCode"));
                    topic.setMssv(rs.getString("mssv"));
                    topic.setDescription(rs.getString("description"));
                    topic.setTechnicalRequirements(rs.getString("technicalRequirements"));
                    topic.setDifficultyScore(rs.getBigDecimal("difficultyScore"));
                    topic.setThesisId(rs.getInt("thesisId"));
                    topic.setReportFile(rs.getString("reportFile"));
                    topic.setSourceCodeLink(rs.getString("sourceCodeLink"));
                    return topic;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đăng ký cho sinh viên: " + mssv, e);
        }
        return null;
    } 
    
    public List<Topic> searchTopics(String keyword) {
        List<Topic> list = new ArrayList<>();
        
        String sql = "SELECT t.* from Topics t WHERE t.title LIKE ? ";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Giả sử bạn có hàm map kết quả
                list.add(mapTopic(rs)); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }    

    public boolean deleteTopic(int topicId,int executedByUserId) {
        String sql = "{call sp_DeleteTopic(?, ?)}";

            try (Connection conn = dbContext.getConnection();
                 CallableStatement cstmt = conn.prepareCall(sql)) {

                cstmt.setInt(1, topicId);
                cstmt.setInt(2, executedByUserId);

                cstmt.execute();
                return true; // Nếu không có lỗi SQLException nghĩa là xóa thành công

            } catch (SQLException e) {
                // Procedure sẽ ném lỗi RAISERROR nếu giảng viên xóa nhầm topic người khác
                LOGGER.log(Level.SEVERE, "Không thể xóa Topic ID: " + topicId + ". Lý do: " + e.getMessage());
                return false;
            }
    }    
    private Topic mapTopic(ResultSet rs) throws SQLException {
        Topic topic = new Topic();
        topic.setTopicId(rs.getInt("topicId"));
        topic.setTopicCode(rs.getString("topicCode"));
        topic.setTitle(rs.getNString("title"));
        topic.setDescription(rs.getNString("description"));
        topic.setTechnicalRequirements(rs.getNString("technicalRequirements"));
        topic.setStatus(rs.getString("status"));
        topic.setType(rs.getString("type"));
        topic.setCreatedBy(rs.getString("createdBy"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            topic.setCreatedAt(createdAt);
        }
        topic.setDifficultyScore(rs.getBigDecimal("difficultyScore"));
        
        try {
            String lecturerName = rs.getNString("lecturerName");
            if (lecturerName != null) {
                Lecturer lecturer = new Lecturer();
                lecturer.setMscv(rs.getString("createdBy"));
                lecturer.setFullName(lecturerName);
                lecturer.setResearchField(rs.getNString("researchField"));
                topic.setLecturer(lecturer);
            }
        } catch (SQLException e) {
            // Ignore if columns don't exist
        }
        
        return topic;
    }

    public boolean updateTopicStatus(int topicId, String newStatus) {
        String sql = "UPDATE Topics SET status = ? WHERE topicId = ?";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, topicId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái đề tài ID: " + topicId, e);
            return false;
        }
    }    
    
}

