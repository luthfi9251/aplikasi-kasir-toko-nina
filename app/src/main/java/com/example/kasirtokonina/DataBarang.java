package com.example.kasirtokonina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.kasirtokonina.Adaptor.AdaptorBarang;
import com.example.kasirtokonina.Adaptor.AdaptorDataBarang;
import com.example.kasirtokonina.Modul.ModulDataBarang;
import com.example.kasirtokonina.Utils.Storage;
import com.example.kasirtokonina.database.DatabaseHelper;
import com.example.kasirtokonina.fragment.BottomSheetTambahDataBarang;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class DataBarang extends AppCompatActivity {

    MaterialButton tambahBarang;
    ArrayList<ModulDataBarang> dataBarang = new ArrayList<ModulDataBarang>();
    RecyclerView recyclerData;
    AdaptorDataBarang adaptorBarang;
    TextInputLayout cariData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);
        tambahBarang = findViewById(R.id.btn_tambah_barang);
        recyclerData = findViewById(R.id.recyclerDataBarang);
        cariData = findViewById(R.id.input_cari_barang);

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
        Storage.arrayListDataBarang = dataBarang;
        Log.d("test", dataBarang.toString());

        recyclerData.setLayoutManager(new LinearLayoutManager(this));
        adaptorBarang = new AdaptorDataBarang(Storage.arrayListDataBarang, getSupportFragmentManager());
        recyclerData.setAdapter(adaptorBarang);
        adaptorBarang.setRecycler(recyclerData);

        tambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetTambahDataBarang bottomSheetTambah = new BottomSheetTambahDataBarang(adaptorBarang);
                bottomSheetTambah.show(getSupportFragmentManager(), bottomSheetTambah.getTag());
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
                    adaptorBarang.updateArrayListData(dataBarangFiltered);
                }else{
                    adaptorBarang.updateArrayListData(Storage.arrayListDataBarang);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}