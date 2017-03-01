package com.chailijun.mweather.utils;


import com.chailijun.mweather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 获取天气状况相关的图标资源工具类
 */

public class GetResUtil {

    /**
     * 选择天气图标
     * @param weatherCode
     * @param sr
     * @param ss
     * @return
     */
    public static int getWeatherIconRes(String weatherCode, String sr, String ss) {
        int res = -1;
        if (weatherCode == null) {
            res = R.drawable.w999_na;
            return res;
        }
        int code = Integer.parseInt(weatherCode);
        switch (code) {
            //阴、多云
            case 101:
            case 102:
            case 104:
                res = R.drawable.w101_cloudy;
                break;
            //有风
            case 200:
            case 202:
            case 203:
            case 204:
                res = R.drawable.w200_windy;
                break;
            //平静
            case 201:
                res = R.drawable.w201_calm;
                break;
            //大风
            case 205:
            case 206:
            case 207:
                res = R.drawable.w205_strong_breeze;
                break;
            //强风
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
                res = R.drawable.w208_strong_gale;
                break;
            //飓风
            case 302:
            case 303:
                res = R.drawable.w302_thundershower;
                break;
            //雷阵雨伴有冰雹
            case 304:
                res = R.drawable.w304_hail;
                break;
            //小雨
            case 305:
            case 309:
                res = R.drawable.w305_light_rain;
                break;
            //中雨
            case 306:
                res = R.drawable.w306_moderraterain;
                break;
            //大雨
            case 307:
            case 308:
            case 310:
            case 311:
            case 312:
                res = R.drawable.w307_heavyrain;
                break;
            //冻雨
            case 313:
                res = R.drawable.w313_freezing_rain;
                break;
            //小雪
            case 400:
            case 401:
                res = R.drawable.w400_snow;
                break;
            //大雪
            case 402:
            case 403:
                res = R.drawable.w402_heavysnow;
                break;
            //雨夹雪
            case 404:
            case 405:
            case 406:
                res = R.drawable.w404_rainsnow;
                break;
            //雾
            case 500:
            case 501:
                res = R.drawable.w500_fog;
                break;
            //霾
            case 502:
                res = R.drawable.w502_haze;
                break;
            //扬沙
            case 503:
                res = R.drawable.w503_sand;
                break;
            //浮尘
            case 504:
                res = R.drawable.w504_dust;
                break;
            //沙尘暴
            case 507:
            case 508:
                res = R.drawable.w507_sandstorm;
                break;
            //热
            case 900:
                res = R.drawable.w900_hot;
                break;
            //冷
            case 901:
                res = R.drawable.w901_cold;
                break;

            default:
                if (isDayOrNight(sr, ss)) {
                    //白天
                    switch (code) {
                        case 100:
                            res = R.drawable.w100_sunny_day;
                            break;
                        case 103:
                            res = R.drawable.w103_partly_cloudy_day;
                            break;
                        case 300:
                        case 301:
                            res = R.drawable.w300_shower_rain_day;
                            break;
                        case 407:
                            res = R.drawable.w407_snow_flurry_day;
                            break;
                        default:
                            res = R.drawable.w999_na;
                            break;
                    }
                } else {
                    //晚上
                    switch (code) {
                        case 100:
                            res = R.drawable.w100_sunny_night;
                            break;
                        case 103:
                            res = R.drawable.w103_partly_cloudy_day_night;
                            break;
                        case 300:
                        case 301:
                            res = R.drawable.w300_shower_rain_night;
                            break;
                        case 407:
                            res = R.drawable.w407_snow_flurry_night;
                            break;
                        default:
                            res = R.drawable.w999_na;
                            break;
                    }
                }
                break;
        }
        return res;
    }

