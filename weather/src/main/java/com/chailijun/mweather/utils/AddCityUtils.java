package com.chailijun.mweather.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.chailijun.mweather.R;
import com.chailijun.mweather.data.City;
import com.chailijun.mweather.data.CityBase;
import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.WorldCity;
import com.chailijun.mweather.data.gen.SelectCityDao;
import com.chailijun.mweather.main.MainActivity;

import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.chailijun.mweather.R.raw.city;

public class AddCityUtils {

    private static final String TAG = AddCityUtils.class.getSimpleName();

    /**
     * 向数据库中添加选择的城市
     * 1.如果已添加城市达到上限则不添加，否则添加，进入步骤2
     * 2.判断城市是否已添加，若添加，则判断isLocation是否一致（否，则更新；是，弹出提示）
     * 若没有添加，则添加
     * @param context
     * @param mSelectCityDao
     * @param cityBase
     * @param isLocation
     */
    public static void addCity(Context context, SelectCityDao mSelectCityDao, Object cityBase,
                               boolean isLocation) {
        if (mSelectCityDao == null || cityBase == null) {
            return;
        }

        //所有已添加城市
        List<SelectCity> selectCityAll = mSelectCityDao.queryBuilder().build().list();

        if (selectCityAll != null && selectCityAll.size() >= Constants.CITY_MAX) {
            //添加的城市达到上限
            Toast.makeText(context,
                    String.format(context.getString(R.string.city_max), Constants.CITY_MAX),
                    Toast.LENGTH_SHORT).show();
        } else {
            if (cityBase instanceof City) {

                //国内城市
                City city = (City) cityBase;

                SelectCity selectCity = mSelectCityDao.queryBuilder()
                        .where(SelectCityDao.Properties.CityId.eq(city.getCityId())).unique();

                if (selectCity != null){
                    if (isLocation){
                        if (selectCity.getIsLocation() != true){

                            //如果已有定位城市则移除
                            if (selectCityAll != null && selectCityAll.size() > 0){
                                for (int i = 0; i < selectCityAll.size(); i++) {
                                    if (selectCityAll.get(i).getIsLocation() == true){
                                        mSelectCityDao.delete(selectCityAll.get(i));
                                    }
                                }
                            }

                            //更新
                            selectCity.setIsLocation(true);
                            mSelectCityDao.update(selectCity);
                        }

                        enterMainActivity(context,city.getCityId());
                    }else {
                        //提示
                        Toast.makeText(context,
                                context.getString(R.string.city_has_been_added),
                                Toast.LENGTH_SHORT).show();
                    }

                }else {
                    insertCity(mSelectCityDao,
                            city.getCityId(),
                            city.getCityEn(),
                            city.getCityZh(),
                            city.getProvinceEn(),
                            city.getProvinceZh(),
                            isLocation);

                    enterMainActivity(context,city.getCityId());
                }

            } else if (cityBase instanceof WorldCity) {

                //国际城市
                WorldCity worldCity = (WorldCity) cityBase;

                SelectCity selectCity = mSelectCityDao.queryBuilder()
                        .where(SelectCityDao.Properties.CityId.eq(worldCity.getCityId())).unique();

                if (selectCity != null){
                    if (isLocation){
                        if (selectCity.getIsLocation() != true){
                            //更新
                            selectCity.setIsLocation(true);
                            mSelectCityDao.update(selectCity);
                        }

                        enterMainActivity(context,worldCity.getCityId());
                    }else {
                        //提示
                        Toast.makeText(context,
                                context.getString(R.string.city_has_been_added),
                                Toast.LENGTH_SHORT).show();
                    }

                }else {
                    insertCity(mSelectCityDao,
                            worldCity.getCityId(),
                            worldCity.getCityEn(),
                            worldCity.getCityZh(),
                            worldCity.getCountryEn(),
                            worldCity.getCountryZh(),
                            isLocation);

                    enterMainActivity(context,worldCity.getCityId());
                }
            }
        }
    }

