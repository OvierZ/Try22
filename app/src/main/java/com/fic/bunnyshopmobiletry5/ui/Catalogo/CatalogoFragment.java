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
import com.fic.bunnyshopmobiletry5.catalogoAdapter.WishListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CatalogoFragment extends Fragment {


    private RecyclerView recyclerView;
    private WishListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogo, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewCatalog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fetchCatalogData();


        return view;
    }


    private void fetchCatalogDataFix() {
        apiService ApiService = RetrofitInstance.getApiService();


        ApiService.getCatalog().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {

//                getWishlist
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new WishListAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                }
            }


            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                // Manejo de errores
            }
        });
    }

    private void fetchCatalogData() {
        apiService apiService = RetrofitInstance.getApiService();

        // Usamos el tipo correcto en la llamada
        Call<List<Map<String, Object>>> call = apiService.getWishlist();

        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Obtenemos los datos directamente de la respuesta
                        List<Map<String, Object>> itemList = response.body();

                        // Creamos el adaptador con los datos obtenidos
                        adapter = new WishListAdapter(itemList);
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


    private List<Map<String, Object>> convertJsonArrayToList(JSONArray jsonArray) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    map.put(key, jsonObject.get(key));
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
