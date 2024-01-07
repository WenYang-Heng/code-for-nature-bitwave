package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.CartModel;
import com.codefornature.model.UserModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Button loginFgt;

    @FXML
    private Button loginSignUp;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private TextField userID;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView userImageView;

    @FXML
    private ImageView passwordImageView;

    @FXML
    private ImageView forestImageView;

    @FXML
    private ImageView pineImageView;

    @FXML
    private HBox IDHbox,IDHbox2;
    private Stage homeStage;
    private Stage startStage;
    private UserDAO userDAO;
    private UserModel user;

    @FXML
    private void login(ActionEvent event) throws IOException, SQLException {
        userID.setText("heng3@gmail.com");
        password.setText("test123");
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
        user = userDAO.getUser(ID, pw);
        if(user == null){
            errorMessageLabel.setText("Your username or password is incorrect. Please try again!");
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
        MainController mainController = loader.getController();
        mainController.setUser(user);
        scene.getStylesheets().add(getClass().getResource("root.css").toExternalForm());
        homeStage.setScene(scene);
        startStage.close();
        homeStage.show();
    }



    // You may have other methods and initialization code here

    // For example, a method to initialize the images in the controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load images and set them to respective ImageViews
        String absPath = new File("").getAbsolutePath();
        Image userImage,passwordImage,forestImage,pineImage;
        passwordImage = forestImage = pineImage = userImage = null;
        try {
            userImage = new Image(new FileInputStream(absPath+"\\src\\images\\userbefore.png"));
            userImageView.setImage(userImage);

            passwordImage = new Image(new FileInputStream(absPath+"\\src\\images\\password.png"));
            passwordImageView.setImage(passwordImage);

            forestImage = new Image(new FileInputStream(absPath+"\\src\\images\\forestnew.png"));
            forestImageView.setImage(forestImage);

            pineImage = new Image(new FileInputStream(absPath+"\\src\\images\\pinenew.png"));
            pineImageView.setImage(pineImage);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
    private CheckBox login_selectshowPassword;

    @FXML
    private TextField login_showPassword;

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

    public void setStartingStage(Stage stage) {
        startStage = stage;
    }

    public void onSignUpClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
        Parent root = loader.load();
        //get the source of this event and cast it to a node, and then cast it to stage
        startStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        RegisterController registerController = loader.getController();
        registerController.setStage(startStage);
        Scene scene = new Scene(root);
        startStage.setScene(scene);
        startStage.show();
    }





    /*@FXML
    ImageView login1;
    Image myImage=new Image(getClass().getResourceAsStream("forest.png"));

    public void displayImage(){
        login1.setImage(myImage);

    } */
}
