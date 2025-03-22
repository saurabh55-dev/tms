package com.saurabh.trainingmanagementsystem.Models;

import javafx.beans.property.SimpleStringProperty;

public class AttendanceReport {
    private SimpleStringProperty studentName;
    private SimpleStringProperty className;
    private SimpleStringProperty presentDayAttendance;
    private SimpleStringProperty monthlyAttendance;
    private SimpleStringProperty totalAttendance;

    public AttendanceReport(String studentName, String className, String presentDayAttendance, String monthlyAttendance, String totalAttendance) {
        this.studentName = new SimpleStringProperty(studentName);
        this.className = new SimpleStringProperty(className);
        this.presentDayAttendance = new SimpleStringProperty(presentDayAttendance);
        this.monthlyAttendance = new SimpleStringProperty(monthlyAttendance);
        this.totalAttendance = new SimpleStringProperty(totalAttendance);
    }

    // Getters and Setters
    public String getStudentName() {
        return studentName.get();
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public String getClassName() {
        return className.get();
    }

    public void setClassName(String className) {
        this.className.set(className);
    }

    public String getPresentDayAttendance() {
        return presentDayAttendance.get();
    }

    public void setPresentDayAttendance(String presentDayAttendance) {
        this.presentDayAttendance.set(presentDayAttendance);
    }

    public String getMonthlyAttendance() {
        return monthlyAttendance.get();
    }

    public void setMonthlyAttendance(String monthlyAttendance) {
        this.monthlyAttendance.set(monthlyAttendance);
    }

    public String getTotalAttendance() {
        return totalAttendance.get();
    }

    public void setTotalAttendance(String totalAttendance) {
        this.totalAttendance.set(totalAttendance);
    }
}