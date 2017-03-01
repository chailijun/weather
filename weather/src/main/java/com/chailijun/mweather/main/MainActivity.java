package com.chailijun.mweather.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chailijun.mweather.R;
import com.chailijun.mweather.WeatherApp;
import com.chailijun.mweather.base.BaseActivity;
import com.chailijun.mweather.cityadd.CityAddActivity;
import com.chailijun.mweather.citymanager.CityManagerActivity;
import com.chailijun.mweather.customview.MainIndicator;
import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.gen.SelectCityDao;
import com.chailijun.mweather.setting.SettingActivity;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.Logger;
import com.chailijun.mweather.utils.SPUtil;
import com.chailijun.mweather.weather.WeatherFragment;
import com.chailijun.mweather.weather.WeatherPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int REQUSET = 1;
    private long firstTime = 0;

    private ImageView mAddCity;
    private ImageView mShare;

    private ImageView mSetting;
    private ImageView iv_location;
    private TextView tv_city;
    private TextView tv_street;//定位城市显示街道名称
    private TextView tv_leader;//上一级（市/国家）

    private MainIndicator mIndicator;

    private ViewPager mViewpager;

    private PagerAdapter mAdapter;

    private List<Fragment> mFragments;

    private SelectCityDao mSelectCityDao;

    private List<SelectCity> mSelectCityList;
    private String mCityId;
//    private boolean isLocation;

    private void getCityId() {
        Intent intent = getIntent();
        mCityId = intent.getStringExtra(Constants.CITY_ID);
//        isLocation = intent.getBooleanExtra(Constants.IS_LOCATION,false);
        Logger.d(TAG, "cityId:" + mCityId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(TAG, "onNewIntent()");
        getCityId();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUSET && resultCode == RESULT_OK) {
//            int index = data.getIntExtra(Constants.CURR_INDEX, 0);
//            Logger.e(TAG, "index:" + index);
//            mViewpager.setCurrentItem(index);
//        }
//    }

    @Override
    public void initParms(Bundle parms) {
        getCityId();

        if (mSelectCityDao == null) {
            mSelectCityDao = WeatherApp.getInstances().getmDaoSession().getSelectCityDao();
        }

        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mSelectCityList = mSelectCityDao.queryBuilder().build().list();
        if (mSelectCityList != null && mSelectCityList.size() > 0) {
            for (int i = 0; i < mSelectCityList.size(); i++) {
                WeatherFragment weatherFragment
                        = WeatherFragment.newInstance(mSelectCityList.get(i));
                WeatherPresenter presenter = new WeatherPresenter(weatherFragment);
                mFragments.add(weatherFragment);
            }
        } else {
            //没有添加任何城市则进入添加城市画面
            startActivity(new Intent(this, CityAddActivity.class));
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        mAddCity = $(R.id.iv_cityadd);
        mShare = $(R.id.iv_share);
        mSetting = $(R.id.iv_setting);

        iv_location = $(R.id.iv_location);
        tv_city = $(R.id.tv_city);
        tv_street = $(R.id.tv_street);
        tv_leader = $(R.id.tv_leader);

        //初始化指示器
        mIndicator = $(R.id.indicator);
        mIndicator.setIndicatorCount(mFragments.size());

        //初始化ViewPager
        mViewpager = $(R.id.viewpager);
        mAdapter = new PagerAdapter(getSupportFragmentManager(), mFragments);
        mViewpager.setAdapter(mAdapter);

        //设置ViewPager的index
        int currIndex = 0;
        if (mCityId != null) {
            //新增城市时index作相应改变
            if (mSelectCityList != null && mSelectCityList.size() > 0) {
                for (int i = 0; i < mSelectCityList.size(); i++) {
                    if (mSelectCityList.get(i).getCityId().equalsIgnoreCase(mCityId)) {
                        currIndex = i;
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < mSelectCityList.size(); i++) {
                if (mSelectCityList.get(i).getIsShow()) {
                    currIndex = i;
                }
            }
        }
        mViewpager.setCurrentItem(currIndex);
        setIsShow(currIndex);

        setCityName(currIndex);
    }

    /**
     * 设置城市名称
     * @param currIndex
     */
    private void setCityName(int currIndex) {
        if (mSelectCityList != null && mSelectCityList.size() > 0) {
            tv_city.setText(mSelectCityList.get(currIndex).getCityZh());

            //国外城市标注所属国家
            if (!mSelectCityList.get(currIndex).getCityId().startsWith("CN")) {
                tv_leader.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mSelectCityList.get(currIndex).getProvinceZh())) {
                    tv_leader.setText(" - " + mSelectCityList.get(currIndex).getProvinceZh());
                }
            } else {
                tv_leader.setVisibility(View.GONE);
            }

            //定位城市显示街道
            if (mSelectCityList.get(currIndex).getIsLocation()){
                tv_street.setVisibility(View.VISIBLE);
                String street = (String) SPUtil.get(MainActivity.this,Constants.LOCATION_STREET,"");
                if (!TextUtils.isEmpty(street)){
                    tv_street.setText(street);
                }
            }else {
                tv_street.setVisibility(View.GONE);
            }

            //定位城市显示定位标识
            if(mSelectCityList.get(currIndex).getIsLocation()){
                iv_location.setVisibility(View.VISIBLE);
            }else {
                iv_location.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 设置已选择城市的isShow字段，并更新
     * @param currIndex
     */
    private void setIsShow(int currIndex) {

        for (int i = 0; i < mSelectCityList.size(); i++) {

            if (mSelectCityList.get(i).getIsShow() && currIndex != i) {
                //默认展示--->不展示
                mSelectCityList.get(i).setIsShow(false);
                mSelectCityDao.update(mSelectCityList.get(i));
            } else if (!mSelectCityList.get(i).getIsShow() && currIndex == i) {
                //不展示--->默认展示
                mSelectCityList.get(i).setIsShow(true);
                mSelectCityDao.update(mSelectCityList.get(i));
            }
        }
    }

    @Override
    public void setListener() {
        mAddCity.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mSetting.setOnClickListener(this);

        //viewPager页面改变监听
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                setIsShow(position);

                setCityName(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cityadd:
                Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
//                startActivityForResult(intent, REQUSET);
                startActivity(intent);
//                overridePendingTransition(R.anim.in_from_left,R.anim.fade);
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_setting:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onBackPressed() {

        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            showToast(getString(R.string.leave_app)+getString(R.string.app_name));
            firstTime = secondTime;//更新firstTime
        }else {
            super.onBackPressed();
        }
    }
}
