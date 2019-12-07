//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.os.Build.VERSION;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

/**
 * Created by generalYan on 2019/10/21.
 */
public class AppUtils {
    private static final boolean DEBUG = true;
    private static final String TAG = "AppUtils";
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    private AppUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static int getVerCode(Context context) {
        int verCode = -1;

        try {
            String packageName = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";

        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return verName;
    }

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void installApk(Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList = activityManager.getRunningServices(2147483647);
        Iterator var5 = servicesList.iterator();

        while (var5.hasNext()) {
            RunningServiceInfo si = (RunningServiceInfo) var5.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        boolean ret = false;

        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if (intent_service != null) {
            ret = context.stopService(intent_service);
        }

        return ret;
    }

    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            return files.length;
        } catch (Exception var2) {
            return 1;
        }
    }

    public static boolean isNamedProcess(Context context, String processName) {
        if (context != null && !TextUtils.isEmpty(processName)) {
            int pid = Process.myPid();
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
            if (processInfoList == null) {
                return true;
            } else {
                Iterator var5 = manager.getRunningAppProcesses().iterator();

                RunningAppProcessInfo processInfo;
                do {
                    if (!var5.hasNext()) {
                        return false;
                    }

                    processInfo = (RunningAppProcessInfo) var5.next();
                }
                while (processInfo.pid != pid || !processName.equalsIgnoreCase(processInfo.processName));

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = ((RunningTaskInfo) taskList.get(0)).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, 64);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static String hexdigest(byte[] paramArrayOfByte) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;

            for (int j = 0; i < 16; ++j) {
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[15 & k >>> 4];
                ++j;
                arrayOfChar[j] = hexDigits[k & 15];
                ++i;
            }

            return new String(arrayOfChar);
        } catch (Exception var8) {
            var8.printStackTrace();
            return "";
        }
    }

//    public static int gc(Context context) {
//        long i = (long) getDeviceUsableMemory(context);
//        int count = 0;
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<RunningServiceInfo> serviceList = am.getRunningServices(100);
//        if (serviceList != null) {
//            Iterator var6 = serviceList.iterator();
//
//            while (var6.hasNext()) {
//                RunningServiceInfo service = (RunningServiceInfo) var6.next();
//                if (service.pid != Process.myPid()) {
//                    try {
//                        Process.killProcess(service.pid);
//                        ++count;
//                    } catch (Exception var16) {
//                        var16.getStackTrace();
//                    }
//                }
//            }
//        }
//
//        List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
//        if (processList != null) {
//            Iterator var18 = processList.iterator();
//
//            label46:
//            while (true) {
//                RunningAppProcessInfo process;
//                do {
//                    if (!var18.hasNext()) {
//                        break label46;
//                    }
//
//                    process = (RunningAppProcessInfo) var18.next();
//                } while (process.importance <= 200);
//
//                String[] pkgList = process.pkgList;
//                String[] var10 = pkgList;
//                int var11 = pkgList.length;
//
//                for (int var12 = 0; var12 < var11; ++var12) {
//                    String pkgName = var10[var12];
//                    LogUtils.d("AppUtils", new Object[]{"======正在杀死包名：" + pkgName});
//
//                    try {
//                        am.killBackgroundProcesses(pkgName);
//                        ++count;
//                    } catch (Exception var15) {
//                        var15.getStackTrace();
//                    }
//                }
//            }
//        }
//
//        LogUtils.d("AppUtils", new Object[]{"清理了" + ((long) getDeviceUsableMemory(context) - i) + "M内存"});
//        return count;
//    }

    public static int getDeviceUsableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        return (int) (mi.availMem / 1048576L);
    }

    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList();
        PackageManager pManager = context.getPackageManager();
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);

        for (int i = 0; i < paklist.size(); ++i) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            if ((pak.applicationInfo.flags & 1) <= 0) {
                apps.add(pak);
            }
        }

        return apps;
    }

    public static String getMODEL() {
        return Build.MODEL;
    }

    public static String getRELEASE() {
        return VERSION.RELEASE;
    }

    public static int getSDKVersion() {
        return VERSION.SDK_INT;
    }

    public static boolean isDalvik() {
        return "Dalvik".equals(getCurrentRuntimeValue());
    }

    public static boolean isART() {
        String currentRuntime = getCurrentRuntimeValue();
        return "ART".equals(currentRuntime) || "ART debug build".equals(currentRuntime);
    }

    public static String getCurrentRuntimeValue() {
        try {
            Class systemProperties = Class.forName("android.os.SystemProperties");

            try {
                Method get = systemProperties.getMethod("get", String.class, String.class);
                if (get == null) {
                    return "WTF?!";
                } else {
                    try {
                        String value = (String) get.invoke(systemProperties, "persist.sys.dalvik.vm.lib", "Dalvik");
                        if ("libdvm.so".equals(value)) {
                            return "Dalvik";
                        } else if ("libart.so".equals(value)) {
                            return "ART";
                        } else {
                            return "libartd.so".equals(value) ? "ART debug build" : value;
                        }
                    } catch (IllegalAccessException var3) {
                        return "IllegalAccessException";
                    } catch (IllegalArgumentException var4) {
                        return "IllegalArgumentException";
                    } catch (InvocationTargetException var5) {
                        return "InvocationTargetException";
                    }
                }
            } catch (NoSuchMethodException var6) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException var7) {
            return "SystemProperties class is not found";
        }
    }

    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;

        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 64);
            Signature[] signatures = pinfo.signatures;

            for (int i = 0; i < signatures.length; ++i) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (NameNotFoundException var8) {
            ;
        } catch (CertificateException var9) {
            ;
        }

        return debuggable;
    }
}

