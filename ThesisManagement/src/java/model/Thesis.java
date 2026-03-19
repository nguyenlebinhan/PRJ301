package model;

import java.util.Date;


public class Thesis {
    private int thesisId;
    private String thesisCode;
    private int topicId;
    private String mssv;
    private String mscvHD;
    private String reportFile;
    private String sourceCodeLink;
    private String status; // REGISTERED, IN_PROGRESS, SUBMITTED, DEFENDED
    private Date registeredAt;
    private double similarityScore;
    private String plagiarismStatus;
    private String bestSource;
    private String plagiarismAnalysis;
    private double relevantTopicScore;
    private String relevantTopicStatus;
    private String relevanceAnalysis;
    private double score;
    private String feedback;

    public Thesis() {
    }

    public Thesis(int thesisId, String mssv, String reportFile, String sourceCodeLink, double similarityScore, String plagiarismStatus, String bestSource, String plagiarismAnalysis, double relevantTopicScore, String relevantTopicStatus, String relevanceAnalysis) {
        this.thesisId = thesisId;
        this.mssv = mssv;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.similarityScore = similarityScore;
        this.plagiarismStatus = plagiarismStatus;
        this.bestSource = bestSource;
        this.plagiarismAnalysis = plagiarismAnalysis;
        this.relevantTopicScore = relevantTopicScore;
        this.relevantTopicStatus = relevantTopicStatus;
        this.relevanceAnalysis = relevanceAnalysis;
    }

    

    
    
    public Thesis(int thesisId, String thesisCode, int topicId, String mssv, String mscvHD, String reportFile, String sourceCodeLink, String status, Date registeredAt) {
        this.thesisId = thesisId;
        this.thesisCode = thesisCode;
        this.topicId = topicId;
        this.mssv = mssv;
        this.mscvHD = mscvHD;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.status = status;
        this.registeredAt = registeredAt;
    }

    public Thesis(int thesisId, String thesisCode, int topicId, String mssv, String mscvHD, String reportFile, String sourceCodeLink, String status, Date registeredAt, double similarityScore, String plagiarismStatus, String bestSource, String plagiarismAnalysis, double relevantTopicScore, String relevantTopicStatus, String relevanceAnalysis, double score, String feedback) {
        this.thesisId = thesisId;
        this.thesisCode = thesisCode;
        this.topicId = topicId;
        this.mssv = mssv;
        this.mscvHD = mscvHD;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.status = status;
        this.registeredAt = registeredAt;
        this.similarityScore = similarityScore;
        this.plagiarismStatus = plagiarismStatus;
        this.bestSource = bestSource;
        this.plagiarismAnalysis = plagiarismAnalysis;
        this.relevantTopicScore = relevantTopicScore;
        this.relevantTopicStatus = relevantTopicStatus;
        this.relevanceAnalysis = relevanceAnalysis;
        this.score = score;
        this.feedback = feedback;
    }

    public Thesis(int topicId, String mssv, String mscvHD, String reportFile, String sourceCodeLink, String status) {
        this.topicId = topicId;
        this.mssv = mssv;
        this.mscvHD = mscvHD;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.status = status;
    }

    

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public String getThesisCode() {
        return thesisCode;
    }

    public void setThesisCode(String thesisCode) {
        this.thesisCode = thesisCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    
    

}

