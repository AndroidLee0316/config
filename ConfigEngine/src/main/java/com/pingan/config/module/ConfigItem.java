package com.pingan.config.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yintangwen952 on 2018/5/19.
 */

public class ConfigItem {

    @SerializedName("id")
    public int id;

    @SerializedName("configId")
    public String configId;

    @SerializedName("configVersion")
    public String configVersion;

    @SerializedName("chineseName")
    public String chineseName;

    @SerializedName("englishName")
    public String englishName;

    @SerializedName("configContent")
    public String configContent;

    @SerializedName("appVersion")
    public String appVersion;
}
