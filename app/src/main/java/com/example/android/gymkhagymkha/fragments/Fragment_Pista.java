package com.example.android.gymkhagymkha.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.gymkhagymkha.R;
import com.example.android.gymkhagymkha.activities.Activity_Login;

public class Fragment_Pista extends Fragment {
    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pista, container, false);
        return view; }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        SharedPreferences prefs = this.getActivity().getSharedPreferences(Activity_Login.nombrePrefs, Context.MODE_PRIVATE);

        String pista = prefs.getString("pista", "no tiene valor");
        ((TextView) getActivity().findViewById(R.id.tvPista)).setText(pista);
    }
}
