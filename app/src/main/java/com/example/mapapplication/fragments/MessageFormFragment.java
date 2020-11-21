package com.example.mapapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mapapplication.R;

public class MessageFormFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public View view;
    public EditText is_adi_edittext;
    public EditText is_aciklama_edittext;
    public Button kaydet_button;
    public Button iptal_button;
    public Button delete_button;

    public MessageFormFragment() {
        // Required empty public constructor
    }

    public MessageFormFragment(String s, String ss) {
        // Required empty public constructor
        this.mParam1 = s;
        this.mParam2 = ss;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_place_details, container, false);

        is_adi_edittext = view.getRootView().findViewById(R.id.is_adi_edittext);
        is_adi_edittext.setText(mParam1);

        is_aciklama_edittext = view.getRootView().findViewById(R.id.is_aciklama_edittext);
        is_aciklama_edittext.setText(mParam2);

        kaydet_button = view.getRootView().findViewById(R.id.kaydet_button);
        iptal_button = view.getRootView().findViewById(R.id.iptal_button);
        delete_button = view.getRootView().findViewById(R.id.delete_button);

        if (mParam1 == null && mParam2 == null){
            kaydet_button.setVisibility(View.VISIBLE);
            iptal_button.setVisibility(View.VISIBLE);
            delete_button.setVisibility(View.GONE);

            is_adi_edittext.setEnabled(true);
            is_aciklama_edittext.setEnabled(true);
        } else {
            kaydet_button.setVisibility(View.GONE);
            delete_button.setVisibility(View.VISIBLE);
            iptal_button.setVisibility(View.VISIBLE);

            is_adi_edittext.setEnabled(false);
            is_aciklama_edittext.setEnabled(false);
        }
        return view;
    }
}