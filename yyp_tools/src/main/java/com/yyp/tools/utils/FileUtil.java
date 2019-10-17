package com.yyp.tools.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.yyp.tools.entity.PhotoItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * app工具类
 * Created by lmk on 2016/4/6.
 */
public class FileUtil {
    private static final String TAG = "FileUtil ";

    private static String BASE_PATH;//app根路径/storage/sdcard0/Blublu
    private static String STICKER_BASE_PATH;//app滤镜处理图片存放/storage/sdcard0/Blublu/camera/filter
    private static String CACHE_PATH;//外部私有缓存路径/storage/sdcard0/Android/data/com.linyun.blublu/cache

    private static FileUtil mInstance;

    private FileUtil(Application app, String rootPath) {
        String sdcardState = Environment.getExternalStorageState();
        //如果没SD卡则放缓存
        if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
            BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + rootPath + File.separator;//
            CACHE_PATH = app.getExternalCacheDir().getAbsolutePath() + File.separator;
        } else {
            BASE_PATH = app.getCacheDir().getAbsolutePath();
            CACHE_PATH = app.getCacheDir().getAbsolutePath();
        }

        STICKER_BASE_PATH = BASE_PATH + "camera/filter/";
    }

    public static FileUtil getInst(Application app, String rootPath) {
        if (mInstance == null) {
            synchronized (FileUtil.class) {
                if (mInstance == null) {
                    mInstance = new FileUtil(app, rootPath);
                }
            }
        }
        return mInstance;
    }

    public static Uri getFileUri(Context context, String applicationId, String filePath) {
        Uri uri;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion < 24) {
            uri = Uri.fromFile(new File(filePath));
        } else {
//            ContentValues contentValues = new ContentValues(1);
//            contentValues.put(MediaStore.Images.Media.DATA, filePath);
//            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            uri = FileProvider.getUriForFile(context, applicationId + ".provider", new File(filePath));
        }
        return uri;
    }

    /**
     * Check if the primary "external" storage device is available.
     * 判断sd卡是否可用
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 存放在这里便于用户删除app后，自动将数据删除
     * 写入外部存储的私有内容
     *
     * @param context  上下文對象
     * @param fileName 文件名称
     * @param content  文件内容
     * @return 是否写入成功
     */
    public static boolean writeExternalstoPrivate(Context context, String fileName, String content) {
        boolean bl = false;
        //创建外部存储的私有内容的文件对象
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            bl = true;
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bl;
    }

    /**
     * 读取外部存储私有空间的内容
     *
     * @param context  上线文对象
     * @param fileName 文件名称
     * @return
     */
    public static String readExternalStorPrivate(Context context, String fileName) {
        String data = null;
        StringBuilder sb = new StringBuilder();
        File file = new File(context.getExternalFilesDir(null), fileName);
        BufferedReader br = null;
        try {
            FileInputStream in = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 写入文件到sdCard的本app根目录下
     *
     * @param fileName
     * @param content
     * @return
     */
    public static boolean writeExternalStorSdCard(String content, String rootPath, String fileName) {
        boolean bl = false;
        //创建sdCard根目录文件下的文件对象
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + rootPath, fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            bl = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bl;
    }

    /**
     * 在SD卡上创建app根目录文件
     *
     * @throws IOException
     */
    public static boolean createSDAppFile(String rootPath) {
        if (isSDCardMounted()) {
            File rootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + rootPath);
            if (!rootDir.exists()) {
                rootDir.mkdir();
            }
            return true;
        }
        return false;
    }

    /*-------------------新加入的------------------------*/

    public File getExtFile(String path) {
        return new File(BASE_PATH + path);
    }


    public String getBasePath(int packageId) {
        return BASE_PATH + packageId + "/";
    }

    private String getImageFilePath(String desKey, int packageId, String imageUrl) {
        DESUtil desUtil = new DESUtil(desKey);
        try {
            imageUrl = desUtil.encrypt(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String md5Str = imageUrl.replace("-", "mm");
        return getBasePath(packageId) + md5Str;
    }


    public void removeAddonFolder(int packageId) {
        String filename = getBasePath(packageId);
        File file = new File(filename);
        if (file.exists()) {
            delete(file);
        }
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

    public static void delete(String path) {
        if (!StringUtils.isEmpty(path)) {
            delete(new File(path));
        }
    }

    public static void deleteFiles(String filePath) {
        deleteFile(new File(filePath), false);
    }

    //flie：要删除的文件夹的所在位置
    public static void deleteFile(File file, boolean isAll) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0)
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteFile(f, false);
                }
            if (isAll)
                file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    //flie：要删除数组的文件夹的所在位置
    public static void deleteListFile(List<String> fileList) {
        //删除文件,清空缓存.
        for (String filePath : fileList) {
            File destroy = new File(filePath);
            if (destroy.exists())
                destroy.delete();
        }
    }

    public String getPhotoSavedPath() {
        return BASE_PATH + "camera";
    }

    public String getPhotoTempPath() {
        return CACHE_PATH + "camera";
    }

    public String getVideoSavedPath() {
        return BASE_PATH + "video";
    }

    public String getVideoTempPath() {
        return BASE_PATH + "video";
    }

    public String getSystemPhotoPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    }

    public boolean createFile(File file) {
        try {
            if (!file.getParentFile().exists()) {
                mkdir(file.getParentFile());
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }

    //都是相对路径，一一对应
    public boolean copyAssetFileToFiles(Context context, String filename) {
        return copyAssetFileToFiles(context, filename, getExtFile("/" + filename));
    }

    private boolean copyAssetFileToFiles(Context context, String filename, File of) {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = context.getAssets().open(filename);
            createFile(of);
            os = new FileOutputStream(of);

            int readedBytes;
            byte[] buf = new byte[1024];
            while ((readedBytes = is.read(buf)) > 0) {
                os.write(buf, 0, readedBytes);
            }
            os.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeStream(is);
            IOUtils.closeStream(os);
        }
    }

    public boolean renameDir(String oldDir, String newDir) {
        File of = new File(oldDir);
        File nf = new File(newDir);
        return of.exists() && !nf.exists() && of.renameTo(nf);
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                File newFile = new File(newPath);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    LogUtils.e(TAG, bytesum + "");
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "复制单个文件操作出错 Exception " + e.toString());
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inStream);
            IOUtils.closeStream(fs);
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     * <code>false</code> otherwise
     */
    public static boolean copySingleFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                LogUtils.e(TAG, "!!!   --Method--  copySingleFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                LogUtils.e(TAG, "!!!   --Method-- copySingleFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                LogUtils.e(TAG, "!!!   --Method--  copySingleFile:  oldFile cannot read.");
                return false;
            }
            File newFile = new File(newPath$Name.substring(0, newPath$Name.lastIndexOf("/")));
            if (!newFile.exists())  //如果文件夹不存在 则建立新文件夹
                newFile.mkdirs();

            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "!!!   --Method--  copySingleFile:  Exception：" + e.toString());
            return false;
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            File fileFolder = new File(newPath);
            if (!fileFolder.exists())  //如果文件夹不存在 则建立新文件夹
                fileFolder.mkdirs();

            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    input = new FileInputStream(temp);
                    output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "复制整个文件夹内容操作出错");
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(input);
            IOUtils.closeStream(output);
        }
    }

    /**
     * 复制文件夹及其中的文件
     *
     * @param oldPath String 原文件夹路径 如：data/user/0/com.test/files
     * @param newPath String 复制后的路径 如：data/user/0/com.test/cache
     * @return <code>true</code> if and only if the directory and files were copied;
     * <code>false</code> otherwise
     */
    public static boolean copyFolderFile(String oldPath, String newPath) {
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                if (!newFile.mkdirs()) {
                    LogUtils.e(TAG, "!!!   --Method--  copyFolderFile:   cannot create directory.");
                    return false;
                }
            }
            File oldFile = new File(oldPath);
            String[] files = oldFile.list();
            File temp;
            for (String file : files) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file);
                } else {
                    temp = new File(oldPath + File.separator + file);
                }

                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (!temp.exists()) {
                    LogUtils.e(TAG, "!!!   --Method--  copyFolderFile:    oldFile not exist.");
                    return false;
                } else if (!temp.isFile()) {
                    LogUtils.e(TAG, "!!!   --Method--  copyFolderFile:    oldFile not file.");
                    return false;
                } else if (!temp.canRead()) {
                    LogUtils.e(TAG, "!!!   --Method--  copyFolderFile:    oldFile cannot read.");
                    return false;
                } else {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }

                /* 如果不需要打log，可以使用下面的语句
                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (temp.exists() && temp.isFile() && temp.canRead()) {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                 */
            }
            return true;
        } catch (Exception e) {
            LogUtils.e(TAG, "!!!   --Method--  copyFolderFile:  Exception：" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    //获取path路径下的图片
    public ArrayList<PhotoItem> findPicsInDir(String path) {
        ArrayList<PhotoItem> photos = new ArrayList<>();
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    String filePath = pathname.getAbsolutePath();
                    return (filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath
                            .endsWith(".jepg"));
                }
            })) {
                photos.add(new PhotoItem(file.getAbsolutePath(), file.lastModified()));
            }
        }
        Collections.sort(photos);
        return photos;
    }

    public interface OnUnzipListener {
        void onUnzipError();

        void onUnzipComplete();
    }

    /**
     * <p>
     * 解压压缩包
     * </p>
     *
     * @param zipFilePath 压缩文件路径
     * @param destDir 压缩包释放目录
     * @throws Exception
     */
    private static Object object = new Object();
    private static int length;

    public static void unZip(String zipFilePath, final String destDir, final OnUnzipListener unzipListener) {
        File[] files = new File(zipFilePath).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name != null && name.endsWith(".zip")) {
                    return true;
                }
                return false;
            }
        });

        length = files.length;
        if (length == 0) {
            if (unzipListener != null)
                unzipListener.onUnzipError();
            return;
        }

        for (final File file : files) {
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        int len = file.getAbsolutePath().length();
                        //判断解压后的文件是否存在,截取.zip之前的字符串
                        if (!new File(file.getAbsolutePath().substring(0, len - 4)).exists()) {
                            unZipFolder(file.getAbsolutePath(), destDir, unzipListener);
                        }
                        synchronized (object) {
                            length--;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    synchronized (object) {
                        if (length == 0) {
                            if (unzipListener != null)
                                unzipListener.onUnzipComplete();
                        }
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public static void unZipFolder(String zipFileString, String outPathString, OnUnzipListener unzipListener) {
        ZipInputStream inZip = null;
        FileOutputStream out = null;
        try {
            inZip = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // get the folder name of the widget
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(outPathString + File.separator + szName);
                    folder.mkdirs();
                } else {
                    File file = new File(outPathString + File.separator + szName);
                    file.createNewFile();
                    out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = inZip.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            inZip.close();
            if (unzipListener != null)
                unzipListener.onUnzipComplete();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (inZip != null)
                    inZip.close();
                if (out != null)
                    out.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (unzipListener != null)
                unzipListener.onUnzipError();
        }
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     * */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param filePath
     * @return
     */

    public static String getFileMD5(String filePath) {

        if (TextUtils.isEmpty(filePath))
            return null;

        File file = new File(filePath);

        if (!file.isFile() || !file.exists())
            return null;

        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        BigInteger bigInt = new BigInteger(1, digest.digest());
//        return bigInt.toString(16);
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param filePath
     * @param listChild ;true递归子目录中的文件
     * @return
     */
    public static Map<String, String> getDirMD5(String filePath, boolean listChild) {

        if (TextUtils.isEmpty(filePath))
            return null;

        File file = new File(filePath);

        if (!file.isFile() || !file.exists() || !file.isDirectory())
            return null;

        Map<String, String> map = new HashMap<>();
        String md5;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f.getAbsolutePath(), listChild));
            } else {
                md5 = getFileMD5(f.getAbsolutePath());
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

    /**
     * 获取文件夹大小
     *
     * @param filePath 文件地址
     * @return long
     */
    public static long getFolderSize(String filePath) {
        long size = 0;
        if (TextUtils.isEmpty(filePath))
            return size;

        try {
            File file = new File(filePath);
            if (!file.exists())
                return size;

            File[] fileList = file.listFiles();
            if (fileList == null || fileList.length <= 0)
                return size;

            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory())
                    size = size + getFolderSize(fileList[i].getAbsolutePath());
                else
                    size = size + fileList[i].length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    //水印图片保存到本地
    public static String getWaterMarkPath(Context mContext, Bitmap logoBitmap) {
        String blendDir = FilePathUtil.getInstance().getFileDir(mContext, FilePathUtil.FileType.SETTINGS);
        String logoWater = "WaterMark.png";  //水印缓存文件名
        File waterMark = new File(blendDir, logoWater);
        if (!waterMark.exists()) {
            //此处做了水印图基于当前手机屏幕的文件缓存.
            File dir = new File(blendDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            ImageUtils.saveBitmapPNG(blendDir + logoWater, logoBitmap);
            waterMark = new File(blendDir, logoWater);
        }
        return waterMark.getPath();
    }
}
