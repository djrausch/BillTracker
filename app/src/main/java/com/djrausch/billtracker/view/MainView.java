package com.djrausch.billtracker.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface MainView extends MvpView {
    void showLoading(boolean loading);
}
