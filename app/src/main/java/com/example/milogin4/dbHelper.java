package com.example.milogin4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "usersDP.db";
    public static final String COLUMN_USER_USER = "name";
    public static final String COLUMN_USER_IMG = "image";
    public static final String TABLE_USER = "tableimage";
    private final String CREATE_USER_TABLE = "CREATE TABLE tableimage(name text PRIMARY KEY, image text);";
    public dbHelper(Context context) {
        super(context, DBNAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tableimage");
    }
    public void addPhoto(String usuario, String img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USER,usuario);
        values.put(COLUMN_USER_IMG,img);
        db.insert(TABLE_USER,null, values);
        db.close();
    }
    public void updatePhoto(String usuario, String img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USER, usuario);
        values.put(COLUMN_USER_IMG, img);
        db.update(TABLE_USER, values, COLUMN_USER_USER + " = ? ", new String[]{usuario});
        db.close();
    }
    // para ver si el mecoboy existe
    public boolean checkPhoto(String usuario){
        String [] columns = {COLUMN_USER_USER};
        SQLiteDatabase db = this.getReadableDatabase();
        String sel = COLUMN_USER_USER + " = ? ";
        String [] selArgs = {usuario};

        //Cursor c = db.query(TABLE_USER, columns, sel, selArgs, null, null, null);
        Cursor c = db.rawQuery("SELECT * FROM tableimage WHERE name =? ", new String[]{usuario});
        System.out.println(c.getCount());
        if(c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}