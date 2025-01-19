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


import com.fic.bunnyshopmobiletry5.R;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONObject;

public class carritoDeComprasFragment extends Fragment {

    private CarritoDeComprasViewModel mViewModel;
    private View rootView;

    public static carritoDeComprasFragment newInstance() {
        return new carritoDeComprasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_carrito_de_compras, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CarritoDeComprasViewModel.class);

        // Guardar datos de prueba en SharedPreferences
        saveCartData();

        // Mostrar los datos guardados
        showCartData();
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
                TextView totalProductoTextView = rootView.findViewById(R.id.totalProductoFinal);
                TextView nombreProductoCard = rootView.findViewById(R.id.nombreProducto);
                TextView totalEnvioProducto = rootView.findViewById(R.id.totalDeEnvio);
                TextView totalProductoCard = rootView.findViewById(R.id.precioProductoN);
                TextView totalEnvioFinal = rootView.findViewById(R.id.totalEnviosFinal);
                TextView totalGeneral = rootView.findViewById(R.id.totalGeneral);

                // Mostrar los datos en el TextView
                String totalProducto = cartJson.getString("totalProducto");
                totalProductoTextView.setText("$" + totalProducto);
                nombreProductoCard.setText("NombreProducto");
                totalEnvioProducto.setText("Envio: " + "20.00");
                totalProductoCard.setText("$$$");
                totalEnvioFinal.setText(cartJson.getString("totalProductoFinal"));
                totalGeneral.setText("TotalGeneral: " + "520.00");

                // Configurar el botón de eliminar
                Button btnDeleteProduct = rootView.findViewById(R.id.btnDeleteProduct);
                btnDeleteProduct.setOnClickListener(v -> {
                    // Eliminar el producto del carrito
                    deleteProductFromCart();
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
    private void deleteProductFromCart() {
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
}

}