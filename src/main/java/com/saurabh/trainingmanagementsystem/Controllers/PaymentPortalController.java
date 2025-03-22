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

public class PaymentPortalController implements Initializable {
    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public VBox body_container;
    public ChoiceBox course_choice;
    public ChoiceBox student_choice;
    public TextField pay_method_field;
    public TextField pay_amount_field;
    public HBox button_container;
    public Button payment_btn;
    public Button cancel_btn;
    public Label sid_label;
    public Label due_amount_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load all courses into the class_choice ChoiceBox
        populateCourseChoiceBox();

        // Add a listener to the class_choice to load students when a course is selected
        course_choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateStudentChoiceBox((String) newValue);
            }
        });

        // Add a listener to the student_choice to display the student ID and due amount
        student_choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayStudentDetails((String) newValue);
            }
        });

        // Set up the payment button
        payment_btn.setOnAction(event -> processPayment());

        // Set up the cancel button
        cancel_btn.setOnAction(event -> clearFields());
    }

    private void populateCourseChoiceBox() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        String query = "SELECT course_name FROM course";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                courses.add(rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        course_choice.setItems(courses);
    }

    private void populateStudentChoiceBox(String courseName) {
        ObservableList<String> students = FXCollections.observableArrayList();
        String query = "SELECT s.name FROM student s " +
                "JOIN student_course sc ON s.student_id = sc.student_id " +
                "JOIN course c ON sc.course_id = c.course_id " +
                "WHERE c.course_name = ?";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        student_choice.setItems(students);
    }

    private void displayStudentDetails(String studentName) {
        String query = "SELECT s.student_id, pd.due_amount FROM student s " +
                "JOIN payment_due pd ON s.student_id = pd.student_id " +
                "WHERE s.name = ?";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int studentId = rs.getInt("student_id");
                double dueAmount = rs.getDouble("due_amount");

                sid_label.setText(String.valueOf(studentId));
                due_amount_lbl.setText(String.valueOf(dueAmount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processPayment() {
        String studentName = (String) student_choice.getValue();
        String paymentMethod = pay_method_field.getText();
        int paymentAmount = Integer.parseInt(pay_amount_field.getText());

        if (studentName == null || paymentMethod.isEmpty() || paymentAmount <= 0) {
            showAlert("Error", "Please fill all fields correctly!");
            return;
        }

        // Fetch student ID and course ID
        String query = "SELECT s.student_id, c.course_id FROM student s " +
                "JOIN student_course sc ON s.student_id = sc.student_id " +
                "JOIN course c ON sc.course_id = c.course_id " +
                "WHERE s.name = ?";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int studentId = rs.getInt("student_id");
                int courseId = rs.getInt("course_id");

                // Save payment to the payment table
                savePayment(conn, studentId, courseId, paymentAmount, paymentMethod);

                // Update the payment_due table
                updatePaymentDue(conn, studentId, courseId, paymentAmount);

                showAlert("Success", "Payment processed successfully!");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void savePayment(Connection conn, int studentId, int courseId, double amount, String paymentMethod) {
        String query = "INSERT INTO payment (student_id, course_id, payment_amount, payment_method, payment_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, paymentMethod);
            pstmt.setString(5, new Date(System.currentTimeMillis()).toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePaymentDue(Connection conn, int studentId, int courseId, double paymentAmount) {
        String query = "UPDATE payment_due SET due_amount = due_amount - ? " +
                "WHERE student_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, paymentAmount);
            pstmt.setInt(2, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        course_choice.getSelectionModel().clearSelection();
        student_choice.getSelectionModel().clearSelection();
        sid_label.setText("");
        due_amount_lbl.setText("");
        pay_method_field.clear();
        pay_amount_field.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
