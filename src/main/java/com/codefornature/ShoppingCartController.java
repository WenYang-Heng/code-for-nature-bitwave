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
import javafx.scene.layout.VBox;

public class ShoppingCartController {
    @FXML
    private VBox shoppingCartContainer;
    private String rootPath;

    @FXML
    public void initialize(){
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add(getClass().getResource("/styles/shopping-cart.css").toExternalForm());
        HBox merchContent = new HBox();
        merchContent.setStyle("-fx-background-color: #1C2835; -fx-background-radius: 20;");
        merchContent.setPadding(new Insets(20));
        ImageView merchImage = new ImageView(rootPath + "images/eco-tote-bag.jpg");
        merchImage.setFitHeight(200);
        merchImage.setFitWidth(200);
        Label merchName = new Label("Save the earth T-shirt");

        HBox quantityContainer = createCounterBox();
        Label merchCost = new Label("100");
        Label totalCost = new Label("100");

        merchContent.getChildren().addAll(merchImage, merchName, quantityContainer, merchCost, totalCost);

        scrollPane.setContent(merchContent);
        shoppingCartContainer.getChildren().add(scrollPane);
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
