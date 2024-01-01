package com.codefornature;

import com.codefornature.dao.TreePlantDAO;
import com.codefornature.dao.UserDAO;
import com.codefornature.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TreePlantController {
    @FXML
    private TextField treeNameTextField;
    @FXML
    private Label errorMessageLabel;
    private UserModel user;
    private int treeCost;

    public void setUser(UserModel user){
        this.user = user;
    }

    public void setTreeCost(int treeCost){
        this.treeCost = treeCost;
    }

    public void onCancelClicked(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void onPlantClicked(ActionEvent actionEvent) throws SQLException {
        String treeName = treeNameTextField.getText();
        if(treeName.isEmpty()){
            errorMessageLabel.setText("Please enter a name!");
        }
        else{
            TreePlantDAO treePlantDAO = new TreePlantDAO();
            treePlantDAO.insertTree(treeName, user.getUser_id());
            String treeRecord = String.format("%s plants a tree with the name \"%s\"%n", user.getUsername(), treeName);
            treePlantDAO.writeTreePlantToFile(treeRecord);
            UserDAO userDAO = new UserDAO();
            userDAO.updatePoints(user.getUser_id(), -treeCost);
            user.setPoints(user.getPoints() - treeCost);
            closeWindow(actionEvent);
        }
    }

    public void closeWindow(ActionEvent actionEvent){
        Stage stage = (Stage)((Node)(actionEvent.getSource())).getScene().getWindow();
        stage.close();
    }
}
