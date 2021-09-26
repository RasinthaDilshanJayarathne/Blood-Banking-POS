package controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class ReceptionDashBoardFormController {

    public Label menuBack;
    public Label menu;
    public Label lblClose;
    public AnchorPane context;
    public AnchorPane slider;
    public AnchorPane loadContext;
    public Label lblTime;
    public Label lblDate;
    public AnchorPane slider1;

    public void initialize() throws IOException {

        URL resource = getClass().getResource("../view/DashBoardData.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);

        loadDateAndTime();

        lblClose.setOnMouseClicked(event -> {
            System.exit(0);
        });

        slider.setTranslateX(-360);
        menu.setOnMouseClicked(event -> {
            TranslateTransition transition=new TranslateTransition();
            transition.setDuration(Duration.seconds(0.4));
            transition.setNode(slider);

            transition.setToX(0);
            transition.play();

            slider.setTranslateX(-360);

            transition.setOnFinished((ActionEvent e)->{
                menu.setVisible(false);
                menuBack.setVisible(true);
            });
        });
        menuBack.setOnMouseClicked(event -> {
            TranslateTransition transition=new TranslateTransition();
            transition.setDuration(Duration.seconds(0.4));
            transition.setNode(slider);
            transition.setToX(-360);
            transition.play();

            slider.setTranslateX(0);

            transition.setOnFinished((ActionEvent e)->{
                menu.setVisible(true);
                menuBack.setVisible(false);
            });
        });
    }

    private void loadDateAndTime() {
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();

            String state = null;
            if (currentTime.getHour() >= 12) {
                state = "PM";
            } else {
                state = "AM";
            }
            lblTime.setText("" + String.format("%02d",currentTime.getHour()) + ":" + String.format("%02d", currentTime.getMinute()) + ":" + String.format("%02d", currentTime.getSecond())+" "+state);

        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void dashboardOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/DashBoardData.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void manageBloodOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageBloodForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void manageDonorOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageDornorForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void manageBloodRackOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageBloodRackForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void manageOrderOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageOrderForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void viewHospitalOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ViewHospitalDetailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void storeDetailOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/StoreBloodDetailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/LoginForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) context.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    public void helpOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/HelpForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene=new Scene(load);
        Stage stage=new Stage();
        stage.setTitle("Help Center");
        stage.setScene(scene);
        stage.show();
    }

    public void donateOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/ManageDonateForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }

    public void orderDetailOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/OrderDetailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        loadContext.getChildren().clear();
        loadContext.getChildren().add(load);
    }
}
