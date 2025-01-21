package com.fic.bunnyshopmobiletry5;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.api.apiServiceFetchResults;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.databinding.ActivityMainBinding;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.fic.bunnyshopmobiletry5.api.UserService;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "UserPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
       /* binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        //.setAnchorView(R.id.fab).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_prueba1, R.id.nav_blank, R.id.nav_bri, R.id.nav_mario,
                R.id.nav_demeza, R.id.nav_ovier, R.id.nav_CatalogoJDD, R.id.carritoDeComprasFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        menuData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void menuData() {

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        String userData = sharedPreferences.getString("user", null);

        // Obtén la referencia al NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view); // Asegúrate de que este ID sea correcto

        // Obtén la vista del header
        View headerView = navigationView.getHeaderView(0);

        // Accede al TextView dentro del header
        TextView nombreUsuario = headerView.findViewById(R.id.textView);

        if (navigationView != null && userData != null) {

            // Modifica el contenido del TextView

            try {
                // Convertir el String JSON a un JSONObject
                JSONObject userJson = new JSONObject(userData);

                String nombre = userJson.has("name") ? userJson.getString("name") : "";

                nombreUsuario.setText("¡Bienvenido, " + nombre +"!");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        } else {
            nombreUsuario.setText("¡Bienvenido, usuario desconocido");
        }
    }
    private void fetchData() {
        apiService apiService = RetrofitInstance.getApiService();  // Obtén la instancia del servicio API

        // Realiza la solicitud sin parámetros
        Call<ResponseBody> call = apiService.getData();  // Usamos ResponseBody para obtener la respuesta cruda

        // Ejecuta la solicitud de forma asíncrona
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Verifica que la respuesta sea exitosa (código 200)
                if (response.isSuccessful()) {
                    try {
                        // Obtener la respuesta como un String
                        String responseString = response.body().string();

                        // Imprimir la respuesta completa en Logcat (opcional)
                        Log.d("API_RESPONSE", "Respuesta completa: " + responseString);

                        // Asignar la respuesta al TextView
                       // textViewResponse.setText(responseString);

                    } catch (IOException e) {
                        Log.e("API_ERROR", "Error al leer el cuerpo de la respuesta: " + e.getMessage());
                    }
                } else {
                    // Si la respuesta no es exitosa, muestra el error en el TextView
                    Log.e("API_ERROR", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response.toString());
                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }{
                    // Si la respuesta no es exitosa, muestra el error en el TextView
                    Log.e("API_ERROR", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response.toString());
                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }{
                    // Si la respuesta no es exitosa, muestra el error en el TextView
                    Log.e("API_ERROR", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response.toString());
                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("API_ERROR", "Algo malo sucede");
            }

//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // Si la solicitud falla, muestra el error en el TextView
//                Log.e("API_ERROR", "Error en la solicitud: " + t.getMessage());
//                textViewResponse.setText("Error en la solicitud: " + t.getMessage());
//            }
        });
    }

    private void fetchDolares() {
        apiService apiService = RetrofitInstance.getApiService();  // Obtén la instancia del servicio API

        // Realiza la solicitud sin parámetros
        Call<ResponseBody> call = apiService.getEstadoApi();  // Usamos ResponseBody para obtener la respuesta cruda

        // Ejecuta la solicitud de forma asíncrona
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Verifica que la respuesta sea exitosa (código 200)
                if (response.isSuccessful()) {
                    try {
                        // Obtener la respuesta como un String
                        String responseString = response.body().string();

                        // Imprimir la respuesta completa en Logcat (opcional)
                        Log.d("API_RESPONSE", "Respuesta completa: " + responseString);

                        // Asignar la respuesta al TextView
                        // textViewResponse.setText(responseString);

                    } catch (IOException e) {
                        Log.e("API_ERROR", "Error al leer el cuerpo de la respuesta: " + e.getMessage());
                    }
                } else {
                    // Si la respuesta no es exitosa, muestra el error en el TextView
                    Log.e("API_ERROR_dolares", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response.toString());
                    Log.e("API_ERROR_2", "Error en la respuesta" + response);
                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("API_ERROR", "Algo malo sucede");
            }

//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // Si la solicitud falla, muestra el error en el TextView
//                Log.e("API_ERROR", "Error en la solicitud: " + t.getMessage());
//                textViewResponse.setText("Error en la solicitud: " + t.getMessage());
//            }
        });
    }

    private void login() {
        apiService apiService = RetrofitInstance.getApiService();  // Obtén la instancia del servicio API

        // Realiza la solicitud sin parámetros
        Call<ResponseBody> call = apiService.login("admin@duo.com", "123456");  // Usamos ResponseBody para obtener la respuesta cruda

        // Ejecuta la solicitud de forma asíncrona
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Verifica que la respuesta sea exitosa (código 200)
                if (response.isSuccessful()) {
                    try {
                        // Obtener la respuesta como un String
                        String responseString = response.body().string();

                        // Imprimir la respuesta completa en Logcat (opcional)
                        Log.d("API_RESPONSE_lOGIN", "Respuesta Login: " + responseString);

                        // Asignar la respuesta al TextView
                        // textViewResponse.setText(responseString);

                    } catch (IOException e) {
                        Log.e("API_ERROR_LOGIN", "Error al leer el cuerpo de la respuesta: " + e);
                    }
                } else {
                    // Si la respuesta no es exitosa, muestra el error en el TextView
                    Log.e("API_ERROR_LOGIN", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response);
                    Log.e("API_ERROR_LOGIN", "Error en la respuesta" + response);
                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Si la solicitud falla, muestra el error en el TextView
                Log.e("API_ERROR_LOGIN", "Error en la solicitud: " + t);
            }
        });
    }

    /*private void checkUserDataAndNavigate() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String userData = sharedPref.getString("user", null);

        Log.d("User", "UserData: " + userData);

        NavController navController = Navigation.findNavController();

        JSONObject userJson;
        if (userData != null) {
            //Si existe, navegar al fragmento principal
            //navController.navigate(R.id.nav_mario);
            Log.d("UserData", "DATOS: " + userData);

            try {
                // Convertir el String JSON a un JSONObject
                userJson = new JSONObject(userData);
                //obtenerHistorial(userJson.getString("id_user"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        } else {
            //Si no existe, navegar al login
            navController.navigate(R.id.nav_blank);
        }
    }*/

}