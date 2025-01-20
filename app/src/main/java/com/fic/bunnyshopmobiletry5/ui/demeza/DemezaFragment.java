package com.fic.bunnyshopmobiletry5.ui.demeza;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.TextView;

import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.CatalogoAdapter;
import com.fic.bunnyshopmobiletry5.catalogoAdapter.HistorialAdapter;

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

public class DemezaFragment extends Fragment {

    private DemezaViewModel mViewModel;

    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private List<Map<String, Object>> productos = new ArrayList<>(); // Lista de productos

    private View rootView;
    public static DemezaFragment newInstance() {
        return new DemezaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_demeza, container, false);
        //return inflater.inflate(R.layout.fragment_demeza, container, false);

        // Inicializa el RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewDemeza);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Configura el layout manager


        // Inicializa el adaptador con la lista vacía y enlázalo al RecyclerView
        adapter = new HistorialAdapter(getContext(), productos);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ahora es seguro llamar a checkUserDataAndNavigate() porque la vista ya está creada
        checkUserDataAndNavigate();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DemezaViewModel.class);
        // TODO: Use the ViewModel
    }

    private void checkUserDataAndNavigate() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String userData = sharedPref.getString("user", null);

        Log.d("User", "UserData: " + userData);

        NavController navController = Navigation.findNavController(requireView());

        JSONObject userJson;
        if (userData != null) {
            //Si existe, navegar al fragmento principal
            //navController.navigate(R.id.nav_mario);
            Log.d("UserData", "DATOS: " + userData);

            try {
                // Convertir el String JSON a un JSONObject
                userJson = new JSONObject(userData);
                obtenerHistorial(userJson.getString("id_user"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        } else {
            //Si no existe, navegar al login
            navController.navigate(R.id.action_historial_to_login);
        }
    }


    private void obtenerHistorial(String key_user) {
        apiService apiService = RetrofitInstance.getApiService();
        Call<ResponseBody> call = apiService.getCompras(key_user);

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

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewDemeza);
        TextView emptyMessage = rootView.findViewById(R.id.textViewEmptyMessage);
        Integer productosCantidad = 0;

        if (productosCantidad > 1) {
            recyclerView.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);
        }
    }
}

