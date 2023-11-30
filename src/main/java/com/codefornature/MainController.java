package com.codefornature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {
    @FXML private BorderPane mainContainer;

    @FXML
    public void initialize() throws IOException {
        System.out.println("root border pane loaded");
    }

    public void loadPage(String page) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(page));
        mainContainer.setCenter(root);
    }

    public void toPointShop(ActionEvent actionEvent) throws IOException {
        System.out.println("point shop button clicked");
        loadPage("point-shop.fxml");
    }
}
