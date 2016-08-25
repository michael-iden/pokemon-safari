package com.magnetic.pokemonsafari.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.Button;
import android.widget.Toast;

import com.magnetic.pokemonsafari.PokemonSafariApplication;
import com.magnetic.pokemonsafari.R;

import dji.sdk.Camera.DJICamera;
import dji.sdk.Camera.DJICamera.CameraReceivedVideoDataCallback;
import dji.sdk.Codec.DJICodecManager;
import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.FlightController.DJISimulator;
import dji.sdk.base.DJIBaseComponent.DJICompletionCallback;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.base.DJIBaseProduct.Model;
import dji.sdk.base.DJIError;
import dji.sdk.Camera.DJICameraSettingsDef.CameraMode;
import dji.sdk.Camera.DJICameraSettingsDef.CameraShootPhotoMode;

public class FlightActivity extends Activity implements SurfaceTextureListener, OnClickListener {

    private static final String TAG = FlightActivity.class.getName();
    protected DJICamera.CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;

    // Codec for video live view
    protected boolean simulation = false;
    protected DJICodecManager codecManager = null;

    protected TextureView videoSurface = null;
    protected SurfaceView overlaySurface = null;
    protected SurfaceHolder overlayHolder = null;

    private Button captureButton;

    Bitmap reticleBitmap;
    String simulatorOutput = "";

    public FlightActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PokemonSafariApplication.getFlightController().setVirtualStickAdvancedModeEnabled(true);

        simulation = getIntent().getBooleanExtra("simulation", false);
        Log.i(TAG, String.format("SIMULATION MODE?  %b", simulation));
        initUI();

