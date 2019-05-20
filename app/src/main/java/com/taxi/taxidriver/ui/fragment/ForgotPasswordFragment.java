package com.taxi.taxidriver.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taxi.taxidriver.R;
import com.taxi.taxidriver.constant.Constant;
import com.taxi.taxidriver.retrofit_provider.RetrofitService;
import com.taxi.taxidriver.retrofit_provider.WebResponse;
import com.taxi.taxidriver.utils.BaseFragment;
import com.taxi.taxidriver.utils.ConnectionDirector;
import com.taxi.taxidriver.utils.pinview.Pinview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.taxi.taxidriver.ui.activity.LoginActivity.loginfragmentManager;

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {
    private View rootview;
    private Button btn_fplogin;
    private TextView otpTime;
    private LinearLayout resendLayout;
    private Pinview pinview1;
    private String strMobile, strOtp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        activity = getActivity();
        mContext = getActivity();
        cd = new ConnectionDirector(mContext);
        init();
        return rootview;
    }

    private void init() {
        ((Button) rootview.findViewById(R.id.btn_fplogin)).setOnClickListener(this);
        ((TextView) rootview.findViewById(R.id.tvLogin)).setOnClickListener(this);

    }

    private void startFragment(String tag, Fragment fragment) {
        loginfragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.login_frame, fragment, tag).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fplogin:
                startFragment(Constant.SignUpFragment, new LoginFragment());
                break;
            case R.id.tvLogin:
                startFragment(Constant.SignUpFragment, new LoginFragment());
                break;
        }
    }

    private void forgotPasswordApi() {
        if (cd.isNetWorkAvailable()) {
            String eAddress = ((EditText) rootview.findViewById(R.id.etEmailAddress)).getText().toString();

            RetrofitService.getServerResponce(new Dialog(mContext), retrofitApiClient.forgotPasswordData(eAddress), new WebResponse() {
                @Override
                public void onResponseSuccess(Response<?> result) throws JSONException {
                    ResponseBody responseBody = (ResponseBody) result.body();

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                      /*  if (!responseBody.string("")){

                        }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onResponseFailed(String error) {

                }
            });
        }
    }
}
