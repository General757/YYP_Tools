//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by generalYan on 2019/10/21.
 */
public class ImeiUtils {
    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static UUID uuid;

    public ImeiUtils() {
    }

    public static String getIMEI(Context context) {
        if (uuid == null) {
            Class var1 = ImeiUtils.class;
            synchronized(ImeiUtils.class) {
                if (uuid == null) {
                    SharedPreferences prefs = context.getSharedPreferences("device_id.xml", 0);
                    String id = prefs.getString("device_id", (String)null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        String androidId = Secure.getString(context.getContentResolver(), "android_id");

                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                String deviceId = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
                                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException var7) {
                            throw new RuntimeException(var7);
                        }

                        prefs.edit().putString("device_id", uuid.toString()).commit();
                    }
                }
            }
        }

        return uuid.toString();
    }
}

