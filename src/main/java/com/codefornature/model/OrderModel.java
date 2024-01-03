package com.codefornature.model;

import java.util.Date;

public class OrderModel {
    private int order_id;
    private String address;
    private String order_date;
    private int user_id;

    public OrderModel(int order_id, String address, String order_date, int user_id) {
        this.order_id = order_id;
        this.address = address;
        this.order_date = order_date;
        this.user_id = user_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getUser_id() {
        return user_id;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "order_id=" + order_id +
                ", address='" + address + '\'' +
                ", order_date=" + order_date +
                ", user_id=" + user_id +
                '}';
    }
}
