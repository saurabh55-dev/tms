package com.saurabh.trainingmanagementsystem.Models;

import java.sql.Date;

public class Income {
    private int incomeId;
    private String description;
    private double amount;
    private Date date;

    public Income(int incomeId, String description, double amount, Date date) {
        this.incomeId = incomeId;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    // Getters and Setters
    public int getIncomeId() { return incomeId; }
    public void setIncomeId(int incomeId) { this.incomeId = incomeId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}