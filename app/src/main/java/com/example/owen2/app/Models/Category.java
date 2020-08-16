package com.example.owen2.app.Models;

public class Category {
    public int ProCat_ID;
    public String Name;
    public String Image;
    public String Description;

    public Category(int proCat_ID, String name, String image, String description) {
        ProCat_ID = proCat_ID;
        Name = name;
        Image = image;
        Description = description;
    }

    public int getProCat_ID() {
        return ProCat_ID;
    }

    public void setProCat_ID(int proCat_ID) {
        ProCat_ID = proCat_ID;
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
}