        // The callback for receiving the raw H264 video data for camera live view
        mReceivedVideoDataCallBack = new CameraReceivedVideoDataCallback() {

            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if (codecManager != null) {
                    // Send the raw H264 video data to codec manager for decoding
                    codecManager.sendDataToDecoder(videoBuffer, size);
                } else {
                    Log.e(TAG, "codecManager is null");
                }
            }
        };

        DJICamera camera = PokemonSafariApplication.getCameraInstance();
    }

    protected void onProductChange() {
        initPreviewer();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
        initPreviewer();
        onProductChange();

        if (videoSurface == null) {
            Log.e(TAG, "videoSurface is null");
        }
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        uninitPreviewer();
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
    }

    public void onReturn(View view) {
        Log.e(TAG, "onReturn");
        this.finish();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        uninitPreviewer();
        super.onDestroy();
    }

    private void initUI() {
        // init videoSurface
        videoSurface = (TextureView) findViewById(R.id.video_previewer_surface);
        overlaySurface = (SurfaceView)findViewById(R.id.overlay_surface);
        captureButton = (Button) findViewById(R.id.btn_capture);

        if (null != videoSurface) {
            videoSurface.setSurfaceTextureListener(this);
        }

        if (null != overlaySurface) {
            overlaySurface.setZOrderOnTop(true);
            overlaySurface.getHolder().setFormat(PixelFormat.TRANSPARENT);
            overlaySurface.getHolder().addCallback(new SurfaceHolder.Callback() {
                                                       @Override
                                                       public void surfaceCreated(SurfaceHolder surfaceHolder) {
                                                           overlayHolder = surfaceHolder;
                                                           redrawOverlay();
                                                       }

                                                       @Override
                                                       public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                                                       }

                                                       @Override
                                                       public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                                                            overlayHolder = null;
                                                       }
                                                   }
            );
        }

        captureButton.setOnClickListener(this);
    }

    private void initPreviewer() {

        DJIBaseProduct product = PokemonSafariApplication.getProductInstance();
        reticleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.reticle);
        if (product == null || !product.isConnected()) {
            showToast(getString(R.string.disconnected));
        } else {
            if (null != videoSurface) {
                videoSurface.setSurfaceTextureListener(this);
            }

            if (!product.getModel().equals(Model.UnknownAircraft)) {
                DJICamera camera = product.getCamera();
                if (camera != null) {
                    // Set the callback
                    camera.setDJICameraReceivedVideoDataCallback(mReceivedVideoDataCallBack);
                }
            }
        }
    }

    private void uninitPreviewer() {
        if (reticleBitmap != null) {
            reticleBitmap.recycle();
            reticleBitmap = null;
        }

        DJICamera camera = PokemonSafariApplication.getCameraInstance();
        if (camera != null) {
            // Reset the callback
            PokemonSafariApplication.getCameraInstance().setDJICameraReceivedVideoDataCallback(null);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureAvailable");
        if (codecManager == null) {
            codecManager = new DJICodecManager(this, surface, width, height);
        }

        if (simulation) {
            DJISimulator simulator = PokemonSafariApplication.getSimulator();
            DJISimulator.DJISimulatorInitializationData initializationData = new DJISimulator.DJISimulatorInitializationData(0.0, 0.0, 10, 10);
            simulator.setUpdatedSimulatorStateDataCallback(djiSimulatorStateData -> {
                Log.i(TAG, "SIMULATION FRAME!!!!");
                String yaw = String.format("%.2f", djiSimulatorStateData.getYaw());
                String pitch = String.format("%.2f", djiSimulatorStateData.getPitch());
                String roll = String.format("%.2f", djiSimulatorStateData.getRoll());
                String positionX = String.format("%.2f", djiSimulatorStateData.getPositionX());
                String positionY = String.format("%.2f", djiSimulatorStateData.getPositionY());
                String positionZ = String.format("%.2f", djiSimulatorStateData.getPositionZ());
                simulatorOutput = "Yaw : " + yaw + ", Pitch : " + pitch + ", Roll : " + roll + "\n" + "PosX : " + positionX +
                        ", PosY : " + positionY +
                        ", PosZ : " + positionZ;
                redrawOverlay();

            });
            simulator.startSimulator(initializationData, error -> { Log.i(TAG, "SIMULATOR FAILED: " + error); });
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e(TAG, "onSurfaceTextureDestroyed");
        if (codecManager != null) {
            codecManager.cleanSurface();
            codecManager = null;
        }

        if (simulation) {
            PokemonSafariApplication.getSimulator().stopSimulator(null);
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(FlightActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_capture: {
                captureAction();
                break;
            }

            default:
                break;
        }
    }

    // Method for taking photo
    private void captureAction() {

        CameraMode cameraMode = CameraMode.ShootPhoto;

        final DJICamera camera = PokemonSafariApplication.getCameraInstance();
        if (camera != null) {

            CameraShootPhotoMode photoMode = CameraShootPhotoMode.Single; // Set the camera capture mode as Single mode
            camera.startShootPhoto(photoMode, new DJICompletionCallback() {

                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        showToast("take photo: success");
                    } else {
                        showToast(error.getDescription());
                    }
                }

            }); // Execute the startShootPhoto API
        }
    }

    private void redrawOverlay() {
        if (overlayHolder == null) {
            return;
        }

        Canvas canvas = overlayHolder.lockCanvas();
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);

        DJIFlightController fc = PokemonSafariApplication.getFlightController();
        String rotor = String.format("Motors On: %b", fc.getCurrentState().areMotorsOn());
        String isInTheAir = "On the ground";
        if (fc.getCurrentState().isFlying()) {
            isInTheAir = "Flying";
        } else if (fc.canTakeOff()) {
            isInTheAir = "Ready for takeoff";
        }

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100.0f);
        canvas.drawText(rotor + ",   " + isInTheAir, 100.0f, 100.0f, paint);

        if (reticleBitmap != null) {
            Rect src = new Rect(0, 0, reticleBitmap.getWidth(), reticleBitmap.getHeight());
            float desiredWidth = 100;
            float desiredHeight = 100;
            float left = (canvas.getWidth() - desiredWidth) / 2.0f;
            float top = (canvas.getHeight() - desiredHeight) / 2.0f;
            RectF dst = new RectF(left, top, left + desiredWidth, top + desiredHeight);
            canvas.drawBitmap(reticleBitmap, src, dst, new Paint());
        }

        if (simulation) {
            String[] lines = simulatorOutput.split("\n");
            int i = 0;
            for (String line : lines) {
                canvas.drawText(line, 100.0f, 200.0f + (100 * i), paint);
                i++;
            }
        }
        overlayHolder.unlockCanvasAndPost(canvas);
    }
}
