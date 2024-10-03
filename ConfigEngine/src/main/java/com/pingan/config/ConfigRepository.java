package com.pingan.config;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.pingan.config.callback.IConfigCryption;
import com.pingan.config.manager.ConfigManager;
import com.pingan.config.manager.ConfigNetManager;
import com.pingan.config.callback.ConfigCallback;
import com.pingan.config.manager.ConfigFileManager;
import com.pingan.config.module.ConfigItem;

import java.util.List;

/**
 * Created by yintangwen952 on 2018/7/10.
 */

public class ConfigRepository {

  //默认asset下配置主目录
  private static final String ASSET_MAIN = "configSystem/";
  private static final String JSON_SUFFIX = ".json";
  private static final String DEFAULT_CONFIG_VERSION = "0.0";
  private static volatile ConfigRepository mRepository;
  private static Context mContext;

  private ConfigFileManager mConfigFileManager;
  private ConfigNetManager mConfigNetManager;
  private boolean useBase;

  /**
   * 创建单例获取
   */
  public static ConfigRepository getInstance() {
    if (mRepository == null) {
      synchronized (ConfigRepository.class) {
        if (mRepository == null) {
          mRepository = new ConfigRepository();
        }
      }
    }
    return mRepository;
  }

  /**
   * 初始化配置查询参数
   */
  public void initConfig(Application context,
      String appVersion,
      String appId,
      String userId,
      String testFlag,
      String baseUrl,
      IConfigCryption iConfigCryption) {
    mContext = context;
    mConfigNetManager = new ConfigNetManager(appVersion, appId, userId, testFlag, baseUrl);
    mConfigFileManager = new ConfigFileManager(iConfigCryption);
    ConfigProviderHelper.init(mContext);
  }

  /**
   * 从本地asset目录下获取配置集合对象
   */
  public <T> List<T> getLocalModelList(String configId, Class<T> modelClass) {
    ConfigItem configItem = ConfigProviderHelper.getInstance().queryConfigItem(configId);
    if (configItem != null && !TextUtils.isEmpty(
        configItem.configContent)) {
      return ConfigManager.configJsonToModels(configItem.configContent, modelClass);
    }
    String fileName = ASSET_MAIN + configId;
    return mConfigFileManager.configJsonToModels(mContext, fileName, modelClass);
  }

  /**
   * 从本地asset目录下获取配置对象
   */
  public <T> T getLocalModel(String configId, Class<T> modelClass) {
    //应用第一次启动无网络情况，且数据库无缓存时，加载本地json数据
    //首先查询本地数据库，否则取缓存
    ConfigItem configItem = ConfigProviderHelper.getInstance().queryConfigItem(configId);
    if (configItem != null && !TextUtils.isEmpty(
        configItem.configContent)) {
      return ConfigManager.configJsonToModel(configItem.configContent, modelClass);
    }
    String fileName = getFullFileName(configId);
    return mConfigFileManager.configJsonToModel(mContext, fileName, modelClass);
  }

  public <T> T getFileModel(String configId, Class<T> modelClass) {
    if (configId == null) return null;
    String fileName = getFullFileName(configId);
    return ConfigManager.configJsonToModel(mConfigFileManager.getAssetsJson(mContext, fileName),
        modelClass);
  }

  public <T> T getContentModel(String content, Class<T> modelClass) {
    if (content == null) return null;
    return ConfigManager.configJsonToModel(content, modelClass);
  }

  public ConfigItem getDBModel(String configId) {
    if (configId == null) return null;
    return ConfigProviderHelper.getInstance().queryConfigItem(configId);
  }

  /**
   * 拼接文件存储路劲
   */
  private String getFullFileName(String configId) {
    return ASSET_MAIN + configId + JSON_SUFFIX;
  }

  /**
   * 获取数据库已有配置版本
   */
  public String getConfigVersion(String configId) {
    ConfigItem configItem = ConfigProviderHelper.getInstance().queryConfigItem(configId);
    if (configItem != null) {
      return TextUtils.isEmpty(configItem.configVersion) ?
          DEFAULT_CONFIG_VERSION : configItem.configVersion;
    }
    return DEFAULT_CONFIG_VERSION;
  }

  public <T> void getNetModel(String configId, String configVersion, Class<T> modelClass,
      final ConfigCallback callback) {
    mConfigNetManager.getNetModel(configId, configVersion, modelClass, callback);
  }

  /**
   * 兼顾网络获取为空时从本地获取
   */
  public <T> void getNetAndLocalModel(String configId, String configVersion, Class<T> modelClass,
      final ConfigCallback callback) {
    mConfigNetManager.getNetAndLocalModel(configId, configVersion, modelClass, callback);
  }

  public boolean isUseBase() {
    return useBase;
  }

  public void setUseBase(boolean useBase) {
    this.useBase = useBase;
  }
}
