package com.codefornature;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Label myLabel;

    @FXML
    private TextField userID;

    @FXML
    private PasswordField password;

    @FXML
    private HBox IDHbox;

    @FXML
    private void login(ActionEvent event){
        String ID = userID.getText();

        String pw = password.getText();
        System.out.println(ID);
        System.out.println(pw);
        if(ID.isEmpty()&&!pw.isEmpty()){
            myLabel.setText("Kindly enter your username or email.");
        }else if(!ID.isEmpty()&&pw.isEmpty()){
            myLabel.setText("Kindly enter your password.");
        }else if(ID.isEmpty()&&pw.isEmpty()){
            myLabel.setText("Kindly enter your username/email and password");
        }
    }

    private void focus(ActionEvent event){
        if(userID.isFocused())
            IDHbox.setStyle("-fx-border-color:#73d960;");
    }
    @FXML
    private ImageView userImageView;

    @FXML
    private ImageView passwordImageView;

    @FXML
    private ImageView forestImageView;

    @FXML
    private ImageView pineImageView;

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

            forestImage = new Image(new FileInputStream(absPath+"\\src\\images\\forest.png"));
            forestImageView.setImage(forestImage);

            pineImage = new Image(new FileInputStream(absPath+"\\src\\images\\pine.png"));
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





    /*@FXML
    ImageView login1;
    Image myImage=new Image(getClass().getResourceAsStream("forest.png"));

    public void displayImage(){
        login1.setImage(myImage);

    } */
}
