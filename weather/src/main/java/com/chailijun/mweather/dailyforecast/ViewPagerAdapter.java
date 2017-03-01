package com.chailijun.mweather.dailyforecast;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    /**
     * 页面容器
     */
    private List<View> views;

    public ViewPagerAdapter(List<View> views) {
        this.views = views;
    }

    public void addView(View v, int position) {
        views.add(position, v);
    }

    /**
     * 获取viewPager页面的总数
     * @return
     */
    @Override
    public int getCount() {
        return views == null ? 0 : views.size();
    }

    /**
     * 销毁position位置的界面
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    /**
     * 相当于listView的adapter中的getView方法
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    /**
     * 判断当前页面与instantiateItem()生成的页面是否相同
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 删除页面需重写此函数
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
