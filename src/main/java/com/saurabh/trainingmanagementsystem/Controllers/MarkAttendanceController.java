package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import com.saurabh.trainingmanagementsystem.Models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MarkAttendanceController implements Initializable {

    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public HBox selection_container;
    public ChoiceBox class_choice;
    public DatePicker attendance_date;
    public Button load_students_btn;
    public TableView students_table;
    public TableColumn id_column;
    public TableColumn student_name_column;
    public TableColumn attendance_column;
    public HBox button_container;
    public Button save_attendance_btn;
    public ChoiceBox course_choice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        class_choice.setDisable(true);
        populateCourseChoiceBox();
        course_choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                class_choice.setDisable(false);
                populateClassChoiceBox((String) newValue);
            }else{
                class_choice.setDisable(true);
            }
        });

        id_column.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        student_name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        attendance_column.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        attendance_column.setCellFactory(column -> {
            CheckBoxTableCell<Student, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        students_table.setEditable(true);
        attendance_column.setEditable(true);

        load_students_btn.setOnAction(event -> handleOnLoad());

        save_attendance_btn.setOnAction(event -> {
            String selectedCourse = (String) course_choice.getValue();
            String selectedClass = (String) class_choice.getValue();

            if(attendance_date.getValue() == null){
                showAlert("Error", "Please select a date");
                return;
            }
            Date date = Date.valueOf(attendance_date.getValue());

            if(selectedCourse == null || selectedClass == null){
                showAlert("Error" , "Please select a course, class and date");
                return;
            }

            int classId = getClassId(selectedClass);
            ObservableList<Student> students = students_table.getItems();

            saveAttendanceToDatabase(students, classId, date);
        });
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
                "WHERE co.course_name = ?";
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

    private int getClassId(String classDisplayText) {
        String[] parts = classDisplayText.split(" : ");
        if (parts.length > 0) {
            return Integer.parseInt(parts[0]); // class_id is the first part
        }
        return -1; // Return -1 if class not found
    }

    private ObservableList<Student> fetchStudentsByCourseAndClass(String courseName, int classId) {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String query = "SELECT s.student_id, s.name, s.email, s.address, s.phone, c.course_name, cl.start_time, cl.end_time " +
                "FROM student s " +
                "JOIN student_course sc ON s.student_id = sc.student_id " +
                "JOIN course c ON sc.course_id = c.course_id " +
                "JOIN student_class scl ON s.student_id = scl.student_id " +
                "JOIN classes cl ON scl.class_id = cl.class_id " +
                "WHERE c.course_name = ? AND cl.class_id = ?";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseName);
            pstmt.setInt(2, classId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                String courseNameFromDB = rs.getString("course_name");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");

                // Format class details: "Java : 9:00 to 10:00"
                String classDetails = String.format("%s : %s to %s", courseNameFromDB, startTime, endTime);

                // Create a Student object and add it to the list
                students.add(new Student(studentId, name, email, address, phone, classDetails));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private void handleOnLoad(){
        String selectedCourse = (String) course_choice.getValue();
        String selectedClass = (String) class_choice.getValue();

        // Validate selection
        if (selectedCourse == null || selectedClass == null) {
            showAlert("Error", "Please select a course and class!");
            return;
        }

        // Extract class_id from the selected class display text
        int classId = getClassId(selectedClass);

        // Fetch students enrolled in the selected course and class
        ObservableList<Student> students = fetchStudentsByCourseAndClass(selectedCourse, classId);

        // Populate the TableView
        students_table.setItems(students);
    }

    private void saveAttendanceToDatabase(ObservableList<Student> students, int classId, Date date) {

        if(doesAttendanceExist(classId, date)){
            updateAttendance(students, classId, date);
            showAlert("Success", "Attendance UPDATED Successfully!");
        }else {
            String query = "INSERT INTO attendance (student_id, class_id, date, is_present) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseDriver.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                for (Student student : students) {
                    pstmt.setInt(1, student.getStudentId());
                    pstmt.setInt(2, classId);
                    pstmt.setDate(3, date);
                    pstmt.setBoolean(4, student.isAttendance());
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                showAlert("Success", "Attendance SAVED successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean doesAttendanceExist(int classId, Date date) {
        String query = "SELECT COUNT(*) FROM attendance WHERE class_id = ? AND date = ?";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, classId);
            pstmt.setDate(2, date);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count > 0, attendance records exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if an error occurs
    }

    private void updateAttendance(ObservableList<Student> students, int classId, Date date) {
        String query = "UPDATE attendance SET is_present = ? WHERE student_id = ? AND class_id = ? AND date = ?";

        try (Connection conn = DatabaseDriver.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (Student student : students) {
                pstmt.setBoolean(1, student.isAttendance());
                pstmt.setInt(2, student.getStudentId());
                pstmt.setInt(3, classId);
                pstmt.setDate(4, date);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
