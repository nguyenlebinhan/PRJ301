/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author ADMIN
 */
public class StudentResponse {
    private String mssv;
    private String fullName;
    private String email;
    private BigDecimal gpa;
    private String phone;
    private String major;
    private String className;
    private String skills;
    

    public StudentResponse() {
    }

    public StudentResponse(String mssv, String fullName, String email, BigDecimal gpa, String phone, String major, String className, String skills) {
        this.mssv = mssv;
        this.fullName = fullName;
        this.email = email;
        this.gpa = gpa;
        this.phone = phone;
        this.major = major;
        this.className = className;
        this.skills = skills;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    
    
}
