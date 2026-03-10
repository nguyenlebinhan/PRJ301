package model;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Entity class cho bảng Lecturers
 */
public class Lecturer {
    private String mscv;
    private int userId;
    private String fullName;
    private String academicTitle;
    private String researchField;
    private int maxStudents;
    private int currentStudents;
    private String email;
    private Date createdAt;
    private User user;

    public Lecturer() {
    }

    public Lecturer(String mscv, int userId, String fullName, String academicTitle, String researchField, int maxStudents, String email) {
        this.mscv = mscv;
        this.userId = userId;
        this.fullName = fullName;
        this.academicTitle = academicTitle;
        this.researchField = researchField;
        this.maxStudents = maxStudents;
        this.email = email;
    }

    public String getMscv() {
        return mscv;
    }

    public void setMscv(String mscv) {
        this.mscv = mscv;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getResearchField() {
        return researchField;
    }

    public void setResearchField(String researchField) {
        this.researchField = researchField;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(int currentStudents) {
        this.currentStudents = currentStudents;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

