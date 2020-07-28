package com.example.sistemmasjidperumda;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

public class JadwalFragment extends Fragment {

    private PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    public JadwalFragment() {
        // Required empty public constructor
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pdfView = view.findViewById(R.id.pdfView);

        pdfView.fromAsset("sampel_1.pdf")
                .enableSwipe(true)
                .load();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jadwal, container, false);
        return view;
    }


}
