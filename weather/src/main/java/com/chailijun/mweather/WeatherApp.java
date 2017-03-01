package com.chailijun.mweather;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.chailijun.mweather.data.gen.DaoMaster;
import com.chailijun.mweather.data.gen.DaoSession;
import com.chailijun.mweather.location.LocationService;
import com.umeng.analytics.MobclickAgent;


public class WeatherApp extends Application {

//    //友盟分享，各个平台的配置
//    {
//        PlatformConfig.setWeixin();
//        PlatformConfig.setSinaWeibo();
//        PlatformConfig.setQQZone();
//    }

    private static Context mContext;
    public static WeatherApp mInstances;

    public LocationService locationService;
    public Vibrator mVibrator;

    private SQLiteDatabase mDatabase;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();

//        UMShareAPI.get(this);

        //开启友盟集成测试
        MobclickAgent.setDebugMode(true);

        mContext = getApplicationContext();
        mInstances = this;

        initDatabase();

        initLocationSDK();
    }

    /**
     * 初始化百度定位sdk
     */
    private void initLocationSDK() {
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }

    public static WeatherApp getInstances() {
        return mInstances;
    }

    /**
     * 初始化数据库
     */
    private void initDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "city.db", null);
        mDatabase = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getmDatabase() {
        return mDatabase;
    }

}
