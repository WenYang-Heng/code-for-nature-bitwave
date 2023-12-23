package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.CartItemsModel;
import com.codefornature.model.CartModel;
import com.codefornature.model.MerchandiseModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartController {
    @FXML
    private VBox shoppingCartContainer;
    private Label grandTotal;
    private String rootPath;
    private CartDAO cartDAO = new CartDAO();
    private int totalItemCost;
    private int grandTotalCost = 0;
    private Map<Integer, Label> totalCostLabels = new HashMap<>();

    @FXML
    public void initialize() throws SQLException {
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        VBox carItemsContainer = new VBox();
        carItemsContainer.setPrefHeight(500);
        carItemsContainer.setSpacing(20);
        HBox cartHeader = new HBox();
        Label merchHeader = new Label("Mer`chandise");
        Label quantityHeader =  new Label("Quantity");
        Label costHeader = new Label("Cost");
        Label totalHeader = new Label("Total");
        Region region1 = new Region();
        Region region2 = new Region();
        Region region3 = new Region();
        region1.setPrefWidth(380); //space between merchandise and quantity
        region2.setPrefWidth(120); //space between quantity and cost
        region3.setPrefWidth(70); //space between cost and total
        cartHeader.getChildren().addAll(merchHeader, region1, quantityHeader, region2, costHeader, region3, totalHeader);
        cartHeader.setPadding(new Insets(20, 20, 5, 20));
        cartHeader.getStyleClass().add("cart-header");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        if(CartModel.getCart_id() == 0){
            System.out.println("no cart");
            // Display a message in the UI indicating that the cart is empty
            Label emptyCartLabel = new Label("Your cart is empty.");
            emptyCartLabel.getStyleClass().add("empty-cart-message"); // Add CSS class for styling
            carItemsContainer.getChildren().add(emptyCartLabel);
        }
        else{
            List<CartItemsModel> cartItems = cartDAO.getCartItems(CartModel.getCart_id());
            for(int i = 0; i < cartItems.size(); i++){
                int cost = cartItems.get(i).getCost();
                int quantity = cartItems.get(i).getQuantity();

                HBox merchContent = new HBox();
                merchContent.setSpacing(20);
                merchContent.setStyle("-fx-background-color: #1C2835; -fx-background-radius: 20;");
                merchContent.setPadding(new Insets(20));
                ImageView merchImage = new ImageView(rootPath + "images/" + cartItems.get(i).getImage_name());
                merchImage.setFitHeight(170);
                merchImage.setFitWidth(170);
                VBox merchDetails = new VBox();
                Label merchName = new Label(cartItems.get(i).getMerchandise_name());
                merchDetails.setPrefWidth(300);
                merchDetails.setMaxWidth(300);
                Button removeItemBtn = new Button("Remove button");
                int finalIndex = i;
                removeItemBtn.setOnAction(event -> {
                    carItemsContainer.getChildren().remove(merchContent);
                    try {
                        cartDAO.removeCartItems(CartModel.getCart_id(), cartItems.get(finalIndex).getMerchandise_id());
                        // Check if the cart is empty after removing the item
                        if (carItemsContainer.getChildren().isEmpty()) {
                            cartDAO.removeCart(CartModel.getCart_id());
                            System.out.println("Cart is now empty and has been deleted from the database.");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                merchDetails.getChildren().addAll(merchName, removeItemBtn);
                merchName.setStyle("fx-background-color: green");
                merchName.setWrapText(true);

                HBox quantityContainer = createCounterBox(quantity, cartItems.get(i).getCost(), cartItems.get(i).getMerchandise_id());
                Label merchCost = new Label(Integer.toString(cost));
                totalItemCost = cost * quantity;
                grandTotalCost += totalItemCost;
                Label totalCost = new Label(Integer.toString(totalItemCost));
                totalCostLabels.put(cartItems.get(i).getMerchandise_id(), totalCost);
                merchCost.setMinWidth(100);
                merchCost.setAlignment(Pos.CENTER);
                totalCost.setMinWidth(100);
                totalCost.setAlignment(Pos.CENTER);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                merchContent.getChildren().addAll(merchImage, merchDetails, quantityContainer, spacer, merchCost, totalCost);
                carItemsContainer.getChildren().add(merchContent);
            }
        }
        scrollPane.setContent(carItemsContainer);

        HBox grandTotalContainer = new HBox();
        Label grandTotalLabel = new Label("Grand Total:");
        grandTotal = new Label(Integer.toString(grandTotalCost));
        grandTotal.setMinWidth(100);
        grandTotal.setStyle("-fx-font-weight: bold");
        grandTotalContainer.setSpacing(20);
        grandTotalContainer.setPadding(new Insets(20, 0, 0, 0));
        grandTotalContainer.setAlignment(Pos.CENTER_RIGHT);
        grandTotalContainer.getChildren().addAll(grandTotalLabel, grandTotal);

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.getStyleClass().add("checkout-btn");
        HBox checkoutBtnContainer = new HBox(checkoutBtn);
        checkoutBtnContainer.setAlignment(Pos.CENTER_RIGHT);
        checkoutBtnContainer.setPadding(new Insets(20, 20, 0 , 0));

        shoppingCartContainer.getChildren().addAll(cartHeader, scrollPane, grandTotalContainer, checkoutBtnContainer);
        shoppingCartContainer.getStylesheets().add(getClass().getResource("/styles/shopping-cart.css").toExternalForm());
    }

    public int getGrandTotalCost() {
        return grandTotalCost;
    }

    public void setGrandTotalCost(int grandTotalCost) {
        this.grandTotalCost = grandTotalCost;
    }

    private HBox createCounterBox(int quantity, int cost, int merch_id) {
        String iconUrl = "@../../assets/icons/";
        Button minusButton = createImageButton(iconUrl + "minus.png");
        Label counterLabel = new Label(Integer.toString(quantity));
        counterLabel.setStyle("-fx-text-fill: #ffffff");
        Button plusButton = createImageButton(iconUrl + "plus.png");

        minusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            if(count > 1){
                counterLabel.setText(Integer.toString(--count));
                int total = cost * count;
                totalCostLabels.get(merch_id).setText(Integer.toString(total));
                setGrandTotalCost(grandTotalCost - cost);
                grandTotal.setText(Integer.toString(getGrandTotalCost()));
            }
        });

        plusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            if(count < 5){
                counterLabel.setText(Integer.toString(++count));
                int total = cost * count;
                totalCostLabels.get(merch_id).setText(Integer.toString(total));
                setGrandTotalCost(grandTotalCost + cost);
                grandTotal.setText(Integer.toString(getGrandTotalCost()));
            }
        });

        HBox counterBox = new HBox(minusButton, counterLabel, plusButton);
        counterBox.setAlignment(Pos.TOP_CENTER);
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
}
