package com.codefornature;

import com.codefornature.dao.Trivia;
import com.codefornature.dao.TriviaDAO;
import com.codefornature.model.UserModel;
import com.codefornature.model.UserTriviaModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuizListController {
    @FXML
    private VBox quizListContainer;
    private HBox quiz;
    private Label quizNumber;
    private Label completionStatus;
    private UserModel user;
    private TriviaDAO triviaDAO;

    public void setUser(UserModel user) throws SQLException {
        triviaDAO = new TriviaDAO();
        this.user = user;
        createQuizListGUI();
    }

    private void createQuizListGUI() throws SQLException {

        List<UserTriviaModel> triviaStatusList = triviaDAO.getTriviasByUser(user.getUser_id());
        for (UserTriviaModel trivia : triviaStatusList) {
            quizNumber = new Label("Day " + trivia.getTriviaNumber() + " Quiz");
            completionStatus = new Label(trivia.isAnswered() ? "Completed" : "Pending");
            completionStatus.getStyleClass().add("completion-status");
            completionStatus.getStyleClass().add(trivia.isAnswered() ? "completed" : "pending");

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            quiz = new HBox(quizNumber, region, completionStatus);
            quiz.getStyleClass().add("quiz");
            quiz.setOnMouseClicked(event -> {
                onQuizClicked(trivia.getTriviaNumber(), trivia.isAnswered());
            });

            quizListContainer.getChildren().add(quiz);
        }
    }

    private void onQuizClicked(int triviaNumber, boolean isAnswered) {
        try{
            System.out.println(triviaNumber + " clicked");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Question.fxml"));
            Parent root = loader.load();
            QuizController quizController = loader.getController();
            quizController.setQuestionIndex(triviaNumber - 1, isAnswered);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
