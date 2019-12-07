package com.yyp.tools.utils;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by generalYan on 2018/11/12.
 * 留海屏-缺口工具
 */
public class NotchUtils {

    public void getPhoneManufacturer() {
        String name = android.os.Build.MANUFACTURER;
        LogUtils.e("---", "MANUFACTURER --- name : " + name);
        switch (name) {
            case "HUAWEI":
                break;
            case "vivo":
                break;
            case "OPPO":
                break;
            case "Coolpad":
                break;
            case "Meizu":
                break;
            case "Xiaomi":
                break;
            case "samsung":
                break;
            case "Sony":
                break;
            case "LG":
                break;
            default:
                break;
        }
    }

    public static int getNotchStatusBarHeight(Context mContext) {
        String name = android.os.Build.MANUFACTURER;
        LogUtils.e("---", "MANUFACTURER --- name : " + name);
        int barHeight = 0;
        switch (name) {
            case "Huawei":
                if (hasNotchInHuawei(mContext))
                    barHeight = getNotchSize(mContext)[1];
                break;
            case "vivo":
                if (hasNotchInVivo(mContext))
                    barHeight = getStatusBarHeight(mContext);
                break;
            case "OPPO":
                if (hasNotchInOppo(mContext))
                    barHeight = getStatusBarHeight(mContext);
                break;
            case "Coolpad":
                break;
            case "Meizu":
                break;
            case "Xiaomi":
                break;
            case "samsung":
                break;
            case "Sony":
                break;
            case "LG":
                break;
            default:
                break;
        }
        return barHeight;
    }

    //华为-判断该华为手机是否刘海屏
    public static boolean hasNotchInHuawei(Context context) {
        boolean hasNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method hasNotchInScreen = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            if (hasNotchInScreen != null)
                hasNotch = (boolean) hasNotchInScreen.invoke(HwNotchSizeUtil);

        } catch (ClassNotFoundException e) {
            LogUtils.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtils.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            LogUtils.e("test", "hasNotchInScreen Exception");
        } finally {
            return hasNotch;
        }
    }

    //华为-获取刘海的高度
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            if (get != null)
                ret = (int[]) get.invoke(HwNotchSizeUtil);

        } catch (ClassNotFoundException e) {
            LogUtils.e("test", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogUtils.e("test", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            LogUtils.e("test", "getNotchSize Exception");
        } finally {
            return ret;
        }
    }

    //华为-华为刘海屏flag动态添加和删除代码
    boolean isAdd = true;

    public void setNotchView(WindowManager windowManager, Window window) {
        if (isAdd) {//add flag
            isAdd = false;
            setFullScreenWindowLayoutInDisplayCutout(window);
            windowManager.updateViewLayout(window.getDecorView(), window.getDecorView().getLayoutParams());
        } else {//clear flag
            isAdd = true;
            setNotFullScreenWindowLayoutInDisplayCutout(window);
            windowManager.updateViewLayout(window.getDecorView(), window.getDecorView().getLayoutParams());
        }
    }

    //华为-应用通过增加华为自定义的刘海屏flag
    public static final int FLAG_NOTCH_SUPPORT = 0x00010000;

    /**
     * 刘海屏全屏显示FLAG
     * 设置应用窗口在华为刘海屏手机使用刘海区
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
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        }
//        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException
//                | InvocationTargetException e) {
//            SystemTools.logE("test", "hw add notch screen flag api error");
//        }
        catch (Exception e) {
            LogUtils.e("test", "other Exception");
        }
    }

    //华为-可以通过clearHwFlags接口清除添加的华为刘海屏Flag

    /**
     * 刘海屏全屏显示FLAG
     * 设置应用窗口在华为刘海屏手机使用刘海区
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
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        }
//        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException
//                | InvocationTargetException e) {
//            SystemTools.logE("test", "hw clear notch screen flag api error");
//        }
        catch (Exception e) {
            LogUtils.e("test", "other Exception");
        }
    }

    //oppo-判断该 OPPO 手机是否为刘海屏手机
    public static boolean hasNotchInOppo(Context context) {
        /**
         * 凹形屏坐标获取方法：
         获取ro.oppo.screen.heteromorphism属性值可获取凹形区域的范围，
         例 [ro.oppo.screen.heteromorphism]: [378,0:702,80]，含义如下 ：
         378：表示竖屏下左上角横坐标
         0 ：表示竖屏下左上角竖坐标
         702：表示竖屏下右下角横坐标
         80 ：表示竖屏下右下角竖坐标
         */
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    //vivo-判断该 vivo 手机是否为刘海屏手机
    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    public static boolean hasNotchInVivo(Context context) {
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

    //获取状态栏的高度
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

//    //google-判断该华为手机是否刘海屏
//    static boolean hasNotch = false;
//
//    @TargetApi(28)
//    public static boolean hasNotchInGoogle(Context context, Window window) {
//        hasNotch = false;
//
//        final View decorView = window.getDecorView();
//        decorView.post(new Runnable() {
//            @Override
//            public void run() {
//                DisplayCutout displayCutout = decorView.getRootWindowInsets().getDisplayCutout();
//                SystemTools.logE("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
//                SystemTools.logE("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
//                SystemTools.logE("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
//                SystemTools.logE("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());
//
//                List<Rect> rects = displayCutout.getBoundingRects();
//                if (rects == null || rects.size() == 0) {
//                    hasNotch = false;
//                    SystemTools.logE("TAG", "不是刘海屏");
//                } else {
//                    hasNotch = true;
//                    SystemTools.logE("TAG", "刘海屏数量:" + rects.size());
//                    for (Rect rect : rects) {
//                        SystemTools.logE("TAG", "刘海屏区域：" + rect);
//                    }
//                }
//            }
//        });
//
//        return hasNotch;
//    }

}
