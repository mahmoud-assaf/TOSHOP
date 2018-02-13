package com.appssquare.mahmoud.myshoppinglist;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    Preferences preferences;
      //  TextView testview;
        DatabaseHelper db;
        List<Item> itemsList;
        ExpandableListAdapter listAdapter;
        List<ShopGroup> UserAllGroups;
    ShopGroup shList;
    List<Item> shopItems;
    private ExpandableListView expandableListView;
    public int previousHeader=-1;
    Button itemgot,itemnotfound;AlertDialog b;
    RemindersManager remindersManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.e("on create","on create");
        db=new DatabaseHelper(getActivity());
        preferences=Preferences.getInstance(getActivity());
       // prepareData();

    }

    private void prepareData() {
        itemsList=db.getUserItems(preferences.getKey("userId"));

        if(itemsList.size()>0) {
            UserAllGroups = new ArrayList<ShopGroup>();

            shList = new ShopGroup();

            shopItems=new ArrayList<Item>();    //list of items of single shop
            shList.setShopName(itemsList.get(0).getShopName());  //first group initialized
            shopItems.add(itemsList.get(0));
            int previousIndex=0;

            if(itemsList.size()==1){
                shList.setItems(shopItems);
                UserAllGroups.add(shList);
            }else {

                for (int j = 1; j < itemsList.size(); j++) {


                    if (itemsList.get(j).getShopId() == itemsList.get(previousIndex).getShopId()) {
                        shopItems.add(itemsList.get(j));
                        previousIndex++;
                        if (j == itemsList.size() - 1) {   //may be last item  equal current one
                            shList.setItems(shopItems);
                            UserAllGroups.add(shList);
                        }
                    } else {
                        shList.setItems(shopItems);
                        UserAllGroups.add(shList);    //add group and create new group
                        shList = new ShopGroup();
                        shopItems = new ArrayList<Item>();    //list of items of single shop
                        shList.setShopName(itemsList.get(j).getShopName());  //first group initialized
                        shopItems.add(itemsList.get(j));
                        previousIndex = j;
                        if (j == itemsList.size() - 1) { //may be last item not equal current one
                            shList.setItems(shopItems);
                            UserAllGroups.add(shList);
                        }

                    }

                }
            }
            listAdapter = new ExpandableListAdapter(getActivity(), UserAllGroups);

        }
    }

    public ShoppingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //HashMap<String,List<Item>> listOfItem;
      //  Log.e("on viewcreated","on viewcreated");
        prepareData();
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandlistview);
       // expandableListView.setAdapter(listAdapter);
        expandableListView.setOnChildClickListener(myListItemClicked);
        //listener for group heading click
        //expandableListView.setOnGroupClickListener(myListGroupClicked);


        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    // You now have everything that you would as if this was an OnChildClickListener()
                    // Add your logic here.
                   // Log.e("long clcik item :" ,UserAllGroups.get(groupPosition).getItems().get(childPosition).getName());
                    openItemDialog(UserAllGroups.get(groupPosition).getItems().get(childPosition),groupPosition,childPosition);
                    // Return true as we are handling the event.
                    return true;
                }

                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {



            public void onGroupExpand(int groupPosition) {

                int len = listAdapter.getGroupCount();

                for (int i = 0; i < len; i++) {
                    if (i != groupPosition) {

                        expandableListView.collapseGroup(i);

                    }

                }

            }

        });
        if (listAdapter!=null) {
            listAdapter.notifyDataSetChanged();
          //  Log.e( "count view created:",String.valueOf(listAdapter.getGroupCount()));

        }
    }


    private void openItemViewDialog(final Item item,final String shopname) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.item_details, null);
        dialogBuilder.setView(dialogView);

        TextView item_name=(TextView)dialogView.findViewById(R.id.itemnamedialog);
        item_name.setText(item.getName());

        TextView shop_name=(TextView)dialogView.findViewById(R.id.item_shop_name);
        shop_name.setText(item.getShopName());

        TextView quantity=(TextView)dialogView.findViewById(R.id.item_quantity);
        quantity.setText(String.valueOf(item.getQuantity()));

        TextView remindat=(TextView)dialogView.findViewById(R.id.reminded_at);
        String remindAtHuman;
        if(item.getReminder()==0){ remindAtHuman=getString(R.string.not_reminding);}
        else {
            Date date = new Date(item.getReminder());
            SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            remindAtHuman = dateformat.format(date);
        }
        remindat.setText(remindAtHuman);

        TextView notes=(TextView)dialogView.findViewById(R.id.item_notes);
        notes.setText(item.getNotes());



        Button itemok = (Button) dialogView.findViewById(R.id.item_ok_btn);

       ImageButton editItem = (ImageButton) dialogView.findViewById(R.id.imageButtonedit_item);

        itemok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                Intent i = new Intent(getActivity(), AddItemActivity.class);
              //  Log.e("on edit quantity: ",String.valueOf(item.getQuantity()) );
                i.putExtra("ITEM_ID",item.getId());
                i.putExtra("ITEM_SHOP_NAME",shopname);
                startActivity(i);
                getActivity().finish();

            }
        });

        b = dialogBuilder.create();
        b.show();

    }


    private void openItemDialog(final Item item, final int groupPosition, final int childPosition) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.item_menu, null);

        dialogBuilder.setView(dialogView);
        //dialogBuilder.setTitle("Item ...");
         itemgot = (Button) dialogView.findViewById(R.id.btn_itemgot);
        itemnotfound = (Button) dialogView.findViewById(R.id.btn_notfound);
        itemgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAllGroups.get(groupPosition).getItems().remove(childPosition);
              //  Log.e("alrt dialog :" ,item.getName());
                b.cancel();
                db.deleteItem(item);
                remindersManager=new RemindersManager(getActivity());
                remindersManager.reschedule();
               // Toast.makeText(getActivity().getBaseContext(),"Item got ,will be deleted from your shop",Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                alertDialog.setMessage("Item deleted from your shop");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_btnn),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();
                listAdapter.notifyDataSetChanged();

            }
        });
        itemnotfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAllGroups.get(groupPosition).getItems().remove(childPosition);
               // Log.e("dialog :" ,item.getName());
                b.cancel();
                db.deleteItem(item);
                remindersManager=new RemindersManager(getActivity());
                remindersManager.reschedule();
               // Toast.makeText(getActivity().getBaseContext(),"Item deleted from your shop",Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                alertDialog.setMessage("Item deleted from your shop");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_btnn),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();
                listAdapter.notifyDataSetChanged();

            }
        });

        b = dialogBuilder.create();
        b.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        prepareData();

        expandableListView.setAdapter(listAdapter);
        if (listAdapter!=null) {
            listAdapter.notifyDataSetChanged();
          //  Log.e( "groups count  :",String.valueOf(listAdapter.getGroupCount()));

        }
       // Log.e("on resume","on resume");

    }

    //our child listener
    private ExpandableListView.OnChildClickListener myListItemClicked =  new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {


            //Toast.makeText(getActivity(), "Clicked on Item " + String.valueOf(UserAllGroups.get(groupPosition).getItems().get(childPosition).getId()), Toast.LENGTH_SHORT).show();

            /*Intent intent = new Intent(getActivity(), AddItemActivity.class);
            intent.putExtra("ITEM_ID", UserAllGroups.get(groupPosition).getItems().get(childPosition).getId());
            intent.putExtra("ITEM_SHOP_NAME", UserAllGroups.get(groupPosition).getShopName());
            startActivity(intent);*/

         Log.e("id item",String.valueOf(UserAllGroups.get(groupPosition).getItems().get(childPosition).getId()));
            openItemViewDialog(UserAllGroups.get(groupPosition).getItems().get(childPosition),UserAllGroups.get(groupPosition).getShopName());

            return false;

        }
    };

    //our group listener
    private ExpandableListView.OnGroupClickListener myListGroupClicked =  new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
           // Log.e( "group number  :",String.valueOf(groupPosition)+" previous= "+String.valueOf(previousHeader));
            if(previousHeader!=-1 && groupPosition !=-1 && groupPosition!= previousHeader) {
                expandableListView.collapseGroup(previousHeader);
            }

                previousHeader=groupPosition;
            //get the group header
            //HeaderInfo headerInfo = SectionList.get(groupPosition);
            //display it or do something with it
            //Toast.makeText(getActivity(), "Clicked on Header " + UserAllItems.get(groupPosition).getShopName(), Toast.LENGTH_SHORT).show();

            return false;
        }
    };

}
