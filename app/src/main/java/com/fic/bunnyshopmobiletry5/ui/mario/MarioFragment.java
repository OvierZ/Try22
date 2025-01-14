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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fic.bunnyshopmobiletry5.R;
import com.google.gson.Gson;

import java.util.HashMap;
import com.google.gson.reflect.TypeToken;



public class MarioFragment extends Fragment {

    private MarioViewModel mViewModel;

    public static MarioFragment newInstance() {
        return new MarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mario, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MarioViewModel.class);
        // TODO: Use the ViewModel
        Object userData = getUserData();
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
        Object userData = getUserData();

        NavController navController = Navigation.findNavController(requireView());

        if (userData != null) {
            //Si existe, navegar al fragmento principal
            //navController.navigate(R.id.nav_mario);
        } else {
            //Si no existe, navegar al login
            //navController.navigate(R.id.nav_mario);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Verificar datos del usuario y navegar
        checkUserDataAndNavigate();

        // Mostrar los datos del usuario
        showUserDate();
    }


    //4. MOSTRAR LOS DATOS DEL USUARIO EN UN FRAGMENTO

    private void showUserDate() {
        TextView usernameTextView = requireView().findViewById(R.id.textView17);

        // Establecer el texto "Mario" directamente
        usernameTextView.setText("Mario");
    }



}