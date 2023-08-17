package com.best.now.autoclick.utils;

import static android.hardware.camera2.CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import io.reactivex.Single;

public class CameraAndFlashProvider {
    private static final String TAG = "CameraAndFlashProvider";
    private Camera mCamera;
    private Camera.Parameters parameters;
    private CameraManager camManager;
    private  Context context;

    private int level = 2;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    //创建 Single 的对象
    private static CameraAndFlashProvider instance;
    private OnChanged onChanged;


    //获取唯一可用的对象
    public static synchronized CameraAndFlashProvider getInstance(Context context){
        if (instance==null){
            instance = new  CameraAndFlashProvider(context);
        }
        return instance;
    }
    public void setonTorchModeChanged(OnChanged onChanged){
        this.onChanged = onChanged;
    }
    private CameraAndFlashProvider(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startBackgroundThread();
            camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            CameraManager.TorchCallback mTorchCallback =
                    new CameraManager.TorchCallback() {
                        @Override
                        public void onTorchModeUnavailable(String cameraId) {

                        }

                        @Override
                        public void onTorchModeChanged(String cameraId, boolean enabled) {
                            super.onTorchModeChanged(cameraId, enabled);
                            Log.e("onTorchModeChanged",enabled+":"+cameraId);
                            if (onChanged!=null){
                                onChanged.change(enabled);
                            }
                        }
                    };
            camManager.registerTorchCallback(mTorchCallback, mBackgroundHandler);
        }else{
            mCamera = Camera.open();
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
        }
    }

    public void turnFlashlightOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String cameraId = null;
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                        camManager.turnOnTorchWithStrengthLevel(cameraId,level);
                        camManager.setTorchMode(cameraId, true);
                    }else{
                        camManager.setTorchMode(cameraId, true);
                    }
                }
            } catch (CameraAccessException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            mCamera.startPreview();
        }
    }

    public void changeStrengthLevel(int level){
        if (camManager != null) {
            String  cameraId = null;
            this.level = level;
            try {
                cameraId = camManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                CameraCharacteristics cameraCharacteristics = camManager.getCameraCharacteristics(cameraId);
               int max = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL);
               if (max>1){
                   camManager.turnOnTorchWithStrengthLevel(cameraId,level);
               }
            }
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void turnFlashlightOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String cameraId;
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                    camManager.setTorchMode(cameraId, false);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            mCamera.stopPreview();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    public void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public interface OnChanged{
        void change(boolean change);
    }
}
