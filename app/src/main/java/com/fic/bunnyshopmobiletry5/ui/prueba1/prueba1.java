package com.fic.bunnyshopmobiletry5.ui.prueba1;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fic.bunnyshopmobiletry5.R;

public class prueba1 extends Fragment {

    private Prueba1ViewModel mViewModel;

    public static prueba1 newInstance() {
        return new prueba1();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prueba1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Prueba1ViewModel.class);
        // TODO: Use the ViewModel
    }
}