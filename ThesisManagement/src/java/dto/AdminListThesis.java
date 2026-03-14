/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author ADMIN
 */
public class AdminListThesis {
    
    private String fullName;
    private String mssv;
    private String className;
    private String title;
    private String lecturerName;
    private String reportFile;
    private String sourceCodeLink;
    private int similarityScore;
    private String plagiarismStatus;
    private String bestSource;
    private double relevantTopicScore;
    private String relevantTopicStatus;    

    public AdminListThesis() {
    }

    public AdminListThesis(String fullName, String mssv, String className, String title, String lecturerName, String reportFile, String sourceCodeLink, int similarityScore, String plagiarismStatus, String bestSource, double relevantTopicScore, String relevantTopicStatus) {
        this.fullName = fullName;
        this.mssv = mssv;
        this.className = className;
        this.title = title;
        this.lecturerName = lecturerName;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.similarityScore = similarityScore;
        this.plagiarismStatus = plagiarismStatus;
        this.bestSource = bestSource;
        this.relevantTopicScore = relevantTopicScore;
        this.relevantTopicStatus = relevantTopicStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
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
    

    
}
