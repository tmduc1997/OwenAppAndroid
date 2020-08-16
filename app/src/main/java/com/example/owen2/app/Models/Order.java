package com.example.owen2.app.Models;

public class Order {
    public int Order_ID;
    public int Customer_ID;
    public String CreateDate;
    public int Shipping_status;
    public int Discount;
    public String Shipped_Date;

    public Order(int order_ID, int customer_ID, String createDate, int shipping_status, int discount, String shipped_Date) {
        Order_ID = order_ID;
        Customer_ID = customer_ID;
        CreateDate = createDate;
        Shipping_status = shipping_status;
        Discount = discount;
        Shipped_Date = shipped_Date;
    }

    public int getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(int order_ID) {
        Order_ID = order_ID;
    }

    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public int getShipping_status() {
        return Shipping_status;
    }

    public void setShipping_status(int shipping_status) {
        Shipping_status = shipping_status;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public String getShipped_Date() {
        return Shipped_Date;
    }

    public void setShipped_Date(String shipped_Date) {
        Shipped_Date = shipped_Date;
    }
}
