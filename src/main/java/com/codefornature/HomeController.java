package com.codefornature;

import com.codefornature.dao.CartDAO;
import com.codefornature.dao.NewsDAO;
import com.codefornature.dao.TreePlantDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.NewsModel;
import com.codefornature.model.UserModel;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.joda.time.DateTimeComparator;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class HomeController {
    private UserModel user;
    private UserDAO userDAO = new UserDAO();
    private CartDAO cartDAO = new CartDAO();
    @FXML
    VBox homeContainer;

    @FXML Label testLabel;
    private String rootPath;
    private int visibleColumns = 3;
    private int pointsAwarded = 1;
    private int treeCost = 5;
    private Node firstItem;
    private Node lastItem;
    private Button leftBtn;
    private Button rightBtn;
    private Label pointsValue;
    private Button claimRewardButton = null;

    public void setUser(UserModel user) throws SQLException, IOException {
        this.user = user;
        createDashboardUI();
        createNewsUI();
        System.out.println(user.getPoints());
//        displayTrivia();
    }

    private void displayTrivia() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trivia-view.fxml"));
        Parent root = loader.load();
        TriviaController triviaController = loader.getController();
        triviaController.setUser(user);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() throws IOException {
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
    }

    public void createDashboardUI() throws SQLException {
        Label pointsLabel = new Label("Points");
        pointsLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        pointsLabel.setFont(new Font("System Bold", 22));
        pointsLabel.setPadding(new Insets(10, 0, 0, 0));

        pointsValue = new Label(Integer.toString(user.getPoints()));
        pointsValue.setTextFill(javafx.scene.paint.Color.WHITE);
        pointsValue.setFont(new Font(24));
        pointsValue.setPadding(new Insets(45, 0, 0, 0));

        ImageView coinIcon = new ImageView(new Image(rootPath + "images/home/coin.png"));
        coinIcon.setFitHeight(71);
        coinIcon.setFitWidth(71);
        coinIcon.setPickOnBounds(true);
        coinIcon.setPreserveRatio(true);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        VBox pointsContainer = new VBox(pointsLabel, pointsValue);

        HBox pointsHBox = new HBox(pointsContainer, region, coinIcon);
        pointsHBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(pointsHBox, Priority.ALWAYS);

        // Daily Rewards Section
        Label dailyRewardsLabel = new Label("Daily Rewards");
        dailyRewardsLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        dailyRewardsLabel.setFont(new Font("System Bold", 22));
        dailyRewardsLabel.setPadding(new Insets(10, 0, 0, 0));

        ImageView giftboxIcon = null;
        giftboxIcon = new ImageView(new Image(rootPath + "images/home/giftbox.png"));
        claimRewardButton = new Button("Claim Your Reward");

        Date lastClaimDate = user.getLast_claim_date();
        //Get current date
        final Date CURRENT_DATE = new Date();
        if(lastClaimDate != null){
            System.out.println("Gift claimed on: " + lastClaimDate);
            //Check date and compare
            if(compareDates(CURRENT_DATE, lastClaimDate) == 0){
                System.out.println("You already claimed your reward today. Come back tomorrow!");
                giftboxIcon = new ImageView(new Image(rootPath + "images/home/gift-box-with-a-bow.png"));
                updateClaimRewardButtonStatus();
            }
        }


        giftboxIcon.setFitHeight(55);
        giftboxIcon.setFitWidth(51);
        giftboxIcon.setPickOnBounds(true);
        giftboxIcon.setPreserveRatio(true);
        claimRewardButton.setPrefSize(133, 32);
        claimRewardButton.setStyle("-fx-background-radius: 50; -fx-background-color: #40B52C;");
        claimRewardButton.setTextFill(javafx.scene.paint.Color.WHITE);


        claimRewardButton.setOnAction(event -> {
            //update last claim date in database
            try {
                if(userDAO.updateLastClaimDate(CURRENT_DATE, user.getUser_id(), pointsAwarded)){
                    System.out.println("last claim date updated");
                    updatePointsDisplay();
                    updateClaimRewardButtonStatus();
                    //need to update user.setDate
                    user.setLast_claim_date(CURRENT_DATE);
                    String alertMessage = String.format("You are awarded %d point", pointsAwarded);
                    AlertController.showAlert("POINTS AWARDED", alertMessage, 1);
                }
                else{
                    System.out.println("last claim date not updated");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        VBox dailyRewardsVBox = new VBox(dailyRewardsLabel, giftboxIcon, claimRewardButton);
        dailyRewardsVBox.setAlignment(Pos.TOP_CENTER);
        dailyRewardsVBox.setSpacing(15);
//        dailyRewardsVBox.setStyle("-fx-border-color: #ffffff");
        HBox.setHgrow(dailyRewardsVBox, Priority.ALWAYS);

        // Plant a Tree Section
        Label plantTreeLabel = new Label("Plant a Tree");
        plantTreeLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        plantTreeLabel.setFont(new Font("System Bold", 22));
        plantTreeLabel.setPadding(new Insets(10, 0, 0, 0));

        ImageView treeIcon = new ImageView(new Image(rootPath + "images/home/tree.png"));
        treeIcon.setFitHeight(57);
        treeIcon.setFitWidth(50);
        treeIcon.setPickOnBounds(true);
        treeIcon.setPreserveRatio(true);

        Button plantNowButton = new Button("Plant Now");
        plantNowButton.setPrefSize(133, 32);
        plantNowButton.setStyle("-fx-background-radius: 50; -fx-background-color: #40B52C;");
        plantNowButton.setTextFill(javafx.scene.paint.Color.WHITE);
        plantNowButton.setOnAction(event -> {
            onPlantNowClicked();
        });

        VBox plantTreeVBox = new VBox(plantTreeLabel, treeIcon, plantNowButton);
        plantTreeVBox.setAlignment(Pos.TOP_CENTER);
        plantTreeVBox.setSpacing(15);
//        plantTreeVBox.setStyle("-fx-border-color: #ffffff");
        HBox.setHgrow(plantTreeVBox, Priority.ALWAYS);

        // Main HBox
        HBox homeDashboard = new HBox(pointsHBox, dailyRewardsVBox, plantTreeVBox);
        homeDashboard.setSpacing(20);
        homeDashboard.setAlignment(Pos.CENTER);
        homeDashboard.setPadding(new Insets(45));
        homeContainer.getChildren().add(homeDashboard);
    }

    private void onPlantNowClicked() {
        if(user.getPoints() < treeCost){
            AlertController.showAlert("Insufficient Points", "Not enough points to plant a tree.", 0);
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tree-plant-view.fxml"));
            Parent root = loader.load();
            TreePlantController treePlantController = loader.getController();
            treePlantController.setUser(user);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();
            pointsValue.setText(Integer.toString(user.getPoints()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void updatePointsDisplay() {
        if (user != null) {
            int points = user.getPoints();
            user.setPoints(points + (user.getTotal_check_in() == 4? pointsAwarded * 2 : pointsAwarded));
            pointsValue.setText(Integer.toString(user.getPoints()));
        }
    }

    public void updateClaimRewardButtonStatus() {
        if(claimRewardButton.isDisable()){
            this.claimRewardButton.setDisable(false);
        }
        else{
            this.claimRewardButton.setDisable(true);
            this.claimRewardButton.setText("Claimed");
        }
    }

    public void createNewsUI() throws IOException {
        //create the containers
        GridPane newsContainer = new GridPane();
//        newsContainer.setStyle("-fx-border-color: green");
        newsContainer.setAlignment(Pos.CENTER);
        newsContainer.setHgap(30);
        newsContainer.getStyleClass().add("label");

        //create news header
        HBox newsHeader = new HBox();
        Label newsHeaderTitle = new Label("News");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        leftBtn = createCarouselButtons("left-arrow.png");
        leftBtn.setOnAction(e -> shiftRight(newsContainer));
        leftBtn.setDisable(true); // first item is already displayed, no need for user to cycle left

        rightBtn = createCarouselButtons("right-arrow.png");
        rightBtn.setOnAction(e -> shiftLeft(newsContainer));

        newsHeader.getChildren().addAll(newsHeaderTitle, region, leftBtn, rightBtn);
        newsHeader.setAlignment(Pos.CENTER);
        newsHeader.setPadding(new Insets(0, 45, 5, 45));


        NewsDAO newsDAO = new NewsDAO();

        List<NewsModel> newsList = newsDAO.getNews();
        newsList.sort(Comparator.comparing(NewsModel::getDate).reversed());
        newsList.removeIf(news -> !news.getTitle().toLowerCase().contains("nature"));
        int i = 0;
        for(NewsModel news : newsList){
            VBox newsChild = createNewsVbox(news);
            if(i >= 3) {
                newsChild.setManaged(false);
                newsChild.setVisible(false);
            }
            newsContainer.add(newsChild, i, 0);
            i++;
            if(i == 4) break;
        }

        firstItem = getNodeFromGridPane(newsContainer, 0, 0);
        lastItem = getNodeFromGridPane(newsContainer,newsContainer.getChildren().size() - 1, 0);

        homeContainer.getChildren().addAll(newsHeader, newsContainer);
    }

    public int compareDates(java.util.Date currentDate, java.util.Date lastClaimDate){
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        int value = dateTimeComparator.compare(currentDate, lastClaimDate);

        return value;
    }

    private void updateCarouselButtonStates(GridPane gridPane){
        leftBtn.setDisable(GridPane.getColumnIndex(firstItem) == 0);
        rightBtn.setDisable(GridPane.getColumnIndex(lastItem) == 2);
    }

    private void shiftRight(GridPane gridPane){
        Node lastNode = getNodeFromGridPane(gridPane, gridPane.getChildren().size() - 1, 0);

        for(int i = gridPane.getChildren().size() - 2; i >= 0; i--){
            Node node = getNodeFromGridPane(gridPane, i, 0);
            if(node != null){
                GridPane.setColumnIndex(node, i + 1);
                //shifting 3rd node to 4th column
                if(i == 2 && node.isVisible()){
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

        updateCarouselButtonStates(gridPane);

    }

    private void shiftLeft(GridPane gridPane){

        Node firstNode = getNodeFromGridPane(gridPane, 0, 0);
        firstNode.setVisible(false);
        firstNode.setManaged(false);

        for(int i = 1; i < gridPane.getChildren().size() ; i++){
            Node node = getNodeFromGridPane(gridPane, i, 0);
            if(node != null){
                GridPane.setColumnIndex(node, i - 1);
                //shifting 4th node to 3rd column
                if(i == 3 && !node.isVisible()){
                    node.setManaged(true);
                    node.setVisible(true);
                }
            }
        }

        if(firstNode != null)
            GridPane.setColumnIndex(firstNode, gridPane.getChildren().size() - 1);

        updateCarouselButtonStates(gridPane);
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

    private VBox createNewsVbox(NewsModel news){
        VBox newsChild = new VBox();
        newsChild.setPrefWidth(280);
        newsChild.getStyleClass().add("newsVbox");

        //create date and time
        HBox date = createNewsTimeStamp(rootPath + "icons/calendar.png", news.getDate().toString());
//        HBox time = createNewsTimeStamp(rootPath + "icons/icons8-time-24.png", "2 Hours Ago");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox dateTimeContainer = new HBox(date, region);
        dateTimeContainer.setPadding(new Insets(0, 0, 5, 0));

        //create news image
//        ImageView newsImage = new ImageView(rootPath + "images/news/FWG_2560x1440.jpg");
//        newsImage.setFitWidth(280);
//        newsImage.setFitHeight(250);

        //create news title
        Hyperlink newsTitle = new Hyperlink(news.getTitle());
        newsTitle.setBorder(Border.EMPTY);
        newsTitle.getStyleClass().add("newsTitle");
        newsTitle.setPadding(new Insets(10, 5, 0, 5));
        newsTitle.setOnAction(event -> {
            System.out.println(newsTitle.getText() + " clicked");
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI(news.getNewsUrl());
                desktop.browse(uri);
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        //adding nodes to respective containers
        newsChild.getChildren().addAll(dateTimeContainer, newsTitle);
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
