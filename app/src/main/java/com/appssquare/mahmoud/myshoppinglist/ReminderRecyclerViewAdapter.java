package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mahmoud on 10/02/2018.
 */

public class ReminderRecyclerViewAdapter extends RecyclerView.Adapter<ReminderRecyclerViewAdapter.ViewHolder> {
    List<Item> remindersList;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;

    public ReminderRecyclerViewAdapter(Context context1,List<Item> remindersLst){

        remindersList = remindersLst;
        context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvItemName,tvItemQuantity,tvIitemShopName,tvItemReminderDate;

        public ViewHolder(View v){

            super(v);

            tvItemName = (TextView)v.findViewById(R.id.reminder_item_name);
            tvItemQuantity = (TextView)v.findViewById(R.id.reminder_item_quantity);
            tvIitemShopName = (TextView)v.findViewById(R.id.reminder_shop_name);
            tvItemReminderDate = (TextView)v.findViewById(R.id.reminder_item_date);
        }
    }

    @Override
    public ReminderRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.reminder_row,parent,false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        holder.tvItemName.setText(remindersList.get(position).getName());
        holder.tvItemQuantity.setText(String.valueOf(remindersList.get(position).getQuantity()));
        holder.tvIitemShopName.setText(remindersList.get(position).getShopName());
        Date date = new Date(remindersList.get(position).getReminder());
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
       String remindAtHuman = dateformat.format(date);
        holder.tvItemReminderDate.setText(remindAtHuman);
    }

    @Override
    public int getItemCount(){

        return remindersList.size();
    }

}
