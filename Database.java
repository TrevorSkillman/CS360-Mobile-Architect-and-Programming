package com.mod5.projecttwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.core.graphics.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    // database constants
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 2;

    // The user table of constants
    public static final String USER_TABLE = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String DATA_TABLE = "data_items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL1 = "label1";
    public static final String COLUMN_LABEL2 = "label2";

    // creating user table
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT" + ")";

    // creating a query for the data items table
    private static final String CREATE_DATA_ITEMS_TABLE = "CREATE TABLE " + DATA_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_LABEL1 + " TEXT,"
            + COLUMN_LABEL2 + " TEXT" + ")";

    // constructor
    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate method to create tables
    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d("Database", "Creating database tables");
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_DATA_ITEMS_TABLE);
    }

    // onUpgrade method to handle database version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE);
        onCreate(db);
    }

    // adding methods for user auth and registration
    public boolean addUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(USER_TABLE, null, values);
        return result != -1;
    }

    // checking if username and password already exists
    public boolean checkUser(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // adding inventory items into the database
    public boolean addDataItem(String label1, String label2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL1, label1);
        values.put(COLUMN_LABEL2, label2);

        long result = db.insert(DATA_TABLE, null, values);
        return result != -1;
    }

    // displaying or reading the list of inventory items in the database
    public List<DataItem> getAllDataItems(){
        List<DataItem> dataItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATA_TABLE, null);

        if(cursor.moveToFirst()){
            do{
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int label1Index = cursor.getColumnIndex(COLUMN_LABEL1);
                int label2Index = cursor.getColumnIndex(COLUMN_LABEL2);

                if (idIndex != -1 && label1Index != -1 && label2Index != -1) {
                    int id = cursor.getInt(idIndex);
                    String label1 = cursor.getString(label1Index);
                    String label2 = cursor.getString(label2Index);
                    DataItem dataItem = new DataItem(id, label1, label2);
                    dataItems.add(dataItem);
                }
            } while(cursor.moveToNext());
        }
        cursor.close();
        return dataItems;
    }

    // deleting an item from the inventory list
    public boolean deleteDataItem(int id ){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATA_TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // updating or editing the label 1 or label 2
    public boolean updateDataItem(DataItem dataItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL1, dataItem.getLabel1());
        values.put(COLUMN_LABEL2, dataItem.getLabel2());

        int result = db.update(DATA_TABLE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(dataItem.getId())});
        return result > 0;
    }
}
