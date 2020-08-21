package com.modernedutech.LoginSet.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.modernedutech.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgFragment extends Fragment {

    public ForgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return  inflater.inflate(R.layout.fragment_forg, container, false);
    }
    public void forget(View view) {



    }
}
