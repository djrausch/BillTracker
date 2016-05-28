package com.djrausch.billtracker.presenter;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.view.MainView;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.realm.RealmResults;

public class MainPresenter extends MvpBasePresenter<MainView> {
    public RealmResults<Bill> loadBills() {
        return BillTrackerApplication.getRealm().where(Bill.class).findAllSorted("dueDate");
    }
}
