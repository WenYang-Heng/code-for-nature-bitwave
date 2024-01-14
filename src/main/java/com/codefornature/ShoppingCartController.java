package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.CartItemsModel;
import com.codefornature.model.CartModel;
import com.codefornature.model.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartController {

    @FXML
    private BorderPane mainContainer;
    @FXML
    private VBox shoppingCartContainer;
    private Label emptyCartLabel = new Label();
    private Label grandTotal;
    private HBox grandTotalContainer;
    private Button checkoutBtn = new Button();
    private String rootPath;
    private CartDAO cartDAO = new CartDAO();
    private List<CartItemsModel> cartItems;
    private UserModel user;
    private int totalItemCost;
    private int grandTotalCost = 0;
    private Map<Integer, Label> totalCostLabels = new HashMap<>();


    @FXML
    public void initialize() throws SQLException {
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        VBox carItemsContainer = new VBox();
        carItemsContainer.setPrefHeight(500);
        carItemsContainer.setSpacing(20);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        emptyCartLabel.setText("Your cart is empty.");
        emptyCartLabel.getStyleClass().add("empty-cart-message");

        if(CartModel.getCart_id() == 0){
            System.out.println("no cart");
            //Display a message in the UI indicating that the cart is empty
            checkoutBtn.setVisible(false);
            checkoutBtn.setManaged(false);
            carItemsContainer.getChildren().add(emptyCartLabel);
        }
        else{
            cartItems = cartDAO.getCartItems(CartModel.getCart_id());
            for(CartItemsModel item : cartItems){
                Label merchHeader = new Label("Merchandise");
                Label quantityHeader =  new Label("Quantity");
                Label costHeader = new Label("Cost");
                Label totalHeader = new Label("Total");

                merchHeader.getStyleClass().add("cart-header");
                quantityHeader.getStyleClass().add("cart-header");
                costHeader.getStyleClass().add("cart-header");
                totalHeader.getStyleClass().add("cart-header");
                int cost = item.getCost();
                int quantity = item.getQuantity();

                HBox merchContent = new HBox();
                merchContent.setSpacing(20);
                merchContent.setStyle("-fx-background-color: #1C2835; -fx-background-radius: 20;");
                merchContent.setPadding(new Insets(20));
                ImageView merchImage = new ImageView(rootPath + "images/" + item.getImage_name());
                merchImage.setFitHeight(170);
                merchImage.setFitWidth(170);
                VBox merchDetails = new VBox();
                Region region = new Region();
                VBox.setVgrow(region, Priority.ALWAYS);

                Label merchName = new Label(item.getMerchandise_name());
                VBox merchNameContainer = new VBox(merchHeader, merchName);

                merchDetails.setPrefWidth(300);
                merchDetails.setMaxWidth(300);
                Button removeItemBtn = new Button("Remove Item");
                removeItemBtn.getStyleClass().add("remove-item");
                removeItemBtn.setOnAction(event -> {
                    carItemsContainer.getChildren().remove(merchContent);
                    try {
                        grandTotalCost -= item.getCost();
                        grandTotal.setText(Integer.toString(grandTotalCost));
                        cartDAO.removeCartItem(CartModel.getCart_id(), item.getMerchandise_id());
                        // Check if the cart is empty after removing the item
                        if (carItemsContainer.getChildren().isEmpty()) {
                            grandTotalContainer.setVisible(false);
                            checkoutBtn.setVisible(false);
                            checkoutBtn.setManaged(false);
                            carItemsContainer.getChildren().add(emptyCartLabel);
                            cartDAO.removeCart(CartModel.getCart_id());
                            CartModel.setCart_id(0);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                merchDetails.getChildren().addAll(merchNameContainer, region, removeItemBtn);
                merchName.setWrapText(true);

                HBox quantityContainer = createCounterBox(quantity, item.getCost(), item.getMerchandise_id());
                VBox quantityBox = new VBox(quantityHeader, quantityContainer);

                Label merchCost = new Label(Integer.toString(cost));
                VBox merchCostContainer = new VBox(costHeader, merchCost);
                merchCostContainer.setMinWidth(100);
                merchCostContainer.setAlignment(Pos.TOP_CENTER);

                totalItemCost = cost * quantity;
                grandTotalCost += totalItemCost;

                Label totalCost = new Label(Integer.toString(totalItemCost));
                VBox totalCostContainer = new VBox(totalHeader, totalCost);
                totalCostContainer.setMinWidth(100);
                totalCostContainer.setAlignment(Pos.TOP_CENTER);

                totalCostLabels.put(item.getMerchandise_id(), totalCost);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                merchContent.getChildren().addAll(merchImage, merchDetails, quantityBox, spacer, merchCostContainer, totalCostContainer);
                carItemsContainer.getChildren().add(merchContent);
            }
        }
        scrollPane.setContent(carItemsContainer);

        grandTotalContainer = new HBox();
        Label grandTotalLabel = new Label("Grand Total:");
        grandTotalLabel.getStyleClass().add("grand-total");
        grandTotal = new Label(Integer.toString(grandTotalCost));
        grandTotal.getStyleClass().add("grand-total");
        grandTotal.setMinWidth(100);
        grandTotalContainer.setSpacing(20);
        grandTotalContainer.setPadding(new Insets(20, 0, 0, 0));
        grandTotalContainer.setAlignment(Pos.CENTER_RIGHT);
        grandTotalContainer.getChildren().addAll(grandTotalLabel, grandTotal);
        if(grandTotalCost == 0){
            grandTotalContainer.setVisible(false);
        }

        checkoutBtn.setText("Checkout");
        checkoutBtn.setOnAction(event -> {
            try {
                onCheckoutClicked();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        checkoutBtn.getStyleClass().add("checkout-btn");
        HBox checkoutBtnContainer = new HBox(checkoutBtn);
        checkoutBtnContainer.setAlignment(Pos.CENTER_RIGHT);
        checkoutBtnContainer.setPadding(new Insets(20, 20, 0 , 0));

        shoppingCartContainer.getChildren().addAll(scrollPane, grandTotalContainer, checkoutBtnContainer);
        shoppingCartContainer.getStylesheets().add(getClass().getResource("/styles/shopping-cart.css").toExternalForm());
    }

    public void onCheckoutClicked() throws IOException {
        //Check if user has enough points to purchase the item
        if(user.getPoints() >= grandTotalCost){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order-view.fxml"));
            Parent root = loader.load();
            OrderController orderController = loader.getController();
            orderController.setCartItems(cartItems);
            orderController.setUser(user);
            orderController.setGrandTotal(grandTotalCost);
            orderController.setMainContainer(mainContainer);
            mainContainer.setCenter(root);
        }
        else{
            //User does not have enough points
            AlertController.showAlert("Insufficient Points", "Not enough points to purchase these products", 0);
        }

    }

    private HBox createCounterBox(int quantity, int cost, int merch_id) {
        String iconUrl = "@../../assets/icons/";
        Button minusButton = createImageButton(iconUrl + "minus.png");
        Label counterLabel = new Label(Integer.toString(quantity));
        Button plusButton = createImageButton(iconUrl + "plus.png");

        minusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            if(count > 1){
                counterLabel.setText(Integer.toString(--count));
                int total = cost * count;
                totalCostLabels.get(merch_id).setText(Integer.toString(total));
                grandTotalCost -= cost;
                grandTotal.setText(Integer.toString(grandTotalCost));
            }
        });

        plusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            if(count < 5){
                counterLabel.setText(Integer.toString(++count));
                int total = cost * count;
                totalCostLabels.get(merch_id).setText(Integer.toString(total));
                grandTotalCost += cost;
                grandTotal.setText(Integer.toString(grandTotalCost));
            }
        });

        HBox counterBox = new HBox(minusButton, counterLabel, plusButton);
        counterBox.setAlignment(Pos.CENTER);
        return counterBox;
    }

    private Button createImageButton(String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(12);
        imageView.setFitHeight(12);
        ColorAdjust white = new ColorAdjust();
        white.setBrightness(1.0);
        imageView.setEffect(white);
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent");
        button.setGraphic(imageView);

        return button;
    }

    public void setMainContainer(BorderPane mainContainer){
        this.mainContainer = mainContainer;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
