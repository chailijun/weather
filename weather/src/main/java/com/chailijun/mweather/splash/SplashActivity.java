package com.chailijun.mweather.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chailijun.mweather.R;
import com.chailijun.mweather.base.BaseActivity;
import com.chailijun.mweather.main.MainActivity;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.GetSignature;
import com.chailijun.mweather.utils.Logger;
import com.chailijun.mweather.utils.SPUtil;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;


public class SplashActivity extends BaseActivity implements SplashADListener {


    private MyHandler handler;
    private boolean isStartMain = false;
    private boolean isFirst = false;

    private SplashAD splashAD;
    private ViewGroup container;
    private TextView skipView;
    private ImageView splashHolder;
    private static final String SKIP_TEXT = "跳过\n%d";

    public boolean canJump = false;

    @Override
    public void initParms(Bundle parms) {
        setAllowFullScreen(true);
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(View view) {
        container = $(R.id.splash_container);
        skipView = $(R.id.skip_view);
        splashHolder = $(R.id.splash_holder);

        handler = new MyHandler(this);
    }


    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doBusiness(Context mContext) {
        isFirst = (boolean) SPUtil.get(SplashActivity.this, Constants.IS_FIRST, true);

        if (isFirst){
            imporDatabase();
            SPUtil.put(SplashActivity.this,Constants.IS_FIRST,false);
        }

        fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 0);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
//            fetchSplashAD(this, container, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
//        }else {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    finish();
//                }
//            }, 1000);
//        }
    }

    private void fetchSplashAD(Activity activity,
                               ViewGroup adContainer,
                               View skipContainer,
                               String appId,
                               String posId,
                               SplashADListener adListener,
                               int fetchDelay) {
        splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
    }

    @Override
    public void onADPresent() {
        Logger.i(TAG, "SplashADPresent");
        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
    }

    @Override
    public void onADClicked() {
        Logger.i(TAG, "SplashADClicked");
    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onADTick(long millisUntilFinished) {
        Logger.i(TAG, "SplashADTick " + millisUntilFinished + "ms");
        skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
    }

    @Override
    public void onADDismissed() {
        Logger.i(TAG, "SplashADDismissed");
        next();
    }

    @Override
    public void onNoAD(int errorCode) {
        Logger.i(TAG, "LoadSplashADFail, eCode=" + errorCode);
        /** 如果加载广告失败，则直接跳转 */
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
//        }, 1000);
        handler.sendEmptyMessageDelayed(1,1000);
//        this.startActivity(new Intent(this, MainActivity.class));
//        this.finish();
    }


    private void next() {
        if (canJump) {
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            canJump = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 直接导入数据库文件
     */
    private void imporDatabase() {
        //存放数据库的目录
        String dirPath = "/data/data/" + getPackageName() + "/databases";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        //数据库文件
        File file = new File(dir, "city.db");

        InputStream is = null;
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            //加载需要导入的数据库
            is = this.getApplicationContext().getResources().openRawResource(R.raw.city);
            fos = new FileOutputStream(file);
            byte[] buffere = new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class MyHandler extends Handler{

        WeakReference<Activity> mWeakReference;

        public MyHandler(Activity activity) {
            mWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Activity activity = mWeakReference.get();

            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();

        }
    }
}
