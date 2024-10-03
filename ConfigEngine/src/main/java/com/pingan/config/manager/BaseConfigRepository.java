package com.pingan.config.manager;

import com.google.common.base.Optional;
import com.pingan.config.ConfigRepository;
import com.pingan.config.callback.ConfigCallback;

import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanglihou519 on 2018/7/26.
 */

public abstract class BaseConfigRepository<T> {
    private final String configId;

    public BaseConfigRepository(String configId) {
        this.configId = configId;
    }

    public Single<T> getConfigFromLocal(final Class<T> clz) {
        return Single.defer(new Callable<SingleSource<T>>() {
                    @Override public SingleSource<T> call() throws Exception {
                        return Single.just(ConfigRepository.getInstance().getLocalModel(configId, clz));
                    }
                })
                .onErrorReturnItem(ConfigRepository.getInstance().getFileModel(configId, clz))
                .subscribeOn(Schedulers.io());
    }

    public Single<Optional<T>> getConfigFromCloud(final Class<T> clz) {
        return Single.create(new SingleOnSubscribe<Optional<T>>() {
            @Override public void subscribe(final SingleEmitter<Optional<T>> emitter) throws Exception {
                ConfigRepository.getInstance()
                        .getNetModel(configId,
                                ConfigRepository.getInstance().getConfigVersion(configId), clz,
                                new ConfigCallback<T>() {

                                    @Override public void onError(String msg) {
                                        emitter.onError(new IllegalAccessError(msg));
                                    }

                                    @Override public void onNext(T o) {
                                        emitter.onSuccess(Optional.fromNullable(o));
                                    }

                                    @Override public void onEmpty() {
                                        emitter.onSuccess(Optional.<T>absent());
                                    }
                                });
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<T> getConfigFromAssets(final Class<T> clz) {
        return Single.defer(new Callable<SingleSource<T>>() {
            @Override public SingleSource<T> call() throws Exception {
                return Single.just(ConfigRepository.getInstance().getFileModel(configId, clz));
            }
        }).subscribeOn(Schedulers.io());
    }
}
