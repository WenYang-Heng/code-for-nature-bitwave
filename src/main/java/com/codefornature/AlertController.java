package com.codefornature;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class AlertController {
    @FXML
    private VBox alertBox;
    @FXML
    private Label alertType;
    @FXML
    private Label alertMessage;
    @FXML
    private ImageView alertIcon;
    @FXML
    private Button closeButton;

    public void setAlertType(String type) {
        alertType.setText(type);
    }

    public void setAlertMessage(String message) {
        alertMessage.setText(message);
        alertMessage.setWrapText(true);
    }

    public void setAlertIcon(int alertType) {
        String rootPath = System.getProperty("user.dir") + "/src/main/resources/assets/icons/";
        String imageName = null;
        switch (alertType){
            case 0:
                imageName = "error.png";
                break;
            case 1:
                imageName = "check.png";
        }
        Image icon = new Image(rootPath + imageName);
        alertIcon.setImage(icon);
    }

    @FXML
    private void closeAlert() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public static void showAlert(String type, String message, int alertType) {
        try {
            FXMLLoader loader = new FXMLLoader(AlertController.class.getResource("alert.fxml"));
            Parent root = loader.load();

            AlertController controller = loader.getController();
            controller.setAlertType(type);
            controller.setAlertMessage(message);
            controller.setAlertIcon(alertType);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setResizable(false);

            stage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(event -> stage.close());
            delay.play();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
