package com.fic.bunnyshopmobiletry5.ui.CatalogoJDD;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;

import org.json.JSONArray;
import org.json.JSONObject;


import retrofit2.Callback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CatalogoJDDFragment extends Fragment {

    private CatalogoJDDViewModel mViewModel;

    public static CatalogoJDDFragment newInstance() {
        return new CatalogoJDDFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalogo_j_d_d, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CatalogoJDDViewModel.class);
        // TODO: Use the ViewModel
    }

    private void obtenerCatalogo(){
        // Obtén la instancia del servicio API
        apiService apiService = RetrofitInstance.getApiService();

        //Realiza la solicitud sin parametros
        Call<ResponseBody> call = apiService.getCatalogo();

        //Ejecuta la solicitud de forma asincrona
        call.enqueue(new Callback<ResponseBody>(){
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                // Verifica que la respuesta sea exitosa (código 200)
                if (response.isSuccessful()) {
                    try {
                        // Obtener la respuesta como un String
                        String responseString = response.body().string();
                        // Imprimir la respuesta cruda (opcional)
                        Log.d("API_RESPONSE_CATALOGO", "Respuesta del catálogo: " + responseString);

                        // Procesar la respuesta (ejemplo: convertirla en un JSON)
                        JSONArray jsonArray = new JSONArray(responseString);

                        // Recorrer el array y extraer datos
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extraer los datos del catálogo
                            int id = jsonObject.getInt("id_articulo");
                            String nombre = jsonObject.getString("nombre");
                            String imagen = jsonObject.getString("imagen");
                            int cantidad = jsonObject.getInt("cantidad");
                            String descripcion = jsonObject.getString("descripcion");

                            Log.d("CATALOGO_ITEM", "ID: " + id + ", Nombre: " + nombre + ", Imagen: " + imagen + ", Cantidad: " + cantidad + ", Descripcion: " + descripcion);

                        }
                    }catch (Exception e){
                        Log.e("API_ERROR_CATALOGO", "Error procesando la respuesta", e);
                    }
                }else{
                    Log.e("API_ERROR_CATALOGO", "Error en la respuesta: " + response.message());
                }
            }
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR_CATALOGO", "Error en la solicitud", t);
            }
        });
    }





}