package com.saurabh.trainingmanagementsystem.Models;

import java.sql.Date;

public class Expense {
    private int expenseId;
    private String description;
    private double amount;
    private Date date;

    public Expense(int expenseId, String description, double amount, Date date) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    // Getters and Setters
    public int getExpenseId() { return expenseId; }
    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}