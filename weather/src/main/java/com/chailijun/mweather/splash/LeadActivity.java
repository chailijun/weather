package com.chailijun.mweather.splash;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chailijun.mweather.R;
import com.chailijun.mweather.WeatherApp;
import com.chailijun.mweather.base.BaseActivity;
import com.chailijun.mweather.customview.ImageFadeIn;
import com.chailijun.mweather.customview.Indicator;
import com.chailijun.mweather.data.City;
import com.chailijun.mweather.data.Scenic;
import com.chailijun.mweather.data.gen.CityDao;
import com.chailijun.mweather.data.gen.ScenicDao;
import com.chailijun.mweather.data.gen.WorldCityDao;
import com.chailijun.mweather.main.MainActivity;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.Logger;
import com.chailijun.mweather.utils.SPUtil;
import com.chailijun.mweather.data.WorldCity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 第一次进入app开启的引导界面
 */
public class LeadActivity extends BaseActivity {

//    private ProgressBar progress_bar;
    private ViewPager mViewpager;
    private Indicator mIndicator;

    private ViewPagerAdapter mAdapter;
    private List<View> mViewContainter = new ArrayList<>();
    private int[] guideRes = {R.layout.lead1,R.layout.lead2,R.layout.lead3,R.layout.lead4};

    private List<City> mCityList;
    private List<Scenic> mScenicList;
    private List<WorldCity> mWorldCityList;

    private ArrayList<ImageFadeIn> imageFadeIns = new ArrayList<>();
    private ImageView iv_guide4_circle;
    private ImageView iv_guide4_finger;

    /**
     * viewPager上一次的位置
     */
    private int prePosition;

