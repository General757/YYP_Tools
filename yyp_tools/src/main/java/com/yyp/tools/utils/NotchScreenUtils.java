package com.yyp.tools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by generalYan on 2019/1/11.
 * notch screen (刘海屏) 检测utils
 */
public class NotchScreenUtils {
    private static final String TAG = "NotchScreenUtils...";

    /**
     * 设置应用窗口在华为notch手机使用刘海区的flag值, 该值为华为官方提供, 不要修改
     */
    private static final int FLAG_NOTCH_SUPPORT_HW = 0x00010000;

    /**
     * vivo手机判断是否是notch, vivo官方提供, 不要修改
     */
    private static final int FLAG_NOTCH_SUPPORT_VIVO = 0x00000020;


    public static boolean checkNotchScreen(Context context) {
        if (checkHuaWei(context)) {
            return true;
        } else if (checkVivo(context)) {
            return true;
        } else if (checkMiUI(context)) {
            return true;
        } else if (checkOppo(context)) {
            return true;
        }

        return false;
    }

    /**
     * oppo提供: 刘海屏判断.
     *
     * @return true, 刘海屏; false: 非刘海屏
     */
    private static boolean checkOppo(Context context) {
        try {
            return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Exception e) {
            Log.e(TAG, "checkOppo notchScreen exception");
        }
        return false;
    }

    /**
     * 小米提供: 刘海屏判断.
     *
     * @return true, 刘海屏; false: 非刘海屏
     */
    private static boolean checkMiUI(Context context) {

        int result = 0;
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressLint("PrivateApi")
            @SuppressWarnings("rawtypes")
            Class systemProperties = classLoader.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = int.class;
            Method getInt = systemProperties.getMethod("getInt", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = "ro.miui.notch";
            params[1] = 0;
            result = (Integer) getInt.invoke(systemProperties, params);
            return result == 1;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 华为提供: 判断是否是刘海屏
     *
     * @param context Context
     * @return true：刘海屏；false：非刘海屏
     */
    private static boolean checkHuaWei(Context context) {

        boolean ret = false;

        try {

            ClassLoader cl = context.getClassLoader();

            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");

            Method get = hwNotchSizeUtil.getMethod("hasNotchInScreen");

            ret = (boolean) get.invoke(hwNotchSizeUtil);

        } catch (ClassNotFoundException e) {
            Log.e(TAG, "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e(TAG, "hasNotchInScreen Exception");

        }
        return ret;
    }
    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    /**
     * vivo提供: 判断是否是刘海屏
     *
     * @param context Context
     * @return true：是刘海屏；false：非刘海屏
     */
    private static boolean checkVivo(Context context) {

//        boolean ret;
//        try {
//            ClassLoader cl = context.getClassLoader();
//            @SuppressLint("PrivateApi")
//            Class ftFeature = cl.loadClass("android.util.FtFeature");
//            Method isFeatureSupport = ftFeature.getMethod("isFeatureSupport");
//            ret = (boolean) isFeatureSupport.invoke(ftFeature, FLAG_NOTCH_SUPPORT_VIVO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ret = false;
//        }
        /**
         * 屏幕是否有凹槽
         * 在vivo系统中，增加一个接口来判断此设备是否具有凹槽，开发者可以使用反射的形式调用
         * 包名：android.util.FtFeature
         * 接口：public static boolean isFeatureSupport(int mask);
         * 参数说明：0x00000020 表示是否有凹槽 0x00000008 表示是否有圆角
         * 返回值： true 表示具备此特征，false表示没有此特征
         * 注意：一定要使用反射调用
         */
        boolean hasNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class ftFeature = cl.loadClass("android.util.FtFeature");
//            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
//            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
            Method[] methods = ftFeature.getDeclaredMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    if (method != null) {
                        if (method.getName().equalsIgnoreCase("isFeatureSupport")) {
                            hasNotch = (boolean) method.invoke(ftFeature, VIVO_NOTCH);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasNotch = false;
        }

        return hasNotch;
    }

    /**
     * 华为提供: 获取刘海尺寸
     *
     * @param context Context
     * @return int[0]值为刘海宽度 int[1]值为刘海高度。
     */
    public static int[] getNotchSize(Context context) {

        int[] ret = new int[]{0, 0};

        try {

            ClassLoader cl = context.getClassLoader();

            Class hwnotchsizeutil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");

            Method get = hwnotchsizeutil.getMethod("getNotchSize");

            ret = (int[]) get.invoke(hwnotchsizeutil);

        } catch (ClassNotFoundException e) {

            Log.e("test", "getNotchSize ClassNotFoundException");

        } catch (NoSuchMethodException e) {

            Log.e("test", "getNotchSize NoSuchMethodException");

        } catch (Exception e) {

            Log.e("test", "getNotchSize Exception");

        }
        return ret;
    }

    /**
     * 华为提供: 设置应用窗口在华为刘海屏手机使用刘海区
     *
     * @param window 应用页面window对象
     */
    public static void setFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("addHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT_HW);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "hw add notch screen flag api error");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "hw add notch screen flag api error");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "hw add notch screen flag api error");
        } catch (InstantiationException e) {
            Log.e(TAG, "hw add notch screen flag api error");
        } catch (InvocationTargetException e) {
            Log.e(TAG, "hw add notch screen flag api error");
        } catch (Exception e) {
            Log.e(TAG, "other Exception");
        }
    }

    /**
     * 华为提供: 设置应用窗口在华为刘海屏手机不使用刘海区
     *
     * @param window 应用页面window对象
     */
    public static void setNotFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        try {
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("clearHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT_HW);
            Log.e(TAG, "............clear");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "hw clear notch screen flag api error");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "hw clear notch screen flag api error");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "hw clear notch screen flag api error");
        } catch (InstantiationException e) {
            Log.e(TAG, "hw clear notch screen flag api error");
        } catch (InvocationTargetException e) {
            Log.e(TAG, "hw clear notch screen flag api error");
        } catch (Exception e) {
            Log.e(TAG, "other Exception");
        }
    }
}

