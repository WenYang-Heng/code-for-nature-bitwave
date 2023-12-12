package com.codefornature;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;

public class HomeController {
    @FXML
    VBox homeContainer;

    public void initialize() throws IOException{
        createNewsUI();
    }

    public void createNewsUI(){
        int numberOfColumnsToShow = 3;
        String rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        //create the containers
        GridPane newsContainer = new GridPane();
        newsContainer.setStyle("-fx-border-color: green");
        newsContainer.setAlignment(Pos.CENTER);
        newsContainer.setHgap(30);
        newsContainer.getStyleClass().add("label");

        for(int i = 0; i < 3; i++){
            VBox newsChild = createNewsVbox(rootPath);
            GridPane.setColumnIndex(newsChild, i);
            newsContainer.getChildren().add(newsChild);
        }

        // Hide subsequent columns
        for (int i = numberOfColumnsToShow; i < 6; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(0);
            newsContainer.getColumnConstraints().add(columnConstraints);
        }

        homeContainer.getChildren().add(newsContainer);
    }

    private VBox createNewsVbox(String rootPath){
        VBox newsChild = new VBox();
        newsChild.setPrefWidth(280);
        newsChild.setStyle("-fx-border-color: white");

        //create date and time
        HBox date = createNewsTimeStamp(rootPath + "icons/calendar.png", "6 November 2023");
        HBox time = createNewsTimeStamp(rootPath + "icons/icons8-time-24.png", "2 Hours Ago");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox dateTimeContainer = new HBox(date, region, time);
        dateTimeContainer.setPadding(new Insets(0, 0, 5, 0));

        //create news image
        ImageView newsImage = new ImageView(rootPath + "images/news/FWG_2560x1440.jpg");
        newsImage.setFitWidth(280);
        newsImage.setFitHeight(250);

        //create news title
        Label newsTitle = new Label("Climate change: Rise in Google searches around ‘anxiety’");
        newsTitle.getStyleClass().add("newsTitle");
        newsTitle.setPadding(new Insets(10, 5, 0, 5));

        //adding nodes to respective containers
        newsChild.getChildren().addAll(dateTimeContainer, newsImage, newsTitle);
        return newsChild;
    }
    private HBox createNewsTimeStamp(String iconPath, String details){
        HBox hbox = new HBox();
        ImageView icon = new ImageView(iconPath);
        icon.setFitWidth(15);
        icon.setFitHeight(15);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(1);
        icon.setEffect(colorAdjust);
        Label label = new Label(details);
        label.setFont(new Font(12));
        hbox.getChildren().addAll(icon, label);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(4);
        return hbox;
    }
}
