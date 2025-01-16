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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fic.bunnyshopmobiletry5.R;

public class DemezaFragment extends Fragment {

    private DemezaViewModel mViewModel;

    public static DemezaFragment newInstance() {
        return new DemezaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_demeza, container, false);
        //return inflater.inflate(R.layout.fragment_demeza, container, false);

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

        if (userData != null) {
            //Si existe, navegar al fragmento principal
//            navController.navigate(R.id.nav_mario);

            //fetchCatalogData();
            //showUserDate();
        } else {
            //Si no existe, navegar al login
            navController.navigate(R.id.nav_blank);
        }
    }


}