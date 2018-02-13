package com.appssquare.mahmoud.myshoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class AddShopActivity extends AppCompatActivity implements View.OnClickListener{
    TextView toolBarTitle;
    public Toolbar toolbar;
    EditText shopNameEdit,shopLocEdit;
    Button addshopBtn,cancelBtn,getMapLocBtn;
    Preferences pref;
    String userId;
    boolean isLocSetOnMap=false;
    LatLng shopLocation;
    int locOnMapRequest=22;
    boolean isEditShop=false;
    String shopName,shopAddress;
    int shopId;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("SHOP_ID")){
            isEditShop=true;
            shopId=(Integer.valueOf(getIntent().getIntExtra("SHOP_ID",0)));
            shopName=getIntent().getStringExtra("SHOP_NAME");
            shopAddress=getIntent().getStringExtra("SHOP_ADDRESS");
           // Log.e("editted  shop: ",shopName);

        }


        setContentView(R.layout.activity_add_shop);
       // View toolbarinc=findViewById(R.id.include);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolBarTitle=(TextView)findViewById(R.id.toolbartext);
       // toolBarTitle.setText("Add Shop");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        getSupportActionBar().setTitle(R.string.add_shop);
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        shopNameEdit=(EditText)findViewById(R.id.editShopName);
        if(isEditShop){
            shopNameEdit.setText(shopName);

        }
        shopLocEdit=(EditText)findViewById(R.id.editShopLocation);
        if(isEditShop){
            shopLocEdit.setText(shopAddress);

        }
        getMapLocBtn=(Button)findViewById(R.id.getloconmap);
        getMapLocBtn.setTransformationMethod(null);

        addshopBtn=(Button)findViewById(R.id.btnAdd);
        addshopBtn.setTransformationMethod(null);
        if(isEditShop) addshopBtn.setText("Save");

        cancelBtn=(Button)findViewById(R.id.btnCancel);
        cancelBtn.setTransformationMethod(null);

        getMapLocBtn.setOnClickListener(this);
        addshopBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        pref=Preferences.getInstance(this);
        userId=pref.getKey("userId");

         db=new DatabaseHelper(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getloconmap:

                Intent intent=new Intent(AddShopActivity.this,MapActivity.class);
                startActivityForResult(intent, locOnMapRequest);// Activity is started with requestCode 2

                break;
            case R.id.btnAdd:

                String shopName=shopNameEdit.getText().toString().trim();
                if (shopName.length()<1){
                    shopNameEdit.setError(getString(R.string.shop_nameerror));
                    return;
                }
                String shopAddress=shopLocEdit.getText().toString().trim();
                if (shopAddress.length()<1){
                    shopLocEdit.setError(getString(R.string.shop_address_error));
                    return;
                }
                //all done add shop
                if(isLocSetOnMap) {
                    if(isEditShop) {
                        db.updateShop(new Shop(shopId, userId, shopName, shopAddress, shopLocation));
                    }else{
                        db.addShop(new Shop(0, userId, shopName, shopAddress, shopLocation)); //address will be get from map latlng

                    }

                }else {
                    if(isEditShop) {
                        db.updateShop(new Shop(shopId, userId, shopName, shopAddress));
                    }else{
                        db.addShop(new Shop(0, userId, shopName, shopAddress)); //address will be get from map latlng

                    }
                }

               // Log.e( "SHOP ",shopName+"/"+ shopAddress + "/"+ userId );
               // Toast.makeText(this,"Shop Created Successfully",Toast.LENGTH_LONG).show();
                AlertDialog alertDialog = new AlertDialog.Builder(AddShopActivity.this).create();
                String msg=isEditShop? "shop saved succefully" : "shop added succefully" ;
                alertDialog.setMessage(msg);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok_btnn),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(AddShopActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                alertDialog.show();


                break;
            case R.id.btnCancel:
                startActivity(new Intent(AddShopActivity.this, MainActivity.class));

                finish();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode==RESULT_OK)
        {

            Double Lat=data.getDoubleExtra("LOCATION_LAT",0.00);
            Double Lng=data.getDoubleExtra("LOCATION_LNG",0.00);
            String Loc=data.getStringExtra("ADDRESS");
            shopLocEdit.setText(Loc);
            shopLocation=new LatLng(Lat,Lng);

            isLocSetOnMap=true;

        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddShopActivity.this,MainActivity.class));
        finish();
        super.onBackPressed();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e("mainactivity attatch", "attachBaseContext");
    }
}
