package com.djrausch.billtracker.network.controllers;

import android.util.Log;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.BillPaid;
import com.djrausch.billtracker.network.NetworkConfig;
import com.djrausch.billtracker.network.services.BillApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level;

public class BillApi {
    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(Level.BODY);
    // set your desired log level
    static OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();

    static Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(NetworkConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    static BillApiService apiService = retrofit.create(BillApiService.class);

    public static void getUserBills() {
        apiService.getUserBills(BillTrackerApplication.getUserToken()).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, final Response<List<Bill>> response) {
                for (Bill b : response.body()) {
                    Log.d("Bill", b.toString());
                }
                BillTrackerApplication.getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(response.body());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void createNewBill(Bill bill) {
        apiService.createNewBill(BillTrackerApplication.getUserToken(), bill).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                Log.d("Create-onResponse", "Code: " + response.code());
                if (response.code() != 200) {
                    for (String line : response.errorBody().toString().split("\n")) {
                        Log.d("Create-onResponse", line);
                    }
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {

            }
        });
    }

    public static void updateBill(Bill bill) {
        Log.d("UpdateBill", bill.toString());
        //ill.name, bill.description, bill.repeatingType, new Date(), bill.payUrl
        apiService.updateBill(bill.uuid, BillTrackerApplication.getUserToken(), bill.name, bill.description, bill.repeatingType, new DateTime(bill.dueDate).toString("yyyy-MM-dd HH:mm:ss"), bill.payUrl).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                Log.d("Update-onResponse", "Code: " + response.code());
                if (response.code() != 200) {
                    try {
                        for (String line : response.errorBody().string().split("\n")) {
                            Log.d("Update-onResponse", line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void deleteBill(String billUuid) {
        apiService.deleteBill(billUuid, BillTrackerApplication.getUserToken()).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if (response.body() != null) {
                    Log.d("delete-onResponse", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {

            }
        });
    }

    public static void markBillPaid(String billUuid, BillPaid billPaid) {
        apiService.createBillPaid(billUuid, BillTrackerApplication.getUserToken(), billPaid.uuid, new DateTime(billPaid.date).toString("yyyy-MM-dd HH:mm:ss")).enqueue(new Callback<BillPaid>() {
            @Override
            public void onResponse(Call<BillPaid> call, Response<BillPaid> response) {
                if (response.body() != null) {
                    Log.d("billPaid-onResponse", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<BillPaid> call, Throwable t) {

            }
        });
    }
}
