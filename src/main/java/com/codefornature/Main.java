package com.codefornature;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException, SQLException, ParseException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        scene.getStylesheets().add(getClass().getResource("/styles/root.css").toExternalForm());
        LoginController loginController = fxmlLoader.getController();
        loginController.setStartingStage(stage, scene);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}