package com.pingan.config.biz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yintangwen952 on 2018/7/10.
 */

public class ConfigItemParam {
  @SerializedName("configId") public String configId;
  @SerializedName("configVersion") public String configVersion;

  public ConfigItemParam(String configId, String configVersion) {
    this.configId = configId;
    this.configVersion = configVersion;
  }

  public ConfigItemParam() {
  }
}
