package com.djrausch.billtracker.network.services;

import com.djrausch.billtracker.models.UserLoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApiService {
    @FormUrlEncoded
    @POST("api/googlelogin")
    Call<UserLoginResponse> googleLoginUser(@Field("google_token") String googleToken);
}
