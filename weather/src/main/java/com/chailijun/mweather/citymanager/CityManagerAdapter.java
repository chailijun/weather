package com.chailijun.mweather.citymanager;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chailijun.mweather.R;
import com.chailijun.mweather.utils.JsonUtil;
import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.WeatherSet;
import com.chailijun.mweather.utils.GetResUtil;

import java.util.List;

public class CityManagerAdapter extends BaseItemDraggableAdapter<SelectCity,BaseViewHolder> {


    public CityManagerAdapter(List<SelectCity> data) {
        super(R.layout.activity_city_manager_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCity item) {

        WeatherSet weatherSet = JsonUtil.fromJson(item.getWeatherSet(), WeatherSet.class);

        if (weatherSet == null){
            return;
        }

        //定位图标
        if (item.getIsLocation()){
            helper.getView(R.id.iv_location).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.iv_location).setVisibility(View.GONE);
        }

        //城市
//        helper.setText(R.id.tv_city,weatherSet.getBasic().getCity());
        helper.setText(R.id.tv_city,item.getCityZh());

        //最高/最低温度
        WeatherSet.DailyForecastBean dailyForecastBean = weatherSet.getDaily_forecast().get(0);
        helper.setText(R.id.tv_temp,dailyForecastBean.getTmp().getMax()+"/"+
                dailyForecastBean.getTmp().getMin()+" ℃");

        //天气状况图标
        helper.setBackgroundRes(R.id.iv_weather,
                GetResUtil.getWeatherIconResBlack(
                        weatherSet.getNow().getCond().getCode(),
                        dailyForecastBean.getAstro().getSr(),dailyForecastBean.getAstro().getSs()));

        //默认选择的城市背景色为橙色
        if (item.getIsShow()){
//            ((TextView) helper.getView(R.id.tv_city))
//                    .setTextColor(ContextCompat.getColor(mContext,R.color.theme2_text));
            helper.getView(R.id.rl_citylist_item)
                    .setBackgroundColor(ContextCompat.getColor(mContext,R.color.colorPress));
        }else {
//            ((TextView) helper.getView(R.id.tv_city))
//                    .setTextColor(ContextCompat.getColor(mContext,R.color.colorGrayText));
            helper.getView(R.id.rl_citylist_item)
                    .setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        }

    }
}
