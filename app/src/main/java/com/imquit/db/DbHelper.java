package com.imquit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.imquit.model.UsageModel;

import java.util.ArrayList;



public class DbHelper extends SQLiteOpenHelper {

    public static final String COL_1 = "app_name";
    public static final String COL_0 = "id";
    public static final String COL_2 = "app_time";
    private static final String DB_NAME = "my_db";
    private static final String TABLE_NAME = "app_info";
    private static final String TABLE_NAME2 = "app_select";
    static String query = "CREATE TABLE " + TABLE_NAME + "(" + COL_1 + " TEXT, " + COL_2 + " TEXT)";
    static String query2 = "CREATE TABLE " + TABLE_NAME2 + "(" + COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COL_1 + " TEXT)";
    private Context context;


    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
    }

    public void insertData(String appName, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, appName);
        contentValues.put(COL_2, time);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long insert = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (insert > 0) {
            Toast.makeText(context, "Restriction added", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<UsageModel> getAllData() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<UsageModel> usageModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            UsageModel usageModel = new UsageModel();
            usageModel.setPackageName(cursor.getString(cursor.getColumnIndex(COL_1)));
            usageModel.setTime(cursor.getString(cursor.getColumnIndex(COL_2)));
            usageModels.add(usageModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return usageModels;

    }

    public void insertSelectedAppData(String appName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, appName);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long insert = sqLiteDatabase.insert(TABLE_NAME2, null, contentValues);
        if (insert > 0) {
            Log.e("success:", "asd");
        }
        sqLiteDatabase.close();
    }

    public ArrayList<UsageModel> getAllSelectedData() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME2;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<UsageModel> usageModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            UsageModel usageModel = new UsageModel();
            usageModel.setPackageName(cursor.getString(cursor.getColumnIndex(COL_1)));
            usageModel.setId(cursor.getInt(cursor.getColumnIndex(COL_0)));
            usageModels.add(usageModel);
        }
        sqLiteDatabase.close();
        return usageModels;

    }

    public int deleteApp(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int delete = sqLiteDatabase.delete(TABLE_NAME2, "id = ?", new String[]{id + ""});
        sqLiteDatabase.close();
        return delete;

    }

    public int deleteAppSeletct(String packageName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int delete = sqLiteDatabase.delete(TABLE_NAME, "app_name = ?", new String[]{packageName});
        sqLiteDatabase.delete(TABLE_NAME2, "app_name = ?", new String[]{packageName});
        sqLiteDatabase.close();
        return delete;

    }
}
