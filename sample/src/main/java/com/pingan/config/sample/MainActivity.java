package com.pingan.config.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pingan.config.ConfigProviderHelper;
import com.pingan.config.ConfigRepository;
import com.pingan.config.callback.ConfigCallback;
import com.pingan.config.module.ConfigItem;
import com.pingan.dynamicconfig.sample.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] configs = new String[]{"smt.sz.life", "smt.sz.government", "smt.sz.homepage", "smt.sz.mine"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void multipleRequest(View view){
        //多线程添加测试1
        for (int i = 0; i < configs.length; i++) {
            new ThreadConfig(configs[i]).run();
        }
        //多线程添加测试2
//        run(configs[2]);
        //数据库查询测试
//        ConfigItem item = ConfigRepository.getInstance().getDBModel("smt.sz.homepage");
        //数据库批量查询测试
//        List<ConfigItem> configItems = ConfigProviderHelper.getInstance().queryConfigItemList();
//        if (configItems != null)
//            configItems.size();
    }

    private void requestConfig(String configId) {
        ConfigRepository.getInstance().getNetModel(configId, "0.0", null,
                new ConfigCallback<String>() {
                    @Override
                    public void onNext(String baseResp) {
                        super.onNext(baseResp);
                        ((TextView) findViewById(R.id.tv_content)).setText(baseResp);
                    }

                    @Override public void onError(String msg) {
                        super.onError(msg);
                        Log.e("msg",""+msg);
                    }
                });
    }

    class ThreadConfig extends Thread {
        private String config;
        public ThreadConfig(String config) {
            this.config = config;
        }

        @Override
        public void run() {
            super.run();
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            requestConfig(config);
        }
    }

}