/*LAYOUT



        <!--LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Historial de compras "
                android:textSize="24sp"
                android:fontFamily="sans-serif-medium"
                android:gravity="center"
                android:layout_marginBottom="16dp"/>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <!-Producto 1 -
                <androidx.cardview.widget.CardView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="4dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="horizontal"
                        android:padding="8dp">

                        <!-- Imagen del producto 1--

                        <!-- Contenido del producto 1 --
                        <ImageView
                            android:id="@+id/productImage1"
                            android:layout_width="96dp"
                            android:layout_height="117dp"
                            android:src="@drawable/producto1" />

                        <LinearLayout
                            android:layout_width="0dp"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:orientation="vertical"
                            android:paddingStart="16dp">

                            <TextView
                                android:id="@+id/productTitle1"
                                android:layout_width="match_parent"
                                android:layout_height="wrap_content"
                                android:fontFamily="sans-serif-medium"
                                android:text="iPhone 16"
                                android:textSize="16sp" />

                            <TextView
                                android:id="@+id/productDescription1"
                                android:layout_width="match_parent"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="4dp"
                                android:text="El iPhone 16 color rosa que cuenta con 128 GB de almacenamiento"
                                android:textSize="14sp" />
                            <Button
                                android:id="@+id/detallesBtn1"
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_gravity="center_horizontal"
                                android:layout_marginTop="16dp"
                                android:backgroundTint="@color/teal_700"
                                android:text="ver detalles"
                                android:textSize="14sp"
                                android:textColor="@android:color/white" />

                        </LinearLayout>
                    </LinearLayout>
                </androidx.cardview.widget.CardView>

                <!-- Producto 2 --

                <androidx.cardview.widget.CardView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="4dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="horizontal"
                        android:padding="8dp">

                        <!-- Imagen del producto 2 --

                        <!-- Contenido del producto 2--

                        <ImageView
                            android:id="@+id/productImage2"
                            android:layout_width="96dp"
                            android:layout_height="117dp"
                            android:src="@drawable/produc2" />

                        <LinearLayout
                            android:layout_width="16dp"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:orientation="vertical"
                            android:paddingStart="16dp">

                            <TextView
                                android:id="@+id/productTitle2"
                                android:layout_width="match_parent"
                                android:layout_height="wrap_content"
                                android:fontFamily="sans-serif-medium"
                                android:text="iPhone 16 de 128 GB "
                                android:textSize="16sp" />

                            <TextView
                                android:id="@+id/productDescription2"
                                android:layout_width="match_parent"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="4dp"
                                android:text="iphone Ultramarino es un diseño elegante con rendimiento avanzado"
                                android:textSize="14sp" />
                            <Button
                                android:id="@+id/detallesBtn2"
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="8dp"
                                android:layout_gravity="center_horizontal"
                                android:backgroundTint="@color/teal_700"
                                android:text="ver detalles"
                                android:textSize="14sp"
                                android:textColor="@android:color/white" />

                             </LinearLayout>
                      </LinearLayout>
                </androidx.cardview.widget.CardView>

        <!-- Producto 3 --
        <androidx.cardview.widget.CardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="8dp"
            app:cardCornerRadius="8dp"
            app:cardElevation="4dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:padding="8dp">

            <!-- Imagen del producto 3 --

            <!-- Contenido del producto 3--


            <ImageView
                android:id="@+id/productCard3"
                android:layout_width="96dp"
                android:layout_height="117dp"
                android:src="@drawable/producto3" />

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical"
                android:paddingStart="16dp">

                <TextView
                    android:id="@+id/productTitle3"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:fontFamily="sans-serif-medium"
                    android:text="Videojuego"
                    android:textSize="16sp" />

                <TextView
                    android:id="@+id/productDescription3"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:text="Es un juego electrónico interactivo diseñado para el entretenimiento"
                    android:textSize="14sp" />
                <Button
                    android:id="@+id/detallesBtn3"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="8dp"
                    android:layout_gravity="center_horizontal"
                    android:backgroundTint="@color/teal_700"
                    android:text="ver detalles"
                    android:textSize="14sp"
                    android:textColor="@android:color/white" />

            </LinearLayout>
         </LinearLayout>
        </androidx.cardview.widget.CardView>
                <!-- Producto 4 --
                <androidx.cardview.widget.CardView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_margin="8dp"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="4dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="horizontal"
                        android:padding="8dp">

                        <!-- Imagen del producto 4 --

                        <!-- Contenido del producto 4--

                        <ImageView
                            android:id="@+id/productCard4_buttonDetails"
                            android:layout_width="96dp"
                            android:layout_height="117dp"
                            android:src="@drawable/img_1" />

                        <LinearLayout
                            android:layout_width="0dp"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:orientation="vertical"
                            android:paddingStart="16dp">

                            <TextView
                                android:id="@+id/productTitle4"
                                android:layout_width="match_parent"
                                android:layout_height="wrap_content"
                                android:fontFamily="sans-serif-medium"
                                android:text="Apple iPhone 12 Mini"
                                android:textSize="16sp" />

                            <TextView
                                android:id="@+id/productCard4"
                                android:layout_width="match_parent"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="4dp"
                                android:text="iPhone color morado con potencia compacta,pantalla Super Retina XDR de 5.4"
                                android:textSize="14sp" />
                      <Button
                          android:id="@+id/detallesBtn4"
                          android:layout_width="wrap_content"
                          android:layout_height="wrap_content"
                          android:layout_gravity="center_horizontal"
                          android:layout_marginTop="0dp"
                          android:backgroundTint="@color/teal_700"
                          android:text="ver detalles"
                          android:textSize="14sp"
                          android:textColor="@android:color/white" />


                        </LinearLayout>
                    </LinearLayout>
                </androidx.cardview.widget.CardView>

            </LinearLayout>
            <!-- Línea horizontal --
            <View
                android:id="@+id/lineFoot"
                android:layout_width="match_parent"
                android:layout_height="2dp"
                android:layout_marginTop="8dp"
                android:background="@android:color/darker_gray" />
        </LinearLayout-->
 */