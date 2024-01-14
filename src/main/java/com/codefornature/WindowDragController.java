package com.codefornature;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class WindowDragController {
    public static void windowDrag(HBox titleBar, Stage window){
        titleBar.setOnMousePressed(pressEvent -> {
            titleBar.setOnMouseDragged(dragEvent -> {
                window.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                window.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });
    }
}
