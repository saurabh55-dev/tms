package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.Course;
import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewCourseController implements Initializable {

    public AnchorPane container;
    public VBox main_container;
    public HBox title_container;
    public Text title_label;
    public HBox button_container;
    public Button refresh_btn;
    public TableView courses_table;
    public TableColumn course_title_column;
    public TableColumn duration_column;
    public TableColumn fees_column;
    public TableColumn enrolled_students_column;
    public TableColumn actions_column;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        course_title_column.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        duration_column.setCellValueFactory(new PropertyValueFactory<>("duration"));
        fees_column.setCellValueFactory(new PropertyValueFactory<>("fees"));
        enrolled_students_column.setCellValueFactory(new PropertyValueFactory<>("enrolledStudents"));
        actions_column.setCellValueFactory(new PropertyValueFactory<>("dummy"));
        actions_column.setCellFactory(createActionCellFactory());

        refreshTable();
    }

    private Callback<TableColumn<Course, String>, TableCell<Course, String>> createActionCellFactory() {
        return column -> new TableCell<Course, String>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                // Handle Update button click
                updateButton.setOnAction(event -> {
                    Course course = getTableView().getItems().get(getIndex());
                    handleUpdateButtonAction(course);
                });

                // Handle Delete button click
                deleteButton.setOnAction(event -> {
                    Course course = getTableView().getItems().get(getIndex());
                    handleDeleteButtonAction(course);
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

    private void handleUpdateButtonAction(Course course) {
        try {
            // Load the update form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UpdateClassForm.fxml"));
            VBox root = loader.load();

            // Get the controller and pass the selected course
            UpdateCourseFormController controller = loader.getController();
            controller.setCourse(course);

            // Set a callback to refresh the table after saving
            controller.setOnSaveCallback(() -> {
                updateCourseInDatabase(course);
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

    private void updateCourseInDatabase(Course course) {
        String query = "UPDATE course SET course_name = ?, duration = ?, fees = ? WHERE course_id = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, course.getCourseName());
            preparedStatement.setString(2, course.getDuration());
            preparedStatement.setDouble(3, course.getFees());
            preparedStatement.setInt(4, course.getCourseID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButtonAction(Course course) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Course");
        confirmationDialog.setHeaderText("Are you sure you want to delete this course?");
        confirmationDialog.setContentText("Course: " + course.getCourseName());

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteCourseFromDatabase(course.getCourseID());
            refreshTable(); // Refresh the table after deletion
        }
    }

    private void deleteCourseFromDatabase(int courseId) {
        String query = "DELETE FROM course WHERE course_id = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        ObservableList<Course> courses = fetchCoursesFromDatabase();
        courses_table.setItems(courses);
    }

    private ObservableList<Course> fetchCoursesFromDatabase() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String query = "SELECT c.course_id, c.course_name, c.duration, c.fees, COUNT(sc.student_id) AS enrolled_students " +
                "FROM course c " +
                "LEFT JOIN student_course sc ON c.course_id = sc.course_id " +
                "GROUP BY c.course_id";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String courseTitle = resultSet.getString("course_name");
                String duration = resultSet.getString("duration");
                int fees = resultSet.getInt("fees");
                int enrolledStudents = resultSet.getInt("enrolled_students");

                Course course = new Course(courseId, courseTitle, duration, fees, enrolledStudents);
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

}
