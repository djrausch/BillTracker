package com.djrausch.billtracker.views;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface GoogleLoginView extends MvpView {

    void showLoginSuccess(String token);
    void showLoginError(String error);
}
