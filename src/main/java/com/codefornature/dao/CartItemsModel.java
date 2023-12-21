package com.codefornature.dao;

public class CartItemsModel {
    private int merchandise_id;
    private String merchandise_name;
    private String image_name;
    private int cost;
    private int quantity;

    public CartItemsModel(int merchandise_id, String merchandise_name, String image_name, int cost, int quantity) {
        this.merchandise_id = merchandise_id;
        this.merchandise_name = merchandise_name;
        this.image_name = image_name;
        this.cost = cost;
        this.quantity = quantity;
    }

    public int getMerchandise_id() {
        return merchandise_id;
    }

    public void setMerchandise_id(int merchandise_id) {
        this.merchandise_id = merchandise_id;
    }

    public String getMerchandise_name() {
        return merchandise_name;
    }

    public void setMerchandise_name(String merchandise_name) {
        this.merchandise_name = merchandise_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItemsModel{" +
                "merchandise_id=" + merchandise_id +
                ", merchandise_name='" + merchandise_name + '\'' +
                ", image_name='" + image_name + '\'' +
                ", cost=" + cost +
                ", quantity=" + quantity +
                '}';
    }
}
