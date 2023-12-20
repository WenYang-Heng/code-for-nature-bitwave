package com.codefornature.dao;

import com.codefornature.ConnectionManager;
import com.codefornature.model.MerchandiseModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MerchandiseDAO {
    public List<MerchandiseModel> getAllMerchandise() throws SQLException {
        List<MerchandiseModel> merchandiseList = new ArrayList<>();
        String query = "SELECT * FROM merchandise";

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        int merchId = rs.getInt("merchandise_id");
                        String merchName = rs.getString("merchandise_name");
                        String imageName = rs.getString("image_name");
                        int cost = rs.getInt("cost");
                        MerchandiseModel merchandise = new MerchandiseModel(merchId, merchName, imageName, cost);
                        merchandiseList.add(merchandise);
                    }
                }
            }
        }
        return merchandiseList;
    }
}
