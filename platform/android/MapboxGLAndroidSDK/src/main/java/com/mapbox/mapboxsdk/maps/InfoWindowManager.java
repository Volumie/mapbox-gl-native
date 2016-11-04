package com.mapbox.mapboxsdk.maps;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mapbox.mapboxsdk.annotations.InfoWindow;
import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.ArrayList;
import java.util.List;

class InfoWindowManager {

    private List<InfoWindow> infoWindows;
    private MapboxMap.InfoWindowAdapter infoWindowAdapter;
    private boolean allowConcurrentMultipleInfoWindows;

    private MapboxMap.OnInfoWindowClickListener onInfoWindowClickListener;
    private MapboxMap.OnInfoWindowLongClickListener onInfoWindowLongClickListener;
    private MapboxMap.OnInfoWindowCloseListener onInfoWindowCloseListener;

    InfoWindowManager() {
        this.infoWindows = new ArrayList<>();
    }

    void setInfoWindowAdapter(@Nullable MapboxMap.InfoWindowAdapter infoWindowAdapter) {
        this.infoWindowAdapter = infoWindowAdapter;
    }

    MapboxMap.InfoWindowAdapter getInfoWindowAdapter() {
        return infoWindowAdapter;
    }

    void setAllowConcurrentMultipleOpenInfoWindows(boolean allow) {
        allowConcurrentMultipleInfoWindows = allow;
    }

    boolean isAllowConcurrentMultipleOpenInfoWindows() {
        return allowConcurrentMultipleInfoWindows;
    }

    List<InfoWindow> getInfoWindows() {
        return infoWindows;
    }

    boolean isInfoWindowValidForMarker(@NonNull Marker marker) {
        return !TextUtils.isEmpty(marker.getTitle()) || !TextUtils.isEmpty(marker.getSnippet());
    }

    void setOnInfoWindowClickListener(@Nullable MapboxMap.OnInfoWindowClickListener listener) {
        onInfoWindowClickListener = listener;
    }

    MapboxMap.OnInfoWindowClickListener getOnInfoWindowClickListener() {
        return onInfoWindowClickListener;
    }

    void setOnInfoWindowLongClickListener(@Nullable MapboxMap.OnInfoWindowLongClickListener listener) {
        onInfoWindowLongClickListener = listener;
    }

    MapboxMap.OnInfoWindowLongClickListener getOnInfoWindowLongClickListener() {
        return onInfoWindowLongClickListener;
    }

    void setOnInfoWindowCloseListener(@Nullable MapboxMap.OnInfoWindowCloseListener listener) {
        onInfoWindowCloseListener = listener;
    }

    MapboxMap.OnInfoWindowCloseListener getOnInfoWindowCloseListener() {
        return onInfoWindowCloseListener;
    }
}
