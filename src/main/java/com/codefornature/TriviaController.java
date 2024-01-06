package com.codefornature;

import com.codefornature.dao.TriviaDAO;
import com.codefornature.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class TriviaController {
    @FXML
    private Label dayLabel;
    @FXML
    private Label questionTitleLabel;
    @FXML
    private FlowPane answersContainer;
    @FXML
    private Label attemptsLabel;
    @FXML
    private Button submitButton;
    private UserModel user;

    @FXML
    public void initialize(){
        System.out.println("Trivia loaded");
//        loadQuestion();
    }

    private void loadQuestion() {
//        TriviaDAO triviaDAO = new TriviaDAO(user.getUser_id(), user.getRegister_date());
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
