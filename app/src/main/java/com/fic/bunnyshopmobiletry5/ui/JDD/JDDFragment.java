package com.fic.bunnyshopmobiletry5.ui.JDD;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.api.enviroment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JDDFragment extends Fragment {

    private int productId;

    private JDDViewModel mViewModel;

    public static JDDFragment newInstance() {
        return new JDDFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener el argumento (ID del producto)
        if (getArguments() != null) {
            String idProducto = getArguments().getString("id_producto");
            Log.d("Producto", idProducto);
            getArticulo(idProducto);
            // Usa el idProducto aquí
        }

        Log.d("SIN ARGUMENTOS", "ALgo Salio mal");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_j_d_d, container, false);
        Button generateIdButton = rootView.findViewById(R.id.generate_id_button);
        TextView tvNombre = rootView.findViewById(R.id.textView2);


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(JDDViewModel.class);
        // TODO: Use the ViewModel
    }

    public void getArticulo(String id_articulo){

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

    }

}