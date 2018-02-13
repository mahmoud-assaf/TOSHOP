package com.appssquare.mahmoud.myshoppinglist;

import java.util.List;

/**
 * Created by mahmoud on 01/02/2018.
 */

public class ShopGroup {
    String shopName;
   public List<Item> items;

    public String getShopName() {
        return shopName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
