package com.chailijun.mweather.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chailijun.mweather.R;
import com.chailijun.mweather.base.BaseFragment;
import com.chailijun.mweather.dailyforecast.DailyForecastActivity;
import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.WeatherSet;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.GetResUtil;
import com.chailijun.mweather.utils.JsonUtil;
import com.chailijun.mweather.utils.Logger;
import com.chailijun.mweather.utils.SPUtil;
import com.chailijun.mweather.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends BaseFragment implements WeatherContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGUMENT = "argument";

    /**
     * 隐藏“更新成功”
     */
    private static final int HIDE = 1;
    /**
     * 发布时间更新
     */
    private static final int RELEASE_UPDATE = 2;
    /**
     * 隐藏“更新成功”的时间
     */
    private static final long HIDE_TIME = 2000;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayout ll_weather;

    //背景图片
    private LinearLayout ll_picture;

    private TextView tv_release_time;

    private TextView tv_tmp;
    private ImageView weather_icon;
    private TextView tv_cond_txt;
    private TextView tv_wind;

    private TextView tv_sendible_tmp;
    private TextView tv_visibility;
    private TextView tv_pressure;
    private TextView tv_humidity;

    //数据更新时间
    private TextView tv_update;

    //空气质量
    private LinearLayout ll_aqi;
    private ImageView iv_aqi_icon;
    private TextView tv_aqi;

    //一周预报
    private RecyclerView rv_daily_forecast;
    private DailyForecastAdapter mDailyForecastAdapter;

    private WeatherContract.Presenter mPresenter;


    /**
     * 背景图片资源
     */
    private int bgPicRes;

    /**
     * 天气数据上次发布的时间
     */
    private String mReleaseTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE:
                    tv_update.setVisibility(View.GONE);
                    break;
                case RELEASE_UPDATE:

                    if (mReleaseTime != null && tv_release_time != null){
                        if (isAdded()){//解决：Fragment WeatherFragment{4a861140} not attached to Activity
                            tv_release_time.setText(String.format(getString(R.string.release_time),
                                    TimeUtils.getTimeDistance(mReleaseTime)));
                        }

                    }
                    handler.removeMessages(RELEASE_UPDATE);
                    handler.sendEmptyMessageDelayed(RELEASE_UPDATE, 1000*60);
                    break;
                default:
                    break;
            }
        }
    };
    private SelectCity mSelectCity;

    public static WeatherFragment newInstance(SelectCity argument) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT, argument);

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mSelectCity = (SelectCity) bundle.getSerializable(ARGUMENT);
    }

    @Override
    public void onPause() {
        handler.removeCallbacksAndMessages(this);
        super.onPause();
    }

    @Override
    public void onDestroyView() {

        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_weather;
    }

    @Override
    public void initView(View view) {

        mSwipeRefreshLayout = $(view, R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.theme2_ColorScheme);

        ll_picture = (LinearLayout) getActivity().findViewById(R.id.ll_picture);

        ll_weather = $(view, R.id.ll_weather);

        tv_release_time = $(view, R.id.tv_release_time);

        tv_tmp = $(view, R.id.tv_tmp);
        weather_icon = $(view, R.id.weather_icon);
        tv_cond_txt = $(view, R.id.now_cond_text);
        tv_wind = $(view, R.id.tv_wind);

        tv_sendible_tmp = $(view, R.id.tv_sendible_tmp);
        tv_visibility = $(view, R.id.tv_visibility);
        tv_pressure = $(view, R.id.tv_pressure);
        tv_humidity = $(view, R.id.tv_humidity);

        tv_update = $(view, R.id.tv_update);

        ll_aqi = $(view, R.id.ll_aqi);
        iv_aqi_icon = $(view, R.id.iv_aqi_icon);
        tv_aqi = $(view, R.id.tv_aqi);

        rv_daily_forecast = $(view, R.id.rv_daily_forecast);
        rv_daily_forecast.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_daily_forecast.setHasFixedSize(true);

        mDailyForecastAdapter = new DailyForecastAdapter(null);
        rv_daily_forecast.setAdapter(mDailyForecastAdapter);
    }

    @Override
    public void setListener() {
        super.setListener();

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void doBusiness(Context mContext) {
        if (mSelectCity != null)
            mPresenter.loadWeatherFromDB(mSelectCity);
    }

    @Override
    public void onRefresh() {
        if (mSelectCity != null)
            mPresenter.loadWeatherFromNet(mSelectCity);
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void showLoading(boolean active) {
        if (active) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mSwipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void showWeather(final WeatherSet weatherSet) {
        Logger.d(TAG, "load weather success!");

        mSwipeRefreshLayout.setRefreshing(false);

        setWeatherData(weatherSet);
    }

    @Override
    public void showLoadWeatherError(String msg) {
        Logger.e(TAG, "load weather error:" + msg);

        mSwipeRefreshLayout.setRefreshing(false);
        showToastLong(getString(R.string.load_weather_failed));
    }


    private void setWeatherData(final WeatherSet weatherSet) {

        if (weatherSet == null) {
            return;
        }
//        setRandBackgroundPic();
        ll_weather.setVisibility(View.VISIBLE);

        //发布时间
        mReleaseTime = weatherSet.getBasic().getUpdate().getLoc();
//        setReleaseTime();
        tv_release_time.setText(String.format(getString(R.string.release_time),
                TimeUtils.getTimeDistance(mReleaseTime)));
        handler.sendEmptyMessageDelayed(RELEASE_UPDATE, 1000*60);

        //温度
//        Typeface fontFace = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Thin.ttf");
//        tv_tmp.setTypeface(fontFace);
        tv_tmp.setText(weatherSet.getNow().getTmp() + getString(R.string.du));

        WeatherSet.DailyForecastBean dailyForecastBean = weatherSet.getDaily_forecast().get(0);
        //日出时间
        String sr = dailyForecastBean.getAstro().getSr();
        //日落时间
        String ss = dailyForecastBean.getAstro().getSs();
        //设置天气状况图标
        int iconRes = GetResUtil.getWeatherIconRes(weatherSet.getNow().getCond().getCode(), sr, ss);
        if (iconRes != -1) {
            weather_icon.setBackgroundResource(iconRes);
        }

        //天气状况
        tv_cond_txt.setText(weatherSet.getNow().getCond().getTxt());

        //风向、风速
        if (weatherSet.getNow().getWind().getSc().contains(getString(R.string.wind)) &&
                !weatherSet.getNow().getWind().getSc().contains(getString(R.string.ji))) {
            tv_wind.setText(weatherSet.getNow().getWind().getDir() + " "
                    + weatherSet.getNow().getWind().getSc());
        } else {
            tv_wind.setText(weatherSet.getNow().getWind().getDir() + " "
                    + weatherSet.getNow().getWind().getSc() + getString(R.string.ji));
        }

        //体感
        tv_sendible_tmp.setText(weatherSet.getNow().getFl()+getString(R.string.du));

        //能见度
        tv_visibility.setText(weatherSet.getNow().getVis() + getString(R.string.kilometer));

        //气压
        tv_pressure.setText(weatherSet.getNow().getPres() + getString(R.string.pa));

        //湿度
        tv_humidity.setText(weatherSet.getNow().getHum() + getString(R.string.percent));

        //数据更新时间
        int timeSpan = TimeUtils.getTimeSpan(
                TimeUtils.TYPE_MINUTES, System.currentTimeMillis(), weatherSet.getUpdateTime());
        if (timeSpan < 1) {
            tv_update.setVisibility(View.VISIBLE);
            tv_update.setText(getString(R.string.update_success));
            handler.sendEmptyMessageDelayed(HIDE, HIDE_TIME);

        } else {
            tv_update.setText(String.format(getString(R.string.update_before_minutes), timeSpan));
        }

        //空气质量
        WeatherSet.AqiBean aqi = weatherSet.getAqi();
        if (aqi != null) {
            if (aqi.getCity() != null) {
                WeatherSet.AqiBean.CityBean cityAqi = weatherSet.getAqi().getCity();
                if (cityAqi != null) {
                    ll_aqi.setVisibility(View.VISIBLE);
                    iv_aqi_icon.setBackgroundResource(
                            GetResUtil.getAqiRes(weatherSet.getAqi().getCity().getAqi()));
                    tv_aqi.setText(GetResUtil.getAqiRank(weatherSet.getAqi().getCity().getAqi()));
                } else {
                    ll_aqi.setVisibility(View.GONE);
                }
            }
        }

        //一周预报
        mDailyForecastAdapter.setNewData(weatherSet.getDaily_forecast());
        rv_daily_forecast.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<WeatherSet.DailyForecastBean> dailyForecastBeanList =
                        (ArrayList<WeatherSet.DailyForecastBean>) adapter.getData();

                Intent intent = new Intent(getActivity(), DailyForecastActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.DAILY_FORECAST, JsonUtil.toJson(weatherSet));
                bundle.putInt(Constants.POSITION, position);
//                bundle.putInt(Constants.BG_RES,bgPicRes);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * 设置数据发布时间
     */
    private void setReleaseTime() {
        if (mReleaseTime != null && tv_release_time != null) {
            tv_release_time.setText(String.format(getString(R.string.release_time),
                    TimeUtils.getTimeDistance(mReleaseTime)));

            handler.sendEmptyMessageDelayed(RELEASE_UPDATE, 1000*60);
        }
    }



    /**
     * 设置随机背景图片
     */
    /*private void setRandBackgroundPic() {
        bgPicRes = GetResUtil.getBackgroundPic();
        SPUtil.put(getActivity(),Constants.BG_RES, bgPicRes);
        ll_picture.setBackgroundResource(bgPicRes);
    }*/


    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
