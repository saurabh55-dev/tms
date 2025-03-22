package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    public Label date_lbl;
    public Label total_students;
    public Label total_classes;
    public Label total_pending_fees;
    public Label total_collected_fees;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboardData();
    }

    public void loadDashboardData() {
        // Step 1: Display today's date
        LocalDate today = LocalDate.now();
        date_lbl.setText("Today, " + today);  // or format it nicely if you want

        // Step 2: Fetch data from database
        String totalStudentsQuery = "SELECT COUNT(*) FROM student";
        String totalClassesQuery = "SELECT COUNT(*) FROM classes";
        String totalPendingFeesQuery = "SELECT SUM(due_amount) FROM payment_due";
        String totalCollectedFeesQuery = "SELECT SUM(payment_amount) FROM payment";

        try (Connection conn = DatabaseDriver.getConnection()) {

            // Fetch total students
            try (PreparedStatement pstmt = conn.prepareStatement(totalStudentsQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int countStudents = rs.getInt(1);
                    total_students.setText(String.valueOf(countStudents));
                }
            }

            // Fetch total classes
            try (PreparedStatement pstmt = conn.prepareStatement(totalClassesQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int countClasses = rs.getInt(1);
                    total_classes.setText(String.valueOf(countClasses));
                }
            }

            // Fetch total pending fees
            try (PreparedStatement pstmt = conn.prepareStatement(totalPendingFeesQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double pendingFees = rs.getDouble(1);
                    total_pending_fees.setText("NRS. " + pendingFees);
                }
            }

            // Fetch total collected fees
            try (PreparedStatement pstmt = conn.prepareStatement(totalCollectedFeesQuery);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double collectedFees = rs.getDouble(1);
                    total_collected_fees.setText("NRS. " + collectedFees);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Optional: showAlert("Error", "Unable to load dashboard data: " + e.getMessage());
        }
    }

}
