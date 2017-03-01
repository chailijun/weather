package com.chailijun.mweather.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static final int TYPE_SECONDS = 1;//秒
    public static final int TYPE_MINUTES = 2;//分钟
    public static final int TYPE_HOURS = 3;//小时
    public static final int TYPE_DAYS = 4;//天

    public static final int FORMAT_WEEK = 1;//格式一：今天、明天、周一
    public static final int FORMAT_DATE = 2;//格式二：2/8、2/10

    /**
     * 获取两个时间戳之间的跨度
     * @param type          返回时间跨度的单位（秒、分钟、小时、天）
     * @param timeStamp1
     * @param timeStamp2
     * @return
     */
    public static int getTimeSpan(int type, long timeStamp1, long timeStamp2) {
        long max = Math.max(timeStamp1, timeStamp2);
        long min = Math.min(timeStamp1, timeStamp2);
        switch (type) {
            case TYPE_SECONDS://秒
                return (int) ((max - min) / 1000);
            case TYPE_MINUTES://分钟
                return (int) ((max - min) / 1000 / 60);
            case TYPE_HOURS://小时
                return (int) ((max - min) / 1000 / 60 / 60);
            case TYPE_DAYS://天
                return (int) ((max - min) / 1000 / 60 / 60 / 24);
            default:
                throw new RuntimeException("参数type无对应值");

        }
    }

    /**
     * 获取时间差距
     * @param timeStr
     * @return
     */
    public static String getTimeDistance(String timeStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null){
            long time = date.getTime();
            return getDistanceTime(time,System.currentTimeMillis());
        }else {
            return null;
        }
    }


    /**
     * 广告屏蔽
     * @param showTimeStr
     * @return true:展示
     */
    public static boolean isShowTime(String showTimeStr){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).parse(showTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null){
            long time = date.getTime();
            if (time - System.currentTimeMillis() < 0){
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }

    }

    public static String getDistanceTime(long timeStamp, long currTime) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        String flag;
        if (timeStamp < currTime) {
            diff = currTime - timeStamp;
            flag = "前";
        } else {
            return TimeStamp2Date(timeStamp, "HH:mm");
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (day > 0 && day < 2) {
            return day + "天" + flag;
        } else if (day >= 2) {
            return TimeStamp2Date(timeStamp, "HH:mm");
        }
        if (hour != 0) return hour + "小时" + flag;
        if (min != 0) return min + "分钟" + flag;
        return "刚刚";
    }

    public static String TimeStamp2Date(long timestamp, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * yyyy-MM-dd  -->  今天(xx月xx日)
     * @param type
     * @param timeStr
     * @return
     */
    public static String getDataOrWeek(int type, String timeStr) {
        String time = timeStr;

        //当前日期
        Calendar pre = Calendar.getInstance();
        Date currDate = new Date(System.currentTimeMillis());
        pre.setTime(currDate);

        Calendar cal = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(timeStr);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String week = getWeekTxt(cal.get(Calendar.DAY_OF_WEEK));

            if (type == FORMAT_WEEK) {
                if (diffDay == 0) {
                    //今天
                    time = "今天";

                } else if (diffDay == 1) {
                    //明天
                    time = "明天";

                } else if (diffDay == 2) {
                    //后天
                    time = "后天";

                } else {
                    //其它
                    time = week;
                }
            } else if (type == FORMAT_DATE) {
                time = month + "/" + day;
            } else {
                throw new RuntimeException("参数type无对应值");
            }
        }
        return time;
    }

    /**
     * 获取周几
     * @param week
     * @return
     */
    public static String getWeekTxt(int week) {
//        Calendar cal = Calendar.getInstance();
//        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }
}
