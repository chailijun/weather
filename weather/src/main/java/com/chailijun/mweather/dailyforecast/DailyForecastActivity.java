package com.chailijun.mweather.dailyforecast;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chailijun.mweather.R;
import com.chailijun.mweather.base.BaseActivity;
import com.chailijun.mweather.data.WeatherSet;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.JsonUtil;
import com.chailijun.mweather.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;


public class DailyForecastActivity extends BaseActivity {

    private ImageView iv_title_back;

    private TextView tv_city;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MyFragmentStatePagerAdapter mAdapter;
    private List<Fragment> mFragments;

    private WeatherSet mWeatherSet;
    private List<WeatherSet.DailyForecastBean> mForecastBeanList;

    private int mPosition;
//    private int bgRes;


    @Override
    public void initParms(Bundle bundle) {
        String dailyForecast = (String) bundle.get(Constants.DAILY_FORECAST);
        mPosition = bundle.getInt(Constants.POSITION);
//        bgRes = bundle.getInt(Constants.BG_RES);
//        bgRes = SPUtil.getInt(Constants.BG_RES);

        mWeatherSet = JsonUtil.fromJson(dailyForecast, WeatherSet.class);

        mFragments = new ArrayList<>();
        mForecastBeanList = mWeatherSet.getDaily_forecast();
        if (mForecastBeanList != null && mForecastBeanList.size() > 0){
            for (int i = 0; i < mForecastBeanList.size(); i++) {
                mFragments.add(DailyForecastFragment.newInstance(mForecastBeanList.get(i)));
            }
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_daily_forecast;
    }

    @Override
    public void initView(View view) {
        iv_title_back = $(R.id.iv_title_back);

        tv_city = $(R.id.tv_city);

        mTabLayout = $(R.id.tabs);
        mViewPager = $(R.id.viewpager);

        mAdapter = new MyFragmentStatePagerAdapter(
                getSupportFragmentManager(),mFragments,getTabTxt());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(mPosition);

//        setBlurBackground(bgRes);
    }

    private List<String> getTabTxt() {
        List<String> titles = new ArrayList<>();
        if (mForecastBeanList != null && mForecastBeanList.size() > 0){
            for (int i = 0; i < mForecastBeanList.size(); i++) {
                String date = mForecastBeanList.get(i).getDate();
                String week = TimeUtils.getDataOrWeek(TimeUtils.FORMAT_WEEK, date);
                String data1 = TimeUtils.getDataOrWeek(TimeUtils.FORMAT_DATE, date);

//                titles.add(week+"\n"+data1);
                titles.add(week+data1);
            }
        }
        return titles;
    }


    @Override
    public void doBusiness(Context mContext) {


//        ll_dailyforecast.setBackgroundResource(GetResUtil.getBackgroundPic());


        if (mWeatherSet != null){
            tv_city.setText(mWeatherSet.getBasic().getCity());
        }
    }

//    /**
//     * 设置毛玻璃背景
//     * @param bgRes
//     */
//    private void setBlurBackground(int bgRes) {
//        int scaleRatio = 20;
//        int blurRadius = 8;
//
//        Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), bgRes);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap,
//                originBitmap.getWidth() / scaleRatio,
//                originBitmap.getHeight() / scaleRatio,
//                false);
//        Bitmap blurBitmap = FastBlurUtil.doBlur(scaledBitmap, blurRadius, true);
//        iv_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        iv_bg.setImageBitmap(blurBitmap);
//        RelativeLayout ll = new RelativeLayout(this);
//    }


    @Override
    public void setListener() {
        iv_title_back.setOnClickListener(this);

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            default:
                break;
        }
    }
}
