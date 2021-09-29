package controller;

import com.jfoenix.controls.JFXComboBox;
import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;
import util.controller.UserController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class LoginFormController {
    public AnchorPane context;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Label errorLabel;

    public void initialize(){
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String userName=txtUserName.getText();
        String password=new String(Base64.getEncoder().encode(txtPassword.getText().getBytes()));

        User loginUser = new UserController().getLoginUser(userName, password);
        if (loginUser!=null){
            if (loginUser.getType().equals("Admin")){
                URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                Stage window = (Stage) context.getScene().getWindow();
                window.setScene(new Scene(load));

            }else if(loginUser.getType().equals("Reception")){
                URL resource = getClass().getResource("../view/ReceptionDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                Stage window = (Stage) context.getScene().getWindow();
                window.setScene(new Scene(load));

            }else {
                errorLabel.setText("Enter correct username or password");
            }
        }
        clear();
    }


    private void clear(){
        txtPassword.clear();
        txtUserName.clear();
    }

    public void exitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToPasswordOnAction(ActionEvent actionEvent) {
       // txtPassword.requestFocus();
    }


    public void AdminloginOnAction(ActionEvent actionEvent) throws IOException {
        /*URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) context.getScene().getWindow();
        window.setScene(new Scene(load));*/
    }

    public void goTiPassword(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }
}
