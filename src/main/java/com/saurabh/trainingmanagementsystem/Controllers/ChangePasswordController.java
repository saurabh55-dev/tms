package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    public PasswordField password_field1;
    public PasswordField password_field2;
    public Button change_pwd_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        change_pwd_btn.setOnAction(e -> onChangePassword());
    }

    private void onChangePassword() {
        String pwd1 = password_field1.getText();
        String pwd2 = password_field2.getText();

        if(pwd1.isEmpty() || pwd2.isEmpty()) {
            error_lbl.setText("Please fill all the fields");
            return;
        }

        if(!pwd1.equals(pwd2)) {
            error_lbl.setText("Passwords do not match");
            return;
        }

        if(updatePasswordInDB(pwd1)){
           showAlert("SUCCESS", "Password changed successfully");
            clearFields();
        }else{
            showAlert("ERROR", "Failed to change password");
        }
    }

    private boolean updatePasswordInDB(String pwd) {
        String query = "UPDATE login SET password = ? WHERE username = ?";
        String username = "admin";

        try(Connection connection = DatabaseDriver.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, pwd);
            pstmt.setString(2, username);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearFields() {
        password_field1.clear();
        password_field2.clear();
        error_lbl.setText("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
