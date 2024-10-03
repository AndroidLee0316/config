package com.pingan.config;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import android.text.TextUtils;
import com.pingan.config.dbhelper.SQLiteDbHelper;
import com.pingan.config.module.ConfigItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yintangwen952 on 2018/10/19.
 */

public class ConfigProviderHelper {

    private static volatile ConfigProviderHelper mConfigDbHelper;
    private static volatile Context mContext;

    /**
     * 创建单例获取
     *
     * @return
     */
    public static ConfigProviderHelper getInstance() {
        if (mConfigDbHelper == null) {
            synchronized (ConfigProviderHelper.class) {
                if (mConfigDbHelper == null) {
                    mConfigDbHelper = new ConfigProviderHelper();
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
        mContext = context;
    }

    public void insertItem(ConfigItem item) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("chineseName", item.chineseName);
        contentValues.put("configVersion", item.configVersion);
        contentValues.put("englishName", item.englishName);
        String appVersion;
        if (TextUtils.isEmpty(item.appVersion)){
            appVersion = getVersionName(mContext);
        }else {
            appVersion = item.appVersion;
        }
        contentValues.put("appVersion", appVersion);
        contentValues.put("configContent", item.configContent);
        if (existItem(item)){
            mContext.getContentResolver().update(
                    ContentProviderDb.CODE_MESSAGE_UPDATE_URI,
                    contentValues,"configId = ? ",
                    new String[]{item.configId});
        }else {
            contentValues.put("configId", item.configId);
            mContext.getContentResolver().insert(
                    ContentProviderDb.CODE_MESSAGE_INSERT_URI,
                    contentValues);
        }
    }

    public boolean existItem(ConfigItem item) {
        return queryConfigItem(item.configId) == null ? false:true;
    }

    /**
     * 存入数据库表
     *
     * @param items
     */
    public void insertItems(List<ConfigItem> items) {
        for (ConfigItem item : items) {
            insertItem(item);
        }
    }

    /**
     * 根据id查询
     *
     * @param configId
     * @return
     */
    public ConfigItem queryConfigItem(String configId) {

        Cursor cursor = null;
        ConfigItem item = null;
        try {
            cursor = mContext.getContentResolver().query(
                    ContentProviderDb.CODE_MESSAGE_QUERY_URI,
                    null,
                    "configId = ? ",
                    new String[]{configId}, null);
            // 直接通过索引获取字段值
            if (cursor != null && cursor.moveToFirst()) {
                item = new ConfigItem();
                // 先获取 id 的索引值，然后再通过索引获取字段值
                item.configId = cursor.getString(cursor.getColumnIndex("configId"));
                item.chineseName = cursor.getString(cursor.getColumnIndex("chineseName"));
                item.configVersion = cursor.getString(cursor.getColumnIndex("configVersion"));
                item.englishName = cursor.getString(cursor.getColumnIndex("englishName"));
                item.appVersion = cursor.getString(cursor.getColumnIndex("appVersion"));
                item.configContent = cursor.getString(cursor.getColumnIndex("configContent"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
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
        Cursor cursor = null;
        List<ConfigItem> configItemList = null;
        try {
            cursor = mContext.getContentResolver().query(ContentProviderDb.CODE_MESSAGE_QUERY_URI,
                    null,
                    null,
                    null
                    , null);
            configItemList = new ArrayList<>();
            ConfigItem item;
            // 不断移动光标获取值
            while (cursor != null && cursor.moveToNext()) {
                item = new ConfigItem();
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
        }
        return configItemList;
    }

    /**
     * 获取版本名
     */
    private String getVersionName(Context ctx) {
        String versionName = "";
        try {
            PackageInfo packageInfo =
                    ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
