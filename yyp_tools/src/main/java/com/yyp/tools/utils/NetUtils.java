//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by generalYan on 2019/10/21.
 * 网络工具
 */
public class NetUtils {
    private NetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; ++i) {
                    if (info[i].getState() == State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }

        return netstate;
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled("gps");
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }

    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 0;
    }

    public static boolean is4G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnectedOrConnecting() && activeNetInfo.getType() == 13;
    }

    public static boolean isWiFi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        State wifi = manager.getNetworkInfo(1).getState();
        return wifi == State.CONNECTED || wifi == State.CONNECTING;
    }

    public static boolean isIP(String ip) {
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static int ipToInt(String addr) {
        String[] addrArray = addr.split("\\.");
        int num = 0;

        for (int i = 0; i < addrArray.length; ++i) {
            int power = 3 - i;
            num = (int) ((double) num + (double) (Integer.parseInt(addrArray[i]) % 256) * Math.pow(256.0D, (double) power));
        }

        return num;
    }

    public NetUtils.NetState isConnected(Context context) {
        NetUtils.NetState stateCode = NetUtils.NetState.NET_NO;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            switch (ni.getType()) {
                case 0:
                    switch (ni.getSubtype()) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            stateCode = NetUtils.NetState.NET_2G;
                            return stateCode;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                            stateCode = NetUtils.NetState.NET_3G;
                            return stateCode;
                        case 13:
                            stateCode = NetUtils.NetState.NET_4G;
                            return stateCode;
                        default:
                            stateCode = NetUtils.NetState.NET_UNKNOWN;
                            return stateCode;
                    }
                case 1:
                    stateCode = NetUtils.NetState.NET_WIFI;
                    break;
                default:
                    stateCode = NetUtils.NetState.NET_UNKNOWN;
            }
        }

        return stateCode;
    }

    public static Map<String, String> getUrlParams(String url) {
        Map<String, String> map = null;
        if (url != null && url.indexOf("&") > -1 && url.indexOf("=") > -1) {
            map = new HashMap();
            String[] arrTemp = url.split("&");
            String[] var3 = arrTemp;
            int var4 = arrTemp.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String str = var3[var5];
                String[] qs = str.split("=");
                map.put(qs[0], qs[1]);
            }
        }

        return map;
    }

    public static enum NetState {
        NET_NO,
        NET_2G,
        NET_3G,
        NET_4G,
        NET_WIFI,
        NET_UNKNOWN;

        private NetState() {
        }
    }

    // 手机网络类型
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    /**
     * 当前是否有网
     *
     * @param context
     * @return
     */
    public static boolean hasInternet(Context context) {
        boolean flag;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null && manager.getActiveNetworkInfo() != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo != null && !extraInfo.isEmpty()) {
                if (extraInfo.equalsIgnoreCase("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public static boolean netIsConnected(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //手机网络连接状态
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //WIFI连接状态
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            //当前无可用的网络
            return false;
        }
        return true;
    }

    /**
     * wifi是否开启
     *
     * @param context
     * @return
     */
    public static boolean isWifiOpen(Context context) {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // check the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }
}

