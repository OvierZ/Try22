package com.fic.bunnyshopmobiletry5.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://mx.dolarapi.com/v1/";

    public static apiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())  // Agregar Gson como convertidor
                    .build();
        }
        return retrofit.create(apiService.class);
    }
}