package com.example.android.gymkhagymkha.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.android.gymkhagymkha.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Mapa extends android.support.v4.app.Fragment {

    //private GoogleMap mMap;

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final double DEFAULT_RADIUS = 1000000;
    public static final double RADIUS_OF_EARTH_METERS = 6371009;

    private static final int WIDTH_MAX = 50;
    private static final int HUE_MAX = 360;
    private static final int ALPHA_MAX = 255;

    private GoogleMap mMap;

    private List<DraggableCircle> mCircles = new ArrayList<DraggableCircle>(1);

    private SeekBar mColorBar;
    private SeekBar mAlphaBar;
    private SeekBar mWidthBar;
    private int mStrokeColor;
    private int mFillColor;

    private class DraggableCircle {
        private final Marker centerMarker;
        private final Marker radiusMarker;
        private final Circle circle;
        private double radius;
        public DraggableCircle(LatLng center, double radius) {
            this.radius = radius;
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(toRadiusLatLng(center, radius))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public DraggableCircle(LatLng center, LatLng radiusLatLng) {
            this.radius = toRadiusMeters(center, radiusLatLng);
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(radiusLatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(centerMarker)) {
                circle.setCenter(marker.getPosition());
                radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
                return true;
            }
            if (marker.equals(radiusMarker)) {
                radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                circle.setRadius(radius);
                return true;
            }
            return false;
        }
        public void onStyleChange() {
            circle.setStrokeWidth(mWidthBar.getProgress());
            circle.setFillColor(mFillColor);
            circle.setStrokeColor(mStrokeColor);
        }
    }

    /** Generate LatLng of radius marker */
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }


    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        map.setContentDescription("Google Map with circles.");

        mFillColor = Color.HSVToColor(
                mAlphaBar.getProgress(), new float[]{mColorBar.getProgress(), 1, 1});
        mStrokeColor = Color.BLACK;

        DraggableCircle circle = new DraggableCircle(SYDNEY, DEFAULT_RADIUS);
        mCircles.add(circle);

        // Move the map so that it is centered on the initial circle
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 4.0f));

    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mColorBar) {
            mFillColor = Color.HSVToColor(Color.alpha(mFillColor), new float[] {progress, 1, 1});
        } else if (seekBar == mAlphaBar) {
            mFillColor = Color.argb(progress, Color.red(mFillColor), Color.green(mFillColor),
                    Color.blue(mFillColor));
        }

        for (DraggableCircle draggableCircle : mCircles) {
            draggableCircle.onStyleChange();
        }
    }
    private void onMarkerMoved(Marker marker) {
        for (DraggableCircle draggableCircle : mCircles) {
            if (draggableCircle.onMarkerMoved(marker)) {
                break;
            }
        }
    }


    public void onMapLongClick(LatLng point) {
        // We know the center, let's place the outline at a point 3/4 along the view.
        View view = ((SupportMapFragment) this.getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                .getView();
        LatLng radiusLatLng = mMap.getProjection().fromScreenLocation(new Point(
                view.getHeight() * 3 / 4, view.getWidth() * 3 / 4));

        // ok create it
        DraggableCircle circle = new DraggableCircle(point, radiusLatLng);
        mCircles.add(circle);
    }


    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        return view;
        //setUpMapIfNeeded();
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }
}
