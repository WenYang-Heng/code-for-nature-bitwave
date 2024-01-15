package com.codefornature;

import com.codefornature.dao.UserDAO;
import com.codefornature.utils.WindowDrag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
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
    private Pane emailPane;
    @FXML
    private Pane usernamePane;
    @FXML
    private Pane passwordPane;
    @FXML
    private Pane confirmPane;
    @FXML
    private HBox titleBar;
    private Stage startStage;

    @FXML
    public void initialize() {

        addFocusListener(emailTextField, emailPane);
        addFocusListener(usernameTextField, usernamePane);
        addFocusListener(setPasswordField, passwordPane);
        addFocusListener(confirmPasswordField, confirmPane);
    }

    private void register() {
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = confirmPasswordField.getText();
        UserDAO userDAO = new UserDAO();

        try{
            if(userDAO.emailExists(email)){
                registerMessageLabel.setText("Email already exists. Please use another email!");
            }
            else{
                boolean isRegistered = userDAO.insertUser(email, password, username);
                int alertType = 0;
                String registerMessage = "Registration failed. PLease try again";
                if(isRegistered){
                    alertType = 1;
                    registerMessage = "Registration succesful. Please login";
                }
                AlertController.showAlert("REGISTER", registerMessage, alertType);
            }
        }catch (SQLException e){
            e.printStackTrace();
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

    public void registerButtonOnAction(ActionEvent event) {
        if (emailTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || setPasswordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            confirmPasswordLabel.setText("Kindly complete all the blanks.");
        } else if (!setPasswordField.getText().equals(confirmPasswordField.getText())){
            confirmPasswordLabel.setText("Ensure password entered is correct.");
        }else{
            System.out.println("Fields are valid. Continue to register");
            register();
        }
    }

    @FXML
    private void onLoginClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        Scene scene = new Scene(root);
        loginController.setStartingStage(startStage, scene);
        startStage.setScene(scene);
        startStage.show();
    }

    public void setStage(Stage stage, Scene scene) {
        this.startStage = stage;
        scene.setFill(Color.TRANSPARENT);
        WindowDrag.windowDrag(titleBar, startStage);
    }

    public void minimizeWindow(ActionEvent actionEvent) {
        startStage.setIconified(true);
    }

    public void closeWindow(ActionEvent actionEvent) {
        startStage.close();
    }
}




