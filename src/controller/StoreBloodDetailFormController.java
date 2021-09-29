package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DonateDetail;
import model.StoreDetail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.controller.DonationController;
import view.tm.DonateDetailTM;
import view.tm.StoreDetailTM;

import java.sql.SQLException;
import java.util.ArrayList;

public class StoreBloodDetailFormController{

    public TableView tblStoreDetail;
    public TableColumn colRackID;
    public TableColumn colQty;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colBloodID;
    public BarChart<String,Integer> donateBloodDetailBarChart;

    DonationController controller=new DonationController();

    public void initialize(){
        initTable();
        try {
            loadChart();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadChart() throws SQLException, ClassNotFoundException {
        donateBloodDetailBarChart.getData().add(new DonationController().setUpBarChartFromDatabase());
    }

    public void initTable(){
       /* colRackID.setCellValueFactory(new PropertyValueFactory<>("rID"));
        colBloodID.setCellValueFactory(new PropertyValueFactory<>("blID"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        //colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        //colTime.setCellValueFactory(new PropertyValueFactory<>("time"));*/

        try {
            setDonateDetailToTable(new DonationController().getAllDonateDetail());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void setDonateDetailToTable(ArrayList<StoreDetail> allDonateDetailToTable) {
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
}
