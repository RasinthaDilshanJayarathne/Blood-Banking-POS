package controller;

import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import model.OrderDetail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.controller.OrderController;

import java.sql.SQLException;
import java.util.ArrayList;

public class DailyOrderDetailFormController {
    public AnchorPane context;
    public LineChart<?,?> dailyOrderChart;

    public void initialize(){
        loadChart();
    }

    private void loadChart(){
        XYChart.Series series=new XYChart.Series();
        XYChart.Series series2=new XYChart.Series();
        series.setName("QtyOnHand");
        series2.setName("TotalQty");

        try {

            loadIStoreData(series2);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dailyOrderChart.getData().addAll(series,series2);
    }

    private void loadIStoreData(XYChart.Series series2) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> donateDetails = OrderController.setUpDailyOrderBarChart();
        //System.out.println(dalyItemIncomeData);
        for (OrderDetail temp : donateDetails
        ) {
            series2.getData().add(new XYChart.Data(String.valueOf(temp.getOrderDate()), temp.getQty()));
        }
    }

    public void printDailyOrderDetailOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/jasperReport/DailyOrderDetailReport.jrxml"));
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
