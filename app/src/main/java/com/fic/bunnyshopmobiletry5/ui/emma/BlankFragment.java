package com.fic.bunnyshopmobiletry5.ui.emma;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.apiService;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.ui.dialogLoading.dialogLoading;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlankFragment extends Fragment {

    private BlankViewModel mViewModel;

    private View rootView;
    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento y guarda la vista raíz
        rootView = inflater.inflate(R.layout.fragment_blank, container, false);

//        Toast.makeText(rootView.getContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

        Button btnLogin = rootView.findViewById(R.id.button_login);

        ImageView macacoGif = rootView.findViewById(R.id.macacoGif);
        // Cargar un GIF con Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.macaco_preocupado) // Cambia a la referencia de tu GIF
                .into(macacoGif);


        btnLogin.setOnClickListener(v -> {
            EditText correoControl = rootView.findViewById(R.id.correoControl);
            EditText passwordControl = rootView.findViewById(R.id.passwordControl);

            login(correoControl.getText().toString(), passwordControl.getText().toString());
        });

        TextView btn_register = rootView.findViewById(R.id.textView22);

        btn_register.setText(Html.fromHtml("<u>¿Aún no tienes cuenta?, Haz click Aquí</u>"));


        btn_register.setOnClickListener(v -> {
            Navigation.findNavController(rootView).navigate(R.id.nav_RegistroFragment);
        });

/*        dialogLoading loadingDialog = new dialogLoading();
        loadingDialog.show(getParentFragmentManager(), "LoadingDialog");

        // Para cerrarlo posteriormente
        loadingDialog.dismiss();*/
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BlankViewModel.class);
        // TODO: Use the ViewModel
        checkUserDataAndNavigate();
    }

    private void login(String email, String password) {

        //dialogLoading loadingDialog = new dialogLoading();
        //loadingDialog.show(getParentFragmentManager(), "LoadingDialog");

        apiService apiService = RetrofitInstance.getApiService();  // Obtén la instancia del servicio API

        // Realiza la solicitud sin parámetros
        Call<ResponseBody> call = apiService.login(email, password);  // Usamos ResponseBody para obtener la respuesta cruda

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

                        Toast.makeText(rootView.getContext(),"Acceso concedido",Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("user", responseString);
                        editor.apply();

                        String userData = sharedPref.getString("user", null);

                        Log.d("Shared_Preferences", userData);

                        // Navega al fragmento con el ID del producto
                        //Navigation.findNavController(rootView).navigate(R.id.nav_mario);

                        Navigation.findNavController(rootView).navigate(R.id.action_login_to_perfil);


                    } catch (IOException e) {
                        Log.e("API_ERROR_LOGIN", "Error al leer el cuerpo de la respuesta: " + e);
                        Toast.makeText(rootView.getContext(),"Ocurrio un error en la respuesta",Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(rootView.getContext())
                                .setTitle("Error en el servidor")
                                .setMessage("Verifica: BlankFragment/login")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    // Acción al presionar el botón Aceptar
                                })
                                /*.setNegativeButton("Cancelar", (dialog, which) -> {
                                    // Acción al presionar el botón Cancelar
                                })*/
                                .show();
                    }
                } else {
                    // Si la respuesta no es exitosa, muestra el error en el TextView
                    Log.e("API_ERROR_LOGIN", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response);
                    Log.e("API_ERROR_LOGIN", "Error en la respuesta" + response);
                    new AlertDialog.Builder(rootView.getContext())
                            .setTitle("Acceso denegado")
                            .setMessage("Tus credenciales de acceso son incorrectas, verificalas e intenta de nuevo...")
                            .setPositiveButton("Aceptar", (dialog, which) -> {
                                // Acción al presionar el botón Aceptar
                            })
                            /*.setNegativeButton("Cancelar", (dialog, which) -> {
                                // Acción al presionar el botón Cancelar
                            })*/
                            .show();
                    Toast.makeText(rootView.getContext(),"Acceso denegado",Toast.LENGTH_SHORT).show();

                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Si la solicitud falla, muestra el error en el TextView
                Log.e("API_ERROR_LOGIN", "Error en la solicitud: " + t);
                Toast.makeText(rootView.getContext(),"Algo salió mal...",Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(rootView.getContext())
                        .setTitle("La petición falló...")
                        .setMessage("Verifica: BlankFragment/login")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            // Acción al presionar el botón Aceptar
                        })
                        /*.setNegativeButton("Cancelar", (dialog, which) -> {
                            // Acción al presionar el botón Cancelar
                        })*/
                        .show();
            }
        });

        // Para cerrarlo posteriormente
        //loadingDialog.dismiss();
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
                //obtenerHistorial(userJson.getString("id_user"));
                navController.navigate(R.id.action_login_to_perfil);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }

}