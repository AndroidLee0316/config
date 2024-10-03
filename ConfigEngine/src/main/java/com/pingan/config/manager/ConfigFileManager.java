package com.pingan.config.manager;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pingan.config.callback.IConfigCryption;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by yintangwen952 on 2018/7/6.
 */

public class ConfigFileManager {

    private static IConfigCryption mConfigCryption;

    public ConfigFileManager(IConfigCryption configCryption) {
        mConfigCryption = configCryption;
    }

    /**
     * 读取assets下json文件数据
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getAssetsJson(Context context, String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = stringBuilder.toString();
        if (mConfigCryption != null && validateJson(content)){
            return content;
        }
        return mConfigCryption.decrypt(content);
    }

    /**
     * 本地json文件转对象
     *
     * @param fileName
     * @param context
     * @param modelClass
     * @param <T>
     * @return
     */
    public static <T> T configJsonToModel(Context context, String fileName, Class<T> modelClass) {
        if (fileName == null) return null;
        return ConfigManager.configJsonToModel(getAssetsJson(context, fileName), modelClass);
    }

    /**
     * 本地json文件转集合对象
     *
     * @param fileName
     * @param context
     * @param modelClass
     * @param <T>
     * @return
     */
    public static <T> List<T> configJsonToModels(Context context, String fileName, Class<T> modelClass) {
        if (fileName == null) return null;
        return ConfigManager.configJsonToModels(getAssetsJson(context, fileName), modelClass);
    }

    public static boolean validateJson(String content) {

        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(content);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;

    }
}
