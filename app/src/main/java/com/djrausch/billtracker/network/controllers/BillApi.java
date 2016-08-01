package com.djrausch.billtracker.network.controllers;

import android.util.Log;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.network.NetworkConfig;
import com.djrausch.billtracker.network.services.BillApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BillApi {
    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    static BillApiService apiService = retrofit.create(BillApiService.class);

    public static void getUserBills() {
        apiService.getUserBills(BillTrackerApplication.getUserToken()).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, final Response<List<Bill>> response) {
                BillTrackerApplication.getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(response.body());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {

            }
        });
    }

    public static void createNewBill(Bill bill) {
        apiService.createNewBill(BillTrackerApplication.getUserToken(), bill).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                Log.d("onResponse", "Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {

            }
        });
    }
}
