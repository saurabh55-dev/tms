package com.saurabh.trainingmanagementsystem.Models;

import java.sql.Date;

public class PaymentHistory {
    private String studentName;
    private String courseName;
    private Date paymentDate;
    private String paymentMethod;
    private int amount;
    private int dueAmount;

    public PaymentHistory(String studentName, String courseName, Date paymentDate, String paymentMethod, int amount, int dueAmount) {
        this.studentName = studentName;
        this.courseName = courseName;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.dueAmount = dueAmount;
    }

    // Getters and Setters
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public int getDueAmount() { return dueAmount; }
    public void setDueAmount(int dueAmount) { this.dueAmount = dueAmount; }
}