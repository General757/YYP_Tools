package com.yyp.tools.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by generalYan on 2019/10/17.
 * SD工具
 */
public class SdcardUtils {

    /**
     * 获取sdcard剩余内存
     *
     * @return 单位b
     */
    public static long getSdcardAvailableSize() {

        File directory = Environment.getExternalStorageDirectory();

        StatFs statFs = new StatFs(directory.getPath());
        //获取可供程序使用的Block数量
        long blockAvailable = statFs.getAvailableBlocks();
        //获得Sdcard上每个block的size
        long blockSize = statFs.getBlockSize();

        return blockAvailable * blockSize;
    }

    /**
     * 获取sdcard总内存大小
     *
     * @return 单位b
     */
    public static long getSdcardTotalSize() {

        File directory = Environment.getExternalStorageDirectory();

        StatFs statFs = new StatFs(directory.getPath());
        //获得sdcard上 block的总数
        long blockCount = statFs.getBlockCount();
        //获得sdcard上每个block 的大小
        long blockSize = statFs.getBlockSize();

        return blockCount * blockSize;
    }

    /**
     * 检测sdcard剩余有效内存，低于参数时弹出内存不足提示
     *
     * @param tagSize 单位M
     * @return true内存足够false内存小
     */
    public static boolean checkAvailableSize(Context context, int tagSize) {
        boolean isAvailable = true;
        boolean isHasPermission = PermissionUtils.checkPermissionsGroup(context, PermissionUtils.PERMISSION_STORAGE);
        if (isHasPermission) {
            long availableSize = getSdcardAvailableSize() / 1024 / 1024;
            LogUtils.e("SdcardUtils ", "log_common_SdcardUtils_availableSize : " + availableSize);
            isAvailable = availableSize >= tagSize;
        }
        return isAvailable;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }

        return sdDir != null ? sdDir.toString() : "";
    }

    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = (long) stat.getBlockSize();
        long availableBlocks = (long) stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = (long) stat.getBlockSize();
        long totalBlocks = (long) stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public static long getAvailableExternalMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = (long) stat.getBlockSize();
            long availableBlocks = (long) stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1L;
        }
    }

    public static long getTotalExternalMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = (long) stat.getBlockSize();
            long totalBlocks = (long) stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1L;
        }
    }

    public static long getTotalExternal_SDMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            File externalSD = new File(path.getPath() + "/external_sd");
            if (externalSD.exists() && externalSD.isDirectory()) {
                StatFs stat = new StatFs(path.getPath() + "/external_sd");
                long blockSize = (long) stat.getBlockSize();
                long totalBlocks = (long) stat.getBlockCount();
                return getTotalExternalMemorySize() != -1L && getTotalExternalMemorySize() != totalBlocks * blockSize ? totalBlocks * blockSize : -1L;
            } else {
                return -1L;
            }
        } else {
            return -1L;
        }
    }

    public static long getAvailableExternal_SDMemorySize() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            File externalSD = new File(path.getPath() + "/external_sd");
            if (externalSD.exists() && externalSD.isDirectory()) {
                StatFs stat = new StatFs(path.getPath() + "/external_sd");
                long blockSize = (long) stat.getBlockSize();
                long availableBlocks = (long) stat.getAvailableBlocks();
                return getAvailableExternalMemorySize() != -1L && getAvailableExternalMemorySize() != availableBlocks * blockSize ? availableBlocks * blockSize : -1L;
            } else {
                return -1L;
            }
        } else {
            return -1L;
        }
    }
}
