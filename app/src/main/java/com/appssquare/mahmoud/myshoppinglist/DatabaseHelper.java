package com.appssquare.mahmoud.myshoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahmoud on 28/01/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "myshoppinglist";

    // Contacts table name
    private static final String TABLE_SHOPS = "shops";

    // Shops Table Columns names
    private static final String KEY_ID = "shop_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER = "user_id";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_REMINDING = "reminding";



    private static final String TABLE_ITEMS = "items";
    // Items Table Columns names
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_ITEM_NAME = "itemname";
    private static final String KEY_ITEM_USER = "itemuser_id";
    private static final String KEY_ITEM_SHOP_ID = "shop_id";
    private static final String KEY_ITEM_QUANTITY = "quantity";
    private static final String KEY_ITEM_REMIND_DATE = "remind_date";
    private static final String KEY_ITEM_CATEGORY = "item_category";
    private static final String KEY_ITEM_NOTES = "item_nots";






    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHOPS_TABLE = "CREATE TABLE " + TABLE_SHOPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                +KEY_USER + " TEXT,"
                +KEY_REMINDING+" INTEGER,"
                +KEY_LOCATION + " TEXT"
                + ")";
        db.execSQL(CREATE_SHOPS_TABLE);

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ITEM_NAME + " TEXT,"
                + KEY_ITEM_SHOP_ID + " INTEGER,"
                +KEY_ITEM_USER + " TEXT,"
                +KEY_ITEM_QUANTITY+" INTEGER,"
                +KEY_ITEM_CATEGORY + " TEXT,"
                +KEY_ITEM_NOTES +" TEXT,"
                +KEY_ITEM_REMIND_DATE +" INTEGER"
                + ")";
        db.execSQL(CREATE_ITEMS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        // Create tables again
        onCreate(db);
    }





    //get operations



    //items of user
    public List<Item> getUserItems(String userid){
       // Log.e("USER ID: ",userid );
        List<Item> resultList=new ArrayList<Item>();
        SQLiteDatabase db = this.getReadableDatabase();
       /*Cursor cursor = db.query(TABLE_ITEMS, null, KEY_ITEM_USER + "=?",
                new String[] { String.valueOf(userid) }, null, null, KEY_ITEM_SHOP_ID, null);*/
        String rawQuery = "SELECT * FROM " + TABLE_ITEMS + " INNER JOIN " + TABLE_SHOPS
                + " ON " + TABLE_SHOPS +"."+KEY_ID + " = " + TABLE_ITEMS+"."+ KEY_ITEM_SHOP_ID
                + " WHERE " + TABLE_ITEMS +"."+KEY_ITEM_USER + " = '" +  userid +"'"+" ORDER BY "+TABLE_ITEMS+"."+KEY_ITEM_SHOP_ID;
        Cursor cursor=db.rawQuery(rawQuery,null);
       //Log.e("sqlite",String.valueOf(cursor.getCount()));
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                Item item = new Item();
                    item.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_ID))));
                   // Log.e("id =" ,cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_ID)));
                    item.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NAME)));
                    // Log.e("item name: ", cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NAME)));
                    item.setShopName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
                    item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_CATEGORY)));
                    item.setShopId(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_SHOP_ID))));
                    item.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NOTES)));
                    item.setReminder(Long.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_REMIND_DATE))));
                    item.setQuantity(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_QUANTITY))));
                // Adding item to list
                resultList.add(item);
            } while (cursor.moveToNext());

        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return resultList;
    }

    public List<Item> getUserRemindingItems(String userid){
        // Log.e("USER ID: ",userid );
        List<Item> resultList=new ArrayList<Item>();
        SQLiteDatabase db = this.getReadableDatabase();
       /*Cursor cursor = db.query(TABLE_ITEMS, null, KEY_ITEM_USER + "=?",
                new String[] { String.valueOf(userid) }, null, null, KEY_ITEM_SHOP_ID, null);*/
        String rawQuery = "SELECT * FROM " + TABLE_ITEMS + " INNER JOIN " + TABLE_SHOPS
                + " ON " + TABLE_SHOPS +"."+KEY_ID + " = " + TABLE_ITEMS+"."+ KEY_ITEM_SHOP_ID
                + " WHERE " + TABLE_ITEMS +"."+KEY_ITEM_USER + " = '" +  userid +"' AND "+TABLE_ITEMS +"."+KEY_ITEM_REMIND_DATE +">0 "+" ORDER BY "+TABLE_ITEMS+"."+KEY_ITEM_REMIND_DATE;



        Cursor cursor=db.rawQuery(rawQuery,null);
        //Log.e("sqlite",String.valueOf(cursor.getCount()));
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                Item item = new Item();
                item.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_ID))));
               // Log.e("id =" ,cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_ID)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NAME)));
                // Log.e("item name: ", cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NAME)));
                item.setShopName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
                item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_CATEGORY)));
                item.setShopId(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_SHOP_ID))));
                item.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NOTES)));
                item.setReminder(Long.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_REMIND_DATE))));
                item.setQuantity(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_QUANTITY))));
                // Adding item to list
                resultList.add(item);
            } while (cursor.moveToNext());

        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return resultList;
    }

