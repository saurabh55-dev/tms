package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import com.saurabh.trainingmanagementsystem.Models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class UpdateStudentFormController{

    public VBox updateForm;
    public TextField nameField;
    public TextField emailField;
    public TextField addressField;
    public TextField phoneField;

    private Student selectedStudent;
    private Runnable onSaveCallback;

    public void setSelectedStudent(Student student) {
        this.selectedStudent = student;
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        addressField.setText(student.getAddress());
        phoneField.setText(student.getPhone());
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }

    @FXML
    public void handleSaveButtonAction(){

        selectedStudent.setName(nameField.getText());
        selectedStudent.setEmail(emailField.getText());
        selectedStudent.setAddress(addressField.getText());
        selectedStudent.setPhone(phoneField.getText());

        emailField.getScene().getWindow().hide();

        updateStudentInDatabase(selectedStudent);

        if(onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    public void handleCancelButtonAction(){
        emailField.getScene().getWindow().hide();
    }

    private void updateStudentInDatabase(Student student) {
        // Update the student table
        String updateStudentQuery = "UPDATE student SET name = ?, email = ?, address = ?, phone = ? WHERE student_id = ?";
        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmtStudent = conn.prepareStatement(updateStudentQuery)) {
            pstmtStudent.setString(1, student.getName());
            pstmtStudent.setString(2, student.getEmail());
            pstmtStudent.setString(3, student.getAddress());
            pstmtStudent.setString(4, student.getPhone());
            pstmtStudent.setInt(5, student.getStudentId());
            pstmtStudent.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
