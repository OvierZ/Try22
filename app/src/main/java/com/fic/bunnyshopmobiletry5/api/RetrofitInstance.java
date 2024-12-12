package com.fic.bunnyshopmobiletry5.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    //private static final String BASE_URL = "http://192.168.22.116:8000/api/";
//    private static final String BASE_URL = "https://9a74-2806-269-481-222-d8d9-3dfc-6276-bfdc.ngrok-free.app/api/";
    //private static final String BASE_URL = "https://bmxnmgv0-8000.usw3.devtunnels.ms/api/";

    private static final String BASE_URL = enviroment.BASE_URL_API;
    public static apiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)  // Tiempo de espera para conectar
                    .readTimeout(10, TimeUnit.SECONDS)    // Tiempo de espera para leer datos
                    .writeTimeout(10, TimeUnit.SECONDS)   // Tiempo de espera para escribir datos
                    .build())
                    .addConverterFactory(GsonConverterFactory.create())  // Agregar Gson como convertidor
                    .build();
        }
        return retrofit.create(apiService.class);
    }

}