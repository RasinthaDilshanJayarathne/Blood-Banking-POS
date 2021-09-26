package controller;

import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Employee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.validation.ValidationUtil;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.validation.controller.EmployeeController;
import view.tm.EmployeeTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageEmployeeFormController {

    public TextField txtEmployeePhoneNo;
    public Button btnAdd;
    public ComboBox<String> cmbGender;
    public TextField txtEmployeeName;
    public TextField txtEmployeeAddress;
    public TextField txtEmployeeCity;
    public TextField txtEmployeeID;
    public TableView<EmployeeTM> tblEmployee;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colType;
    public TableColumn colGender;
    public Button btnDelete;
    public Button btnUpdate;
    public TextField txtEmployeeType;
    public TableColumn colCity;
    public TableColumn colPhoneNo;
    public ComboBox cmbUserID;
    public TableColumn colUserID;
    public TableColumn colEID;
    public TextField txtSearchEmployee;

    private EmployeeController controller=new EmployeeController();

    public void initialize() throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        storeValidations();
        employeeCombo();
        loadUserId();
        initTable();

        txtSearchEmployee.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern employeeIDPattern = Pattern.compile("^(E00)[-]?[0-9]{4}$");
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{6,30}$");
    Pattern cityPattern = Pattern.compile("^[A-z]{3,}$");
    Pattern employeeTypePattern = Pattern.compile("^[A-z]{1,}$");
    Pattern phoneNoPattern = Pattern.compile("^[0-9]{3}[-]?[0-9]{7}$");

    private void storeValidations() {
        map.put(txtEmployeeID, employeeIDPattern);
        map.put(txtEmployeeName, namePattern);
        map.put(txtEmployeeAddress, addressPattern);
        map.put(txtEmployeeCity, cityPattern);
        map.put(txtEmployeeType, employeeTypePattern);
        map.put(txtEmployeePhoneNo, phoneNoPattern);

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
        try {
            colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            colEID.setCellValueFactory(new PropertyValueFactory<>("empId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("empName"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("empAddress"));
            colCity.setCellValueFactory(new PropertyValueFactory<>("empCity"));
            colType.setCellValueFactory(new PropertyValueFactory<>("empType"));
            colGender.setCellValueFactory(new PropertyValueFactory<>("empGender"));
            colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("empPhoneNo"));

            setEmployeeToTable(controller.getAllEmployee());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void employeeCombo(){
        cmbGender.getItems().addAll(
                "Male", "Female"
        );
    }

    private void loadUserId() throws SQLException, ClassNotFoundException {
        List<String> userID = new EmployeeController().getUserId();
        cmbUserID.getItems().addAll(userID);
    }


    public void deleteEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you suer you want to Delete?", yes, no);
        alert.setTitle("Confirmation alert");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if (controller.deleteEmployee(txtEmployeeID.getText())) ;

            new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
            clear();
            setEmployeeToTable(controller.getAllEmployee());
        } else{
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
            clear();
        }

    }

    public void saveEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Employee e1 = new Employee(
                cmbUserID.getValue().toString(),txtEmployeeID.getText(),txtEmployeeName.getText(),txtEmployeeAddress.getText(), txtEmployeeCity.getText(),
                txtEmployeeType.getText(),cmbGender.getValue().toString(),txtEmployeePhoneNo.getText()
        );
        System.out.println(e1.getUserID());
        if(controller.saveEmployee(e1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();

            setEmployeeToTable(controller.getAllEmployee());
            clear();

        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }

    private void setEmployeeToTable(ArrayList<Employee> allEmployee) {
        ObservableList<EmployeeTM> obList = FXCollections.observableArrayList();
        allEmployee.forEach(e->{
            obList.add(
                    new EmployeeTM(e.getEmpId(),e.getUserID(),e.getEmpName(),e.getEmpAddress(),e.getEmpCity(),e.getEmpType(),e.getEmpGender(),e.getEmpPhoneNo()));
        });
        tblEmployee.setItems(obList);
    }

    public void updateEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        Employee e1 = new Employee(
                cmbUserID.getValue().toString(),txtEmployeeID.getText(),txtEmployeeName.getText(),txtEmployeeAddress.getText(), txtEmployeeCity.getText(),
                txtEmployeeType.getText(),cmbGender.getValue().toString(),txtEmployeePhoneNo.getText()
        );

        if (controller.updateEmployee(e1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();

            setEmployeeToTable(controller.getAllEmployee());
            clear();

        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    public void searchEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        String employeeID = txtEmployeeID.getText();

        Employee e1= new EmployeeController().getEmployee(employeeID);
        System.out.println(e1.getUserID());
        if (e1==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(e1);
        }
    }

    private void clear(){
        cmbUserID.getSelectionModel().clearSelection();
        txtEmployeeID.clear();
        txtEmployeeName.clear();
        txtEmployeeAddress.clear();
        txtEmployeeCity.clear();
        txtEmployeeType.clear();
        cmbGender.getSelectionModel().clearSelection();
        txtEmployeePhoneNo.clear();
    }

    private void setData(Employee e) {
        txtEmployeeID.setText(e.getUserID());
        cmbUserID.setValue(e.getEmpId());
        txtEmployeeName.setText(e.getEmpName());
        txtEmployeeAddress.setText(e.getEmpAddress());
        txtEmployeeCity.setText(e.getEmpCity());
        txtEmployeeType.setText(e.getEmpType());
        cmbGender.setValue(e.getEmpGender());
        txtEmployeePhoneNo.setText(e.getEmpPhoneNo());
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void searchOnAction(ActionEvent actionEvent) {
        search(txtSearchEmployee.getText());
    }
    private void search(String value){
        ObservableList<EmployeeTM> obList = FXCollections.observableArrayList();
        try {
            List<Employee> employees = EmployeeController.searchEmployee(value);
            employees.forEach(e->{
                obList.add(
                        new EmployeeTM(e.getEmpId(),e.getUserID(),e.getEmpName(),e.getEmpAddress(),e.getEmpCity(),e.getEmpType(),e.getEmpGender(),e.getEmpPhoneNo()));
            });
            tblEmployee.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printEmployeeOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/Employee_Report.jrxml"));
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
