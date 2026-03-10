package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class cho bảng Councils
 */
public class Council {
    private int councilId;
    private String councilCode;
    private String councilName;
    private LocalDateTime defenseDate;
    private String location;
    private String chairman;
    private String secretary;
    private String members; // JSON string hoặc comma-separated
    private String status; // PLANNED, SCHEDULED, COMPLETED, CANCELLED
    private LocalDateTime createdAt;
    private List<Thesis> theses;
    private Lecturer chairmanLecturer;
    private Lecturer secretaryLecturer;

    public Council() {
    }

    public Council(String councilCode, String councilName, LocalDateTime defenseDate, String location) {
        this.councilCode = councilCode;
        this.councilName = councilName;
        this.defenseDate = defenseDate;
        this.location = location;
    }

    public int getCouncilId() {
        return councilId;
    }

    public void setCouncilId(int councilId) {
        this.councilId = councilId;
    }

    public String getCouncilCode() {
        return councilCode;
    }

    public void setCouncilCode(String councilCode) {
        this.councilCode = councilCode;
    }

    public String getCouncilName() {
        return councilName;
    }

    public void setCouncilName(String councilName) {
        this.councilName = councilName;
    }

    public LocalDateTime getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDateTime defenseDate) {
        this.defenseDate = defenseDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getChairman() {
        return chairman;
    }

    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    public String getSecretary() {
        return secretary;
    }

    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Thesis> getTheses() {
        return theses;
    }

    public void setTheses(List<Thesis> theses) {
        this.theses = theses;
    }

    public Lecturer getChairmanLecturer() {
        return chairmanLecturer;
    }

    public void setChairmanLecturer(Lecturer chairmanLecturer) {
        this.chairmanLecturer = chairmanLecturer;
    }

    public Lecturer getSecretaryLecturer() {
        return secretaryLecturer;
    }

    public void setSecretaryLecturer(Lecturer secretaryLecturer) {
        this.secretaryLecturer = secretaryLecturer;
    }
}

