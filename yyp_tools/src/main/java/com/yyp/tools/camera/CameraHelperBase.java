//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.camera;

import android.content.Context;
import android.hardware.Camera;

/**
 * Created by generalYan on 2019/10/21.
 */
public class CameraHelperBase implements CameraHelperImpl {
    private final Context mContext;

    public CameraHelperBase(Context context) {
        this.mContext = context;
    }

    public int getNumberOfCameras() {
        return this.hasCameraSupport() ? 1 : 0;
    }

    public Camera openCamera(int id) {
        return Camera.open();
    }

    public Camera openDefaultCamera() {
        return Camera.open();
    }

    public boolean hasCamera(int facing) {
        return facing == 0 && this.hasCameraSupport();
    }

    public Camera openCameraFacing(int facing) {
        return facing == 0 ? Camera.open() : null;
    }

    public void getCameraInfo(int cameraId, CameraInfo2 cameraInfo) {
        cameraInfo.facing = 0;
        cameraInfo.orientation = 90;
    }

    private boolean hasCameraSupport() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.camera");
    }
}

