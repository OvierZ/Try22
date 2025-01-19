package com.fic.bunnyshopmobiletry5.ui.Catalogo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.CatalogoAdapter;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogoFragment extends Fragment {

    private RecyclerView recyclerView;
    private CatalogoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_catalogo, container, false);

        // Inicializamos el RecyclerView y el adaptador
        recyclerView = view.findViewById(R.id.recyclerViewCatalog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Asegúrate de usar el contexto adecuado

        // Llamamos a la función para cargar el catálogo
        fetchCatalogData();

        return view;
    }

    // Método para obtener los datos del catálogo de la API
    private void fetchCatalogData() {
        apiService apiService = RetrofitInstance.getApiService();

        // Realizamos la solicitud a la API para obtener el catálogo
        Call<List<Map<String, Object>>> call = apiService.getCatalog();

        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Obtenemos los datos de la respuesta
                        List<Map<String, Object>> productos = response.body();

                        // Creamos el adaptador pasándole el contexto y la lista de productos
                        adapter = new CatalogoAdapter(requireContext(), productos);

                        // Asignamos el adaptador al RecyclerView
                        recyclerView.setAdapter(adapter);

                        // Notificamos al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("API_ERROR_CATALOGO", "Error procesando la respuesta", e);
                    }
                } else {
                    Log.e("API_ERROR_CATALOGO", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.e("API_ERROR_CATALOGO", "Error en la solicitud", t);
            }
        });
    }
}



