package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.User;
import util.ValidationUtil;
import util.controller.UserController;
import view.tm.UserTM;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class SystemUserFormController {
    public TextField txtUserID;
    public TextField txtUserName;
    public TextField txtUserPassword;
    public Button btnAdd;
    public TableView<UserTM> tblUser;
    public TableColumn colUserID;
    public TableColumn colName;
    public TableColumn colPassword;
    public TableColumn colUserType;
    public TextField txtUserType;
    public TextField txtSearchUser;
    public Button btnDelete;
    public Button btnUpdate;

    private UserController controller=new UserController();

    public void initialize(){
        storeValidations();
        initTable();

        txtSearchUser.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    public void initTable() {
        try {
            colUserID.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
           // colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
            colUserType.setCellValueFactory(new PropertyValueFactory<>("type"));

            setUserToTable(controller.getAllUser());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();

    Pattern userIDPattern = Pattern.compile("^(U00)[-]?[0-9]{4}$");
    Pattern namePattern = Pattern.compile("^[A-z ]{1,}$");
    Pattern passwordPattern = Pattern.compile("^[A-z0-9]{1,}$");
    Pattern typePattern = Pattern.compile("^[A-z/ ]{3,}$");

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    private void storeValidations() {
        map.put(txtUserID, userIDPattern);
        map.put(txtUserName, namePattern);
        map.put(txtUserPassword, passwordPattern);
        map.put(txtUserType, typePattern);

    }

    private void setUserToTable(ArrayList<User> allUsers) {
        ObservableList<UserTM> obList = FXCollections.observableArrayList();
        allUsers.forEach(e->{
            obList.add(
                    new UserTM(e.getId(),e.getName(),e.getPassword(),e.getType()));
        });
        tblUser.setItems(obList);

    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        User u1= new User(
                txtUserID.getText(),txtUserName.getText(),txtUserPassword.getText(),txtUserType.getText()
        );

        if (controller.updateUser(u1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();

            setUserToTable(controller.getAllUser());
            clear();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    public void saveDonorOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        User u1 = new User(
                txtUserID.getText(),
                txtUserName.getText(),
                new String(Base64.getEncoder().encode(txtUserPassword.getText().getBytes())),
                txtUserType.getText()
        );



        if(controller.saveUser(u1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();

            setUserToTable(controller.getAllUser());
            clear();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you suer you want to Delete?", yes, no);
        alert.setTitle("Confirmation alert");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if (controller.deleteUser(txtUserID.getText())) ;

            new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
            clear();
            setUserToTable(controller.getAllUser());
        } else{
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
            clear();
        }
    }

    private void clear(){
        txtUserID.clear();
        txtUserName.clear();
        txtUserPassword.clear();
        txtUserType.clear();
    }

    public void searchUserOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        String uderId=txtUserID.getText();

        User u1=new UserController().getUser(uderId);

        if (u1==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(u1);
        }
    }

    private void setData(User u1) {
        txtUserID.setText(u1.getId());
        txtUserName.setText(u1.getName());
        txtUserPassword.setText(u1.getPassword());
        txtUserType.setText(u1.getType());
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void searchOnAction(ActionEvent actionEvent) {
        search(txtSearchUser.getText());
    }

    private void search(String value){
        ObservableList<UserTM> obList = FXCollections.observableArrayList();
        try {
            List<User> users = UserController.searchUser(value);

            users.forEach(e->{
                obList.add(
                        new UserTM(e.getId(),e.getName(),e.getPassword(),e.getType()));
            });
            tblUser.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
