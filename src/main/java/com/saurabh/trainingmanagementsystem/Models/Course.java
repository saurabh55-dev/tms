package com.saurabh.trainingmanagementsystem.Models;

public class Course {
    private int courseID;
    private String courseName;
    private String duration;
    private int fees;
    private int enrolledStudents;

    public Course(int courseID, String courseName, String duration, int fees, int enrolledStudents) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.duration = duration;
        this.fees = fees;
        this.enrolledStudents = enrolledStudents;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public String getDummy() {
        return "";
    }

}
