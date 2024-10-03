package com.pingan.config.biz.model;

import com.google.gson.annotations.SerializedName;
import com.pingan.config.module.ConfigItem;

import java.util.List;

public class ConfigResp {
    @SerializedName("list") public List<ConfigItem> list;
}
