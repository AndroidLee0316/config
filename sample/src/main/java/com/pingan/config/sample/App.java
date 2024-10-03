package com.pingan.config.sample;

import android.app.Application;

import com.pasc.lib.net.NetConfig;
import com.pasc.lib.net.NetManager;
import com.pingan.config.ConfigRepository;
import com.pingan.config.biz.UrlManager;
import com.pingan.config.callback.IConfigCryption;

/**
 * Created by zhangxu678 on 2018/8/8.
 */
public class App extends Application{
  @Override public void onCreate() {
    super.onCreate();
    NetConfig config = new NetConfig.Builder(this)
            .baseUrl(UrlManager.HOST)
            .isDebug(true)
            .build();
    NetManager.init(config);
    initConfig();


  }
  private void initConfig(){
    String appVersion = "1.5.0";
    String appId = "com.pingan.smt";
    ConfigRepository.getInstance().initConfig(this,
            appVersion, appId, "",
            "1",
            "http://isz-app.sz.gov.cn/",
            new IConfigCryption() {
              @Override
              public String encrypt(String s) {
                return null;
              }

              @Override
              public String decrypt(String s) {
                return null;
              }
            });
  }

}
