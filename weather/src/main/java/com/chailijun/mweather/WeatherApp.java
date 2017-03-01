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
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


public class WeatherApp extends Application {

    //    //友盟分享，各个平台的配置
//    {
//        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//    }
    {
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        PlatformConfig.setAlipay("2015111700822536");
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        PlatformConfig.setPinterest("1439206");
        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
        PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
        PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");

    }


    private static Context mContext;
    public static WeatherApp mInstances;

    public LocationService locationService;
    public Vibrator mVibrator;

    private SQLiteDatabase mDatabase;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        Config.DEBUG = true;
        UMShareAPI.get(this);

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
