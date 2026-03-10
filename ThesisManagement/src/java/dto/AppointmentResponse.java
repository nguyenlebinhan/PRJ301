/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;


import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @author ADMIN
 */
public class AppointmentResponse {
    private int appointmentId;
    private String fullName;
    private String mssv;
    private String mscv;
    private String title;
    private String purpose;
    private Date meetingDate;
    private String location;
    private Timestamp createdAt;

    public AppointmentResponse() {
    }

    public AppointmentResponse(int appointmentId, String fullName, String mssv, String mscv, String title, String purpose, Date meetingDate, String location, Timestamp createdAt) {
        this.appointmentId = appointmentId;
        this.fullName = fullName;
        this.mssv = mssv;
        this.mscv = mscv;
        this.title = title;
        this.purpose = purpose;
        this.meetingDate = meetingDate;
        this.location = location;
        this.createdAt = createdAt;
    }



    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getMscv() {
        return mscv;
    }

    public void setMscv(String mscv) {
        this.mscv = mscv;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
    
}
