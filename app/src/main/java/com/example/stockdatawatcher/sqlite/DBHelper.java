package com.example.stockdatawatcher.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "UserPortfolio.db";
    static final int DATABASE_VERSION = 3;
    static final String DATABASE_TABLE = "userPortfolio";
    static final String STOCK_SYMBOL = "symbol";
    static final String STOCK_AMOUNT = "amount";
    static final String STOCK_PRICE = "price";
    private final SQLiteDatabase DB = this.getWritableDatabase();

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE userPortfolio(symbol TEXT PRIMARY KEY, amount Integer, price Real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(DB);
    }

    public boolean changePortfolio(String symbol, int amount, double price){
        if (existsInPortfolio(symbol)) {
            return updatePortfolio(symbol, amount, price);
        }
        else {
            return addToPortfolio(symbol, amount, price);
        }
    }

    private boolean addToPortfolio(String symbol, int amount, double price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOCK_SYMBOL, symbol);
        contentValues.put(STOCK_AMOUNT, amount);
        contentValues.put(STOCK_PRICE, price);
        long result = DB.insert(DATABASE_TABLE, null, contentValues);

        if (result == -1) {
            // query failed
            return false;
        }
        else {
            return true;
        }
    }

    private boolean updatePortfolio(String symbol, int amount, double price) {
        ContentValues contentValues = new ContentValues();
        //int newAmount = amount + getAmount(symbol);
        //contentValues.put(STOCK_AMOUNT, newAmount);
        contentValues.put(STOCK_AMOUNT, amount);
        contentValues.put(STOCK_PRICE, price);

        Cursor cursor = DB.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE "+ STOCK_SYMBOL + "= ?", new String [] {symbol});

        if (cursor.getCount() > 0){
            // query the symbol being changed
            long result = DB.update(DATABASE_TABLE, contentValues, STOCK_SYMBOL + "=?", new String[] {symbol});

            if (result == -1) {
                // query failed
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    private boolean removeFromPortfolio(String symbol){

        Cursor cursor = DB.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE "+ STOCK_SYMBOL + "= ?", new String [] {symbol});

        if (cursor.getCount() > 0){
            // query the symbol being changed
            long result = DB.delete(DATABASE_TABLE, STOCK_SYMBOL + "=?", new String[] {symbol});

            if (result == -1) {
                // query failed
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    public Cursor getPortfolioData() {

        Cursor cursor = DB.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        return cursor;
    }

    public int getAmount(String symbol) {
        Cursor cursor = DB.rawQuery("SELECT " + STOCK_AMOUNT + " FROM " + DATABASE_TABLE + " WHERE " + STOCK_SYMBOL + "= ?", new String [] {symbol});

        if (cursor.getCount() > 0){
            // zero based index for columns so amount is at index 1
            cursor.moveToNext();
            return cursor.getInt(1);
        }
        // symbol does not exist in db
        return 0;
    }

    public boolean existsInPortfolio(String symbol) {
        Cursor cursor = DB.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE "+ STOCK_SYMBOL + "= ?", new String [] {symbol});

        if (cursor.getCount() > 0) return true;
        return false;
    }
}
