package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.CartModel;
import com.codefornature.model.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException, SQLException, ParseException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        loginController.setStartingStage(stage);
//        MainController mainController = fxmlLoader.getController();
//        mainController.setUser(user);
//        scene.getStylesheets().add(getClass().getResource("root.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}