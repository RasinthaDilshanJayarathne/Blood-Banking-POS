package controller;

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
import model.DonateDetail;
import model.Hospital;
import model.StoreDetail;
import util.ValidationUtil;
import util.controller.BloodRackController;
import util.controller.HospitalController;
import util.controller.OrderController;
import view.tm.CartTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ManageOrderFormController {

    public TextField txtOrderQty;
    public Button btnAdd;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colBType;
    public TableColumn colSQty;
    public TableColumn colOQty;
    public TableColumn colDate;
    public TableColumn colTime;
    public Button btnDelete;
    public Button btnUpdate;
    public TextField txtOrderID;
    public TextField txtOrderDate;
    public TextField txtOrderTime;
    public TextField txtName;
    public ComboBox<String> cmbHospital;
    public ComboBox<String> cmbBloodType;
    public ComboBox<String> cmbBloodRackName;
    public TableView tblOrder;
    public TextField txtAvailableQty;
    public Button btnPlaceOrder;
    public TableColumn colRackName;

    int cartSelectedRowForRemove = -1;

    OrderController controller=new OrderController();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern orderQTYPattern = Pattern.compile("^[0-9]{1,3}$");

    public void initialize() throws SQLException, ClassNotFoundException {
        btnAdd.setDisable(true);
        storeValidations();
        getHospitalIds();
        hospitalBloodType();
        loadRackName();
        setOrderId();
        setIniTable();

        cmbHospital.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                getHospitalData(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        cmbBloodRackName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!(controller.getAvailability(newValue) ==null)){
                    txtAvailableQty.setText(controller.getAvailability(newValue));
                    int i = updateAvailabilityQty(Integer.parseInt(controller.getAvailability(newValue)));
                    txtAvailableQty.setText(String.valueOf(i));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        txtOrderQty.textProperty().addListener((observable, oldValue, newValue) -> {
            loadDateTime();
        });

        cmbBloodType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                name.clear();
                name=new BloodRackController().loadRackNameByBloodType(newValue,name);
                cmbBloodRackName.getItems().clear();
                cmbBloodRackName.setItems((FXCollections.observableArrayList(name)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void setIniTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("hospitalName"));
        colBType.setCellValueFactory(new PropertyValueFactory<>("blType"));
       // colSQty.setCellValueFactory(new PropertyValueFactory<>("availableQty"));
        colRackName.setCellValueFactory(new PropertyValueFactory<>("rackName"));
        colOQty.setCellValueFactory(new PropertyValueFactory<>("orderQty"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

    }

    /*private void setOrderToTable(ArrayList<Order> allOrders) {
        ObservableList<OrderTM> obList = FXCollections.observableArrayList();
        allOrders.forEach(e->{
            obList.add(
                    new OrderTM(e.gethId(),e.gethName(),e.getBlType(),e.getAvailableQty(),e.getrName(),e.getOrderQty(),e.getDate(),e.getTime()));
        });
        tblOrder.setItems(obList);
    }*/

    public void loadDateTime(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtOrderDate.setText(java.time.LocalDateTime.now().format(dateFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");
        txtOrderTime.setText(java.time.LocalDateTime.now().format(timeFormatter));
    }

    private void setOrderId() {
        try {
            txtOrderID.setText(controller.getOrderId());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getHospitalIds() throws SQLException, ClassNotFoundException {
        List<String> hospitalIds = new HospitalController().getHospitalIds();
        cmbHospital.getItems().addAll(FXCollections.observableArrayList(hospitalIds));
    }

    public void getHospitalData(String id) throws SQLException, ClassNotFoundException {
        try {
            Hospital hospital = new HospitalController().getHospital(id);
            txtName.setText(hospital.getName());
        }catch (Exception e){

        }
    }
    private void hospitalBloodType() throws SQLException, ClassNotFoundException {
        List<String> bloodType = BloodRackController.getBloodType();
        cmbBloodType.getItems().addAll(FXCollections.observableArrayList(bloodType));
    }
    List<String>name =new ArrayList<>();
    public void loadRackName() throws SQLException, ClassNotFoundException {
        name = BloodRackController.getBloodRackName();
        cmbBloodRackName.getItems().addAll(name);
    }

    private void storeValidations() {
        map.put(txtOrderQty, orderQTYPattern);
    }

    private void mail() throws IOException {
        URL resource = getClass().getResource("../view/SendMailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map, btnAdd);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    private void clear(){
        try {
            txtOrderDate.clear();txtOrderTime.clear();cmbHospital.getSelectionModel().clearSelection();
            txtName.clear();cmbBloodType.getSelectionModel().clearSelection();cmbBloodRackName.getSelectionModel().clearSelection();
            txtAvailableQty.clear();txtOrderQty.clear();
        }catch (Exception e){

        }

    }
    private void clearOne(){
        try {
            cmbBloodType.getSelectionModel().clearSelection();cmbBloodRackName.getSelectionModel().clearSelection();
            txtAvailableQty.clear();txtOrderQty.clear();
        }catch (Exception e){

        }
    }

    public void deleteOrderOnAction(ActionEvent actionEvent) {
    }

    private int updateAvailabilityQty(int availableQty){
        int orderQtyList;
        for (CartTM temp:obList) {
            if (cmbBloodType.getValue().equals(temp.getBlType()) && cmbBloodRackName.getValue().equals(temp.getRackName())) {
                orderQtyList = temp.getOrderQty();
                availableQty = availableQty - orderQtyList;
            }
        }
        return availableQty;
    }

    ObservableList<CartTM> obList= FXCollections.observableArrayList();
    public void addOrderOnAction(ActionEvent actionEvent) {

        String hId = cmbHospital.getSelectionModel().getSelectedItem();
        String hName=txtName.getText();
        String blType = cmbBloodType.getSelectionModel().getSelectedItem();
        int availableQty= Integer.parseInt(txtAvailableQty.getText());
        String rackName = cmbBloodRackName.getSelectionModel().getSelectedItem();
        int orderQty = Integer.parseInt(txtOrderQty.getText());
        String date=txtOrderDate.getText();
        String time=txtOrderTime.getText();

        updateAvailabilityQty(availableQty);

        if (availableQty<orderQty){
            new Alert(Alert.AlertType.WARNING,"Invalid QTY").show();
            return;
        }

        CartTM tm = new CartTM(
                hId,
                hName,
                blType,
                orderQty,
                rackName,
                date,
                time

        );

        int rowNumber=isExists(tm);

        if (rowNumber==-1){// new Add
            obList.add(tm);
        }else{
            // update
            CartTM temp = obList.get(rowNumber);
            CartTM newTm = new CartTM(
                    temp.getHospitalId(),
                    temp.getHospitalName(),
                    temp.getBlType(),
                    temp.getOrderQty()+orderQty,
                    temp.getRackName(),
                    temp.getDate(),
                    temp.getTime()
            );

            obList.remove(rowNumber);
            obList.add(newTm);
        }
        tblOrder.setItems(obList);

       /* try {
            String newAvailabitlity=new OrderController().updateNewAvalibilityQty(cmbBloodRackName.getValue(),txtOrderQty.getText(),cmbBloodType.getValue());
            txtAvailableQty.setText(newAvailabitlity);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        clearOne();
        cmbHospital.setDisable(true);
        txtName.setDisable(true);
    }

    private int isExists(CartTM tm) {
        for (int i = 0; i < obList.size(); i++) {
            if (tm.getBlType().equals(obList.get(i).getBlType())){
                return i;
            }
        }
        return -1;
    }

    public void updateOrderOnAction(ActionEvent actionEvent) {

    }

    public void clearOnAction(ActionEvent actionEvent) {
       /* if (cartSelectedRowForRemove==-1){
            new Alert(Alert.AlertType.WARNING, "Please Select a row").show();
        }else{
            obList.remove(cartSelectedRowForRemove);
            if (obList.isEmpty()){
                btnPlaceOrder.setDisable(true);
            }
            //calculateCost();
            tblOrder.refresh();
        }*/
        clearOne();

    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws IOException {
        cmbHospital.setDisable(false);
        txtName.setDisable(false);
        clear();
        tblOrder.getItems().clear();
        //setOrderId();
        mail();

    }
}
