/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.logging.*;
import java.sql.*;
import utils.ConfigManager;
/**
 *
 * @author ADMIN
 */
public class DBContext {
    private static final Logger LOGGER = Logger.getLogger(DBContext.class.getName());
    
    private ConfigManager configManager;

    public DBContext() {
        configManager = ConfigManager.getInstance();
        LOGGER.log(Level.INFO,"DBContext instance created");
    }
    
    public Connection getConnection() throws SQLException{
        Connection conn = null;
        try{
            String url = configManager.getProperty("db.url");
            String username = configManager.getProperty("db.username");
            String password = configManager.getProperty("db.password");
            
            if(url == null || username == null || password == null){
                LOGGER.log(Level.SEVERE,"Missing database configuration in .env file");
                throw new RuntimeException("Missing database configuration information in .env file");
                
            }
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url,username,password);
            return conn;
        }catch(ClassNotFoundException e){
            LOGGER.log(Level.SEVERE,"SQL Server JDBC Driver not found",e);
            throw new SQLException("SQL Server JDBC Driver does not exist",e);
        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"Error connecting to database: "+e.getMessage(),e);
            if(conn != null){
                try{
                    conn.close();
                }catch(SQLException ex){
                    LOGGER.log(Level.SEVERE,"Error closing connection after failed acquisition.",ex);
                }
            }
            throw e;
        }
    }
    
    public void closeConnection(){
        LOGGER.log(Level.INFO,"closeConnection() called in DBContext. No shared connection to close here");
    }
    
    
    
    
            
}
