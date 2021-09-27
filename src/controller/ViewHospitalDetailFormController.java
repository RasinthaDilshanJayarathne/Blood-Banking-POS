package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Hospital;
import util.controller.HospitalController;
import view.tm.HospitalTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewHospitalDetailFormController {
    public TableView<HospitalTM> tblHospital;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colWebsite;
    public TableColumn colEmail;
    public TableColumn colPhoneNo;
    public TextField txtSearch;


    public void initialize() throws SQLException, ClassNotFoundException {
        initTable();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search(newValue);
            }
        });
    }

    public void initTable() throws SQLException, ClassNotFoundException {
        colID.setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colWebsite.setCellValueFactory(new PropertyValueFactory<>("website"));

        setHospitalToTable(new HospitalController().getAllHospital());
    }

    private void setHospitalToTable(ArrayList<Hospital> allHospital) {
        ObservableList<HospitalTM> obList = FXCollections.observableArrayList();
        allHospital.forEach(e->{
            obList.add(
                    new HospitalTM(e.getHospitalId(),e.getName(),e.getEmail(),e.getAddress(),e.getCity(),e.getPhoneNo(),e.getEmail()));
        });
        tblHospital.setItems(obList);
    }

    public void searchOnAction(ActionEvent actionEvent) {
        search(txtSearch.getText());
    }

    private void search(String value){
        ObservableList<HospitalTM> obList = FXCollections.observableArrayList();
        try {
            List<Hospital> hospitals = HospitalController.searchHospital(value);

            hospitals.forEach(e->{
                obList.add(
                        new HospitalTM(e.getHospitalId(),e.getName(),e.getEmail(),e.getAddress(),e.getCity(),e.getPhoneNo(),e.getEmail()));
            });
            tblHospital.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printHospitalOnAction(ActionEvent actionEvent) {
        new ManageHospitalFormController().printHospitalOnAction(actionEvent);
    }
}
