package com.example.kasirtokonina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.andremion.counterfab.CounterFab;
import com.example.kasirtokonina.Adaptor.AdaptorDataBarang;
import com.example.kasirtokonina.Adaptor.AdaptorKasirMudah;
import com.example.kasirtokonina.Modul.ModulDataBarang;
import com.example.kasirtokonina.Utils.Storage;
import com.example.kasirtokonina.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class KasirMudah extends AppCompatActivity {

    ArrayList<ModulDataBarang> dataBarang = new ArrayList<ModulDataBarang>();
    RecyclerView recyclerData;
    AdaptorKasirMudah adaptorKasirMudah;
    CounterFab checkoutcart;
    TextInputLayout cariData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir_mudah);
        recyclerData = findViewById(R.id.recyclerKasirMudah);
        checkoutcart = findViewById(R.id.checkoutCart);
        cariData = findViewById(R.id.input_cari_barang);

        checkoutcart.setCount(Storage.arrayListBarang.size());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        DatabaseHelper dbh= new DatabaseHelper(this);
        Cursor cursor=dbh.getDataBarang();
        cursor.moveToPrevious();

        while (cursor.moveToNext()){
            dataBarang.add(new ModulDataBarang(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.valueOf(cursor.getString(4)),
                    Double.valueOf(cursor.getString(3)),
                    Integer.valueOf(cursor.getString(5))));
        }
        if(Storage.arrayListDataBarang.size() == 0){
            Storage.arrayListDataBarang = dataBarang;
        }

        recyclerData.setLayoutManager(new LinearLayoutManager(this));
        adaptorKasirMudah = new AdaptorKasirMudah(dataBarang, getSupportFragmentManager(), checkoutcart);
        recyclerData.setAdapter(adaptorKasirMudah);
        adaptorKasirMudah.setRecycler(recyclerData);

        checkoutcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
            }
        });

        cariData.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                ArrayList<ModulDataBarang> dataBarangFiltered = new ArrayList<>();

                if(!text.isEmpty()){
                    for(int i = 0; i < dataBarang.size(); i++){
                        if(dataBarang.get(i).getNamaBarang().toLowerCase().contains(text.toLowerCase())){
                            dataBarangFiltered.add(dataBarang.get(i));
                        }
                    }
                    adaptorKasirMudah.updateArrayListData(dataBarangFiltered);
                }else{
                    adaptorKasirMudah.updateArrayListData(Storage.arrayListDataBarang);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}