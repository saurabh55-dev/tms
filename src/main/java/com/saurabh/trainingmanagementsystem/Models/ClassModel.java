package com.saurabh.trainingmanagementsystem.Models;

import java.time.LocalTime;

public class ClassModel {
    private int classId;
    private int courseId;
    private String courseName;
    private String startTime;
    private String endTime;
    private int enrolledStudents;

    public ClassModel() {
    }

    public ClassModel(int classId, int courseId, String courseName, String startTime, String endTime, int enrolledStudents) {
        this.classId = classId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enrolledStudents = enrolledStudents;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
