package com.pingan.config.manager;

import android.text.TextUtils;

import com.pasc.lib.net.resp.BaseRespObserver;
import com.pingan.config.ConfigProviderHelper;
import com.pingan.config.biz.UrlManager;
import com.pingan.config.dbhelper.ConfigDbHelper;
import com.pingan.config.ConfigRepository;
import com.pingan.config.biz.ConfigBiz;
import com.pingan.config.biz.model.ConfigItemParam;
import com.pingan.config.biz.model.ConfigParamNew;
import com.pingan.config.biz.model.ConfigUserInfo;
import com.pingan.config.callback.ConfigCallback;
import com.pingan.config.module.ConfigItem;
import com.pingan.dynamicconfig.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yintangwen952 on 2018/7/13.
 */

public class ConfigNetManager {

    private static String mAppVersion;
    private static String mAppId;
    private static String mUserId;
    private static String mTestFlag;

    public ConfigNetManager(String appVersion,
                            String appId,
                            String userId,
                            String testFlag,String baseUrl) {
        mAppVersion = appVersion;
        mAppId = appId;
        mUserId = userId;
        mTestFlag = testFlag;
        if (!TextUtils.isEmpty(baseUrl)){
            UrlManager.HOST = baseUrl;
        }
    }

    /**
     * 获取制定配置id与版本号的配置项
     */
    public <T> void getNetModel(final String configId,
                                String configVersion, final Class<T> modelClass,
                                final ConfigCallback callback) {
        ConfigItemParam configItemParam = new ConfigItemParam(configId, configVersion);
        //配置项id集合
        ConfigBiz.queryConfig(
                ConfigParamNew.builder()
                        .configItem(configItemParam)
                        .appId(mAppId)
                        .appVersion(mAppVersion)
                        .testFlag(mTestFlag)
                        .configUserInfo(ConfigUserInfo.builder().phoneNumber(mUserId).build())
                        .build()
        )
                .subscribe(new BaseRespObserver<List<ConfigItem>>() {
                    @Override
                    public void onError(int code, String msg) {
                        //加载错误
                        callback.onError(msg);
                    }

                    @Override
                    public void onSuccess(List<ConfigItem> configItemList) {
                        String configContent = null;
                        if (configItemList != null) {
                            for (ConfigItem item : configItemList) {
                                if (configId.equals(item.configId)) {
                                    configContent = item.configContent;
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(configContent)) {
                            callback.onNext(ConfigManager.configJsonToModel(configContent, modelClass));
                        } else {
                            callback.onEmpty();
                        }
                    }

                });
    }

    /**
     * cd
     * 获取制定配置id与版本号的配置项，网络获取不到时从本地解析
     */
    public <T> void getNetAndLocalModel(final String configId,
                                        String configVersion, final Class<T> modelClass,
                                        final ConfigCallback callback) {
        ConfigItemParam configItemParam = new ConfigItemParam(configId, configVersion);
        //配置项id集合
        ConfigBiz.queryConfig(
                ConfigParamNew.builder()
                        .configItem(configItemParam)
                        .appId(mAppId)
                        .appVersion(mAppVersion)
                        .testFlag(mTestFlag)
                        .configUserInfo(ConfigUserInfo.builder().phoneNumber(mUserId).build())
                        .build()
        )
                .subscribe(new BaseRespObserver<List<ConfigItem>>() {
                    @Override
                    public void onError(int code, String msg) {
                        //加载错误时，取本地数据
                        callback.onNext(ConfigRepository.getInstance().getLocalModel(configId, modelClass));
                    }

                    @Override
                    public void onSuccess(List<ConfigItem> configItemList) {
                        String configContent = null;
                        if (configItemList == null) {
                            return;
                        }
                        for (ConfigItem item : configItemList) {
                            if (configId.equals(item.configId)) {
                                configContent = item.configContent;
                            }
                        }
                        if (!TextUtils.isEmpty(configContent)) {
                            callback.onNext(ConfigManager.configJsonToModel(configContent, modelClass));
                        } else {
                            callback.onNext(ConfigRepository.getInstance().getLocalModel(configId, modelClass));
                        }
                    }

                });
    }

    /**
     * 获取配置集合对象，该方法会返回所有已有配置项，包括测试数据
     */
    public void getNetModelList(final ConfigCallback callback) {
        ConfigItemParam configItemParam = new ConfigItemParam();
        //配置项id集合
        ConfigBiz.queryConfig(
                ConfigParamNew.builder()
                        .configItem(configItemParam)
                        .appId(mAppId)
                        .appVersion(mAppVersion)
                        .testFlag(mTestFlag)
                        .configUserInfo(ConfigUserInfo.builder().phoneNumber(mUserId).build())
                        .build()
        )
                .subscribe(new BaseRespObserver<List<ConfigItem>>() {
                    @Override
                    public void onError(int code, String msg) {
                        //加载错误
                        callback.onError(msg);
                    }

                    @Override
                    public void onSuccess(List<ConfigItem> configItemList) {
                        if (configItemList == null) {
                            return;
                        }
                        callback.onNext(configItemList);
                    }
                });
    }

    /**
     * 更新数据库已有配置集合对象
     */
    public void getNetModelALL(final ConfigCallback callback) {
        //参数集合对象
        List<ConfigItemParam> temp = new ArrayList<>();
        ConfigItemParam configItemParam;
        //获取已有所有数据库缓存配置对象集合
        List<ConfigItem> configList = ConfigProviderHelper.getInstance().queryConfigItemList();
        for (ConfigItem item : configList) {
            configItemParam = new ConfigItemParam(item.configId, item.configVersion);
            temp.add(configItemParam);
        }
        //配置项id集合
        ConfigBiz.queryConfig(
                ConfigParamNew.builder()
                        .configItems(temp)
                        .appId(mAppId)
                        .appVersion(mAppVersion)
                        .testFlag(mTestFlag)
                        .configUserInfo(ConfigUserInfo.builder().phoneNumber(mUserId).build())
                        .build()
        )
                .subscribe(new BaseRespObserver<List<ConfigItem>>() {
                    @Override
                    public void onError(int code, String msg) {
                        //加载错误
                        callback.onError(msg);
                    }

                    @Override
                    public void onSuccess(List<ConfigItem> configItemList) {
                        if (configItemList == null) {
                            return;
                        }
                        callback.onNext(configItemList);
                    }

                });
    }
}
