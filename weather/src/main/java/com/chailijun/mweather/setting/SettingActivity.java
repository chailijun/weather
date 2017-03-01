package com.chailijun.mweather.setting;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chailijun.mweather.R;
import com.chailijun.mweather.base.BaseActivity;

public class SettingActivity extends BaseActivity {


    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void setListener() {
        findViewById(R.id.iv_title_back).setOnClickListener(this);
        findViewById(R.id.tv_setting_theme).setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_setting_theme:
                startActivity(SettingThemeActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }
}
