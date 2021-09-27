package controller;

import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Donor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.ValidationUtil;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.controller.BloodRackController;
import util.controller.DonorController;
import util.controller.EmployeeController;
import view.tm.DonorTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageDornorFormController {
    public ComboBox<String> cmbUserId;
    public ComboBox<String> cmbBloodType;
    public ComboBox<String> cmbGender;
    public TextField txtName;
    public TextField txtNIC;
    public TextField txtAddress;
    public TextField txtCity;
    public TextField txtEmail;
    public TextField txtPhoneNo;
    public Button btnAdd;
    public TextField txtSearchDonor;
    public TableView<DonorTM> tblDonor;
    public TableColumn colName;
    public TableColumn colNIC;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colGender;
    public TableColumn colEmail;
    public TableColumn colPhoneNo;
    public TableColumn colUserID;
    public TableColumn colType;
    public TableColumn colBloodID;
    public TextField txtBloodID;

    private DonorController controller=new DonorController();

    public void initialize() throws SQLException, ClassNotFoundException {

        cmbGender.getItems().addAll(
                "Male", "Female"
        );

        btnAdd.setDisable(true);
        storeValidations();
        loadUserId();
        donorBloodType();
        initTable();

        cmbBloodType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getBloodIds(newValue);
            }
        });

        txtSearchDonor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    //Pattern userIDPattern = Pattern.compile("^(U00)[-]?[0-9]{4}$");
    //Pattern bloodTypePattern = Pattern.compile("");
    //Pattern donorIDPattern = Pattern.compile("^(D00)[-]?[0-9]{4}$");
    Pattern nicPattern = Pattern.compile("^[0-9]{12}|[0-9]{11}[A-Z]$");
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern phoneNoPattern = Pattern.compile("^[0-9]{3}[-]?[0-9]{7}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{6,30}$");
    Pattern cityPattern = Pattern.compile("^[A-z]{3,}$");
    Pattern emailPattern = Pattern.compile("^[a-z0-9]{3,}[@](gmail)[.][a-z]{3,}$");

    private void storeValidations() {
        map.put(txtNIC, nicPattern);
        map.put(txtName, namePattern);
        map.put(txtAddress, addressPattern);
        map.put(txtCity, cityPattern);
        map.put(txtPhoneNo, phoneNoPattern);
        map.put(txtEmail, emailPattern);
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

    public void initTable(){
        colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBloodID.setCellValueFactory(new PropertyValueFactory<>("blID"));

        try {
            setDonorToTable(controller.getAllDonor());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getBloodIds(String id){
        try {
            String bloodIds = controller.getBloodIds(id);
            txtBloodID.setText(bloodIds);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void donorBloodType() throws SQLException, ClassNotFoundException {
        List<String> bloodType = BloodRackController.getBloodType();
        cmbBloodType.getItems().addAll(FXCollections.observableArrayList(bloodType));
    }

    private void loadUserId() throws SQLException, ClassNotFoundException {
        List<String> userID = new EmployeeController().getUserId();
        cmbUserId.getItems().addAll(userID);
    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you suer you want to Delete?", yes, no);
        alert.setTitle("Confirmation alert");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if (controller.deleteDonor(txtNIC.getText())) ;

            new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
            clear();
            setDonorToTable(controller.getAllDonor());
        } else{
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
            clear();
        }
    }

    public void saveDonorOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Donor d1 = new Donor(
                txtNIC.getText(),cmbUserId.getValue(),txtName.getText(),txtAddress.getText(),txtCity.getText(),cmbBloodType.getValue(),
                txtBloodID.getText(),cmbGender.getValue(),txtPhoneNo.getText(),txtEmail.getText()
        );

        if(controller.saveDonor(d1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();

            setDonorToTable(controller.getAllDonor());
            clear();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }

    private void setDonorToTable(ArrayList<Donor> allDonor) {
        ObservableList<DonorTM> obList = FXCollections.observableArrayList();
        allDonor.forEach(e->{
            obList.add(
                    new DonorTM(e.getNic(),e.getUserID(),e.getName(),e.getAddress(),e.getCity(),e.getType(),e.getBlID(),e.getGender(),e.getPhoneNo(),e.getEmail()));
        });
        tblDonor.setItems(obList);
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        Donor d1 = new Donor(
                txtNIC.getText(),cmbUserId.getValue(),txtName.getText(),txtAddress.getText(),txtCity.getText(),cmbBloodType.getValue(),
                txtBloodID.getText(),cmbGender.getValue(),txtPhoneNo.getText(),txtEmail.getText()
        );

        if (controller.updateDonor(d1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();

            setDonorToTable(controller.getAllDonor());
            clear();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    public void searchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        String donorNic = txtNIC.getText();

        Donor d1= new DonorController().getDonor(donorNic);
        if (d1==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(d1);
        }
    }

    private void setData(Donor d1) {
        txtNIC.setText(d1.getNic());
        cmbUserId.setValue(d1.getUserID());
        txtName.setText(d1.getName());
        txtAddress.setText(d1.getAddress());
        txtCity.setText(d1.getCity());
        txtPhoneNo.setText(d1.getPhoneNo());
        txtEmail.setText(d1.getEmail());
        cmbBloodType.setValue(d1.getType());
        txtBloodID.setText(d1.getBlID());
        cmbGender.setValue(d1.getGender());
    }

    private void clear() throws SQLException, ClassNotFoundException {
        txtNIC.clear();
        cmbUserId.getSelectionModel().clearSelection();
        txtCity.clear();
        txtAddress.clear();
        txtName.clear();
        txtPhoneNo.clear();
        txtEmail.clear();
        txtBloodID.clear();
        cmbGender.getSelectionModel().clearSelection();
        cmbBloodType.getSelectionModel().clearSelection();
    }

    public void clearOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clear();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        search(txtSearchDonor.getText());
    }


    private void search(String value){
        ObservableList<DonorTM> obList = FXCollections.observableArrayList();
        try {
            List<Donor> donors = DonorController.searchDonor(value);

            donors.forEach(e->{
                obList.add(
                        new DonorTM(e.getNic(),e.getUserID(),e.getName(),e.getAddress(),e.getCity(),e.getType(),e.getBlID(),e.getGender(),e.getPhoneNo(),e.getEmail()));
            });
            tblDonor.getItems().setAll(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printDonorDetailOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Donor_Report.jrxml"));
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
