package com.chailijun.mweather.splash;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> mViewContainter;

    public ViewPagerAdapter(List<View> viewContainter) {
        this.mViewContainter = viewContainter;
    }

    @Override
    public int getCount() {
        return mViewContainter == null ? 0 : mViewContainter.size();
    }

    /**
     * 滑动切换的时候销毁当前的组件
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewContainter.get(position));
    }

    /**
     * 每次滑动的时候生成的组件
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewContainter.get(position));
        return mViewContainter.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
