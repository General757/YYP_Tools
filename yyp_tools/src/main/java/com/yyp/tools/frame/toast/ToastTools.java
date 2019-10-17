package com.yyp.tools.frame.toast;

import android.content.Context;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YanYan on 2019/3/16.
 * 提示工具
 */

public class ToastTools {

    private static Toast toast;

    public static void showToastShort(Context mContext, CharSequence message) {
        if (mContext == null)
            return;
        try {
            if (null == toast)
                toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            else
                toast.setText(message);

            toast.show();
        } catch (Exception var3) {
            var3.printStackTrace();
            if (toast != null)
                toast.cancel();
        }
    }

    public static void showToastShort(Context mContext, int message) {
        if (mContext == null)
            return;
        try {
            if (null == toast)
                toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            else
                toast.setText(message);

            toast.show();
        } catch (Exception var3) {
            var3.printStackTrace();
            if (toast != null)
                toast.cancel();
        }
    }

    public static void showToastLong(Context mContext, CharSequence message) {
        if (mContext == null)
            return;
        try {
            if (null == toast)
                toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            else
                toast.setText(message);

            toast.show();
        } catch (Exception var3) {
            var3.printStackTrace();
            if (toast != null)
                toast.cancel();
        }
    }

    public static void showToastLong(Context mContext, int message) {
        if (mContext == null)
            return;
        try {
            if (null == toast)
                toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            else
                toast.setText(message);

            toast.show();
        } catch (Exception var3) {
            var3.printStackTrace();
            if (toast != null)
                toast.cancel();
        }
    }

    public static void showToast(Context mContext, CharSequence message, int duration) {
        if (mContext == null)
            return;
        try {
            if (null == toast)
                toast = Toast.makeText(mContext, message, duration);
            else
                toast.setText(message);

            toast.show();
        } catch (Exception var3) {
            var3.printStackTrace();
            if (toast != null)
                toast.cancel();
        }
    }

    public static void showToast(Context mContext, int message, int duration) {
        if (mContext == null)
            return;
        try {
            if (null == toast)
                toast = Toast.makeText(mContext, message, duration);
            else
                toast.setText(message);

            toast.show();
        } catch (Exception var3) {
            var3.printStackTrace();
            if (toast != null)
                toast.cancel();
        }
    }

    public static void hideToast() {
        try {
            if (null != toast)
                toast.cancel();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static void showTimes(Context context, String message, int duration) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                toast.setText(message);
            }

            toast.show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, duration);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
