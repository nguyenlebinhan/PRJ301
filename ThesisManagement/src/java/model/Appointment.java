/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.*;

/**
 *
 * @author ADMIN
 */
public class Appointment {
    private int appointmentId;
    private String mssv;
    private String mscv;
    private int thesisId;
    private String purpose;
    private Timestamp meetingDate;
    private String location;
    private String status;
    private Date createdAt;

    public Appointment() {
    }

    public Appointment(int appointmentId, String mssv, String mscv, int thesisId, String purpose, Timestamp meetingDate, String location, String status, Date createdAt) {
        this.appointmentId = appointmentId;
        this.mssv = mssv;
        this.mscv = mscv;
        this.thesisId = thesisId;
        this.purpose = purpose;
        this.meetingDate = meetingDate;
        this.location = location;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Appointment(String mssv, String mscv, String purpose, Timestamp meetingDate, String location, String status) {
        this.mssv = mssv;
        this.mscv = mscv;
        this.purpose = purpose;
        this.meetingDate = meetingDate;
        this.location = location;
        this.status = status;
    }

    public Appointment(String mssv, String mscv, int thesisId, String purpose, Timestamp meetingDate, String location) {
        this.mssv = mssv;
        this.mscv = mscv;
        this.thesisId = thesisId;
        this.purpose = purpose;
        this.meetingDate = meetingDate;
        this.location = location;
    }
    
    

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Timestamp getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Timestamp meetingDate) {
        this.meetingDate = meetingDate;
    }
    
    


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
}
