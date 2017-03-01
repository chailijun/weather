package com.chailijun.mweather.setting;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chailijun.mweather.R;
import com.chailijun.mweather.data.ThemeBean;

import java.util.List;

public class SettingThemeAdapter extends BaseQuickAdapter<ThemeBean,BaseViewHolder>{

    public SettingThemeAdapter(List<ThemeBean> data) {
        super(R.layout.activity_setting_theme_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeBean item) {
        helper.setText(R.id.tv_theme_name,item.getName());
        helper.setBackgroundColor(R.id.iv_theme_color,item.getTheme_start());
    }
}
