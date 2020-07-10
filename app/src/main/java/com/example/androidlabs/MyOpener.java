package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.logging.Logger;

public class MyOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "msgDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "msgTable";
    public final static String COL_MESSAGE = "message";
    public final static String COL_ID = "messageID";
    public final static String COL_ISSENDORRECEIVE = "isSendOrReceive";



    public MyOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME +"("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COL_MESSAGE+" TEXT,"
                +COL_ISSENDORRECEIVE+" TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //to trigger this increment the version number to drop and create a new table

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) { //to trigger this decrement the version number to drop and create a new table

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    public void printCursor(Cursor c, int version){
        String columnNames = "";
        for (String s : c.getColumnNames()) {
            columnNames+=s+",";

        }
        Log.wtf("DB Version", version+", No of columns: "+c.getColumnCount()+
                " Names of columns: "+columnNames);
        Log.wtf("No of rows: ", c.getCount()+"");
        Log.wtf("Rows in the cursors", "");
        int counter = 1;

        while (!c.isLast()){
            Log.wtf("Row "+counter, c.getLong(0)+", "+c.getString(1)+", "+c.getString(2) );
            counter++;
            c.moveToNext();
        }
        Log.wtf("Row "+counter, c.getLong(0)+", "+c.getString(1)+", "+c.getString(2) );


    }


}