//get user shops

    public List<Shop> getUserShops(String userid){
        List<Shop> resultList=new ArrayList<Shop>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPS, null, KEY_USER + "=?",
                new String[] { String.valueOf(userid) }, null, null, KEY_ID, null);
     //   Log.e("sqlite shops cunt",String.valueOf(cursor.getCount()));
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                Shop shop = new Shop();
                shop.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID))));
                shop.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
                shop.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS)));
                shop.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER)));


                //shop.setReminding(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REMINDING))));


                    String locationstr=cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION));
                    if(locationstr!=null) {


                        String[] cords = locationstr.split(":");
                       // Log.e("string", cords[0] + "   " + cords[1]);
                        Double l1 = Double.parseDouble(cords[0]);
                        Double l2 = Double.parseDouble(cords[1]);

                        LatLng location = new LatLng(l1, l2);
                        shop.setLocation(location);
                    }
                // Adding item to list
                resultList.add(shop);
            } while (cursor.moveToNext());

        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return resultList;
    }


        //add item
        public void addItem(Item item) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            //values.put(KEY_ITEM_ID,"null");
            values.put(KEY_ITEM_NAME, item.getName()); // item Name
            values.put(KEY_ITEM_QUANTITY, item.getQuantity());
            values.put(KEY_ITEM_USER, item.getUserId());
            values.put(KEY_ITEM_REMIND_DATE, item.getReminder());
            values.put(KEY_ITEM_SHOP_ID, item.getShopId());

            values.put(KEY_ITEM_CATEGORY, item.getCategory());
            values.put(KEY_ITEM_NOTES, item.getNotes());

            // Inserting Row
            db.insert(TABLE_ITEMS, null, values);
            db.close(); // Closing database connection
        }

        public  Item  getItem(int id){
            SQLiteDatabase db = this.getReadableDatabase();
            String rawQuery = "SELECT * FROM " + TABLE_ITEMS +" WHERE "+KEY_ITEM_ID + "="+String.valueOf(id);
           // Log.e("query",rawQuery );
            //Cursor cursor = db.query(TABLE_ITEMS, null, KEY_ITEM_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
            Cursor cursor = db.rawQuery(rawQuery, null);

           // Log.e("sqlite items count",String.valueOf(cursor.getCount()));
            if (cursor != null && cursor.getCount()>0) {
                cursor.moveToFirst();
                Item item=new Item();
                item.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_ID))));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NAME)));
                item.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_USER)));
                item.setShopId(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_SHOP_ID))));
                item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_CATEGORY)));
                item.setQuantity(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_QUANTITY))));
                item.setReminder(Long.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_REMIND_DATE))));
                item.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ITEM_NOTES)));
                return item;
            }else{
                return  null;
            }
        }

    //delete item
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ITEM_NAME + " = ?",
                new String[] { item.getName() });
        db.close();
       // Log.e( "deleteItem : ",item.getName() );

    }

    public void deleteShopItems(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ITEM_SHOP_ID + " = ?",
                new String[] { String.valueOf(shop.getId()) });
        db.close();
       // Log.e( "deleteItem : ",shop.getName() );

    }

    //add shop
    public void addShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, shop.getName()); // item Name
        values.put(KEY_USER, shop.getUserId());
      //  values.put(KEY_LOCATION,shop.getLocation().toString());
        if(shop.getLocation()!=null){
        String loc=String.valueOf(shop.getLocation().latitude)+":"+String.valueOf(shop.getLocation().longitude);
        values.put(KEY_LOCATION,loc);
        }
        values.put(KEY_ADDRESS,shop.getAddress());
        //values.put(KEY_REMINDING,shop.getReminding());
        // Inserting Row
        db.insert(TABLE_SHOPS, null, values);
        db.close(); // Closing database connection
    }


    //delete shop
    public void deleteShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPS, KEY_ID + " = ?",
                new String[] { String.valueOf(shop.getId()) });
        db.close();
    }
//update shop

    // Updating single shop
    public int updateShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shop.getName());
        values.put(KEY_USER, shop.getUserId());
        if(shop.getLocation()!=null){
            String loc=String.valueOf(shop.getLocation().latitude)+":"+String.valueOf(shop.getLocation().longitude);
            values.put(KEY_LOCATION,loc);
        }
        values.put(KEY_ADDRESS,shop.getAddress());
        //values.put(KEY_REMINDING,shop.getReminding());


        // updating shop
        return db.update(TABLE_SHOPS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(shop.getId()) });
    }

    //add item
    public void updateItem(Item item) {
       // Log.e( "UPDATE Item name: ",String.valueOf(item.getId() ));
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.getName()); // item Name
        values.put(KEY_ITEM_QUANTITY, String.valueOf(item.getQuantity()));
        //values.put(KEY_ITEM_USER, item.getUserId());
        values.put(KEY_ITEM_REMIND_DATE,  String.valueOf(item.getReminder()));
        values.put(KEY_ITEM_SHOP_ID,  String.valueOf(item.getShopId()));

        values.put(KEY_ITEM_CATEGORY, item.getCategory());
        values.put(KEY_ITEM_NOTES, item.getNotes());

        // update Row
        String _id=String.valueOf(item.getId());
    //  int y= db.update(TABLE_ITEMS,  values,KEY_ITEM_ID+" = ?", (new String[] { _id }));
        //Log.e( "string array: ", Arrays.toString(new String[] { _id })+" --" + _id);
       String q="update "+TABLE_ITEMS+" set "+

               KEY_ITEM_REMIND_DATE+"="+ String.valueOf(item.getReminder())+","+
               KEY_ITEM_NOTES+"='"+ item.getNotes()+ "',"+
               KEY_ITEM_NAME+"='"+item.getName()+"',"+
               KEY_ITEM_CATEGORY+"='"+item.getCategory()+"',"+
               KEY_ITEM_QUANTITY+"="+String.valueOf(item.getQuantity()) +
               " WHERE "+KEY_ITEM_ID+"="+String.valueOf(item.getId() ) +";";
      //  Log.e("QUERY",q );
       db.execSQL(q);
       // Log.e("updateItem: ",String.valueOf(y) );
        db.close(); // Closing database connection
    }

}
