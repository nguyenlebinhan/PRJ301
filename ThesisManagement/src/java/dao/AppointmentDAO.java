/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dal.DBContext;
import dto.AppointmentResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;

/**
 *
 * @author ADMIN
 */
public class AppointmentDAO {
    private static final Logger LOGGER = Logger.getLogger(LecturerDAO.class.getName());
    private final DBContext dbContext = new DBContext();
    
    public boolean createAppointment(Appointment appointment) {
        LOGGER.log(Level.INFO, "Creating appointment: mssv={0}, mscv={1}, purpose={2}", 
                new Object[]{appointment.getMssv(), appointment.getMscv(), appointment.getPurpose()});
        
        String sql = "INSERT INTO Appointment (mssv, mscv, thesisId, purpose, meetingDate, location) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, appointment.getMssv());
            ps.setString(2, appointment.getMscv());
            ps.setInt(3, appointment.getThesisId());
            ps.setNString(4, appointment.getPurpose());
            ps.setTimestamp(5, appointment.getMeetingDate());
            ps.setNString(6,appointment.getLocation());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Appointment created successfully");
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Appointment creation failed: no rows affected");
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating appointment", e);
            return false;
        }
    }
    
    public List<AppointmentResponse> getAppointmentByMscv(String mscv){    
        List<AppointmentResponse> appointments = new ArrayList<>();
        String sql = "select a.appointmentId,s.fullName,a.mssv,a.mscv,tp.title,a.purpose,a.meetingDate,a.location,a.createdAt from Appointment a inner join Theses t on t.thesisId = a.thesisId inner join Topics tp on tp.topicId = t.topicId inner join Students s on s.mssv =a.mssv where mscv = ? and a.status ='PENDING'";
        try(Connection conn = dbContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, mscv);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    AppointmentResponse a= new AppointmentResponse(rs.getInt("appointmentId"),rs.getNString("fullName"),rs.getString("mssv"),rs.getString("mscv"),rs.getNString("title"),rs.getNString("purpose"),rs.getTimestamp("meetingDate"),rs.getNString("location"),rs.getTimestamp("createdAt"));
                    appointments.add(a);
                }
            }
        }catch(SQLException e){
            LOGGER.log(Level.SEVERE,"Error getting appointment by mscv: "+ mscv,e);
        }
        return appointments;
    }

        
    
}
