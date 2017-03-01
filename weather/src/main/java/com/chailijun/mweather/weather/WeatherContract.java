package com.chailijun.mweather.weather;

import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.WeatherSet;
import com.chailijun.mweather.base.BasePresenter;
import com.chailijun.mweather.base.BaseView;

public interface WeatherContract {

    interface View extends BaseView<Presenter>{

        void showLoading(boolean active);

        void showWeather(WeatherSet weatherSet);

        void showLoadWeatherError(String msg);

    }

    interface Presenter extends BasePresenter{

        /**
         * 从数据库中加载数据
         * @param selectCity
         */
        void loadWeatherFromDB(SelectCity selectCity);

        /**
         * 网络请求数据
         * @param selectCity
         */
        void loadWeatherFromNet(SelectCity selectCity);

        /**
         * 保存数据至数据库
         */
        void saveDataToDB(WeatherSet weatherSet);
    }
}
