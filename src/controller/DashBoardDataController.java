package controller;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import util.controller.DonationController;
import util.controller.DonorController;
import util.controller.EmployeeController;
import util.controller.HospitalController;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashBoardDataController {

    public Label totEmployee;
    public Label totDonors;
    public Label totHospitals;
    public Label totDonations;
    public Label totOrder;
    public LineChart<String,Integer> donateDetail;
    public BarChart<String,Integer> orderDetail;
    public Label lblDate;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCount();
        loadChart();
        loadDateAndTime();
    }

    public void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
    }

    public void loadChart() throws SQLException, ClassNotFoundException {
        donateDetail.getData().add(new DonationController().setUpBarChartFromDatabase());
    }



    public void setCount() throws SQLException, ClassNotFoundException {
        totDonors.setText(String.valueOf(new DonorController().donorCount()));
        totEmployee.setText(String.valueOf(new EmployeeController().employeeCount()));
        totHospitals.setText(String.valueOf(new HospitalController().hospitalCount()));
        totDonations.setText(String.valueOf(new DonationController().donationCount()));
    }
}
