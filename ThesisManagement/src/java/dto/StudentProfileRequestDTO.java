/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import model.User;

/**
 *
 * @author ADMIN
 */
public class StudentProfileRequestDTO {
    private String mssv;
    private String fullName;
    private String className;
    private String major;
    private String skills;
    private String email;
    private String phone;

    public StudentProfileRequestDTO() {
    }

    public StudentProfileRequestDTO(String mssv, String fullName, String className, String major, String skills, String email, String phone) {
        this.mssv = mssv;
        this.fullName = fullName;
        this.className = className;
        this.major = major;
        this.skills = skills;
        this.email = email;
        this.phone = phone;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
}
