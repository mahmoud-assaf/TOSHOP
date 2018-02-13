package com.appssquare.mahmoud.myshoppinglist;

import android.app.Activity;

/**
 * Created by mahmoud on 23/01/2018.
 */

public class MenuItem {
    int id;
    String title;
    int iconId;
    Class<? extends Activity> intentActivity;

    public MenuItem(int id,String title,int icon,final Class<? extends Activity> action){
        this.id=id;
        this.title=title;
        this.iconId=icon;
        this.intentActivity=action;
    }

}
