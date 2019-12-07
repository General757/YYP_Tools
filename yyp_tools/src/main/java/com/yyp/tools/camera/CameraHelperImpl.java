package com.yyp.tools.camera;

import android.hardware.Camera;

/**
 * Created by generalYan on 2019/10/21.
 */
public interface CameraHelperImpl {
    int getNumberOfCameras();

    Camera openCamera(int var1);

    Camera openDefaultCamera();

    Camera openCameraFacing(int var1);

    boolean hasCamera(int var1);

    void getCameraInfo(int var1, CameraInfo2 var2);
}
