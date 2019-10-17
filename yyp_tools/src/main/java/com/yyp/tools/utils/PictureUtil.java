package com.yyp.tools.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.yyp.tools.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by YanYan on 2018/12/29.
 */

public class PictureUtil {
    private static String TAG = "PictureUtil";

    //  获得缓存文件路径
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    //  获得缓存文件
    public static File getCacheFile(Context context, String child) {/// storage/emulated/0/Android/data/com.kejiakeji.buddhas/cache/www.png
        String parent = getDiskCacheDir(context);
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getRootPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return "/mnt/sdcard";
        }
    }

    public static File getSavePathFile(String rootPath, String fileName) {
        return new File(getSaveFilePath(rootPath, "images", fileName));
    }

    public static String getSaveFilePath(String rootPath, String catalog, String fileName) {/// storage/emulated/0/guanchan/image/www.png
        String dir = getRootPath() + rootPath + catalog + File.separator;

        if (!TextUtils.isEmpty(fileName))// fileName为空获取文件夹路径
        {// 创建File对象，用于存储拍照后的图片
            File file = new File(dir, fileName);

            if (file.exists()) {
                file.delete();
            }
            try {
                if (!file.getParentFile().exists()) {// 父目录不存在 创建父目录
                    file.getParentFile().mkdirs();
                }

                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dir = file.getAbsolutePath();
        }

        return dir;
    }

    public static String getExternalFilesDir(Context context, String fileName) {/// storage/emulated/0/Android/data/com.kejiakeji.buddhas/files/Download/gift/
        String fileDir = "";
        if (context != null)
            fileDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName + File.separator;
//        else
//            fileDir = Application.getBaseContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName + File.separator;

        return fileDir;
    }

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {
        try {
            Bitmap bm = getSmallBitmap(filePath);
            FileOutputStream fos = new FileOutputStream(filePath);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int inSampleSize) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = inSampleSize;
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_4444;
        ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, newOpts);
    }

    public static String compressBitmap(Context context, Bitmap bitmap, int size) {
        try {
            Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            newbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if (!newbitmap.isRecycled()) {
                newbitmap.recycle();
            }
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String compressBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = 2;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
        Bitmap newbitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        try {
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            newbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if (!newbitmap.isRecycled()) {
                newbitmap.recycle();
            }
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

//    public static String setWaterMarketBitmap(Context context, String imagePath, String name, String text) {
//        return setWaterMarketBitmap(context, imagePath, name, text, false);
//    }
//
//    public static String setWaterMarketBitmap(Context context, String imagePath, String name, String text, boolean isSave) {
//        try {
//            FileInputStream fis = new FileInputStream(imagePath);
//            Bitmap bitmap = BitmapFactory.decodeStream(fis);
//            fis.close();
//            RubberStampConfig.RubberStampConfigBuilder builder = new RubberStampConfig.RubberStampConfigBuilder().base(bitmap).rubberStamp(getWaterBitmap(1, context, name, text)).rubberStampPosition(RubberStampPosition.BOTTOM_RIGHT);
//            RubberStamp rubberStamp = new RubberStamp(context);
//
//            Bitmap newbitmap = rubberStamp.addStamp(builder.build());
//            if (!bitmap.isRecycled()) {
//                bitmap.recycle();
//            }
//
//            File file = null;
//            if (isSave) {
//                file = getSavePathFile(System.currentTimeMillis() + ".png");
//            } else {
//                file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
//            }
//            FileOutputStream fos = new FileOutputStream(file);
//            newbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//            if (!newbitmap.isRecycled()) {
//                newbitmap.recycle();
//            }
//            return file.getPath();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }

    public static Bitmap getWaterBitmap(int waterType, Context context, String name, String text, int drawableId) {
        return getWaterBitmap(waterType, context, 0, 0, name, text, drawableId);
    }

    public static Bitmap getWaterBitmap(int waterType, Context context, int screenWidth, int screenHeight, String name, String text, int drawableId) {
        Bitmap newBitmap = null;/** 新图片 */
        int width;
        if (context.getResources().getDisplayMetrics().widthPixels > context.getResources().getDisplayMetrics().heightPixels) {
            if (waterType != 1) {
                width = (int) (screenWidth * 0.125f);
            } else {
                width = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.125f);
            }
        } else {
            if (waterType != 1) {
                width = (int) (screenHeight * 0.15f);
            } else {
                width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.125f);
            }
        }

        Paint textPaint = new Paint();/** 画文字的画笔 */
        textPaint.setTextSize(width / 8);// 一行十个字符
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setShadowLayer(1, 0, 0, Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setFilterBitmap(true);

        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), drawableId);
        int oneFontSize = (int) textPaint.measureText("国");
        int logoHeight = (width - 2 * oneFontSize) * logo.getHeight() / logo.getWidth();// 实际logo高度
        int padding = DensityUtils.dip2px(context, 2);// 间距
        if (waterType == 1) {// 截图
            newBitmap = Bitmap.createBitmap(width, logoHeight + oneFontSize * 2 + (width - 2 * oneFontSize) + 3 * padding, Bitmap.Config.ARGB_4444);// width-padding*2是图片宽，高
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(logo, null, new Rect(oneFontSize, 0, width - oneFontSize, logoHeight), null);// 把图片画上来
            textPaint.setColor(Color.WHITE);// 把文字画上来
            // 画文字的时候高度需要注意文字大小以及文字行间距
            canvas.drawText(name, width / 2 - textPaint.measureText(name) / 2, logoHeight + oneFontSize, textPaint);

            canvas.drawText(text, width / 2 - textPaint.measureText(text) / 2, logoHeight + oneFontSize + padding + oneFontSize, textPaint);

            Bitmap code = BitmapFactory.decodeResource(context.getResources(), drawableId);
            canvas.drawBitmap(code, null, new Rect(oneFontSize, logoHeight + oneFontSize + padding + oneFontSize + padding, width - oneFontSize, logoHeight + oneFontSize + padding + oneFontSize + padding + (width - 2 * oneFontSize)), null);// 把图片画上来

        } else {// 录屏
            newBitmap = Bitmap.createBitmap(width, logoHeight + oneFontSize * 2 + 2 * padding, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(logo, null, new Rect(oneFontSize, 0, width - oneFontSize, logoHeight), null);// 把图片画上来
            textPaint.setColor(Color.WHITE);// 把文字画上来
            // 画文字的时候高度需要注意文字大小以及文字行间距
            canvas.drawText(name, width / 2 - textPaint.measureText(name) / 2, logoHeight + oneFontSize, textPaint);
            canvas.drawText(text, width / 2 - textPaint.measureText(text) / 2, logoHeight + oneFontSize + padding + oneFontSize, textPaint);
        }

        return newBitmap;
    }

    public static Bitmap getWaterBitmap(int waterType, Bitmap source, String text) { /** source传递进来的源图片text图片的配文waterType:1图片水印2文字水印3图片右文字水印4图片下文字水印 */

        if (waterType == 1) {
            return source;
        }

        Bitmap newBitmap = null;/** 新图片 */
        Paint bitmapPaint = new Paint();/** 画图片的画笔 */
        int bitmapWidth = source.getWidth();/** 图片的宽度 */
        int bitmapHeight = source.getHeight(); /** 图片的高度 */

        int textColor = Color.BLACK;/** 配文的颜色 */
        float textSize = 64;/** 配文的字体大小 */
        float padding = 20; /** 配文与图片间的距离 */
        float linePadding = 5;/** 配文行与行之间的距离 */
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);/** 画文字的画笔 */
        textPaint.setTextSize(textSize);
        int textWHidth = (int) textPaint.measureText("国");
        int textWidth = (int) textWHidth * text.length();

        if (waterType == 2) {
            // 新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
            newBitmap = Bitmap.createBitmap((int) (padding + textWidth + padding), bitmapHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(newBitmap);
            textPaint.setColor(Color.RED);

            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;// 为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;// 为基线到字体下边框的距离,即上图中的bottom
            int baseLineY = (int) (bitmapHeight / 2 - top / 2 - bottom / 2);// 基线中间点的y轴计算公式
            canvas.drawText(text, padding, baseLineY, textPaint);
        } else if (waterType == 3) {
            // 新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
            newBitmap = Bitmap.createBitmap((int) (bitmapWidth + padding + textWidth + padding), bitmapHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(source, 0, 0, bitmapPaint);// 把图片画上来
            textPaint.setColor(Color.RED);// 把文字画上来

            // 画文字的时候高度需要注意文字大小以及文字行间距
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;// 为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;// 为基线到字体下边框的距离,即上图中的bottom
            int baseLineY = (int) (bitmapHeight / 2 - top / 2 - bottom / 2);// 基线中间点的y轴计算公式
            canvas.drawText(text, bitmapWidth + padding, baseLineY, textPaint);
        } else if (waterType == 4) {
            int height = (int) (bitmapHeight + padding + textWidth + padding);
            if (bitmapWidth > textWidth) {
                newBitmap = Bitmap.createBitmap(bitmapWidth, height, Bitmap.Config.ARGB_8888);// 新创建一个新图片比源图片多出一部分，后续用来与文字叠加用

                Canvas canvas = new Canvas(newBitmap);
                canvas.drawBitmap(source, 0, 0, bitmapPaint);// 把图片画上来

                textPaint.setColor(Color.RED);// 把文字画上来

                // 画文字的时候高度需要注意文字大小以及文字行间距
                canvas.drawText(text, bitmapWidth / 2 - textWidth / 2, bitmapHeight + padding / 4 + textWHidth, textPaint);
            } else {
                newBitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_8888);// 新创建一个新图片比源图片多出一部分，后续用来与文字叠加用

                Canvas canvas = new Canvas(newBitmap);
                canvas.drawBitmap(source, textWidth / 2 - bitmapWidth / 2, 0, bitmapPaint);// 把图片画上来

                textPaint.setColor(Color.RED);// 把文字画上来

                // 画文字的时候高度需要注意文字大小以及文字行间距
                canvas.drawText(text, 0, bitmapHeight + padding + textWHidth, textPaint);
            }
        }

        return newBitmap;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 下面是对外公开的重载的方法
    ///////////////////////////////////////////////////////////////////////////

    public static void notifyScanDcim(Context context, String filePath) {
        scanFile(context, filePath);
    }

    public static void insertVideoToMediaStore(Context context, String filePath, long dateTaken, long duration) {
        insertVideoToMediaStore(context, filePath, dateTaken, 0, 0, duration);
    }

    public static void insertImageToMediaStore(Context context, String filePath, long createTime) {
        insertImageToMediaStore(context, filePath, createTime, 0, 0);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 扫描系统相册核心方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 针对系统文夹只需要扫描,不用插入内容提供者,不然会重复
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void scanFile(Context context, String filePath) {
        if (!checkFile(filePath))
            return;
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(intent);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 非系统相册像MediaContent中插入数据，核心方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 针对非系统文件夹下的文件,使用该方法 插入时初始化公共字段
     *
     * @param filePath 文件
     * @param time     ms
     * @return ContentValues
     */
    private static ContentValues initCommonContentValues(String filePath, long time) {
        ContentValues values = new ContentValues();
        File saveFile = new File(filePath);
        long timeMillis = getTimeWrap(time);
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, timeMillis);
        values.put(MediaStore.MediaColumns.DATE_ADDED, timeMillis);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        return values;
    }

    /**
     * 保存到照片到本地，并插入MediaStore以保证相册可以查看到,这是更优化的方法，防止读取的照片获取不到宽高
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param createTime 创建时间 <=0时为当前时间 ms
     * @param width      宽度
     * @param height     高度
     */
    public static void insertImageToMediaStore(Context context, String filePath, long createTime, int width, int height) {
        if (!checkFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, createTime);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0)
                values.put(MediaStore.Images.ImageColumns.WIDTH, 0);
            if (height > 0)
                values.put(MediaStore.Images.ImageColumns.HEIGHT, 0);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getPhotoMimeType(filePath));
        context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void insertImageToMediaStore(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), path, file.getName(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存到视频到本地，并插入MediaStore以保证相册可以查看到,这是更优化的方法，防止读取的视频获取不到宽高
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param createTime 创建时间 <=0时为当前时间 ms
     * @param duration   视频长度 ms
     * @param width      宽度
     * @param height     高度
     */
    public static void insertVideoToMediaStore(Context context, String filePath, long createTime, int width, int height, long duration) {
        if (!checkFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, createTime);
        if (duration > 0)
            values.put(MediaStore.Video.VideoColumns.DURATION, duration);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0)
                values.put(MediaStore.Video.VideoColumns.WIDTH, width);
            if (height > 0)
                values.put(MediaStore.Video.VideoColumns.HEIGHT, height);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getVideoMimeType(filePath));
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    // 是不是系统相册
    private static boolean isSystemDcim(String path) {
        return path.toLowerCase().contains("dcim") || path.toLowerCase().contains("camera");
    }

    // 获取照片的mine_type
    private static String getPhotoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("jpg") || lowerPath.endsWith("jpeg")) {
            return "image/jpeg";
        } else if (lowerPath.endsWith("png")) {
            return "image/png";
        } else if (lowerPath.endsWith("gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }

    // 获取video的mine_type,暂时只支持mp4,3gp
    private static String getVideoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("mp4") || lowerPath.endsWith("mpeg4")) {
            return "video/mp4";
        } else if (lowerPath.endsWith("3gp")) {
            return "video/3gp";
        }
        return "video/mp4";
    }

    // 获得转化后的时间
    private static long getTimeWrap(long time) {
        if (time <= 0) {
            return System.currentTimeMillis();
        }
        return time;
    }

    // 检测文件存在
    private static boolean checkFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            return true;
        }
        return false;
    }

    public static long getVideoDuration(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        try {
            return Long.parseLong(duration);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVideoThumbnail(Context context, String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = 2;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
        Bitmap newbitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        try {
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            newbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if (!newbitmap.isRecycled()) {
                newbitmap.recycle();
            }
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取指定Activity的截屏，保存到png文件
     *
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        savePic(b, "/sdcard/screen_test.png");
        return b;
    }

    /**
     * 保存到sdcard
     *
     * @param b
     * @param strFileName
     */
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把View对象转换成bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public static Bitmap getBitmapByView(View vBottomCode) {

        Bitmap bottom = Bitmap.createBitmap(vBottomCode.getWidth(), vBottomCode.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bottom);
        vBottomCode.draw(canvas2);

        return bottom;
    }

    /**
     * 截取scrollview的屏幕
     */
    public static Bitmap getBitmapByView(ScrollView scrollView, View vBottomCode) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            // scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        Log.d(TAG, "实际高度:" + h);
        Log.d(TAG, " 高度:" + scrollView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        Bitmap bottom = Bitmap.createBitmap(vBottomCode.getWidth(), vBottomCode.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bottom);
        vBottomCode.draw(canvas2);

        int disparity = DensityUtils.dip2px(scrollView.getContext(), 5);
        return splitVertical(bitmap, bottom, disparity);
    }

    public static Bitmap splitVertical(Bitmap first, Bitmap second, int disparity) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight() - disparity;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight() - disparity, null);
        return result;
    }

    public static Bitmap viewToBitmap(View view, int viewWidth, int viewHeight) {
        view.layout(0, 0, viewWidth, viewHeight);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 保存图片并插入图库
     *
     * @param context
     * @param bmp
     */
    public static String saveImageToGallery(Context context, String rootPath, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), rootPath + File.separator + "Images/");

        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        String path = file.getAbsolutePath();
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

        return path;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static File rotaingImageView(Context context, int angle, File oldfile) {
        //旋转图片 动作
        BitmapFactory.Options opts = new BitmapFactory.Options();//获取缩略图显示到屏幕上
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(oldfile.getAbsolutePath(), opts);

        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        try {
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (!resizedBitmap.isRecycled()) {
                resizedBitmap.recycle();
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 图片file转换uri
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ", new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    // 图片uri转换String
    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}

