package com.appssquare.mahmoud.myshoppinglist;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mahmoud on 27/01/2018.
 */

public class Shop {

    int id;
    String userId;
    String name;
    String address;
    LatLng location=null;

    public  Shop(){

    }

    public Shop(int id, String userId, String name, String address) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;


    }


    //constructor with location
    public Shop(int id, String userId, String name, String address, LatLng location) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.location = location;

    }
//setters getters


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocation(LatLng location) {
        this.location = location;
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

    public String getAddress() {
        return address;
    }

    public LatLng getLocation() {
        return location;
    }


}
