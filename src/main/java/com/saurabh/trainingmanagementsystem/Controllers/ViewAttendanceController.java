package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.AttendanceReport;
import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class ViewAttendanceController implements Initializable {

    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public HBox filter_container;
    public ChoiceBox class_choice;
    public Button filter_btn;
    public RadioButton present_day_radio;
    public ToggleGroup filterToggleGroup;
    public RadioButton monthly_radio;
    public RadioButton total_radio;
    public Button refresh_btn;
    public TableView attendance_table;
    public TableColumn student_name_column;
    public TableColumn class_column;
    public TableColumn present_day_column;
    public TableColumn monthly_column;
    public TableColumn total_column;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set up the columns
        student_name_column.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        class_column.setCellValueFactory(new PropertyValueFactory<>("className"));
        present_day_column.setCellValueFactory(new PropertyValueFactory<>("presentDayAttendance"));
        monthly_column.setCellValueFactory(new PropertyValueFactory<>("monthlyAttendance"));
        total_column.setCellValueFactory(new PropertyValueFactory<>("totalAttendance"));

        // Populate the class choice box
        populateClassChoiceBox();

        class_choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filter_btn.setDisable(newValue == null);
        });

        // Set up event handlers
        filter_btn.setOnAction(event -> applyFilters());
        refresh_btn.setOnAction(event -> refreshTable());
        refreshTable();
    }

    private ObservableList<AttendanceReport> fetchAllAttendanceReports() {
        ObservableList<AttendanceReport> reports = FXCollections.observableArrayList();
        String query = "SELECT s.name AS student_name, " +
                "       c.course_name AS class_name, " +
                "       SUM(CASE WHEN a.date = DATE('now') AND a.is_present = 1 THEN 1 ELSE 0 END) AS present_day, " +
                "       SUM(CASE WHEN STRFTIME('%m', a.date) = STRFTIME('%m', DATE('now')) AND a.is_present = 1 THEN 1 ELSE 0 END) AS monthly, " +
                "       SUM(CASE WHEN a.is_present = 1 THEN 1 ELSE 0 END) AS total " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.student_id " +
                "JOIN classes cl ON a.class_id = cl.class_id " +
                "JOIN course c ON cl.course_id = c.course_id " +
                "GROUP BY s.student_id, c.course_id;";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String studentName = rs.getString("student_name");
                String className = rs.getString("class_name");
                String presentDay = rs.getString("present_day");
                String monthly = rs.getString("monthly");
                String total = rs.getString("total");

                reports.add(new AttendanceReport(studentName, className, presentDay, monthly, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    private void populateClassChoiceBox() {
        ObservableList<String> classes = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT c.course_name, cl.start_time, cl.end_time FROM course c JOIN classes cl ON c.course_id = cl.course_id";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                classes.add(rs.getString("course_name") + " : " + rs.getString("start_time") + " : " + rs.getString("end_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        class_choice.setItems(classes);
    }

    private void applyFilters() {
        String selected = (String) class_choice.getValue();

        String selectedClass = selected.split(":")[0].trim();

        String filterType = ((RadioButton) filterToggleGroup.getSelectedToggle()).getUserData().toString();

        ObservableList<AttendanceReport> filteredReports = fetchAllAttendanceReports();

        // Filter by class
        if (!selectedClass.isEmpty()) {
            filteredReports = filteredReports.filtered(report -> report.getClassName().equals(selectedClass));
        }

        // Clear existing columns
        attendance_table.getColumns().clear();

        // Add common columns
        attendance_table.getColumns().add(student_name_column);
        attendance_table.getColumns().add(class_column);

        // Add columns based on filter type
        switch (filterType) {
            case "present_day":
                attendance_table.getColumns().add(present_day_column);
                break;
            case "monthly":
                attendance_table.getColumns().add(monthly_column);
                break;
            case "total":
                attendance_table.getColumns().add(total_column);
                break;
        }

        attendance_table.setItems(filteredReports);
    }

    private void refreshTable() {
        ObservableList<AttendanceReport> reports = fetchAllAttendanceReports();

        attendance_table.setItems(reports);
        class_choice.getSelectionModel().clearSelection();
        filterToggleGroup.selectToggle(total_radio); // Reset to "Total" filter
        attendance_table.getColumns().clear();

        attendance_table.getColumns().addAll(
                student_name_column,
                class_column,
                present_day_column,
                monthly_column,
                total_column
        );
        attendance_table.setItems(reports);
        filter_btn.setDisable(true);
    }

}
