package com.fic.bunnyshopmobiletry5.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiService {
    @GET("cotizaciones/usd")
    Call<ResponseBody> getData();

    @GET("estado")
    Call<ResponseBody> getEstadoApi();

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("email") String email, @Field("password") String password);

    @GET("catalogo")
    Call<ResponseBody> getCatalogo();
}

