package com.djrausch.billtracker.network.services;

import com.djrausch.billtracker.models.Bill;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BillApiService {
    @GET("api/bills")
    Call<List<Bill>> getUserBills(@Query("token") String token);
}
