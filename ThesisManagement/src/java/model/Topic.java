package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Entity class cho bảng Topics
 */
public class Topic {
    private int topicId;
    private String topicCode;
    private String title;
    private String description;
    private String technicalRequirements;
    private String status; // AVAILABLE, END
    private String type; // LECTURER_SUGGESTED, STUDENT_SUGGESTED
    private String createdBy;
    private Date createdAt;
    private Integer approvedBy;
    private BigDecimal difficultyScore;
    private Lecturer lecturer;

    public Topic() {
    }

    public Topic(int topicId, String topicCode, String title, String description, String technicalRequirements, String status, String type, String createdBy, Date createdAt, Integer approvedBy, BigDecimal difficultyScore, Lecturer lecturer) {
        this.topicId = topicId;
        this.topicCode = topicCode;
        this.title = title;
        this.description = description;
        this.technicalRequirements = technicalRequirements;
        this.status = status;
        this.type = type;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.approvedBy = approvedBy;
        this.difficultyScore = difficultyScore;
        this.lecturer = lecturer;
    }




    public Topic(String topicCode, String title, String description, String technicalRequirements, String status, String type, String createdBy, BigDecimal difficultyScore) {
        this.topicCode = topicCode;
        this.title = title;
        this.description = description;
        this.technicalRequirements = technicalRequirements;
        this.status = status;
        this.type = type;
        this.createdBy = createdBy;
        this.difficultyScore = difficultyScore;
    }
    
    

    public Topic(String topicCode, String title, String description, String technicalRequirements, String status, String type, String createdBy) {
        this.topicCode = topicCode;
        this.title = title;
        this.description = description;
        this.technicalRequirements = technicalRequirements;
        this.status = status;
        this.type = type;
        this.createdBy = createdBy;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnicalRequirements() {
        return technicalRequirements;
    }

    public void setTechnicalRequirements(String technicalRequirements) {
        this.technicalRequirements = technicalRequirements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }


    public BigDecimal getDifficultyScore() {
        return difficultyScore;
    }

    public void setDifficultyScore(BigDecimal difficultyScore) {
        this.difficultyScore = difficultyScore;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}

