package com.fic.bunnyshopmobiletry5.ui.CatalogoJDD;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.CatalogoAdapter;

public class CatalogoJDDFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private CatalogoAdapter adapter;
    private List<Map<String, Object>> productos = new ArrayList<>(); // Lista de productos

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_catalogo_j_d_d, container, false);

        // Inicializa el RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Configura el layout manager

        // Inicializa el adaptador con la lista vacía y enlázalo al RecyclerView
        adapter = new CatalogoAdapter(getContext(), productos);
        recyclerView.setAdapter(adapter);

        // Llama al método para obtener los datos del catálogo
        obtenerCatalogo();

        return rootView;
    }

    private void obtenerCatalogo() {
        apiService apiService = RetrofitInstance.getApiService();
        Call<ResponseBody> call = apiService.getCatalogo();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseString);
                        Log.d("Respuesta", jsonArray.toString());
                        // Procesar los datos
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Map<String, Object> producto = Map.of(
                                    "id_articulo", jsonObject.getString("id_articulos"),
                                    "nombre", jsonObject.getString("nombre"),
                                    "descripcion", jsonObject.getString("descripcion"),
                                    "precio", jsonObject.getDouble("precio"),
                                    "imagen", jsonObject.getString("imagen")
                            );

                            productos.add(producto); // Agrega a la lista de productos
                        }

                        // Notifica al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("API_ERROR_CATALOGO", "Error procesando la respuesta", e);
                    }
                } else {
                    Log.e("API_ERROR_CATALOGO", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR_CATALOGO", "Error en la solicitud", t);
            }
        });
    }
}
