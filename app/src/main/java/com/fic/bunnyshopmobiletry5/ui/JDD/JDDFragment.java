package com.fic.bunnyshopmobiletry5.ui.JDD;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.UserService;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.api.enviroment;
import com.fic.bunnyshopmobiletry5.ui.dialogLoading.dialogLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JDDFragment extends Fragment {

    private String productId;

    private JDDViewModel mViewModel;

    private View rootView;

    public static JDDFragment newInstance() {
        return new JDDFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener el argumento (ID del producto)
        if (getArguments() != null) {
            productId = getArguments().getString("id_producto");
            Log.d("Producto", productId);
            getArticulo(productId);
            // Usa el idProducto aquí
        }

        Log.d("SIN ARGUMENTOS", "ALgo Salio mal");


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        Button btn_comprar = rootView.findViewById(R.id.button_comprar);
//
//        btn_comprar.setOnClickListener(v->{
//            Bundle bundle = new Bundle();
//            bundle.putString("id_producto", productId.toString()); // Pasa el ID como String
//
//            // Obtén la vista desde el ViewHolder
//            View view = holder.itemView;
//
//            // Navega al fragmento con el ID del producto
//            Navigation.findNavController(view).navigate(R.id.nav_jdd, bundle);
//        });
        rootView = inflater.inflate(R.layout.fragment_j_d_d, container, false);
        Button generateIdButton = rootView.findViewById(R.id.generate_id_button);

        // Si no se recibió ID, muestra el botón
        if (getArguments() == null || getArguments().getString("id_producto") == null) {
            ((View) generateIdButton).setVisibility(View.VISIBLE);
            ((View) generateIdButton).setOnClickListener(v -> {
                // Generar ID aleatorio entre 1 y 24
                int randomId = (int) (Math.random() * 24) + 1;
                Log.d("RANDOM_ID", "ID generado: " + randomId);

                // Llamar al método para obtener el artículo
                getArticulo(String.valueOf(randomId));
            });
        }
        return rootView;
    }

    private String getUserIdFromPreferences() {
        // Obtener las SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        // Obtener los datos del usuario como String
        String userData = sharedPref.getString("user", null);
        Log.d("User_Data", "Datos completos: " + userData);
        // Procesar el JSON para obtener el ID del usuario
        if (userData != null) {
            try {
                JSONObject userJson = new JSONObject(userData);
                return userJson.getString("id_user");
            } catch (JSONException e) {
                Log.e("JSON_ERROR", "Error al procesar el JSON: " + e);
            }
        } else {
            Log.d("Shared_Preferences", "No se encontraron datos del usuario.");
        }
        // Retornar null si no hay datos o ocurrió un error
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_comprar = rootView.findViewById(R.id.button_comprar);
        Button btn_agregarCarrito = rootView.findViewById(R.id.button2);
        Button btn_wishlist = rootView.findViewById(R.id.button3);

        apiService api = RetrofitInstance.getApiService();

        String finalUserId = getUserIdFromPreferences();
        boolean isUserLoggedIn = (finalUserId != null && !finalUserId.isEmpty());

        //Comprar
        btn_comprar.setOnClickListener(v -> {
            if (!isUserLoggedIn) {
                Toast.makeText(getContext(), "Inicie sesión primeramente", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("id_producto", productId); // Pasa el ID como String

            // Obtén la vista desde el ViewHolder
            // View view = holder.itemView;

            // Navega al fragmento con el ID del producto
            Navigation.findNavController(rootView).navigate(R.id.action_detalles_to_comprar, bundle);
        });

        //WishList
        //Verificar si el producto esta en la wishlist
        api.wishlistCheck(productId, finalUserId).enqueue(new Callback<ResponseBody>() {
            //Conecto
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Respondio
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseString);
                        boolean isInWishlist = jsonResponse.getBoolean("Message");
                        btn_wishlist.setText(isInWishlist ? "Quitar De La Wishlist" : "Agregar A La Wishlist");
                    } catch (Exception e) {
                        Log.e("WISHLIST_CHECK", "Error procesando la respuesta", e);
                    }
                    //No Respondio
                } else {
                    Log.e("WISHLIST_CHECK", "Error al verificar wishlist: " + response.code());
                }
            }

            //No Conecto
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("WISHLIST_CHECK", "Error de conexión", t);
            }
        });

        // Configurar el listener para el botón wishlist
        btn_wishlist.setOnClickListener(v -> {
            if (!isUserLoggedIn) {
                Toast.makeText(getContext(), "Inicie sesión primeramente", Toast.LENGTH_SHORT).show();
                return;
            }
            String action = btn_wishlist.getText().toString();
            if (action.equals("Agregar A La Wishlist")) {
                //Agregar a la wishlist
                api.wishlistAgg(productId, finalUserId).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            btn_wishlist.setText("Quitar De La Wishlist");
                            Log.d("WISHLIST_ACTION", "Producto agregado a la wishlist");
                        }else{
                            Log.e("WISHLIST_ACTION", "Error al agregar: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("WISHLIST_ACTION", "Error de conexión", t);
                    }
                });
            }else if (action.equals("Quitar De La Wishlist")){
                //Quitar De La Wishlist
                api.wishlistDelete(productId, finalUserId).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            btn_wishlist.setText("Agregar A La Wishlist");
                            Log.d("WISHLIST_ACTION", "Producto eliminado de la wishlist");
                        }else {
                            Log.e("WISHLIST_ACTION", "Error al eliminar: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("WISHLIST_ACTION", "Error de conexión", t);
                    }
                });
            }
        });

        //Carrito
        btn_agregarCarrito.setOnClickListener(v -> {
            if (!isUserLoggedIn) {
                Toast.makeText(getContext(), "Inicie sesión primeramente", Toast.LENGTH_SHORT).show();
                return;
            }
            api.agregarCarrito(productId, finalUserId).enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                    if(response.isSuccessful()){
                        Toast.makeText(getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                        Log.d("CARRITO", "Producto agregado correctamente: " + productId);
                    }else{
                        Toast.makeText(getContext(), "Error al agregar al carrito", Toast.LENGTH_SHORT).show();
                        Log.e("CARRITO", "Error: " + response.message());
                    }
                }
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    Log.e("CARRITO", "Error de conexión", t);
                }
            });
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(JDDViewModel.class);
        // TODO: Use the ViewModel
    }

    public void getArticulo(String id_articulo){

        dialogLoading loadingDialog = new dialogLoading();
        loadingDialog.show(getParentFragmentManager(), "LoadingDialog");

        apiService apiService = RetrofitInstance.getApiService();
        Call<ResponseBody> call = apiService.getArticulo(id_articulo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseString);
                        Log.d("JDD ~ Producto", jsonArray.toString());
                        // Procesar los datos

                        // Ejemplo: Asume que la respuesta es un array con objetos que contienen "nombre" y "descripcion"
                        JSONObject producto = jsonArray.getJSONObject(0); // Acceder al primer producto
                        String nombre = producto.getString("nombre");
                        String descripcion = producto.getString("descripcion");
                        String urlImagen = enviroment.BASE_URL_STORAGE + "productos/" + producto.getString("imagen");
                        String precio = producto.getString("precio");

                        // Actualizar vistas
                        if (getView() != null) {

                            TextView tvNombre = getView().findViewById(R.id.textView2);
                            TextView tvDescripcion = getView().findViewById(R.id.textView3);
                            TextView tvPrecio = getView().findViewById(R.id.textView5);
                            ImageView imageView = (ImageView) getView().findViewById(R.id.imageView2);

                            Glide.with(getContext())
                                    .load(urlImagen)
                                    .placeholder(R.drawable.macaco_preocupado) // Imagen de carga
                                    .error(R.drawable.bunny) // Imagen en caso de error
                                    .into(imageView);


                            tvNombre.setText(nombre);
                            tvDescripcion.setText(descripcion);
                            tvPrecio.setText(precio);
                        }

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

        
        loadingDialog.dismiss();
    }
}