package com.example.susanne.susannebinkhorst_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Susanne on 27-11-2017.
 */

public class RestoDatabase extends SQLiteOpenHelper {

    private static String TABLE_NAME = "orders";
    private static RestoDatabase instance;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, name TEXT, price FLOAT, amount INTEGER)";
        sqLiteDatabase.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private RestoDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    public static RestoDatabase getInstance(Context context){
        if (instance == null){
            instance = new RestoDatabase(context);
        }
        return instance;
    }

    public void addItem(String name,Float price) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("Select amount from "+ TABLE_NAME + " where name = '"+name + "';",null);
        ContentValues contentValues = new ContentValues();

        if (c.getCount() == 0){
            contentValues.put("name", name);
            contentValues.put("amount", 1);
            contentValues.put("price", price);

            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        }
        else if (c.getCount() > 0){
            String sql = "UPDATE " + TABLE_NAME + " SET amount = amount + 1 WHERE name = '" + name + "';";
            sqLiteDatabase.execSQL(sql);
        }
    }

    public Cursor selectAll(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from "+ TABLE_NAME,null);
        return c;
    }


    public void delete(String dish){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select amount from "+ TABLE_NAME + " where name = '"+ dish + "';",null);

        if (c.getCount() == 1){
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE name = '" +dish + "';";
            sqLiteDatabase.execSQL(sql);

        }
        else if (c.getCount() > 1){
            String sql = "UPDATE " + TABLE_NAME + " SET amount = amount - 1 WHERE name = '" + dish + "';";
            sqLiteDatabase.execSQL(sql);
        }
    }

    public Float selectSum(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * FROM " + TABLE_NAME + ";", null);
        Integer price_index = c.getColumnIndex("price");
        Integer amount_index = c.getColumnIndex("amount");

        float total = 0;

        while (c.moveToNext()){
            total = total + c.getFloat(price_index) * c.getInt(amount_index);
        }
        return total;
    }
    public void clear(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