    /**
     * 选择天气图标
     * @param weatherCode
     * @param sr
     * @param ss
     * @return
     */
    public static int getWeatherIconResBlack(String weatherCode, String sr, String ss) {
        int res = -1;
        if (weatherCode == null) {
            res = R.drawable.w999_na_black;
            return res;
        }
        int code = Integer.parseInt(weatherCode);
        switch (code) {
            //阴、多云
            case 101:
            case 102:
            case 104:
                res = R.drawable.w101_cloudy_black;
                break;
            //有风
            case 200:
            case 202:
            case 203:
            case 204:
                res = R.drawable.w200_windy_black;
                break;
            //平静
            case 201:
                res = R.drawable.w201_calm_black;
                break;
            //大风
            case 205:
            case 206:
            case 207:
                res = R.drawable.w205_strong_breeze_black;
                break;
            //强风
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
                res = R.drawable.w208_strong_gale_black;
                break;
            //飓风
            case 302:
            case 303:
                res = R.drawable.w302_thundershower_black;
                break;
            //雷阵雨伴有冰雹
            case 304:
                res = R.drawable.w304_hail_black;
                break;
            //小雨
            case 305:
            case 309:
                res = R.drawable.w305_light_rain_black;
                break;
            //中雨
            case 306:
                res = R.drawable.w306_moderraterain_black;
                break;
            //大雨
            case 307:
            case 308:
            case 310:
            case 311:
            case 312:
                res = R.drawable.w307_heavyrain_black;
                break;
            //冻雨
            case 313:
                res = R.drawable.w313_freezing_rain_black;
                break;
            //小雪
            case 400:
            case 401:
                res = R.drawable.w400_snow_black;
                break;
            //大雪
            case 402:
            case 403:
                res = R.drawable.w402_heavysnow_black;
                break;
            //雨夹雪
            case 404:
            case 405:
            case 406:
                res = R.drawable.w404_rainsnow_black;
                break;
            //雾
            case 500:
            case 501:
                res = R.drawable.w500_fog_black;
                break;
            //霾
            case 502:
                res = R.drawable.w502_haze_black;
                break;
            //扬沙
            case 503:
                res = R.drawable.w503_sand_black;
                break;
            //浮尘
            case 504:
                res = R.drawable.w504_dust_black;
                break;
            //沙尘暴
            case 507:
            case 508:
                res = R.drawable.w507_sandstorm_black;
                break;
            //热
            case 900:
                res = R.drawable.w900_hot_black;
                break;
            //冷
            case 901:
                res = R.drawable.w901_cold_black;
                break;

            default:
                if (isDayOrNight(sr, ss)) {
                    //白天
                    switch (code) {
                        case 100:
                            res = R.drawable.w100_sunny_day_black;
                            break;
                        case 103:
                            res = R.drawable.w103_partly_cloudy_day_black;
                            break;
                        case 300:
                        case 301:
                            res = R.drawable.w300_shower_rain_day_black;
                            break;
                        case 407:
                            res = R.drawable.w407_snow_flurry_day_black;
                            break;
                        default:
                            res = R.drawable.w999_na_black;
                            break;
                    }
                } else {
                    //晚上
                    switch (code) {
                        case 100:
                            res = R.drawable.w100_sunny_night_black;
                            break;
                        case 103:
                            res = R.drawable.w103_partly_cloudy_day_night_black;
                            break;
                        case 300:
                        case 301:
                            res = R.drawable.w300_shower_rain_night_black;
                            break;
                        case 407:
                            res = R.drawable.w407_snow_flurry_night_black;
                            break;
                        default:
                            res = R.drawable.w999_na_black;
                            break;
                    }
                }
                break;
        }
        return res;
    }


    /**
     * 获取空气质量图标
     * @param aqi
     * @return
     */
    public static int getAqiRes(String aqi) {

        int res;
        int aqiInt = Integer.parseInt(aqi);
        if (aqiInt <= 50) {                       //优
            res = R.drawable.ic_pm25_01;
        } else if (aqiInt > 50 && aqiInt <= 100) {  //良
            res = R.drawable.ic_pm25_02;
        } else if (aqiInt > 100 && aqiInt <= 150) {//轻度污染
            res = R.drawable.ic_pm25_03;
        } else if (aqiInt > 150 && aqiInt <= 200) {//中度污染
            res = R.drawable.ic_pm25_04;
        } else if (aqiInt > 200 && aqiInt <= 300) {//重度污染
            res = R.drawable.ic_pm25_05;
        } else {
            res = R.drawable.ic_pm25_06;      //严重污染
        }

        return res;
    }

    /**
     * 获取空气质量图标
     * @param aqi
     * @return
     */
    public static String getAqiRank(String aqi) {

        String rank = null;
        int aqiInt = Integer.parseInt(aqi);
        if (aqiInt <= 50) {                       //优
            rank = aqi + " 优";
        } else if (aqiInt > 50 && aqiInt <= 100) {  //良
            rank = aqi + " 良";
        } else if (aqiInt > 100 && aqiInt <= 150) {//轻度污染
            rank = aqi + " 轻度";
        } else if (aqiInt > 150 && aqiInt <= 200) {//中度污染
            rank = aqi + " 中度";
        } else if (aqiInt > 200 && aqiInt <= 300) {//重度污染
            rank = aqi + " 重度";
        } else {
            rank = aqi + " 严重";      //严重污染
        }
        return rank;
    }

    /**
     * 设置背景图片
     */
    public static int getBackgroundPic() {

        int[] picArray = {
                R.drawable.backimg_random1, R.drawable.backimg_random2,
                R.drawable.backimg_random4, R.drawable.backimg_random5,
                R.drawable.backimg_random7, R.drawable.backimg_random8, R.drawable.backimg_random9,
                R.drawable.backimg_random10, R.drawable.backimg_random11, R.drawable.backimg_random12,
                R.drawable.backimg_random13, R.drawable.backimg_random14};

        return picArray[new Random().nextInt(picArray.length)];

    }

    /**
     * 根据日出日落时间判断当前属于白天还是晚上
     *
     * @param sr:日出时间的字符串,如"05:32"
     * @param ss:日落时间的字符串,如"19:02"
     * @return :白天返回true，晚上返回false
     */
    public static boolean isDayOrNight(String sr, String ss) {
        if (sr == null && ss != null) {
            return true;//日出为空，则为白天
        }
        if (sr != null && ss == null) {
            return false;//日落为空，则为晚上
        }

        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.CHINA);//24小时制
        Calendar c_now = Calendar.getInstance();
        Calendar c_sr = Calendar.getInstance();
        Calendar c_ss = Calendar.getInstance();

        try {
            String currDateStr = df.format(new Date());
            Date currDate = df.parse(currDateStr);//当前时间
            Date date1 = df.parse(sr);
            Date date2 = df.parse(ss);

            c_now.setTime(currDate);//当前时间
            c_sr.setTime(date1);//日出时间
            c_ss.setTime(date2);//日落时间
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (c_now.compareTo(c_sr) < 0 || c_now.compareTo(c_ss) >= 0 && c_sr.compareTo(c_ss) < 0) {
            return false;//晚上
        } else {
            return true;//白天
        }
    }


}
