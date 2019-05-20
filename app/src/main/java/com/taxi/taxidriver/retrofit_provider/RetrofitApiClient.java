package com.taxi.taxidriver.retrofit_provider;

import com.taxi.taxidriver.constant.Constant;
import com.taxi.taxidriver.modal.main_category_modal.TaxiMainCategoryModal;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApiClient {
    @GET(Constant.MAIN_CATEGORY_API)
    Call<TaxiMainCategoryModal> mainCategoryData();

    @FormUrlEncoded
    @POST(Constant.LOGIN_API)
    Call<ResponseBody> loginData(@Field("email") String email,
                                 @Field("password") String password);

    @FormUrlEncoded
    @POST(Constant.FORGOT_PASSWORD_API)
    Call<ResponseBody> forgotPasswordData(@Field("email") String email);

    @Multipart
    @POST(Constant.DRIVER_REGISTRATION_API)
    Call<ResponseBody> updateProfile(@Part("name") RequestBody name,
                                     @Part("password") RequestBody password,
                                     @Part("mobile_number") RequestBody mobile_number,
                                     @Part("email") RequestBody email,
                                     @Part("gender") RequestBody gender,
                                     @Part("city") RequestBody city,
                                     @Part("state") RequestBody state,
                                     @Part("country") RequestBody country,
                                     @Part("category_id") RequestBody category_id,
                                     @Part("subcategory_id") RequestBody subcategory_id,
                                     @Part("aadhar_number") RequestBody aadhar_number,
                                     @Part("driving_license_number") RequestBody driving_license_number,
                                     @Part("insurance_expiration") RequestBody insurance_expiration,
                                     @Part("model_number") RequestBody model_number,
                                     @Part("vehicle_number") RequestBody vehicle_number,
                                     @Part("driving_license_image") RequestBody driving_license_image,
                                     @Part("insurance_image") RequestBody insurance_image,
                                     @Part("aadhar_cart_image") RequestBody aadhar_cart_image,
                                     @Part("vehicle_image") RequestBody vehicle_image,
                                     @Part MultipartBody.Part profile_image);


}