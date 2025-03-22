package com.saurabh.trainingmanagementsystem.Models;

import com.saurabh.trainingmanagementsystem.Views.ViewFactory;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;

    private boolean adminLoginSuccessFlag;

    private Model(){
        this.viewFactory = new ViewFactory();
        this.adminLoginSuccessFlag = false;
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public boolean getAdminLoginSuccessFlag() {return this.adminLoginSuccessFlag;}
    public void setAdminLoginSuccessFlag(boolean flag) {this.adminLoginSuccessFlag = flag;}

}
