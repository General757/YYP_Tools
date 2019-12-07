//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

/**
 * Created by generalYan on 2019/10/21.
 * 轮询工具
 */
public class PollingUtils {
    private static final boolean DEBUG = true;
    private static final String TAG = "PollingUtils";

    private PollingUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static boolean isPollingServiceExist(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 536870912);
        if (pendingIntent != null) {
            LogUtils.v("PollingUtils", new Object[]{pendingIntent.toString()});
        }

        LogUtils.v("PollingUtils", new Object[]{pendingIntent != null ? "Exist" : "Not exist"});
        return pendingIntent != null;
    }

    public static void startPollingService(Context context, int interval, Class<?> cls) {
        startPollingService(context, interval, cls, (String) null);
    }

    public static void startPollingService(Context context, int interval, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 134217728);
        long triggerAtTime = SystemClock.elapsedRealtime();
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, (long) (interval * 1000), pendingIntent);
    }

    public static void stopPollingService(Context context, Class<?> cls) {
        stopPollingService(context, cls, (String) null);
    }

    public static void stopPollingService(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 134217728);
        manager.cancel(pendingIntent);
    }
}

