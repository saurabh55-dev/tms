package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.Course;
import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AddClassController {
    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public VBox body_container;
    public Label start_label;
    public TextField start_field;
    public Label end_label;
    public TextField end_field;
    public Label course_label;
    public ChoiceBox course_choice;
    public HBox button_container;
    public Button add_btn;
    public Button cancel_btn;

    public void initialize() {

        List<String> courses = fetchCourseFromDatabase();

        course_choice.getItems().addAll(courses);

        add_btn.setOnAction(e -> handleAdd());
        cancel_btn.setOnAction(e -> handleCancel());
    }

    private List<String> fetchCourseFromDatabase() {
        List<String> courses = new ArrayList<>();
        String query = "SELECT course_name FROM course";
        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                courses.add(resultSet.getString("course_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public void handleAdd() {

        String startTime = start_field.getText();
        String endTime = end_field.getText();

        String selectedCourseName = (String) course_choice.getValue();

        if (selectedCourseName == null || selectedCourseName.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            showAlert("Empty Fields", "All fields are required");
            return;
        }

        // Fetch the course_id for the selected course name
        int courseId = fetchCourseIdFromDatabase(selectedCourseName);

        if (courseId == -1) {
            showAlert("Error", "Course ID not found for the selected course.");
            return;
        }

        // Save the course_id and course_name into the classes table
        boolean isSaved = saveCourseToClassesTable(courseId, startTime, endTime);
        if (isSaved) {
            showAlert("Success", "Class saved successfully!");
            start_field.setText("");
            end_field.setText("");
            course_choice.getSelectionModel().clearSelection();
        } else {
            showAlert("Error", "Failed to save the class.");
        }
    }

    private int fetchCourseIdFromDatabase(String courseName) {
        int courseId = -1;

        // SQL query to fetch course_id for the given course_name
        String query = "SELECT course_id FROM course WHERE course_name = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                courseId = resultSet.getInt("course_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseId;
    }

    private boolean saveCourseToClassesTable(int course_id, String startTime, String endTime) {

        // SQL query to insert into the classes table
        String query = "INSERT INTO classes (course_id, start_time, end_time) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, course_id);
            statement.setString(2, startTime);
            statement.setString(3, endTime);

            int rowsInserted = statement.executeUpdate();
            // Return true if the insertion was successful
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleCancel() {
        start_field.setText("");
        end_field.setText("");
        course_choice.getSelectionModel().clearSelection();
    }
}
