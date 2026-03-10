package dao;

import dal.DBContext;
import model.Result;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultDAO extends DBContext {
    private static final Logger LOGGER = Logger.getLogger(ResultDAO.class.getName());

    /**
     * Lấy kết quả đồ án dựa trên thesisId.
     */
    public Result getResultByThesisId(int thesisId) {
        String sql = "SELECT * FROM Results WHERE thesisId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thesisId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractResultFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi lấy kết quả cho thesisId: " + thesisId, e);
        }
        return null;
    }

    /**
     * Khởi tạo bảng điểm trống cho một đồ án mới đăng ký.
     */
    public boolean initializeResult(int thesisId) {
        String sql = "INSERT INTO Results (thesisId) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thesisId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khởi tạo bảng điểm", e);
            return false;
        }
    }

    /**
     * Cập nhật điểm và tính toán điểm tổng kết.
     * Thường dùng khi Hội đồng chốt điểm cuối cùng.
     */
    public boolean updateFinalResult(Result result) {
        String sql = "UPDATE Results SET supervisorScore = ?, reviewerScore = ?, councilScore = ?, " +
                     "finalScore = ?, generalComment = ?, gradedAt = GETDATE(), gradedBy = ? " +
                     "WHERE thesisId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBigDecimal(1, result.getSupervisorScore());
            ps.setBigDecimal(2, result.getReviewerScore());
            ps.setBigDecimal(3, result.getCouncilScore());
            ps.setBigDecimal(4, result.getFinalScore());
            ps.setNString(5, result.getGeneralComment());
            ps.setInt(6, result.getGradedBy());
            ps.setInt(7, result.getThesisId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi cập nhật điểm tổng kết", e);
            return false;
        }
    }

    /**
     * Trích xuất dữ liệu từ ResultSet sang Object Result.
     */
    private Result extractResultFromResultSet(ResultSet rs) throws SQLException {
        Result r = new Result();
        r.setResultId(rs.getInt("resultId"));
        r.setThesisId(rs.getInt("thesisId"));
        r.setSupervisorScore(rs.getBigDecimal("supervisorScore"));
        r.setReviewerScore(rs.getBigDecimal("reviewerScore"));
        r.setCouncilScore(rs.getBigDecimal("councilScore"));
        r.setFinalScore(rs.getBigDecimal("finalScore"));
        r.setGeneralComment(rs.getNString("generalComment"));
        r.setGradedBy(rs.getObject("gradedBy") != null ? rs.getInt("gradedBy") : null);

        Timestamp gradedAt = rs.getTimestamp("gradedAt");
        if (gradedAt != null) {
            r.setGradedAt(gradedAt.toLocalDateTime());
        }
        return r;
    }
}