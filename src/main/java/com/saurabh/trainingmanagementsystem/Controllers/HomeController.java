package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public BorderPane home_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getHomeSelectedMenuItem().addListener((observable, oldValue, newValue) -> {
            switch (newValue){
                case ADD_CLASS -> home_parent.setCenter(Model.getInstance().getViewFactory().getAddClassView());
                case VIEW_CLASS -> home_parent.setCenter(Model.getInstance().getViewFactory().getViewClassView());
                case ADD_COURSE -> home_parent.setCenter(Model.getInstance().getViewFactory().getAddCourseView());
                case VIEW_COURSE -> home_parent.setCenter(Model.getInstance().getViewFactory().getViewCoursesView());
                case ADD_STUDENT -> home_parent.setCenter(Model.getInstance().getViewFactory().getAddStudentView());
                case VIEW_STUDENT -> home_parent.setCenter(Model.getInstance().getViewFactory().getViewStudentView());
                case MARK_ATTENDANCE -> home_parent.setCenter(Model.getInstance().getViewFactory().getMarkAttendanceView());
                case VIEW_ATTENDANCE -> home_parent.setCenter(Model.getInstance().getViewFactory().getViewAttendanceView());
                case PAYMENT -> home_parent.setCenter(Model.getInstance().getViewFactory().getFeePaymentView());
                case PAYMENT_HISTORY -> home_parent.setCenter(Model.getInstance().getViewFactory().getViewPaymentView());
                case ADD_EXPENSE -> home_parent.setCenter(Model.getInstance().getViewFactory().getAddExpenseView());
                case ADD_INCOME -> home_parent.setCenter(Model.getInstance().getViewFactory().getAddIncomeView());
                case SUMMARY -> home_parent.setCenter(Model.getInstance().getViewFactory().getSummaryView());
                case CHANGE_PASSWORD -> home_parent.setCenter(Model.getInstance().getViewFactory().getChangePasswordView());
                default -> home_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
            }
        });
    }
}
