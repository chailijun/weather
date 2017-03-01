package com.chailijun.mweather.customview;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chailijun.mweather.R;


/**
 * 图片淡出效果
 */
public class ImageFadeIn extends LinearLayout {

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    public ImageFadeIn(Context context) {
        super(context);

        //解析XML
        View view = inflater(context);
        this.addView(view);

//        addAnimation();
    }

    public ImageFadeIn(Context context, AttributeSet attrs) {
        super(context, attrs);

        //解析XML
        View view = inflater(context);
        this.addView(view);

        getAttrs(attrs);

//        addAnimation();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ImageFadeIn);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int index = ta.getIndex(i);

            switch (index){
                case R.styleable.ImageFadeIn_first_image:
                    imageView1.setImageResource(ta.getResourceId(index,0));
                    break;
                case R.styleable.ImageFadeIn_second_image:
                    imageView2.setImageResource(ta.getResourceId(index,0));
                    break;
                case R.styleable.ImageFadeIn_third_image:
                    imageView3.setImageResource(ta.getResourceId(index,0));
                    break;
                case R.styleable.ImageFadeIn_fourth_image:
                    imageView4.setImageResource(ta.getResourceId(index,0));
                    break;
                default:
                    break;
            }
        }
        ta.recycle();
    }

    private AnimatorSet animaSet;

    public void cancelAnimation(){
        animaSet.cancel();
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addAnimation() {

        imageView1.setImageAlpha(0);
        imageView2.setImageAlpha(0);
        imageView3.setImageAlpha(0);
        imageView4.setImageAlpha(0);

        ObjectAnimator anim1 = ObjectAnimator
                .ofInt(imageView4,"alpha",0,255)
                .setDuration(500);

        ObjectAnimator anim2 = ObjectAnimator
                .ofInt(imageView3,"alpha",0,255)
                .setDuration(500);

        ObjectAnimator anim3 = ObjectAnimator
                .ofInt(imageView2,"alpha",0,255)
                .setDuration(500);

        ObjectAnimator anim4 = ObjectAnimator
                .ofInt(imageView1,"alpha",0,255)
                .setDuration(500);

        animaSet = new AnimatorSet();
//        animaSet.play(anim1).before(anim2).before(anim3).before(anim4);
        animaSet.play(anim1).before(anim2);
        animaSet.play(anim2).before(anim3);
        animaSet.play(anim3).before(anim4);
        animaSet.start();


    }

    /**解析XML*/
    public View inflater(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_fade_in, null);
        imageView1 = (ImageView) view.findViewById(R.id.imageview1);
        imageView2 = (ImageView) view.findViewById(R.id.imageview2);
        imageView3 = (ImageView) view.findViewById(R.id.imageview3);
        imageView4 = (ImageView) view.findViewById(R.id.imageview4);

        return view;
    }
}
