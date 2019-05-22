package com.taxi.taxidriver.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxi.taxidriver.R;
import com.taxi.taxidriver.ui.map_activity.MapsActivity;
import com.taxi.taxidriver.utils.AppProgressDialog;
import com.taxi.taxidriver.utils.BaseFragment;
import com.taxi.taxidriver.utils.GpsTracker;


public class NewJobs extends BaseFragment implements View.OnClickListener, OnMapReadyCallback {
    private View rootView;
    private GoogleMap mMap;

    private double latitude = 0.0;
    private double longitude = 0.0;

    private Dialog dialog, dialogPaid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_jobs, container, false);
        mContext = getActivity();

        rootView.findViewById(R.id.googleMap).setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleMap:
                startActivity(new Intent(mContext, MapsActivity.class));
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    private void getLatLong() {
        GpsTracker gpsTracker = new GpsTracker(mContext);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        refreshLocation();
    }

    private void refreshLocation() {
        AppProgressDialog.show(dialog);
        if (latitude == 0) {
            AppProgressDialog.show(dialog);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLatLong();
                }
            }, 3000);
        } else {
            //deliveryJobApi();
        }
    }
}
