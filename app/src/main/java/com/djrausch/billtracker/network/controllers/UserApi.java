package com.djrausch.billtracker.network.controllers;

import com.djrausch.billtracker.models.UserLoginResponse;
import com.djrausch.billtracker.network.NetworkConfig;
import com.djrausch.billtracker.network.services.UserApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    static UserApiService apiService = retrofit.create(UserApiService.class);


    public static Call<UserLoginResponse> googleLogin(String googleToken) {
        return apiService.googleLoginUser(googleToken);
    }
}
