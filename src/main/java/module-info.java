module com.saurabh.trainingmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.saurabh.trainingmanagementsystem to javafx.fxml;
    exports com.saurabh.trainingmanagementsystem;
    exports com.saurabh.trainingmanagementsystem.Controllers;
    exports com.saurabh.trainingmanagementsystem.Models;
    exports com.saurabh.trainingmanagementsystem.Views;
}