package com.example.susanne.susannebinkhorst_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Susanne on 30-11-2017.
 */

public class DishesDatabase extends SQLiteOpenHelper {

    private static String TABLE_NAME = "dishes";
    private static DishesDatabase instance;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY, name TEXT, category TEXT, description TEXT, image_url TEXT, price FLOAT, amount INTEGER)";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void onDrop() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private DishesDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    public static DishesDatabase getInstance(Context context){
        if (instance == null){
            instance = new DishesDatabase(context);
        }
        return instance;
    }

    public void addItem(String name, String description, String category, String image_url, Float price) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("category", category);
        contentValues.put("image_url", image_url);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

    }

    public Cursor selectAll(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from "+ TABLE_NAME,null);
        return c;
    }

    public Cursor selectRow(Long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from "+ TABLE_NAME + " where _id = " + id,null);
        return c;
    }

}
