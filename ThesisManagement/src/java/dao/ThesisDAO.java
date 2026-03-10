package dao;

import dal.DBContext;
import dto.StudentProgressDTO;
import dto.ThesisUpdateRequest;
import dto.TopicThesisDTO;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object cho Thesis
 */
public class ThesisDAO {
    private static final Logger LOGGER = Logger.getLogger(ThesisDAO.class.getName());
    private final DBContext dbContext;
    private final StudentDAO studentDAO;
    private final LecturerDAO lecturerDAO;
    private final TopicDAO topicDAO;

    public ThesisDAO() {
        this.dbContext = new DBContext();
        this.studentDAO = new StudentDAO();
        this.lecturerDAO = new LecturerDAO();
        this.topicDAO = new TopicDAO();
    }

    public Thesis getThesisById(int thesisId) {
        String sql = "SELECT * FROM Theses WHERE thesisId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thesisId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapThesis(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting thesis by id: " + thesisId, e);
        }
        return null;
    }
    

    public Thesis getThesisByCode(String thesisCode) {
        String sql = "SELECT * FROM Theses WHERE thesisCode = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, thesisCode);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapThesis(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting thesis by code: " + thesisCode, e);
        }
        return null;
    }
    public List<StudentProgressDTO> getStudentProgress(String mscv){
        List<StudentProgressDTO> studentList = new ArrayList<>();
        String sql = "select s.fullName,th.reportFile,th.sourceCodeLink,th.similarityScore,th.plagiarismStatus,th.bestSource,th.relevantTopicScore,th.relevantTopicStatus from Theses th join Students s on s.mssv = th.mssv where th.mscvHD=?";
        try(Connection conn = dbContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, mscv);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    StudentProgressDTO s= new StudentProgressDTO(rs.getNString("fullName"),rs.getString("reportFile"),rs.getString("sourceCodeLink"),rs.getInt("similarityScore"),rs.getString("plagiarismStatus"),rs.getString("bestSource"),rs.getDouble("relevantTopicScore"),rs.getString("relevantTopicStatus"));
                    studentList.add(s);
                }
            }
        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"Error getting student progress by mscv: "+ mscv,e);
        }
        return studentList;
    }

    public Thesis getThesisByStudent(String mssv) {
        String sql = "SELECT * FROM Theses WHERE mssv = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mssv);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapThesis(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting thesis by student: " + mssv, e);
        }
        return null;
    }

    public List<Thesis> getThesesBySupervisor(String mscvHD) {
        List<Thesis> theses = new ArrayList<>();
        String sql = "SELECT * FROM Theses WHERE mscvHD = ? ORDER BY registeredAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mscvHD);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    theses.add(mapThesis(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting theses by supervisor: " + mscvHD, e);
        }
        return theses;
    }


    public boolean cancelThesis(int thesisId) {
        // Chuyển trạng thái sang 'CANCELLED' để ẩn khỏi Dashboard nhưng vẫn giữ lại DB
        String sql = "UPDATE Theses SET status = 'CANCELLED' WHERE thesisId = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thesisId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    public List<Thesis> getAllTheses() {
        List<Thesis> theses = new ArrayList<>();
        String sql = "SELECT * FROM Theses ORDER BY registeredAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                theses.add(mapThesis(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all theses", e);
        }
        return theses;
    }

    public boolean createThesis(Thesis thesis) {
        String sql = "INSERT INTO Theses (topicId, mssv, mscvHD,reportFile,sourceCodeLink, status,similarityScore,plagiarismStatus,bestSource,plagiarism_analysis,relevantTopicScore,relevantTopicStatus,relevance_analysis) VALUES (? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thesis.getTopicId());
            ps.setString(2, thesis.getMssv());
            ps.setString(3, thesis.getMscvHD());
            ps.setNString(4, thesis.getReportFile());
            ps.setNString(5, thesis.getSourceCodeLink());
            ps.setString(6, thesis.getStatus());
            ps.setDouble(7, thesis.getSimilarityScore());
            ps.setString(8, thesis.getPlagiarismStatus());
            ps.setString(9,thesis.getBestSource());
            ps.setString(10, thesis.getPlagiarismAnalysis());
            ps.setDouble(11, thesis.getRelevantTopicScore());
            ps.setString(12, thesis.getRelevantTopicStatus());
            ps.setString(13, thesis.getRelevanceAnalysis());
            boolean result = ps.executeUpdate() > 0;
            
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating thesis", e);
            return false;
        }
    }

    public boolean updateThesis(Thesis thesis) {
        String sql = "UPDATE Theses SET reportFile = ?, sourceCodeLink = ?, status = ?,similarityScore = ?,plagiarismStatus = ?,bestSource = ?"
                + " WHERE thesisId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, thesis.getReportFile());
            ps.setNString(2, thesis.getSourceCodeLink());
            ps.setString(3, thesis.getStatus());
            ps.setDouble(4, thesis.getSimilarityScore());
            ps.setString(5,thesis.getPlagiarismStatus());
            ps.setString(6, thesis.getBestSource());
            ps.setInt(7, thesis.getThesisId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating thesis", e);
            return false;
        }
    }
    
    public int getNumberOfReport(String mscv){
        String sql = "select Count(thesisId) as numberOfReport from Theses where mscvHD = ?";
        int count =0;
        try (Connection conn = dbContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mscv);
            
            try(ResultSet rs =ps.executeQuery()){
                if(rs.next()){
                    count = rs.getInt("numberOfReport");
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting reports for lecturer:"+mscv, e);
            
        }  
        return count;
    }
    
    public boolean updateThesis(ThesisUpdateRequest thesis) {
        String sql = "UPDATE Theses SET reportFile = ?, sourceCodeLink = ?, similarityScore = ?, plagiarismStatus =?, bestSource = ?, plagiarism_analysis = ?, relevantTopicScore = ?, relevantTopicStatus = ?, relevance_analysis = ?"
                + "WHERE thesisId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, thesis.getReportFile());
            ps.setNString(2, thesis.getSourceCodeLink());
            ps.setDouble(3, thesis.getSimilarityScore());
            ps.setString(4, thesis.getPlagiarismStatus());
            ps.setString(5, thesis.getBestSource());
            ps.setString(6, thesis.getPlagiarismAnalysis());
            ps.setDouble(7, thesis.getRelevantTopicScore());
            ps.setString(8, thesis.getRelevantTopicStatus());
            ps.setString(9, thesis.getRelevanceAnalysis());
            ps.setInt(10,thesis.getThesisId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating thesis", e);
            return false;
        }
    }

    private Thesis mapThesis(ResultSet rs) throws SQLException {
        Thesis thesis = new Thesis();
        thesis.setThesisId(rs.getInt("thesisId"));
        thesis.setThesisCode(rs.getString("thesisCode"));
        thesis.setTopicId(rs.getInt("topicId"));
        thesis.setMssv(rs.getString("mssv"));
        thesis.setMscvHD(rs.getString("mscvHD"));
        thesis.setReportFile(rs.getNString("reportFile"));
        thesis.setSourceCodeLink(rs.getNString("sourceCodeLink"));
        thesis.setStatus(rs.getString("status"));
        
        Timestamp registeredAt = rs.getTimestamp("registeredAt");
        if (registeredAt != null) {
            thesis.setRegisteredAt(registeredAt);
        }
        
        return thesis;
    }
}

