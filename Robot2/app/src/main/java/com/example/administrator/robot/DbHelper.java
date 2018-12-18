package com.example.administrator.robot;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xc on 2018-06-06.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK="create table contacts("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"phone text,"
            +"sex text)";

    private Context myContext;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        myContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);
//        Toast.makeText(myContext,"create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

