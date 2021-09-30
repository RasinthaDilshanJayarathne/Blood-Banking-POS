package controller;

import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.controller.BloodController;
import util.controller.BloodRackController;
import util.controller.DonationController;
import util.controller.DonorController;
import view.tm.BloodTM;
import view.tm.DonateDetailTM;
import view.tm.EmployeeTM;
import view.tm.StoreDetailTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreBloodDetailFormController{

    public TableView tblStoreDetail;
    public TableColumn colRackID;
    public TableColumn colQty;
    public TableColumn colRackName;
    public TableColumn colBloodType;
    public TextField txtSearch;
    public BarChart<?,?> donateBloodDetailBarChart;

    DonationController controller=new DonationController();

    public void initialize() throws SQLException, ClassNotFoundException {
        initTable();
        loadChart();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    public void initTable(){
        //tblStoreDetail.setItems(FXCollections.observableArrayList(ManageDonateFormController.obList));

        colRackID.setCellValueFactory(new PropertyValueFactory<>("rackName"));
        colRackName.setCellValueFactory(new PropertyValueFactory<>("rackId"));
        colBloodType.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        try {
            setDonateDetailToTable(new DonorController().getAllDetail());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

   /* public void loadChart1() throws SQLException, ClassNotFoundException {
        donateBloodDetailBarChart.getData().add(new DonationController().setUpBarChartFromDatabase());
    }*/

    public void loadChart(){
        XYChart.Series series=new XYChart.Series();
        XYChart.Series series2=new XYChart.Series();
        series.setName("Total Qty");
        series2.setName("Store Qty");

        try {
            loadData(series);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            loadIStoreData(series2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        donateBloodDetailBarChart.getData().addAll(series,series2);
    }

    private void loadIStoreData(XYChart.Series series2) throws SQLException, ClassNotFoundException {
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

    private void setDonateDetailToTable(ArrayList<StoreDetailTM> allDonateDetailToTable) {
        ObservableList<StoreDetailTM> obList = FXCollections.observableArrayList();
        allDonateDetailToTable.forEach(e->{
            obList.add(
                    new StoreDetailTM(e.getBlId(),e.getBloodGroup(),e.getRackName(),e.getRackId(),e.getSpace(),e.getQty()));
        });
        tblStoreDetail.setItems(obList);

    }

    public void printDonateDetailOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/DonateDetail_Report.jrxml"));
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

    public void searchOnAction(ActionEvent actionEvent) {
        search(txtSearch.getText());
    }

    private void search(String value){
        ObservableList<StoreDetailTM> obList = FXCollections.observableArrayList();
        try {
            List<StoreDetail> storeDetails = DonationController.searchStore(value);

            storeDetails.forEach(e->{
                obList.add(
                        new StoreDetailTM(e.getBlId(),e.getBloodGroup(),e.getRackName(),e.getRackId(),e.getSpace(),e.getQty()));
            });
            tblStoreDetail.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
