package dal;

import java.sql.*;
import java.util.logging.*;

/**
 * Database Initializer cho hệ thống Quản lý Đồ án Tốt nghiệp
 * Tạo tất cả các bảng cần thiết và dữ liệu mẫu
 */
public class DBInitializer {
    private static final Logger LOGGER = Logger.getLogger(DBInitializer.class.getName());
    private final DBContext dbContext;

    public DBInitializer() {
        this.dbContext = new DBContext();
    }

    // ========== CREATE TABLES ==========
    


    private void createUsersTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE Users ("
                + "id INT PRIMARY KEY IDENTITY(1,1), "
                + "username VARCHAR(50) NOT NULL UNIQUE, "
                + "password VARCHAR(255) NOT NULL, "
                + "email VARCHAR(100) NOT NULL UNIQUE, "
                + "fullName NVARCHAR(100), "
                + "role NVARCHAR(100) NOT NULL, "
                + "createdAt DATETIME DEFAULT GETDATE(), "
                + "isActive BIT DEFAULT 1) ";
        execute(conn, sql, "Users");
    }
    
    private void createAppointmentTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE Appointment ("
                + "appointmentId INT PRIMARY KEY IDENTITY(1,1), "
                + "mssv VARCHAR(20) NOT NULL, "
                + "mscv VARCHAR(20) NOT NULL, "
                + "thesisId INT, "
                + "purpose NVARCHAR(500),"
                + "meetingDate DATETIME NOT NULL, "
                + "location NVARCHAR(500), "
                + "status VARCHAR(50) DEFAULT 'Pending', "
                + "createdAt DATETIME DEFAULT GETDATE(), "
                + "FOREIGN KEY (mssv) REFERENCES Students(mssv) ON DELETE CASCADE,"
                + "FOREIGN KEY (mscv) REFERENCES Lecturers(mscv) ON DELETE NO ACTION,"
                + "FOREIGN KEY (thesisId) REFERENCES THESES(thesisId) ON DELETE NO ACTION "
                + ")";
        execute(conn, sql, "Appointment");
    }
    
    private void createStudentsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE Students ("
                + "mssv VARCHAR(20) PRIMARY KEY, "
                + "userId INT NOT NULL, "
                + "fullName NVARCHAR(100) NOT NULL, "
                + "className NVARCHAR(50), "
                + "major NVARCHAR(100), "
                + "gpa DECIMAL(3,2), "
                + "skills NVARCHAR(MAX), "
                + "email VARCHAR(100), "
                + "phone VARCHAR(20), "
                + "createdAt DATETIME DEFAULT GETDATE(), "
                + "FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE)";
        execute(conn, sql, "Students");
    }

    private void createLecturersTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE Lecturers ("
                + "mscv VARCHAR(20) PRIMARY KEY, "
                + "userId INT NOT NULL, "
                + "fullName NVARCHAR(100) NOT NULL, "
                + "academicTitle NVARCHAR(50), "
                + "researchField NVARCHAR(MAX), "
                + "maxStudents INT DEFAULT 5, "
                + "currentStudents INT DEFAULT 0, "
                + "email VARCHAR(100), "
                + "createdAt DATETIME DEFAULT GETDATE(), "
                + "FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE)";
        execute(conn, sql, "Lecturers");
    }

    private void createTopicsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE Topics ("
                + "topicId INT PRIMARY KEY IDENTITY(1,1), "
                + "topicCode VARCHAR(50) UNIQUE NOT NULL, "
                + "title NVARCHAR(500) NOT NULL, "
                + "description NVARCHAR(MAX), "
                + "technicalRequirements NVARCHAR(MAX), "
                + "status VARCHAR(20) DEFAULT 'AVAILABLE', "
                + "type VARCHAR(20) DEFAULT 'LECTURER_SUGGESTED', "
                + "createdBy VARCHAR(20), "
                + "createdAt DATETIME DEFAULT GETDATE(), "
                + "difficultyScore INT, "
                + "FOREIGN KEY (createdBy) REFERENCES Lecturers(mscv)) ";
        execute(conn, sql, "Topics");
    }

