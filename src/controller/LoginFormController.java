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
        //String password=txtPassword.getText();
        String password=new String(Base64.getEncoder().encode(txtPassword.getText().getBytes()));

        User loginUser = new UserController().getLoginUser(userName, password);

        if (txtPassword.getText()!=password || txtUserName.getText()!=userName){
            errorLabel.setText("Enterr correct Username or Password");
            //new Alert(Alert.AlertType.WARNING, "Enterr correct username or passwod").showAndWait();
        }
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

    public void goTiPassword(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }
}
