package com.codefornature.dao;

import com.codefornature.ConnectionManager;
import com.codefornature.model.UserModel;

import java.sql.*;

public class UserDAO {

    public UserModel getUser(String emailTxt, String passwordTxt) throws SQLException {
        UserModel user = null;
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, emailTxt);
                ps.setString(2, passwordTxt);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        int user_id = rs.getInt("user_id");
                        String email = rs.getString("email");
                        Date date = rs.getDate("register_date");
                        int points = rs.getInt("points");
                        int total_check_in = rs.getInt("total_check_in");

                        user = new UserModel(user_id, email, date, points, total_check_in);
                    }
                }
            }
        }
        return user;
    }

    public Boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, email);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next())
                        return true;
                }
            }
        }
        return false;
    }


}
