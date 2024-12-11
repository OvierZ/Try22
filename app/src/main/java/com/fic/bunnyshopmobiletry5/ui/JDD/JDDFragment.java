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

import com.fic.bunnyshopmobiletry5.R;

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
            productId = getArguments().getInt("PRODUCT_ID");
            Log.d("ID_PRODUCTO", String.valueOf(productId));
            return;
        }

        Log.d("SIN ARGUMENTOS", "ALgo Salio mal");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_j_d_d, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(JDDViewModel.class);
        // TODO: Use the ViewModel
    }

}