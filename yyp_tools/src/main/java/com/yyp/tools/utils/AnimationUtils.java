//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by generalYan on 2019/10/21.
 */

public class AnimationUtils {
    public static final long DEFAULT_ANIMATION_DURATION = 400L;

    private AnimationUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long durationMillis, AnimationListener animationListener) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        rotateAnimation.setDuration(durationMillis);
        if (animationListener != null) {
            rotateAnimation.setAnimationListener(animationListener);
        }

        return rotateAnimation;
    }

    public static RotateAnimation getRotateAnimationByCenter(long durationMillis, AnimationListener animationListener) {
        return getRotateAnimation(0.0F, 359.0F, 1, 0.5F, 1, 0.5F, durationMillis, animationListener);
    }

    public static RotateAnimation getRotateAnimationByCenter(long duration) {
        return getRotateAnimationByCenter(duration, (AnimationListener) null);
    }

    public static RotateAnimation getRotateAnimationByCenter(AnimationListener animationListener) {
        return getRotateAnimationByCenter(400L, animationListener);
    }

    public static RotateAnimation getRotateAnimationByCenter() {
        return getRotateAnimationByCenter(400L, (AnimationListener) null);
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis, AnimationListener animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(durationMillis);
        if (animationListener != null) {
            alphaAnimation.setAnimationListener(animationListener);
        }

        return alphaAnimation;
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis) {
        return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, (AnimationListener) null);
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, AnimationListener animationListener) {
        return getAlphaAnimation(fromAlpha, toAlpha, 400L, animationListener);
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha) {
        return getAlphaAnimation(fromAlpha, toAlpha, 400L, (AnimationListener) null);
    }

    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis, AnimationListener animationListener) {
        return getAlphaAnimation(1.0F, 0.0F, durationMillis, animationListener);
    }

    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis) {
        return getHiddenAlphaAnimation(durationMillis, (AnimationListener) null);
    }

    public static AlphaAnimation getHiddenAlphaAnimation(AnimationListener animationListener) {
        return getHiddenAlphaAnimation(400L, animationListener);
    }

    public static AlphaAnimation getHiddenAlphaAnimation() {
        return getHiddenAlphaAnimation(400L, (AnimationListener) null);
    }

    public static AlphaAnimation getShowAlphaAnimation(long durationMillis, AnimationListener animationListener) {
        return getAlphaAnimation(0.0F, 1.0F, durationMillis, animationListener);
    }

    public static AlphaAnimation getShowAlphaAnimation(long durationMillis) {
        return getAlphaAnimation(0.0F, 1.0F, durationMillis, (AnimationListener) null);
    }

    public static AlphaAnimation getShowAlphaAnimation(AnimationListener animationListener) {
        return getAlphaAnimation(0.0F, 1.0F, 400L, animationListener);
    }

    public static AlphaAnimation getShowAlphaAnimation() {
        return getAlphaAnimation(0.0F, 1.0F, 400L, (AnimationListener) null);
    }

    public static ScaleAnimation getLessenScaleAnimation(long durationMillis, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    public static ScaleAnimation getLessenScaleAnimation(long durationMillis) {
        return getLessenScaleAnimation(400L);
    }

    public static ScaleAnimation getLessenScaleAnimation(AnimationListener animationListener) {
        return getLessenScaleAnimation(400L, (AnimationListener) null);
    }

    public static ScaleAnimation getAmplificationAnimation(long durationMillis, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    public static ScaleAnimation getAmplificationAnimation(long durationMillis) {
        return getLessenScaleAnimation(400L);
    }

    public static ScaleAnimation getAmplificationAnimation(AnimationListener animationListener) {
        return getLessenScaleAnimation(400L, (AnimationListener) null);
    }
}

