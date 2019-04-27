package com.taxi.taxidriver.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taxi.taxidriver.R;
import com.taxi.taxidriver.constant.Constant;
import com.taxi.taxidriver.ui.MainHomeActivity;
import com.taxi.taxidriver.utils.BaseFragment;
import static com.taxi.taxidriver.ui.activity.LoginActivity.loginfragmentManager;


public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private View rootview;
    private TextView tvSignUp,tvSignIn,tvForgotPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_login_layout, container, false);
        activity = getActivity();
        mContext = getActivity();
        init();
        return rootview;
    }

    private void init() {
        tvSignIn = rootview.findViewById(R.id.tvSignIn);
        tvSignUp = rootview.findViewById(R.id.tvSignUp);
        tvForgotPassword = rootview.findViewById(R.id.tvForgotPassword);
        tvSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
    }

    private void startFragment(String tag, Fragment fragment) {
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignIn:
                startActivity(new Intent(mContext, MainHomeActivity.class));
                break;
            case R.id.tvSignUp:
                startFragment(Constant.SignUpFragment, new SignUpFragment());
                break;
                case R.id.tvForgotPassword:
                startFragment(Constant.ForgotPasswordFragment, new ForgotPasswordFragment());
                break;
        }

    }
}
