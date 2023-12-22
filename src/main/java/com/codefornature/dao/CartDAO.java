package com.codefornature.dao;

import com.codefornature.ConnectionManager;
import com.codefornature.model.CartModel;
import com.codefornature.model.MerchandiseModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public boolean cartExists(int user_id) throws SQLException {
        String query = "SELECT 1 FROM cart WHERE user_id = ?";

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        }
    }

    public boolean itemExist(int merchandise_id, int cart_id) throws SQLException {
        String query = "SELECT 1 FROM cart_items WHERE merchandise_id = ? AND cart_id = ?";

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, merchandise_id);
                ps.setInt(2, cart_id);
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        }
    }

    public boolean addNewCart(int user_id) throws SQLException {
        String query = "INSERT INTO cart(user_id) VALUES(?)";
        int rowsUpdated;

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                rowsUpdated = ps.executeUpdate();
                System.out.printf("Rows affected %d%n", rowsUpdated);
            }
        }
        return rowsUpdated > 0;
    }

    public boolean updateItemQuantity(int merchandise_id, int cart_id, int quantity) throws SQLException {
        String query = "UPDATE cart_items SET quantity = ? WHERE merchandise_id = ? AND cart_id = ?";
        int rowsUpdated;

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, quantity);
                ps.setInt(2, merchandise_id);
                ps.setInt(3, cart_id);
                rowsUpdated = ps.executeUpdate();
                System.out.println("rows updated: " + rowsUpdated);
            }
        }

        return rowsUpdated > 0;
    }

    public CartModel getCart(int user_id) throws SQLException {
        String query = "SELECT * from cart WHERE user_id = ?";
        CartModel cart = null;
        try(Connection con = ConnectionManager.getConnection()) {
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        cart = new CartModel(rs.getInt("cart_id"), rs.getInt("user_id"));
                    }
                }
            }
        }
        return cart;
    }

    public boolean addCartItems(int cart_id, int merchandise_id, int quantity) throws SQLException {
        String query = "INSERT INTO cart_items(cart_id, merchandise_id, quantity) VALUES(?, ?, ?)";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, cart_id);
                ps.setInt(2, merchandise_id);
                ps.setInt(3, quantity);
                int r = ps.executeUpdate();
                return r > 0;
            }
        }
        catch (SQLException e) {
            e.printStackTrace(); // This will print any SQL exception that occurs
            throw e; // Rethrow the exception or handle it as appropriate
        }
    }

    public boolean removeCart(int cart_id) throws SQLException {
        String query = "DELETE FROM cart where cart_id = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, cart_id);
                int r = ps.executeUpdate();
                System.out.printf("%d row(s) deleted%n", r);
                return r > 0;
            }
        }
    }

    public boolean removeCartItems(int cart_id, int merchandise_id) throws SQLException {
        String query = "DELETE FROM cart_items where cart_id = ? AND merchandise_id = ?";

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, cart_id);
                ps.setInt(2, merchandise_id);
                int r = ps.executeUpdate();
                return r > 0;
            }
        }
    }

    public List<CartItemsModel> getCartItems(int cart_id) throws SQLException {
        String query = "SELECT * FROM merchandise m INNER JOIN cart_items c on m.merchandise_id = c.merchandise_id WHERE cart_id = ?";

        List<CartItemsModel> cartItems = new ArrayList<>();
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, cart_id);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        int merchId = rs.getInt("merchandise_id");
                        String merchName = rs.getString("merchandise_name");
                        String imageName = rs.getString("image_name");
                        int cost = rs.getInt("cost");
                        int quantity = rs.getInt("quantity");
                        CartItemsModel item = new CartItemsModel(merchId, merchName, imageName, cost, quantity);
                        cartItems.add(item);
                    }
                }
            }
        }
        return cartItems;
    }

    public int getItemQuantity(int merchandise_id, int cart_id) throws SQLException {
        int quantity = 0;
        String query = "SELECT quantity from cart_items where merchandise_id = ? AND cart_id = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, merchandise_id);
                ps.setInt(2, cart_id);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        quantity = rs.getInt("quantity");
                    }
                }
            }
        }
        return quantity;
    }

}
