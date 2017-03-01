package com.chailijun.mweather.cityadd;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chailijun.mweather.R;
import com.chailijun.mweather.data.City;
import com.chailijun.mweather.data.CityBase;
import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.WorldCity;
import com.chailijun.mweather.data.gen.SelectCityDao;

import java.util.List;


public class SearchResultAdapter extends BaseQuickAdapter<CityBase,BaseViewHolder>{

    private List<SelectCity> mSelectCityList;

    public SearchResultAdapter(List<CityBase> data, SelectCityDao selectCityDao) {
        super(R.layout.activity_city_add_search_result, data);

        mSelectCityList = selectCityDao.queryBuilder().build().list();
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBase item) {

        String cityId = null;
        if (item instanceof City){

            //国内城市
            City city = (City) item;
            helper.setText(R.id.tv_city,city.getCityZh());
            helper.setText(R.id.tv_province," - "+city.getProvinceZh());

            cityId = city.getCityId();


        }else if(item instanceof WorldCity){

            //国际城市
            WorldCity worldCity = (WorldCity) item;
            helper.setText(R.id.tv_city,worldCity.getCityZh());
            helper.setText(R.id.tv_province," - "+worldCity.getCountryZh());

            cityId = worldCity.getCityId();
        }


        //已经添加的城市字体颜色变为橙色
        if (mSelectCityList != null && mSelectCityList.size() > 0){

            boolean flag = false;
            for (int i = 0; i < mSelectCityList.size(); i++) {
                if (!TextUtils.isEmpty(mSelectCityList.get(i).getCityId())){

                    if (cityId.equalsIgnoreCase(mSelectCityList.get(i).getCityId())){

                        ((TextView) helper.getView(R.id.tv_city))
                                .setTextColor(ContextCompat.getColor(mContext,R.color.theme2_text));
                        ((TextView) helper.getView(R.id.tv_province))
                                .setTextColor(ContextCompat.getColor(mContext,R.color.theme2_text));

                        flag = true;
                    }
                }
            }
            if (!flag){
                ((TextView) helper.getView(R.id.tv_city))
                        .setTextColor(ContextCompat.getColor(mContext,R.color.colorGrayText));
                ((TextView) helper.getView(R.id.tv_province))
                        .setTextColor(ContextCompat.getColor(mContext,R.color.colorGrayText));
            }
        }

    }
}
