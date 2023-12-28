package com.codefornature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class register3Controller {

    @FXML
    private ImageView emailImageView;
    @FXML
    private ImageView usernameImageView;
    @FXML
    private ImageView passwordImageView;
    @FXML
    private ImageView confirmImageView;
    @FXML
    private ImageView pineImageView;
    @FXML
    private ImageView forestImageView;
    @FXML
    private ImageView arrowImageView;
    @FXML
    private Button registerButton;
    @FXML
    private Label registerMessageLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Pane emailPane;
    @FXML
    private Pane usernamePane;
    @FXML
    private Pane passwordPane;
    @FXML
    private Pane confirmPane;


    private void register(ActionEvent event) {
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
    }

    @FXML
    public void initialize() {
        addFocusListener(emailTextField, emailPane);
        addFocusListener(usernameTextField, usernamePane);
        addFocusListener(setPasswordField, passwordPane);
        addFocusListener(confirmPasswordField, confirmPane);
    }

    private void addFocusListener(TextField textField, Pane pane) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) ->
                updateBorder(pane, newValue));
    }

    private void addFocusListener(PasswordField passwordField, Pane pane) {
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) ->
                updateBorder(pane, newValue));
    }

    private void updateBorder(Pane pane, boolean focused) {
        pane.setStyle("-fx-border-color: " + (focused ? "transparent transparent #73d960 transparent; -fx-border-width: 0 0 3 0;" : "transparent transparent #b5b5b5 transparent; -fx-border-width: 0 0 3 0;"));
    }


    public void registerButtonOnAction(ActionEvent event) {
        if (emailTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || setPasswordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            confirmPasswordLabel.setText("Kindly complete all the blanks.");
        } else {
            if (setPasswordField.getText().equals(confirmPasswordField.getText())) {
                confirmPasswordLabel.setText("");
                registerMessageLabel.setText("User has been registered successfully!");
            } else {
                confirmPasswordLabel.setText("Password does not match!");
            }
        }
    }


    @FXML
    private void closeButtonOnAction() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void arrowImageViewOnMouseClicked(MouseEvent event) {
        // Call the same logic as loginButtonOnAction
        closeButtonOnAction();
    }

}




