package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import com.saurabh.trainingmanagementsystem.Models.PaymentHistory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewPaymentController implements Initializable {
    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public HBox filter_container;
    public ChoiceBox filter_choice;
    public Button filter_btn;
    public Button refresh_btn;
    public TableView payment_history_table;
    public TableColumn name_column;
    public TableColumn date_column;
    public TableColumn course_column;
    public TableColumn method_column;
    public TableColumn amount_column;
    public TableColumn due_column;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the columns
        name_column.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        course_column.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        method_column.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        amount_column.setCellValueFactory(new PropertyValueFactory<>("amount"));
        due_column.setCellValueFactory(new PropertyValueFactory<>("dueAmount"));

        // Load all courses into the filter_choice ChoiceBox
        populateCourseChoiceBox();

        // Load all payment history data initially
        refreshTable();

        // Set up the filter button
        filter_btn.setOnAction(event -> applyFilter());

        // Set up the refresh button
        refresh_btn.setOnAction(event -> refreshTable());
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
        filter_choice.setItems(courses);
    }

    private ObservableList<PaymentHistory> fetchPaymentHistory(String courseName) {
        ObservableList<PaymentHistory> paymentHistory = FXCollections.observableArrayList();
        String query = "SELECT s.name AS student_name, c.course_name, p.payment_date, p.payment_method, p.payment_amount, pd.due_amount " +
                "FROM payment p " +
                "JOIN student s ON p.student_id = s.student_id " +
                "JOIN course c ON p.course_id = c.course_id " +
                "JOIN payment_due pd ON p.student_id = pd.student_id" +
                (courseName != null ? " WHERE c.course_name = ?" : "");

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (courseName != null) {
                pstmt.setString(1, courseName);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String studentName = rs.getString("student_name");
                String course = rs.getString("course_name");
                String paymentDate = rs.getString("payment_date");
                String paymentMethod = rs.getString("payment_method");
                int amount = rs.getInt("payment_amount");
                int dueAmount = rs.getInt("due_amount");

                paymentHistory.add(new PaymentHistory(studentName, course, Date.valueOf(paymentDate), paymentMethod, amount, dueAmount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentHistory;
    }

    private void applyFilter() {
        String selectedCourse = (String) filter_choice.getValue();
        ObservableList<PaymentHistory> filteredHistory = fetchPaymentHistory(selectedCourse);
        payment_history_table.setItems(filteredHistory);
    }

    private void refreshTable() {
        // Clear the filter choice
        filter_choice.getSelectionModel().clearSelection();

        // Load all payment history data
        ObservableList<PaymentHistory> paymentHistory = fetchPaymentHistory(null);
        payment_history_table.setItems(paymentHistory);
    }

}
