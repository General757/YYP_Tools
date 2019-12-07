//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by generalYan on 2019/10/21.
 */
public class VideoUtils {
    public VideoUtils() {
    }

    public static File getVideoThumbnail(String filePath, int frame, String thumbDir) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime((long) frame);
        } catch (IllegalArgumentException var20) {
            var20.printStackTrace();
        } catch (RuntimeException var21) {
            var21.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException var18) {
                var18.printStackTrace();
            }

        }

        File videoFile = new File(filePath);
        String videoName = videoFile.getName();
        String thumbName = videoName.substring(0, videoName.lastIndexOf(".")) + ".jpg";
        File thumbFile = new File(thumbDir + File.separator + thumbName);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(thumbFile));
            bitmap.compress(CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException var19) {
            var19.printStackTrace();
        }

        return thumbFile;
    }

    public static int getVideoDuration(String filePath) {
        int duration = 0;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(filePath);
            duration = Integer.parseInt(retriever.extractMetadata(9));
            duration = duration % 1000 == 0 ? duration / 1000 : duration / 1000 + 1;
        } catch (RuntimeException var12) {
            var12.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException var11) {
                var11.printStackTrace();
            }

        }

        return duration;
    }
}

