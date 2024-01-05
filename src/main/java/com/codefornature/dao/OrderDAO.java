package com.codefornature.dao;

import com.codefornature.ConnectionManager;
import com.codefornature.model.CartItemsModel;
import com.codefornature.model.OrderModel;
import com.codefornature.model.UserModel;
import javafx.scene.layout.Region;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO {
    private String address;
    private String orderDate;
    private UserModel user;

    public OrderDAO(String address, String orderDate, UserModel user){
        this.address = address;
        this.orderDate = orderDate;
        this.user = user;
    }

    public boolean createOrder() throws SQLException {
        String query = "INSERT INTO orders(address, order_date, user_id) VALUES(?, ?, ?)";
        int rowsUpdated;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, address);
                ps.setString(2, orderDate);
                ps.setInt(3, user.getUser_id());
                rowsUpdated = ps.executeUpdate();
            }
        }
        return rowsUpdated > 0;
    }

    public void addItemsToOrder(List<CartItemsModel> cartItems, int order_id) throws SQLException {
        String query = "INSERT INTO orders_merchandise(order_id, merchandise_id, quantity) VALUES(?, ?, ?)";
        int[] rowsUpdated = new int[0];
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                int i = 0;
                for(CartItemsModel item : cartItems){
                    ps.setInt(1, order_id);
                    ps.setInt(2, item.getMerchandise_id());
                    ps.setInt(3, item.getQuantity());
                    ps.addBatch();
                    i++;
                    if(i == cartItems.size()){
                        ps.executeBatch();
                    }
                }
            }
        }
    }

    public void writeOrdersToFile(List<CartItemsModel> cartItems){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/assets/text/MerchandiseOrder.txt", true))) {
            for (CartItemsModel item : cartItems) {
                String orderRecord = String.format("User %s orders %d %s to %s%n", user.getUsername(), item.getQuantity(), item.getMerchandise_name(), address);
                writer.write(orderRecord);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OrderModel getOrder(int user_id, String date) throws SQLException {
        String query = "SELECT * FROM orders where user_id = ? AND order_date = ?";
        OrderModel order = null;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                ps.setString(2, date);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        int order_id = rs.getInt("order_id");
                        String address = rs.getString("address");
                        String order_date = rs.getString("order_date");
                        int userID = rs.getInt("user_id");
                        order = new OrderModel(order_id, address, order_date, userID);
                    }
                }
            }
        }
        return order;
    }
}
