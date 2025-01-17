package com.fic.bunnyshopmobiletry5.ui.registro;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fic.bunnyshopmobiletry5.R;
import com.fic.bunnyshopmobiletry5.api.RetrofitInstance;
import com.fic.bunnyshopmobiletry5.api.apiService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroFragment extends Fragment {

    public static RegistroFragment newInstance() {
        return new RegistroFragment();
    }

    private RegistroViewModel mViewModel;

    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_registro, container, false);
        rootView = inflater.inflate(R.layout.fragment_registro, container, false);

        Button btn_register = rootView.findViewById(R.id.boton_registrar);

        btn_register.setOnClickListener(v -> {

            btn_register.setEnabled(false);

            EditText email = rootView.findViewById(R.id.email);
            EditText name = rootView.findViewById(R.id.nombre);
            EditText password = rootView.findViewById(R.id.password);
            EditText passwordRepeat = rootView.findViewById(R.id.password_repeat);
            EditText direccion = rootView.findViewById(R.id.direccion);
            EditText telefono = rootView.findViewById(R.id.telefono);

            if(!password.getText().toString().equals(passwordRepeat.getText().toString())){

                Toast.makeText(rootView.getContext(),"Repita correctamente la contraseña",Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(rootView.getContext())
                        .setTitle("Las contraseñas no coinciden...")
                        .setMessage("Repita las contraseñas correctamente para continuar...")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            // Acción al presionar el botón Aceptar
                        })
                        /*.setNegativeButton("Cancelar", (dialog, which) -> {
                            // Acción al presionar el botón Cancelar
                        })*/
                        .show();
                Log.d("Contraseñas", "Contraseña: " + password + " y contraseña repetida : "+ passwordRepeat);
                return;
            }
            registrarse(
                    name.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    direccion.getText().toString(),
                    telefono.getText().toString()
            );

        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistroViewModel.class);
        // TODO: Use the ViewModel
    }

    private void registrarse(String name, String email, String password, String direccion, String telefono){
        apiService apiService = RetrofitInstance.getApiService();  // Obtén la instancia del servicio API

        // Realiza la solicitud sin parámetros
        Call<ResponseBody> call = apiService.register(name, email, password, direccion, telefono);  // Usamos ResponseBody para obtener la respuesta cruda

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
                        Log.d("API_RESPONSE_REGISTER", "Respuesta REGISTER: " + responseString);

                        Toast.makeText(rootView.getContext(),"Usuario creado con éxito",Toast.LENGTH_LONG).show();

//                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPref.edit();
//                        editor.putString("Registrado", responseString);
//                        editor.apply();

//                        String userData = sharedPref.getString("User", null);
//
//                        Log.d("Shared_Preferences", userData);

//                        wait(3000);
//                        // Navega al fragmento con el ID del producto
//                        Navigation.findNavController(rootView).navigate(R.id.nav_mario);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // Navegar al fragmento después de 3 segundos
                            Navigation.findNavController(rootView).navigate(R.id.nav_blank);
                        }, 3000); // 3000 milisegundos = 3 segundos

                    } catch (IOException e) {
                        Log.e("API_ERROR_REGISTER", "Error al leer el cuerpo de la respuesta: " + e);
                        Toast.makeText(rootView.getContext(),"Ocurrio un error en la respuesta",Toast.LENGTH_SHORT).show();

                        new AlertDialog.Builder(rootView.getContext())
                                .setTitle("Error en el servidor ...")
                                .setMessage("Verifica: RegistroFragment/registrarse")
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
                    Log.e("API_ERROR_REGISTER", "Error en la respuesta: Código " + response.code() + ", Mensaje: " + response);
                    Log.e("API_ERROR_REGISTER", "Error en la respuesta" + response);
                    Toast.makeText(rootView.getContext(),"Falta parametros",Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(rootView.getContext())
                            .setTitle("Error al enviar la petición...")
                            .setMessage("Verifica: BlankFragment/login")
                            .setPositiveButton("Aceptar", (dialog, which) -> {
                                // Acción al presionar el botón Aceptar
                            })
                            /*.setNegativeButton("Cancelar", (dialog, which) -> {
                                // Acción al presionar el botón Cancelar
                            })*/
                            .show();
                    //textViewResponse.setText("Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Si la solicitud falla, muestra el error en el TextView
                Log.e("API_ERROR_REGISTER", "Error en la solicitud: " + t);
                Toast.makeText(rootView.getContext(),"Algo salió mal...",Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(rootView.getContext())
                        .setTitle("La petición falló...")
                        .setMessage("Verifica: RegistroFragment/registrarse")
                        .setPositiveButton("Aceptar", (dialog, which) -> {
                            // Acción al presionar el botón Aceptar
                        })
                        /*.setNegativeButton("Cancelar", (dialog, which) -> {
                            // Acción al presionar el botón Cancelar
                        })*/
                        .show();
            }
        });
    }

}