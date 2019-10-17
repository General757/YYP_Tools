package com.yyp.tools.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by YanYan on 2019/10/17.
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

}
