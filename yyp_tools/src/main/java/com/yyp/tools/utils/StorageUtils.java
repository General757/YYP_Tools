//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Yan on 2019/10/11.
 */

public final class StorageUtils {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String INDIVIDUAL_DIR_NAME = "uil-images";

    private StorageUtils() {
    }

    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File var2 = null;
        if (preferExternal && "mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            var2 = getExternalCacheDir(context);
        }

        if (var2 == null) {
            var2 = context.getCacheDir();
        }

        if (var2 == null) {
            String var3 = "/data/data/" + context.getPackageName() + "/cache/";
            L.w("Can't define system cache directory! '%s' will be used.", new Object[]{var3});
            var2 = new File(var3);
        }

        return var2;
    }

    public static File getFilesDirectory(Context context) {
        return getFilesDirectory(context, true);
    }

    public static File getFilesDirectory(Context context, boolean preferExternal) {
        File var2 = null;
        if (preferExternal && "mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            var2 = getExternalFilesDir(context);
        }

        if (var2 == null) {
            var2 = context.getFilesDir();
        }

        if (var2 == null) {
            String var3 = "/data/data/" + context.getPackageName() + "/files/";
            L.w("Can't define system cache directory! '%s' will be used.", new Object[]{var3});
            var2 = new File(var3);
        }

        return var2;
    }

    public static File getIndividualCacheDirectory(Context context) {
        File var1 = getCacheDirectory(context);
        File var2 = new File(var1, "uil-images");
        if (!var2.exists() && !var2.mkdir()) {
            var2 = var1;
        }

        return var2;
    }

    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File var2 = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            var2 = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }

        if (var2 == null || !var2.exists() && !var2.mkdirs()) {
            var2 = context.getCacheDir();
        }

        return var2;
    }

    private static File getExternalCacheDir(Context context) {
        File var1 = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File var2 = new File(new File(var1, context.getPackageName()), "cache");
        if (!var2.exists()) {
            if (!var2.mkdirs()) {
                L.w("Unable to create external cache directory", new Object[0]);
                return null;
            }

            try {
                (new File(var2, ".nomedia")).createNewFile();
            } catch (IOException var4) {
                L.i("Can't create \".nomedia\" file in application external cache directory", new Object[0]);
            }
        }

        return var2;
    }

    private static File getExternalFilesDir(Context context) {
        File var1 = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File var2 = new File(new File(var1, context.getPackageName()), "files");
        if (!var2.exists()) {
            if (!var2.mkdirs()) {
                L.w("Unable to create external cache directory", new Object[0]);
                return null;
            }

            try {
                (new File(var2, ".nomedia")).createNewFile();
            } catch (IOException var4) {
                L.i("Can't create \".nomedia\" file in application external cache directory", new Object[0]);
            }
        }

        return var2;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int var1 = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return var1 == 0;
    }
}

