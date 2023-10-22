package com.example.kasirtokonina.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andremion.counterfab.CounterFab;
import com.example.kasirtokonina.Modul.ModulBarang;
import com.example.kasirtokonina.Modul.ModulDataBarang;
import com.example.kasirtokonina.R;
import com.example.kasirtokonina.Utils.ConvertToCurrency;
import com.example.kasirtokonina.Utils.Storage;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.atomic.AtomicReference;

public class BottomSheetKasirMudah extends BottomSheetDialogFragment {
  ModulDataBarang dataBarang;
  Double banyakBarang = 1.;
  CounterFab counterBadge;
    public BottomSheetKasirMudah(ModulDataBarang dataBarang, CounterFab counterBadge) {
        this.dataBarang = dataBarang;
        this.counterBadge = counterBadge;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_kasir_mudah, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView subTotal;
        TextInputLayout namaBarang;
        TextInputLayout hargaBarang;
        TextInputLayout jumlahBarang;
        TextInputLayout pcsBarang;
        MaterialButton tambahBarangBtn, btnTambah, btnKurang;

        namaBarang = getView().findViewById(R.id.input_nama_barang);
        hargaBarang= getView().findViewById(R.id.input_harga_barang);
        jumlahBarang= getView().findViewById(R.id.input_jumlah_barang);
        pcsBarang = getView().findViewById(R.id.input_pcs_barang);
        tambahBarangBtn = getView().findViewById(R.id.btn_tambah_barang);
        btnTambah = getView().findViewById(R.id.btnPlus);
        btnKurang = getView().findViewById(R.id.btnMinus);
        subTotal = getView().findViewById(R.id.subTotal);

        namaBarang.getEditText().setText(dataBarang.getNamaBarang());
        hargaBarang.getEditText().setText(ConvertToCurrency.convert(dataBarang.getHarga().intValue()));
        jumlahBarang.getEditText().setText(banyakBarang + "");
        subTotal.setText(ConvertToCurrency.convert(banyakBarang.intValue()*dataBarang.getHarga().intValue()));

        String[] items = {"pcs", "dzn", "mtr", "pack", "roll"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getView().getContext(), R.layout.list_item, items);
        pcsBarang.getEditText().setText(dataBarang.getSatuanBarang());
        ((AutoCompleteTextView) pcsBarang.getEditText()).setAdapter(adapter);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.clearFocus();
                banyakBarang += 1;
                jumlahBarang.getEditText().setText(banyakBarang + "");
                subTotal.setText(ConvertToCurrency.convert(banyakBarang.intValue()*dataBarang.getHarga().intValue()));
            }
        });

        btnKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.clearFocus();
                if(banyakBarang > 1){
                    banyakBarang -= 1;
                    jumlahBarang.getEditText().setText(banyakBarang + "");
                    subTotal.setText(ConvertToCurrency.convert(banyakBarang.intValue()*dataBarang.getHarga().intValue()));
                }
            }
        });

        jumlahBarang.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String result = s.toString();
                Double banyakText;
                if(!result.isEmpty()){
                    banyakText = Double.valueOf(result);
                }else{
                    banyakText = 0.;
                }
                banyakBarang = banyakText;
                subTotal.setText(ConvertToCurrency.convert((int)(banyakBarang*dataBarang.getHarga())));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tambahBarangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaBarangTemp = namaBarang.getEditText().getText().toString();
                String hargaBarangTemp = hargaBarang.getEditText().getText().toString();
                String jumlahBarangTemp = jumlahBarang.getEditText().getText().toString();
                String pcsBarangTemp = pcsBarang.getEditText().getText().toString();
                Double totalbarang = banyakBarang * dataBarang.getHarga();

                if(namaBarangTemp.isEmpty() || hargaBarangTemp.isEmpty() || jumlahBarangTemp.isEmpty()){
                    Toast.makeText(getContext(), "Pastikan tidak ada input yang kosong!", Toast.LENGTH_SHORT).show();
                }else {

                    ModulBarang barangNew = new ModulBarang(namaBarangTemp, banyakBarang,dataBarang.getHarga(),totalbarang);
                    barangNew.setSatuan(dataBarang.getSatuanBarang());
                    AtomicReference<Boolean> alreadyAdded = new AtomicReference<>(false);
                    Storage.arrayListBarang.forEach(itemBarang -> {
                        if(itemBarang.getNamaBarang().equalsIgnoreCase(barangNew.getNamaBarang()) && itemBarang.getSatuan().equalsIgnoreCase(barangNew.getSatuan())){
                            itemBarang.setJumlah(itemBarang.getJumlah()+barangNew.getJumlah());
                            itemBarang.setTotal(itemBarang.getJumlah()*itemBarang.getHarga());
                            alreadyAdded.set(true);
                        }
                    });
                    if(!alreadyAdded.get()){
                        Storage.arrayListBarang.add(barangNew);
                        counterBadge.setCount(Storage.arrayListBarang.size());
                        Toast.makeText(getContext(), "data berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "jumlah barang berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            }
        });


    }
}