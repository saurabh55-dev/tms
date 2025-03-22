package com.saurabh.trainingmanagementsystem;

import com.saurabh.trainingmanagementsystem.Models.DatabaseDriver;
import com.saurabh.trainingmanagementsystem.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{
    @Override
    public void start(Stage stage) {
        DatabaseDriver.initializeDatabase();
        DatabaseDriver.insertDefaultAdmin();
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}