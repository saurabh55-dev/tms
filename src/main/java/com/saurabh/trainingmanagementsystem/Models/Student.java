package com.saurabh.trainingmanagementsystem.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Student {
    private int studentId;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String classDetails;
    private BooleanProperty attendance;

    public Student() {
        this.attendance = new SimpleBooleanProperty(false);
    }

    public Student(int studentId, String name, String email, String address, String phone, String classDetails) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.classDetails = classDetails;
        this.attendance = new SimpleBooleanProperty(false);
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClassDetails() {
        return classDetails;
    }

    public void setClassDetails(String classDetails) {
        this.classDetails = classDetails;
    }
    public boolean isAttendance() {
        return attendance.get();
    }

    public void setAttendance(boolean attendance) {
        this.attendance.set(attendance);
    }

    public BooleanProperty attendanceProperty() {
        return attendance;
    }

    public String getDummy(){
        return "";
    }
}
