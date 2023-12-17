package com.codefornature;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;

public class HomeController {
    @FXML
    VBox homeContainer;
    private String rootPath;
    private int visibleColumns = 3;

    public void initialize() throws IOException{
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        createNewsUI();
    }

    public void createNewsUI(){
        //create the containers
        GridPane newsContainer = new GridPane();
        newsContainer.setStyle("-fx-border-color: green");
        newsContainer.setAlignment(Pos.CENTER);
        newsContainer.setHgap(30);
        newsContainer.getStyleClass().add("label");

        //create news header
        HBox newsHeader = new HBox();
        Label newsHeaderTitle = new Label("News");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Button leftBtn = createCarouselButtons("left-arrow.png");
        leftBtn.setOnAction(e -> shiftRight(newsContainer));

        Button rightBtn = createCarouselButtons("right-arrow.png");
        rightBtn.setOnAction(e -> shiftLeft(newsContainer));

        newsHeader.getChildren().addAll(newsHeaderTitle, region, leftBtn, rightBtn);
        newsHeader.setAlignment(Pos.CENTER);
        newsHeader.setPadding(new Insets(0, 45, 5, 45));

        for(int i = 0; i < 7; i++){
            VBox newsChild = createNewsVbox(i);
            ColumnConstraints column = new ColumnConstraints();
            if(i >= 3) {
                newsChild.setManaged(false);
                newsChild.setVisible(false);
            }
            newsContainer.add(newsChild, i, 0);
        }

        // Hide subsequent columns
//        for (int i = visibleColumns; i < 6; i++) {
//            ColumnConstraints columnConstraints = new ColumnConstraints();
//            columnConstraints.setPercentWidth(0);
//            newsContainer.getColumnConstraints().add(columnConstraints);
//        }

        homeContainer.getChildren().addAll(newsHeader, newsContainer);
    }

    private void shiftRight(GridPane gridPane){
        Node lastNode = getNodeFromGridPane(gridPane, gridPane.getChildren().size() - 1, 0);

        for(int i = gridPane.getChildren().size() - 2; i >= 0; i--){
            Node node = getNodeFromGridPane(gridPane, i, 0);
            if(node != null){
                GridPane.setColumnIndex(node, i + 1);
                if(i == 2 && node.isVisible() == true){
                    node.setVisible(false);
                    node.setManaged(false);
                }
            }
        }

        if(lastNode != null){
            GridPane.setColumnIndex(lastNode, 0);
            lastNode.setVisible(true);
            lastNode.setManaged(true);
        }

    }

    private void shiftLeft(GridPane gridPane){

        Node firstNode = getNodeFromGridPane(gridPane, 0, 0);
        firstNode.setVisible(false);
        firstNode.setManaged(false);

        for(int i = 1; i < gridPane.getChildren().size() ; i++){
            Node node = getNodeFromGridPane(gridPane, i, 0);
            if(node != null){
                GridPane.setColumnIndex(node, i - 1);
                if(i == 3 && node.isVisible() == false){
                    node.setManaged(true);
                    node.setVisible(true);
                }
            }
        }

        if(firstNode != null)
            GridPane.setColumnIndex(firstNode, gridPane.getChildren().size() - 1);
    }

    private Button createCarouselButtons(String iconPath){
        ImageView imageView = new ImageView(rootPath + "icons/" + iconPath);
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

    private VBox createNewsVbox(int index){
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
        Label newsTitle = new Label(Integer.toString(index));
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

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row){
        for(Node node : gridPane.getChildren()){
            if(GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row){
                return node;
            }
        }
        return null;
    }
}
