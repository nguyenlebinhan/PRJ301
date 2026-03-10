/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author ADMIN
 */
public class ThesisUpdateRequest {
    private int thesisId;
    private String reportFile; 
    private String sourceCodeLink;
    private double similarityScore;
    private String plagiarismStatus;
    private String bestSource;
    private String plagiarismAnalysis;
    private double relevantTopicScore;
    private String relevantTopicStatus;
    private String relevanceAnalysis;

    public ThesisUpdateRequest() {
    }

    public ThesisUpdateRequest(int thesisId, String reportFile, String sourceCodeLink, double similarityScore, String plagiarismStatus, String bestSource, String plagiarismAnalysis, double relevantTopicScore, String relevantTopicStatus, String relevanceAnalysis) {
        this.thesisId = thesisId;
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


    


    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
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
