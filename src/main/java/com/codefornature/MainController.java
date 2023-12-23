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
import java.sql.SQLException;
import java.text.ParseException;

public class MainController {
    private UserModel user;
    @FXML private BorderPane mainContainer;
    @FXML private VBox sidebar;

    @FXML
    public void initialize() throws IOException {
        System.out.println("root border pane loaded");
        sidebar.getStylesheets().add(getClass().getResource("/styles/sidebar.css").toExternalForm());
    }

    public void loadPage(String page) throws IOException, SQLException, ParseException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
        Parent root = loader.load();
        switch(page){
            case "home-view.fxml":
                HomeController homeController = loader.getController();
                homeController.setUser(user);
                break;
            case "point-shop.fxml":
                PointShopController pointShopController = loader.getController();
                pointShopController.setUser(user);
                pointShopController.setBorderPane(mainContainer);
                break;
        }
        mainContainer.setCenter(root);
    }

    public BorderPane getMainContainer(){
        return mainContainer;
    }

    public void toPointShop(ActionEvent actionEvent) throws IOException, SQLException, ParseException {
        System.out.println("point shop button clicked");
        loadPage("point-shop.fxml");
    }

    public void toHome(ActionEvent actionEvent) throws IOException, SQLException, ParseException {
        loadPage("home-view.fxml");
    }

    public void setUser(UserModel user) throws IOException, SQLException, ParseException {
        this.user = user;
        loadPage("home-view.fxml");
    }

    public void toShoppingCart(ActionEvent actionEvent) throws IOException, SQLException, ParseException {
        loadPage("shopping-cart-view.fxml");
    }
}
