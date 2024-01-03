package com.codefornature.model;

public class CartModel {
    private static int cart_id;
    private static int user_id;

    public CartModel(int cart_id, int user_id) {
        this.cart_id = cart_id;
        this.user_id = user_id;
    }

    public static int getCart_id() {
        return cart_id;
    }

    public static int getUser_id() {
        return user_id;
    }

    public static void setCart_id(int cartId) {
        cart_id = cartId;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "cart_id=" + cart_id +
                ", user_id=" + user_id +
                '}';
    }
}
