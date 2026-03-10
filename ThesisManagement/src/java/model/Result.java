package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class cho bảng Results
 */
public class Result {
    private int resultId;
    private int thesisId;
    private BigDecimal supervisorScore;
    private BigDecimal reviewerScore;
    private BigDecimal councilScore;
    private BigDecimal finalScore;
    private String generalComment;
    private LocalDateTime gradedAt;
    private Integer gradedBy;
    private Thesis thesis;
    private User gradedByUser;

    public Result() {
    }

    public Result(int thesisId) {
        this.thesisId = thesisId;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public BigDecimal getSupervisorScore() {
        return supervisorScore;
    }

    public void setSupervisorScore(BigDecimal supervisorScore) {
        this.supervisorScore = supervisorScore;
    }

    public BigDecimal getReviewerScore() {
        return reviewerScore;
    }

    public void setReviewerScore(BigDecimal reviewerScore) {
        this.reviewerScore = reviewerScore;
    }

    public BigDecimal getCouncilScore() {
        return councilScore;
    }

    public void setCouncilScore(BigDecimal councilScore) {
        this.councilScore = councilScore;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public Integer getGradedBy() {
        return gradedBy;
    }

    public void setGradedBy(Integer gradedBy) {
        this.gradedBy = gradedBy;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    public User getGradedByUser() {
        return gradedByUser;
    }

    public void setGradedByUser(User gradedByUser) {
        this.gradedByUser = gradedByUser;
    }
}

