//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by generalYan on 2019/10/21.
 */
public class ImsiUtil {
    private Integer simId_1 = 0;
    private Integer simId_2 = 1;
    private String imsi_1 = "";
    private String imsi_2 = "";
    private String imei_1 = "";
    private String imei_2 = "";
    private Context mContext;

    public ImsiUtil(Context mContext) {
        this.mContext = mContext;
    }

    public ImsiUtil.IMSInfo getIMSInfo() {
        ImsiUtil.IMSInfo imsInfo = this.initQualcommDoubleSim();
        if (imsInfo != null) {
            return imsInfo;
        } else {
            imsInfo = this.initMtkDoubleSim();
            if (imsInfo != null) {
                return imsInfo;
            } else {
                imsInfo = this.initMtkSecondDoubleSim();
                if (imsInfo != null) {
                    return imsInfo;
                } else {
                    imsInfo = this.initSpreadDoubleSim();
                    if (imsInfo != null) {
                        return imsInfo;
                    } else {
                        imsInfo = this.getIMSI();
                        if (imsInfo != null) {
                            return imsInfo;
                        } else {
                            imsInfo = null;
                            return imsInfo;
                        }
                    }
                }
            }
        }
    }

    public ImsiUtil.IMSInfo initMtkDoubleSim() {
        ImsiUtil.IMSInfo imsInfo = null;

        try {
            TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService("phone");
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            this.simId_1 = (Integer) fields1.get((Object) null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            this.simId_2 = (Integer) fields2.get((Object) null);
            Method m = TelephonyManager.class.getDeclaredMethod("getSubscriberIdGemini", Integer.TYPE);
            this.imsi_1 = (String) m.invoke(tm, this.simId_1);
            this.imsi_2 = (String) m.invoke(tm, this.simId_2);
            Method m1 = TelephonyManager.class.getDeclaredMethod("getDeviceIdGemini", Integer.TYPE);
            this.imei_1 = (String) m1.invoke(tm, this.simId_1);
            this.imei_2 = (String) m1.invoke(tm, this.simId_2);
            imsInfo = new ImsiUtil.IMSInfo();
            imsInfo.chipName = "MTK芯片";
            imsInfo.imei_1 = this.imei_1;
            imsInfo.imei_2 = this.imei_2;
            imsInfo.imsi_1 = this.imsi_1;
            imsInfo.imsi_2 = this.imsi_2;
            return imsInfo;
        } catch (Exception var8) {
            imsInfo = null;
            return imsInfo;
        }
    }

    public ImsiUtil.IMSInfo initMtkSecondDoubleSim() {
        ImsiUtil.IMSInfo imsInfo = null;

        try {
            TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService("phone");
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            this.simId_1 = (Integer) fields1.get((Object) null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            this.simId_2 = (Integer) fields2.get((Object) null);
            Method mx = TelephonyManager.class.getMethod("getDefault", Integer.TYPE);
            TelephonyManager tm1 = (TelephonyManager) mx.invoke(tm, this.simId_1);
            TelephonyManager tm2 = (TelephonyManager) mx.invoke(tm, this.simId_2);
            this.imsi_1 = tm1.getSubscriberId();
            this.imsi_2 = tm2.getSubscriberId();
            this.imei_1 = tm1.getDeviceId();
            this.imei_2 = tm2.getDeviceId();
            imsInfo = new ImsiUtil.IMSInfo();
            imsInfo.chipName = "MTK芯片";
            imsInfo.imei_1 = this.imei_1;
            imsInfo.imei_2 = this.imei_2;
            imsInfo.imsi_1 = this.imsi_1;
            imsInfo.imsi_2 = this.imsi_2;
            return imsInfo;
        } catch (Exception var9) {
            imsInfo = null;
            return imsInfo;
        }
    }

    public ImsiUtil.IMSInfo initSpreadDoubleSim() {
        ImsiUtil.IMSInfo imsInfo = null;

        try {
            Class<?> c = Class.forName("com.android.internal.telephony.PhoneFactory");
            Method m = c.getMethod("getServiceName", String.class, Integer.TYPE);
            String spreadTmService = (String) m.invoke(c, "phone", 1);
            TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService("phone");
            this.imsi_1 = tm.getSubscriberId();
            this.imei_1 = tm.getDeviceId();
            TelephonyManager tm1 = (TelephonyManager) this.mContext.getSystemService(spreadTmService);
            this.imsi_2 = tm1.getSubscriberId();
            this.imei_2 = tm1.getDeviceId();
            imsInfo = new ImsiUtil.IMSInfo();
            imsInfo.chipName = "展讯芯片";
            imsInfo.imei_1 = this.imei_1;
            imsInfo.imei_2 = this.imei_2;
            imsInfo.imsi_1 = this.imsi_1;
            imsInfo.imsi_2 = this.imsi_2;
            return imsInfo;
        } catch (Exception var7) {
            imsInfo = null;
            return imsInfo;
        }
    }

    public ImsiUtil.IMSInfo initQualcommDoubleSim() {
        ImsiUtil.IMSInfo imsInfo = null;

        try {
            Class<?> cx = Class.forName("android.telephony.MSimTelephonyManager");
            Object obj = this.mContext.getSystemService("phone_msim");
            Method md = cx.getMethod("getDeviceId", Integer.TYPE);
            Method ms = cx.getMethod("getSubscriberId", Integer.TYPE);
            this.imei_1 = (String) md.invoke(obj, this.simId_1);
            this.imei_2 = (String) md.invoke(obj, this.simId_2);
            this.imsi_1 = (String) ms.invoke(obj, this.simId_1);
            this.imsi_2 = (String) ms.invoke(obj, this.simId_2);
            int statephoneType_2 = 0;
            boolean flag = false;

            try {
                Method mx = cx.getMethod("getPreferredDataSubscription", Integer.TYPE);
                Method is = cx.getMethod("isMultiSimEnabled", Integer.TYPE);
                statephoneType_2 = (Integer) mx.invoke(obj);
                flag = (Boolean) is.invoke(obj);
            } catch (Exception var10) {
                ;
            }

            imsInfo = new ImsiUtil.IMSInfo();
            imsInfo.chipName = "高通芯片-getPreferredDataSubscription:" + statephoneType_2 + ",flag:" + flag;
            imsInfo.imei_1 = this.imei_1;
            imsInfo.imei_2 = this.imei_2;
            imsInfo.imsi_1 = this.imsi_1;
            imsInfo.imsi_2 = this.imsi_2;
            return imsInfo;
        } catch (Exception var11) {
            imsInfo = null;
            return imsInfo;
        }
    }

    public ImsiUtil.IMSInfo getIMSI() {
        ImsiUtil.IMSInfo imsInfo = null;

        try {
            TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService("phone");
            this.imsi_1 = tm.getSubscriberId();
            this.imei_1 = tm.getDeviceId();
        } catch (Exception var3) {
            imsInfo = null;
            return imsInfo;
        }

        if (!TextUtils.isEmpty(this.imsi_1) && this.imsi_1.length() >= 10) {
            imsInfo = new ImsiUtil.IMSInfo();
            imsInfo.chipName = "单卡芯片";
            imsInfo.imei_1 = this.imei_1;
            imsInfo.imei_2 = "没有";
            imsInfo.imsi_1 = this.imsi_1;
            imsInfo.imsi_2 = "没有";
            return imsInfo;
        } else {
            imsInfo = null;
            return imsInfo;
        }
    }

    public class IMSInfo {
        public String chipName;
        public String imsi_1;
        public String imei_1;
        public String imsi_2;
        public String imei_2;

        public IMSInfo() {
        }
    }
}

