package com.pingan.config.biz;

import com.pasc.lib.net.param.BaseParam;
import com.pasc.lib.net.resp.BaseResp;
import com.pasc.lib.net.resp.BaseV2Resp;
import com.pingan.config.biz.model.ConfigResp;
import com.pingan.config.biz.model.ConfigParamNew;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 网络接口定义
 * <p>
 * Created by yintangwen952 on 2017/3/23.
 */
public interface ConfigService {
    @FormUrlEncoded @POST(UrlManager.QUERY_SERVICE_CONFIG_NEW)
    Single<BaseResp<ConfigResp>> queryServiceConfigNew(
        @Field("jsonData") BaseParam<ConfigParamNew> param);
    @POST(UrlManager.QUERY_SERVICE_CONFIG_BASE)
    Single<BaseResp<ConfigResp>> queryServiceConfigBase(@Body ConfigParamNew param);

}
