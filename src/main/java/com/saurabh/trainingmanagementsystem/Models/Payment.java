package com.saurabh.trainingmanagementsystem.Models;

import java.sql.Date;

public class Payment {
    private int paymentId;
    private int studentId;
    private int courseId;
    private double amount;
    private String paymentMethod;
    private Date paymentDate;

    public Payment(int paymentId, int studentId, int courseId, double amount, String paymentMethod, Date paymentDate) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
}