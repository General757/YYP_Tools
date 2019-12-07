//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by generalYan on 2019/10/21.
 */

public final class WindowUtils {
    private WindowUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static int measureWidth(int measureSpec, int defaultWidth) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int result = defaultWidth;
        if (specMode == -2147483648) {
            result = specSize;
        } else if (specMode == 1073741824) {
            result = specSize;
        }

        return result;
    }

    public static int measureHeight(int measureSpec, int defaultHeight) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int result = defaultHeight;
        if (specMode == -2147483648) {
            result = specSize;
        } else if (specMode == 1073741824) {
            result = specSize;
        }

        return result;
    }

    public static int getDisplayRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
            default:
                return 0;
        }
    }

    public static final boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static final boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    public static void setTransparencyBar(Activity activity) {
        Window window;
        if (Build.VERSION.SDK_INT >= 21) {
            window = activity.getWindow();
            window.clearFlags(201326592);
            window.getDecorView().setSystemUiVisibility(1792);
            window.addFlags(-2147483648);
            window.setStatusBarColor(0);
        } else if (Build.VERSION.SDK_INT >= 19) {
            window = activity.getWindow();
            window.setFlags(67108864, 67108864);
        }

    }

    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(67108864);
            window.addFlags(-2147483648);
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup mContentView = (ViewGroup) activity.findViewById(android.R.id.content);
            window.addFlags(67108864);
            int statusBarHeight = getStatusHeight(activity);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    lp.topMargin += statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
                statusBarView.setBackgroundColor(color);
                return;
            }

            statusBarView = new View(activity);
            android.view.ViewGroup.LayoutParams lp = new android.view.ViewGroup.LayoutParams(-1, statusBarHeight);
            statusBarView.setBackgroundColor(color);
            mContentView.addView(statusBarView, 0, lp);
        }

    }

    public static void setStatusBarColor(Activity activity, int colorId, boolean isFollow) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            if (!isFollow) {
                window.setStatusBarColor(colorId);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            setTransparencyBar(activity);
            ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }

            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(colorId);
        }

    }

    public static int setStatusBarLightMode(Activity activity, boolean dark) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            if (mIUISetStatusBarLightMode(activity.getWindow(), dark)) {
                result = 1;
            } else if (flymeSetStatusBarLightMode(activity.getWindow(), dark)) {
                result = 2;
            } else if (otherSetStatusBarLightMode(activity.getWindow(), dark)) {
                result = 3;
            }

            if (!dark) {
                result = 0;
            }
        }

        return result;
    }

    public static boolean otherSetStatusBarLightMode(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (dark) {
                window.getDecorView().setSystemUiVisibility(8192);
                return true;
            } else {
                window.getDecorView().setSystemUiVisibility(0);
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean flymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                android.view.WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = android.view.WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = android.view.WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt((Object) null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }

                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception var8) {
                ;
            }
        }

        return result;
    }

    public static boolean mIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();

            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }

                result = true;
            } catch (Exception var8) {
                ;
            }
        }

        return result;
    }

    public static void setStatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            mIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            flymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(9216);
        }

    }

    public static void setStatusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            mIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            flymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    public static int getTotalScreenHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return dpi;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getStatusHeight(Context context) {
        int statusHeight = -1;

        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return statusHeight;
    }

    public static boolean isStatusBarUsed(Context context) {
        try {
            View decorView = ((Activity) context).getWindow().getDecorView();
            int state = decorView.getSystemUiVisibility();
            if ((state | 256) == state) {
                return false;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return true;
    }

    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public static boolean hasShortcut(Activity activity, int app_name) {
        boolean isInstallShortcut = false;
        ContentResolver cr = activity.getContentResolver();
        String AUTHORITY = "com.android.launcher.settings";
        Uri CONTENT_URI = Uri.parse("content://com.android.launcher.settings/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{activity.getString(app_name).trim()}, (String) null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }

        return isInstallShortcut;
    }

    public static void addShortcut(Activity activity, int app_name, int ic_launcher) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra("android.intent.extra.shortcut.NAME", activity.getString(app_name));
        shortcut.putExtra("duplicate", false);
        Intent shortcutIntent = new Intent("android.intent.action.MAIN");
        shortcutIntent.setClassName(activity, activity.getClass().getName());
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, ic_launcher);
        shortcut.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", iconRes);
        activity.sendBroadcast(shortcut);
    }

    public static void delShortcut(Activity activity, int app_name) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra("android.intent.extra.shortcut.NAME", activity.getString(app_name));
        String appClass = activity.getPackageName() + "." + activity.getLocalClassName();
        ComponentName comp = new ComponentName(activity.getPackageName(), appClass);
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", (new Intent("android.intent.action.MAIN")).setComponent(comp));
        activity.sendBroadcast(shortcut);
    }

    //Android跳转到应用市场详情页面
    public static void launchAppDetail(Context context, String marketPkg) {
        String appPkg = "com.linyun.show";
        if (!TextUtils.isEmpty(appPkg)) {
            Intent goToMarket = new Intent();

            try {
                String sysName = Build.FINGERPRINT;
                String BRAND = Build.BRAND;
                LogUtils.i("!!! market:" + BRAND);
                Uri uri;
                if ((sysName.toLowerCase().contains("samsung") || BRAND.toLowerCase().contains("samsung")) && (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT >= 24)) {
                    uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + appPkg);
                    goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
                    goToMarket.setData(uri);
                } else if (!sysName.toLowerCase().contains("letv") && !BRAND.toLowerCase().contains("letv")) {
                    uri = Uri.parse("market://details?id=" + appPkg);
                    goToMarket = new Intent("android.intent.action.VIEW", uri);
                    if (!TextUtils.isEmpty(marketPkg)) {
                        goToMarket.setPackage(marketPkg);
                    }

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    goToMarket.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
                    goToMarket.setAction("com.letv.app.appstore.appdetailactivity");
                    goToMarket.putExtra("packageName", appPkg);
                }

                context.startActivity(goToMarket);
            } catch (Exception var9) {
                var9.printStackTrace();
                Uri uri2 = Uri.parse("market://details?id=" + appPkg);
                Intent intent = new Intent("android.intent.action.VIEW", uri2);

                try {
                    intent.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
                    context.startActivity(intent);
                } catch (ActivityNotFoundException var8) {
                    var8.printStackTrace();
                }
            }
        }
    }
}

