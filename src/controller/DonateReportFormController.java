package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class DonateReportFormController {

    public AnchorPane context;

    public void initialize() throws IOException {
        URL resource = getClass().getResource("../view/DailyOrderDetailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        context.getChildren().clear();
        context.getChildren().add(load);
    }

    public void dailyReportOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/DailyOrderDetailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        context.getChildren().clear();
        context.getChildren().add(load);
    }

    public void monthlyOnAction(ActionEvent actionEvent) throws IOException {
        /*URL resource = getClass().getResource("../view/DailyStoreDetailForm.fxml");
        Parent load = FXMLLoader.load(resource);
        context.getChildren().clear();
        context.getChildren().add(load);*/
    }

    public void annualyOnAction(ActionEvent actionEvent) {

    }
}