    private void createThesesTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE Theses ("
                + "thesisId INT PRIMARY KEY IDENTITY(1,1), "
                + "thesisCode AS ('TH_' + CAST(thesisId AS VARCHAR(10))) PERSISTED, "
                + "topicId INT NOT NULL, "
                + "mssv VARCHAR(20) NOT NULL, "
                + "mscvHD VARCHAR(20) NOT NULL, "
                // reportFile sẽ lưu URL nội bộ dẫn đến file PDF trong thư mục uploads
                + "reportFile NVARCHAR(500), " 
                + "sourceCodeLink NVARCHAR(500), "
                + "status VARCHAR(20) DEFAULT 'IN_PROGRESS', "
                + "registeredAt DATETIME DEFAULT GETDATE(), "
                + "similarityScore FLOAT DEFAULT 0, "        
                + "plagiarismStatus NVARCHAR(100), "        
                + "bestSource NVARCHAR(500), "  
                + "plagiarism_analysis NVARCHAR(MAX),"
                + "relevantTopicScore FLOAT DEFAULT 0,"
                + "relevantTopicStatus NVARCHAR(100), " 
                + "relevance_analysis NVARCHAR(MAX),"
                + "FOREIGN KEY (topicId) REFERENCES Topics(topicId), "
                + "FOREIGN KEY (mssv) REFERENCES Students(mssv), "
                + "FOREIGN KEY (mscvHD) REFERENCES Lecturers(mscv)) ";
        execute(conn, sql, "Theses");
    

    }

    private void createTopicRegistrationsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE TopicRegistrations ("
                + "registrationId INT PRIMARY KEY IDENTITY(1,1), "
                + "topicId INT NOT NULL, "
                + "topicCode VARCHAR(100) NOT NULL,"
                + "status VARCHAR(100) DEFAULT 'AVAILABLE',"
                + "mssv VARCHAR(20) NOT NULL, "
                + "mscvHD VARCHAR(20), "
                + "registeredAt DATETIME DEFAULT GETDATE(), "
                + "processedAt DATETIME, "
                + "FOREIGN KEY (topicId) REFERENCES Topics(topicId), "
                + "FOREIGN KEY (mssv) REFERENCES Students(mssv), "
                + "FOREIGN KEY (mscvHD) REFERENCES Lecturers(mscv))";
        execute(conn, sql, "TopicRegistrations");
    }
    
    private void createThesisHistory(Connection conn) throws SQLException{
        String sql = "CREATE TABLE ThesisHistory ("
                + "historyId INT PRIMARY KEY IDENTITY(1,1), "
                + "thesisId INT NOT NULL,"
                + "mssv VARCHAR(20) NOT NULL,"
                + "reportFile NVARCHAR(500), "
                + "sourceCodeLink NVARCHAR(500), "
                + "createdAt DATETIME DEFAULT GETDATE(), "
                + "similarityScore FLOAT DEFAULT 0, "        
                + "plagiarismStatus NVARCHAR(100), "         
                + "bestSource NVARCHAR(500), "
                + "plagiarism_analysis NVARCHAR(MAX),"
                + "relevantTopicScore FLOAT DEFAULT 0,"
                + "relevantTopicStatus NVARCHAR(100), " 
                + "relevance_analysis NVARCHAR(MAX),"
                + "FOREIGN KEY (mssv) REFERENCES Students(mssv), "                
                + "FOREIGN KEY (thesisId) REFERENCES Theses(thesisId)) ";
        execute (conn,sql,"ThesisHistory");
    }

    private void createResetTokensTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE ResetTokens ("
                + "tokenId INT PRIMARY KEY IDENTITY(1,1), "
                + "userId INT NOT NULL, "
                + "token VARCHAR(255) NOT NULL UNIQUE, "
                + "email VARCHAR(100) NOT NULL, "
                + "expiresAt DATETIME NOT NULL, "
                + "used BIT DEFAULT 0, "
                + "createdAt DATETIME DEFAULT GETDATE(), "
                + "FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE)";
        execute(conn, sql, "ResetTokens");
    }

    // ========== INITIALIZATION LOGIC ==========

