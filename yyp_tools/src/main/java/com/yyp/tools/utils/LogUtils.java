//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Yan on 2019/10/11.
 * 日志工具类
 */

public final class LogUtils {
    private static volatile String LOG_TAG = "### LogUtils ";
    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static volatile boolean DISABLED = false;//false输出日志true禁止日志

    private LogUtils() {
    }

    public static void changeLogTag(String tag) {
        LOG_TAG = tag;
    }

    public static void enableLogging() {
        DISABLED = false;
    }

    public static void disableLogging() {
        DISABLED = true;
    }

    public static void v(String msg, Object... args) {
        v(LOG_TAG, msg, args);
    }

    public static void v(String tag, String msg, Object... args) {
        v(tag, msg, null, args);
    }

    public static void v(String msg, Throwable tr, Object... args) {
        v(LOG_TAG, msg, tr, args);
    }

    public static void v(String tag, String msg, Throwable tr, Object... args) {
        log(Log.VERBOSE, TextUtils.isEmpty(tag) ? LOG_TAG : tag, tr, msg, args);
    }

    public static void d(String msg, Object... args) {
        d(LOG_TAG, msg, args);
    }

    public static void d(String tag, String msg, Object... args) {
        d(tag, msg, null, args);
    }

    public static void d(String msg, Throwable tr, Object... args) {
        d(LOG_TAG, msg, tr, args);
    }

    public static void d(String tag, String msg, Throwable tr, Object... args) {
        log(Log.DEBUG, TextUtils.isEmpty(tag) ? LOG_TAG : tag, tr, msg, args);
    }

    public static void i(String msg, Object... args) {
        i(LOG_TAG, msg, args);
    }

    public static void i(String tag, String msg, Object... args) {
        i(tag, msg, null, args);
    }

    public static void i(String msg, Throwable tr, Object... args) {
        i(LOG_TAG, msg, tr, args);
    }

    public static void i(String tag, String msg, Throwable tr, Object... args) {
        log(Log.INFO, TextUtils.isEmpty(tag) ? LOG_TAG : tag, tr, msg, args);
    }

    public static void w(String msg, Object... args) {
        w(LOG_TAG, msg, args);
    }

    public static void w(String tag, String msg, Object... args) {
        w(tag, msg, null, args);
    }

    public static void w(Throwable tr, Object... args) {
        w(LOG_TAG, "", tr, args);
    }

    public static void w(String msg, Throwable tr, Object... args) {
        w(LOG_TAG, msg, tr, args);
    }

    public static void w(String tag, String msg, Throwable tr, Object... args) {
        log(Log.WARN, TextUtils.isEmpty(tag) ? LOG_TAG : tag, tr, msg, args);
    }

    public static void e(String msg, Object... args) {
        e(LOG_TAG, msg, msg);
    }

    public static void e(String tag, String msg, Object... args) {
        e(tag, msg, null, args);
    }

    public static void e(String msg, Throwable tr, Object... args) {
        e(LOG_TAG, msg, tr, args);
    }

    public static void e(String tag, String msg, Throwable tr, Object... args) {
        log(Log.ERROR, TextUtils.isEmpty(tag) ? LOG_TAG : tag, tr, msg, args);
    }

    private static void log(int priority, String tag, Throwable ex, String message, Object... args) {
        if (!DISABLED) {
            if (args.length > 0)
                message = String.format(message, args);

            String var4;
            if (ex == null) {
                var4 = message;
            } else {
                String var5 = message == null ? ex.getMessage() : message;
                String var6 = Log.getStackTraceString(ex);
                var4 = String.format(LOG_FORMAT, var5, var6);
            }

            Log.println(priority, tag, var4);
        }
    }
}

