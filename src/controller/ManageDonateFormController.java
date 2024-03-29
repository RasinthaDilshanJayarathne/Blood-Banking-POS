package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;
import util.ValidationUtil;
import util.controller.BloodRackController;
import util.controller.DonationController;
import util.controller.DonorController;
import view.tm.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ManageDonateFormController extends StoreDetail {
    public TableView<DonorTM> tblDonor;
    public TableColumn colNIC;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colType;
    public TableColumn colGender;
    public TableColumn colEmail;
    public TableColumn colPhoneNo;
    public TableView<StoreDetailTM> tblBloodRack;
    public TableColumn colBloodType;
    public TableColumn colQty;
    public TableColumn colUserID;
    public TableColumn colRackNo;
    public ComboBox<String> cmbRackNo;
    public TableColumn colStore;
    public TextField txtDonateQTY;
    public TextField txtType;
    public TextField txtDonID;
    public TextField txtDonateDate;
    public TextField txtDonateTime;
    public Button btnDonate;
    public TableColumn colRackID;
    public TableColumn colBloodID;
    public TextField txtBloodID;
    public TextField txtRackID;
    public TextField txtAvailableQty;
    public TextField txtStoreSearch;
    public TextField txtDonorSearch;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    //Pattern nicPattern = Pattern.compile("^[0-9]{12}|[0-9]{11}[A-Z]$");
    //Pattern bloodTypePattern = Pattern.compile("^[A-Z]{1,2}[+]|[A-Z]{1,2}[-]$");
    Pattern qtyPattern = Pattern.compile("^[0-9]{1}$");

    public DonorController controller=new DonorController();

    public void initialize() throws SQLException, ClassNotFoundException {
        btnDonate.setDisable(true);
        initDonorTable();
        initBloodRack();
        loadRackName();
        storeValidations();
        //getDonor();
        setDonorToTable(new DonorController().getAllDonor());

        txtDonateQTY.textProperty().addListener((observable, oldValue, newValue) -> {
            loadDateTime();
        });

        cmbRackNo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getRackIds(newValue);
            }
        });

        txtDonorSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchDonor(newValue);
            }
        });

        txtStoreSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchStore(newValue);
            }
        });
       txtType.textProperty().addListener((observable, oldValue, newValue) -> {
           try {
               name.clear();
               name=new BloodRackController().loadRackNameByBloodType(newValue,name);
               cmbRackNo.getItems().clear();
               cmbRackNo.setItems((FXCollections.observableArrayList(name)));
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
       });
       if (donorTM!=null){
           txtDonID.setText(donorTM.getNic());
           txtType.setText(donorTM.getType());
           txtBloodID.setText(donorTM.getBlID());
       }else {

       }
    }

    private void storeValidations() {
        map.put(txtDonateQTY, qtyPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnDonate);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void loadDateTime(){
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtDonateDate.setText(java.time.LocalDateTime.now().format(dateFormatter));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");
            txtDonateTime.setText(java.time.LocalDateTime.now().format(timeFormatter));
    }

    public void initDonorTable() throws SQLException, ClassNotFoundException {
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colPhoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colBloodID.setCellValueFactory(new PropertyValueFactory<>("blID"));

        tblDonor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadDonorData(newValue);
        });
    }

    public void getRackIds(String id){
       try {
           try {
               BloodRack rack = new BloodRackController().getRackIds(id);
               txtRackID.setText(rack.getId());

               if(!new DonationController().checkAvailabilityOrNot(txtRackID.getText())){
                    txtAvailableQty.setText(new BloodRackController().getCapacity(txtRackID.getText()));
               }else {
                   String qty = new DonationController().getAvailability(txtRackID.getText());
                   txtAvailableQty.setText(qty);
               }
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
       }catch (Exception e){
       }
    }

    private void loadDonorData(DonorTM tm) {
        txtDonID.setText(tm.getNic());
        txtType.setText(tm.getType());
        txtBloodID.setText(tm.getBlID());
    }

    List<String>name =new ArrayList<>();
    public void loadRackName() throws SQLException, ClassNotFoundException {
        name = BloodRackController.getBloodRackName();
        cmbRackNo.getItems().addAll(name);
    }

    int x=-1;
    public void initBloodRack() throws SQLException, ClassNotFoundException {
        colBloodType.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        colRackNo.setCellValueFactory(new PropertyValueFactory<>("rackName"));
        colRackID.setCellValueFactory(new PropertyValueFactory<>("rackId"));
        colStore.setCellValueFactory(new PropertyValueFactory<>("space"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        ArrayList<StoreDetailTM>arrayList=new ArrayList<>();

        for (BloodRack tm:new BloodRackController().getAllRack()) {
            StoreDetailTM detailTM=new StoreDetailTM(
                    tm.getBlId(),
                    tm.getBloodType(),
                    tm.getId(),
                    tm.getName(),
                    tm.getQty(),
                    tm.getStoreQty()
            );
            arrayList.add(detailTM);
        }
        setDonateDetailToTable(arrayList);
        tblBloodRack.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
             x = (int) newValue;
        });
    }

    ObservableList<StoreDetailTM> obList = FXCollections.observableArrayList();
    private void setDonateDetailToTable(ArrayList<StoreDetailTM> allDonateDetailToTable) {
        allDonateDetailToTable.forEach(e->{
            obList.add(
                    new StoreDetailTM(e.getBlId(),e.getBloodGroup(),e.getRackId(),e.getRackName(),e.getSpace(),e.getQty()));
        });
        tblBloodRack.setItems(obList);
    }

    static DonorTM donorTM=null;
    public void loadNewDonor(DonorTM donor1){
        donorTM=donor1;
    }

    public void setDonorToTable(ArrayList<Donor> allDonor) {
        ObservableList<DonorTM> obList = FXCollections.observableArrayList();

        for (DonorTM tm:obList) {
            if (allDonor.equals(tm)){
                try {
                    saveDonateDetail();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                allDonor.forEach(e->{
                    obList.add(
                            new DonorTM(e.getNic(),e.getUserID(),e.getName(),e.getAddress(),e.getCity(),e.getType(),e.getBlID(),e.getGender(),e.getPhoneNo(),e.getEmail()));
                });
                tblDonor.setItems(obList);
            }
        }

        allDonor.forEach(e->{
            obList.add(
                    new DonorTM(e.getNic(),e.getUserID(),e.getName(),e.getAddress(),e.getCity(),e.getType(),e.getBlID(),e.getGender(),e.getPhoneNo(),e.getEmail()));
        });
        tblDonor.setItems(obList);
    }

    public void donateOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        List<String> donorNicList=new DonorController().getDonorNic();
        String nic=null;
            for (String s : donorNicList) {
                if (txtDonID.getText().equals(s)) {
                    nic = s;
                    DonateDetail detail = new DonateDetail(
                            txtBloodID.getText(),
                            txtRackID.getText(),
                            cmbRackNo.getValue().toString(),
                            nic,
                            txtDonateDate.getText(),
                            txtDonateTime.getText(),
                            Integer.parseInt(txtDonateQTY.getText()),
                            Integer.parseInt(txtAvailableQty.getText())
                    );
                    new DonorController().saveDonateDetail(detail);
                    new DonorController().updateBloodRackStoreQty(txtRackID.getText(), Integer.parseInt(txtDonateQTY.getText()));
                    sendMail();
                    clear();
                    new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
                    setDonateDetailToTable(controller.getAllDetail());
                    return;
                }
            }
        saveDonateDetail();
        clear();
        setDonateDetailToTable(controller.getAllDetail());
        setDonorToTable(new DonorController().getAllDonor());

    }

    private void sendMail() throws IOException {
        URL resource = getClass().getResource("../view/SendMailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene=new Scene(load);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void saveDonateDetail() throws SQLException, ClassNotFoundException {
        if (updateRowQty()==true){
            DonateDetail donateDetail= new DonateDetail(
                    txtBloodID.getText(),txtRackID.getText(),cmbRackNo.getValue(),txtDonID.getText(),txtDonateDate.getText(),txtDonateTime.getText(),Integer.parseInt(txtDonateQTY.getText()),Integer.parseInt(txtAvailableQty.getText())
            );

            if (new DonorController().saveDonor(donor,donateDetail)){
                try {
                    sendMail();
                    updateRowQty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
            }
        }
    }

    static Donor donor;
    public void getDonor(Donor donorOne){
        donor=donorOne;
    }

    private void clear(){
        donorTM=null;
        //tblBloodRack.getItems().clear();
        obList.clear();txtDonID.clear();txtType.clear();cmbRackNo.getSelectionModel().clearSelection();txtDonateQTY.clear();
        txtDonateDate.clear();txtDonateTime.clear();txtBloodID.clear();txtRackID.clear();txtAvailableQty.clear();
    }

    public boolean updateRowQty() {
        int qtyOnHand = Integer.parseInt(txtAvailableQty.getText());
        int donateQty = Integer.parseInt(txtDonateQTY.getText()+"");
        String blId=txtBloodID.getText();
        String bloodGroup=txtType.getText();
        String rackName=cmbRackNo.getSelectionModel().getSelectedItem();
        String rackId=txtRackID.getText();

        if (qtyOnHand<donateQty){
            new Alert(Alert.AlertType.WARNING,"Invalid Quantity").show();
            return false;
        }else {
            StoreDetailTM tm = new StoreDetailTM(
                    blId,
                    bloodGroup,
                    rackName,
                    rackId,
                    qtyOnHand,
                    donateQty
            );

            int rowNumber = isExists(tm);

            if (rowNumber == -1) {
                // new Add
                obList.add(tm);
            } else {
                // update
                StoreDetailTM temp = obList.get(rowNumber);

                if (temp.getSpace() < donateQty) {
                    StoreDetailTM newTm = new StoreDetailTM(
                            temp.getBlId(),
                            temp.getBloodGroup(),
                            temp.getRackName(),
                            temp.getRackId(),
                            temp.getSpace(),
                            temp.getQty() + donateQty
                    );
                    obList.remove(rowNumber);
                    obList.add(newTm);
                }
                else{
                    new Alert(Alert.AlertType.WARNING,"Invalid Quantity").show();
                }
            }
            //tblBloodRack.getItems().clear();
            tblBloodRack.setItems(obList);
            tblBloodRack.refresh();
            if (!obList.isEmpty()) {
                btnDonate.setDisable(false);
            }
        }
        return true;
    }

    private int isExists(StoreDetailTM tm) {
            for (int i = 0; i < obList.size(); i++) {
                if (tm.getRackId().equals(obList.get(i).getRackId())){
                    return i;
                }
            }
            return -1;
        }

    public void txtStoreSearchOnAction(ActionEvent actionEvent) {
        searchStore(txtStoreSearch.getText());
    }

    public void txtDonorSearchOnAction(ActionEvent actionEvent) {
        searchDonor(txtDonorSearch.getText());
    }

    private void searchDonor(String value){
        ObservableList<DonorTM> obList = FXCollections.observableArrayList();
        try {
            List<Donor> donors = DonorController.searchDonor(value);

            donors.forEach(e->{
                obList.add(
                        new DonorTM(e.getNic(),e.getUserID(),e.getName(),e.getAddress(),e.getCity(),e.getType(),e.getBlID(),e.getGender(),e.getPhoneNo(),e.getEmail()));
            });
            tblDonor.getItems().setAll(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void searchStore(String value) {
        ObservableList<StoreDetailTM> obList = FXCollections.observableArrayList();
        try {
            List<StoreDetail> storeDetails = DonationController.searchStore(value);

            storeDetails.forEach(e->{
                obList.add(
                        new StoreDetailTM(e.getBlId(),e.getBloodGroup(),e.getRackName(),e.getRackId(),e.getSpace(),e.getQty()));
            });
            tblBloodRack.setItems(obList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
