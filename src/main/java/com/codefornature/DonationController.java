package com.codefornature;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.util.ResourceBundle;

import java.net.URL;

import java.sql.Connection;
import java.sql.Statement;

import javafx.scene.layout.HBox;

import java.io.BufferedWriter;
import java.io.FileOutputStream;

public class DonationController implements Initializable {
    @FXML
    private Button donateButton;
    @FXML
    private Button ciButton;
    @FXML
    private Button edfButton;
    @FXML
    private Button tncButton;
    @FXML
    private Button wwfButton;
    @FXML
    private Button ocButton;
    @FXML
    private Button nrdcButton;
    @FXML
    private ImageView ciImageView;
    @FXML
    private ImageView edfImageView;
    @FXML
    private ImageView tncImageView;
    @FXML
    private ImageView wwfImageView;
    @FXML
    private ImageView ocImageView;
    @FXML
    private ImageView nrdcImageView;
    @FXML
    private TextField amountDonate;
    @FXML
    private Label donateMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File ciFile = new File("./src/main/resources/assets/images/donations/CI.png");
        Image ciImage = new Image(ciFile.toURI().toString());
        ciImageView.setImage(ciImage);

        File edfFile = new File("./src/main/resources/assets/images/donations/EDF.png");
        Image edfImage = new Image(edfFile.toURI().toString());
        edfImageView.setImage(edfImage);

        File tncFile = new File("./src/main/resources/assets/images/donations/TNC.png");
        Image tncImage = new Image(tncFile.toURI().toString());
        tncImageView.setImage(tncImage);

        File wwfFile = new File("./src/main/resources/assets/images/donations/WWF.png");
        Image wwfImage = new Image(wwfFile.toURI().toString());
        wwfImageView.setImage(wwfImage);

        File ocFile = new File("./src/main/resources/assets/images/donations/OC.png");
        Image ocImage = new Image(ocFile.toURI().toString());
        ocImageView.setImage(ocImage);

        File nrdcFile = new File("./src/main/resources/assets/images/donations/NRDC.png");
        Image nrdcImage = new Image(nrdcFile.toURI().toString());
        nrdcImageView.setImage(nrdcImage);

        Button button1 = ciButton;
        Button button2 = edfButton;
        Button button3 = tncButton;
        Button button4 = wwfButton;
        Button button5 = ocButton;
        Button button6 = nrdcButton;

        setButtonClickAction(button1);
        setButtonClickAction(button2);
        setButtonClickAction(button3);
        setButtonClickAction(button4);
        setButtonClickAction(button5);
        setButtonClickAction(button6);
    }
    public void ciOnAction(ActionEvent event){
        System.out.println(ciButton.getText());
    }

    private double getDonationAmount() {
        try {
            // Attempt to parse the amount from the TextField
            return Double.parseDouble(amountDonate.getText());
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid double
            return 0.0;
        }
    }

    private void setButtonClickAction(Button button) {
        button.setOnAction(event -> {
            String buttonText = button.getText();
            double amount = getDonationAmount();
            writeToFile(buttonText, amount);
        });
    }

    public void donateOnAction(ActionEvent event){
        donateMessage.setText("Donation has been received.");

        if (amountDonate.getText().isEmpty()) {
            // Handle the case where no amount is entered
            donateMessage.setText("No amount is entered.");
        } else {
            //Perform actions when an amount is entered
            //writeToFile();
            //You can use the 'amount' variable for further processing
        }
    }

    public void createAccountForm(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("donation-view.fxml"));
            Stage registerStage = new Stage();
            registerStage.setTitle("Donation");
            registerStage.setScene(new Scene(root, 1000, 800));
            registerStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void writeToFile(String organizationName, double amount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("donation.txt", true))) {
            //still need get userID and write to file
            writer.write(organizationName + "," + amount);
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
