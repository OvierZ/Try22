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
                totalEnvioProducto.setText( "Envio: "  );  //totalEnvioProducto.setText( "Envio: " + totalEnvioProducto );
                totalProductoCard.setText("$$$");
                totalEnvioFinal.setText("totalProductoFinal");
                totalGeneral.setText("TotalGeneral:");


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

    /**
     * Método para cargar y mostrar la imagen de perfil.
     */
    private void loadProductImage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String imagePath = sharedPreferences.getString("productImage", null);

        ShapeableImageView productImageView = requireView().findViewById(R.id.productImage1);

        if (imagePath != null) {
            // Si la ruta es una URL remota
            Uri imageUri = Uri.parse(imagePath);
            Glide.with(this)
                    .load(imageUri)
                    .into(productImageView);
        } else {
            // Imagen predeterminada si no hay ruta
            productImageView.setImageResource(R.drawable.bunny);
        }
    }

    /**
     * Función para verificar datos del usuario y navegar a la vista de pago.
     */
    private void checkUserDataAndNavigate() {
        // Recuperar datos del usuario desde SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String userData = sharedPref.getString("user", null);

        Log.d("User", "UserData: " + userData);

        // Obtener el controlador de navegación
        NavController navController = Navigation.findNavController(requireView());

        if (userData != null) {
            // Si existen datos del usuario, navegar al fragmento de pago
            navController.navigate(R.id.pagoFragment);
        } else {
            // Si no existen datos, navegar al fragmento de inicio de sesión
            navController.navigate(R.id.nav_blank);
        }
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


}


