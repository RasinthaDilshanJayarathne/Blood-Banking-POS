package controller;

import javafx.scene.control.Alert;
import model.Donor;
import util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.controller.DonorController;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SendMailFormController extends ManageDornorFormController{
    public TextField txtSubject;
    public TextField txtToEmail;
    public Button btnSending;
    public TextField txtMassages;
    public TextField txtFromEmail;
    public TextField txtSearch;

    private DonorController controller=new DonorController();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern toEmailPattern = Pattern.compile("^[a-z0-9]{3,}[@](gmail)[.][a-z]{3,}$");
    Pattern fromEmailPattern = Pattern.compile("^[a-z0-9]{3,}[@](gmail)[.][a-z]{3,}$");
    Pattern subjectPattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern messagePattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");

    public void initialize(){
        btnSending.setDisable(true);
        storeValidations();
    }

    private void storeValidations() {
        map.put(txtToEmail, toEmailPattern);
        map.put(txtFromEmail, fromEmailPattern);
        map.put(txtSubject,subjectPattern);
        map.put(txtMassages,messagePattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = ValidationUtil.validate(map,btnSending);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void sendingEmail(ActionEvent actionEvent) {
        String ToEmail=txtToEmail.getText();
        String FromEmail=txtFromEmail.getText();
        String FromEmailPassword="rasintha123";
        String Subjects=txtSubject.getText();

        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session=Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FromEmail,FromEmailPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(ToEmail));
            message.setSubject(Subjects);
            String massege=txtMassages.getText();
            message.setText(massege);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        new Alert(Alert.AlertType.CONFIRMATION, "Mail Send......").showAndWait();

    }

    public void searchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       /* String donorNic =;
        System.out.println(txtNIC);

        Donor d1= new DonorController().getDonor(donorNic);
        if (d1==null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(d1);
        }*/
    }

    private void setData(Donor d1) {
        txtToEmail.setText(d1.getEmail());
    }
}
