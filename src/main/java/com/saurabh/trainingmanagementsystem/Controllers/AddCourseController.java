package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCourseController implements Initializable {

    public AnchorPane main_container;
    public Text title_label;
    public VBox body_container;
    public Label course_label;
    public TextField course_field;
    public Label duration_label;
    public TextField duration_field;
    public Label fee_label;
    public TextField fee_field;
    public HBox button_container;
    public Button add_btn;
    public Button cancel_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_btn.setOnAction(event -> onAddCourse());
        cancel_btn.setOnAction(event -> onCancel());
    }

    protected void onAddCourse() {
        String courseName = course_field.getText();
        String duration = duration_field.getText();
        int fees = Integer.parseInt(fee_field.getText());

        if(courseName.isEmpty() || duration.isEmpty() || fees < 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }
        String query = "INSERT INTO course (course_name, duration, fees) VALUES (?, ?, ?)";
        try(Connection connection = DatabaseDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, duration);
            preparedStatement.setInt(3, fees);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                int courseId = generatedKeys.getInt(1);

            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Course added Successfully!");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void onCancel() {
        clearFields();
    }

    private void clearFields() {
        course_field.clear();
        duration_field.clear();
        fee_field.clear();
    }

    private void showAlert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(headerText);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
