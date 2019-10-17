package com.yyp.tools;

import android.content.Context;
import android.os.Environment;

import com.yyp.tools.utils.LogUtils;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by YanYan on 2018/12/25.
 * APP 缓存清理
 */
public class AppCacheManager {
    private static final String TAG = "AppCacheManager ";

    /**
     * 获取缓存大小
     *
     * @param mContext
     * @return
     * @throws Exception
     */
    public static long getTotalCacheSize(Context mContext) {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(mContext.getCacheDir());
            LogUtils.i(TAG, "!! getCacheDir：" + mContext.getCacheDir() + " cacheSize：" + cacheSize);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(mContext.getExternalCacheDir());
                LogUtils.i(TAG,"!! getExternalCacheDir：" + mContext.getExternalCacheDir() + " cacheSize：" + getFolderSize(mContext.getExternalCacheDir()));
            }
            LogUtils.i(TAG,"!! getFormatSize(cacheSize)：" + getFormatSize(cacheSize));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheSize;
    }

    //删除内外缓存
    public static void clearAllCache(Context mContext) {
        deleteDir(mContext.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(mContext.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */

    public static String getFormatSize(long size) {
        if (size == 0)
            return "0M";

        double kiloByte = size / 1024;
        if (kiloByte < 1) {//k
//            return size / 1024 + "M";
            return "0M";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {//kb
//            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
//            SystemTools.outPrintln("!! size：" + size + " BigDecimal：" + result1.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB");
//            return result1.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
            return "0M";
        } else {
            String megaStr = megaByte <= 500 ? Double.toString(megaByte) : "500";
            BigDecimal result2 = new BigDecimal(megaStr);
            LogUtils.i(TAG,"!! size：" + size + " BigDecimal：" + result2.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB");
            return result2.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
//        double gigaByte = megaByte / 1024;//mb
//        if (gigaByte < 1) {
//            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
//            SystemTools.outPrintln("!! size：" + size + " BigDecimal：" + result2.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB");
//            return result2.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
//        }
//        double teraBytes = gigaByte / 1024;//gb
//        if (teraBytes < 1) {
//            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
//            SystemTools.outPrintln("!! size：" + size + " BigDecimal：" + result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB");
//            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
//        }
//
//        BigDecimal result4 = new BigDecimal(teraBytes);//tb
//        SystemTools.outPrintln("!! size：" + size + " BigDecimal：" + result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB");
//        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

}
