package com.codefornature.dao;

import com.codefornature.ConnectionManager;
import com.codefornature.model.UserModel;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDAO {

    public boolean insertUser(String email, String password, String username) throws SQLException {
        String query = "INSERT INTO user(email, password, username, register_date, points, total_check_in) " +
                "VALUES(?, ?, ?, curdate(), ?, ?)";
        int rowsUpdated;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, email);
                ps.setString(2, password);
                ps.setString(3, username);
                ps.setInt(4, 0);
                ps.setInt(5, 0);
                rowsUpdated = ps.executeUpdate();
            }
        }
        return rowsUpdated > 0;
    }

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
                        String username = rs.getString("username");
                        String date = rs.getString("register_date");
                        int points = rs.getInt("points");
                        int total_check_in = rs.getInt("total_check_in");
                        Timestamp last_claim_date = rs.getTimestamp("last_claim_date");

                        user = new UserModel(user_id, email, username, date, points, total_check_in, last_claim_date);
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

    public boolean updateLastClaimDate(java.util.Date date, int user_id, int pointsAwarded) throws SQLException {
        String query = "UPDATE user SET last_claim_date = ?, " +
                "points = points + ? + CASE WHEN total_check_in = 4 THEN ? ELSE 0 END, " +
                "total_check_in = CASE WHEN total_check_in = 4 THEN 0 ELSE total_check_in + 1 END " +
                "WHERE user_id = ?";
        int rowsUpdated;

        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                String currentDate = convertDateFormat(date);
                ps.setString(1, currentDate);
                ps.setInt(2, pointsAwarded);
                ps.setInt(3, pointsAwarded);
                ps.setInt(4, user_id);
                rowsUpdated = ps.executeUpdate();
                System.out.println("rows updated: " + rowsUpdated);
            }
        }
        return rowsUpdated > 0;
    }

    public void resetTotalCheckIn(int user_id) throws SQLException{
        String query = "UPDATE user SET total_check_in = 0 where user_id = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                ps.executeUpdate();
            }
        }
    }

    public Boolean emailExists(String email) throws SQLException {
        String query = "SELECT 1 FROM user WHERE email = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, email);
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        }
    }

    public Boolean updatePoints(int user_id, int points) throws SQLException {
        String query = "UPDATE user SET points = points + ? WHERE user_id = ?";
        System.out.println(points + " to be updated");
        int rowsUpdated;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, points);
                ps.setInt(2, user_id);
                rowsUpdated = ps.executeUpdate();
            }
        }
        return rowsUpdated > 0;
    }

    public String convertDateFormat(java.util.Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public boolean isLocked(String email) throws SQLException {
        String query = "select is_locked from user where email = ?";
        boolean isLocked = false;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, email);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        isLocked = rs.getBoolean(1);
                    }
                }
            }
        }
        return isLocked;
    }

    public void updateIsLocked(String email) throws SQLException{
        String query = "UPDATE user SET is_locked = !is_locked WHERE email = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, email);
                ps.executeUpdate();
            }
        }
    }

    public boolean isPasswordIdentical(String email, String password) throws SQLException{
        String query = "SELECT 1 FROM user where email = ? AND password = ?";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, email);
                ps.setString(2, password);
                try(ResultSet rs = ps.executeQuery()){
                    return rs.next();
                }
            }
        }
    }

    public boolean updatePassword(String email, String password) throws SQLException{
        String query = "UPDATE user SET password = ? WHERE email = ?";
        int rowsUpdated;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, password);
                ps.setString(2, email);
                rowsUpdated = ps.executeUpdate();
            }
        }
        return rowsUpdated > 0;
    }

}
