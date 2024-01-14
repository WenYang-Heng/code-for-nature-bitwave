package com.codefornature;

import com.codefornature.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class MainController {
    @FXML
    private Button homeButton;
    private UserModel user;
    @FXML private BorderPane mainContainer;
    @FXML private HBox titleBar;
    private Button currentSelectedButton;
    private Stage mainWindow;

    @FXML
    public void initialize() throws IOException {
        mainContainer.getStylesheets().add(getClass().getResource("/styles/root.css").toExternalForm());
        selectedNavButton(homeButton);
    }

    public void loadPage(String page) throws IOException, SQLException{
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
            case "shopping-cart-view.fxml":
                ShoppingCartController shoppingCartController = loader.getController();
                shoppingCartController.setUser(user);
                shoppingCartController.setMainContainer(mainContainer);
                break;
            case "donation-view.fxml":
                DonationController donationController = loader.getController();
                donationController.setUser(user);
                break;
            case "quiz-list-view.fxml":
                QuizListController quizListController = loader.getController();
                quizListController.setUser(user);
        }
        mainContainer.setCenter(root);
    }

    public BorderPane getMainContainer(){
        return mainContainer;
    }

    public void toPointShop(ActionEvent actionEvent) throws IOException, SQLException {
        selectedNavButton((Button) actionEvent.getSource());
        loadPage("point-shop.fxml");
    }

    public void toHome(ActionEvent actionEvent) throws IOException, SQLException {
        selectedNavButton((Button) actionEvent.getSource());
        loadPage("home-view.fxml");
    }

    public void toDonation(ActionEvent actionEvent) throws IOException, SQLException {
        selectedNavButton((Button) actionEvent.getSource());
        loadPage("donation-view.fxml");
    }

    public void setUser(UserModel user) throws IOException, SQLException {
        this.user = user;
        loadPage("home-view.fxml");
    }

    public void toShoppingCart(ActionEvent actionEvent) throws IOException, SQLException {
        selectedNavButton((Button) actionEvent.getSource());
        loadPage("shopping-cart-view.fxml");
    }

    public void toQuiz(ActionEvent actionEvent) throws SQLException, IOException {
        selectedNavButton((Button) actionEvent.getSource());
        loadPage("quiz-list-view.fxml");
    }

    public void toLogout(ActionEvent actionEvent) throws IOException {
        Stage currentWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Stage loginWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        Scene scene = new Scene(root);
        loginController.setStartingStage(loginWindow, scene);
        scene.setFill(Color.TRANSPARENT);
        loginWindow.initStyle(StageStyle.TRANSPARENT);
        loginWindow.setResizable(false);
        currentWindow.close(); //close current window
        loginWindow.setScene(scene);
        loginWindow.show(); //open new window for login
    }

    public void selectedNavButton(Button selectedButton){
        if (currentSelectedButton != null) {
            currentSelectedButton.getStyleClass().remove("selected-button");
        }
        currentSelectedButton = selectedButton;
        currentSelectedButton.getStyleClass().add("selected-button");
    }

    public void minimizeWindow(ActionEvent actionEvent) {
        mainWindow.setIconified(true);
    }

    public void closeWindow(ActionEvent actionEvent) {
        mainWindow.close();
    }

    public void setStage(Stage mainWindow) {
        this.mainWindow = mainWindow;
        mainWindow.initStyle(StageStyle.TRANSPARENT);
        mainWindow.setResizable(false);
        WindowDragController.windowDrag(titleBar, mainWindow);
    }
}
