package com.heim.menuview;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Created by zxp on 2016/7/19 0019.
 */
public class AnimationUtil {
    //放大动画
    public static Animation bigScaleAnimaiton(int duration) {
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(duration);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);

        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);

        return set;
    }

    //缩小动画
    public static Animation smallScaleAnimaiton(int duration) {
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(duration);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);

        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);

        return set;
    }
}

