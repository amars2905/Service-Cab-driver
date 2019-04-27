package com.taxi.taxidriver.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.taxi.taxidriver.R;
import com.taxi.taxidriver.ui.activity.MapsActivity;
import com.taxi.taxidriver.utils.BaseFragment;

import static com.taxi.taxidriver.ui.MainHomeActivity.tvEditProfile;


public class NewJobs extends BaseFragment implements View.OnClickListener {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_jobs, container, false);
        mContext = getActivity();

        rootView.findViewById(R.id.googleMap).setOnClickListener(this);
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
}
