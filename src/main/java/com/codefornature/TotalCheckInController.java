package com.codefornature;

import com.codefornature.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TotalCheckInController {
    @FXML
    private Label rewardMessage;
    @FXML
    private Label dayLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private HBox streakPointsContainer;
    private int totalCheckInDays = 0;
    private boolean isStreakBroken = false;
    private int pointsAwarded;

    public void setPoints(Circle circle, int day){
        if(day > 0){
            circle.getStyleClass().add("streak");
        }
    }

    public void setTotalCheckInDays(int totalCheckInDays){
        this.totalCheckInDays = totalCheckInDays;
        updateGUI();
    }

    public void setPointsAwarded(int pointsAwarded){
        this.pointsAwarded = pointsAwarded;
    }

    public void setStreakBroken(boolean isStreakBroken){
        this.isStreakBroken = isStreakBroken;
    }

    public void updateGUI(){
        int i = 1;
        for(Node node : streakPointsContainer.getChildren()){
            VBox pointsContainer = (VBox) node;
            for(Node points : pointsContainer.getChildren()){
                if(points instanceof Circle){
                    Circle circle = (Circle) points;
                    setPoints(circle, i <= totalCheckInDays ? i : 0);
                    i++;
                }
            }
        }
        dayLabel.setText(String.format("%d Day Streak", totalCheckInDays));
        if(totalCheckInDays == 5){
            messageLabel.setText("Congratulations! You have continuously checked in for 5 days!");
            rewardMessage.setText("You have been rewarded with " + pointsAwarded * 2 + " points!");
            return;
        }

        if(isStreakBroken){
            messageLabel.setText("Oops! Your streak is broken.");
        }
        else{
            messageLabel.setText("Keep up the streak!");
        }

        rewardMessage.setText("You are rewarded with " + pointsAwarded + " point!");
    }

    public void onContinueClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
