package com.example.owen2.app.Models;

public class OrderDetail_ID {
    int ID;
    int Quantity;

    public OrderDetail_ID(int ID, int quantity) {
        this.ID = ID;
        Quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
