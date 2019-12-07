//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DistanceUtil {
    public DistanceUtil() {
    }

    public static int getCameraAlbumWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        return (dm.widthPixels - dp2px(10.0F, dm)) / 4 - dp2px(4.0F, dm);
    }

    public static int getCameraPhotoAreaHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels / 4 - dp2px(2.0F, dm) + dp2px(4.0F, dm);
    }

    public static int getCameraPhotoWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width / 4 - (int)(0.5D + 2.0D * (double)dm.density);
    }

    public static int getActivityHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        return (dm.widthPixels - dp2px(24.0F, dm)) / 3;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int dp2px(float f, DisplayMetrics dm) {
        return (int)(0.5F + f * dm.density);
    }

    public static int dp2px(Context context, float f) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService("window");
        wm.getDefaultDisplay().getMetrics(dm);
        return (int)(0.5F + f * dm.density);
    }
}