    public void initializeDatabase(boolean enforceReset) {
        try (Connection conn = dbContext.getConnection()) {
            if (conn == null) {
                LOGGER.severe("Cannot get database connection!");
                return;
            }

            String[] dropOrder = {
                "Appointment",
                "ThesisHistory",     // Tham chiếu Theses                
                "TopicRegistrations",// Tham chiếu Topics, Students, Lecturers
                "ResetTokens",       // Tham chiếu Users
                "Theses",            // Tham chiếu Topics, Students, Lecturers
                "Topics",            // Tham chiếu Lecturers
                "Students",          // Tham chiếu Users
                "Lecturers",         // Tham chiếu Users
                "Users",             // Tham chiếu Roles
                
            };
            // Thứ tự CREATE (Từ bảng cha đến bảng phụ thuộc)
            String[] createOrder = {
                 "Users", "Students", "Lecturers", "Topics", 
                "Theses","ThesisHistory", 
                "Appointment", "TopicRegistrations", "ResetTokens"
            };

            if (enforceReset) {
                LOGGER.info("Enforce reset: Dropping all tables...");
                for (String table : dropOrder) {
                    dropTable(conn, table);
                }
            }

            for (String table : createOrder) {
                if (enforceReset || !tableExists(conn, table)) {
                    switch (table) {
                        case "Users": createUsersTable(conn); break;
                        case "Students": createStudentsTable(conn); break;
                        case "Lecturers": createLecturersTable(conn); break;
                        case "Topics": createTopicsTable(conn); break;
                        case "Theses": createThesesTable(conn); break;
                        case "ThesisHistory": createThesisHistory(conn);break;
                        case "Appointment" : createAppointmentTable(conn);break;
                        case "TopicRegistrations": createTopicRegistrationsTable(conn); break;
                        case "ResetTokens": createResetTokensTable(conn); break;
                    }
                }
            }
            
            insertInitialData(conn);
            LOGGER.info("Database initialization completed successfully!");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Initialization failed", e);
        }
    }

    // ========== INSERT INITIAL DATA ==========

    private void insertInitialData(Connection conn) {
        try {
            if (countRows(conn, "Users") > 0) return;
            LOGGER.info("Starting to seed initial data...");


            // 2. Users (Sửa roleId Admin thành 4)
            int adminId = insertUser(conn, "admin", "admin123", "admin@thesis.edu.vn", "Nguyễn Lê B", "ADMIN");
            int gv01UserId = insertUser(conn, "gv001", "gv001", "gv001@thesis.edu.vn", "Nguyễn Văn A", "LECTURER");
            int gv02UserId = insertUser(conn, "gv002", "gv002", "gv002@thesis.edu.vn", "Nguyễn Văn B", "LECTURER");
            int sv01UserId = insertUser(conn, "sv001", "sv001", "sv001@student.edu.vn", "Trần Thị B", "STUDENT");
            int sv02UserId = insertUser(conn, "sv002", "sv002", "sv002@student.edu.vn", "Trần Thị C", "STUDENT");

            // 3. Lecturers (Dùng addBatch để không mất GV01)
            String lecturerSql = "INSERT INTO Lecturers (mscv, userId, fullName, academicTitle, researchField, maxStudents, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(lecturerSql)) {
                // GV001
                ps.setString(1, "GV001"); ps.setInt(2, gv01UserId); ps.setNString(3, "Nguyễn Văn A");
                ps.setNString(4, "Tiến sĩ"); ps.setNString(5, "AI/ML"); ps.setInt(6, 5); ps.setString(7, "gv001@thesis.edu.vn");
                ps.addBatch();
                // GV002
                ps.setString(1, "GV002"); ps.setInt(2, gv02UserId); ps.setNString(3, "Nguyễn Văn B");
                ps.setNString(4, "Tiến sĩ"); ps.setNString(5, "Data Science"); ps.setInt(6, 5); ps.setString(7, "gv002@thesis.edu.vn");
                ps.addBatch();
                ps.executeBatch();
            }

            // 4. Students (Khớp 9 tham số)
            String studentSql = "INSERT INTO Students (mssv, userId, fullName, className, major, gpa, skills, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(studentSql)) {
                // SV001
                ps.setString(1, "SV001"); ps.setInt(2, sv01UserId); ps.setNString(3, "Trần Thị B");
                ps.setNString(4, "CNTT2021"); ps.setNString(5, "CNTT"); ps.setBigDecimal(6, new java.math.BigDecimal("3.5"));
                ps.setNString(7, "Java, SQL"); ps.setString(8, "sv001@student.edu.vn"); ps.setString(9, "0912345678");
                ps.addBatch();
                // SV002
                ps.setString(1, "SV002"); ps.setInt(2, sv02UserId); ps.setNString(3, "Trần Thị C");
                ps.setNString(4, "CNTT2021"); ps.setNString(5, "CNTT"); ps.setBigDecimal(6, new java.math.BigDecimal("3.2"));
                ps.setNString(7, "Python"); ps.setString(8, "sv002@student.edu.vn"); ps.setString(9, "0987654321");
                ps.addBatch();
                ps.executeBatch();
            }

