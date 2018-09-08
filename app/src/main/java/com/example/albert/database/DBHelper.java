package com.example.albert.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final int DATABASE_VERSION = 1;
    public static final String CONTACTS_TABLE_NAME = "contact";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_PHONE = "phone";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+CONTACTS_TABLE_NAME+ "("
                +CONTACTS_COLUMN_ID+" INTEGER PRIMARY KEY, "
                +CONTACTS_COLUMN_NAME+" TEXT, "
                +CONTACTS_COLUMN_PHONE+" TEXT, "
                +CONTACTS_COLUMN_EMAIL+" TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact(String name, String phone, String email){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_NAME, name);
            contentValues.put(CONTACTS_COLUMN_PHONE, phone);
            contentValues.put(CONTACTS_COLUMN_EMAIL, email);
            db.insert(CONTACTS_TABLE_NAME, null, contentValues);
            db.close();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+CONTACTS_TABLE_NAME+" WHERE "+CONTACTS_COLUMN_ID+" = "+id+";", null);
        return res;
    }

    public boolean updateContact(Integer id, String name, String phone, String email){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_ID, id);
            contentValues.put(CONTACTS_COLUMN_NAME, name);
            contentValues.put(CONTACTS_COLUMN_PHONE, phone);
            contentValues.put(CONTACTS_COLUMN_EMAIL, email);
            db.update(CONTACTS_TABLE_NAME, contentValues, CONTACTS_COLUMN_ID + " = ?", new String[] { Integer.toString(id) } );
            db.close();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean deleteContact(Integer id){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_ID + " = ?", new String[] { Integer.toString(id) });
            db.close();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public ArrayList<String> getAllContacts(){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+CONTACTS_TABLE_NAME, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
