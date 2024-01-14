package com.codefornature;

import com.codefornature.dao.DonationDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.UserModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.sql.SQLException;

import javafx.scene.layout.HBox;

import java.io.BufferedWriter;

public class DonationController {
    @FXML
    private Button donateButton;
    private UserModel user;
    @FXML
    private AnchorPane donationContainer;
    @FXML
    private FlowPane organisationItems;
    @FXML
    private HBox amountContainer;
    @FXML
    private TextField donateAmountTxtField;
    @FXML
    private Label amountErrorMessage;
    private String organisation = null;
    private HBox selectedOrganisation = null;
    private Button selectedAmount = null;
    private int donateAmount = 0;
    private BorderPane mainContainer;

    public void initialize(){
        donationContainer.getStylesheets().add(getClass().getResource("/styles/donation.css").toExternalForm());

        //retrieve children (hbox) from flowpane first, then retrieve the child of the hbox
        //-----flowpane
        //--------hbox
        //----------label
        for(Node item : organisationItems.getChildren()){
            HBox organisationContainer = (HBox) item;
            Label organisationNameLabel = null;
            for(Node child : organisationContainer.getChildren()){
                if(child instanceof Label){
                    organisationNameLabel = (Label) child;
                }
            }
            final Label finalOrganisationNameLabel = organisationNameLabel;
            final HBox finalOranisationContainer = organisationContainer;
            organisationContainer.setOnMouseClicked(event -> {
                onOrganisationSelected(finalOrganisationNameLabel, finalOranisationContainer);
            });
        }

        //retrieve the first 4 amount buttons
        int i = 0;
        for(Node item : amountContainer.getChildren()){
            Button amountButton;
            amountButton = (Button) item;
            final Button finalAmountButton = amountButton;
            amountButton.setOnAction(event -> {
                onAmountSelected(finalAmountButton);
            });
            i++;
            if(i == 4) break;
        }
    }

    private void onAmountSelected(Button amountButton) {
        if(amountButton.equals(selectedAmount)){
            amountButton.getStyleClass().remove("on-selected");
            selectedAmount = null;
            donateAmount = 0;
        }
        else{
            if(selectedAmount != null){
                selectedAmount.getStyleClass().remove("on-selected");
            }
            //clear the text field
            amountErrorMessage.setText("");
            donateAmountTxtField.setText("");
            selectedAmount = amountButton;
            selectedAmount.getStyleClass().add("on-selected");
            String amountText = selectedAmount.getText().replaceAll("[^\\d.]", "");
            donateAmount = Integer.parseInt(amountText);
            System.out.println(donateAmount);
        }
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void onOrganisationSelected(Label organisationName, HBox organisationContainer){
        //if clicking on the same organisation, deselect it
        if (organisationContainer.equals(selectedOrganisation)) {
            organisationContainer.getStyleClass().remove("on-selected");
            selectedOrganisation = null;
            organisation = null;
            System.out.println("Organisation deselected");
        } else {
            //select the organisation
            if (selectedOrganisation != null) {
                selectedOrganisation.getStyleClass().remove("on-selected");
            }
            selectedOrganisation = organisationContainer;
            selectedOrganisation.getStyleClass().add("on-selected");
            organisation = organisationName.getText();
            System.out.println(organisation);
        }
    }

    public void donateOnAction(ActionEvent event) throws SQLException {
        if(selectedOrganisation == null){//check if user has selected an organisation
            AlertController.showAlert("No organisation selected", "Please select one of the organisation.", 0);
        }else if(selectedAmount != null || !donateAmountTxtField.getText().isEmpty()){//after checking if organisation is selected, it checks if amount is specified via selection or input
            if(selectedAmount == null){//if amount is not selected, that means amount is entered via input (textfield)
                try{
                    donateAmount = Integer.parseInt(donateAmountTxtField.getText());//ensure only integer number is entered, else an error will be thrown
                    if (donateAmount <= 0) {//ensure only positive numbers
                        amountErrorMessage.setText("Please enter a positive integer only");
                        return;
                    }
                } catch (NumberFormatException e){
                    amountErrorMessage.setText("Invalid amount.");
                    return;
                }
            }

            //Database operation
            DonationDAO donationDAO = new DonationDAO(donateAmount, organisation, user.getUsername());
            UserDAO userDAO = new UserDAO();
            int pointsAwarded = donateAmount * 10;
            userDAO.updatePoints(user.getUser_id(), pointsAwarded);//update points in database
            user.setPoints(user.getPoints() + pointsAwarded);//update points in current operation, to ensure gui updated
            donationDAO.insertDonation(user.getUser_id());//insert record into database
            donationDAO.writeDonationToFile();//insert record into text file
            AlertController.showAlert("DONATION", "Donation successful", 1);

            System.out.printf("%s has donated %d to %s%n", user.getUsername(), donateAmount, organisation);
        }
        else{
            amountErrorMessage.setText("No amount is entered or selected.");
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if (selectedAmount != null) {
            selectedAmount.getStyleClass().remove("on-selected");
            selectedAmount = null;
        }
        amountErrorMessage.setText("");
    }
}