            // 5. Topics (Khớp 8 tham số)
            String topicSql = "INSERT INTO Topics (topicCode, title, description, technicalRequirements, status, type, createdBy, difficultyScore) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(topicSql)) {
                ps.setString(1, "DT001"); ps.setNString(2, "AI Management"); ps.setNString(3, "Tạo dựng 1 hệ thống RAG "); ps.setNString(4, "Python");
                ps.setString(5, "AVAILABLE"); ps.setString(6, "LECTURER_SUGGESTED"); ps.setString(7, "GV001"); ps.setBigDecimal(8, new java.math.BigDecimal("4.5"));
                ps.addBatch();

                ps.setString(1, "DT002"); ps.setNString(2, "E-Commerce"); ps.setNString(3, "Tạo dựng 1 hệ thống trang thương mại điện tử"); ps.setNString(4, "Java");
                ps.setString(5, "AVAILABLE"); ps.setString(6, "LECTURER_SUGGESTED"); ps.setString(7, "GV001"); ps.setBigDecimal(8, new java.math.BigDecimal("1.0"));
                ps.addBatch();

                ps.setString(1, "DT003"); ps.setNString(2, "Banking"); ps.setNString(3, "Tạo dựng 1 hệ thống mô phỏng ngân hàng"); ps.setNString(4, "Java");
                ps.setString(5, "AVAILABLE"); ps.setString(6, "LECTURER_SUGGESTED"); ps.setString(7, "GV001"); ps.setBigDecimal(8, new java.math.BigDecimal("1.0"));
                ps.addBatch();

                ps.setString(1, "DT004"); ps.setNString(2, "Money_Management_Application"); ps.setNString(3, "Tạo dựng 1 hệ thống mô phỏng trang quản lí tài chính"); ps.setNString(4, "Java");
                ps.setString(5, "AVAILABLE"); ps.setString(6, "LECTURER_SUGGESTED"); ps.setString(7, "GV001"); ps.setBigDecimal(8, new java.math.BigDecimal("1.0"));
                ps.addBatch();                
                ps.executeBatch();
            }

            LOGGER.info("Seeding completed successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Seeding failed", e);
        }
    }

    // Hàm Helper để code sạch hơn
    private int insertUser(Connection conn, String u, String p, String e, String f, String r) throws SQLException {
        String sql = "INSERT INTO Users (username, password, email, fullName, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u); ps.setString(2, p); ps.setString(3, e); ps.setNString(4, f); ps.setString(5, r);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    // ========== HELPER METHODS ==========

    private void execute(Connection conn, String sql, String label) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.info("Executed: " + label);
        }
    }

    private void dropTable(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            // SQL Server doesn't support DROP TABLE IF EXISTS, so we use try-catch
            stmt.execute("DROP TABLE " + tableName);
            LOGGER.info("Dropped table: " + tableName);
        } catch (SQLException e) {
            LOGGER.warning("Could not drop " + tableName + ": " + e.getMessage());
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private int countRows(Connection conn, String tableName) throws SQLException {
        try (Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public static void main(String[] args) {
        DBInitializer initializer = new DBInitializer();
        initializer.initializeDatabase(true);
    }
}
