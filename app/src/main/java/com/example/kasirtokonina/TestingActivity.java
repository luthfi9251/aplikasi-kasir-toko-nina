package com.example.kasirtokonina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kasirtokonina.Adaptor.AdaptorBarang;
import com.example.kasirtokonina.fragment.BottomSheetKasirCepat;
import com.example.kasirtokonina.fragment.MyBottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Button buttonBottomSheetModal = findViewById(R.id.buttonBottomSheetModal);
        buttonBottomSheetModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BottomSheetKasirCepat bottomSheetDialogFragment = new BottomSheetKasirCepat(AdaptorBarang());
                //bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

    }
}