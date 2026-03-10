/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class ThesisHistoryResponse {
    private int historyId;
    private int thesisId;
    private String mssv;
    private String reportFile;
    private String sourceCodeLink;
    private LocalDateTime createdAt;
    private double similarityScore;
    private String plagiarismStatus;
    private String bestSource;
    private String plagiarismAnalysis;
    private double relevantTopicScore;
    private String relevantTopicStatus;
    private String relevanceAnalysis;
    private String title;
    private String topicCode;

    public ThesisHistoryResponse() {
    }

    public ThesisHistoryResponse(int historyId, int thesisId, String mssv, String reportFile, String sourceCodeLink, LocalDateTime createdAt, String title, String topicCode) {
        this.historyId = historyId;
        this.thesisId = thesisId;
        this.mssv = mssv;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.createdAt = createdAt;
        this.title = title;
        this.topicCode = topicCode;
    }

    public ThesisHistoryResponse(int historyId, int thesisId, String mssv, String reportFile, String sourceCodeLink, LocalDateTime createdAt) {
        this.historyId = historyId;
        this.thesisId = thesisId;
        this.mssv = mssv;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.createdAt = createdAt;
    }

    public ThesisHistoryResponse(int historyId, int thesisId, String mssv, String reportFile, String sourceCodeLink, LocalDateTime createdAt, double similarityScore, String plagiarismStatus, String bestSource, String plagiarismAnalysis, double relevantTopicScore, String relevantTopicStatus, String relevanceAnalysis, String title, String topicCode) {
        this.historyId = historyId;
        this.thesisId = thesisId;
        this.mssv = mssv;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.createdAt = createdAt;
        this.similarityScore = similarityScore;
        this.plagiarismStatus = plagiarismStatus;
        this.bestSource = bestSource;
        this.plagiarismAnalysis = plagiarismAnalysis;
        this.relevantTopicScore = relevantTopicScore;
        this.relevantTopicStatus = relevantTopicStatus;
        this.relevanceAnalysis = relevanceAnalysis;
        this.title = title;
        this.topicCode = topicCode;
    }



    
    
    

    public ThesisHistoryResponse(String title, String topicCode) {
        this.title = title;
        this.topicCode = topicCode;
    }
    
    

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String getPlagiarismStatus() {
        return plagiarismStatus;
    }

    public void setPlagiarismStatus(String plagiarismStatus) {
        this.plagiarismStatus = plagiarismStatus;
    }

    public String getBestSource() {
        return bestSource;
    }

    public void setBestSource(String bestSource) {
        this.bestSource = bestSource;
    }

    public String getPlagiarismAnalysis() {
        return plagiarismAnalysis;
    }

    public void setPlagiarismAnalysis(String plagiarismAnalysis) {
        this.plagiarismAnalysis = plagiarismAnalysis;
    }

    public double getRelevantTopicScore() {
        return relevantTopicScore;
    }

    public void setRelevantTopicScore(double relevantTopicScore) {
        this.relevantTopicScore = relevantTopicScore;
    }

    public String getRelevantTopicStatus() {
        return relevantTopicStatus;
    }

    public void setRelevantTopicStatus(String relevantTopicStatus) {
        this.relevantTopicStatus = relevantTopicStatus;
    }

    public String getRelevanceAnalysis() {
        return relevanceAnalysis;
    }

    public void setRelevanceAnalysis(String relevanceAnalysis) {
        this.relevanceAnalysis = relevanceAnalysis;
    }

    
    
    
    
    
    
}
