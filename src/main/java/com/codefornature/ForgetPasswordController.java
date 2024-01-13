package com.codefornature;

import com.codefornature.dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ForgetPasswordController {
    @FXML
    private Pane emailPane;
    @FXML
    private Pane passwordPane;
    @FXML
    private Pane confirmPane;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Label errorMessageLabel;
    private String email;
    private String password;

    public void initialize() {
        addFocusListener(emailTextField, emailPane);
        addFocusListener(setPasswordField, passwordPane);
        addFocusListener(confirmPasswordField, confirmPane);
    }

    public void onLoginClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        //get the source of this event and cast it to a node, and then cast it to stage
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        LoginController loginController = loader.getController();
        loginController.setStartingStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void resetPasswordOnAction(ActionEvent actionEvent) throws SQLException {
        if (emailTextField.getText().isEmpty() || setPasswordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            confirmPasswordLabel.setText("Kindly complete all the blanks.");
        } else if (!setPasswordField.getText().equals(confirmPasswordField.getText())){
            confirmPasswordLabel.setText("Ensure password entered is correct.");
        }else{
            System.out.println("Fields are valid. Continue to register");
            email = emailTextField.getText();
            password = setPasswordField.getText();
            resetPassword();
        }
    }

    private void resetPassword() throws SQLException {
        UserDAO userDAO = new UserDAO();
        if(userDAO.emailExists(email)){
            if(!userDAO.isPasswordIdentical(email, password)){
                if(userDAO.updatePassword(email, password)){
                    AlertController.showAlert("Password Change", "Password changed successfully, please proceed to login.", 1);
                    if(userDAO.isLocked(email)){
                        userDAO.updateIsLocked(email);
                    }
                }
                else{
                    AlertController.showAlert("Password Change", "Password change failed, please try again.", 0);
                }
            }
            else{
                errorMessageLabel.setText("Password is identical with the previous entry, please enter a different password!");
            }
        }
        else{
            errorMessageLabel.setText("Email does not exist. Please try again!");
        }
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
}
