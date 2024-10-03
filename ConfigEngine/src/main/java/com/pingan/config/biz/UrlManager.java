package com.pingan.config.biz;

/**
 * 后台网络接口类
 * Created by yintangwen952 on 17/3/22.
 */

public class UrlManager {
  public static String HOST = "https://smt-app.pingan.com.cn/";
  //新版更多服务-根据编码查询服务
  public static final String QUERY_SERVICE_CONFIG_NEW = "smtapp/configSystem/getConfigInfoNew.do";
  //基线更多服务-根据编码查询服务
  public static final String QUERY_SERVICE_CONFIG_BASE = "api/cs/appVersion/getConfigInfo";
}
