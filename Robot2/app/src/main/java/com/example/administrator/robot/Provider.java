package com.example.administrator.robot;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class Provider extends ContentProvider {

    private static UriMatcher uriMatcher;
    private static final String TAG="provider";
    private static final int DIR=0;
    private static final int ITEM=1;
    private static final String AUTHORITY ="com.example.administrator.robot";
    private static final String TABLE_NAME="contacts";
    private static final String CONTENT_PREFIX="content://";
    private static final String MIME_PREFIX="vnd.";
    private static final String MINE_INFIX_DIR="android.cursor.dir";
    private static final String MINE_INFIX_ITEM="android.cursor.item";
    static{
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,TABLE_NAME,DIR);
        uriMatcher.addURI(AUTHORITY,TABLE_NAME+"/#",ITEM);
    }
    private DbHelper dbHelper;


    public Provider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int deleteRows=0;
        switch(uriMatcher.match(uri)){
            case DIR:
                deleteRows=db.delete("contacts", selection, selectionArgs);
                break;
            case ITEM:
                String Id=uri.getPathSegments().get(1);
                deleteRows=db.delete("contacts", "id=?", new String[]{Id});
                break;
            default:
                break;
        }
        return deleteRows;//被删除的行数作为返回值返回
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch(uriMatcher.match(uri)){
            case DIR:
                return MIME_PREFIX + MINE_INFIX_DIR + "/" + MIME_PREFIX + AUTHORITY + "."+TABLE_NAME;
            case ITEM:
                return MIME_PREFIX + MINE_INFIX_ITEM + "/" + MIME_PREFIX + AUTHORITY + "." + TABLE_NAME;
        }
        return null;

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri uriReturn=null;
        long newContactId=db.insert("contacts",null,values);
        uriReturn=Uri.parse("content://"+AUTHORITY+"/contacts"+newContactId);
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper=new DbHelper(getContext(),"contacts.db",null,2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case DIR:
                cursor=db.query("contacts",projection,selection,selectionArgs,null,
                        null,sortOrder);
                break;
            case ITEM:
                String contactId=uri.getPathSegments().get(1);
                cursor=db.query("contacts",projection,"id=?",new String[]{contactId},
                        null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case DIR:
                updateRows=db.update("contacts",values,selection,selectionArgs);
                break;
            case ITEM:
                String contactId=uri.getPathSegments().get(1);
                updateRows=db.update("contacts",values,"id=?",new String[]{contactId});
                break;
        }
        return updateRows;
    }
}
