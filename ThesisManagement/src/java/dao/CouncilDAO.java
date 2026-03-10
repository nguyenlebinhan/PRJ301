package dao;

import dal.DBContext;
import model.Council;
import model.Lecturer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CouncilDAO extends DBContext {
    private static final Logger LOGGER = Logger.getLogger(CouncilDAO.class.getName());


    public List<Council> getAll() {
        List<Council> list = new ArrayList<>();
        String sql = "SELECT c.*, l1.fullName AS chairmanName, l2.fullName AS secretaryName " +
                     "FROM Councils c " +
                     "LEFT JOIN Lecturers l1 ON c.chairman = l1.mscv " +
                     "LEFT JOIN Lecturers l2 ON c.secretary = l2.mscv " +
                     "ORDER BY c.defenseDate DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToCouncil(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi getAll Councils", e);
        }
        return list;
    }

    /**
     * Tạo mới một Hội đồng bảo vệ
     */
    public boolean insert(Council c) {
        String sql = "INSERT INTO Councils (councilCode, councilName, defenseDate, location, " +
                     "chairman, secretary, members, status, createdAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'PLANNED', GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, c.getCouncilCode());
            ps.setNString(2, c.getCouncilName());
            ps.setTimestamp(3, Timestamp.valueOf(c.getDefenseDate()));
            ps.setNString(4, c.getLocation());
            ps.setString(5, c.getChairman());
            ps.setString(6, c.getSecretary());
            ps.setNString(7, c.getMembers());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi insert Council", e);
            return false;
        }
    }

    /**
     * Cập nhật trạng thái Hội đồng (SCHEDULED -> COMPLETED)
     */
    public boolean updateStatus(int councilId, String status) {
        String sql = "UPDATE Councils SET status = ? WHERE councilId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, councilId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi updateStatus Council", e);
            return false;
        }
    }

    private Council mapResultSetToCouncil(ResultSet rs) throws SQLException {
        Council c = new Council();
        c.setCouncilId(rs.getInt("councilId"));
        c.setCouncilCode(rs.getString("councilCode"));
        c.setCouncilName(rs.getNString("councilName"));
        c.setLocation(rs.getNString("location"));
        c.setChairman(rs.getString("chairman"));
        c.setSecretary(rs.getString("secretary"));
        c.setMembers(rs.getNString("members"));
        c.setStatus(rs.getString("status"));
        
        Timestamp defenseTs = rs.getTimestamp("defenseDate");
        if (defenseTs != null) c.setDefenseDate(defenseTs.toLocalDateTime());
        
        // Map thông tin Chủ tịch & Thư ký (Dữ liệu từ JOIN)
        Lecturer chairman = new Lecturer();
        chairman.setMscv(rs.getString("chairman"));
        chairman.setFullName(rs.getNString("chairmanName"));
        c.setChairmanLecturer(chairman);
        
        Lecturer secretary = new Lecturer();
        secretary.setMscv(rs.getString("secretary"));
        secretary.setFullName(rs.getNString("secretaryName"));
        c.setSecretaryLecturer(secretary);
        
        return c;
    }
}