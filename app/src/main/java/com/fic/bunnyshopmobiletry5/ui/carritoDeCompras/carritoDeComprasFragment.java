package com.fic.bunnyshopmobiletry5.ui.carritoDeCompras;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import android.widget.Button;
import android.widget.LinearLayout;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.CarritoAdapter;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.HistorialAdapter;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class carritoDeComprasFragment extends Fragment {

    private CarritoDeComprasViewModel mViewModel;
    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private List<Map<String, Object>> productos = new ArrayList<>();


    private View rootView;

    public static carritoDeComprasFragment newInstance() {
        return new carritoDeComprasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_carrito_de_compras, container, false);


        // Inicializa el RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewCompras);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Configura el layout manager


        // Inicializa el adaptador con la lista vacía y enlázalo al RecyclerView
        adapter = new CarritoAdapter(getContext(), productos);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CarritoDeComprasViewModel.class);

        checkUserDataAndNavigate();
        // Guardar datos de prueba en SharedPreferences
        //saveCartData();

        // Mostrar los datos guardados
        //showCartData();
    }

    /**
     * Método para mostrar datos del carrito de compras en los TextView.
     */
    private void showCartData() {
        SharedPreferences sharedPref = requireContext().getSharedPreferences("CartPreferences", Context.MODE_PRIVATE);
        String cartData = sharedPref.getString("cart", null);

        // Obtener el contenedor que contiene los productos
       // LinearLayout cartLayout = rootView.findViewById(R.id.cardLeyoutCarrito); // Suponiendo que todo el contenido del carrito está en un LinearLayout

        // Limpiar la vista antes de mostrar los productos
       // cartLayout.removeAllViews();

        if (cartData != null) {
            try {
                // Convertir el String JSON a un JSONObject
                JSONObject cartJson = new JSONObject(cartData);

                // Obtener referencias de los TextView
                //TextView totalProductoTextView = rootView.findViewById(R.id.totalProductoFinal);
                //TextView nombreProductoCard = rootView.findViewById(R.id.nombreProducto);
                TextView totalEnvioProducto = rootView.findViewById(R.id.totalDeEnvio);
                //TextView totalProductoCard = rootView.findViewById(R.id.precioProductoN);
                //TextView totalEnvioFinal = rootView.findViewById(R.id.totalEnviosFinal);


                // Mostrar los datos en el TextView
                String totalProducto = cartJson.getString("totalProducto");
                //totalProductoTextView.setText("$" + totalProducto);
                //nombreProductoCard.setText("NombreProducto");
                totalEnvioProducto.setText("Envio: " + "20.00");
                //totalProductoCard.setText("$$$");
                //totalEnvioFinal.setText(cartJson.getString("totalProductoFinal"));


                // Configurar el botón de eliminar
                Button btnDeleteProduct = rootView.findViewById(R.id.btnDeleteProduct);
                btnDeleteProduct.setOnClickListener(v -> {
                    // Eliminar el producto del carrito
                    //deleteProductFromCart();
                    // Actualizar la interfaz de usuario
                    showCartData();
                });

            } catch (Exception e) {
                Log.e("CarritoFragment", "Error al procesar el JSON", e);
            }
        } else {
            Log.d("CarritoFragment", "No se encontraron datos del carrito en SharedPreferences.");
        }
    }



    /**
     * Método para guardar datos del carrito de compras en SharedPreferences.
     */
    private void saveCartData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CartPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Crear un objeto JSON con los datos del carrito
        JSONObject cartData = new JSONObject();
        try {
            cartData.put("totalProducto", "199.99"); // Ejemplo: Total de productos
            cartData.put("totalProductoFinal", "500.99"); // Ejemplo: Total final de productos
            cartData.put("nombreProducto", "Zapatos Deportivos"); // Ejemplo: Nombre del producto
            cartData.put("cantidad", 2); // Ejemplo: Cantidad de productos
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Guardar los datos en SharedPreferences como un String JSON
        editor.putString("cart", cartData.toString());
        editor.apply(); // Usa apply() para guardar los cambios de forma asíncrona
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtén la referencia al NavController
        NavController navController = Navigation.findNavController(view);

        // Configura el botón para navegar a pagoFragment
        Button buttonCompra = view.findViewById(R.id.buttonCompra);
        buttonCompra.setOnClickListener(v -> {
            // Muestra un Toast para verificar que el botón está funcionando
            Log.d("CarritoDeCompras", "Botón presionado");
            navController.navigate(R.id.pagoFragment);
        });
    }

    // Método para eliminar el producto del carrito visualmente y en SharedPreferences
    /*private void deleteProductFromCart() {
        // Eliminar el producto de SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CartPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cart"); // Eliminar el carrito almacenado
        editor.apply();

        // Eliminar el LinearLayout que contiene el producto en la vista
        LinearLayout productLayout = rootView.findViewById(R.id.contenedorProductos); // Suponiendo que el LinearLayout del producto tiene este ID
        if (productLayout != null) {
            // Eliminar el producto de la vista
            ((ViewGroup) productLayout.getParent()).removeView(productLayout);
            Log.d("CarritoFragment", "Producto eliminado de la vista.");
        }

        // Volver a mostrar el carrito actualizado
        showCartData();
    }*/

    private void checkUserDataAndNavigate() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String userData = sharedPreferences.getString("user", null);

        Log.d("CarritoFragment", "UserData: " + userData);

        if (userData == null) {
            // Redirige al login si no hay datos del usuario
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_carrito_to_pago);
        } else {
            try {
                JSONObject userJson = new JSONObject(userData);
                String userId = userJson.getString("id_user");
                obtenerCarrito(userId);
            } catch (JSONException e) {
                Log.e("DemezaFragment", "Error al analizar los datos del usuario", e);
            }
        }

    }

    private void obtenerCarrito(String key_user) {
        apiService apiService = RetrofitInstance.getApiService();
        Call<ResponseBody> call = apiService.getCarrito(key_user);

        productos.clear();

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
                                    "imagen", jsonObject.getString("imagen")
                            );

                            productos.add(producto); // Agrega a la lista de productos
                        }

                        // Notifica al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("API_ERROR_CARRITO", "Error procesando la respuesta", e);
                    }
                } else {
                    Log.e("API_ERROR_CARRITO", "Error en la respuesta: " + response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR_CARRITO", "Error en la solicitud", t);
            }
        });

 /*       RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewDemeza);
        TextView emptyMessage = rootView.findViewById(R.id.textViewEmptyMessage);
        Integer productosCantidad = 0;

        if (productosCantidad > 1) {
            recyclerView.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);
        }*/

        TextView totalGeneral = rootView.findViewById(R.id.totalGeneral);

        totalGeneral.setText("TOTAL : $" + adapter.getTotal());
    }

    private void getTotal(){
        adapter.getTotal();
    }


}