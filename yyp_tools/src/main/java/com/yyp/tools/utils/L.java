//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.util.Log;

/**
 * Created by Yan on 2019/10/11.
 */

public final class L {
    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static volatile boolean DISABLED = false;

    private L() {
    }

    public static void enableLogging() {
        DISABLED = false;
    }

    public static void disableLogging() {
        DISABLED = true;
    }

    public static void d(String message, Object... args) {
        log(3, (Throwable) null, message, args);
    }

    public static void i(String message, Object... args) {
        log(4, (Throwable) null, message, args);
    }

    public static void w(String message, Object... args) {
        log(5, (Throwable) null, message, args);
    }

    public static void e(Throwable ex) {
        log(6, ex, (String) null);
    }

    public static void e(String message, Object... args) {
        log(6, (Throwable) null, message, args);
    }

    public static void e(Throwable ex, String message, Object... args) {
        log(6, ex, message, args);
    }

    private static void log(int priority, Throwable ex, String message, Object... args) {
        if (!DISABLED) {
            if (args.length > 0) {
                message = String.format(message, args);
            }

            String var4;
            if (ex == null) {
                var4 = message;
            } else {
                String var5 = message == null ? ex.getMessage() : message;
                String var6 = Log.getStackTraceString(ex);
                var4 = String.format("%1$s\n%2$s", var5, var6);
            }

            Log.println(priority, "VideoLoader", var4);
        }
    }
}

