package com.pingan.config.callback;

/**
 * Created by yintangwen952 on 2018/7/10.
 */

public abstract class ConfigCallback<T> {
    public T t;
    public void onError(String msg){};
    public void onNext(T t){};
    public void onEmpty(){

    }
}
