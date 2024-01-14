package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.CartModel;
import com.codefornature.model.UserModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {

    @FXML
    private HBox titleBar;
    @FXML
    private Label errorMessageLabel;

    @FXML
    private TextField userID;

    @FXML
    private PasswordField password;
    @FXML
    private HBox IDHbox,IDHbox2;
    @FXML
    private CheckBox login_selectshowPassword;

    @FXML
    private TextField login_showPassword;
    private Stage homeStage;
    private Stage startStage;
    private UserDAO userDAO;
    private UserModel user;
    private int loginAttempts = 3;

    public void initialize() {
        userID.setFocusTraversable(false);
        password.setFocusTraversable(false);
        userID.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(userID.isFocused()){
                    IDHbox.setStyle("-fx-border-color:#73d960;");
                }else{
                    IDHbox.setStyle("-fx-border-color:#b5b5b5;");
                }
            }
        });

        password.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(password.isFocused()){
                    IDHbox2.setStyle("-fx-border-color:#73d960;");
                }else{
                    IDHbox2.setStyle("-fx-border-color:#b5b5b5;");
                }
            }
        });
    }

    @FXML
    private void login(ActionEvent event) throws IOException, SQLException {
        String ID = userID.getText();
        String pw = password.getText();

        if(ID.isEmpty() || pw.isEmpty()){
            if(ID.isEmpty()){
                errorMessageLabel.setText("Kindly enter your username or email.");
            }else if(pw.isEmpty()){
                errorMessageLabel.setText("Kindly enter your password.");
            }else if(ID.isEmpty()&&pw.isEmpty()){
                errorMessageLabel.setText("Kindly enter your username/email and password");
            }
            return;
        }

        userDAO = new UserDAO();

        if(userDAO.isLocked(ID)){
            errorMessageLabel.setText("Due to multiple failed login attempts, please reset your password!");
            return;
        }

        if(userDAO.emailExists(ID)){
            user = userDAO.getUser(ID, pw);
            if(user == null) {
                errorMessageLabel.setText("Your password is incorrect. Please try again!");
                loginAttempts--;
                if(loginAttempts == 0){
                    System.out.println("Locking account");
                    userDAO.updateIsLocked(ID);
                }
                return;
            }
        }
        else{
            errorMessageLabel.setText("This email does not exist. Please try again!");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        if(cartDAO.cartExists(user.getUser_id())){
            CartModel cart = cartDAO.getCart(user.getUser_id());
        }
        System.out.println(user);

        homeStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-container.fxml"));
        Scene scene = new Scene(loader.load());
        scene.setFill(Color.TRANSPARENT);
        MainController mainController = loader.getController();
        mainController.setStage(homeStage);
        mainController.setUser(user);
        homeStage.setScene(scene);
        startStage.close();
        homeStage.show();
    }

    public void showPassword(){

        if(login_selectshowPassword.isSelected()){
            login_showPassword.setText(password.getText());
            login_showPassword.setVisible(true);
            password.setVisible(false);

        }else{
            password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            password.setVisible(true);

        }
    }

    public void setStartingStage(Stage stage, Scene scene) {
        startStage = stage;
        scene.setFill(Color.TRANSPARENT);
        WindowDragController.windowDrag(titleBar, startStage);
    }

    public void onSignUpClicked(ActionEvent event) throws IOException {
        loadPage("register-view.fxml");
    }

    public void onForgetPasswordClicked(MouseEvent mouseEvent) throws IOException {
        loadPage("forget-password-view.fxml");
    }

    public void loadPage(String page) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        if(page.equals("register-view.fxml")){
            RegisterController registerController = loader.getController();
            registerController.setStage(startStage, scene);
        }
        else{
            ForgetPasswordController forgetPasswordController = loader.getController();
            forgetPasswordController.setStage(startStage, scene);
        }
        startStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        startStage.show();
    }

    public void minimizeWindow(ActionEvent actionEvent) {
        startStage.setIconified(true);
    }

    public void closeWindow(ActionEvent actionEvent) {
        startStage.close();
    }
}
