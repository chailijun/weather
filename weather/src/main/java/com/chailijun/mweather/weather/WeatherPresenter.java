package com.chailijun.mweather.weather;

import android.support.annotation.NonNull;

import com.chailijun.mweather.WeatherApp;
import com.chailijun.mweather.api.ApiManage;
import com.chailijun.mweather.data.WeatherInfo;
import com.chailijun.mweather.data.WeatherSet;
import com.chailijun.mweather.data.gen.SelectCityDao;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.JsonUtil;
import com.chailijun.mweather.utils.Logger;
import com.chailijun.mweather.utils.TimeUtils;
import com.chailijun.mweather.data.SelectCity;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class WeatherPresenter implements WeatherContract.Presenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();
    @NonNull
    private WeatherContract.View mWeatherView;

    @NonNull
    private CompositeSubscription mSubscriptions;
    private SelectCityDao mSelectCityDao;

    public WeatherPresenter(@NonNull WeatherContract.View weatherView) {
        mWeatherView = weatherView;

        mSubscriptions = new CompositeSubscription();
        mWeatherView.setPresenter(this);

        mSelectCityDao = WeatherApp.getInstances().getmDaoSession().getSelectCityDao();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadWeatherFromDB(SelectCity city) {

        SelectCity selectCity = mSelectCityDao.queryBuilder()
                .where(SelectCityDao.Properties.CityId.eq(city.getCityId())).unique();


        if (selectCity != null){
            int timeSpan = TimeUtils.getTimeSpan(TimeUtils.TYPE_MINUTES,
                            System.currentTimeMillis(), selectCity.getUpdateTime());

//            Logger.d(TAG,"数据未过期，直接从数据库中取");
            WeatherSet weatherSet =
                    JsonUtil.fromJson(selectCity.getWeatherSet(), WeatherSet.class);
            mWeatherView.showWeather(weatherSet);

            if (timeSpan > Constants.UPDATE_FREQUENCY) {
                //数据过期(或者新添加的城市)，重新请求
                Logger.d(TAG,"数据已过期，重新请求");
                loadWeatherFromNet(selectCity);
            }/*else {

                Logger.d(TAG,"数据未过期，直接从数据库中取");
                WeatherSet weatherSet =
                        JsonUtil.fromJson(selectCity.getWeatherSet(), WeatherSet.class);

                mWeatherView.showWeather(weatherSet);
            }*/
        }else {
            loadWeatherFromNet(city);
        }
    }

    @Override
    public void loadWeatherFromNet(final SelectCity selectCity) {
        mWeatherView.showLoading(true);
        Subscription subscription = ApiManage.getInstence()
                .getApiService()
                .getWeatherSet(selectCity.getCityId(), Constants.KEY)
                .map(new Func1<WeatherInfo<WeatherSet>, List<WeatherSet>>() {
                    @Override
                    public List<WeatherSet> call(WeatherInfo<WeatherSet> info) {
                        if (info.getHeWeather5() != null && info.getHeWeather5().size() > 0) {
                            return info.getHeWeather5();
                        }
                        throw new RuntimeException("请求的天气数据为空！");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<WeatherSet>>() {
                    @Override
                    public void onCompleted() {
                        mWeatherView.showLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mWeatherView.showLoading(false);
                        mWeatherView.showLoadWeatherError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<WeatherSet> weatherSets) {

                        if (weatherSets != null && weatherSets.size() == 1) {

                            WeatherSet weatherSet = weatherSets.get(0);
                            weatherSet.setUpdateTime(System.currentTimeMillis());
                            saveDataToDB(weatherSet);

                            mWeatherView.showWeather(weatherSet);

                        } else if (weatherSets != null && weatherSets.size() > 1) {
                            mWeatherView.showLoadWeatherError("id为" + selectCity.getCityId() + "的城市不止一个");
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void saveDataToDB(WeatherSet weatherSet) {
        List<SelectCity> list = mSelectCityDao.queryBuilder()
                .where(SelectCityDao.Properties.CityId.eq(weatherSet.getBasic().getId())).list();

        if (list != null && list.size() > 0) {
            SelectCity selectCity = list.get(0);

            //添加更新时间
            selectCity.setUpdateTime(System.currentTimeMillis());

            selectCity.setWeatherSet(JsonUtil.toJson(weatherSet));
            Logger.d("TAG", "Json:" + JsonUtil.toJson(weatherSet));

            mSelectCityDao.update(selectCity);
        }
    }
}
