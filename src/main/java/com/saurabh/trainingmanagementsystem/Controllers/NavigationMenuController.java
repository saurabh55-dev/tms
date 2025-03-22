package com.saurabh.trainingmanagementsystem.Controllers;

import com.saurabh.trainingmanagementsystem.Models.Model;
import com.saurabh.trainingmanagementsystem.Views.MenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NavigationMenuController implements Initializable {

    public Button add_course_btn;
    public Button view_course_btn;
    public Button add_student_btn;
    public Button view_student_btn;
    public Button mark_attendance_btn;
    public Button view_attendance_btn;
    public Button payment_btn;
    public Button payment_history_btn;
    public Button logout_btn;
    public Button dashboard_btn;
    public TitledPane course_pane;
    public TitledPane student_pane;
    public TitledPane attendance_pane;
    public TitledPane fee_pane;
    public TitledPane income_expenses_pane;
    public Button view_class_btn;
    public Button add_class_btn;
    public TitledPane class_pane;
    public Button add_expense_btn;
    public Button add_income_btn;
    public Button summary_btn;
    public Button change_password_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        addListeners();
    }

    private void addListeners(){
        dashboard_btn.setOnAction(event -> onDashboard());
        add_class_btn.setOnAction(event -> onAddClass());
        view_class_btn.setOnAction(event -> onViewClass());
        add_course_btn.setOnAction(event -> onAddCourse());
        view_course_btn.setOnAction(event -> onViewCourse());
        add_student_btn.setOnAction(event -> onAddStudent());
        view_student_btn.setOnAction(event -> onViewStudent());
        mark_attendance_btn.setOnAction(event -> onMarkAttendance());
        view_attendance_btn.setOnAction(event -> onViewAttendance());
        payment_btn.setOnAction(event -> onPayment());
        payment_history_btn.setOnAction(event -> onPaymentHistory());
        add_expense_btn.setOnAction(event -> onAddExpense());
        add_income_btn.setOnAction(event -> onAddIncome());
        summary_btn.setOnAction(event -> onSummary());
        logout_btn.setOnAction(event -> onLogout());
        change_password_btn.setOnAction(event -> onChangePassword());
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.DASHBOARD);
    }

    private void onAddClass() {Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.ADD_CLASS);}

    private void onViewClass() {Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.VIEW_CLASS);}

    private void onAddCourse() {Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.ADD_COURSE);}

    private void onViewCourse() {Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.VIEW_COURSE);}

    private void onAddStudent() {Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.ADD_STUDENT);}

    private void onViewStudent() {Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.VIEW_STUDENT);}

    private void onMarkAttendance(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.MARK_ATTENDANCE);}

    private void onViewAttendance(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.VIEW_ATTENDANCE);}

    private void onPayment(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.PAYMENT);}

    private void onPaymentHistory(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.PAYMENT_HISTORY);}

    private void onAddExpense(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.ADD_EXPENSE);}

    private void onAddIncome(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.ADD_INCOME);}

    private void onSummary(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.SUMMARY);}

    private void onChangePassword(){Model.getInstance().getViewFactory().getHomeSelectedMenuItem().set(MenuOptions.CHANGE_PASSWORD);}

    private void onLogout(){
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();

        Model.getInstance().getViewFactory().closeStage(stage);

        Model.getInstance().getViewFactory().showLoginWindow();

        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
