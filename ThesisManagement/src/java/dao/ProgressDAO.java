package dao;

import dal.DBContext;
import model.Progress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object cho Progress
 */
public class ProgressDAO {
    private static final Logger LOGGER = Logger.getLogger(ProgressDAO.class.getName());
    private final DBContext dbContext;
    private final ThesisDAO thesisDAO;

    public ProgressDAO() {
        this.dbContext = new DBContext();
        this.thesisDAO = new ThesisDAO();
    }

    public Progress getProgressById(int progressId) {
        String sql = "SELECT * FROM Progress WHERE progressId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, progressId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProgress(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting progress by id: " + progressId, e);
        }
        return null;
    }

    public List<Progress> getProgressByThesis(int thesisId) {
        List<Progress> progressList = new ArrayList<>();
        String sql = "SELECT * FROM Progress WHERE thesisId = ? ORDER BY week ASC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thesisId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    progressList.add(mapProgress(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting progress by thesis: " + thesisId, e);
        }
        return progressList;
    }

    public Progress getLatestProgress(int thesisId) {
        String sql = "SELECT TOP 1 * FROM Progress WHERE thesisId = ? ORDER BY week DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thesisId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProgress(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting latest progress for thesis: " + thesisId, e);
        }
        return null;
    }

    public boolean createProgress(Progress progress) {
        String sql = "INSERT INTO Progress (thesisId, week, reportContent, attachments, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, progress.getThesisId());
            ps.setInt(2, progress.getWeek());
            ps.setNString(3, progress.getReportContent());
            ps.setNString(4, progress.getAttachments());
            ps.setString(5,progress.getStatus());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating progress", e);
            return false;
        }
    }

    public boolean updateProgress(Progress progress) {
        String sql = "UPDATE Progress SET reportContent = ?, attachments = ?, lecturerComment = ?, "
                + "aiStatus = ?, aiWarning = ?, status = ?, reviewedAt = GETDATE() WHERE progressId = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, progress.getReportContent());
            ps.setNString(2, progress.getAttachments());
            ps.setNString(3, progress.getLecturerComment());
            ps.setString(4, progress.getStatus());
            ps.setInt(5, progress.getProgressId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating progress", e);
            return false;
        }
    }

    private Progress mapProgress(ResultSet rs) throws SQLException {
        Progress progress = new Progress();
        progress.setProgressId(rs.getInt("progressId"));
        progress.setThesisId(rs.getInt("thesisId"));
        progress.setWeek(rs.getInt("week"));
        progress.setReportContent(rs.getNString("reportContent"));
        progress.setAttachments(rs.getNString("attachments"));
        progress.setLecturerComment(rs.getNString("lecturerComment"));
        progress.setStatus(rs.getString("status"));
        
        Timestamp submittedAt = rs.getTimestamp("submittedAt");
        if (submittedAt != null) {
            progress.setSubmittedAt(submittedAt.toLocalDateTime());
        }
        
        Timestamp reviewedAt = rs.getTimestamp("reviewedAt");
        if (reviewedAt != null) {
            progress.setReviewedAt(reviewedAt.toLocalDateTime());
        }
        
        // Load thesis
        progress.setThesis(thesisDAO.getThesisById(progress.getThesisId()));
        
        return progress;
    }

    public int countByThesisId(int thesisId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

