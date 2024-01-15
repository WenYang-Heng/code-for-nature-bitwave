package com.codefornature;

import com.codefornature.dao.TriviaDAO;
import com.codefornature.model.UserModel;
import com.codefornature.model.UserTriviaModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuizListController {
    @FXML
    private VBox quizListContainer;
    private HBox quiz;
    private Label quizNumber;
    private UserModel user;
    private TriviaDAO triviaDAO;
    private List<UserTriviaModel> triviaStatusList;

    public void setUser(UserModel user) throws SQLException {
        triviaDAO = new TriviaDAO();
        this.user = user;
        createQuizListGUI();
    }

    private void createQuizListGUI() throws SQLException {

        Label quizTitle = new Label("Trivias");
        quizTitle.getStyleClass().add("quiz-title");
        quizListContainer.getChildren().add(quizTitle);
        triviaStatusList = triviaDAO.getTriviasByUser(user.getUser_id()); //get all trivias that is currently available
        for (UserTriviaModel triviaStatus : triviaStatusList) { //loop through the list to display available trivias
            quizNumber = new Label("Day " + triviaStatus.getTriviaNumber() + " Quiz");
            final Label attempts = new Label();
            final HBox completionStatus = new HBox();
            Label status = new Label();
            status.getStyleClass().add("status");
            completionStatus.setAlignment(Pos.CENTER);
            completionStatus.getChildren().add(status);
            completionStatus.getStyleClass().add("completion-status"); //general design style, rounded corners, padding

            updateCompletionStatus(completionStatus, triviaStatus, attempts);

            HBox attemptsContainer = new HBox(attempts);
            HBox.setHgrow(attemptsContainer, Priority.ALWAYS);
            attemptsContainer.setAlignment(Pos.CENTER);

            quiz = new HBox(quizNumber, attemptsContainer, completionStatus);
            quiz.getStyleClass().add("quiz");
            quiz.setOnMouseClicked(event -> {
                onQuizClicked(triviaStatus, completionStatus, attempts);
            });

            quizListContainer.getChildren().add(quiz);
        }
    }

    private void onQuizClicked(UserTriviaModel triviaStatus, HBox completionStatus, Label attempts) {
        int prevAttempts = triviaStatus.getAttempts();
        System.out.println("prev attempt: " + prevAttempts);
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Question.fxml"));
            Parent root = loader.load();
            QuizController quizController = loader.getController();
            quizController.setTriviaStatus(triviaStatus, user);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();
            if(prevAttempts != 0 && triviaStatus.getAttempts() > 0){
                //user has not completed the question, but has attempted
                TriviaDAO.updateAttempts(user.getUser_id(), triviaStatus.getTriviaNumber(), triviaStatus.getAttempts());
            }
            updateCompletionStatus(completionStatus, triviaStatus, attempts);
        } catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }

    public void updateCompletionStatus(HBox completionStatus, UserTriviaModel triviaStatus, Label attempts) {
        Label status = null;
        attempts.setText("Attempts: " + triviaStatus.getAttempts());
        for(Node node : completionStatus.getChildren()){
            status = (Label) node;
        }
        status.setText(triviaStatus.isAnswered() ? "Completed" : "Pending");
        completionStatus.getStyleClass().removeAll("completed", "pending"); //due to css order, have to clear either one of the styles first before applying a new style
        status.getStyleClass().removeAll("completed", "pending");
        completionStatus.getStyleClass().add(triviaStatus.isAnswered() ? "completed" : "pending");
        status.getStyleClass().add(triviaStatus.isAnswered() ? "completed" : "pending");
    }
}
