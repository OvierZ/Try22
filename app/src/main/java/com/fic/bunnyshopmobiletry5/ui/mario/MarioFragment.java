package com.fic.bunnyshopmobiletry5.ui.mario;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.android.material.imageview.ShapeableImageView;
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
        // Puedes usar userData según tus necesidades
    }

    private Object getUserData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        // Leer el JSON desde SharedPreferences
        String json = sharedPreferences.getString("user", null);

        if (json != null) {
            // Convertir el JSON de nuevo a un objeto
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
        }
        return null; // Retornar null si no existe
    }

    private void checkUserDataAndNavigate() {
        Object userData = getUserData();

        NavController navController = Navigation.findNavController(requireView());

        if (userData != null) {
            // Si existe, navegar al fragmento principal
            navController.navigate(R.id.nav_mario);
        } else {
            // Si no existe, navegar al login
            navController.navigate(R.id.nav_blank);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Simular datos iniciales solo si es necesario
        if (isFirstRun()) {
            saveUserDataToPreferences();
        }

        // Mostrar los datos del usuario
        showUserDate();

        // Cargar y mostrar la imagen de perfil
        loadProfileImage();
    }

    /**
     * Método para guardar datos de usuario en SharedPreferences.
     */
    private void saveUserDataToPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", "Mario");
        editor.putString("profileText", "Hola Yo soy mario");
        editor.putString("address", "21 de Marzo");
        editor.putString("location", "Culiacan");
        editor.putString("phone", "6675144399");
        editor.putString("email", "maritommeza@gmail.com");
        editor.putString("birthday", "15 de mayo");
        editor.putString("gender", "Masculino");

        // URL de ejemplo para la imagen de perfil
        editor.putString("profileImage", "https://example.com/profile.jpg");

        editor.apply();
    }

    /**
     * Método para mostrar los datos guardados en la vista.
     */
    private void showUserDate() {
        TextView usernameTextView = requireView().findViewById(R.id.textView17);
        TextView TextPerfil = requireView().findViewById(R.id.textView18);
        TextView direccionUsuario = requireView().findViewById(R.id.userDirection);
        TextView ubicacionUsuario = requireView().findViewById(R.id.userUbication);
        TextView telefonoUsuario = requireView().findViewById(R.id.userPhone);
        TextView imailUsuario = requireView().findViewById(R.id.userMail);
        TextView nacimientoUsuari = requireView().findViewById(R.id.userBirthday);
        TextView orientacionUsuario = requireView().findViewById(R.id.userSex);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        usernameTextView.setText(sharedPreferences.getString("username", "Default Username"));
        TextPerfil.setText(sharedPreferences.getString("profileText", "Default Profile Text"));
        direccionUsuario.setText(sharedPreferences.getString("address", "Default Address"));
        ubicacionUsuario.setText(sharedPreferences.getString("location", "Default Location"));
        telefonoUsuario.setText(sharedPreferences.getString("phone", "Default Phone"));
        imailUsuario.setText(sharedPreferences.getString("email", "Default Email"));
        nacimientoUsuari.setText(sharedPreferences.getString("birthday", "Default Birthday"));
        orientacionUsuario.setText(sharedPreferences.getString("gender", "Default Gender"));
    }

    /**
     * Método para cargar y mostrar la imagen de perfil.
     */
    private void loadProfileImage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String imagePath = sharedPreferences.getString("profileImage", null);

        ShapeableImageView profileImageView = requireView().findViewById(R.id.imageView4);

        if (imagePath != null) {
            // Si es una URI o URL
            Uri imageUri = Uri.parse(imagePath);
            profileImageView.setImageURI(imageUri);
        } else {
            // Imagen predeterminada
            profileImageView.setImageResource(R.drawable.foto_perfil);
        }
    }

    /**
     * Verifica si es la primera ejecución para guardar datos iniciales.
     */
    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
        }

        return isFirstRun;
    }
}
