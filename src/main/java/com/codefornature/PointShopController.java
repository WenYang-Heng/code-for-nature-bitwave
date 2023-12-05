package com.codefornature;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;

public class PointShopController {
    @FXML
    FlowPane merchContent;

    @FXML
    public void initialize() throws IOException {
        System.out.println("parent flow pane loaded");
        VBox vbox = new VBox();
        vbox.setPrefSize(252, 268);
        String imageUrl = "@../../assets/images/";
        ImageView merchImage = new ImageView(new Image(imageUrl + "shirt.jpg"));
        merchImage.setFitWidth(252);
        merchImage.setFitHeight(200);

        Label itemName = new Label("Item Name");
        Label itemPrice = new Label("$50");
        itemName.setStyle("-fx-text-fill: #E4EAF1");
        itemPrice.setStyle("-fx-text-fill: #E4EAF1");
        itemPrice.setFont(new Font(20));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox labelBox = new HBox(
                itemName,
                region,
                itemPrice
        );
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setPadding(new Insets(0, 5, 0 ,5));

        HBox buttonBox = new HBox(
                createStyledButton("Buy Now"),
                createStyledButton("Add to Cart"),
                createCounterBox()
        );
        buttonBox.setPadding(new Insets(0, 5, 0 ,5));
        buttonBox.setSpacing(10);
        vbox.getChildren().addAll(merchImage, labelBox, buttonBox);
        vbox.setSpacing(10);
        merchContent.getChildren().add(vbox);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font(11));
        button.setStyle(
                "-fx-background-radius: 20;" +
                (text.equals("Buy Now")? "-fx-background-color: #359424;-fx-text-fill: #ffffff;" : "-fx-background-color: #A9E79C; -fx-text-fill: #1E4B15")
        );
        button.setPrefSize(200, 27);
        return button;
    }

    private HBox createCounterBox() {
        String iconUrl = "@../../assets/icons/";
        Button minusButton = createImageButton(iconUrl + "minus.png");
        Label counterLabel = new Label("0");
        counterLabel.setStyle("-fx-text-fill: #ffffff");
        Button plusButton = createImageButton(iconUrl + "plus.png");

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
}
