package com.taxi.taxidriver.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taxi.taxidriver.retrofit_provider.RetrofitApiClient;


public class

BaseFragment extends Fragment {
    public Context mContext;
    public Activity activity;
    public ConnectionDirector cd;
    public RetrofitApiClient retrofitApiClient;
    public BaseFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        mContext = getActivity();
 /*       cd = new ConnectionDirector(mContext);
        retrofitApiClient = RetrofitService.getRetrofit();*/
        return null;
    }
}
