package com.saurabh.trainingmanagementsystem.Models;

public class PaymentDue {
    private int studentId;
    private double dueAmount;

    public PaymentDue(int studentId, double dueAmount) {
        this.studentId = studentId;
        this.dueAmount = dueAmount;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public double getDueAmount() { return dueAmount; }
    public void setDueAmount(double dueAmount) { this.dueAmount = dueAmount; }
}