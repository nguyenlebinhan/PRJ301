/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class ThesisHistory {
    private int historyId;
    private int thesisId;
    private String mssv;
    private String reportFile;
    private String sourceCodeLink;
    private Date createdAt;

    public ThesisHistory() {
    }

    public ThesisHistory(int historyId, int thesisId, String mssv, String reportFile, String sourceCodeLink, Date createdAt) {
        this.historyId = historyId;
        this.thesisId = thesisId;
        this.mssv = mssv;
        this.reportFile = reportFile;
        this.sourceCodeLink = sourceCodeLink;
        this.createdAt = createdAt;
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

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }
    

    public String getSourceCodeLink() {
        return sourceCodeLink;
    }

    public void setSourceCodeLink(String sourceCodeLink) {
        this.sourceCodeLink = sourceCodeLink;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    
    
    
}
