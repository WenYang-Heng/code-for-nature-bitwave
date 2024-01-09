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
            setDonateAmount(0);
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
            setDonateAmount(Integer.parseInt(amountText));
            System.out.println(getDonateAmount());
        }
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public int getDonateAmount(){
        return donateAmount;
    }

    public void setDonateAmount(int donateAmount){
        this.donateAmount = donateAmount;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void onOrganisationSelected(Label organisationName, HBox organisationContainer){
        //if clicking on the same organisation, deselect it
        if (organisationContainer.equals(selectedOrganisation)) {
            organisationContainer.getStyleClass().remove("on-selected");
            selectedOrganisation = null;
            setOrganisation(null);
            System.out.println("Organisation deselected");
        } else {
            //select the organisation
            if (selectedOrganisation != null) {
                selectedOrganisation.getStyleClass().remove("on-selected");
            }
            selectedOrganisation = organisationContainer;
            selectedOrganisation.getStyleClass().add("on-selected");
            setOrganisation(organisationName.getText());
            System.out.println(getOrganisation());
        }
    }

    private double getDonationAmount() {
        try {
            // Attempt to parse the amount from the TextField
            return Double.parseDouble(donateAmountTxtField.getText());
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid double
            return 0.0;
        }
    }

    public void donateOnAction(ActionEvent event) throws SQLException {
        if(selectedOrganisation == null){
            AlertController.showAlert("No organisation selected", "Please select one of the organisation.", 0);
        }else if(selectedAmount != null || !donateAmountTxtField.getText().isEmpty()){
            if(selectedAmount == null){
                try{
                    donateAmount = Integer.parseInt(donateAmountTxtField.getText());
                } catch (NumberFormatException e){
                    amountErrorMessage.setText("Please enter number only");
                    return;
                }
            }
            AlertController.showAlert("Donation", "Donation has been received. Thank you.", 1);

            DonationDAO donationDAO = new DonationDAO(donateAmount, organisation, user.getUsername());
            UserDAO userDAO = new UserDAO();
            int pointsAwarded = donateAmount * 10;
            userDAO.updatePoints(user.getUser_id(), pointsAwarded);
            user.setPoints(user.getPoints() + pointsAwarded);
            donationDAO.insertDonation(user.getUser_id());
            donationDAO.writeDonationToFile();
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
