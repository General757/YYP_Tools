package com.yyp.tools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Blu文件路径工具类
 * Created by Jesse_Chin on 16/5/31.
 */
public class FilePathUtils {
    private String TAG = "FilePathUtils ";

    private static FilePathUtils instance;

    public static FilePathUtils getInstance() {
        if (instance == null) {
            synchronized (FilePathUtils.class) {
                if (instance == null) {
                    instance = new FilePathUtils();
                }
            }
        }
        return instance;
    }

    public enum FileType {
        BASE,              //根目录文件
        TEMP,              //临时文件
        PHOTO_TO,          //拍摄的照片
        PHOTO_FROM,        //拍摄的照片
        SPEECH,            //语音
        VIDEO_TO,          //视频
        VIDEO_FROM,        //视频
        CHAT_COVER,        //视频
        SETTINGS,          //设置
        ASSETBUNDLE        //Unity资源
    }

    private static String ROOT_DIR = "BluShow" + File.separator;
    public static String DIR_TEMP = ROOT_DIR + "Temp" + File.separator;
    public static String DIR_SPEECH = ROOT_DIR + "Speech" + File.separator;
    public static String DIR_VIDEO_TO = ROOT_DIR + "Video_to" + File.separator;
    public static String DIR_VIDEO_FROM = ROOT_DIR + "Video_from" + File.separator;
    public static String DIR_SETTINGS = ROOT_DIR + "Settings" + File.separator;
    public static String DIR_PHOTO_TO = ROOT_DIR + "Photo_to" + File.separator;
    public static String DIR_PHOTO_FROM = ROOT_DIR + "Photo_from" + File.separator;
    public static String DIR_CHAT_COVER = ROOT_DIR + "Chat_Cover" + File.separator;
    public static String DIR_ASSETBUNDLE = ROOT_DIR + "AssetBundle" + File.separator;

    public String getFileDir(Context app, FileType type) {
        String dir = null;
        switch (type) {
            case BASE:
                dir = getFilesPath(app, "");
                break;
            case TEMP:
                dir = getFilesPath(app, DIR_TEMP);
                break;
            case SPEECH:
                dir = getFilesPath(app, DIR_SPEECH);
                break;
            case VIDEO_TO:
                dir = getFilesPath(app, DIR_VIDEO_TO);
                break;
            case VIDEO_FROM:
                dir = getFilesPath(app, DIR_VIDEO_FROM);
                break;
            case SETTINGS:
                dir = getFilesPath(app, DIR_SETTINGS);
                break;
            case PHOTO_TO:
                dir = getFilesPath(app, DIR_PHOTO_TO);
                break;
            case PHOTO_FROM:
                dir = getFilesPath(app, DIR_PHOTO_FROM);
                break;
            case CHAT_COVER:
                dir = getFilesPath(app, DIR_CHAT_COVER);
                break;
            case ASSETBUNDLE:
                dir = getFilesPath(app, DIR_ASSETBUNDLE);
                break;
        }
        return dir;
    }

    /**
     * 获取文件位置
     *
     * @param folderPath
     * @return
     */
    private String getFilesPath(Context app, String folderPath) {
        File file = app.getExternalFilesDir(folderPath);

        if (file != null) {
            if ((!file.exists() || !file.isDirectory()))
                file.mkdirs();
        } else
            return "";

        StringBuilder absoluteFolderPath = new StringBuilder(file.getAbsolutePath());
        if (!absoluteFolderPath.toString().endsWith("/"))
            absoluteFolderPath.append("/");

        return absoluteFolderPath.toString();
    }

    /**
     * 清理指定文件夹中几天前的文件
     *
     * @param day
     */
    public void clearFilesBeforeDay(Context app, FileType fileType, int day) {
        String fileName = getFileDir(app, fileType);
        if (StringUtils.isEmpty(fileName)) return;
        File dir = new File(fileName);

        Date now = new Date(); //now

        if (!dir.isDirectory()) {
            return;
        }

        File[] childFiles = dir.listFiles();
        if (childFiles == null || childFiles.length == 0) {
            return;
        }

        for (File file : childFiles) {
            Date date = new Date(file.lastModified());

            int timeDiff = TimeUtils.differentDays(now, date);
            if (timeDiff > day) {
                file.delete();
            }
        }
    }

    /**
     * 清理指定前缀的所有文件
     */
    public void clearFilesByPrefix(Context app, FileType fileType, String prefix) {

        File dir = new File(getFileDir(app, fileType));

        if (!dir.isDirectory()) {
            return;
        }

        File[] childFiles = dir.listFiles();
        if (childFiles == null || childFiles.length == 0) {
            return;
        }

        for (File file : childFiles) {

            if (file.getName().startsWith(prefix))
                file.delete();
        }
    }

    /**
     * 获取文件的Uri, 适配7.0版本及联系7.0
     */
    public Uri getFileUri(Context context, String applicationId, File file) {
        Uri uriSource = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || (Build.FINGERPRINT.toLowerCase().contains("lenovo")
                || Build.BRAND.toLowerCase().contains("lenovo"))) {//联想
            LogUtils.e(TAG, "!!! 联想.");
            uriSource = Uri.fromFile(file);
        } else {
            uriSource = FileProvider.getUriForFile(context, applicationId + ".provider", file);
        }
        return uriSource;
    }

    public static String saveBitmapToSD(Context context, Bitmap bt) {

        String fileDir = FilePathUtils.getInstance().getFileDir(context, FilePathUtils.FileType.TEMP);
        String filePth = fileDir + "logo.jpeg";

        // 已经存在 直接返回路径
        File logFile = new File(filePth);

        if (logFile.exists()) {
            return filePth;
        }

        try {
            FileOutputStream fileout = new FileOutputStream(filePth);
            BufferedOutputStream bufferOutStream = new BufferedOutputStream(fileout);
            bt.compress(Bitmap.CompressFormat.JPEG, 100, bufferOutStream);
            bufferOutStream.flush();
            bufferOutStream.close();
        } catch (IOException e) {
            LogUtils.e("ImageUtils", "Err when saving bitmap...");
            e.printStackTrace();
        }

        return filePth;
    }

    /**
     * 保存文件
     *
     * @return
     */
    public void saveFile(FileType type, File file) {

    }

    /**
     * 通过url获取语音文件
     *
     * @param url 该url可能是网络地址,所以要截取字符串
     * @return
     */
    public File getFile(Context app, FileType type, String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return new File(getFileDir(app, type) + File.separator + fileName);
    }

    public static void delete(File file) {
        if (file.isFile() && file.exists()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (File childFile : childFiles) {
                delete(childFile);
            }
            file.delete();
        }
    }
}
