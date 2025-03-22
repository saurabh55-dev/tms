package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.ClassModel;
import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewClassController implements Initializable {
    public Button refresh_btn;
    public TableView courses_table;
    public TableColumn class_title_column;
    public TableColumn start_time_column;
    public TableColumn end_time_column;
    public TableColumn enrolled_students_column;
    public TableColumn actions_column;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        class_title_column.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        start_time_column.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end_time_column.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        enrolled_students_column.setCellValueFactory(new PropertyValueFactory<>("enrolledStudents"));
        actions_column.setCellValueFactory(new PropertyValueFactory<>("dummy"));
        actions_column.setCellFactory(createActionCellFactory());

        refreshTable();

    }
    private Callback<TableColumn<ClassModel, String>, TableCell<ClassModel, String>> createActionCellFactory() {
        return column -> new TableCell<ClassModel, String>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                // Handle Update button click
                updateButton.setOnAction(event -> {
                    ClassModel classModel = getTableView().getItems().get(getIndex());
                    handleUpdateButtonAction(classModel);
                });

                // Handle Delete button click
                deleteButton.setOnAction(event -> {
                    ClassModel classModel = getTableView().getItems().get(getIndex());
                    handleDeleteButtonAction(classModel);
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

    private void handleUpdateButtonAction(ClassModel classModel) {
        try {
            // Load the update form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UpdateClassForm.fxml"));
            VBox root = loader.load();

            // Get the controller and pass the selected course
            UpdateClassFormController controller = loader.getController();
            controller.setClassModel(classModel);

            // Set a callback to refresh the table after saving
            controller.setOnSaveCallback(() -> {
                updateClassInDatabase(classModel);
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
    private void updateClassInDatabase(ClassModel classModel) {
        String query = "UPDATE classes SET start_time = ?, end_time = ? WHERE class_id = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, classModel.getStartTime());
            preparedStatement.setString(2, classModel.getEndTime());
            preparedStatement.setInt(3, classModel.getClassId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteButtonAction(ClassModel classModel) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Class");
        confirmationDialog.setHeaderText("Are you sure you want to delete this class?");
        confirmationDialog.setContentText("Class: " + classModel.getClassId() + " : " + classModel.getCourseName());

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteClassFromDatabase(classModel.getClassId());
            refreshTable(); // Refresh the table after deletion
        }
    }

    private void deleteClassFromDatabase(int classId) {
        String query = "DELETE FROM classes WHERE class_id = ?";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, classId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        ObservableList<ClassModel> classes = fetchClassesFromDatabase();
        courses_table.setItems(classes);
    }

    private ObservableList<ClassModel> fetchClassesFromDatabase() {
        ObservableList<ClassModel> classes = FXCollections.observableArrayList();
        String query = "SELECT c.class_id, c.course_id, co.course_name, c.start_time, c.end_time, COUNT(sc.student_id) AS enrolled_students " +
                "FROM classes c " +
                "JOIN course co ON c.course_id = co.course_id " +
                "LEFT JOIN student_class sc ON c.class_id = sc.class_id " +
                "GROUP BY c.class_id, c.course_id, co.course_name, c.start_time, c.end_time;";

        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int classId = resultSet.getInt("class_id");
                int courseId = resultSet.getInt("course_id");
                String courseTitle = resultSet.getString("course_name");
                String startTime = resultSet.getString("start_time");
                String endTime = resultSet.getString("end_time");
                int enrolledStudents = resultSet.getInt("enrolled_students");

                ClassModel classModel = new ClassModel(classId, courseId, courseTitle, startTime, endTime, enrolledStudents);
                classes.add(classModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }
}
