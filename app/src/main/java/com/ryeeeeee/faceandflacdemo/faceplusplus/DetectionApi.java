package com.ryeeeeee.faceandflacdemo.faceplusplus;

import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by Ryeeeeee on 1/12/16.
 */
public interface DetectionApi {

    @Headers("Accept: application/json")
    @Multipart
    @POST("detection/detect")
    Call<DetectionResult> detect(@Part("api_key") RequestBody apiKey,
                                 @Part("api_secret") RequestBody apiSecret,
                                 @Part("img\";filename=\"image\" ") RequestBody file);
}
