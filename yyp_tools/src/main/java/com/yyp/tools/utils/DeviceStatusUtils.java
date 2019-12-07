//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

@TargetApi(17)
public class DeviceStatusUtils {
    private DeviceStatusUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static int getScreenBrightnessModeState(Context context) {
        return System.getInt(context.getContentResolver(), "screen_brightness_mode", 1);
    }

    public static boolean isScreenBrightnessModeAuto(Context context) {
        return getScreenBrightnessModeState(context) == 1;
    }

    public static boolean setScreenBrightnessMode(Context context, boolean auto) {
        boolean result = true;
        if (isScreenBrightnessModeAuto(context) != auto) {
            result = System.putInt(context.getContentResolver(), "screen_brightness_mode", auto ? 1 : 0);
        }

        return result;
    }

    public static int getScreenBrightness(Context context) {
        return System.getInt(context.getContentResolver(), "screen_brightness", 255);
    }

    public static boolean setScreenBrightness(Context context, int screenBrightness) {
        int brightness = screenBrightness;
        if (screenBrightness < 1) {
            brightness = 1;
        } else if (screenBrightness > 255) {
            brightness = screenBrightness % 255;
            if (brightness == 0) {
                brightness = 255;
            }
        }

        boolean result = System.putInt(context.getContentResolver(), "screen_brightness", brightness);
        return result;
    }

    public static void setWindowBrightness(Activity activity, float screenBrightness) {
        float brightness = screenBrightness;
        if (screenBrightness < 1.0F) {
            brightness = 1.0F;
        } else if (screenBrightness > 255.0F) {
            brightness = screenBrightness % 255.0F;
            if (brightness == 0.0F) {
                brightness = 255.0F;
            }
        }

        Window window = activity.getWindow();
        LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.screenBrightness = brightness / 255.0F;
        window.setAttributes(localLayoutParams);
    }

    public static boolean setScreenBrightnessAndApply(Activity activity, int screenBrightness) {
        boolean result = true;
        result = setScreenBrightness(activity, screenBrightness);
        if (result) {
            setWindowBrightness(activity, (float) screenBrightness);
        }

        return result;
    }

    public static int getScreenDormantTime(Context context) {
        return System.getInt(context.getContentResolver(), "screen_off_timeout", 30000);
    }

    public static boolean setScreenDormantTime(Context context, int millis) {
        return System.putInt(context.getContentResolver(), "screen_off_timeout", millis);
    }

    public static int getAirplaneModeState(Context context) {
        return VERSION.SDK_INT < 17 ? System.getInt(context.getContentResolver(), "airplane_mode_on", 0) : Global.getInt(context.getContentResolver(), "airplane_mode_on", 0);
    }

    public static boolean isAirplaneModeOpen(Context context) {
        return getAirplaneModeState(context) == 1;
    }

    @TargetApi(17)
    public static boolean setAirplaneMode(Context context, boolean enable) {
        boolean result = true;
        if (isAirplaneModeOpen(context) != enable) {
            if (VERSION.SDK_INT < 17) {
                result = System.putInt(context.getContentResolver(), "airplane_mode_on", enable ? 1 : 0);
            } else {
                result = Global.putInt(context.getContentResolver(), "airplane_mode_on", enable ? 1 : 0);
            }

            context.sendBroadcast(new Intent("android.intent.action.AIRPLANE_MODE"));
        }

        return result;
    }

    public static int getBluetoothState() throws Exception {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new Exception("bluetooth device not found!");
        } else {
            return bluetoothAdapter.getState();
        }
    }

    public static boolean isBluetoothOpen() throws Exception {
        int bluetoothStateCode = getBluetoothState();
        return bluetoothStateCode == 12 || bluetoothStateCode == 11;
    }

    public static void setBluetooth(boolean enable) throws Exception {
        if (isBluetoothOpen() != enable) {
            if (enable) {
                BluetoothAdapter.getDefaultAdapter().enable();
            } else {
                BluetoothAdapter.getDefaultAdapter().disable();
            }
        }

    }
}

