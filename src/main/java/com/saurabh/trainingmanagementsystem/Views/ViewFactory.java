package com.saurabh.trainingmanagementsystem.Views;

import com.saurabh.trainingmanagementsystem.Controllers.HomeController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private final ObjectProperty<MenuOptions> homeSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane addClassView;
    private AnchorPane viewClassView;
    private AnchorPane addCourseView;
    private AnchorPane viewCoursesView;
    private AnchorPane addStudentView;
    private AnchorPane viewStudentView;
    private AnchorPane markAttendanceView;
    private AnchorPane viewAttendanceView;
    private AnchorPane feePaymentView;
    private AnchorPane viewPaymentView;
    private AnchorPane addIncomeView;
    private AnchorPane addExpenseView;
    private AnchorPane summaryView;
    private AnchorPane changePasswordView;

    public ViewFactory() {
        this.homeSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<MenuOptions> getHomeSelectedMenuItem() {
        return homeSelectedMenuItem;
    }

    public AnchorPane getDashboardView() {
        try {
            dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dashboardView;
    }

    public AnchorPane getAddClassView() {
            try {
                addClassView = new FXMLLoader(getClass().getResource("/Fxml/AddClass.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return addClassView;
    }

    public AnchorPane getViewClassView() {
        try {
            viewClassView = new FXMLLoader(getClass().getResource("/Fxml/ViewClass.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return viewClassView;
    }

    public AnchorPane getAddCourseView() {
            try {
                addCourseView = new FXMLLoader(getClass().getResource("/Fxml/AddCourse.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return addCourseView;
    }

    public AnchorPane getViewCoursesView() {
        try {
            viewCoursesView = new FXMLLoader(getClass().getResource("/Fxml/ViewCourse.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewCoursesView;
    }

    public AnchorPane getAddStudentView() {
            try {
                addStudentView = new FXMLLoader(getClass().getResource("/Fxml/AddStudent.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return addStudentView;
    }

    public AnchorPane getViewStudentView() {
        try {
            viewStudentView = new FXMLLoader(getClass().getResource("/Fxml/ViewStudent.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewStudentView;
    }

    public AnchorPane getMarkAttendanceView() {
            try {
                markAttendanceView = new FXMLLoader(getClass().getResource("/Fxml/MarkAttendance.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return markAttendanceView;
    }

    public AnchorPane getViewAttendanceView() {
        try {
            viewAttendanceView = new FXMLLoader(getClass().getResource("/Fxml/ViewAttendance.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewAttendanceView;
    }

    public AnchorPane getFeePaymentView() {
            try {
                feePaymentView = new FXMLLoader(getClass().getResource("/Fxml/PaymentPortal.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return feePaymentView;
    }

    public AnchorPane getViewPaymentView() {
        try {
            viewPaymentView = new FXMLLoader(getClass().getResource("/Fxml/ViewPayment.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewPaymentView;
    }

    public AnchorPane getAddIncomeView() {
            try {
                addIncomeView = new FXMLLoader(getClass().getResource("/Fxml/AddIncome.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return addIncomeView;
    }

    public AnchorPane getAddExpenseView() {
            try {
                addExpenseView = new FXMLLoader(getClass().getResource("/Fxml/AddExpense.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return addExpenseView;
    }

    public AnchorPane getSummaryView() {
        try {
            summaryView = new FXMLLoader(getClass().getResource("/Fxml/ViewSummary.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return summaryView;
    }

    public AnchorPane getChangePasswordView() {
            try {
                changePasswordView = new FXMLLoader(getClass().getResource("/Fxml/ChangePassword.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return changePasswordView;
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showHomeWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Home.fxml"));
        HomeController homeController = new HomeController();
        loader.setController(homeController);
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("Management System");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
