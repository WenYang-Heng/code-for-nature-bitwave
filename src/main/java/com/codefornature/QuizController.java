package com.codefornature;

import com.codefornature.dao.Trivia;
import com.codefornature.dao.TriviaDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.UserModel;
import com.codefornature.model.UserTriviaModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class QuizController {
    private int questionIndex;
    @FXML
    private Label dayLabel;
    @FXML
    private Label questionTitleLabel;
    @FXML
    private VBox triviaContainer;
    @FXML
    private Label attemptCount;
    @FXML
    private FlowPane answerFlowPane;
    @FXML
    private Button showAnswerButton;
    private String selectedAns;
    private int num_attempt = 2;
    private int pointsAwarded = 2;
    private boolean isAnswered = false;
    private Button currButton = null;
    private Button correctButton;
    private List<Trivia> trivia;
    private UserTriviaModel triviaStatus;
    private TriviaDAO triviaDAO;
    private String rootPath;
    private String correctAns;
    private Image correct;
    private Image wrong;
    private ImageView correctImageView;
    private ImageView wrongImageView;
    private UserModel user;

    public void loadGUI(){
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        trivia = TriviaDAO.readFile("TriviaSample.txt");
        dayLabel.setText(String.format("Day %d Trivia", questionIndex + 1));
        questionTitleLabel.setText(trivia.get(questionIndex).getQuestion());
        correctAns = trivia.get(questionIndex).getCorrectAnswer();

        //visibility of show answer button, if already answered previously, button will be visible, else not displayed
        if(!isAnswered){
            showAnswerButton.setVisible(false);
            showAnswerButton.setManaged(false);
        }

        //set icons and dimensions
        double imageDimension = 15;
        correct = new Image(rootPath + "/icons/check.png");
        wrong = new Image(rootPath + "/icons/close.png");
        correctImageView = new ImageView(correct);
        wrongImageView = new ImageView(wrong);
        correctImageView.setFitWidth(imageDimension);
        correctImageView.setFitHeight(imageDimension);
        wrongImageView.setFitWidth(imageDimension);
        wrongImageView.setFitHeight(imageDimension);


        triviaContainer.getStylesheets().add(getClass().getResource("/styles/quiz.css").toExternalForm());
        //add answers to each button and set an event handler dynamically
        addAnswersToButton();
    }

    private void addAnswersToButton() {
        int i = 0;
        Collections.shuffle(trivia.get(questionIndex).getChoices());
        //Flowpane
        //----Button
        //----Button
        //----Button
        //----Button
        for(Node node : answerFlowPane.getChildren()) {
            Button ans = (Button) node;
            ans.setText(trivia.get(questionIndex).getChoices().get(i));
            ans.setWrapText(true);
            final String answerText = ans.getText();
            if(answerText.equals(correctAns)){
                correctButton = ans; //store button with the correct answer, show to user when show answer is clicked or no more attempts
            }
            final Button button = ans;
            ans.setOnAction(event -> {
                selectAns(answerText, button);
            });
            i++;
        }
    }

    private void selectAns(String answerText, Button button) {
        //If one of the button is selected, it will not be null, so need to clear the style first, then update new currently selected button, reapply the style again
        if (currButton != null) {
            currButton.getStyleClass().remove("selectedAnswer");
        }
        currButton = button;
        currButton.getStyleClass().add("selectedAnswer");
        selectedAns = answerText;
    }

    public void confirmClicked(ActionEvent event) throws SQLException {
        triviaDAO = new TriviaDAO();
        String style = "correct";
        boolean answered = false;

        //user get it correct or no more attempts
        if(num_attempt == -1){
            closeWindow(event);
            return;
        }

        if(selectedAns.equals(correctAns)){
            answered = true;
            currButton.setGraphic(correctImageView);
            num_attempt = -1;
            disableAllAnswersOption();
        }
        else{
            style = "wrong";
            Collections.shuffle(trivia.get(questionIndex).getChoices());
            addAnswersToButton();
            currButton.setGraphic(wrongImageView);
            attemptCount.setText(--num_attempt + " Attempts left");
            pointsAwarded--;
        }
        currButton.getStyleClass().add(style);

        if(num_attempt == 0){
            answered = true;
            attemptCount.setText("No more attempts left");
            showCorrectAnswer();
            disableAllAnswersOption();
            num_attempt = -1;
        }

        if(answered){
            triviaStatus.setAnswered(true);
            triviaDAO.updateTriviaStatus(user.getUser_id(), triviaStatus.getTriviaNumber());
            //check if user got it correct within the attempts limit and this trivia is not answered previously
            if(pointsAwarded > 0 && !isAnswered){
                UserDAO userDAO = new UserDAO();
                if(userDAO.updatePoints(user.getUser_id(), pointsAwarded)){  //return true if database record is added
                    AlertController.showAlert("Points", "You have gained " + pointsAwarded + " points", 1);
                    user.setPoints(user.getPoints() + pointsAwarded);
                }
                else{
                    AlertController.showAlert("Error", "Something went wrong, points not updated.", 0);
                }
            }
            else{
                System.out.println("This question was completed previously.");
            }
        }
    }

    public void showCorrectAnswer(){
        correctButton.setGraphic(correctImageView);
        correctButton.getStyleClass().add("correct");
    }

    private void disableAllAnswersOption() {
        //loop through each button to disable it
        for (Node node : answerFlowPane.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
                node.setStyle("-fx-opacity: 1");
            }
        }
    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
        stage.close();
    }

    public void setTriviaStatus(UserTriviaModel triviaStatus, UserModel user){
        this.user = user;
        this.triviaStatus = triviaStatus;
        questionIndex = this.triviaStatus.getTriviaNumber() - 1;
        isAnswered = this.triviaStatus.isAnswered();
        loadGUI();
    }

    public void onShowAnswerClicked(ActionEvent actionEvent) {
        showCorrectAnswer();
        disableAllAnswersOption();
        num_attempt = -1;
    }
}
