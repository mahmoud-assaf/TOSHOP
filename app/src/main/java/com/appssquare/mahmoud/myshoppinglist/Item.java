package com.appssquare.mahmoud.myshoppinglist;

/**
 * Created by mahmoud on 27/01/2018.
 */

public class Item {
    int id;

    String name;
    String userId;
    String shopName;
    int quantity;
    int shopId;
    long reminder;        //dateTime
    String category;
    String notes;

    public Item(){

    }
    public Item(int id, String title, String userId, int quantity, int shopId,String shopNamee, long reminder, String category, String notes) {
        this.id = id;
        this.name = title;
        this.userId = userId;
        this.shopName=shopName;
        this.quantity = quantity;
        this.shopId = shopId;
        this.reminder = reminder;
        this.category = category;
        this.notes = notes;
    }


    //getters setters


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int  getShopId() {
        return shopId;
    }

    public long getReminder() {
        return reminder;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String title) {
        this.name = title;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setReminder(long reminder) {
        this.reminder = reminder;
    }
}
