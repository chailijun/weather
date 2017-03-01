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


public class CityAddAdapter extends BaseQuickAdapter<CityBase,BaseViewHolder>{

    private List<SelectCity> mSelectCityList;

    public CityAddAdapter(List<CityBase> data, SelectCityDao selectCityDao) {
        super(R.layout.activity_city_add_item, data);

        mSelectCityList = selectCityDao.queryBuilder().build().list();
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBase item) {

        String cityId = null;
        if (item instanceof City){

            //国内城市
            City city = (City) item;
            helper.setText(R.id.tv_city,city.getCityZh());

            cityId = city.getCityId();


        }else if(item instanceof WorldCity){

            //国际城市
            WorldCity worldCity = (WorldCity) item;
            helper.setText(R.id.tv_city,worldCity.getCityZh() + "-" + worldCity.getCountryZh());

            cityId = worldCity.getCityId();
        }

        //已经添加的城市字体颜色变为橙色
        if (mSelectCityList != null && mSelectCityList.size() > 0){

            boolean flag = false;
            for (int i = 0; i < mSelectCityList.size(); i++) {
                //已添加过的城市（不包括定位城市）
                if (!TextUtils.isEmpty(mSelectCityList.get(i).getCityId())){

                    if (cityId.equalsIgnoreCase(mSelectCityList.get(i).getCityId())){

                        ((TextView) helper.getView(R.id.tv_city))
                                .setTextColor(ContextCompat.getColor(mContext,R.color.theme2_text));

                        flag = true;
                    }
                }
            }
            if (!flag){
                ((TextView) helper.getView(R.id.tv_city))
                        .setTextColor(ContextCompat.getColor(mContext,R.color.colorGrayText));
            }
        }

    }
}
