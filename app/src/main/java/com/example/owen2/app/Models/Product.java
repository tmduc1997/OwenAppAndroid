package com.example.owen2.app.Models;

import java.io.Serializable;

public class Product implements Serializable {
    public int Product_ID;
    public String Name;
    public String Image;
    public String Description;
    public int Price;
    public int ProCat_ID;
    public int New;
    public int Top;
    public int Freeship;
    public int Available;

    public Product(int product_ID, String name, String image, String description, int price, int proCat_ID, int aNew, int top, int freeship, int available) {
        Product_ID = product_ID;
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        ProCat_ID = proCat_ID;
        New = aNew;
        Top = top;
        Freeship = freeship;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getProCat_ID() {
        return ProCat_ID;
    }

    public void setProCat_ID(int proCat_ID) {
        ProCat_ID = proCat_ID;
    }

    public int getNew() {
        return New;
    }

    public void setNew(int aNew) {
        New = aNew;
    }

    public int getTop() {
        return Top;
    }

    public void setTop(int top) {
        Top = top;
    }

    public int getFreeship() {
        return Freeship;
    }

    public void setFreeship(int freeship) {
        Freeship = freeship;
    }

    public int getAvailable() {
        return Available;
    }

    public void setAvailable(int available) {
        Available = available;
    }
}