    /**
     * 进入主界面并重构
     * @param context
     * @param cityId
     */
    private static void enterMainActivity(Context context, String cityId) {
        if (cityId != null) {
            //进入主界面
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Constants.CITY_ID, cityId);
//            intent.putExtra(Constants.IS_LOCATION, isLocation);
            context.startActivity(intent);
        }
    }

    /**
     * 保存添加的城市
     * @param mSelectCityDao
     * @param cityBase
     * @param isLocation 是否是定位城市，只保存一个定位城市
     * @return
     */
    public static String addCity(SelectCityDao mSelectCityDao, CityBase cityBase, boolean isLocation) {

        String cityId = null;
        String cityEn = null;
        String cityZh = null;
        String provinceEn = null;
        String provinceZh = null;

        if (cityBase instanceof City) {
            City city = (City) cityBase;
            cityId = city.getCityId();
            cityEn = city.getCityEn();
            cityZh = city.getCityZh();
            provinceEn = city.getProvinceEn();
            provinceZh = city.getProvinceZh();
        } else if (cityBase instanceof WorldCity) {
            WorldCity worldCity = (WorldCity) cityBase;
            cityId = worldCity.getCityId();
            cityEn = worldCity.getCityEn();
            cityZh = worldCity.getCityZh();
            provinceEn = worldCity.getCountryEn();
            provinceZh = worldCity.getCountryZh();
        }

        Logger.d("TAG", "添加城市(定位):" + isLocation);
        Logger.d("TAG", "添加城市：" + cityZh);

        if (isLocation) {//定位城市
//            List<SelectCity> list = mSelectCityDao.queryBuilder().build().list();

            List<SelectCity> list = mSelectCityDao.queryBuilder()
                    .where(SelectCityDao.Properties.IsLocation.eq(true))
                    .list();

            if (list != null && list.size() > 0) {
                //清除已添加的定位城市
                for (int i = 0; i < list.size(); i++) {
                    mSelectCityDao.delete(list.get(i));
                }
            } else {
                //重新添加定位城市
                insertCity(mSelectCityDao, cityId, cityEn, cityZh, provinceEn, provinceZh, true);
            }
        } else {//非定位城市
            List<SelectCity> cityList = mSelectCityDao.queryBuilder()
                    .where(SelectCityDao.Properties.CityId.eq(cityId),
                            SelectCityDao.Properties.IsLocation.eq(false))
                    .list();

            if (cityList != null && cityList.size() > 0) {
                for (int i = 0; i < cityList.size(); i++) {
                    //已添加且非定位城市
                    cityList.get(i).setCityId(cityId);
                    cityList.get(i).setCityEn(cityEn);
                    cityList.get(i).setCityZh(cityZh);
                    cityList.get(i).setProvinceEn(provinceEn);
                    cityList.get(i).setProvinceZh(provinceZh);
                    mSelectCityDao.update(cityList.get(i));
                }
            } else {
                //添加城市
                insertCity(mSelectCityDao, cityId, cityEn, cityZh, provinceEn, provinceZh, false);
            }
        }

        return cityId;
    }

    private static void insertCity(SelectCityDao mSelectCityDao,
                                   String cityId,
                                   String cityEn,
                                   String cityZh,
                                   String provinceEn,
                                   String provinceZh,
                                   boolean isLocation) {

        SelectCity selectCity = new SelectCity();
        selectCity.setCityId(cityId);
        selectCity.setCityEn(cityEn);
        selectCity.setCityZh(cityZh);
        selectCity.setProvinceEn(provinceEn);
        selectCity.setProvinceZh(provinceZh);
        selectCity.setIsLocation(isLocation);

        long insert = mSelectCityDao.insert(selectCity);
        if (insert < 0) {
            Logger.e(TAG, "add city error!");
        }
    }
}

