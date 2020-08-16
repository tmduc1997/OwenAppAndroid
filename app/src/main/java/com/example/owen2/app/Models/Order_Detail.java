package com.example.owen2.app.Models;

public class Order_Detail {
    public String Name;
    public int Price;
    public String Image;
    public int Quantity;
    public int Totail;

    public Order_Detail(String name, int price, String image, int quantity, int totail) {
        Name = name;
        Price = price;
        Image = image;
        Quantity = quantity;
        Totail = totail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getTotail() {
        return Totail;
    }

    public void setTotail(int totail) {
        Totail = totail;
    }
}
