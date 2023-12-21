package com.codefornature;

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

public class ShoppingCartController {
    @FXML
    private VBox shoppingCartContainer;
    private String rootPath;

    @FXML
    public void initialize(){
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        VBox carItemsContainer = new VBox();
        carItemsContainer.setPrefHeight(500);
        carItemsContainer.setSpacing(20);
        HBox cartHeader = new HBox();
        Label merchHeader = new Label("Merchandise");
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
        for(int i = 0; i < 0; i++){
            HBox merchContent = new HBox();
            merchContent.setSpacing(20);
            merchContent.setStyle("-fx-background-color: #1C2835; -fx-background-radius: 20;");
            merchContent.setPadding(new Insets(20));
            ImageView merchImage = new ImageView(rootPath + "images/eco-tote-bag.jpg");
            merchImage.setFitHeight(170);
            merchImage.setFitWidth(170);
            Label merchName = new Label("Save the earth T-shirt");
            merchName.setMinWidth(300);
            merchName.setStyle("fx-background-color: green");
            merchName.setWrapText(true);

            HBox quantityContainer = createCounterBox();
            Label merchCost = new Label("1000");
            Label totalCost = new Label("1000");
            merchCost.setMinWidth(100);
            merchCost.setAlignment(Pos.CENTER);
            totalCost.setMinWidth(100);
            totalCost.setAlignment(Pos.CENTER);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            merchContent.getChildren().addAll(merchImage, merchName, quantityContainer, spacer, merchCost, totalCost);
            carItemsContainer.getChildren().add(merchContent);
        }
        scrollPane.setContent(carItemsContainer);

        HBox grandTotalContainer = new HBox();
        Label grandTotalLabel = new Label("Grand Total:");
        Label grandTotal = new Label("200");
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

    private HBox createCounterBox() {
        String iconUrl = "@../../assets/icons/";
        Button minusButton = createImageButton(iconUrl + "minus.png");
        Label counterLabel = new Label("0");
        counterLabel.setStyle("-fx-text-fill: #ffffff");
        Button plusButton = createImageButton(iconUrl + "plus.png");

        minusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            if(count > 0){
                counterLabel.setText(Integer.toString(--count));
            }
        });

        plusButton.setOnAction(event -> {
            int count = Integer.parseInt(counterLabel.getText());
            counterLabel.setText(Integer.toString(++count));
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
