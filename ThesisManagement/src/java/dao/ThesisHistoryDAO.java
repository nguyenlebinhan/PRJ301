/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dal.DBContext;
import dto.ImprovementRequest;
import dto.ScoreUpdateRequest;
import dto.ThesisHistoryResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import model.Thesis;
import model.ThesisHistory;

public class ThesisHistoryDAO extends DBContext {
    public static final Logger LOGGER = Logger.getLogger(ThesisHistoryDAO.class.getName());
    private final DBContext dbContext;

    public ThesisHistoryDAO() {
        this.dbContext = new DBContext();
    } 
    
    public List<ThesisHistory> getThesisHistoryByMssv(String mssv,int thesisId){
        List<ThesisHistory> thesisHistory = new ArrayList<>();
        String sql = "SELECT th.* FROM ThesisHistory th WHERE mssv = ? AND thesisId = ?  ORDER BY createdAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mssv);
            ps.setInt(2,thesisId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    thesisHistory.add(mapThesisHistory(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting theses by Student: " + mssv, e);
        }
        return thesisHistory;        
    }
    
    
    public List<ThesisHistoryResponse> getThesisHistory(String mssv,int thesisId){
        List<ThesisHistoryResponse> thesisHistory = new ArrayList<>();
        String sql = "SELECT th.*FROM ThesisHistory th INNER JOIN Theses t on t.thesisId = th.thesisId INNER JOIN Topics topics on topics.topicId = t.topicId WHERE th.mssv = ? AND th.thesisId = ?  ORDER BY createdAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mssv);
            ps.setInt(2,thesisId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThesisHistoryResponse th = new ThesisHistoryResponse();
                    th.setHistoryId(rs.getInt("historyId"));
                    th.setThesisId(rs.getInt("thesisId"));
                    th.setMssv(rs.getString("mssv"));
                    th.setReportFile(rs.getString("reportFile"));
                    th.setSourceCodeLink(rs.getString("sourceCodeLink"));
                    Timestamp ts = rs.getTimestamp("createdAt");
                    if (ts != null) {
                        th.setCreatedAt(ts.toLocalDateTime()); 
                    }
                    th.setSimilarityScore(rs.getDouble("similarityScore"));
                    th.setPlagiarismStatus(rs.getString("plagiarismStatus"));
                    th.setBestSource(rs.getString("bestSource"));
                    th.setPlagiarismAnalysis(rs.getString("plagiarism_analysis"));
                    th.setRelevantTopicScore(rs.getDouble("relevantTopicScore"));
                    th.setRelevantTopicStatus(rs.getString("relevantTopicStatus"));
                    th.setRelevanceAnalysis(rs.getString("relevance_analysis"));
                    th.setFocusAnalysis(rs.getNString("focus_analysis"));
                    th.setGeneralObservations(rs.getNString("general_observations"));
                    th.setTopPrior(rs.getNString("top_three_prior"));
                    th.setAiRequestPrompt(rs.getNString("aiRequestPrompt"));
                    th.setScore(rs.getDouble("score"));
                    th.setFeedback(rs.getNString("feedback"));                    
                    thesisHistory.add(th);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting theses by Student: " + mssv, e);
        }
        return thesisHistory;        
    }
    
    public List<ThesisHistoryResponse> getThesisHistoryByMssv(String mssv){
        List<ThesisHistoryResponse> thesisHistory = new ArrayList<>();
        String sql = "SELECT th.* FROM ThesisHistory th INNER JOIN Theses t on t.thesisId = th.thesisId INNER JOIN Topics topics on topics.topicId = t.topicId  WHERE th.mssv = ? ORDER BY createdAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mssv);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThesisHistoryResponse th = new ThesisHistoryResponse();
                    th.setHistoryId(rs.getInt("historyId"));
                    th.setThesisId(rs.getInt("thesisId"));
                    th.setMssv(rs.getString("mssv"));
                    th.setReportFile(rs.getString("reportFile"));
                    th.setSourceCodeLink(rs.getString("sourceCodeLink"));
                    Timestamp ts = rs.getTimestamp("createdAt");
                    if (ts != null) {
                        th.setCreatedAt(ts.toLocalDateTime()); 
                    }
                    th.setSimilarityScore(rs.getDouble("similarityScore"));
                    th.setPlagiarismStatus(rs.getString("plagiarismStatus"));
                    th.setBestSource(rs.getString("bestSource"));  
                    th.setSimilarityScore(rs.getDouble("similarityScore"));
                    th.setPlagiarismStatus(rs.getString("plagiarismStatus"));
                    th.setBestSource(rs.getString("bestSource"));
                    th.setPlagiarismAnalysis(rs.getString("plagiarism_analysis"));
                    th.setRelevantTopicScore(rs.getDouble("relevantTopicScore"));
                    th.setRelevantTopicStatus(rs.getString("relevantTopicStatus"));
                    th.setRelevanceAnalysis(rs.getString("relevance_analysis"));
                    th.setScore(rs.getDouble("score"));
                    th.setFeedback(rs.getNString("feedback"));
                    thesisHistory.add(th);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting theses by Student: " + mssv, e);
        }
        return thesisHistory;        
    } 

    public boolean addToHistory(Thesis thesis) {
        String sql = "INSERT INTO ThesisHistory (thesisId, mssv,reportFile,sourceCodeLink,createdAt,similarityScore,plagiarismStatus,bestSource,plagiarism_analysis,relevantTopicScore,relevantTopicStatus,relevance_analysis) VALUES (? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thesis.getThesisId());
            ps.setString(2, thesis.getMssv());
            ps.setNString(3, thesis.getReportFile());
            ps.setNString(4, thesis.getSourceCodeLink());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()) );
            ps.setDouble(6, thesis.getSimilarityScore());
            ps.setString(7, thesis.getPlagiarismStatus());
            ps.setString(8,thesis.getBestSource());
            ps.setString(9, thesis.getPlagiarismAnalysis());
            ps.setDouble(10, thesis.getRelevantTopicScore());
            ps.setString(11, thesis.getRelevantTopicStatus());
            ps.setString(12, thesis.getRelevanceAnalysis());
            boolean result = ps.executeUpdate() > 0;
            
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating thesis", e);
            return false;
        }
    }    
    
    public boolean updateScoreInThesisHistory(ScoreUpdateRequest request) {
        String sql = "UPDATE ThesisHistory SET score = ?, feedback = ? WHERE historyId = (SELECT TOP 1 historyId FROM ThesisHistory WHERE thesisId = ? ORDER BY createdAt DESC)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
           
            ps.setDouble(1, request.getScore());
            ps.setNString(2,request.getFeedback());
            ps.setInt(3, request.getThesisId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating thesis", e);
            return false;
        }
    }    

    public boolean updateImprovementInThesisHistory(ImprovementRequest request) {
        String sql = "UPDATE ThesisHistory SET focus_analysis = ? ,general_observations = ?, top_three_prior = ?, aiRequestPrompt = ? WHERE historyId = (SELECT TOP 1 historyId FROM ThesisHistory WHERE thesisId = ? ORDER BY createdAt DESC)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
           
            ps.setNString(1, request.getFocusAnalysis());
            ps.setNString(2,request.getGeneralObservations());
            ps.setNString(3, request.getTopPrior());
            ps.setNString(4, request.getAiRequestPrompt());
            ps.setInt(5, request.getThesisId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating thesis", e);
            return false;
        }
    }      
    public ThesisHistoryResponse getNameAndtopicCode(String mssv, int thesisId) {
        ThesisHistoryResponse th = null;
        String sql = "SELECT topics.title, topics.topicCode " +
                     "FROM ThesisHistory th " +
                     "INNER JOIN Theses t ON t.thesisId = th.thesisId " +
                     "INNER JOIN Topics topics ON topics.topicId = t.topicId " +
                     "WHERE th.mssv = ? AND th.thesisId = ? " +
                     "ORDER BY th.createdAt DESC";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);
            ps.setInt(2, thesisId);

            try (ResultSet rs = ps.executeQuery()) {
                // QUAN TRỌNG: Phải có rs.next() để di chuyển con trỏ đến dòng dữ liệu đầu tiên
                if (rs.next()) {
                    th = new ThesisHistoryResponse();
                    th.setTitle(rs.getString("title"));
                    th.setTopicCode(rs.getString("topicCode"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting Name and TopicCode for Student: " + mssv, e);
        }
        return th; 
    }
    public ThesisHistoryResponse getNameAndtopicCodeByMssv(String mssv) {
        ThesisHistoryResponse th = null;
        // Câu SQL của bạn đã ổn, sử dụng JOIN để lấy thông tin từ bảng Topics
        String sql = "SELECT topics.title, topics.topicCode " +
                     "FROM ThesisHistory th " +
                     "INNER JOIN Theses t ON t.thesisId = th.thesisId " +
                     "INNER JOIN Topics topics ON topics.topicId = t.topicId " +
                     "WHERE th.mssv = ? " +
                     "ORDER BY th.createdAt DESC";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);

            try (ResultSet rs = ps.executeQuery()) {
                // QUAN TRỌNG: Phải có rs.next() để di chuyển con trỏ đến dòng dữ liệu đầu tiên
                if (rs.next()) {
                    th = new ThesisHistoryResponse();
                    th.setTitle(rs.getString("title"));
                    th.setTopicCode(rs.getString("topicCode"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting Name and TopicCode for Student: " + mssv, e);
        }
        return th; 
    }
    
    public boolean addThesisHistory(String mssv, int thesisId, String reportFile, String sourceCodeLink) {
        String sql = "INSERT INTO ThesisHistory (mssv, thesisId, reportFile, sourceCodeLink, createdAt) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mssv);
            ps.setInt(2, thesisId);
            ps.setNString(3, reportFile);
            ps.setNString(4, sourceCodeLink);
            // Sets the current system time for the registration record
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding thesis history for Student: " + mssv, e);
            return false;
        }
    }
    
    private ThesisHistory mapThesisHistory(ResultSet rs) throws SQLException {
        ThesisHistory thesisHistory = new ThesisHistory();
        thesisHistory.setHistoryId(rs.getInt("historyId"));
        thesisHistory.setThesisId(rs.getInt("thesisId"));
        thesisHistory.setMssv(rs.getString("mssv"));
        thesisHistory.setReportFile(rs.getNString("reportFile"));
        thesisHistory.setSourceCodeLink(rs.getNString("sourceCodeLink"));
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            thesisHistory.setCreatedAt(createdAt);
        }
        
        return thesisHistory;
    }    
    
}
