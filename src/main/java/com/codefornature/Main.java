package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.CartModel;
import com.codefornature.model.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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

        UserDAO userDAO = new UserDAO();
        UserModel user = userDAO.getUser("abc123@gmail.com", "test123");
        CartDAO cartDAO = new CartDAO();
        if(cartDAO.cartExists(user.getUser_id())){
            CartModel cart = cartDAO.getCart(user.getUser_id());
        }
        System.out.println(user);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-container.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MainController mainController = fxmlLoader.getController();
        mainController.setUser(user);
//        scene.getStylesheets().add(getClass().getResource("root.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}