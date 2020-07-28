package com.example.sistemmasjidperumda;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class DataFragment extends Fragment {

    private DataFragment dataFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_data, container, false);

        Button btnInven = root.findViewById(R.id.inventarisbtn);
        btnInven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), InventarisActivity.class));
                Toast.makeText(getActivity(), "Data Inventaris", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnKeuangan = root.findViewById(R.id.uangbtn);
        btnKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), KeuanganMasukActivity.class));
            }
        });

        return root;
    }

}
