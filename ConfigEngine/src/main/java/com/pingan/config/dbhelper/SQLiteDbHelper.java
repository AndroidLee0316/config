package com.pingan.config.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yintangwen952 on 2018/8/15.
 */

public class SQLiteDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "config.db";

    public static final int DB_VERSION = 1;

    public static final String TABLE_CONFIG = "config";
    private static SQLiteDbHelper mInstance = null;
    private Context mCxt;
    public static SQLiteDbHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new SQLiteDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    //创建 students 表的 sql 语句
    private static final String STUDENTS_CREATE_TABLE_SQL = "create table " + TABLE_CONFIG + "("
            + "configId varchar(20) primary key,"
            + "id integer,"
            + "configVersion nvarchar(20),"
            + "chineseName nvarchar(20),"
            + "englishName nvarchar(20),"
            + "configContent text,"
            + "appVersion nvarchar(20)"
            + ");";

    /**
     * constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    public SQLiteDbHelper(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DB_NAME, null, DB_VERSION);
        this.mCxt = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        // 创建 students 表
        db.execSQL(STUDENTS_CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 数据库版本号变更会调用 onUpgrade 函数，在这根据版本号进行升级数据库
        switch (oldVersion) {
            case 1:
                // do something
                break;

            default:
                break;
        }
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
