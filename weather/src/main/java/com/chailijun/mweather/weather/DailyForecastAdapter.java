package com.chailijun.mweather.weather;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chailijun.mweather.R;
import com.chailijun.mweather.data.WeatherSet;
import com.chailijun.mweather.utils.GetResUtil;
import com.chailijun.mweather.utils.TimeUtils;

import java.util.List;


public class DailyForecastAdapter extends BaseQuickAdapter<WeatherSet.DailyForecastBean,BaseViewHolder>{

    public DailyForecastAdapter(List<WeatherSet.DailyForecastBean> data) {
        super(R.layout.daily_forecast_layout_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeatherSet.DailyForecastBean item) {

        helper.setText(R.id.tv_week, TimeUtils.getDataOrWeek(TimeUtils.FORMAT_WEEK,item.getDate()));
        helper.setText(R.id.tv_date,TimeUtils.getDataOrWeek(TimeUtils.FORMAT_DATE,item.getDate()));

        helper.setText(R.id.tv_tmp,item.getTmp().getMax()+"/"+ item.getTmp().getMin()+" ℃");
        //天气状况图标  白天/晚上
        helper.setBackgroundRes(R.id.iv_icon,
                GetResUtil.getWeatherIconRes(item.getCond().getCode_d(),null,item.getAstro().getSs()));
        helper.setText(R.id.tv_txt,item.getCond().getTxt_d()+"/"+item.getCond().getTxt_n());

        helper.setBackgroundRes(R.id.ll_dailyforecast,R.drawable.selector_dailyforecast_bg);
    }
}
