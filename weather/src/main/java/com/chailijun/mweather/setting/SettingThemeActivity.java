package com.chailijun.mweather.setting;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chailijun.mweather.R;
import com.chailijun.mweather.base.BaseActivity;
import com.chailijun.mweather.data.ThemeBean;

import java.util.ArrayList;
import java.util.List;

public class SettingThemeActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<ThemeBean> mDatas;
    private SettingThemeAdapter mAdapter;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_setting_theme;
    }

    @Override
    public void initView(View view) {
        mRecyclerView = $(R.id.rv_theme);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatas = new ArrayList<>();

        ThemeBean themeBean = new ThemeBean();
        themeBean.setName(getString(R.string.theme1_name));
        themeBean.setTheme_start(0xffffb5b5);
//        themeBean.setTheme_center(ContextCompat.getColor(this,R.color.theme1_center));
//        themeBean.setTheme_end(ContextCompat.getColor(this,R.color.theme1_end));
        mDatas.add(themeBean);

        themeBean = new ThemeBean();
        themeBean.setName(getString(R.string.theme2_name));
        themeBean.setTheme_start(0xffffb5b5);
//        themeBean.setTheme_center(ContextCompat.getColor(this,R.color.theme1_center));
//        themeBean.setTheme_end(ContextCompat.getColor(this,R.color.theme1_end));
        mDatas.add(themeBean);

        mAdapter = new SettingThemeAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
