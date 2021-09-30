package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import model.DonateDetail;
import util.controller.DonationController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class DailyStoreDetailFormController {
    public AnchorPane context;
    public LineChart<?,?> dailyStoreChart;


    public void initialize() throws IOException {
        loadChart();
    }

    private void loadChart(){
        XYChart.Series series=new XYChart.Series();
        XYChart.Series series2=new XYChart.Series();
        series.setName("QtyOnHand");
        series2.setName("TotalQty");

        try {

            loadData(series);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {

            loadIStoreData(series2);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dailyStoreChart.getData().addAll(series,series2);
    }

    private void loadIStoreData(XYChart.Series series2) throws SQLException, ClassNotFoundException {
        ArrayList<DonateDetail> dalyStoreData = DonationController.setUpDailyBarChart();
        //System.out.println(dalyItemIncomeData);
        for (DonateDetail temp : dalyStoreData
        ) {
            series2.getData().add(new XYChart.Data(String.valueOf(temp.getDate()), temp.getTotalQty(),temp.getrID()));
        }
    }

    private void loadData(XYChart.Series series) throws SQLException, ClassNotFoundException {
        ArrayList<DonateDetail> dalyStoreData1 = DonationController.setUpDailyBarChart();
        //System.out.println(dalyIncomeData);
        for (DonateDetail temp : dalyStoreData1
        ) {
            series.getData().add(new XYChart.Data(String.valueOf(temp.getDate()), temp.getQtyOnHand(),temp.getrID()));
        }
    }
}
