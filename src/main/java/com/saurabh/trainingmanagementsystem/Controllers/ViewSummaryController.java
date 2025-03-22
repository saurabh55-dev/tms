package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewSummaryController implements Initializable {

    public Text username_text;
    public Label login_date;
    public Label total_income;
    public Label total_expense;
    public Label total_fee;
    public ListView transaction_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fetch and display total income
        double totalIncome = fetchTotalIncome();
        total_income.setText("+ NPR. " + totalIncome);

        // Fetch and display total expense
        double totalExpense = fetchTotalExpense();
        total_expense.setText("- NPR. " + totalExpense);

        // Fetch and display all transactions
        ObservableList<String> transactions = fetchAllTransactions();
        transaction_listview.setItems(transactions);
    }

    private double fetchTotalIncome() {
        double totalIncome = 0.0;
        String query = "SELECT SUM(amount) AS total FROM income";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalIncome = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalIncome;
    }

    private double fetchTotalExpense() {
        double totalExpense = 0.0;
        String query = "SELECT SUM(amount) AS total FROM expense";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalExpense = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalExpense;
    }

    private ObservableList<String> fetchAllTransactions() {
        ObservableList<String> transactions = FXCollections.observableArrayList();

        // Fetch income records
        String incomeQuery = "SELECT topic, amount, date FROM income";
        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(incomeQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String description = rs.getString("topic");
                double amount = rs.getDouble("amount");
                Date date = rs.getDate("date");

                transactions.add("INCOME: " + description + " (+ NPR. " + amount + ") - " + date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Fetch expense records
        String expenseQuery = "SELECT topic, purpose, amount, date FROM expense";
        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(expenseQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String description = rs.getString("topic");
                String purpose = rs.getString("purpose");
                double amount = rs.getDouble("amount");
                Date date = rs.getDate("date");

                transactions.add("EXPENSE: " + description + " Purpose: "+ purpose +" (- NPR. " + amount + ") - " + date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}
