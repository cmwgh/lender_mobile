package net.christopherwhite.lender.lender;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemsDBHandler extends SQLiteOpenHelper {

    //information of database
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "itemsDB.db";
    public static final String TABLE_NAME = "Item";
    public static final String COLUMN_ID = "ItemID";
    public static final String COLUMN_NAME = "ItemName";
    public static final String COLUMN_DESCRIPTION = "ItemDescription";
    public static final String COLUMN_IMAGE = "ItemImage";
    public static final String COLUMN_BORROWERNAME = "BorrowerName";
    public static final String COLUMN_BORROWEREMAIL = "BorrowerEmail";
    public static final String COLUMN_DATELENT = "DateLent";
    public static final String COLUMN_RETURNDATE = "ReturnDate";
    public static final String COLUMN_VERIFY = "Verify";
    public static final String COLUMN_ARCHIVE = "Archive";
    public static final String TAG = "DATABASE";


    //initialize the database
    public ItemsDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_BORROWERNAME + " TEXT, " +
                COLUMN_BORROWEREMAIL + " TEXT, " +
                COLUMN_DATELENT + " DATE, " +
                COLUMN_RETURNDATE + " DATE, " +
                COLUMN_VERIFY + " TEXT, " +
                COLUMN_ARCHIVE + " INTEGER DEFAULT 0 " +
                " ) ";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String loadHandler() {
        String result = "";
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            result += String.valueOf(result_0) + " " + result_1 +
                    System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Item> loadAllHandler() {
        JSONArray jsonResultSet     = new JSONArray();
        List<Item> items = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ARCHIVE + " = 0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Item item = new Item();

        if(cursor.moveToFirst()){
            do {
                JSONObject rowObject = new JSONObject();
                int id = Integer.parseInt(cursor.getString(0));
                try {
                    rowObject.put(cursor.getColumnName(1), cursor.getString(1));
                    rowObject.put(cursor.getColumnName(2), cursor.getString(2));
                    rowObject.put(cursor.getColumnName(3), cursor.getString(3));
                    rowObject.put(cursor.getColumnName(4), cursor.getString(4));
                    rowObject.put(cursor.getColumnName(5), cursor.getString(5));
                    rowObject.put(cursor.getColumnName(6), cursor.getString(6));
                    rowObject.put(cursor.getColumnName(7), cursor.getString(7));
                    rowObject.put(cursor.getColumnName(8), cursor.getString(8));
                    rowObject.put(cursor.getColumnName(9), cursor.getString(9));

                }
                catch (Exception e){
                    Log.d("TAG_NAME", e.getMessage()  );
                }
                jsonResultSet.put(rowObject);

                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String image = cursor.getString(3);
                String borrowerName = cursor.getString(4);
                String borrowerEmail = cursor.getString(5);
                Date dateLent = null;
                Date dateReturn = null;
                try {
                    dateLent = dateFormat.parse(cursor.getString(6));
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing DateLent datetime failed", e);
                }
                try {
                    dateReturn = dateFormat.parse(cursor.getString(7));
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing ReturnDate datetime failed", e);
                }
                String verify = cursor.getString(8);
                int archive = Integer.parseInt(cursor.getString(9));
                items.add(new Item(id, name, description, image, borrowerName, borrowerEmail, dateLent, dateReturn, verify, archive));
            }while (cursor.moveToNext());
        }        cursor.close();
        Log.d("sql list: ", String.valueOf(items.size()));
        Log.d("json list: ", jsonResultSet.toString() );
        db.close();
        return items;
    }

    public List<Item> loadAllArchivedHandler() {
        List<Item> items = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ARCHIVE + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Item item = new Item();
        if(cursor.moveToFirst()){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String image = cursor.getString(3);
                String borrowerName = cursor.getString(4);
                String borrowerEmail = cursor.getString(5);
                Date dateLent = null;
                Date dateReturn = null;
                try {
                    dateLent = dateFormat.parse(cursor.getString(6));
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing DateLent datetime failed", e);
                }
                try {
                    dateReturn = dateFormat.parse(cursor.getString(7));
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing ReturnDate datetime failed", e);
                }
                String verify = cursor.getString(8);
                int archive = Integer.parseInt(cursor.getString(9));
                items.add(new Item(id, name, description, image, borrowerName, borrowerEmail, dateLent, dateReturn, verify, archive));
            }while (cursor.moveToNext());
        }        cursor.close();
        db.close();
        return items;
    }

    public void addHandler(Item item) {
        ContentValues values = new ContentValues();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        // On an INSERT, if the ROWID or INTEGER PRIMARY KEY column is not explicitly given a value,
        // then it will be filled automatically with an unused integer
        // values.put(COLUMN_ID, item.getItemID());
        values.put(COLUMN_NAME, item.getItemName());
        values.put(COLUMN_DESCRIPTION, item.getItemDescription());
        if (item.getItemImage() != ""){
            values.put(COLUMN_IMAGE, item.getItemImage());
        } else {
            values.put(COLUMN_IMAGE, "none");
        }
        values.put(COLUMN_BORROWERNAME, item.getBorrowerName());
        values.put(COLUMN_BORROWEREMAIL, item.getBorrowerEmail());
        values.put(COLUMN_DATELENT, dateFormat.format(item.getDateLent()));
        values.put(COLUMN_RETURNDATE, dateFormat.format(item.getReturnDate()));
        if(item.getVerify()!= ""){
            values.put(COLUMN_VERIFY, item.getVerify());
        } else {
            values.put(COLUMN_VERIFY, "false");
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public Item findHandler(String itemName) {
        String query = "Select * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME + " LIKE " + "'%" + itemName + "%'";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Item item = new Item();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            item.setItemID(Integer.parseInt(cursor.getString(0)));
            item.setItemName(cursor.getString(1));
            item.setItemDescription(cursor.getString(2));
            item.setItemImage(cursor.getString(3));
            item.setBorrowerName(cursor.getString(4));
            item.setBorrowerEmail(cursor.getString(5));
            try {
                item.setDateLent(dateFormat.parse(cursor.getString(6)));
            } catch (ParseException e) {
                Log.e(TAG, "Parsing DateLent datetime failed", e);
            }
        try {
            item.setReturnDate(dateFormat.parse(cursor.getString(7)));
        } catch (ParseException e) {
            Log.e(TAG, "Parsing ReturnDate datetime failed", e);
        }
            item.setVerify(cursor.getString(8));
            cursor.close();
        } else {
            item = null;
        }
        db.close();
        return item;
    }


    public Item findByIDHandler(int itemID) {
        String query = "Select * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " IS " + itemID;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Item item = new Item();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            item.setItemID(Integer.parseInt(cursor.getString(0)));
            item.setItemName(cursor.getString(1));
            item.setItemDescription(cursor.getString(2));
            item.setItemImage(cursor.getString(3));
            item.setBorrowerName(cursor.getString(4));
            item.setBorrowerEmail(cursor.getString(5));
            try {
                item.setDateLent(dateFormat.parse(cursor.getString(6)));
            } catch (ParseException e) {
                Log.e(TAG, "Parsing DateLent datetime failed", e);
            }
            try {
                item.setReturnDate(dateFormat.parse(cursor.getString(7)));
            } catch (ParseException e) {
                Log.e(TAG, "Parsing ReturnDate datetime failed", e);
            }
            item.setVerify(cursor.getString(8));
            cursor.close();
        } else {
            item = null;
        }
        db.close();
        return item;
    }


    public boolean deleteHandler(int ID) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Item item = new Item();
        if (cursor.moveToFirst()) {
            item.setItemID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, COLUMN_ID + "=?",
                    new String[] {
                            String.valueOf(item.getItemID())
                    });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }


    public boolean archiveHandler(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ARCHIVE, 1);
        return db.update(TABLE_NAME, args,COLUMN_ID + " = " + ID, null) > 0;
    }

    public boolean unArchiveHandler(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ARCHIVE, 0);
        return db.update(TABLE_NAME, args,COLUMN_ID + " = " + ID, null) > 0;
    }

    public boolean updateHandler(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //args.put(COLUMN_ID, ID);
        args.put(COLUMN_NAME, item.getItemName());
        args.put(COLUMN_DESCRIPTION, item.getItemDescription());
        if (item.getItemImage() != ""){
            args.put(COLUMN_IMAGE, item.getItemImage());
        } else {
            args.put(COLUMN_IMAGE, "none");
        }
        args.put(COLUMN_BORROWERNAME, item.getBorrowerName());
        args.put(COLUMN_BORROWEREMAIL, item.getBorrowerEmail());
        args.put(COLUMN_DATELENT, dateFormat.format(item.getDateLent()));
        args.put(COLUMN_RETURNDATE, dateFormat.format(item.getReturnDate()));
        if(item.getVerify()!= ""){
            args.put(COLUMN_VERIFY, item.getVerify());
        } else {
            args.put(COLUMN_VERIFY, "false");
        }
        return db.update(TABLE_NAME, args,COLUMN_ID + " = " + item.getItemID(), null) > 0;
    }
}
