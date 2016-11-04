package com.mapbox.mapboxsdk.maps;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;

import java.util.concurrent.TimeUnit;

class Transform {

    private MapboxMap mapboxMap;
    private MapView mapView;

    private CameraPosition cameraPosition;

    private MapboxMap.OnCameraChangeListener onCameraChangeListener;

    Transform(MapboxMap mapboxMap, MapView mapView) {
        this.mapboxMap = mapboxMap;
        this.mapView = mapView;
    }

    //
    // Camera API
    //

    @UiThread
    public final CameraPosition getCameraPosition() {
        invalidateCameraPosition();
        return cameraPosition;
    }

    @UiThread
    final void moveCamera(CameraUpdate update, MapboxMap.CancelableCallback callback) {
        cameraPosition = update.getCameraPosition(mapboxMap);
        mapView.resetTrackingModesIfRequired(cameraPosition);
        mapView.jumpTo(cameraPosition.bearing, cameraPosition.target, cameraPosition.tilt, cameraPosition.zoom);
        if (callback != null) {
            callback.onFinish();
        }

        if (onCameraChangeListener != null) {
            onCameraChangeListener.onCameraChange(this.cameraPosition);
        }
    }

    @UiThread
    final void easeCamera(
            CameraUpdate update, int durationMs, boolean easingInterpolator, boolean resetTrackingMode, final MapboxMap.CancelableCallback callback) {
        // dismiss tracking, moving camera is equal to a gesture
        cameraPosition = update.getCameraPosition(mapboxMap);
        if (resetTrackingMode) {
            mapView.resetTrackingModesIfRequired(cameraPosition);
        }

        mapView.easeTo(cameraPosition.bearing, cameraPosition.target, getDurationNano(durationMs), cameraPosition.tilt,
                cameraPosition.zoom, easingInterpolator, new MapboxMap.CancelableCallback() {
                    @Override
                    public void onCancel() {
                        if (callback != null) {
                            callback.onCancel();
                        }
                        invalidateCameraPosition();
                    }

                    @Override
                    public void onFinish() {
                        if (callback != null) {
                            callback.onFinish();
                        }
                        invalidateCameraPosition();
                    }
                });
    }

    @UiThread
    final void animateCamera(CameraUpdate update, int durationMs, final MapboxMap.CancelableCallback callback) {
        cameraPosition = update.getCameraPosition(mapboxMap);
        mapView.resetTrackingModesIfRequired(cameraPosition);
        mapView.flyTo(cameraPosition.bearing, cameraPosition.target, getDurationNano(durationMs), cameraPosition.tilt,
                cameraPosition.zoom, new MapboxMap.CancelableCallback() {
                    @Override
                    public void onCancel() {
                        if (callback != null) {
                            callback.onCancel();
                        }
                        invalidateCameraPosition();
                    }

                    @Override
                    public void onFinish() {
                        if (onCameraChangeListener != null) {
                            onCameraChangeListener.onCameraChange(cameraPosition);
                        }

                        if (callback != null) {
                            callback.onFinish();
                        }
                        invalidateCameraPosition();
                    }
                });
    }

    @UiThread
    void invalidateCameraPosition() {
        CameraPosition cameraPosition = mapView.invalidateCameraPosition();
        if (cameraPosition != null) {
            this.cameraPosition = cameraPosition;
        }

        if (onCameraChangeListener != null) {
            onCameraChangeListener.onCameraChange(this.cameraPosition);
        }
    }

    @UiThread
    void resetNorth() {
        mapView.resetNorth();
    }

    void setOnCameraChangeListener(@Nullable MapboxMap.OnCameraChangeListener listener) {
        this.onCameraChangeListener = listener;
    }

    private long getDurationNano(long durationMs) {
        return durationMs > 0 ? TimeUnit.NANOSECONDS.convert(durationMs, TimeUnit.MILLISECONDS) : 0;
    }
}
