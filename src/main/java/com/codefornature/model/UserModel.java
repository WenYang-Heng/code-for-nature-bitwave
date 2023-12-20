package com.codefornature.model;

import java.sql.Date;

public class UserModel {
    private int user_id;
    private String email;
    private Date register_date;
    private int points;
    private int total_check_in;

    public UserModel(int user_id, String email, Date register_date, int points, int total_check_in) {
        this.user_id = user_id;
        this.email = email;
        this.register_date = register_date;
        this.points = points;
        this.total_check_in = total_check_in;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public Date getRegister_date() {
        return register_date;
    }

    public int getPoints() {
        return points;
    }

    public int getTotal_check_in() {
        return total_check_in;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRegister_date(Date register_date) {
        this.register_date = register_date;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTotal_check_in(int total_check_in) {
        this.total_check_in = total_check_in;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", register_date=" + register_date +
                ", points=" + points +
                ", total_check_in=" + total_check_in +
                '}';
    }
}
