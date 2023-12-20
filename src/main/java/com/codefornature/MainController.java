package com.codefornature;

import com.codefornature.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;

public class MainController {
    private UserModel user;
    @FXML private BorderPane mainContainer;

    @FXML
    public void initialize() throws IOException {
        System.out.println("root border pane loaded");
    }

    public void loadPage(String page) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
        Parent root = loader.load();
        switch(page){
            case "home-view.fxml":
                HomeController homeController = loader.getController();
                homeController.setUser(user);
                break;
            case "point-shop.fxml":
                PointShopController pointShopController = loader.getController();
                break;
        }
        mainContainer.setCenter(root);
    }

    public void toPointShop(ActionEvent actionEvent) throws IOException {
        System.out.println("point shop button clicked");
        loadPage("point-shop.fxml");
    }

    public void toHome(ActionEvent actionEvent) throws IOException {
        loadPage("home-view.fxml");
    }

    public void setUser(UserModel user) throws IOException {
        this.user = user;
        loadPage("home-view.fxml");
    }
}
