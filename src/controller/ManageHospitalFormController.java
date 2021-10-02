package controller;

import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Hospital;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.ValidationUtil;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.controller.HospitalController;
import view.tm.HospitalTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageHospitalFormController {

    public TextField txtHospitalCity;
    public TextField txtHospitalAddress;
    public TextField txtHospitalName;
    public TextField txtHospitalID;
    public TextField txtHospitalEmail;
    public TextField txtHospitalPhoneNo;
    public TextField txtHospitalWebSite;
    public Button btnAdd;
    public TableView<HospitalTM> tblHospital;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colEmail;
    public TableColumn colPhoneNo;
    public TableColumn colWebSite;
    public Button btnDelete;
    public Button btnUpdate;
    public TextField txtSearch;

    private HospitalController controller=new HospitalController();


    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern hospitalIDPattern = Pattern.compile("^(H00)[-]?[0-9]{4}$");
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{6,30}$");
    Pattern cityPattern = Pattern.compile("^[A-z]{3,}$");
    Pattern phoneNoPattern = Pattern.compile("^[0-9]{3}[-]?[0-9]{7}$");
    Pattern emailPattern = Pattern.compile("^[a-z0-9]{3,}[@](gmail)[.][a-z]{3,}$");
    Pattern websitePattern = Pattern.compile("^[A-z]{3}[.][A-z0-9]{1,}[.][A-z]{1,}$");

    public void initialize(){
        btnAdd.setDisable(true);
        storeValidations();
        initTable();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    public void initTable(){
        colID.setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colWebSite.setCellValueFactory(new PropertyValueFactory<>("website"));

        try {
           setHospitalToTable(controller.getAllHospital());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void storeValidations() {
        map.put(txtHospitalID, hospitalIDPattern);
        map.put(txtHospitalName, namePattern);
        map.put(txtHospitalAddress,addressPattern);
        map.put(txtHospitalCity,cityPattern);
        map.put(txtHospitalPhoneNo,phoneNoPattern);
        map.put(txtHospitalEmail,emailPattern);
        map.put(txtHospitalWebSite,websitePattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
               // new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void deleteHospitalOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you suer you want to Delete?", yes, no);
        alert.setTitle("Confirmation alert");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if (controller.deleteHospital(txtHospitalID.getText())) ;
            clear();
        } else{
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
        }
    }

    public void updateHospitalOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        Hospital h1 = new Hospital(
                txtHospitalID.getText(),txtHospitalName.getText(),txtHospitalEmail.getText(),txtHospitalAddress.getText(),
                txtHospitalCity.getText(),txtHospitalPhoneNo.getText(),txtHospitalWebSite.getText()
        );

        if (controller.updateHospital(h1)) {
            clear();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    public void searchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        String hospitalID = txtHospitalID.getText();

        Hospital h1= controller.getHospital(hospitalID);
        if (h1==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(h1);
        }
    }

    private void setData(Hospital h1) {
        txtHospitalID.setText(h1.getHospitalId());
        txtHospitalName.setText(h1.getName());
        txtHospitalAddress.setText(h1.getAddress());
        txtHospitalCity.setText(h1.getCity());
        txtHospitalEmail.setText(h1.getEmail());
        txtHospitalWebSite.setText(h1.getWebsite());
        txtHospitalPhoneNo.setText(h1.getPhoneNo());
    }

    private void setHospitalToTable(ArrayList<Hospital> allHospital) {
        ObservableList<HospitalTM> obList = FXCollections.observableArrayList();
        allHospital.forEach(e->{
            obList.add(
                    new HospitalTM(e.getHospitalId(),e.getName(),e.getEmail(),e.getAddress(),e.getCity(),e.getPhoneNo(),e.getWebsite()));
        });
        tblHospital.setItems(obList);
    }

    public void saveHospitalOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Hospital h1 = new Hospital(
                txtHospitalID.getText(),txtHospitalName.getText(),txtHospitalEmail.getText(),txtHospitalAddress.getText(),
                txtHospitalCity.getText(),txtHospitalPhoneNo.getText(),txtHospitalWebSite.getText()
        );

        if(controller.saveHospital(h1)) {
            clear();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }

    private void clear() throws SQLException, ClassNotFoundException {
        new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
        setHospitalToTable(controller.getAllHospital());

        txtHospitalID.clear();
        txtHospitalName.clear();
        txtHospitalEmail.clear();
        txtHospitalAddress.clear();
        txtHospitalCity.clear();
        txtHospitalPhoneNo.clear();
        txtHospitalWebSite.clear();
    }

    public void clearOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clear();
    }

    public void searchHospitalOnAction(ActionEvent actionEvent) {
        search(txtSearch.getText());
    }

    private void search(String value){
        ObservableList<HospitalTM> obList = FXCollections.observableArrayList();
        try {
            List<Hospital> hospitals = HospitalController.searchHospital(value);

            hospitals.forEach(e->{
                obList.add(
                        new HospitalTM(e.getHospitalId(),e.getName(),e.getEmail(),e.getAddress(),e.getCity(),e.getPhoneNo(),e.getEmail()));
            });
            tblHospital.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printHospitalOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Hospital_Report.jrxml"));
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
