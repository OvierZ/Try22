package com.fic.bunnyshopmobiletry5.ui.briseyda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.WishListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BriseydaFragment extends Fragment {

    private RecyclerView recyclerViewWishlist;
    private WishListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_briseyda, container, false);

        // Inicialización del RecyclerView
        recyclerViewWishlist = root.findViewById(R.id.recyclerViewCatalog);
        recyclerViewWishlist.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicialización del adaptador con una lista vacía
        adapter = new WishListAdapter(requireContext(), new ArrayList<>());
        recyclerViewWishlist.setAdapter(adapter);

        // Llamada para obtener los datos de la wishlist
        fetchWishlistData();

        return root;
    }

    private void fetchWishlistData() {
        int keyUserInt = obtenerKeyUser();

        if (keyUserInt == -1) {
            Log.w("BriseydaFragment", "key_user no encontrado en SharedPreferences.");
            Toast.makeText(requireContext(), "Inicia sesión para ver tu Wishlist", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir keyUserInt a String
        String keyUserString = String.valueOf(keyUserInt);

        apiService apiService = RetrofitInstance.getApiService();
        Call<List<Map<String, Object>>> call = apiService.getWishlist(keyUserString);

        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful()) {
                    List<Map<String, Object>> wishlist = response.body();

                    // Verificar los datos recibidos en la respuesta
                    Log.d("BriseydaFragment", "Wishlist: " + wishlist.toString());

                    if (wishlist != null && !wishlist.isEmpty()) {
                        Log.d("BriseydaFragment", "Wishlist data received: " + wishlist.size() + " items.");
                        adapter.updateData(wishlist); // Actualizar el adaptador con los datos obtenidos
                    } else {
                        Log.e("BriseydaFragment", "Wishlist data is empty.");
                        Toast.makeText(requireContext(), "No hay productos en tu Wishlist.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("BriseydaFragment", "Error in response: " + response.code());
                    Toast.makeText(requireContext(), "Error al cargar la Wishlist.", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Log.e("BriseydaFragment", "Error al realizar la llamada a la API: " + t.getMessage());
                Toast.makeText(requireContext(), "Algo salió mal al cargar la Wishlist.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerKeyUser() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String keyUserString = sharedPreferences.getString("user", null); // Recupera el valor almacenado

        if (keyUserString != null) {
            try {
                // Parsear el valor recuperado de SharedPreferences
                JSONObject userJson = new JSONObject(keyUserString);
                return userJson.getInt("id_user"); // Devuelve el ID del usuario
            } catch (JSONException e) {
                Log.e("BriseydaFragment", "Error al parsear JSON: " + keyUserString, e);
                return -1; // Devuelve -1 si hay error
            }
        } else {
            return -1; // Devuelve -1 si no se encontró el key_user
        }
    }
}

















