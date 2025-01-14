package com.fic.bunnyshopmobiletry5.ui.mario;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fic.bunnyshopmobiletry5.R;
import com.google.gson.Gson;

import java.util.HashMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;


public class MarioFragment extends Fragment {

    private MarioViewModel mViewModel;

    private View rootView;

    public static MarioFragment newInstance() {
        return new MarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mario, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MarioViewModel.class);
        // TODO: Use the ViewModel
//        Object userData = getUserData();
        // Puedes usar userData segun tus necesidades
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

    private void checkUserDataAndNavigate() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String userData = sharedPref.getString("user", null);

        Log.d("User", "UserData: " + userData);

        NavController navController = Navigation.findNavController(requireView());

        if (userData != null) {
            //Si existe, navegar al fragmento principal
//            navController.navigate(R.id.nav_mario);

            showUserDate();
        } else {
            //Si no existe, navegar al login
            navController.navigate(R.id.nav_blank);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Verficar datos del usuario y navegar
        checkUserDataAndNavigate();
    }

    //4. MOSTRAR LOS DATOS DEL USUARIO EN UN FRAGMENTO

    private void showUserDate() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        // Obtener el JSON como String
        String userData = sharedPref.getString("user", null);

        if (userData != null) {
            try {
                // Convertir el String JSON a un JSONObject
                JSONObject userJson = new JSONObject(userData);

                // Mostrar los datos en los TextViews
                TextView usernameTextView = rootView.findViewById(R.id.textView17); // Usar rootView

                // Acceder al dato "name" del JSON
                String username = userJson.getString("name");

                // Mostrar el nombre de usuario
                usernameTextView.setText("Usuario: " + username);

            } catch (Exception e) {
                Log.e("Shared_Preferences", "Error al procesar el JSON", e);
            }
        } else {
            Log.d("Shared_Preferences", "No se encontró el usuario en SharedPreferences.");
        }
    }

}
