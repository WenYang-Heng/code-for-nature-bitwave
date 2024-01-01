package com.codefornature.dao;

import com.codefornature.ConnectionManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TreePlantDAO {
    public boolean insertTree(String treeName, int userId) throws SQLException {
        String query = "INSERT INTO tree(tree_name, plant_date, user_id) VALUES(?, curdate(), ?)";
        int rowsUpdated;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, treeName);
                ps.setInt(2, userId);
                rowsUpdated = ps.executeUpdate();
            }
        }
        return rowsUpdated > 0;
    }

    public void writeTreePlantToFile(String treeRecord){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/assets/text/TreePlantOrder.txt", true))) {
            writer.write(treeRecord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
