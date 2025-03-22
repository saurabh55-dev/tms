package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.ClassModel;
import com.saurabh.trainingmanagementsystem.Models.Course;
import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import com.saurabh.trainingmanagementsystem.Models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewStudentController implements Initializable {

    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public TableView students_table;
    public TableColumn name_column;
    public TableColumn email_column;
    public TableColumn address_column;
    public TableColumn phone_column;
    public TableColumn actions_column;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        email_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("address"));
        phone_column.setCellValueFactory(new PropertyValueFactory<>("phone"));
        actions_column.setCellValueFactory(new PropertyValueFactory<>("dummy"));
        actions_column.setCellFactory(createActionCellFactory());

        refreshTable();
    }

    private Callback<TableColumn<Student, String>, TableCell<Student, String>> createActionCellFactory() {
        return column -> new TableCell<Student, String>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                // Handle Update button click
                updateButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleUpdateButtonAction(student);
                });

                // Handle Delete button click
                deleteButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleDeleteButtonAction(student);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Display both buttons in the cell
                    setGraphic(new HBox(5, updateButton, deleteButton));
                }
            }
        };
    }

    private void handleDeleteButtonAction(Student student) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Student");
        confirmationDialog.setHeaderText("Are you sure you want to delete this student?");
        confirmationDialog.setContentText("Student: " + student.getStudentId());

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteStudentFromDatabase(student.getStudentId());
            refreshTable(); // Refresh the table after deletion
        }
    }

    private void deleteStudentFromDatabase(int studentId) {
        String query = "DELETE FROM student WHERE student_id = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateButtonAction(Student student) {
        try {
            // Load the update form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UpdateStudentForm.fxml"));
            VBox root = loader.load();

            // Get the controller and pass the selected course
            UpdateStudentFormController controller = loader.getController();
            controller.setSelectedStudent(student);

            // Set a callback to refresh the table after saving
            controller.setOnSaveCallback(() -> {
                updateStudentInDatabase(student);
                refreshTable();
            });

            // Create a new stage (popup window)
            Stage stage = new Stage();
            stage.setTitle("Update Course");
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show the popup and wait for it to close
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStudentInDatabase(Student student) {
        String query = "UPDATE student SET name = ?, email = ?, address = ?, phone = ? WHERE student_id = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getEmail());
            preparedStatement.setString(3, student.getAddress());
            preparedStatement.setString(4, student.getPhone());
            preparedStatement.setInt(5, student.getStudentId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Student> fetchStudentsFromDatabase() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String query = "SELECT * FROM student ";

        try (Connection conn = DatabaseDriver.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phone = rs.getString("phone");

                String classDetails = "Dummy";

                // Create a Student object and add it to the list
                students.add(new Student(studentId, name, email, address, phone, classDetails));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private void refreshTable() {
        ObservableList<Student> students = fetchStudentsFromDatabase();
        students_table.setItems(students);
    }
}
