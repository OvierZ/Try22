package com.fic.bunnyshopmobiletry5.ui.briseyda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.WishListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BriseydaFragment extends Fragment {

    private RecyclerView recyclerView;
    private WishListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_briseyda, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewCatalog);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Crear el listener para el clic en el botón
        WishListAdapter.OnItemClickListener listener = new WishListAdapter.OnItemClickListener() {
            @Override
            public void onAddToWishlistClick(Map<String, Object> item) {
                // Aquí llamas al método para agregar el producto a la wishlist
                addToWishlist(item);
            }
        };

        // Inicializamos el adaptador con el listener
        adapter = new WishListAdapter(new ArrayList<>(), listener);
        recyclerView.setAdapter(adapter);

        fetchWishlistData();  // Cargar la wishlist

        return root;
    }

    private void fetchWishlistData() {
        apiService apiService = RetrofitInstance.getApiService();

        Call<List<Map<String, Object>>> call = apiService.getWishlist();

        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> itemList = response.body();
                    adapter.updateData(itemList);  // Actualizar los datos en el adaptador
                } else {
                    Log.e("API_ERROR_CATALOG", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.e("API_ERROR_CATALOG", "Error en la solicitud", t);
            }
        });
    }

    // Método para agregar un producto a la wishlist
    public void addToWishlist(Map<String, Object> item) {
        String idArticulo = (String) item.get("id_articulo");

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String keyUser = sharedPref.getString("user_key", null);  // Ajusta el nombre del key de acuerdo a tu implementación

        if (keyUser != null) {
            apiService apiService = RetrofitInstance.getApiService();
            apiService.addToWishlist(idArticulo, keyUser).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("Wishlist", "Producto agregado a la wishlist.");
                    } else {
                        Log.e("Wishlist", "Error al agregar el producto.");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Wishlist", "Error en la solicitud: " + t.getMessage());
                }
            });
        }
    }
}





