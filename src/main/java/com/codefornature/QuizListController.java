package com.codefornature;

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
    private UserModel user;
    private TriviaDAO triviaDAO;
    private List<UserTriviaModel> triviaStatusList;

    public void setUser(UserModel user) throws SQLException {
        triviaDAO = new TriviaDAO();
        this.user = user;
        createQuizListGUI();
    }

    private void createQuizListGUI() throws SQLException {

        triviaStatusList = triviaDAO.getTriviasByUser(user.getUser_id()); //get all trivias that is currently available
        for (UserTriviaModel triviaStatus : triviaStatusList) { //loop through the list to display available trivias
            quizNumber = new Label("Day " + triviaStatus.getTriviaNumber() + " Quiz");
            final Label completionStatus = new Label();
            completionStatus.getStyleClass().add("completion-status"); //general design style, rounded corners, padding
            updateCompletionStatus(completionStatus, triviaStatus);

            Region region = new Region(); //put space between something
            HBox.setHgrow(region, Priority.ALWAYS);
            quiz = new HBox(quizNumber, region, completionStatus);
            quiz.getStyleClass().add("quiz");
            quiz.setOnMouseClicked(event -> {
                onQuizClicked(triviaStatus, completionStatus);
            });

            quizListContainer.getChildren().add(quiz);
        }
    }

    private void onQuizClicked(UserTriviaModel triviaStatus, Label completionStatus) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Question.fxml"));
            Parent root = loader.load();
            QuizController quizController = loader.getController();
            quizController.setTriviaStatus(triviaStatus, user);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            updateCompletionStatus(completionStatus, triviaStatus);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void updateCompletionStatus(Label completionStatus, UserTriviaModel triviaStatus) {
        completionStatus.setText(triviaStatus.isAnswered() ? "Completed" : "Pending");
        completionStatus.getStyleClass().removeAll("completed", "pending"); //due to css order, have to clear either one of the styles first before applying a new style
        completionStatus.getStyleClass().add(triviaStatus.isAnswered() ? "completed" : "pending");
    }
}
