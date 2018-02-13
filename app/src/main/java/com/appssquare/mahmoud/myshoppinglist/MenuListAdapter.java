package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mahmoud on 23/01/2018.
 */

public class MenuListAdapter extends BaseAdapter {

    MenuItem[] menuList;

    Context context;

    private static LayoutInflater inflater=null;
    public MenuListAdapter(Context context, MenuItem[] menuItems) {
        // TODO Auto-generated constructor stub
        menuList=menuItems;
        context=context;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menuList.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.nav_menu_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.menu_text);
        holder.img=(ImageView) rowView.findViewById(R.id.menu_icon);
        holder.tv.setText(menuList[position].title);
        holder.img.setImageResource(menuList[position].iconId);

        return rowView;
    }



}
