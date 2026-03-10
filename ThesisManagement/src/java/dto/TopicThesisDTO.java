/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author ADMIN
 */
public class TopicThesisDTO {
    private int topicId;
    private String topicCode;
    private String mssv;
    private String topicTitle;
    private String description;
    private String technicalRequirements;
    private BigDecimal difficultyScore;
    private Integer thesisId;
    private String reportFile;
    private String sourceCodeLink;
    

    public TopicThesisDTO() {
    }

    public TopicThesisDTO(int topicId, String topicCode, String description, String technicalRequirements, BigDecimal difficultyScore) {
        this.topicId = topicId;
        this.topicCode = topicCode;
        this.description = description;
        this.technicalRequirements = technicalRequirements;
        this.difficultyScore = difficultyScore;
    }

    public TopicThesisDTO(int topicId, String topicCode, String topicTitle, String description, String technicalRequirements, BigDecimal difficultyScore) {
        this.topicId = topicId;
        this.topicCode = topicCode;
        this.topicTitle = topicTitle;
        this.description = description;
        this.technicalRequirements = technicalRequirements;
        this.difficultyScore = difficultyScore;
    }

    public TopicThesisDTO(int topicId, String topicCode, String topicTitle, String description, String technicalRequirements, BigDecimal difficultyScore, Integer thesisId) {
        this.topicId = topicId;
        this.topicCode = topicCode;
        this.topicTitle = topicTitle;
        this.description = description;
        this.technicalRequirements = technicalRequirements;
        this.difficultyScore = difficultyScore;
        this.thesisId = thesisId;
    }

    public TopicThesisDTO(int topicId, String topicCode, String mssv, String topicTitle, String description, String technicalRequirements, BigDecimal difficultyScore, Integer thesisId, String reportFile, String sourceCodeLink) {
        this.topicId = topicId;
        this.topicCode = topicCode;
        this.mssv = mssv;
        this.topicTitle = topicTitle;
        this.description = description;
        this.technicalRequirements = technicalRequirements;
        this.difficultyScore = difficultyScore;
        this.thesisId = thesisId;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
    }


    public Integer getThesisId() {
        return thesisId;
    }

    public void setThesisId(Integer thesisId) {
        this.thesisId = thesisId;
    }

    
    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
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

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
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

    public BigDecimal getDifficultyScore() {
        return difficultyScore;
    }

    public void setDifficultyScore(BigDecimal difficultyScore) {
        this.difficultyScore = difficultyScore;
    }

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public String getSourceCodeLink() {
        return sourceCodeLink;
    }

    public void setSourceCodeLink(String sourceCodeLink) {
        this.sourceCodeLink = sourceCodeLink;
    }
    
    
    
}
