package com.chailijun.mweather.api;

import com.chailijun.mweather.WeatherApp;
import com.chailijun.mweather.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManage {

    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetWorkUtil.isNetWorkAvailable(WeatherApp.getContext())) {
                int maxAge = 2; // 在线缓存在2秒内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .removeHeader("User-Agent")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
    private static ApiManage apiManage;
    private static File httpCacheDirectory = new File(WeatherApp.getContext().getCacheDir(), "weather_Cache");
    private static Cache cache = new Cache(httpCacheDirectory, 1024 * 1024 * 100);//100M
    private final Object monitor = new Object();
    private static final long DEFAULT_TIMEOUT = 5000;
    private static OkHttpClient mOkHttpClient;

    private ApiService apiService;

    private static void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (ApiManage.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .cache(cache)
                            .build();
                }
            }
        }

    }

    public static ApiManage getInstence() {
        if (apiManage == null) {
            synchronized (ApiManage.class) {
                if (apiManage == null) {
                    apiManage = new ApiManage();
                }
            }
        }
        return apiManage;
    }

    public ApiService getApiService() {
        if (apiService == null) {
            synchronized (monitor) {
                if (apiService == null) {
                    initOkHttpClient();
                    apiService = new Retrofit.Builder()
                            .baseUrl("https://free-api.heweather.com/")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(mOkHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
