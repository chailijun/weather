package com.chailijun.mweather.customview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.chailijun.mweather.utils.DensityUtil;


public class MainIndicator extends View {

    private Paint mPaint;
    private float mX;
    private float mY;
    /**移动的圆的x坐标*/
    private float moveX;
    /**圆半径*/
    private float mRadius = DensityUtil.dp2px(2);
    /**绘制的圆心间距*/
    private float mLeftMargin = DensityUtil.dp2px(10);

    /**指示器的个数，即viewPager页数*/
    private int indicatorCount;

    /**父控件的宽度*/
    private int parentWidth;
    /**父控件的高度*/
    private int parentHeight;

    public MainIndicator(Context context) {
        super(context);
        init();
    }

    public MainIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

        mPaint = new Paint();
        mPaint.setColor(0xffffffff);//颜色
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidth = getMeasuredWidth();
        parentHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (indicatorCount > 1){//至少2个
            mX = parentWidth - ((indicatorCount - 1) * mLeftMargin);
            mX /= 2.0;
            mY = parentHeight/2;
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(2);
            for (int i = 0; i < indicatorCount; i++) {
                canvas.drawCircle(mX + i * mLeftMargin, mY, mRadius, mPaint);
            }
            //绘制移动的指示器
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mX + moveX, mY, mRadius, mPaint);
            super.onDraw(canvas);
        }
    }

    /**
     * 指示器滚动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        moveX = (int) ((position + offset) * mLeftMargin);
        invalidate();
    }

    public int getIndicatorCount() {
        return indicatorCount;
    }

    public void setIndicatorCount(int count) {
        this.indicatorCount = count;
        invalidate();
    }
}
