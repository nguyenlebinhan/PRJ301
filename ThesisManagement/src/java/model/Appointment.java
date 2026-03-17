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
    private Date appointmentDate;
    private String location;    
    private Timestamp meetingDate;
    private String description;
    private String status;    
    private String mssv;
    private String mscv;
    private int thesisId;
    private Timestamp createdAt;

    public Appointment() {
    }

    public Appointment(int appointmentId, Date appointmentDate, String location, Timestamp meetingDate, String description, String status, String mssv, String mscv, int thesisId, Timestamp createdAt) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.location = location;
        this.meetingDate = meetingDate;
        this.description = description;
        this.status = status;
        this.mssv = mssv;
        this.mscv = mscv;
        this.thesisId = thesisId;
        this.createdAt = createdAt;
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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
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

}
