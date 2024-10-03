package com.pingan.config.manager;

import com.google.gson.Gson;

import java.util.List;
import java.lang.reflect.Type;
import ikidou.reflect.TypeBuilder;

/**
 * Created by yintangwen952 on 2018/7/6.
 */

public class ConfigManager {

  private ConfigManager() {
  }

  private static ConfigManager configManager = new ConfigManager();
  private Gson gson = new Gson();

  /**
   * 对象转Json字符串
   */
  public static String configModelToJson(Object o) {
    if (o == null) return "";
    return configManager.gson.toJson(o);
  }

  /**
   * json字符串转对象
   */
  public static <T> T configJsonToModel(String jsonString, Class<T> modelClass) {
    if (jsonString == null) return null;
    if (modelClass == null) return (T)jsonString;
    try {
      return configManager.gson.fromJson(jsonString, modelClass);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 集合对象转json字符串
   */
  public static String configModelsToJson(List modelList) {
    if (modelList == null) return "";
    return configManager.gson.toJson(modelList);
  }

  /**
   * json字符串转集合对象
   */
  public static <T> List<T> configJsonToModels(String jsonString, Class<T> modelClass) {
    try {
      Type listType = TypeBuilder
          .newInstance(List.class)
          .addTypeParam(modelClass)
          .build();
      List<T> modelList = configManager.gson.fromJson(jsonString, listType);
      return modelList;
    } catch (RuntimeException e) {
      e.printStackTrace();
      return null;
    }
  }
}
