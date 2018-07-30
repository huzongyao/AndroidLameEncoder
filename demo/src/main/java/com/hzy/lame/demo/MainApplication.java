package com.hzy.lame.demo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by huzongyao on 2018/7/30.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
