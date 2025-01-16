package com.fic.bunnyshopmobiletry5.ui.briseyda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.WishListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BriseydaFragment extends Fragment {

    private RecyclerView recyclerView;
    private WishListAdapter adapter;

    private ImageView productImage;
    private TextView productTitle;
    private TextView productDescription;
    private ImageButton changeInfoBtn; // Cambiado a ImageButton

    private int currentIndex = 0;

    // Lista de productos
    private final List<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar la vista del fragmento
        View root = inflater.inflate(R.layout.fragment_briseyda, container, false);

        // Inicializar vistas
        productImage = root.findViewById(R.id.productImage1);
        productTitle = root.findViewById(R.id.productTitle1);
        productDescription = root.findViewById(R.id.productDescription1);
        changeInfoBtn = root.findViewById(R.id.changeInfoBtn); // Corregido el tipo

        // Inicializar productos
//        initializeProducts();


//        fetchCatalogData();
        // Configurar botón para cambiar información
        changeInfoBtn.setOnClickListener(v -> updateProductInfo());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ahora es seguro llamar a checkUserDataAndNavigate() porque la vista ya está creada
        checkUserDataAndNavigate();
    }


    /**
     * Inicializa la lista de productos
     */
    private void initializeProducts() {
        products.add(new Product(R.drawable._producto, "Producto 1", "Descripción breve del producto 1"));
        products.add(new Product(R.drawable._producto2, "Producto 2", "Descripción breve del producto 2"));

        // Mostrar el primer producto
        if (!products.isEmpty()) {
            setProductInfo(products.get(0));
        }
    }

    /**
     * Actualiza la información al siguiente producto
     */
    private void updateProductInfo() {
        if (products.isEmpty()) return; // Validar que la lista no esté vacía

        // Cambiar al siguiente producto
        currentIndex = (currentIndex + 1) % products.size();
        setProductInfo(products.get(currentIndex));
    }

    /**
     * Establece la información del producto en las vistas
     */
    private void setProductInfo(Product product) {
        productImage.setImageResource(product.getImageRes());
        productTitle.setText(product.getTitle());
        productDescription.setText(product.getDescription());
    }

    /**
     * Clase interna para representar un producto
     */
    private static class Product {
        private final int imageRes;
        private final String title;
        private final String description;

        public Product(int imageRes, String title, String description) {
            this.imageRes = imageRes;
            this.title = title;
            this.description = description;
        }

        public int getImageRes() {
            return imageRes;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
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

    private void checkUserDataAndNavigate() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String userData = sharedPref.getString("user", null);

        Log.d("User", "UserData: " + userData);

        NavController navController = Navigation.findNavController(requireView());

        if (userData != null) {
            //Si existe, navegar al fragmento principal
//            navController.navigate(R.id.nav_mario);

            fetchCatalogData();
            //showUserDate();
        } else {
            //Si no existe, navegar al login
            navController.navigate(R.id.nav_blank);
        }
    }

    private Object getUserData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        //Leer el JSON desde SharedPreferences
        String json = sharedPreferences.getString("user", null);

        if (json != null) {
            //Convertir el JSON de nuevo a un objeto
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<HashMap<String,
                    Object>>() {
            }.getType());
        }
        return null; //Retornar null si no exite
    }
}
