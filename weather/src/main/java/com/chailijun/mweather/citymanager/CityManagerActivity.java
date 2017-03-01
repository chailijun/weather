package com.chailijun.mweather.citymanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextPaint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chailijun.mweather.R;
import com.chailijun.mweather.WeatherApp;
import com.chailijun.mweather.base.BaseActivity;
import com.chailijun.mweather.cityadd.CityAddActivity;
import com.chailijun.mweather.data.SelectCity;
import com.chailijun.mweather.data.gen.SelectCityDao;
import com.chailijun.mweather.main.MainActivity;
import com.chailijun.mweather.utils.Constants;
import com.chailijun.mweather.utils.DensityUtil;
import com.chailijun.mweather.utils.Logger;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import java.util.List;

public class CityManagerActivity extends BaseActivity {

    private ImageView mBack;
    private ImageView mAdd;

    private RecyclerView mRecyclerView;
    private CityManagerAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;

    private SelectCityDao mSelectCityDao;
    private List<SelectCity> mSelectCityList;

    /**
     * 单元格是否有拖拽移动
     */
    private boolean isDragMoving = false;
    /**
     * 单元格是否滑动移除
     */
    private boolean isItemSwiped = false;


    /**
     * 广告位
     */
    private FrameLayout mBannerContainer;
    /**
     * 广告条
     */
    BannerView mBannerView;

    @Override
    public void initParms(Bundle parms) {
        mSelectCityDao = WeatherApp.getInstances().getmDaoSession().getSelectCityDao();
        mSelectCityList = mSelectCityDao.queryBuilder().build().list();
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_city_manager;
    }

    @Override
    public void initView(View view) {
        mBack = $(R.id.iv_title_back);
        mAdd = $(R.id.iv_title_add);

        mBannerContainer = $(R.id.bannerContainer);
        initBanner();

//        if (TimeUtils.isShowTime(Constants.BLOCKAD_TIME))
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
//            mBannerView.loadAD();
//        }
        mBannerView.loadAD();

        initRecyclerView();
    }

    /**
     * 初始化广告条
     */
    private void initBanner() {
        mBannerView = new BannerView(this, ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
        mBannerView.setRefresh(20);
        mBannerView.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Logger.i(TAG, "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Logger.i(TAG, "ONBannerReceive");
            }
        });
        mBannerContainer.addView(mBannerView);
    }

    private void initRecyclerView() {

        mRecyclerView = $(R.id.rv_city);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Logger.d(TAG, "drag start");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                Logger.d(TAG, "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());

                isDragMoving = true;

                //交换数据的主键
                Long sourceId = mSelectCityList.get(source.getAdapterPosition()).getId();
                Long targetId = mSelectCityList.get(target.getAdapterPosition()).getId();
                mSelectCityList.get(source.getAdapterPosition()).setId(targetId);
                mSelectCityList.get(target.getAdapterPosition()).setId(sourceId);
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Logger.d(TAG, "drag end");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.BLACK);
            }
        };

        final TextPaint paint = new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(DensityUtil.sp2px(16.0f));
        paint.setColor(Color.WHITE);

//        Rect rect = new Rect();
//        String hint = getString(R.string.left_slide_delete);
//        paint.getTextBounds(hint,0,hint.length(),rect);
//        int hintWidth = rect.width();//文本的宽度
//        int hintHeight = rect.height();//文本的高度
//
//        float height = getResources().getDimension(R.dimen.city_manager_item_height);
////        height = DensityUtil.dp2px(height);
//
//        final float hintY = (height - hintHeight) / 2;
//
//        Logger.e(TAG,"height:"+height);
//        Logger.e(TAG,"hintHeight:"+hintHeight);
//        Logger.e(TAG,"hintY:"+hintY);


        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Logger.d(TAG, "view swiped start: " + pos);
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Logger.d(TAG, "View reset: " + pos);
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.BLACK);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Logger.d(TAG, "View Swiped: " + pos);

                isItemSwiped = true;

                if (pos < mSelectCityList.size()) {
                    SelectCity selectCity = mSelectCityList.get(pos);
                    mSelectCityDao.delete(selectCity);
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(CityManagerActivity.this, R.color.red));
                canvas.drawText(getString(R.string.left_slide_delete), 50, 100, paint);

            }
        };
        mAdapter = new CityManagerAdapter(mSelectCityList);
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START);
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
        mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(listener);

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {
        mBack.setOnClickListener(this);
        mAdd.setOnClickListener(this);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

//                Intent intent = new Intent();
//                intent.putExtra(Constants.CURR_INDEX, position);
//                setResult(RESULT_OK, intent);
//                finish();

                setIsShow(position);
                reConstructionMainActivity();
            }
        });
    }

    /**
     * 设置已选择城市的isShow字段，并更新
     *
     * @param currIndex
     */
    private void setIsShow(int currIndex) {

        for (int i = 0; i < mSelectCityList.size(); i++) {

            if (mSelectCityList.get(i).getIsShow() && currIndex != i) {
                //默认展示--->不展示
                mSelectCityList.get(i).setIsShow(false);
                mSelectCityDao.update(mSelectCityList.get(i));
            } else if (!mSelectCityList.get(i).getIsShow() && currIndex == i) {
                //不展示--->默认展示
                mSelectCityList.get(i).setIsShow(true);
                mSelectCityDao.update(mSelectCityList.get(i));
            }
        }
    }

    @Override
    public void widgetClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_title_back:
                if (isDragMoving || isItemSwiped) {
                    reConstructionMainActivity();
                } else {
                    //对数据没有操作时，直接返回
                    finish();
                }
//                overridePendingTransition(R.anim.out_to_left,R.anim.fade);
                break;
            case R.id.iv_title_add:
                intent = new Intent(this, CityAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 重构MainActivity
     */
    private void reConstructionMainActivity() {
        Intent intent = new Intent(CityManagerActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        overridePendingTransition(R.anim.out_to_left,R.anim.fade);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onPause() {
        super.onPause();

//        for (int i = 0; i < mSelectCityList.size(); i++) {
//            Logger.e(TAG,mSelectCityList.get(i).getCityZh()+"\n");
//        }

        if (isDragMoving && mSelectCityList != null && mSelectCityList.size() > 0) {
            //重新保存已选择城市
            mSelectCityDao.deleteAll();

            for (int i = 0; i < mSelectCityList.size(); i++) {
                mSelectCityDao.insert(mSelectCityList.get(i));
            }
        }
    }

    @Override
    public void onBackPressed() {
        reConstructionMainActivity();
        super.onBackPressed();
    }
}
