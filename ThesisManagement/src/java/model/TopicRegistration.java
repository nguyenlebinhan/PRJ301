package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Entity class cho bảng TopicRegistrations
 */
public class TopicRegistration {
    private int registrationId;
    private int topicId;
    private String topicCode;
    private String status; //AVAILABLE, PENDING, APPROVED, REJECTED
    private String mssv;
    private String mscvHD;
    private Date registeredAt;
    private Date processedAt;
    private Topic topic;
    private Student student;
    private Lecturer supervisor;

    public TopicRegistration() {
    }

    public TopicRegistration(int registrationId, int topicId, String topicCode, String status, String mssv, String mscvHD,  Date registeredAt, Date processedAt, Topic topic, Student student, Lecturer supervisor) {
        this.registrationId = registrationId;
        this.topicId = topicId;
        this.topicCode = topicCode;
        this.status = status;
        this.mssv = mssv;
        this.mscvHD = mscvHD;
        this.registeredAt = registeredAt;
        this.processedAt = processedAt;
        this.topic = topic;
        this.student = student;
        this.supervisor = supervisor;
    }



    public TopicRegistration(int topicId, String topicCode,String status, String mssv, String mscvHD) {
        this.topicId = topicId;
        this.topicCode = topicCode;
        this.status =status;
        this.mssv = mssv;
        this.mscvHD = mscvHD;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }
    
    


    
    public TopicRegistration(int topicId, String mssv) {
        this.topicId = topicId;
        this.mssv = mssv;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getMscvHD() {
        return mscvHD;
    }

    public void setMscvHD(String mscvHD) {
        this.mscvHD = mscvHD;
    }


    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Date getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Date processedAt) {
        this.processedAt = processedAt;
    }
    
    
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lecturer getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Lecturer supervisor) {
        this.supervisor = supervisor;
    }
}

