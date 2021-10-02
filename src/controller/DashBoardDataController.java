package controller;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import model.BloodRack;
import model.OrderDetail;
import util.controller.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashBoardDataController {

    public Label totEmployee;
    public Label totDonors;
    public Label totHospitals;
    public Label totDonations;
    public Label totOrder;
    public LineChart<?,?> donateDetail;
    public BarChart<?,?> orderDetail;
    public Label lblDate;
    public Label lblDate1;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCount();
        loadChart();
        loadOneChart();
        loadDateAndTime();
    }

    public void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
        lblDate1.setText(f.format(date));
    }

    private void loadChart() throws SQLException, ClassNotFoundException {
        XYChart.Series series=new XYChart.Series();
        series.setName("Rack Name");

        loadData(series);
        donateDetail.getData().addAll(series);
    }

    private void loadData(XYChart.Series series) throws SQLException, ClassNotFoundException {
        ArrayList<BloodRack> bloodRacks = BloodRackController.setUpDailyBarChart();
        for (BloodRack temp : bloodRacks
        ) {
            series.getData().add(new XYChart.Data(String.valueOf(temp.getName()), temp.getQty()));
        }
    }

    private void loadOneChart() throws SQLException, ClassNotFoundException {
        XYChart.Series series=new XYChart.Series();
        series.setName("Rack Name");

        loadBarChartData(series);
        orderDetail.getData().addAll(series);
    }

    private void loadBarChartData(XYChart.Series series) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> orderDetails = OrderController.setUpDailyOrderBarChartOne();
        for (OrderDetail temp : orderDetails
        ) {
            series.getData().add(new XYChart.Data(String.valueOf(temp.getRackId()), temp.getQty()));
        }
    }

    public void setCount() throws SQLException, ClassNotFoundException {
        totDonors.setText(String.valueOf(new DonorController().donorCount()));
        totEmployee.setText(String.valueOf(new EmployeeController().employeeCount()));
        totHospitals.setText(String.valueOf(new HospitalController().hospitalCount()));
        totDonations.setText(String.valueOf(new DonationController().donationCount()));
        totOrder.setText(String.valueOf(new DonationController().donationCount()));
    }
}
