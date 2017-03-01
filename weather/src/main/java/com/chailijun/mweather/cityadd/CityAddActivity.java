package com.chailijun.mweather.cityadd;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chailijun.mweather.R;
import com.chailijun.mweather.WeatherApp;
import com.chailijun.mweather.base.BaseActivity;
import com.chailijun.mweather.customview.ClearEditText;
import com.chailijun.mweather.data.City;
import com.chailijun.mweather.data.CityBase;
import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.WorldCity;
import com.chailijun.mweather.data.gen.CityDao;
import com.chailijun.mweather.data.gen.SelectCityDao;
import com.chailijun.mweather.data.gen.WorldCityDao;
import com.chailijun.mweather.location.LocationService;
import com.chailijun.mweather.utils.AddCityUtils;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.Logger;
import com.chailijun.mweather.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import static com.chailijun.mweather.utils.SPUtil.get;

public class CityAddActivity extends BaseActivity {

    //定位服务
    private LocationService locationService;

    /**
     * gridLayoutManager列数
     */
    private static final int ROW_SPAN = 4;

    private ImageView iv_title_back;

    private ClearEditText mSearchEditText;
    private RecyclerView mRecyclerView;

    //头布局控件（定位）
    private LinearLayout ll_location;
    private ProgressBar progress_bar;
    private TextView tv_location_city;
    private ImageView iv_icon;

    private CityAddAdapter mAdapter;
    private SearchResultAdapter mSearchResultAdapter;

    private CityDao mCityDao;
    private WorldCityDao mWorldCityDao;
    private SelectCityDao mSelectCityDao;

    private List<CityBase> mHotCityList;


