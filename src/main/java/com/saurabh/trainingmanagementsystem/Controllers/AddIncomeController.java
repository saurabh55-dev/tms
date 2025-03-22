package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddIncomeController implements Initializable {

    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public VBox body_container;
    public TextField topic_field;
    public TextField amount_field;
    public TextField remarks_field;
    public HBox button_container;
    public Button add_btn;
    public Button cancel_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_btn.setOnAction(event -> addIncome());
        cancel_btn.setOnAction(event -> clearFields());
    }

    private void addIncome() {
        String topic = topic_field.getText();
        String amount = amount_field.getText();
        String remarks = remarks_field.getText();
        LocalDate today = LocalDate.now();
        double amountDouble = 0;

        try {
            amountDouble = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            showAlert("ERROR", "Invalid amount. Please enter a valid number.");
            return;
        }

        // Save to database
        try (Connection connection = DatabaseDriver.getConnection()) {
            String query = "INSERT INTO income (topic, amount, remarks, date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, topic);
                preparedStatement.setDouble(2, amountDouble);
                preparedStatement.setString(3, remarks);
                preparedStatement.setDate(4, java.sql.Date.valueOf(today));
                preparedStatement.executeUpdate();

                clearFields();
                showAlert("SUCCESS", "Income added successfully");
            }
        } catch (Exception e) {
            showAlert("ERROR", "Database error");
            e.printStackTrace();
        }

        // Clear fields after saving
        clearFields();
    }

    private void clearFields() {
        topic_field.clear();
        amount_field.clear();
        remarks_field.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
