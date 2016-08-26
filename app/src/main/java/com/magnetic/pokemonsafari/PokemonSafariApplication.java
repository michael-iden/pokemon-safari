package com.magnetic.pokemonsafari;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import dji.sdk.Camera.DJICamera;
import dji.sdk.FlightController.DJICompass;
import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.FlightController.DJISimulator;
import dji.sdk.Products.DJIAircraft;
import dji.sdk.Products.DJIHandHeld;
import dji.sdk.SDKManager.DJISDKManager;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseComponent.DJIComponentListener;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.base.DJIBaseProduct.DJIBaseProductListener;
import dji.sdk.base.DJIBaseProduct.DJIComponentKey;
import dji.sdk.base.DJIError;
import dji.sdk.base.DJISDKError;

public class PokemonSafariApplication extends Application {

    public static final String FLAG_CONNECTION_CHANGE = "fpv_tutorial_connection_change";

    private static DJIBaseProduct baseProduct;
    private Handler connectionNotificationLooper;

    /**
     * This function is used to get the instance of DJIBaseProduct.
     * If no product is connected, it returns null.
     */
    public static synchronized DJIBaseProduct getProductInstance() {
        if (null == baseProduct) {
            baseProduct = DJISDKManager.getInstance().getDJIProduct();
        }
        return baseProduct;
    }

    public static boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof DJIAircraft;
    }

    public static boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof DJIHandHeld;
    }

    public static synchronized DJICamera getCameraInstance() {

        if (getProductInstance() == null) return null;
        return getProductInstance().getCamera();

    }

    public static synchronized DJIFlightController getFlightController() {
        if (isAircraftConnected()) {
            DJIAircraft aircraft = (DJIAircraft)getProductInstance();
            return aircraft.getFlightController();
        }
        return null;
    }

    public static synchronized DJISimulator getSimulator() {
        if (isAircraftConnected()) {
            return ((DJIAircraft) getProductInstance()).getFlightController().getSimulator();
        }
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        connectionNotificationLooper = new Handler(Looper.getMainLooper());
        //This is used to start SDK services and initiate SDK.
        DJISDKManager.getInstance().initSDKManager(this, sdkRegistrationCallback);
    }

    /**
     * When starting SDK services, an instance of interface DJISDKManager.DJISDKManagerCallback will be used to listen to
     * the SDK Registration result and the product changing.
     */
    private DJISDKManager.DJISDKManagerCallback sdkRegistrationCallback = new DJISDKManager.DJISDKManagerCallback() {

        //Listens to the SDK registration result
        @Override
        public void onGetRegisteredResult(DJIError error) {

            if (error == DJISDKError.REGISTRATION_SUCCESS) {
                DJISDKManager.getInstance().startConnectionToProduct();
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(getApplicationContext(), "Register sdk fails, check network is available", Toast.LENGTH_LONG).show());
            }
            Log.e("TAG", error.toString());
        }

        //Listens to the connected product changing, including two parts, component changing or product connection changing.
        @Override
        public void onProductChanged(DJIBaseProduct oldProduct, DJIBaseProduct newProduct) {

            baseProduct = newProduct;
            if (baseProduct != null) {
                baseProduct.setDJIBaseProductListener(mDJIBaseProductListener);
            }

            notifyStatusChange();
        }
    };

    private DJIBaseProductListener mDJIBaseProductListener = new DJIBaseProductListener() {
        @Override
        public void onComponentChange(DJIComponentKey key, DJIBaseComponent oldComponent, DJIBaseComponent newComponent) {
            if (newComponent != null) {
                newComponent.setDJIComponentListener(componentConnectionListener);
            }
            notifyStatusChange();
        }

        @Override
        public void onProductConnectivityChanged(boolean isConnected) {
            notifyStatusChange();
        }

    };

    private DJIComponentListener componentConnectionListener = isConnected -> notifyStatusChange();

    public static DJICompass getCompass() {
        if (getFlightController() != null) {
            return getFlightController().getCompass();
        }
        return null;
    }

    private void notifyStatusChange() {
        connectionNotificationLooper.removeCallbacks(updateRunnable);
        connectionNotificationLooper.postDelayed(updateRunnable, 500);
    }

    private Runnable updateRunnable = () -> {
        Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
        sendBroadcast(intent);
    };

}
