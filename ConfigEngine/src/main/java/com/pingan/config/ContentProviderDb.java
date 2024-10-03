package com.pingan.config;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.pingan.config.dbhelper.SQLiteDbHelper;

public class ContentProviderDb extends ContentProvider {
    SQLiteDbHelper dbHelper ;
    //空白匹配器
    UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    //自身的匹配规则
    final static int CODE_MESSAGE_INSERT = 0;
    final static int CODE_MESSAGE_QUERY = 1;
    final static int CODE_MESSAGE_DELETE = 2;
    final static int CODE_MESSAGE_UPDATE = 3;

    public static String AUTHORITY;
    //specific for our our app, will be specified in maninfed
    public static Uri BASE_URI;
    //写入该主机名的匹配规则
    //{
    //
    //}

    //对外提供的URI
    public static Uri CODE_MESSAGE_INSERT_URI;
    public static Uri CODE_MESSAGE_QUERY_URI;
    public static Uri CODE_MESSAGE_DELETE_URI;
    public static Uri CODE_MESSAGE_UPDATE_URI;
    //数据库名
    public final static String DB_CONFIG = SQLiteDbHelper.TABLE_CONFIG;
    private void initConfig(){
        AUTHORITY=getContext().getPackageName();
        BASE_URI=Uri.parse("content://" + AUTHORITY);

        CODE_MESSAGE_INSERT_URI = Uri.parse("content://" + AUTHORITY + "/config/insert");
        CODE_MESSAGE_QUERY_URI = Uri.parse("content://" + AUTHORITY + "/config/query");
        CODE_MESSAGE_DELETE_URI = Uri.parse("content://" + AUTHORITY + "/config/delete");
        CODE_MESSAGE_UPDATE_URI = Uri.parse("content://" + AUTHORITY + "/config/update");

        matcher.addURI(AUTHORITY, "config/insert", CODE_MESSAGE_INSERT);
        matcher.addURI(AUTHORITY, "config/query", CODE_MESSAGE_QUERY);
        matcher.addURI(AUTHORITY, "config/delete", CODE_MESSAGE_DELETE);
        matcher.addURI(AUTHORITY, "config/update", CODE_MESSAGE_UPDATE);
    }
    @Override
    public boolean onCreate() {
        dbHelper = SQLiteDbHelper.getInstance(getContext());
        initConfig();
        return true;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case CODE_MESSAGE_INSERT:
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                long rowId = database.insert(DB_CONFIG, null, values);
                if (rowId == -1) {
                    //添加失败
                    return null;
                } else {
                    //发送内容广播
                    getContext().getContentResolver().notifyChange(BASE_URI, null);
                    //添加成功
                    return ContentUris.withAppendedId(uri, rowId);
                }
            default:
                throw new IllegalArgumentException("未识别的uri" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case CODE_MESSAGE_QUERY:
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                Cursor cursor = database.query(DB_CONFIG, projection, selection, selectionArgs, null, null, sortOrder);
                //注册内容观察者，观察数据变化
                cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
                return cursor;
            default:
                throw new IllegalArgumentException("未识别的uri" + uri);
        }
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case CODE_MESSAGE_DELETE:
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                int number = database.delete(DB_CONFIG, selection, selectionArgs);
                //发送内容广播
                getContext().getContentResolver().notifyChange(BASE_URI, null);
                return number;
            default:
                throw new IllegalArgumentException("未识别的uri" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case CODE_MESSAGE_UPDATE:
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                int number = database.update(DB_CONFIG, values, selection, selectionArgs);
                //发送内容广播
                getContext().getContentResolver().notifyChange(BASE_URI, null);
                return number;
            default:
                throw new IllegalArgumentException("未识别的uri" + uri);
        }
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }
}
