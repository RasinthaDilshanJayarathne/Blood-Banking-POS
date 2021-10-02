package controller;

import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BloodRack;
import model.Employee;
import model.OrderDetail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.controller.BloodRackController;
import util.controller.EmployeeController;
import util.controller.OrderController;
import view.tm.EmployeeTM;
import view.tm.OrderDetailTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailFormController {
    public TableView tblOrderDetail;
    public TableColumn colOrderId;
    public TableColumn colHID;
    public TableColumn colQty;
    public TableColumn colDate;
    public TextField txtSearch;
    public TableColumn colRackId;
    public LineChart<?,?> employeeBarChart;

    OrderController controller=new OrderController();

    public void initialize(){
        initTable();
        loadChart();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    public void loadChart(){
        XYChart.Series series=new XYChart.Series();
        series.setName("Order ID");

        try {
            loadIStoreData(series);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        employeeBarChart.getData().addAll(series);
    }

    private void loadIStoreData(XYChart.Series series) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> orderDetails = OrderController.setUpDailyBarChart();
        for (OrderDetail temp : orderDetails
        ) {
            series.getData().add(new XYChart.Data(temp.getOrderDate(),temp.getQty()));
        }
    }


    private void initTable(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colHID.setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
        colRackId.setCellValueFactory(new PropertyValueFactory<>("rackId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        try {
            setOrderDetailToTable(controller.getAllOrderDetail());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setOrderDetailToTable(ArrayList<OrderDetailTM> detailToTable) {
        ObservableList<OrderDetailTM> obList = FXCollections.observableArrayList();
        detailToTable.forEach(e->{
            obList.add(
                    new OrderDetailTM(e.getOrderId(),e.getHospitalId(),e.getRackId(),e.getQty(),e.getOrderDate()));
        });
        tblOrderDetail.setItems(obList);
    }

    public void searchOnAction(ActionEvent actionEvent) {
        search(txtSearch.getText());
    }
    private void search(String value) {
        ObservableList<OrderDetailTM> obList = FXCollections.observableArrayList();
        try {
            List<OrderDetailTM> orderDetailTMList = OrderController.searchOrderDetail(value);
            orderDetailTMList.forEach(e -> {
                obList.add(
                        new OrderDetailTM(e.getOrderId(),e.getHospitalId(),e.getRackId(), e.getQty(),e.getOrderDate()));
            });
            tblOrderDetail.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void prinOrderDetailOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/OrderDetail_Report.jrxml"));
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
