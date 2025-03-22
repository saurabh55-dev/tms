package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.ClassModel;
import javafx.scene.control.TextField;

public class UpdateClassFormController {


    public ClassModel classModel;
    public Runnable onSaveCallback;

    public TextField startTimeField;
    public TextField endTimeField;

    public void setClassModel(ClassModel classModel) {
        this.classModel = classModel;
        startTimeField.setText(classModel.getStartTime());
        endTimeField.setText(classModel.getEndTime());
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }

    public void handleSaveButtonAction() {
        // Update the course object with new values
        classModel.setStartTime(startTimeField.getText());
        classModel.setEndTime(endTimeField.getText());

        // Close the popup window
        startTimeField.getScene().getWindow().hide();

        // Trigger the callback to refresh the table
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }
}