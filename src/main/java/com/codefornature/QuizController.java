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

    private int questionIndex = 7;
    @FXML
    private AnchorPane anchor;
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
    private String correctAns = "1";
    private boolean isConfirm;
    private int num_attempt = 2;
    private Button currButton = null;
    private Button correctButton;
    private Button prevButton = null;
    @FXML
    private Button confirmButton;
    private List<Trivia> trivia;

    public Button findCorrectButton(FlowPane answerFlowPane) {
        for(Node node : answerFlowPane.getChildren()) {
            if (node instanceof Button) {
                Button currButton = (Button) node;
                if (currButton.getText().equals(correctAns)) {
                    return currButton;
                }
            }
        }
        return null;
    }

    @FXML
    public void initialize() {
        System.out.println("LOL");
        trivia = TriviaDAO.readFile("TriviaSample.txt");

        dayLabel.setText("Day " + questionIndex + 1 + " Question");
        questionTitleLabel.setText(trivia.get(questionIndex).getQuestion());

        triviaContainer.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        correctButton = findCorrectButton(answerFlowPane);
        int i = 0;
        for(Node node : answerFlowPane.getChildren()) {
            Button ans = (Button) node;
            ans.setText(trivia.get(questionIndex).getChoices().get(i));
            ans.setWrapText(true);
            final String answerText = ans.getText();
            final Button button = ans;
            ans.setOnAction(event -> {
                selectAns(answerText, button);
            });
            i++;
        }

    }

    private void selectAns(String answerText, Button button) {
        //selectedAns = answerText;
        //prevButton = button;
        //button.getStyleClass().add("selectedAnswer");
        //button.setStyle("-fx-border-color: yellow");
        //System.out.println(answerText);
        //currButton = button;

        if (currButton != null) {
            currButton.getStyleClass().remove("selectedAnswer");
        }
        currButton = button;
        currButton.getStyleClass().add("selectedAnswer");
        selectedAns = answerText;

    }

    public void confirmClicked(ActionEvent event) {
        isConfirm = true;
        System.out.println("I selected " + selectedAns);

        //if attempt = 0
        if (num_attempt == 0) {
            closedWindow(event);
        }

        if (num_attempt > 1) {
            num_attempt -= 1;
            System.out.println("Number of attempt: " + num_attempt);
            attemptCount.setText(num_attempt + " attempts left");

            try {
                if (selectedAns.equals(correctAns) && isConfirm) {
                    currButton.setStyle("-fx-border-color: lightgreen;");
                    Image image = new Image("C:/Users/User/OneDrive/Pictures/Screenshots/black_green_icon.jpg");
                    ImageView imageView = new ImageView(image);
                    currButton.setGraphic(imageView);
                    attemptCount.setText("Congratulations!! Your first option is correct!!");
                    //Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    //stage.close();
                    num_attempt = 0;
                    confirmButton.setText("Continue ->");

                } else if (!selectedAns.equals(correctAns) && isConfirm) {
                    currButton.setStyle("-fx-border-color: red;");
                    Image image = new Image("C:/Users/User/OneDrive/Pictures/Screenshots/red_cross_icon.jpg");
                    ImageView imageView = new ImageView(image);
                    currButton.setGraphic(imageView);

                }
            } catch (Exception e) {
                System.out.println("No option is selected, pls try again!");
                //e.printStackTrace();
            }

        } else {
            num_attempt -= 1;
            System.out.println("Number of attempt: " + num_attempt);
            attemptCount.setText(num_attempt + " attempts left");

            //Wrong Answer
            currButton.setStyle("-fx-border-color: red;");
            Image image1 = new Image("C:/Users/User/OneDrive/Pictures/Screenshots/red_cross_icon.jpg");
            ImageView imageView1 = new ImageView(image1);
            currButton.setGraphic(imageView1);

            //Correct Answer
            correctButton.setStyle("-fx-border-color: lightgreen");
            Image image2 = new Image("C:/Users/User/OneDrive/Pictures/Screenshots/black_green_icon.jpg");
            ImageView imageView2 = new ImageView(image2);
            correctButton.setGraphic(imageView2);

            confirmButton.setText("Continue ->");

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
