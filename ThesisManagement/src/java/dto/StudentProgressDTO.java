/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author ADMIN
 */
public class StudentProgressDTO {
    private String fullName;
    private String mssv;
    private int thesisId;
    private String reportFile;
    private String sourceCodeLink;
    private int similarityScore;
    private String plagiarismStatus;
    private String bestSource;
    private double relevantTopicScore;
    private String relevantTopicStatus;

    public StudentProgressDTO() {
    }

    public StudentProgressDTO(String fullName, String mssv, int thesisId, String reportFile, String sourceCodeLink, int similarityScore, String plagiarismStatus, String bestSource, double relevantTopicScore, String relevantTopicStatus) {
        this.fullName = fullName;
        this.mssv = mssv;
        this.thesisId = thesisId;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.similarityScore = similarityScore;
        this.plagiarismStatus = plagiarismStatus;
        this.bestSource = bestSource;
        this.relevantTopicScore = relevantTopicScore;
        this.relevantTopicStatus = relevantTopicStatus;
    }





    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public int getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(int similarityScore) {
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

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }
    
}
