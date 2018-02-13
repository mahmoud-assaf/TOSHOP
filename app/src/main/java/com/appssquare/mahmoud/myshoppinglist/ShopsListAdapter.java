package com.appssquare.mahmoud.myshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by mahmoud on 23/01/2018.
 */

public class ShopsListAdapter extends BaseAdapter {

    List<Shop> shopsLlist;

    Context context;
    DatabaseHelper db;
    private static LayoutInflater inflater = null;

    public ShopsListAdapter(Context context, List<Shop> shopsLst) {
        // TODO Auto-generated constructor stub
        shopsLlist = shopsLst;
        this.context = context;
        db=new DatabaseHelper(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return shopsLlist.size();
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

    /*public class Holder {
        TextView tvShopName, tvShopAddress, tvPopMenu;

    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
       ;
        final View rowView;
        rowView = inflater.inflate(R.layout.shop_row, null);
        final  TextView tvShopName = (TextView) rowView.findViewById(R.id.shop_row_name);
        final  TextView tvShopAddress = (TextView) rowView.findViewById(R.id.shop_row_address);
        final  TextView tvPopMenu = (TextView) rowView.findViewById(R.id.shop_row_menu);
        tvPopMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context.getApplicationContext(), tvPopMenu);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        int i = item.getItemId();
                        if (i == R.id.navigate) {
                            if(shopsLlist.get(position).getLocation()==null){
                                Toast.makeText(context,"you didnt set accurate location to this store",Toast.LENGTH_SHORT);
                            }else{
                                Intent in = new Intent(context, MapActivity.class);
                                Log.e("view shop: ",shopsLlist.get(position).getName() );
                                String loc=String.valueOf(shopsLlist.get(position).getLocation().latitude)+":"+String.valueOf(shopsLlist.get(position).getLocation().longitude);
                                in.putExtra("SHOP_LOCATION",loc);
                                in.putExtra("SHOP_NAME",shopsLlist.get(position).getName());
                                context.startActivity(in);
                            }
                            //do something
                            return true;
                        } else if (i == R.id.edit) {
                            Intent inn = new Intent(context, AddShopActivity.class);

                            inn.putExtra("SHOP_ID",shopsLlist.get(position).getId());
                            inn.putExtra("SHOP_NAME",shopsLlist.get(position).getName());
                            inn.putExtra("SHOP_ADDRESS",shopsLlist.get(position).getAddress());
                            context.startActivity(inn);

                            //do something
                            return true;
                        } else if (i == R.id.delete) {
                            //confirm
                             AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            builder
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Yes-code
                                            db.deleteShop(new Shop(shopsLlist.get(position).getId(),"","","",new LatLng(0,0)));
                                            db.deleteShopItems(new Shop(shopsLlist.get(position).getId(),"","","",new LatLng(0,0)));
                                            AlertDialog.Builder b = new AlertDialog.Builder(context);


                                            b.setMessage("Shop deleted");
                                            b.setPositiveButton( "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            ((Activity)context).finish();
                                                            context.startActivity(new Intent(context,MainActivity.class));
                                                        }
                                                    });
                                            b.show();


                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();




                            return true;
                        } else {
                            return onMenuItemClick(item);
                        }
                    }
                });

                popup.show();
            }


        });
        tvShopName.setText(shopsLlist.get(position).getName());
        tvShopAddress.setText(shopsLlist.get(position).getAddress());
        return rowView;


    }
}