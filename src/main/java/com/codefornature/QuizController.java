package com.codefornature;

import com.codefornature.dao.Trivia;
import com.codefornature.dao.TriviaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class QuizController {
    private int questionIndex = 1;
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
    private String selectedAns;
    private int num_attempt = 2;
    private Button currButton = null;
    private Button correctButton;
    private Button prevButton = null;
    private List<Trivia> trivia;
    private String rootPath;
    private String correctAns;
    private Image correct;
    private Image wrong;
    private ImageView correctImageView;
    private ImageView wrongImageView;

    @FXML
    public void initialize() {
        loadGUI();
    }

    public void loadGUI(){
        trivia = TriviaDAO.readFile("TriviaSample.txt");
        rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/";
        dayLabel.setText(String.format("Day %d Trivia", questionIndex + 1));
        questionTitleLabel.setText(trivia.get(questionIndex).getQuestion());
        correctAns = trivia.get(questionIndex).getCorrectAnswer();

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
        int i = 0;
        //add answers to each button and set an event handler dynamically
        for(Node node : answerFlowPane.getChildren()) {
            Button ans = (Button) node;
            ans.setText(trivia.get(questionIndex).getChoices().get(i));
            ans.setWrapText(true);
            final String answerText = ans.getText();
            if(answerText.equals(correctAns)){
                correctButton = ans;
            }
            final Button button = ans;
            ans.setOnAction(event -> {
                selectAns(answerText, button);
            });
            i++;
        }
    }

    private void selectAns(String answerText, Button button) {
        if (currButton != null) {
            currButton.getStyleClass().remove("selectedAnswer");
        }
        currButton = button;
        currButton.getStyleClass().add("selectedAnswer");
        selectedAns = answerText;
    }

    public void confirmClicked(ActionEvent event) {

        if(num_attempt == -1){
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }

        String style = "correct";

        if(selectedAns.equals(correctAns)){
            currButton.setGraphic(correctImageView);
            num_attempt = -1;
            disableAllAnswersOption();
        }
        else{
            style = "wrong";
            currButton.setGraphic(wrongImageView);
            attemptCount.setText(--num_attempt + " Attempts left");
        }
        currButton.getStyleClass().add(style);

        if(num_attempt == 0){
            attemptCount.setText("No more attempts left");
            correctButton.setGraphic(correctImageView);
            correctButton.getStyleClass().add("correct");
            disableAllAnswersOption();
            num_attempt = -1;
        }
    }

    private void disableAllAnswersOption() {
        for (Node node : answerFlowPane.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
                node.setStyle("-fx-opacity: 1");
            }
        }
    }

    public void closedWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node)(event.getSource())).getScene().getWindow();
        stage.close();
    }

    public void setQuestionIndex(int questionIndex){
        this.questionIndex = questionIndex;
    }
}
