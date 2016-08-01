package com.djrausch.billtracker.network.services;

import com.djrausch.billtracker.models.Bill;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BillApiService {
    @GET("api/bills")
    Call<List<Bill>> getUserBills(@Query("token") String token);

    @POST("api/bills/new")
    Call<Bill> createNewBill(@Query("token") String token, @Body Bill bill);

    @FormUrlEncoded
    @POST("api/bills/{uuid}/update")
    Call<Bill> updateBill(@Path("uuid") String uuid, @Query("token") String token, @Field("name") String name, @Field("description") String description, @Field("repeating_type") int repeatingType, @Field("due_date") String dueDate, @Field("pay_url") String payUrl);

    @POST("api/bills/{uuid}/delete")
    Call<Bill> deleteBill(@Path("uuid") String uuid, @Query("token") String token);
}
