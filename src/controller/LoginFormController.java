package controller;

import com.jfoenix.controls.JFXComboBox;
import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane context;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public Label errorLabel;
    public JFXComboBox cmbUserType;

    public void initialize(){
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        URL resource = getClass().getResource("../view/ReceptionDashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) context.getScene().getWindow();
        window.setScene(new Scene(load));

        /*Connection con= DbConnection.getInstance().getConnection();
        String query="SELECT * FROM Users";
        PreparedStatement stm = con.prepareStatement(query);

        String UserName=txtUserName.getText();
        String Password=txtPassword.getText();

        ResultSet rst=stm.executeQuery(query);

        if (rst.next()){
            if (UserName.equals(rst.getString(2)) && Password.equals(rst.getString(1))){
                URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                Stage window = (Stage) context.getScene().getWindow();
                window.setTitle("Admin Form");
                window.setScene(new Scene(load));
            }else {
                errorLabel.setText("Enter correct username or password");
            }
        }
        if (rst.next()){
            if (UserName.equals(rst.getString(2)) && Password.equals(rst.getString(1))){
                URL resource = getClass().getResource("../view/ReceptionDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                Stage window = (Stage) context.getScene().getWindow();
                window.setTitle("Cashier Form");
                window.setScene(new Scene(load));
            }else {
                errorLabel.setText("Enter correct username or password");
            }
        }*/


    }




    public void exitOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToPasswordOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }


    public void AdminloginOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) context.getScene().getWindow();
        window.setScene(new Scene(load));
    }
}
