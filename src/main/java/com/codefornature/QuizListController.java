package com.codefornature;

import com.codefornature.model.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class QuizListController {
    @FXML
    private VBox quizListContainer;
    private HBox quiz;
    private Label quizNumber;
    private Label completionStatus;
    private UserModel user;

    public void setUser(UserModel user) {
        this.user = user;
        System.out.println("Hello");
        createQuizListGUI();
    }

    private void createQuizListGUI() {
        quizNumber = new Label("Day 1 Quiz");
        completionStatus = new Label("Completed");
        completionStatus.getStyleClass().add("completion-status");
        completionStatus.getStyleClass().add("pending");
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        quiz = new HBox(quizNumber, region, completionStatus);
        quiz.getStyleClass().add("quiz");
        quizListContainer.getChildren().add(quiz);
    }
}
