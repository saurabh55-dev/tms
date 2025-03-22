package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import com.saurabh.trainingmanagementsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Label title_label;
    public Label username_label;
    public Label password_label;
    public TextField username_field;
    public PasswordField password_field;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction((event) -> onLogin());

        // Press Enter while focused on the password field
        password_field.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onLogin();
            }
        });

        // Optional: Press Enter while focused on the username field
        username_field.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                password_field.requestFocus(); // Move focus to password field
            }
        });
    }

    private void onLogin(){
        String username = username_field.getText();
        String password = password_field.getText();

        if(validateLogin(username, password)){
            Stage stage = (Stage) error_lbl.getScene().getWindow();
            Model.getInstance().setAdminLoginSuccessFlag(true);
            Model.getInstance().getViewFactory().showHomeWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        }else{
            username_field.setText("");
            password_field.setText("");
            error_lbl.setText("No Such Login Credentials");
        }
    }

    private boolean validateLogin(String username, String password){
        String query = "SELECT * FROM login WHERE username = ? AND password = ?";
        try(Connection connection = DatabaseDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            System.out.println("Connected: " + connection.getMetaData().getURL());

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
