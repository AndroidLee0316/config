package com.pingan.config.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.pingan.config.ContentProviderDb;
import com.pingan.config.module.ConfigItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yintangwen952 on 2018/8/15.
 */

public class ConfigDbHelper {


    private static volatile SQLiteDbHelper dbHelper;
    private static volatile ConfigDbHelper mConfigDbHelper;

    /**
     * 创建单例获取
     *
     * @return
     */
    public static ConfigDbHelper getInstance() {
        if (mConfigDbHelper == null) {
            synchronized (ConfigDbHelper.class) {
                if (mConfigDbHelper == null) {
                    mConfigDbHelper = new ConfigDbHelper();
                }
            }
        }
        return mConfigDbHelper;
    }

    /**
     * 传入上下文，进行初始化
     *
     * @return
     */
    public static void init(Context context) {
        dbHelper = SQLiteDbHelper.getInstance(context);

    }

    /**
     * 存入数据库表
     *
     * @param item
     */
    public void insertConfigItem(ConfigItem item) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("configId", item.configId);
            contentValues.put("chineseName", item.chineseName);
            contentValues.put("configVersion", item.configVersion);
            contentValues.put("englishName", item.englishName);
            contentValues.put("appVersion", item.appVersion);
            contentValues.put("configContent", item.configContent);
            db.replace(SQLiteDbHelper.TABLE_CONFIG, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
    }

    public void insertConfigItem(ConfigItem item, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("configId", item.configId);
        contentValues.put("chineseName", item.chineseName);
        contentValues.put("configVersion", item.configVersion);
        contentValues.put("englishName", item.englishName);
        contentValues.put("appVersion", item.appVersion);
        contentValues.put("configContent", item.configContent);
        db.replace(SQLiteDbHelper.TABLE_CONFIG, null, contentValues);
    }

    /**
     * 存入数据库表
     *
     * @param items
     */
    public void insertConfigItems(List<ConfigItem> items) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            db.beginTransaction();
            for (ConfigItem item : items) {
                insertConfigItem(item, db);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
            }
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
    }

    /**
     * 根据id查询
     *
     * @param configId
     * @return
     */
    public ConfigItem queryConfigItem(String configId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ConfigItem item = null;
        try {
            db = dbHelper.getReadableDatabase();
            item = new ConfigItem();
            cursor = db.query(SQLiteDbHelper.TABLE_CONFIG, null,
                    "configId = ? ", new String[]{configId},
                    null, null, null, null);
            // 不断移动光标获取值
            if (cursor.moveToNext()) {
                // 直接通过索引获取字段值
                int id = cursor.getInt(0);
                // 先获取 id 的索引值，然后再通过索引获取字段值
                item.configId = cursor.getString(cursor.getColumnIndex("configId"));
                item.chineseName = cursor.getString(cursor.getColumnIndex("chineseName"));
                item.configVersion = cursor.getString(cursor.getColumnIndex("configVersion"));
                item.englishName = cursor.getString(cursor.getColumnIndex("englishName"));
                item.appVersion = cursor.getString(cursor.getColumnIndex("appVersion"));
                item.configContent = cursor.getString(cursor.getColumnIndex("configContent"));
                // 关闭光标
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return item;
    }

    /**
     * 根据id查询
     *
     * @return
     */
    public List<ConfigItem> queryConfigItemList() {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<ConfigItem> configItemList = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(SQLiteDbHelper.TABLE_CONFIG, null,
                    null, null,
                    null, null, null, null);
            configItemList = new ArrayList<>();
            ConfigItem item;
            // 不断移动光标获取值
            while (cursor.moveToNext()) {
                item = new ConfigItem();
                // 直接通过索引获取字段值
                int id = cursor.getInt(0);
                // 先获取 id 的索引值，然后再通过索引获取字段值
                item.configId = cursor.getString(cursor.getColumnIndex("configId"));
                item.chineseName = cursor.getString(cursor.getColumnIndex("chineseName"));
                item.configVersion = cursor.getString(cursor.getColumnIndex("configVersion"));
                item.englishName = cursor.getString(cursor.getColumnIndex("englishName"));
                item.appVersion = cursor.getString(cursor.getColumnIndex("appVersion"));
                item.configContent = cursor.getString(cursor.getColumnIndex("configContent"));
                configItemList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return configItemList;
    }
}
