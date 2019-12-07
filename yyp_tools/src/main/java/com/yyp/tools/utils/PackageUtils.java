//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by generalYan on 2019/10/21.
 */
public class PackageUtils {
    public static final String TAG = "PackageUtils";
    public static final int APP_INSTALL_AUTO = 0;
    public static final int APP_INSTALL_INTERNAL = 1;
    public static final int APP_INSTALL_EXTERNAL = 2;
    public static final int INSTALL_SUCCEEDED = 1;
    public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
    public static final int INSTALL_FAILED_INVALID_APK = -2;
    public static final int INSTALL_FAILED_INVALID_URI = -3;
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;
    public static final int INSTALL_FAILED_NO_SHARED_USER = -6;
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;
    public static final int INSTALL_FAILED_DEXOPT = -11;
    public static final int INSTALL_FAILED_OLDER_SDK = -12;
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;
    public static final int INSTALL_FAILED_NEWER_SDK = -14;
    public static final int INSTALL_FAILED_TEST_ONLY = -15;
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;
    public static final int INSTALL_FAILED_MISSING_FEATURE = -17;
    public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;
    public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;
    public static final int INSTALL_FAILED_UID_CHANGED = -24;
    public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;
    public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
    public static final int INSTALL_FAILED_OTHER = -1000000;
    public static final int DELETE_SUCCEEDED = 1;
    public static final int DELETE_FAILED_INTERNAL_ERROR = -1;
    public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER = -2;
    public static final int DELETE_FAILED_INVALID_PACKAGE = -3;
    public static final int DELETE_FAILED_PERMISSION_DENIED = -4;

    private PackageUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static File downLoadFile(Context context, String httpUrl) {
        String fileName = "updata.apk";
        File tmpFile = new File("/sdcard/update");
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }

        File file = new File("/sdcard/update/updata.apk");

        try {
            URL url = new URL(httpUrl);

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[256];
                conn.connect();
                double count = 0.0D;
                if (conn.getResponseCode() >= 400) {
                    Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
                } else {
                    while (count <= 100.0D && is != null) {
                        int numRead = is.read(buf);
                        if (numRead <= 0) {
                            break;
                        }

                        fos.write(buf, 0, numRead);
                    }
                }

                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            }
        } catch (MalformedURLException var14) {
            var14.printStackTrace();
        }

        return file;
    }

    public static void installApp(Context context, File apkFile) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void openApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent("android.intent.action.VIEW");
        File file = new File(filePath);
        if (file != null && file.exists() && file.isFile() && file.length() > 0L) {
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(268435456);
            context.startActivity(i);
            return true;
        } else {
            return false;
        }
    }

    public static boolean uninstallNormal(Context context, String packageName) {
        if (packageName != null && packageName.length() != 0) {
            Intent i = new Intent("android.intent.action.DELETE", Uri.parse((new StringBuilder(32)).append("package:").append(packageName).toString()));
            i.addFlags(268435456);
            context.startActivity(i);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSystemApplication(Context context) {
        return context == null ? false : isSystemApplication(context, context.getPackageName());
    }

    public static boolean isSystemApplication(Context context, String packageName) {
        return context == null ? false : isSystemApplication(context.getPackageManager(), packageName);
    }

    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager != null && packageName != null && packageName.length() != 0) {
            try {
                ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
                return app != null && (app.flags & 1) > 0;
            } catch (NameNotFoundException var3) {
                var3.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static Boolean isTopActivity(Context context, String packageName) {
        if (context != null && !TextUtils.isEmpty(packageName)) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
            if (tasksInfo != null && tasksInfo.size() >= 1) {
                try {
                    return packageName.equals(((RunningTaskInfo) tasksInfo.get(0)).topActivity.getPackageName());
                } catch (Exception var5) {
                    var5.printStackTrace();
                    return false;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                try {
                    PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (NameNotFoundException var4) {
                    var4.printStackTrace();
                }
            }
        }

        return -1;
    }

    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            try {
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    return pi.versionName;
                }
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
            }
        }

        return "";
    }

    public static int getInstallLocation() {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location", false, true);
        if (commandResult.result == 0 && commandResult.successMsg != null && commandResult.successMsg.length() > 0) {
            try {
                int location = Integer.parseInt(commandResult.successMsg.substring(0, 1));
                switch (location) {
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                }
            } catch (NumberFormatException var2) {
                var2.printStackTrace();
                Log.e("PackageUtils", "pm get-install-location error");
            }
        }

        return 0;
    }

    @SuppressLint({"InlinedApi"})
    public static void startInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = VERSION.SDK_INT;
        if (VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", packageName, (String) null));
        } else {
            intent.setAction("android.intent.action.VIEW");
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra(sdkVersion == 8 ? "pkg" : "com.android.settings.ApplicationPkgName", packageName);
        }

        intent.addFlags(268435456);
        context.startActivity(intent);
    }
}

