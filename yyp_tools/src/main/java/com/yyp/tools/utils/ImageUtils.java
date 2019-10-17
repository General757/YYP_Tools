package com.yyp.tools.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.yyp.tools.entity.Album;
import com.yyp.tools.entity.PhotoItem;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片工具类
 */
public class ImageUtils {

    public static int getMiniSize(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return Math.min(options.outHeight, options.outWidth);
    }

    /**
     * 判断是不是方形
     *
     * @param imagePath
     * @return
     */
    public static boolean isSquare(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return options.outHeight == options.outWidth;
    }

    /**
     * 判断是不是4:3比例
     *
     * @param imagePath
     * @return
     */
    public static boolean isPropotion(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return (options.outHeight / 3 * 4 == options.outWidth) || (options.outWidth / 3 * 4 == options.outHeight);
    }

    /**
     * 是否是官方的16:9尺寸.
     *
     * @param imagePath
     * @return
     */
    public static boolean isOfficalSize(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        boolean isOk = (options.outHeight / 16 == options.outWidth / 9);
        if (!isOk) {
            float temp = Float.valueOf(options.outHeight / 16) / Float.valueOf(options.outWidth / 9);
            if (temp > 0.8f || temp < 1.2f) {
                isOk = true;
            }
        }
        return isOk;
    }

    public static Bitmap rotationBitmap(Bitmap bitmap, int degree) {
        if (degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree); /*翻转90度*/
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return bitmap;
    }

    //图片是不是正方形
    public static boolean isSquare(Application app, Uri imageUri) {
        ContentResolver resolver = app.getContentResolver();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(resolver.openInputStream(imageUri), null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return options.outHeight == options.outWidth;
    }

    //保存图片文件
    public static String saveToFile(Application app, String rootPath, String fileFolderStr, boolean isDir, Bitmap croppedImage) throws IOException {
        File jpgFile;
        if (isDir) {
            File fileFolder = new File(fileFolderStr);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            String filename = format.format(date) + ".png";
            if (!fileFolder.exists()) { // 如果目录不存在，则创建目录
                fileFolder.mkdirs();
//                FileUtil.getInst().mkdir(fileFolder);
            }
            jpgFile = new File(fileFolder, filename);
        } else {
            jpgFile = new File(fileFolderStr);
            if (!jpgFile.getParentFile().exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                FileUtil.getInst(app, rootPath).mkdir(jpgFile.getParentFile());
            }
        }
        if (!jpgFile.exists())
            jpgFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流

        croppedImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        IOUtils.closeStream(outputStream);
        return jpgFile.getPath();
    }

    public static String saveToFile(Application app, String rootPath, String fileFolderStr, boolean isDir, Bitmap croppedImage, String fileName) throws IOException {
        File jpgFile;
        if (isDir) {
            File fileFolder = new File(fileFolderStr);
            fileName = fileName + ".png";
            if (!fileFolder.exists()) { // 如果目录不存在，则创建目录
                fileFolder.mkdirs();
//                FileUtil.getInst().mkdir(fileFolder);
            }
            jpgFile = new File(fileFolder, fileName);
        } else {
            jpgFile = new File(fileFolderStr);
            if (!jpgFile.getParentFile().exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                FileUtil.getInst(app, rootPath).mkdir(jpgFile.getParentFile());
            }
        }
        if (!jpgFile.exists())
            jpgFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流

        croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        IOUtils.closeStream(outputStream);
        return jpgFile.getPath();
    }

    public static String saveBitmapGetPathPNG(String fileName, Bitmap bmp, int width, int height) {
        saveBitmapPNG(bmp, fileName, width, height);
        return fileName;
    }

    public static void saveBitmapPNG(Bitmap bm, String filename, int newWidth, int newHeight) {
        LogUtils.i("ImageUtils", "saving Bitmap : " + filename);

        Bitmap bmp = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);

        try {
            FileOutputStream fileout = new FileOutputStream(filename);
            BufferedOutputStream bufferOutStream = new BufferedOutputStream(fileout);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bufferOutStream);
            bufferOutStream.flush();
            bufferOutStream.close();
        } catch (IOException e) {
            LogUtils.e("ImageUtils", "Err when saving bitmap...");
            e.printStackTrace();
            return;
        }

