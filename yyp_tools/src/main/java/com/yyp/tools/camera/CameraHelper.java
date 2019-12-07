//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build.VERSION;

/**
 * Created by generalYan on 2019/10/21.
 */
public class CameraHelper {
    private final CameraHelperImpl mImpl;

    public CameraHelper(Context context) {
        if (VERSION.SDK_INT >= 9) {
            this.mImpl = new CameraHelperGB();
        } else {
            this.mImpl = new CameraHelperBase(context);
        }

    }

    public int getNumberOfCameras() {
        return this.mImpl.getNumberOfCameras();
    }

    public Camera openCamera(int id) {
        return this.mImpl.openCamera(id);
    }

    public Camera openDefaultCamera() {
        return this.mImpl.openDefaultCamera();
    }

    public Camera openFrontCamera() {
        return this.mImpl.openCameraFacing(1);
    }

    public Camera openBackCamera() {
        return this.mImpl.openCameraFacing(0);
    }

    public boolean hasFrontCamera() {
        return this.mImpl.hasCamera(1);
    }

    public boolean hasBackCamera() {
        return this.mImpl.hasCamera(0);
    }

    public void getCameraInfo(int cameraId, CameraInfo2 cameraInfo) {
        this.mImpl.getCameraInfo(cameraId, cameraInfo);
    }

    public void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        int result = this.getCameraDisplayOrientation(activity, cameraId);
        camera.setDisplayOrientation(result);
    }

    public int getCameraDisplayOrientation(Activity activity, int cameraId) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
        }

        CameraInfo2 info = new CameraInfo2();
        this.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == 1) {
            result = (info.orientation + degrees) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

}

