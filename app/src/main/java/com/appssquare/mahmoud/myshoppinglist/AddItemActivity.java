package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {
    TextView toolBarTitle;
    public Toolbar toolbar;
    Preferences pref;
    String userId;
    DatabaseHelper db;
    EditText editShopName,editItemName,editItemQuantity,editItemCategory,editItemRemindingDate,editItemNotes;
    Button btnAdd,btnCancel;
    AlertDialog.Builder dialogBuilder;
    CheckBox chkremind;
    boolean isReminding;
    long remindAt;
    int shopId;
    String remindAtHuman="";
    DatePicker datePicker;
    TimePicker timePicker;
    Calendar calendar;
    boolean isEditItem=false;
    Item edittedItem;
    String itemShopName;
    RemindersManager remindersManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         db=new DatabaseHelper(this);
        if(getIntent().hasExtra("ITEM_ID")){
            isEditItem=true;
            edittedItem=db.getItem(Integer.valueOf(getIntent().getIntExtra("ITEM_ID",0)));
            itemShopName=getIntent().getStringExtra("ITEM_SHOP_NAME");
          //  Log.e("post edit quantity: ",String.valueOf(edittedItem.getQuantity()) );

        }

        setContentView(R.layout.activity_add_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolBarTitle=(TextView)findViewById(R.id.toolbartext);
       // toolBarTitle.setText("Add Item");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        getSupportActionBar().setTitle(R.string.add_item);
        pref=Preferences.getInstance(this);
        userId=pref.getKey("userId");

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        editShopName=findViewById(R.id.editTextShopName);
        if(isEditItem){
            editShopName.setText(itemShopName);
            editShopName.setEnabled(false);
        }
        editShopName.setFocusable(false);
        editShopName.setClickable(true);
        editShopName.setOnClickListener(this);

        editItemName=findViewById(R.id.editTextitemname);
        if(isEditItem) editItemName.setText(edittedItem.getName());
        editItemQuantity=findViewById(R.id.editTextquantity);
        if(isEditItem) editItemQuantity.setText(String.valueOf(edittedItem.getQuantity()));
       // editItemQuantity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000000)});

        editItemCategory=findViewById(R.id.editTextcategory);
        if(isEditItem) editItemCategory.setText(edittedItem.getCategory());
        editItemCategory.setFocusable(false);
        editItemCategory.setClickable(true);
        editItemCategory.setOnClickListener(this);

        editItemRemindingDate=findViewById(R.id.editTextdatetime);

        if(isEditItem) {
            remindAt=edittedItem.getReminder();
            Date date = new Date(edittedItem.getReminder());
            SimpleDateFormat dateformat = new SimpleDateFormat("MM dd, yyyy HH:mm");
            remindAtHuman=dateformat.format(date);
            editItemRemindingDate.setText(remindAtHuman);
        }
        editItemRemindingDate.setFocusable(false);
        editItemRemindingDate.setClickable(true);
        editItemRemindingDate.setEnabled(false);
        editItemRemindingDate.setOnClickListener(this);

        editItemNotes=findViewById(R.id.editTextnotes);
        if(isEditItem) editItemNotes.setText(edittedItem.getNotes());

        chkremind = (CheckBox) findViewById(R.id.checkBoxIsRemind);
        chkremind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isReminding=isChecked;
                if(isReminding)chkremind.setTextColor(Color.BLACK);else chkremind.setTextColor(Color.GRAY);

                editItemRemindingDate.setEnabled(isChecked);

            }
        });

        btnAdd=findViewById(R.id.btnAdditem);
        if(isEditItem) btnAdd.setText(R.string.save);
        btnAdd.setTransformationMethod(null);
        btnCancel=findViewById(R.id.btnCancelitem);
        btnCancel.setTransformationMethod(null);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        db=new DatabaseHelper(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editTextShopName:
                openShopsDialog();

                break;
            case R.id.editTextcategory:
                openCategoryDialog();

                break;

            case R.id.editTextdatetime:
                openReminderDialog();

                break;

            case R.id.btnAdditem:
                if(editShopName.getText().toString().trim().length()<1){
                    editShopName.setError(getString(R.string.shop_name_error));
                return;
            }

                if(editItemName.getText().toString().trim().length()<1){
                    editItemName.setError(getString(R.string.item_name_error));
                    return;
                }
                if(editItemQuantity.getText().toString().trim().length()<1){
                    editItemQuantity.setError(getString(R.string.item_quantity_error));
                    return;
                }
                Item item=new Item();
                item.setUserId(userId);
                item.setName(editItemName.getText().toString().trim());
                item.setQuantity(Integer.parseInt(editItemQuantity.getText().toString()));
               // Log.e("quantity",String.valueOf(Integer.valueOf(editItemQuantity.getText().toString())));
                item.setNotes(editItemNotes.getText().toString().trim());
                item.setShopName(editShopName.getText().toString().trim());
                if(chkremind.isChecked())
                item.setReminder(remindAt);
                else
                    item.setReminder(0);
                item.setCategory(editItemCategory.getText().toString().trim());
                item.setShopId(shopId);
                if(isEditItem){
                    item.setReminder(remindAt);
                    item.setId(edittedItem.getId());
                    db.updateItem(item);
                }
                    else {
                    db.addItem(item);
                }

                Log.e("item  "+item.getName() , "saved at >> "+String.valueOf(item.getReminder()));
                 remindersManager=new RemindersManager(this);
                remindersManager.reschedule();
               // Log.e("editted iteeeeeeeeem: ",String.valueOf(item.getId()) );
                //Toast.makeText(this,"item added succefully",Toast.LENGTH_LONG).show();
                AlertDialog alertDialog = new AlertDialog.Builder(AddItemActivity.this).create();
                String msg=isEditItem? getString(R.string.item_saved_success) : getString(R.string.item_add_success) ;
                alertDialog.setMessage(msg);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_btn),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(AddItemActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                alertDialog.show();
               // Log.e("item : ",item.getName() + "  " + item.getShopName() );


                break;

            case  R.id.btnCancelitem:
                startActivity(new Intent(AddItemActivity.this, MainActivity.class));

                finish();
                break;
        }

    }

    private void openReminderDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
         View dialogView = inflater.inflate(R.layout.date_time_dialog, null);
        dialogBuilder.setView(dialogView);
          datePicker= (DatePicker) dialogView.findViewById(R.id.datePicker);
          timePicker=(TimePicker)dialogView.findViewById(R.id.timePicker);
          calendar=Calendar.getInstance();


        dialogBuilder.setTitle(R.string.reminder_dialog);

        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute(),0);
                remindAt=calendar.getTimeInMillis();
                Date date = new Date(remindAt);
                SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                remindAtHuman=dateformat.format(date);
                editItemRemindingDate.setText(remindAtHuman);
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();


    }

    private void openShopsDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.which_shop);
       // Log.e("userid 1  : ",userId );
         final List<Shop> allShops= db.getUserShops(userId);
       // Log.e("userid 2   : ",userId );
        final String[] shopNmaes=new String[allShops.size()];
        for (int i = 0; i <allShops.size() ; i++) {
            shopNmaes[i]=allShops.get(i).getName();

            
        }

        dialogBuilder.setItems(shopNmaes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editShopName.setText(shopNmaes[i]);
                shopId=allShops.get(i).getId();

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void openCategoryDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.which_category);
        final String[] items={getString(R.string.cat1),getString(R.string.cat2),getString(R.string.cat3)};  // will be get later from resources
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    editItemCategory.setText(items[i]);
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddItemActivity.this,MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e("mainactivity attatch", "attachBaseContext");
    }
}
