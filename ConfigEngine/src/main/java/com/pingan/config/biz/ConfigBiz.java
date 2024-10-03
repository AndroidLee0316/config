package com.pingan.config.biz;

import com.pasc.lib.net.ApiGenerator;
import com.pasc.lib.net.param.BaseParam;
import com.pasc.lib.net.transform.RespTransformer;
import com.pingan.config.ConfigProviderHelper;
import com.pingan.config.ConfigRepository;
import com.pingan.config.biz.model.ConfigResp;
import com.pingan.config.biz.model.ConfigParamNew;
import com.pingan.config.module.ConfigItem;
import java.util.List;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yintangwen952 on 17/11/15.
 */

public class ConfigBiz {
  /**
   * 获取新的模块配置信息
   */
  public static Single<List<ConfigItem>> queryConfig(ConfigParamNew configParamNew) {
    return queryConfigFromNet(configParamNew);
  }

  /**
   * 网络获取新的模块配置信息
   */
  public static Single<List<ConfigItem>> queryConfigFromNet(ConfigParamNew configParamNew) {
    BaseParam<ConfigParamNew> param =
        new BaseParam<>(configParamNew);
    RespTransformer<ConfigResp> respTransformer = RespTransformer.newInstance();

    return (!ConfigRepository.getInstance().isUseBase() ? ApiGenerator.createApi(UrlManager.HOST,
        ConfigService.class)
        .queryServiceConfigNew(param)
        : ApiGenerator.createApi(UrlManager.HOST, ConfigService.class)
            .queryServiceConfigBase(configParamNew))
        .compose(respTransformer)
        .doOnSuccess(new Consumer<ConfigResp>() {
          @Override
          public void accept(final ConfigResp resp)
              throws Exception {
            ConfigProviderHelper.getInstance().insertItems(resp.list);
          }
        })
        .map(new Function<ConfigResp, List<ConfigItem>>() {
          @Override
          public List<ConfigItem> apply(ConfigResp resp)
              throws Exception {
            return resp.list;
          }
        })
        .subscribeOn(Schedulers.io());
  }
}
