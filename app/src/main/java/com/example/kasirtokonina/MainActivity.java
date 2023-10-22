package com.example.kasirtokonina;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.kasirtokonina.Adaptor.AdaptorBarang;
import com.example.kasirtokonina.Modul.ModulBarang;
import com.example.kasirtokonina.Utils.Storage;
import com.example.kasirtokonina.fragment.BottomSheetKasirCepat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerBarang;
    TextInputLayout namaBarang, hargaBarang, jumlahBarang, pcsBarang;
    ArrayList<ModulBarang> barangArrayList = new ArrayList<ModulBarang>();
    AdaptorBarang adaptorBarang;
    MaterialButton tambahBarangBtn, bayarBtn, resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerBarang = findViewById(R.id.recyclerBarang);
        //namaBarang = findViewById(R.id.input_nama_barang);
        //hargaBarang= findViewById(R.id.input_harga_barang);
        //jumlahBarang= findViewById(R.id.input_jumlah_barang);
        tambahBarangBtn= findViewById(R.id.btn_tambah_barang);
        barangArrayList = Storage.arrayListBarang;
        //pcsBarang = findViewById(R.id.input_pcs_barang);
        bayarBtn = findViewById(R.id.btn_bayar);
        resetBtn = findViewById(R.id.btn_reset);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String[] items = {"pcs", "dzn", "mtr"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, items);
        //pcsBarang.getEditText().setText(adapter.getItem(0));
        //((AutoCompleteTextView) pcsBarang.getEditText()).setAdapter(adapter);


        recyclerBarang.setLayoutManager(new LinearLayoutManager(this));
        adaptorBarang = new AdaptorBarang(barangArrayList);
        recyclerBarang.setAdapter(adaptorBarang);

        tambahBarangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetKasirCepat bottomSheetDialogFragment = new BottomSheetKasirCepat(adaptorBarang);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

//        tambahBarangBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String namaBarangTemp = namaBarang.getEditText().getText().toString();
//                String hargaBarangTemp = hargaBarang.getEditText().getText().toString();
//                String jumlahBarangTemp = jumlahBarang.getEditText().getText().toString();
//                String pcsBarangTemp = pcsBarang.getEditText().getText().toString();
//
//                if(namaBarangTemp.isEmpty() || hargaBarangTemp.isEmpty() || jumlahBarangTemp.isEmpty()){
//                    Toast.makeText(MainActivity.this, "Pastikan tidak ada input yang kosong!", Toast.LENGTH_SHORT).show();
//                }else {
//                    Double total = Double.valueOf(hargaBarangTemp) * Double.valueOf(jumlahBarangTemp);
//                    ModulBarang barangNew = new ModulBarang(namaBarangTemp, Double.valueOf(jumlahBarangTemp),Double.valueOf(hargaBarangTemp),total);
//                    barangNew.setSatuan(pcsBarangTemp);
//                    //barangArrayList.add(barangNew);
//                    adaptorBarang.updateDataBarang(barangNew);
//
//                    namaBarang.getEditText().setText("");
//                    hargaBarang.getEditText().setText("");
//                    jumlahBarang.getEditText().setText("");
//                }
//
//            }
//        });

        bayarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ModulBarang> dataTemp = adaptorBarang.getArrayList();
                if(dataTemp.size() == 0) {
                    Toast.makeText(MainActivity.this, "Harap Isi Produk Dulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(dataTemp);
                editor.putString("activeTransaction", json);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), PrintView.class);
                startActivity(intent);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptorBarang.deleteArrayList();
                barangArrayList.clear();
            }
        });
    }
}