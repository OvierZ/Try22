package com.fic.bunnyshopmobiletry5.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface apiService {
    @GET("cotizaciones/usd")
    Call<ResponseBody> getData();

    @GET("estado")
    Call<ResponseBody> getEstadoApi();
}