    @Override
    public void initParms(Bundle parms) {
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        imporDatabase();
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_lead;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView(View view) {
        initViews();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doBusiness(Context mContext) {
        setGuideAnimation();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initViews() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mIndicator = (Indicator) findViewById(R.id.indicator);

        for (int i = 0; i < guideRes.length; i++) {
            View view = LayoutInflater.from(this).inflate(guideRes[i], null);
            mViewContainter.add(view);

            //获取viewPager中的ImageFadeIn控件
            ImageFadeIn imageFadeIn = (ImageFadeIn) view.findViewById(R.id.image_fade_in);
            imageFadeIns.add(imageFadeIn);
        }
        mAdapter = new ViewPagerAdapter(mViewContainter);
        mViewpager.setAdapter(mAdapter);
        mIndicator.setIndicatorCount(mViewContainter.size());

        //开启当前viewPager中的ImageFadeIn的动画
        imageFadeIns.get(prePosition).addAnimation();

        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                //指示器滚动
                mIndicator.scroll(position, positionOffset);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                //移除上一次显示的viewPager中的ImageFadeIn的动画
                if (imageFadeIns.get(prePosition) != null) {
                    imageFadeIns.get(prePosition).cancelAnimation();
                }
                //添加当前显示的viewPager中的ImageFadeIn的动画
                if (imageFadeIns.get(position) != null) {
                    imageFadeIns.get(position).addAnimation();
                }
                prePosition = position;
            }
        });
    }

    private void setGuideAnimation() {
        iv_guide4_circle = (ImageView) mViewContainter.get(3).findViewById(R.id.iv_guide4_circle);
        iv_guide4_finger = (ImageView) mViewContainter.get(3).findViewById(R.id.iv_guide4_finger);

        ObjectAnimator anim = ObjectAnimator
                .ofInt(iv_guide4_finger, "alpha", 0, 255, 0)
                .setDuration(2000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                1.2f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                1.2f, 1f);
        ObjectAnimator anim2 = ObjectAnimator
                .ofPropertyValuesHolder(iv_guide4_circle, pvhX, pvhY, pvhZ)
                .setDuration(2000);
        anim2.setRepeatCount(ValueAnimator.INFINITE);
        anim2.start();
    }


    public void enterMain(View view){
        SPUtil.put(LeadActivity.this,Constants.IS_FIRST,false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SPUtil.put(LeadActivity.this,Constants.IS_FIRST,false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 直接导入数据库文件
     */
    private void imporDatabase() {
        //存放数据库的目录
        String dirPath="/data/data/"+getPackageName()+"/databases";
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdir();
        }

        //数据库文件
        File file = new File(dir, "city.db");

        InputStream is = null;
        FileOutputStream fos = null;
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            //加载需要导入的数据库
            is = this.getApplicationContext().getResources().openRawResource(R.raw.city);
            fos = new FileOutputStream(file);
            byte[] buffere=new byte[is.available()];
            is.read(buffere);
            fos.write(buffere);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 国内城市列表
     * json数据-->java对象-->数据库文件
     */
    private void initCityData() {

        InputStream is = null;
        try {
            is = getResources().getAssets().open("cityJson");
            String cityList = getStringFromInputStream(is);
            mCityList = getCityList(cityList);
            saveCityToDB();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 国内景点列表
     * json数据-->java对象-->数据库文件
     */
    private void initScenicData() {
        InputStream is = null;
        try {
            is = getResources().getAssets().open("scenicJson");
            String scenicList = getStringFromInputStream(is);
            mScenicList = getScenicList(scenicList);
            saveScenicToDB();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 国际城市列表
     * json数据-->java对象-->数据库文件
     */
    private void initWorldCityData() {
        InputStream is = null;
        try {
            is = getResources().getAssets().open("worldCityJson");
            String worldCity = getStringFromInputStream(is);
            mWorldCityList = getWorldCityList(worldCity);
            saveWorldCityToDB();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 从输入流中获取字符串
     * @param is
     * @return
     */
    private String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 国内景点json解析
     * @param jsonString
     * @return
     */
    private List<Scenic> getScenicList(String jsonString) {
        List<Scenic> scenicList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                Scenic scenicBean = new Scenic();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                scenicBean.setScenicId(jsonObject.optString("id"));
                scenicBean.setName(jsonObject.optString("name"));
                scenicBean.setCity(jsonObject.optString("city"));
                scenicBean.setProvince(jsonObject.optString("province"));

                scenicList.add(scenicBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return scenicList;
    }

    /**
     * 国内城市Json解析
     * @param jsonString
     * @return
     */
    private List<City> getCityList(String jsonString) {
        List<City> cityList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                City cityBean = new City();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                cityBean.setCityId(jsonObject.optString("id"));
                cityBean.setCityEn(jsonObject.optString("cityEn"));
                cityBean.setCityZh(jsonObject.optString("cityZh"));

                cityBean.setCountryCode(jsonObject.optString("countryCode"));
                cityBean.setCountryEn(jsonObject.optString("countryEn"));
                cityBean.setCountryZh(jsonObject.optString("countryZh"));

                cityBean.setProvinceEn(jsonObject.optString("provinceEn"));
                cityBean.setProvinceZh(jsonObject.optString("provinceZh"));

                cityBean.setLeaderEn(jsonObject.optString("leaderEn"));
                cityBean.setLeaderZh(jsonObject.optString("leaderZh"));

                cityBean.setLat(jsonObject.optString("lat"));
                cityBean.setLon(jsonObject.optString("lon"));

                cityList.add(cityBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    /**
     * 国际城市json解析
     * @param jsonString
     * @return
     */
    private List<WorldCity> getWorldCityList(String jsonString) {
        List<WorldCity> worldCityList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                WorldCity worldCity = new WorldCity();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                worldCity.setCityId(jsonObject.optString("id"));
                worldCity.setCityEn(jsonObject.optString("cityEn"));
                worldCity.setCityZh(jsonObject.optString("cityZh"));

                worldCity.setContinent(jsonObject.optString("continent"));

                worldCity.setCountryCode(jsonObject.optString("countryCode"));
                worldCity.setCountryEn(jsonObject.optString("countryEn"));

                worldCity.setLat(jsonObject.optString("lat"));
                worldCity.setLon(jsonObject.optString("lon"));

                worldCityList.add(worldCity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return worldCityList;
    }

    /**
     * 国内城市列表保存至数据库
     */
    private void saveCityToDB() {
        if (mCityList != null && mCityList.size() > 0){
            CityDao cityDao = WeatherApp.getInstances().getmDaoSession().getCityDao();
            List<City> list = cityDao.queryBuilder().list();
            if (list.size() == mCityList.size()){
                /*for (int i = 0; i < mCityList.size(); i++) {
                    cityDao.update(mCityList.get(i));
                }*/
            }else {
                for (int i = 0; i < mCityList.size(); i++) {
                    long l = cityDao.insert(mCityList.get(i));
                    Logger.i(LeadActivity.class.getSimpleName(),"国内城市 insert:"+l);
                }
            }
        }else {
            try {
                throw new Exception("城市列表数据解析出错！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 国内景点列表保存至数据库
     */
    private void saveScenicToDB() {
        if (mScenicList != null && mScenicList.size() > 0){
            ScenicDao scenicBeanDao = WeatherApp.getInstances().getmDaoSession().getScenicDao();
            List<Scenic> list = scenicBeanDao.queryBuilder().list();
            if (list.size() == mScenicList.size()){
                /*for (int i = 0; i < mCityList.size(); i++) {
                    cityDao.update(mCityList.get(i));
                }*/
            }else {
                for (int i = 0; i < mScenicList.size(); i++) {
                    long l = scenicBeanDao.insert(mScenicList.get(i));
                    Logger.i(LeadActivity.class.getSimpleName(),"景点 insert:"+l);
                }
            }
        }else {
            try {
                throw new Exception("景点列表数据解析出错！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 国际城市列表保存至数据库
     */
    private void saveWorldCityToDB() {
        if (mWorldCityList != null && mWorldCityList.size() > 0){
            WorldCityDao worldCityDao = WeatherApp.getInstances().getmDaoSession().getWorldCityDao();
            List<WorldCity> list = worldCityDao.queryBuilder().list();
            if (list.size() == mWorldCityList.size()){
                /*for (int i = 0; i < mCityList.size(); i++) {
                    cityDao.update(mCityList.get(i));
                }*/
            }else {
                for (int i = 0; i < mWorldCityList.size(); i++) {
                    long l = worldCityDao.insert(mWorldCityList.get(i));
                    Logger.i(LeadActivity.class.getSimpleName(),"国际城市 insert:"+l);
                }
            }
        }else {
            try {
                throw new Exception("国际城市列表数据解析出错！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
