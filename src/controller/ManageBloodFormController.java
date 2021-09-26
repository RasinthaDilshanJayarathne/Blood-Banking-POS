package controller;

import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Blood;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.validation.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.validation.controller.BloodController;
import view.tm.BloodTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageBloodFormController {
    public AnchorPane context;
    public Button btnDelete;
    public Button btnAdd;
    public Button btnUpdate;
    public TextField txtBloodType;
    public TextField txtBloodID;
    public TextField txtDescription;
    public TableColumn colBloodId;
    public TableColumn colBloodDescription;
    public TableView<BloodTM> tblBlood;
    public TableColumn colBloodType;
    public TextField txtSearch;

    private BloodController controller=new BloodController();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern bloodIDPattern = Pattern.compile("^(B00)[-]?[0-9]{4}$");
    Pattern bloodTypePattern = Pattern.compile("^[A-Z]{1,2}[+]|[A-Z]{1,2}[-]$");
    Pattern descriptionPattern = Pattern.compile("^[A-z0-9\\s(')(|)(,)(-)(+)]{3,}$");

    public void initialize(){
        btnAdd.setDisable(true);
        storeValidations();
        initTable();
    }

    public void initTable(){
        try {
            colBloodId.setCellValueFactory(new PropertyValueFactory<>("blId"));
            colBloodType.setCellValueFactory(new PropertyValueFactory<>("blType"));
            colBloodDescription.setCellValueFactory(new PropertyValueFactory<>("blDescription"));

            setBloodToTable(controller.getAllBlood());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });

    }

    private void setBloodToTable(ArrayList<Blood> allBloods) {
        ObservableList<BloodTM> obList = FXCollections.observableArrayList();
        allBloods.forEach(e->{
            obList.add(
                    new BloodTM(e.getBlId(),e.getBlType(),e.getBlDescription()));
        });
        tblBlood.setItems(obList);

    }

    private void storeValidations() {
        map.put(txtBloodID, bloodIDPattern);
        map.put(txtBloodType, bloodTypePattern);
        map.put(txtDescription,descriptionPattern);
    }

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

    public void deleteBloodOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you suer you want to Delete?", yes, no);
        alert.setTitle("Confirmation alert");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if (controller.deleteBlood(txtBloodID.getText())) ;

            new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
            clear();
            setBloodToTable(controller.getAllBlood());
        } else{
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
            clear();
        }
    }

    public void saveBloodOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        Blood b1 = new Blood(
                txtBloodID.getText(),txtBloodType.getText(),txtDescription.getText()
        );

        if(controller.saveBlood(b1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();

            setBloodToTable(controller.getAllBlood());
            clear();

        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }

    public void updateBloodOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        btnAdd.setDisable(true);
        Blood b1= new Blood(
                txtBloodID.getText(),txtBloodType.getText(),txtDescription.getText()
        );

        if (controller.updateBlood(b1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();

            setBloodToTable(controller.getAllBlood());
            clear();

        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    public void serachOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        String bloodId = txtBloodID.getText();

        Blood b1= new BloodController().getBlood(bloodId);
        if (b1==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(b1);
        }
    }

    private void setData(Blood b) {
        txtBloodID.setText(b.getBlId());
        txtBloodType.setText(b.getBlType());
        txtDescription.setText(b.getBlDescription());
    }

    private void clear(){
        txtBloodID.clear();
        txtBloodType.clear();
        txtDescription.clear();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        search(txtSearch.getText());
    }

    private void search(String value){
        try {
            List<Blood> bloods = BloodController.searchBlood(value);
            ObservableList<BloodTM> obList = FXCollections.observableArrayList();
            bloods.forEach(e->{
                obList.add(
                        new BloodTM(e.getBlId(),e.getBlType(),e.getBlDescription()));
            });
            tblBlood.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void printBloodOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Blood_Report.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
