package com.example.owen2.app.Models;

import android.util.Log;

public class Card {
    public int Product_ID;
    public String Name;
    public String Image;
    public int Price;
    public int Quantity;
    public int OrderDetail_ID;
    public String Classify;
    public int Available;

    public Card(int product_ID, String name, String image, int price, int quantity, int orderDetail_ID, String classify, int available) {
        Product_ID = product_ID;
        Name = name;
        Image = image;
        Price = price;
        Quantity = quantity;
        OrderDetail_ID = orderDetail_ID;
        Classify = classify;
        Available = available;
    }

    public int getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(int product_ID) {
        Product_ID = product_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getOrderDetail_ID() {
        return OrderDetail_ID;
    }

    public void setOrderDetail_ID(int orderDetail_ID) {
        OrderDetail_ID = orderDetail_ID;
    }

    public String getClassify() {
        return Classify;
    }

    public void setClassify(String classify) {
        Classify = classify;
    }

    public int getAvailable() {
        return Available;
    }

    public void setAvailable(int available) {
        Available = available;
    }
}
