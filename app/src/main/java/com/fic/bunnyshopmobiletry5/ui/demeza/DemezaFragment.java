package com.fic.bunnyshopmobiletry5.ui.demeza;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        return inflater.inflate(R.layout.fragment_demeza, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DemezaViewModel.class);
        // TODO: Use the ViewModel
    }

}