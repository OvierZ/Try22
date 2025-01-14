package com.fic.bunnyshopmobiletry5.ui.Catalogo;

import android.os.Bundle;
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
import retrofit2.Retrofit;


public class CatalogoFragment extends Fragment {


    private RecyclerView recyclerView;
    private CatalogoAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogo, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewCatalog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fetchCatalogData();


        return view;
    }


    private void fetchCatalogData() {
        apiService ApiService = RetrofitInstance.getApiService();


        ApiService.getCatalog().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new CatalogoAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                }
            }


            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                // Manejo de errores
            }
        });
    }
}
