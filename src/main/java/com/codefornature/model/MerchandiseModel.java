package com.codefornature.model;

public class MerchandiseModel {
    private int merchandise_id;
    private String merchandise_name;
    private String image_name;
    private int cost;

    public MerchandiseModel(int merchandise_id, String merchandise_name, String image_name, int cost) {
        this.merchandise_id = merchandise_id;
        this.merchandise_name = merchandise_name;
        this.image_name = image_name;
        this.cost = cost;
    }

    public int getMerchandise_id() {
        return merchandise_id;
    }

    public String getMerchandise_name() {
        return merchandise_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "MerchandiseModel{" +
                "merchandise_id=" + merchandise_id +
                ", merchandise_name='" + merchandise_name + '\'' +
                ", image_name='" + image_name + '\'' +
                ", cost=" + cost +
                '}';
    }
}
