package com.rodriguezpacojr.winestore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by francisco on 3/21/18.
 */
public class DataBase extends SQLiteOpenHelper {

    String sql = "CREATE TABLE shoppingcart (id INTEGER PRIMARY KEY, keyOrder INTEGER, keyEmployee INTEGER, keyCustomer INTEGER,  keyProduct INTEGER, product VARCHAR(30), price DOUBLE, quantity INTEGER)";

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
