package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.Course;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UpdateCourseFormController {

    public TextField courseTitleField;
    public TextField durationField;
    public TextField feesField;

    public Course course;
    public Runnable onSaveCallback;

    public void setCourse(Course course) {
        this.course = course;
        courseTitleField.setText(course.getCourseName());
        durationField.setText(course.getDuration());
        feesField.setText(String.valueOf(course.getFees()));
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }

    @FXML
    public void handleSaveButtonAction() {
        // Update the course object with new values
        course.setCourseName(courseTitleField.getText());
        course.setDuration(durationField.getText());
        course.setFees(Integer.parseInt(feesField.getText()));

        // Close the popup window
        courseTitleField.getScene().getWindow().hide();

        // Trigger the callback to refresh the table
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }
}