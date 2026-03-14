package dao;

import dal.DBContext;
import dto.AdminInformationRequest;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Topic;

/**
 * Data Access Object cho User
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private final DBContext dbContext;

    public UserDAO() {
        this.dbContext = new DBContext();
    }
    
    public User login(String username, String password) {
        LOGGER.log(Level.INFO, "Attempting login for username: {0}", username);
        
        String sql = "SELECT u.* "
                + "FROM Users u "
                + "WHERE TRIM(u.username) = ? AND TRIM(u.password) = ? AND u.isActive = 1";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            LOGGER.log(Level.FINE, "Database connection established for login");
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapUser(rs);
                    
                    LOGGER.log(Level.INFO, "Login successful for username: {0}, role: {1}", 
                            new Object[]{username, user.getRole()});
                    return user;
                } else {
                    LOGGER.log(Level.WARNING, "Login failed: Invalid credentials for username: {0}", username);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error logging in user: " + username, e);
        }
        return null;
    }
    
    public User getUserById(int id) {
        LOGGER.log(Level.INFO, "Getting user by id: {0}", id);
        
        String sql = "SELECT u.* "
                + "FROM Users u "
                + "WHERE u.id = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapUser(rs);
                    LOGGER.log(Level.INFO, "User found: id={0}, username={1}, role={2}", 
                            new Object[]{id, user.getUsername(), user.getRole()});
                    return user;
                } else {
                    LOGGER.log(Level.WARNING, "User not found with id: {0}", id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by id: " + id, e);
        }
        return null;
    }

    public User getUserByUsername(String username) {
        LOGGER.log(Level.INFO, "Getting user by username: {0}", username);
        
        String sql = "SELECT u.* "
                + "FROM Users u "
                + "WHERE u.username = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapUser(rs);
                    LOGGER.log(Level.INFO, "User found: username={0}, id={1}, role={2}", 
                            new Object[]{username, user.getId(), user.getRole()});
                    return user;
                } else {
                    LOGGER.log(Level.WARNING, "User not found with username: {0}", username);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username: " + username, e);
        }
        return null;
    }
    
    
    public User getUserByEmail(String email) {
        LOGGER.log(Level.INFO, "Getting user by email: {0}", email);
        
        String sql = "SELECT u.* "
                + "FROM Users u "
                + "WHERE u.email = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapUser(rs);
                    LOGGER.log(Level.INFO, "User found: email={0}, id={1}, username={2}", 
                            new Object[]{email, user.getId(), user.getUsername()});
                    return user;
                } else {
                    LOGGER.log(Level.WARNING, "User not found with email: {0}", email);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email: " + email, e);
        }
        return null;
    }

    public List<User> getAllNewUsers() {
        LOGGER.log(Level.INFO, "Getting all users");
        
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.* "
                + "FROM Users u "
                + "ORDER BY u.createdAt DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                User user = mapUser(rs);
                users.add(user);
            }
            LOGGER.log(Level.INFO, "Retrieved {0} users", users.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
        }
        return users;
    }
    
    public List<AdminInformationRequest> getAllInformation() {
        LOGGER.log(Level.INFO, "Getting all users");
        
        List<AdminInformationRequest> infos = new ArrayList<>();
        String sql = "select u.*,l.mscv,l.academicTitle,l.researchField,s.mssv,s.className,s.major,s.gpa,s.skills,s.phone from Users u left join Students s on u.id=s.userId left join Lecturers l on l.userId=u.id ";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                AdminInformationRequest info =new AdminInformationRequest();
                info.setId(rs.getInt("id"));
                info.setUsername(rs.getString("username"));
                info.setPassword(rs.getString("password"));
                info.setEmail(rs.getString("email"));
                info.setFullName(rs.getNString("fullName"));
                info.setRole(rs.getString("role"));
                info.setCreatedAt(rs.getTimestamp("createdAt"));
                info.setIsActive(rs.getBoolean("isActive"));
                if(rs.getString("mscv") != null){
                    info.setMscv(rs.getString("mscv"));
                }
                if(rs.getNString("academicTitle") != null){
                    info.setAcademicTitle(rs.getNString("academicTitle"));
                }
                if(rs.getNString("researchField") != null){
                    info.setResearchField(rs.getNString("researchField"));
                }
                if(rs.getString("mssv") != null){
                    info.setMssv(rs.getString("mssv"));
                }
                if(rs.getNString("className") != null){
                    info.setClassName(rs.getNString("className"));
                }
                if(rs.getNString("major") != null){
                    info.setMajor(rs.getNString("major"));
                }
                if(rs.getBigDecimal("gpa") != null){
                    info.setGpa(rs.getBigDecimal("gpa"));
                }
                if(rs.getNString("skills") != null){
                    info.setSkills(rs.getNString("skills"));
                }                
                if(rs.getString("phone") != null){
                    info.setPhone(rs.getString("phone"));
                }
                
                infos.add(info);
            }
            LOGGER.log(Level.INFO, "Retrieved {0} users", infos.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
        }
        return infos;
    }    
    
    public List<AdminInformationRequest> searchUser(String name) {
        LOGGER.log(Level.INFO, "Search users");
        
        List<AdminInformationRequest> infos = new ArrayList<>();
        StringBuilder sql = new StringBuilder( "select u.*,l.mscv,l.academicTitle,l.researchField,s.mssv,s.className,s.major,s.gpa,s.skills,s.phone from Users u left join Students s on u.id=s.userId left join Lecturers l on l.userId=u.id WHERE 1=1  ");
        
        if (name != null && !name.isBlank()) {
            sql.append(" AND (u.fullName LIKE ? OR u.email LIKE ? OR l.mscv LIKE ? OR s.mssv LIKE ?)");
        }
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            
            if (name != null && !name.isBlank()) {
                ps.setString(paramIndex++, "%" + name + "%");
                ps.setString(paramIndex++, "%" + name + "%");
                ps.setString(paramIndex++, "%" + name + "%");
                ps.setString(paramIndex++, "%" + name + "%");
            }            
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    AdminInformationRequest info =new AdminInformationRequest();
                    info.setId(rs.getInt("id"));
                    info.setUsername(rs.getString("username"));
                    info.setPassword(rs.getString("password"));
                    info.setEmail(rs.getString("email"));
                    info.setFullName(rs.getNString("fullName"));
                    info.setRole(rs.getString("role"));
                    info.setCreatedAt(rs.getTimestamp("createdAt"));
                    info.setIsActive(rs.getBoolean("isActive"));
                    if(rs.getString("mscv") != null){
                        info.setMscv(rs.getString("mscv"));
                    }
                    if(rs.getNString("academicTitle") != null){
                        info.setAcademicTitle(rs.getNString("academicTitle"));
                    }
                    if(rs.getNString("researchField") != null){
                        info.setResearchField(rs.getNString("researchField"));
                    }
                    if(rs.getString("mssv") != null){
                        info.setMssv(rs.getString("mssv"));
                    }
                    if(rs.getNString("className") != null){
                        info.setClassName(rs.getNString("className"));
                    }
                    if(rs.getNString("major") != null){
                        info.setMajor(rs.getNString("major"));
                    }
                    if(rs.getBigDecimal("gpa") != null){
                        info.setGpa(rs.getBigDecimal("gpa"));
                    }
                    if(rs.getNString("skills") != null){
                        info.setSkills(rs.getNString("skills"));
                    }                
                    if(rs.getString("phone") != null){
                        info.setPhone(rs.getString("phone"));
                    }

                    infos.add(info);                
                }
            }
            LOGGER.log(Level.INFO, "Retrieved {0} users", infos.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
        }
        return infos;
    }    
        

    public boolean createUser(User user) {
        LOGGER.log(Level.INFO, "Creating new user: username={0}, email={1}, roleId={2}", 
                new Object[]{user.getUsername(), user.getEmail(), user.getRole()});
        
        String sql = "INSERT INTO Users (username, password, email, fullName, role) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setNString(4, user.getFullName());
            ps.setNString(5, user.getRole());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "User created successfully: username={0}", user.getUsername());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "User creation failed: no rows affected for username={0}", user.getUsername());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user: username=" + user.getUsername(), e);
            return false;
        }
    }

    public boolean updateUser(User user) {
        LOGGER.log(Level.INFO, "Updating user: id={0}, username={1}", 
                new Object[]{user.getId(), user.getUsername()});
        
        String sql = "UPDATE Users SET username = ? ,email = ?, fullName = ?, role = ?, isActive = ? WHERE id = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1,user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setNString(3, user.getFullName());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.getIsActive());
            ps.setInt(6, user.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "User updated successfully: id={0}", user.getId());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "User update failed: no rows affected for id={0}", user.getId());
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: id=" + user.getId(), e);
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        LOGGER.log(Level.INFO, "Updating password for user id: {0}", userId);
        
        String sql = "UPDATE Users SET password = ? WHERE id = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newPassword); // TODO: Hash với BCrypt
            ps.setInt(2, userId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Password updated successfully for user id: {0}", userId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Password update failed: no rows affected for user id: {0}", userId);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password for user id: " + userId, e);
            return false;
        }
    }
    public boolean deactivateUser(int userId) {
        LOGGER.log(Level.INFO, "Dectivate user id: {0}", userId);
        
        String sql = "UPDATE Users SET isActive = 0 WHERE id = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Dectivate successfully for user id: {0}", userId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Dectivate failed: no rows affected for user id: {0}", userId);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deactivating user id: " + userId, e);
            return false;
        }
    }    
    
    public boolean deleteUser(int userId){
        LOGGER.log(Level.INFO,"Delete user with user id: {0}",userId);
        
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Delete user successfully with user id: {0}", userId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Failed to delete user with user id: {0}", userId);
                return false;
            }
        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"Error deleting user with userId: " + userId,e);
            return false;
        }        
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getNString("fullName"));
        user.setRole(rs.getNString("role"));
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            user.setCreatedAt(createdAt);
        }
        user.setIsActive(rs.getBoolean("isActive"));
        return user;
    }
}

