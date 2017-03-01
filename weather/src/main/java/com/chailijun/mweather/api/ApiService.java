package com.chailijun.mweather.api;

import com.chailijun.mweather.data.WeatherInfo;
import com.chailijun.mweather.data.WeatherSet;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface ApiService {

    @GET("/v5/weather")
    Observable<WeatherInfo<WeatherSet>> getWeatherSet(@Query("city") String city,
                                                      @Query("key") String key);
}
