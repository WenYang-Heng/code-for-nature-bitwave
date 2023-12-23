package com.codefornature.dao;

import com.codefornature.ConnectionManager;
import com.codefornature.model.UserModel;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
                        Timestamp last_claim_date = rs.getTimestamp("last_claim_date");
                        System.out.println("Retrieving from db, last claim date = " + last_claim_date);

                        user = new UserModel(user_id, email, date, points, total_check_in, last_claim_date);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return user;
    }

    public Date rewardLastClaimDate(int user_id) throws SQLException {
        String query = "SELECT last_claim_date from user where user_id = ?";
        Date lastClaimDate = null;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        lastClaimDate = rs.getDate("last_claim_date");
                    }
                }
            }
        }
        return lastClaimDate;
    }

    public boolean updateLastClaimDate(java.util.Date date, int user_id) throws SQLException {
        String query = "UPDATE user SET last_claim_date = ?, points = points + 100 WHERE user_id = ?";
        int rowsUpdated;

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                String currentDate = convertDateFormat(date);
                ps.setString(1, currentDate);
                ps.setInt(2, user_id);
                rowsUpdated = ps.executeUpdate();
                System.out.println("rows updated: " + rowsUpdated);
            }
        }
        return rowsUpdated > 0;
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

    public void updatePoints(int points){

    }

    public String convertDateFormat(java.util.Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}