        LogUtils.i("ImageUtils", "Bitmap " + filename + " saved!");
    }

    public static void saveBitmapJPEG(Bitmap bm, String filename, int newWidth, int newHeight) {
        LogUtils.i("ImageUtils", "saving Bitmap : " + filename);

        Bitmap bmp = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);

        try {
            FileOutputStream fileout = new FileOutputStream(filename);
            BufferedOutputStream bufferOutStream = new BufferedOutputStream(fileout);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bufferOutStream);
            bufferOutStream.flush();
            bufferOutStream.close();
        } catch (IOException e) {
            LogUtils.e("ImageUtils", "Err when saving bitmap...");
            e.printStackTrace();
            return;
        }

        LogUtils.i("ImageUtils", "Bitmap " + filename + " saved!");
    }

    //从文件中读取Bitmap
    public static Bitmap decodeBitmapWithOrientation(String pathName, int width, int height) {
        return decodeBitmapWithSize(pathName, width, height, false);
    }

    public static Bitmap decodeBitmapWithOrientationMax(String pathName, int width, int height) {
        return decodeBitmapWithSize(pathName, width, height, true);
    }

    private static Bitmap decodeBitmapWithSize(String pathName, int width, int height,
                                               boolean useBigger) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inInputShareable = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(pathName, options);

        int decodeWidth = width, decodeHeight = height;
        final int degrees = getImageDegrees(pathName);
        if (degrees == 90 || degrees == 270) {
            decodeWidth = height;
            decodeHeight = width;
        }

        if (useBigger) {
            options.inSampleSize = (int) Math.min(((float) options.outWidth / decodeWidth),
                    ((float) options.outHeight / decodeHeight));
        } else {
            options.inSampleSize = (int) Math.max(((float) options.outWidth / decodeWidth),
                    ((float) options.outHeight / decodeHeight));
        }

        options.inJustDecodeBounds = false;
        Bitmap sourceBm = BitmapFactory.decodeFile(pathName, options);
        return imageWithFixedRotation(sourceBm, degrees);
    }

    public static int getImageDegrees(String pathName) {
        int degrees = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(pathName);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degrees;
    }

    public static Bitmap imageWithFixedRotation(Bitmap bm, int degrees) {
        if (bm == null || bm.isRecycled())
            return null;

        if (degrees == 0)
            return bm;

        final Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (result != bm)
            bm.recycle();
        return result;

    }


    public static float getImageRadio(ContentResolver resolver, Uri fileUri) {
        InputStream inputStream = null;
        try {
            inputStream = resolver.openInputStream(fileUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            int initWidth = options.outWidth;
            int initHeight = options.outHeight;
            float rate = initHeight > initWidth ? (float) initHeight / (float) initWidth
                    : (float) initWidth / (float) initHeight;
            return rate;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            IOUtils.closeStream(inputStream);
        }
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public static Map<String, Album> findGalleries(Application app, String rootPath, List<String> paths, long babyId) {
        paths.clear();
        //文件夹照片
        Map<String, Album> galleries = new HashMap<>();
        //应用相册.
        ArrayList<PhotoItem> appPhotos = FileUtil.getInst(app, rootPath).findPicsInDir(
                FileUtil.getInst(app, rootPath).getPhotoTempPath());
        if (!appPhotos.isEmpty()) {
            galleries.put(FileUtil.getInst(app, rootPath).getPhotoTempPath(), new Album("应用相册", FileUtil
                    .getInst(app, rootPath).getPhotoTempPath(), appPhotos));
            paths.add(FileUtil.getInst(app, rootPath).getPhotoTempPath());
        }

        paths.add(FileUtil.getInst(app, rootPath).getSystemPhotoPath());
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED};//拍照时间为新增照片时间
        Cursor cursor = app.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,//指定所要查询的字段
                MediaStore.Images.Media.SIZE + ">?",//查询条件
                new String[]{"100000"}, //查询条件中问号对应的值
                MediaStore.Images.Media.DATE_ADDED + " desc");

        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            String data = cursor.getString(1);
            if (data.lastIndexOf("/") < 1) {
                continue;
            }
            String sub = data.substring(0, data.lastIndexOf("/"));
            if (!galleries.keySet().contains(sub)) {
                String name = sub.substring(sub.lastIndexOf("/") + 1, sub.length());
                if (!paths.contains(sub)) {
                    paths.add(sub);
                }
                galleries.put(sub, new Album(name, sub, new ArrayList<PhotoItem>()));
            }

            galleries.get(sub).getPhotos().add(new PhotoItem(data, (long) (cursor.getInt(2)) * 1000));
        }

        cursor.close();


        //系统相机照片
        ArrayList<PhotoItem> sysPhotos = FileUtil.getInst(app, rootPath).findPicsInDir(
                FileUtil.getInst(app, rootPath).getSystemPhotoPath());
        if (!sysPhotos.isEmpty()) {
            galleries.put(FileUtil.getInst(app, rootPath).getSystemPhotoPath(), new Album("胶卷相册", FileUtil
                    .getInst(app, rootPath).getSystemPhotoPath(), sysPhotos));
        } else {
            galleries.remove(FileUtil.getInst(app, rootPath).getSystemPhotoPath());
            paths.remove(FileUtil.getInst(app, rootPath).getSystemPhotoPath());
        }
        return galleries;
    }

    public static PhotoItem getLastPhoto(Application app, String rootPath) {
        PhotoItem photoItem = null;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED};//拍照时间为新增照片时间
        Cursor cursor = app.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,//指定所要查询的字段
                MediaStore.Images.Media.SIZE + ">?",//查询条件
                new String[]{"100000"}, //查询条件中问号对应的值
                MediaStore.Images.Media.DATE_ADDED + " desc");

        cursor.moveToFirst();
        //文件夹照片
        if (cursor.moveToNext()) {
            String data = cursor.getString(1);
            photoItem = new PhotoItem(data, (long) (cursor.getInt(2)) * 1000);
        }
        cursor.close();
        //系统相机照片
        ArrayList<PhotoItem> sysPhotos = FileUtil.getInst(app, rootPath).findPicsInDir(FileUtil.getInst(app, rootPath).getSystemPhotoPath());
        sysPhotos.addAll(FileUtil.getInst(app, rootPath).findPicsInDir(FileUtil.getInst(app, rootPath).getPhotoTempPath()));
        for (PhotoItem item : sysPhotos) {
            if (photoItem == null)
                photoItem = item;
            if (item.getDate() > photoItem.getDate())
                photoItem = item;
        }
        return photoItem;
    }


    //异步加载图片
    public interface LoadImageCallback {
        void callback(Bitmap result);
    }

    public static void asyncLoadImage(Context context, Uri imageUri, LoadImageCallback callback) {
        new LoadImageUriTask(context, imageUri, callback).execute();
    }

    private static class LoadImageUriTask extends AsyncTask<Void, Void, Bitmap> {
        private final Uri imageUri;
        private final Context context;
        private LoadImageCallback callback;

        public LoadImageUriTask(Context context, Uri imageUri, LoadImageCallback callback) {
            this.imageUri = imageUri;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                InputStream inputStream;
                if (imageUri.getScheme().startsWith("http")
                        || imageUri.getScheme().startsWith("https")) {
                    inputStream = new URL(imageUri.toString()).openStream();
                } else {
                    inputStream = context.getContentResolver().openInputStream(imageUri);
                }
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            callback.callback(result);
        }
    }

    //异步加载缩略图
    public static void asyncLoadSmallImage(Context context, Uri imageUri, LoadImageCallback callback) {
        new LoadSmallPicTask(context, imageUri, callback).execute();
    }

    private static class LoadSmallPicTask extends AsyncTask<Void, Void, Bitmap> {

        private final Uri imageUri;
        private final Context context;
        private LoadImageCallback callback;

        public LoadSmallPicTask(Context context, Uri imageUri, LoadImageCallback callback) {
            this.imageUri = imageUri;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return getResizedBitmap(context, imageUri, 300, 300);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            callback.callback(result);
        }

    }

    //得到指定大小的Bitmap对象
    public static Bitmap getResizedBitmap(Context context, Uri imageUri, int width, int height) {
        InputStream inputStream = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            inputStream = context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(inputStream, null, options);

            options.outWidth = width;
            options.outHeight = height;
            options.inJustDecodeBounds = false;
            IOUtils.closeStream(inputStream);
            inputStream = context.getContentResolver().openInputStream(imageUri);
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inputStream);
        }
        return null;
    }

    public static void saveBitmapPNG(String filename, Bitmap bm) {
        LogUtils.i("ImageUtils", "saving Bitmap : " + filename);

        try {
            FileOutputStream fileout = new FileOutputStream(filename);
            BufferedOutputStream bufferOutStream = new BufferedOutputStream(fileout);
            bm.compress(Bitmap.CompressFormat.PNG, 100, bufferOutStream);
            bufferOutStream.flush();
            bufferOutStream.close();
        } catch (IOException e) {
            LogUtils.e("ImageUtils", "Err when saving bitmap...");
            e.printStackTrace();
            return;
        }

        LogUtils.i("ImageUtils", "Bitmap " + filename + " saved!");
    }

    public static Bitmap setTransparentBitmap(Bitmap sourceImg, int number) {

        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Bitmap.Config.ARGB_8888);

        return sourceImg;
    }

    public static String modifyImageTransparent(Application app, String rootPath, String sourceImg, int number) {
        try {
            String photoDir = FilePathUtil.getInstance().getFileDir(app, FilePathUtil.FileType.TEMP);
            Bitmap sourceBit = setTransparentBitmap(BitmapFactory.decodeFile(sourceImg), number);
            return saveToFile(app, rootPath, photoDir, true, sourceBit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
