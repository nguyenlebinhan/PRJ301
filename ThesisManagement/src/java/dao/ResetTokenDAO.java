package dao;

import dal.DBContext;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ResetTokenDAO {
    private static final Logger LOGGER = Logger.getLogger(ResetTokenDAO.class.getName());
    private final DBContext dbContext;

    public ResetTokenDAO() {
        this.dbContext = new DBContext();
    }


    public String createResetToken(int userId, String email) {
        LOGGER.log(Level.INFO, "Creating reset token for userId: {0}, email: {1}", new Object[]{userId, email});
        
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24); // Token hết hạn sau 24 giờ
        
        LOGGER.log(Level.FINE, "Generated token: {0}, expires at: {1}", new Object[]{token, expiresAt});
        
        String sql = "INSERT INTO ResetTokens (userId, token, email, expiresAt) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setString(3, email);
            ps.setTimestamp(4, Timestamp.valueOf(expiresAt));
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Reset token created successfully for userId: {0}", userId);
                return token;
            } else {
                LOGGER.log(Level.WARNING, "Reset token creation failed: no rows affected for userId: {0}", userId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating reset token for userId: " + userId, e);
        }
        return null;
    }

    public boolean isValidToken(String token) {
        LOGGER.log(Level.FINE, "Validating token: {0}", token != null ? "provided" : "null");
        
        String sql = "SELECT * FROM ResetTokens WHERE token = ? AND used = 0 AND expiresAt > GETDATE()";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);
            
            try (ResultSet rs = ps.executeQuery()) {
                boolean isValid = rs.next();
                LOGGER.log(Level.FINE, "Token validation result: {0}", isValid);
                return isValid;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error validating token", e);
            return false;
        }
    }


    public Integer getUserIdByToken(String token) {
        String sql = "SELECT userId FROM ResetTokens WHERE token = ? AND used = 0 AND expiresAt > GETDATE()";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userId");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting userId by token", e);
        }
        return null;
    }


    public boolean markTokenAsUsed(String token) {
        LOGGER.log(Level.INFO, "Marking token as used: {0}", token != null ? "provided" : "null");
        
        String sql = "UPDATE ResetTokens SET used = 1 WHERE token = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Token marked as used successfully");
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Failed to mark token as used: no rows affected");
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking token as used", e);
            return false;
        }
    }
}

