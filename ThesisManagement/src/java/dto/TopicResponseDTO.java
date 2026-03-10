/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;
import java.sql.*;
/**
 *
 * @author ADMIN
 */
public class TopicResponseDTO {
    private String studentName;
    private String mssv;
    private String topicTitle;
    private Timestamp registeredAt;
    private int topicId; 

    public TopicResponseDTO() {
    }

    public TopicResponseDTO(String studentName, String mssv, String topicTitle, Timestamp registeredAt, int topicId) {
        this.studentName = studentName;
        this.mssv = mssv;
        this.topicTitle = topicTitle;
        this.registeredAt = registeredAt;
        this.topicId = topicId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
    
    
    
}
