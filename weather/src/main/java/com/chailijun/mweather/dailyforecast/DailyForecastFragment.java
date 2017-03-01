package com.chailijun.mweather.dailyforecast;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chailijun.mweather.R;
import com.chailijun.mweather.base.BaseFragment;
import com.chailijun.mweather.data.WeatherSet;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.GetResUtil;
import com.chailijun.mweather.utils.Logger;
import com.chailijun.mweather.utils.TimeUtils;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import static com.chailijun.mweather.weather.WeatherFragment.ARGUMENT;


public class DailyForecastFragment extends BaseFragment {


    private WeatherSet.DailyForecastBean mDailyForecastBean;

    private ImageView iv_icon_day;
    private TextView tv_cond_txt;
    private TextView tv_tmp;

    private TextView tv_sunrise;
    private TextView tv_sunset;
    private TextView tv_wind_direction;
    private TextView tv_wind_speed;
    private TextView tv_humidity;

    private TextView tv_pressure;
    private TextView tv_visibility;

    private ViewGroup bannerContainer;
    private BannerView bv;

    public static DailyForecastFragment newInstance(WeatherSet.DailyForecastBean argument) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT, argument);

        DailyForecastFragment fragment = new DailyForecastFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mDailyForecastBean = (WeatherSet.DailyForecastBean) arguments.getSerializable(ARGUMENT);
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_dailyforecast;
    }

    @Override
    public void initView(View view) {
        iv_icon_day = $(view, R.id.iv_icon_day);
        tv_cond_txt = $(view, R.id.tv_cond_txt);

        tv_tmp = $(view, R.id.tv_tmp);
        tv_sunrise = $(view, R.id.tv_sunrise);
        tv_sunset = $(view, R.id.tv_sunset);

        tv_wind_direction = $(view, R.id.tv_wind_direction);
        tv_wind_speed = $(view, R.id.tv_wind_speed);
        tv_humidity = $(view, R.id.tv_humidity);

        tv_pressure = $(view, R.id.tv_pressure);
        tv_visibility = $(view, R.id.tv_visibility);

        bannerContainer= $(view,R.id.bannerContainer);

//        if (TimeUtils.isShowTime(Constants.BLOCKAD_TIME)){
//
//
//        }
        initBanner();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            bv.loadAD();
        }
    }

    private void initBanner() {
        bv = new BannerView(getActivity(), ADSize.BANNER, Constants.APPID, Constants.BannerPos2ID);

        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Logger.i(TAG, "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Logger.i(TAG, "ONBannerReceive");
            }
        });
        bannerContainer.addView(bv);
    }

    @Override
    public void doBusiness(Context mContext) {
        if (mDailyForecastBean != null) {
            iv_icon_day.setBackgroundResource(
                    GetResUtil.getWeatherIconRes(mDailyForecastBean.getCond().getCode_d(),
                            null, mDailyForecastBean.getAstro().getSs()));

            tv_cond_txt.setText(mDailyForecastBean.getCond().getTxt_d());
            tv_tmp.setText(mDailyForecastBean.getTmp().getMax() + getString(R.string.du) + "/"
                    + mDailyForecastBean.getTmp().getMin() + getString(R.string.du));

            tv_sunrise.setText(mDailyForecastBean.getAstro().getSr());
            tv_sunset.setText(mDailyForecastBean.getAstro().getSs());

            //风速、风向
            tv_wind_direction.setText(mDailyForecastBean.getWind().getDir());
            if (mDailyForecastBean.getWind().getSc().contains(getString(R.string.wind)) &&
                    !mDailyForecastBean.getWind().getSc().contains(getString(R.string.ji))) {
                tv_wind_speed.setText(mDailyForecastBean.getWind().getSc());
            } else {
                tv_wind_speed.setText(mDailyForecastBean.getWind().getSc() + getString(R.string.ji));
            }

            tv_humidity.setText(mDailyForecastBean.getHum() + getString(R.string.percent));
            tv_pressure.setText(mDailyForecastBean.getPres() + getString(R.string.pa));
            tv_visibility.setText(mDailyForecastBean.getVis() + getString(R.string.kilometer));
        }
    }

    @Override
    public void widgetClick(View v) {

    }
}
