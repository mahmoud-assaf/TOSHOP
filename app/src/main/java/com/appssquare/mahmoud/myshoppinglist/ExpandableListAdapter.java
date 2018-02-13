package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mahmoud on 01/02/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    List<ShopGroup> shopGroup;


    public ExpandableListAdapter(Context context, List<ShopGroup> shopGroup) {
        this.context = context;
        this.shopGroup = shopGroup;

    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
                return  this.shopGroup.get(listPosition).getItems().get(expandedListPosition);

       // return /*this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
               // .get(expandedListPosition);*/
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Item item=(Item)getChild(listPosition,expandedListPosition);
       // final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
       /* TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);*/
        TextView itemNameCategory=(TextView) convertView.findViewById(R.id.list_item_name_category);
        TextView itemRemindAt=(TextView) convertView.findViewById(R.id.list_item_remindat);
        TextView itemQuantity=(TextView) convertView.findViewById(R.id.list_item_quantity);
        String remindAtHuman;
        if(item.getReminder()==0) remindAtHuman=context.getString(R.string.notreminding);
        else {
            Date date = new Date(item.getReminder());
            SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            remindAtHuman = dateformat.format(date);
        }
            itemNameCategory.setText(item.getName() + "||" + item.getCategory());
            itemRemindAt.setText(remindAtHuman);

            itemQuantity.setText(String.valueOf(item.getQuantity()));
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
       /* return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();*/
       return  this.shopGroup.get(listPosition).getItems().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        //return this.expandableListTitle.get(listPosition);
        return  this.shopGroup.get(listPosition);
    }

    @Override
    public int getGroupCount() {
      //  return this.expandableListTitle.size();
        return this.shopGroup.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ShopGroup shopGroup = (ShopGroup) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_header, null);
        }
       /* TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);*/
        TextView heading = (TextView) convertView.findViewById(R.id.list_header_title);
        heading.setText(shopGroup.getShopName());
        ImageView indicator=(ImageView)convertView.findViewById(R.id.headerindicatorImageView);
        if (isExpanded) {
            indicator.setImageResource(R.drawable.arrowup);
        } else {
            indicator.setImageResource(R.drawable.arrowdown);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }




}