    /**
     * 定位回调监听
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location) {

//                Logger.d(TAG, "省份:" + location.getProvince());
//                Logger.d(TAG, "城市:" + location.getCity());
//                Logger.d(TAG, "城市编码:" + location.getCityCode());
//                Logger.d(TAG, "区/县:" + location.getDistrict());
//                Logger.d(TAG, "街道:" + location.getStreet());
//                Logger.d(TAG, "街道号码:" + location.getStreetNumber());
//                Logger.d(TAG, "经度:" + location.getLongitude());
//                Logger.d(TAG, "纬度:" + location.getLatitude());
//                Logger.d(TAG, "地址:" + location.getAddrStr());

                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    showToast("GPS定位成功");
                    saveLocationCity(location);
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    showToast("网络定位成功");
                    saveLocationCity(location);
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    showToast("离线定位成功");
                    saveLocationCity(location);
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    showToast("服务端网络定位失败");

                    progress_bar.setVisibility(View.GONE);
                    iv_icon.setVisibility(View.GONE);
                    tv_location_city.setText(getString(R.string.location_failed));

                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    showToast("网络不同导致定位失败，请检查网络是否通畅");

                    progress_bar.setVisibility(View.GONE);
                    iv_icon.setVisibility(View.GONE);
                    tv_location_city.setText(getString(R.string.location_failed));

                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    showToast("无法获取有效定位依据导致定位失败，一般是由于手机的原因，" +
                            "处于飞行模式下一般会造成这种结果，可以试着重启手机");

                    progress_bar.setVisibility(View.GONE);
                    iv_icon.setVisibility(View.GONE);
                    tv_location_city.setText(getString(R.string.location_failed));
                }
            }
        }
    };

    @Override
    public void initParms(Bundle parms) {

        Logger.e(TAG, "CityAddActivity:" + this.toString());

        mCityDao = WeatherApp.getInstances().getmDaoSession().getCityDao();
        mWorldCityDao = WeatherApp.getInstances().getmDaoSession().getWorldCityDao();
        mSelectCityDao = WeatherApp.getInstances().getmDaoSession().getSelectCityDao();

        initLocationService();
    }

    /**
     * 初始化定位服务
     */
    private void initLocationService() {
        locationService = ((WeatherApp) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_city_add;
    }


    @Override
    public void initView(View view) {
        iv_title_back = $(R.id.iv_title_back);
        mSearchEditText = $(R.id.et_search);

        //添加热门城市的数据
        mHotCityList = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.hotcity);

        for (int i = 0; i < stringArray.length; i++) {
            City city = mCityDao.queryBuilder()
                    .where(CityDao.Properties.CityZh.eq(stringArray[i])).unique();
            mHotCityList.add(city);
        }

        mAdapter = new CityAddAdapter(mHotCityList, mSelectCityDao);
        mSearchResultAdapter = new SearchResultAdapter(null, mSelectCityDao);

        mRecyclerView = $(R.id.rv_citylist);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, ROW_SPAN));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        //添加头布局（定位）
        addHeadView();
    }


    /**
     * 添加头布局（定位）
     */
    private void addHeadView() {
        View headView = LayoutInflater.from(this).inflate(R.layout.activity_city_add_location,
                (ViewGroup) mRecyclerView.getParent(), false);

        progress_bar = (ProgressBar) headView.findViewById(R.id.progress_bar);
        ll_location = (LinearLayout) headView.findViewById(R.id.ll_location);
        tv_location_city = (TextView) headView.findViewById(R.id.tv_location_city);
        iv_icon = (ImageView) headView.findViewById(R.id.iv_icon);

        String addr = (String) get(CityAddActivity.this, Constants.LOCATION_ADDR, "");
        Logger.e(TAG, "定位地址:" + addr);

        if (TextUtils.isEmpty(addr) &&
                !(boolean) SPUtil.get(CityAddActivity.this, Constants.IS_LOCATION, false)) {

            iv_icon.setVisibility(View.GONE);
            tv_location_city.setText(getString(R.string.locating));
        }else if (TextUtils.isEmpty(addr) &&
                (boolean) SPUtil.get(CityAddActivity.this, Constants.IS_LOCATION, false)) {

            iv_icon.setVisibility(View.GONE);
            tv_location_city.setText(getString(R.string.click_locating));
        } else {

            iv_icon.setVisibility(View.VISIBLE);
            tv_location_city.setText(addr);
        }
        mAdapter.addHeaderView(headView);
    }

    @Override
    public void doBusiness(Context mContext) {
        boolean isLocation = (boolean) SPUtil.get(CityAddActivity.this, Constants.IS_LOCATION, false);
        if (!isLocation) {
            startLocation();
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        locationService.start();
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {
        iv_title_back.setOnClickListener(this);

        ll_location.setOnClickListener(this);

        mRecyclerView.addOnItemTouchListener(new CityItemTouchListener());

        //搜索框文字变化监听
        mSearchEditText.addTextChangedListener(new SearchTextWatcher());
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                List<SelectCity> list = mSelectCityDao.queryBuilder().build().list();
                if (list.size() == 0) {
                    showToast(getString(R.string.at_least_one_city));
                } else if (list.size() > 0) {
                    finish();
                }
                break;
            case R.id.ll_location:
                startLocation();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        List<SelectCity> list = mSelectCityDao.queryBuilder().build().list();
        if (list.size() == 0) {
            showToast(getString(R.string.at_least_one_city));
        } else if (list.size() > 0) {
            super.onBackPressed();
        }
    }

    private class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Logger.d(TAG, "搜索栏文字:" + editable.toString());
            if (TextUtils.isEmpty(editable.toString())) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(CityAddActivity.this, ROW_SPAN));
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setNewData(mHotCityList);
            } else {

                List<City> cityList = mCityDao.queryBuilder()
                        .whereOr(CityDao.Properties.CityZh.like("%" + editable + "%"),
                                CityDao.Properties.CityEn.like("%" + editable + "%"),
                                CityDao.Properties.LeaderZh.like("%" + editable + "%"),
                                CityDao.Properties.LeaderEn.like("%" + editable + "%"))
                        .list();

                List<WorldCity> worldCityList = mWorldCityDao.queryBuilder()
                        .whereOr(WorldCityDao.Properties.CityZh.like("%" + editable + "%"),
                                WorldCityDao.Properties.CityEn.like("%" + editable + "%"),
                                WorldCityDao.Properties.CountryZh.like("%" + editable + "%"),
                                WorldCityDao.Properties.CountryEn.like("%" + editable + "%"))
                        .list();

                List<CityBase> result = new ArrayList<>();
                result.addAll(cityList);
                result.addAll(worldCityList);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(CityAddActivity.this));
                mRecyclerView.setAdapter(mSearchResultAdapter);
                mSearchResultAdapter.setNewData(result);
            }
        }
    }

    /**
     * 保存定位城市的信息
     * 1.首先匹配 县/区，若成功则验证其省份，若没有则步骤2
     * 2.匹配 市，若成功则验证其省份
     *
     * @param location
     */
    private void saveLocationCity(BDLocation location) {

        progress_bar.setVisibility(View.GONE);
        locationService.stop();

        String provinceZh = location.getProvince();//省
        String cityZh = location.getCity();//市
        String districtZh = location.getDistrict();//县
        String street = location.getStreet();//街

        //去掉省份名称中后面的“省”或“市”字(因为表CITY中省份名称后面没有“省”或“市”字)
        String provinceUn = null;
        if (provinceZh.substring(provinceZh.length() - 1, provinceZh.length()).equals("省") ||
                provinceZh.substring(provinceZh.length() - 1, provinceZh.length()).equals("市")) {
            if (provinceZh.substring(0, provinceZh.length() - 1).length() >= 2) {//省份名称至少两个汉字
                provinceUn = provinceZh.substring(0, provinceZh.length() - 1);
                Logger.d(TAG, "-->省:" + provinceUn);
            }
        }

        boolean flag = false;
        for (int i = 0; i < districtZh.length(); i++) {

            districtZh = districtZh.substring(0, districtZh.length() - i);
            Logger.d(TAG, "县-->匹配的县名字:" + districtZh + " i:" + i);

            List<City> districtList = mCityDao.queryBuilder()
                    .where(CityDao.Properties.CityZh.eq(districtZh)).list();
            if (districtList != null && districtList.size() > 0) {//县/区匹配成功
                //验证对应的省份
                for (int j = 0; j < districtList.size(); j++) {
                    if (districtList.get(j).getProvinceZh().equalsIgnoreCase(provinceUn)) {
                        Logger.d(TAG, "县-->匹配成功的城市:" + districtList.get(j).getCityZh());
                        Logger.d(TAG, "县-->匹配成功的城市:" + districtList.get(j).getCityId());

                        //写入已经定位了
                        SPUtil.put(CityAddActivity.this, Constants.IS_LOCATION, true);

                        //添加匹配成功后的定位城市
                        AddCityUtils.addCity(CityAddActivity.this,
                                mSelectCityDao, districtList.get(j), true);

                        flag = true;
                        break;
                    }
                }

            }
        }

        if (!flag) {//匹配 市
            for (int i = 0; i < cityZh.length(); i++) {
                cityZh = cityZh.substring(0, cityZh.length() - i);
                Logger.d(TAG, "市-->匹配的市名字:" + cityZh + " i:" + i);

                List<City> cityList = mCityDao.queryBuilder()
                        .where(CityDao.Properties.CityZh.eq(cityZh)).list();

                if (cityList != null && cityList.size() > 0) {
                    //验证对应的省份
                    for (int j = 0; j < cityList.size(); j++) {
                        if (cityList.get(j).getProvinceZh().equalsIgnoreCase(provinceUn)) {
                            Logger.d(TAG, "市-->匹配成功的城市:" + cityList.get(j).getCityZh());
                            Logger.d(TAG, "市-->匹配成功的城市:" + cityList.get(j).getCityId());

                            //写入已经定位了
                            SPUtil.put(CityAddActivity.this, Constants.IS_LOCATION, true);

                            //添加匹配成功后的定位城市
                            AddCityUtils.addCity(CityAddActivity.this,
                                    mSelectCityDao, cityList.get(j), true);

                            break;
                        }
                    }
                }
            }
        }

        String addrStr = location.getCity() + location.getDistrict() + location.getStreet();
        SPUtil.put(CityAddActivity.this, Constants.LOCATION_ADDR, addrStr);
        SPUtil.put(CityAddActivity.this, Constants.LOCATION_STREET, location.getStreet());

        iv_icon.setVisibility(View.VISIBLE);
        tv_location_city.setText(addrStr);
    }

    private class CityItemTouchListener extends OnItemClickListener {

        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            //添加城市
            AddCityUtils.addCity(CityAddActivity.this,
                    mSelectCityDao, adapter.getData().get(position),
                    false);
        }
    }
}