//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

/**
 * Created by generalYan on 2019/10/21.
 */

public class AnimationViewUtils {
    private AnimationViewUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static void invisibleViewByAlpha(final View view, long durationMillis, final boolean isBanClick, final AnimationListener animationListener) {
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
            AlphaAnimation hiddenAlphaAnimation = AnimationUtils.getHiddenAlphaAnimation(durationMillis);
            hiddenAlphaAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (isBanClick) {
                        view.setClickable(false);
                    }

                    if (animationListener != null) {
                        animationListener.onAnimationStart(animation);
                    }

                }

                public void onAnimationRepeat(Animation animation) {
                    if (animationListener != null) {
                        animationListener.onAnimationRepeat(animation);
                    }

                }

                public void onAnimationEnd(Animation animation) {
                    if (isBanClick) {
                        view.setClickable(true);
                    }

                    if (animationListener != null) {
                        animationListener.onAnimationEnd(animation);
                    }

                }
            });
            view.startAnimation(hiddenAlphaAnimation);
        }

    }

    public static void invisibleViewByAlpha(View view, long durationMillis, AnimationListener animationListener) {
        invisibleViewByAlpha(view, durationMillis, false, animationListener);
    }

    public static void invisibleViewByAlpha(View view, long durationMillis, boolean isBanClick) {
        invisibleViewByAlpha(view, durationMillis, isBanClick, (AnimationListener) null);
    }

    public static void invisibleViewByAlpha(View view, long durationMillis) {
        invisibleViewByAlpha(view, durationMillis, false, (AnimationListener) null);
    }

    public static void invisibleViewByAlpha(View view, boolean isBanClick, AnimationListener animationListener) {
        invisibleViewByAlpha(view, 400L, isBanClick, animationListener);
    }

    public static void invisibleViewByAlpha(View view, AnimationListener animationListener) {
        invisibleViewByAlpha(view, 400L, false, animationListener);
    }

    public static void invisibleViewByAlpha(View view, boolean isBanClick) {
        invisibleViewByAlpha(view, 400L, isBanClick, (AnimationListener) null);
    }

    public static void invisibleViewByAlpha(View view) {
        invisibleViewByAlpha(view, 400L, false, (AnimationListener) null);
    }

    public static void goneViewByAlpha(final View view, long durationMillis, final boolean isBanClick, final AnimationListener animationListener) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
            AlphaAnimation hiddenAlphaAnimation = AnimationUtils.getHiddenAlphaAnimation(durationMillis);
            hiddenAlphaAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (isBanClick) {
                        view.setClickable(false);
                    }

                    if (animationListener != null) {
                        animationListener.onAnimationStart(animation);
                    }

                }

                public void onAnimationRepeat(Animation animation) {
                    if (animationListener != null) {
                        animationListener.onAnimationRepeat(animation);
                    }

                }

                public void onAnimationEnd(Animation animation) {
                    if (isBanClick) {
                        view.setClickable(true);
                    }

                    if (animationListener != null) {
                        animationListener.onAnimationEnd(animation);
                    }

                }
            });
            view.startAnimation(hiddenAlphaAnimation);
        }

    }

    public static void goneViewByAlpha(View view, long durationMillis, AnimationListener animationListener) {
        goneViewByAlpha(view, durationMillis, false, animationListener);
    }

    public static void goneViewByAlpha(View view, long durationMillis, boolean isBanClick) {
        goneViewByAlpha(view, durationMillis, isBanClick, (AnimationListener) null);
    }

    public static void goneViewByAlpha(View view, long durationMillis) {
        goneViewByAlpha(view, durationMillis, false, (AnimationListener) null);
    }

    public static void goneViewByAlpha(View view, boolean isBanClick, AnimationListener animationListener) {
        goneViewByAlpha(view, 400L, isBanClick, animationListener);
    }

    public static void goneViewByAlpha(View view, AnimationListener animationListener) {
        goneViewByAlpha(view, 400L, false, animationListener);
    }

    public static void goneViewByAlpha(View view, boolean isBanClick) {
        goneViewByAlpha(view, 400L, isBanClick, (AnimationListener) null);
    }

    public static void goneViewByAlpha(View view) {
        goneViewByAlpha(view, 400L, false, (AnimationListener) null);
    }

    public static void visibleViewByAlpha(final View view, long durationMillis, final boolean isBanClick, final AnimationListener animationListener) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation showAlphaAnimation = AnimationUtils.getShowAlphaAnimation(durationMillis);
            showAlphaAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (isBanClick) {
                        view.setClickable(false);
                    }

                    if (animationListener != null) {
                        animationListener.onAnimationStart(animation);
                    }

                }

                public void onAnimationRepeat(Animation animation) {
                    if (animationListener != null) {
                        animationListener.onAnimationRepeat(animation);
                    }

                }

                public void onAnimationEnd(Animation animation) {
                    if (isBanClick) {
                        view.setClickable(true);
                    }

                    if (animationListener != null) {
                        animationListener.onAnimationEnd(animation);
                    }

                }
            });
            view.startAnimation(showAlphaAnimation);
        }

    }

    public static void visibleViewByAlpha(View view, long durationMillis, AnimationListener animationListener) {
        visibleViewByAlpha(view, durationMillis, false, animationListener);
    }

    public static void visibleViewByAlpha(View view, long durationMillis, boolean isBanClick) {
        visibleViewByAlpha(view, durationMillis, isBanClick, (AnimationListener) null);
    }

    public static void visibleViewByAlpha(View view, long durationMillis) {
        visibleViewByAlpha(view, durationMillis, false, (AnimationListener) null);
    }

    public static void visibleViewByAlpha(View view, boolean isBanClick, AnimationListener animationListener) {
        visibleViewByAlpha(view, 400L, isBanClick, animationListener);
    }

    public static void visibleViewByAlpha(View view, AnimationListener animationListener) {
        visibleViewByAlpha(view, 400L, false, animationListener);
    }

    public static void visibleViewByAlpha(View view, boolean isBanClick) {
        visibleViewByAlpha(view, 400L, isBanClick, (AnimationListener) null);
    }

    public static void visibleViewByAlpha(View view) {
        visibleViewByAlpha(view, 400L, false, (AnimationListener) null);
    }

    public static void translate(final View view, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, float cycles, long durationMillis, final boolean isBanClick) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        translateAnimation.setDuration(durationMillis);
        if ((double) cycles > 0.0D) {
            translateAnimation.setInterpolator(new CycleInterpolator(cycles));
        }

        translateAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                if (isBanClick) {
                    view.setClickable(false);
                }

            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (isBanClick) {
                    view.setClickable(true);
                }

            }
        });
        view.startAnimation(translateAnimation);
    }

    public static void translate(View view, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, float cycles, long durationMillis) {
        translate(view, fromXDelta, toXDelta, fromYDelta, toYDelta, cycles, durationMillis, false);
    }

    public static void shake(View view, float fromXDelta, float toXDelta, float cycles, long durationMillis, boolean isBanClick) {
        translate(view, fromXDelta, toXDelta, 0.0F, 0.0F, cycles, durationMillis, isBanClick);
    }

    public static void shake(View view, float fromXDelta, float toXDelta, float cycles, long durationMillis) {
        translate(view, fromXDelta, toXDelta, 0.0F, 0.0F, cycles, durationMillis, false);
    }

    public static void shake(View view, float cycles, long durationMillis, boolean isBanClick) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, cycles, durationMillis, isBanClick);
    }

    public static void shake(View view, float cycles, boolean isBanClick) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, cycles, 700L, isBanClick);
    }

    public static void shake(View view, float cycles, long durationMillis) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, cycles, durationMillis, false);
    }

    public static void shake(View view, long durationMillis, boolean isBanClick) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, 7.0F, durationMillis, isBanClick);
    }

    public static void shake(View view, float cycles) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, cycles, 700L, false);
    }

    public static void shake(View view, long durationMillis) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, 7.0F, durationMillis, false);
    }

    public static void shake(View view, boolean isBanClick) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, 7.0F, 700L, isBanClick);
    }

    public static void shake(View view) {
        translate(view, 0.0F, 10.0F, 0.0F, 0.0F, 7.0F, 700L, false);
    }
}

