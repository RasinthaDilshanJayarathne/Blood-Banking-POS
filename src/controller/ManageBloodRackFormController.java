package controller;

import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BloodRack;
import model.DonateDetail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.ValidationUtil;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.controller.BloodRackController;
import util.controller.DonationController;
import util.controller.DonorController;
import view.tm.BloodRackTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageBloodRackFormController {

    public TextField txtRackName;
    public TextField txtRackID;
    public TextField txtRackQTY;
    public Button btnAdd;
    public TableView tblBloodRack;
    public TableColumn colRackID;
    public TableColumn colName;
    public TableColumn colType;
    public TableColumn colQTY;
    public TextField txtSearch;
    public ComboBox<String> cmbBloodType;
    public BarChart<?,?> employeeBarChart;
    public TextField txtBloodID;
    public Button btnDelete;
    public Button btnUpdate;
    public TableColumn colStoreQty;

    private BloodRackController controller=new BloodRackController();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern rackIDPattern = Pattern.compile("^(R00)[-]?[0-9]{4}$");
    Pattern rackNamePattern = Pattern.compile("^[A-Z]{1,}[-][A-z]{1,}[-][A-z]{1,}$");
    Pattern rackQtyPattern = Pattern.compile("^[0-9]{1,3}$");

    public void initialize() throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        storeValidations();
        loadBloodType();
        initTable();
        loadChart();

        cmbBloodType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getBloodIds(newValue);
            }
        });

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    /*public void loadChart() throws SQLException, ClassNotFoundException {
        employeeBarChart.getData().add(new BloodRackController().setUpBarChartFromDatabase());
    }*/

    private void loadChart(){
        XYChart.Series series=new XYChart.Series();
        XYChart.Series series2=new XYChart.Series();
        series.setName("Total Qty");
        series2.setName("Store Qty");

        try {

            loadData(series);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            loadStoreData(series2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        employeeBarChart.getData().addAll(series,series2);
    }

    private void loadStoreData(XYChart.Series series2) throws SQLException, ClassNotFoundException {
        ArrayList<BloodRack> bloodRacks = BloodRackController.setUpDailyBarChart();
        for (BloodRack temp : bloodRacks
        ) {
            series2.getData().add(new XYChart.Data(String.valueOf(temp.getId()), temp.getStoreQty()));
        }
    }

    private void loadData(XYChart.Series series) throws SQLException, ClassNotFoundException {
        ArrayList<BloodRack> bloodRacks = BloodRackController.setUpDailyBarChart();
        for (BloodRack temp : bloodRacks
        ) {
            series.getData().add(new XYChart.Data(String.valueOf(temp.getId()), temp.getQty()));
        }
    }

    public void initTable(){
        colRackID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQTY.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colType.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        colStoreQty.setCellValueFactory(new PropertyValueFactory<>("storeQty"));

        try {
            setRackToTable(controller.getAllRack());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void storeValidations() {
        map.put(txtRackID, rackIDPattern);
        map.put(txtRackName, rackNamePattern);
        map.put(txtRackQTY,rackQtyPattern);
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

    private void loadBloodType() throws SQLException, ClassNotFoundException {
        List<String> bloodType = new BloodRackController().getBloodType();
        cmbBloodType.getItems().addAll(bloodType);
    }

    public void getBloodIds(String id){
        try {
            String bloodIds = new DonorController().getBloodIds(id);
            txtBloodID.setText(bloodIds);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setRackToTable(ArrayList<BloodRack> allBloodRack) {
        ObservableList<BloodRackTM> obList = FXCollections.observableArrayList();
        allBloodRack.forEach(e->{
            obList.add(new BloodRackTM(e.getId(),e.getBlId(),e.getName(),e.getQty(),e.getBloodType(),e.getStoreQty()));
        });
        tblBloodRack.setItems(obList);
    }

    public void searchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        String rackID = txtRackID.getText();

        BloodRack r1=new BloodRackController().getRack(rackID);
        if (r1==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(r1);
        }
    }

    private void setData(BloodRack r1) {
        txtRackID.setText(r1.getId());
        txtRackName.setText(r1.getName());
        txtRackQTY.setText(String.valueOf(r1.getQty()));
        cmbBloodType.setValue(r1.getBloodType());
    }

    public void deleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you suer you want to Delete?", yes, no);
        alert.setTitle("Confirmation alert");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if (controller.deleteRack(txtRackID.getText())) ;

            new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
            clear();
            setRackToTable(controller.getAllRack());
        } else{
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
            clear();
        }
    }

    public void saveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        BloodRack r1 = new BloodRack(
                txtRackID.getText(),txtBloodID.getText(),txtRackName.getText(),Integer.parseInt(txtRackQTY.getText()),cmbBloodType.getValue(),0
        );
        if(controller.saveBloodRack(r1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();

            setRackToTable(controller.getAllRack());
            clear();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            btnAdd.setDisable(true);
            BloodRack r1= new BloodRack(
                    txtRackID.getText(),txtBloodID.getText(),txtRackName.getText(),Integer.parseInt(txtRackQTY.getText()),cmbBloodType.getValue(),0
            );

            if (controller.updateRack(r1)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated..").show();

                setRackToTable(controller.getAllRack());
                clear();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again").show();
            }
        }catch (Exception e){

        }
    }

    private void clear(){
        txtRackID.clear();
        txtRackName.clear();
        txtRackQTY.clear();
        cmbBloodType.getSelectionModel().clearSelection();
        txtBloodID.clear();
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        search(txtSearch.getText());
    }
    private void search(String value){
        ObservableList<BloodRackTM> obList = FXCollections.observableArrayList();
        try {
            List<BloodRack> racks = BloodRackController.searchBloodRack(value);

            racks.forEach(e->{
                obList.add(new BloodRackTM(e.getId(),e.getBlId(),e.getName(),e.getQty(),e.getBloodType(),0));
            });
            tblBloodRack.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printBloodRackOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/BoodRack_Report.jrxml"));
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
