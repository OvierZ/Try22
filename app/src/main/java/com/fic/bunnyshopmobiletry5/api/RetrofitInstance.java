package com.fic.bunnyshopmobiletry5.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;

    // URL base para la API
    private static final String BASE_URL = enviroment.BASE_URL_API;


    public static apiService getApiService() {
        if (retrofit == null) {
            // Configurar OkHttpClient con tiempos de espera personalizados
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS) // Tiempo de espera para conectar
                    .readTimeout(10, TimeUnit.SECONDS)   // Tiempo de espera para leer datos
                    .writeTimeout(10, TimeUnit.SECONDS)  // Tiempo de espera para escribir datos
                    .build();

            // Construir la instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Establece la URL base
                    .client(okHttpClient) // Asocia el cliente OkHttp
                    .addConverterFactory(GsonConverterFactory.create()) // Convertidor JSON
                    .build();
        }
        return retrofit.create(apiService.class); // Retorna la implementación de ApiService
    }
}
