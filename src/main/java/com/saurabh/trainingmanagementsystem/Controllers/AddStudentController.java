package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddStudentController implements Initializable {

    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public VBox body_container;
    public TextField name_field;
    public TextField email_field;
    public TextField address_field;
    public TextField phone_field;
    public ChoiceBox class_choice;
    public HBox button_container;
    public Button add_btn;
    public Button cancel_btn;
    public ChoiceBox course_choice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        class_choice.setDisable(true);
        populateCourseChoiceBox();
        course_choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                class_choice.setDisable(false);
                populateClassChoiceBox((String) newValue);
            } else {
                class_choice.setDisable(true);
            }
        });
        cancel_btn.setOnAction(event -> handleCancelButtonAction());
        add_btn.setOnAction(event -> handleAddButtonAction());
    }

    public void populateCourseChoiceBox() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        String query = "Select course_name FROM course";

        try (Connection conn = DatabaseDriver.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String courseName = rs.getString("course_name");
                courses.add(courseName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        course_choice.setItems(courses);
    }

    public void populateClassChoiceBox(String courseName) {
        ObservableList<String> classes = FXCollections.observableArrayList();
        String query = "SELECT c.class_id, c.course_id, c.start_time, c.end_time, co.course_name " +
                "FROM classes c " +
                "JOIN course co ON c.course_id = co.course_id " +
                "WHERE co.course_name = ?;";
        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int classId = rs.getInt("class_id");
                String courseNameFromDB = rs.getString("course_name");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");

                // Format the string to display in the ChoiceBox
                String displayText = String.format("%d : %s : %s to %s", classId, courseNameFromDB, startTime, endTime);
                classes.add(displayText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        class_choice.setItems(classes);
    }

    public void handleCancelButtonAction() {
        // Clear all text fields
        name_field.clear();
        email_field.clear();
        address_field.clear();
        phone_field.clear();

        // Clear and reset the ChoiceBoxes
        course_choice.getSelectionModel().clearSelection(); // Clear selected item
        course_choice.setValue(null); // Reset the displayed value
        class_choice.getSelectionModel().clearSelection(); // Clear selected item
        class_choice.setValue(null); // Reset the displayed value

        // Disable the class_choice ChoiceBox
        class_choice.setDisable(true);
    }

    // Helper method to fetch course_id from the database

    private void handleAddButtonAction() {
        // Get values from the form
        String name = name_field.getText();
        String email = email_field.getText();
        String address = address_field.getText();
        String phone = phone_field.getText();
        String selectedCourse = (String) course_choice.getValue();
        String selectedClass = (String) class_choice.getValue();

        // Validate input fields
        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty() || selectedCourse == null || selectedClass == null) {
            showAlert("Error", "All fields are required!");
            return;
        }

        // Insert student into the database
        try (Connection conn = DatabaseDriver.getConnection()) {
            // Step 1: Insert student into the `student` table
            String insertStudentQuery = "INSERT INTO student (name, email, address, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtStudent = conn.prepareStatement(insertStudentQuery, Statement.RETURN_GENERATED_KEYS);
            pstmtStudent.setString(1, name);
            pstmtStudent.setString(2, email);
            pstmtStudent.setString(3, address);
            pstmtStudent.setString(4, phone);
            pstmtStudent.executeUpdate();

            // Step 2: Retrieve the auto-generated student_id
            try (ResultSet generatedKeys = pstmtStudent.getGeneratedKeys()) {
                int studentId = -1;
                if (generatedKeys.next()) {
                    studentId = generatedKeys.getInt(1);
                } else {
                    showAlert("Error", "Failed to retrieve student ID!");
                    return;
                }

                // Step 3: Insert into `student_course` table
                int courseId = getCourseId(selectedCourse); // Fetch course_id from the database
                if (courseId == -1) {
                    showAlert("Error", "Invalid course selected!");
                    return;
                }

                String insertStudentCourseQuery = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";

                try (PreparedStatement pstmtStudentCourse = conn.prepareStatement(insertStudentCourseQuery)) {
                    pstmtStudentCourse.setInt(1, studentId);
                    pstmtStudentCourse.setInt(2, courseId);
                    pstmtStudentCourse.executeUpdate();
                }

                //Step 4: Fetching Course Fee from course table
                String fetchCourseFeeQuery = "SELECT fees FROM course WHERE course_id = ?";

                try (PreparedStatement pstmtCourseFee = conn.prepareStatement(fetchCourseFeeQuery)) {
                    pstmtCourseFee.setInt(1, courseId);
                    try (ResultSet rsCourseFee = pstmtCourseFee.executeQuery()) {
                        int courseFee = 0;
                        if (rsCourseFee.next()) {
                            courseFee = rsCourseFee.getInt(1);

                            //Step 5: Insert Due amount in payment_due table
                            String insertPaymentDueQuery = "INSERT INTO payment_due (student_id, due_amount) VALUES (?, ?)";

                            try (PreparedStatement pstmtPaymentDue = conn.prepareStatement(insertPaymentDueQuery)) {
                                pstmtPaymentDue.setInt(1, studentId);
                                pstmtPaymentDue.setInt(2, courseFee);
                                pstmtPaymentDue.executeUpdate();
                            }
                        } else {
                            showAlert("ERROR", "Course fee not found for the selected course!");
                            return;
                        }
                    }
                }
                // Step 6: Insert into `student_class` table
                int classId = getClassId(selectedClass); // Fetch class_id from the database
                if (classId == -1) {
                    showAlert("Error", "Invalid class selected!");
                    return;
                }

                String insertStudentClassQuery = "INSERT INTO student_class (student_id, class_id) VALUES (?, ?)";
                try (PreparedStatement pstmtStudentClass = conn.prepareStatement(insertStudentClassQuery)) {
                    pstmtStudentClass.setInt(1, studentId);
                    pstmtStudentClass.setInt(2, classId);
                    pstmtStudentClass.executeUpdate();
                }
                // Show success message
                showAlert("Success", "Student added successfully!");

                // Clear the form after successful insertion
                handleCancelButtonAction();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private int getCourseId(String courseName) {
        String query = "SELECT course_id FROM course WHERE course_name = ?";
        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("course_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if course not found
    }

    // Helper method to fetch class_id from the database
    private int getClassId(String classDisplayText) {
        // Extract class_id from the display text (e.g., "1 : Java : 9:00 to 10:00")
        String[] parts = classDisplayText.split(" : ");
        if (parts.length > 0) {
            return Integer.parseInt(parts[0]); // class_id is the first part
        }
        return -1; // Return -1 if class not found
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
