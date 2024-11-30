package com.fic.bunnyshopmobiletry5.ui.briseyda;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fic.bunnyshopmobiletry5.R;

public class BriseydaFragment extends Fragment {

    private BriseydaViewModel mViewModel;

    public static BriseydaFragment newInstance() {
        return new BriseydaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_briseyda, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BriseydaViewModel.class);
        // TODO: Use the ViewModel
    }

}