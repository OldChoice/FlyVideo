package com.myq.flyvideo;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.multidex.MultiDex;

import com.danikula.videocache.HttpProxyCacheServer;

import gr.free.grfastuitils.GrUtilsInstance;

/**
 * 能不能干掉这个类，非常想干掉这个类，可以不可以移到MainActivity
 * Created by Nathen
 * On 2015/12/01 11:29
 */
public class MyApplication extends Application {

    private HttpProxyCacheServer proxy;
    private static MyApplication instance;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        GrUtilsInstance.getmContext(getInstance());

    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获取versionname
     */
    public static String getVersionName() {
        try {
            return MyApplication.getInstance().getPackageManager().getPackageInfo(MyApplication.getInstance().getPackageName(),
                    0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "00.00.00.0";
        }
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
