package com.codefornature.model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserModel {
    private int user_id;
    private String email;
    private String username;
    private Date register_date;
    private int points;
    private int total_check_in;
    private java.util.Date last_claim_date;

    public UserModel(int user_id, String email, String username, Date register_date, int points, int total_check_in, java.util.Date last_claim_date) throws ParseException {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.register_date = register_date;
        this.points = points;
        this.total_check_in = total_check_in;
        this.last_claim_date = convertDateFormat(last_claim_date);
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() { return username; }

    public Date getRegister_date() {
        return register_date;
    }

    public int getPoints() {
        return points;
    }

    public int getTotal_check_in() {
        return total_check_in;
    }

    public java.util.Date getLast_claim_date(){
        return last_claim_date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTotal_check_in(int total_check_in) {
        this.total_check_in = total_check_in;
    }

    public void setLast_claim_date(java.util.Date last_claim_date) {
        this.last_claim_date = last_claim_date;
    }

    public java.util.Date convertDateFormat(java.util.Date date) throws ParseException {
        if(date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
        String formattedDate = sdf.format(date);
        java.util.Date last_claim_date = sdf.parse(formattedDate);
        return last_claim_date;
    }


    @Override
    public String toString() {
        return "UserModel{" +
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", register_date=" + register_date +
                ", points=" + points +
                ", total_check_in=" + total_check_in +
                ", last_claim_date=" + last_claim_date +
                '}';
    }
}
