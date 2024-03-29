package com.codefornature;

import com.codefornature.dao.*;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class HomeController {
    private UserModel user;
    private UserDAO userDAO = new UserDAO();
    private CartDAO cartDAO = new CartDAO();
    private TriviaDAO triviaDAO = new TriviaDAO();
    @FXML
    VBox homeContainer;
    private String rootPath;
    private int visibleColumns = 3;
    private int pointsAwarded = 1;
    private int treeCost = 5;
    private boolean isStreakBroken = false;
    private Node firstItem;
    private Node lastItem;
    private Button leftBtn;
    private Button rightBtn;
    private Label pointsValue;
    private Button claimRewardButton = null;

    public void setUser(UserModel user) throws SQLException, IOException {
        this.user = user;
        distributeTrivia();
        createDashboardUI();
        createNewsUI();
        System.out.println(user.getPoints());
    }

    private void distributeTrivia() throws SQLException {
        TriviaDAO triviaDAO = new TriviaDAO();
        List<Trivia> trivia = TriviaDAO.readFile("TriviaSample.txt");
        int triviaSize = trivia.size();

        //calculate difference between 2 dates, 1/1/2024 - 5/1/2024 = 5 days
        long daysBetweenRegisterDate = calcCalandarDateDifference();

        //retrieve how many questions already distributed to the user
        int triviaDistributed = triviaDAO.getNumberOfTriviasDistributed(user.getUser_id());

        //all questions already distributed,no need to add anymore
        if(triviaDistributed == triviaSize){
            return;
        }

        //to account for when user registered, but did not login daily
        int actualDistributionNum = (int) (daysBetweenRegisterDate - triviaDistributed);
        int startingNum = 0;
        int endingNum = triviaSize;
        System.out.println("Question to be distributed: " + actualDistributionNum);
        if(actualDistributionNum < triviaSize){
            startingNum = triviaDistributed;
            //to ensure that question distributed does not exceed the trivia sample size
            endingNum = Math.min(startingNum + actualDistributionNum, triviaSize);
        }

        if(actualDistributionNum > 0){
            triviaDAO.insertTriviaDistributedRecord(user.getUser_id(), startingNum + 1, endingNum);
            AlertController.showAlert("Trivia", "New trivias added, complete them to gain points", 1);
        }
    }

    public void initialize() throws IOException {
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";

    }

    public void createDashboardUI() throws SQLException {
        Label pointsLabel = new Label("Points");
        pointsLabel.getStyleClass().add("points-label");
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
            int value = compareDates(CURRENT_DATE, lastClaimDate);
            if(value == 0){ //same day
                System.out.println("You already claimed your reward today. Come back tomorrow!");
                giftboxIcon = new ImageView(new Image(rootPath + "images/home/gift-box-with-a-bow.png"));
                updateClaimRewardButtonStatus();
            }
            else if(value > 0){//Different day, current day is greater than last checked in day
                //Check if difference in days is more than 1, if yes, streak is broken, reset total checked in days to 0
                long differenceInDays = calculateDifferenceInDays(lastClaimDate.toString(), CURRENT_DATE.toString());
                System.out.println("Difference in days: " + differenceInDays);
                if(differenceInDays > 1 && user.getTotal_check_in() != 0){
                    //reset the streak
                    System.out.println("Oops streak broken!");
                    user.setTotal_check_in(0);
                    userDAO.resetTotalCheckIn(user.getUser_id());
                    isStreakBroken = true;
                }
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
                    loadCheckInStreakView();
                    updatePointsDisplay();
                    updateClaimRewardButtonStatus();
                    //need to update user.setDate
                    user.setLast_claim_date(CURRENT_DATE);
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
        HBox.setHgrow(dailyRewardsVBox, Priority.ALWAYS);

        //Plant a Tree Section
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
        HBox.setHgrow(plantTreeVBox, Priority.ALWAYS);

        // Main HBox
        HBox homeDashboard = new HBox(pointsHBox, dailyRewardsVBox, plantTreeVBox);
        homeDashboard.setSpacing(20);
        homeDashboard.setAlignment(Pos.CENTER);
        homeDashboard.setPadding(new Insets(45));
        homeContainer.getChildren().add(homeDashboard);
    }

    private void loadCheckInStreakView() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("total-check-in-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);
            scene.setFill(Color.TRANSPARENT);
            TotalCheckInController totalCheckInController = loader.getController();
            totalCheckInController.setPointsAwarded(pointsAwarded);
            totalCheckInController.setStreakBroken(isStreakBroken);
            totalCheckInController.setTotalCheckInDays(user.getTotal_check_in() + 1);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
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
        FlowPane newsContainer = new FlowPane();
        newsContainer.setPadding(new Insets(0, 40, 0, 40));
        newsContainer.setHgap(30);
        newsContainer.setVgap(30);
        newsContainer.getStyleClass().add("label");

        //create news header
        HBox newsHeader = new HBox();
        Label newsHeaderTitle = new Label("News");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        newsHeader.getChildren().addAll(newsHeaderTitle);
        newsHeader.setPadding(new Insets(0, 45, 5, 45));


        NewsDAO newsDAO = new NewsDAO();

        List<NewsModel> newsList = newsDAO.getNews(); //read news from text and store in list
        newsList.sort(Comparator.comparing(NewsModel::getDate).reversed());
        newsList.removeIf(news -> !news.getTitle().toLowerCase().contains("nature"));
        int i = 0;
        for(NewsModel news : newsList){
            VBox newsChild = createNewsVbox(news);
            newsContainer.getChildren().add(newsChild);
            i++;
            if(i == 5) break;
        }

        homeContainer.getChildren().addAll(newsHeader, newsContainer);
    }

    public int compareDates(java.util.Date currentDate, java.util.Date lastClaimDate){
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        int value = dateTimeComparator.compare(currentDate, lastClaimDate);

        return value;
    }

    public long calculateDifferenceInDays(String dateString1, String dateString2){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd kk:mm:ss z yyyy");
        LocalDateTime date1 = LocalDateTime.parse(dateString1, dtf);
        LocalDateTime date2 = LocalDateTime.parse(dateString2, dtf);
        return Duration.between(date1, date2).toDays();
    }

    public long calcCalandarDateDifference() {
        LocalDate date = LocalDate.now();
        String inputDate = date.toString() + " 00:00:00";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDate = LocalDateTime.parse(inputDate, dtf);
        LocalDateTime registerDate = LocalDateTime.parse(user.getRegister_date() + " 00:00:00", dtf);
        System.out.println(currentDate);
        System.out.println(registerDate);
        return Duration.between(registerDate, currentDate).toDays() + 1;
    }

    private VBox createNewsVbox(NewsModel news){
        VBox newsChild = new VBox();
        newsChild.setPrefWidth(280);
        newsChild.getStyleClass().add("newsVbox");

        //create date and time
        HBox date = createNewsTimeStamp(rootPath + "icons/calendar.png", news.getDate().toString());
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox dateTimeContainer = new HBox(date, region);
        dateTimeContainer.setPadding(new Insets(0, 0, 5, 0));

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
}
