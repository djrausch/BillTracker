package com.djrausch.billtracker.presenters;

import com.djrausch.billtracker.models.UserLoginResponse;
import com.djrausch.billtracker.network.controllers.UserApi;
import com.djrausch.billtracker.views.GoogleLoginView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleLoginPresenter extends MvpBasePresenter<GoogleLoginView> {

    public void googleLogin(String googleToken) {
        UserApi.googleLogin(googleToken).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.code() == 200) {
                    //Successful login
                    if (getView() != null) {
                        getView().showLoginSuccess(response.body().token);
                    }
                } else {
                    //Error
                    if (getView() != null) {
                        getView().showLoginError("error!");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                //Error
            }
        });
    }
}
