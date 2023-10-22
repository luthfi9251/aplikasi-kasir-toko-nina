package com.example.kasirtokonina.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kasirtokonina.Adaptor.AdaptorDataBarang;
import com.example.kasirtokonina.Modul.ModulBarang;
import com.example.kasirtokonina.Modul.ModulDataBarang;
import com.example.kasirtokonina.R;
import com.example.kasirtokonina.Utils.GenerateId;
import com.example.kasirtokonina.database.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class BottomSheetTambahDataBarang extends BottomSheetDialogFragment {
    AdaptorDataBarang adaptor;
    public BottomSheetTambahDataBarang(AdaptorDataBarang adaptor) {
        this.adaptor = adaptor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.botom_sheet_data_barang, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextInputLayout namaBarang, hargaBarang, stokBarang, pcsBarang;
        MaterialButton tambahBarangBtn, cancelBtn;

        namaBarang = getView().findViewById(R.id.input_nama_barang);
        hargaBarang= getView().findViewById(R.id.input_harga_barang);
        stokBarang= getView().findViewById(R.id.input_stok_barang);
        pcsBarang = getView().findViewById(R.id.input_pcs_barang);
        tambahBarangBtn = getView().findViewById(R.id.btn_tambah_barang);
        cancelBtn = getView().findViewById(R.id.btn_cancel_barang);

        String[] items = {"pcs", "dzn", "mtr", "pack", "roll"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getView().getContext(), R.layout.list_item, items);
        pcsBarang.getEditText().setText(adapter.getItem(0));
        ((AutoCompleteTextView) pcsBarang.getEditText()).setAdapter(adapter);

        tambahBarangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaBarangTemp = namaBarang.getEditText().getText().toString();
                String hargaBarangTemp = hargaBarang.getEditText().getText().toString();
                String stokBarangTemp = stokBarang.getEditText().getText().toString();
                String pcsBarangTemp = pcsBarang.getEditText().getText().toString();

                if(namaBarangTemp.isEmpty() || hargaBarangTemp.isEmpty() || stokBarangTemp.isEmpty()){
                    Toast.makeText(getContext(), "Pastikan tidak ada input yang kosong!", Toast.LENGTH_SHORT).show();
                }else {
                    Double harga = Double.valueOf(hargaBarangTemp);
                    Double stok = Double.valueOf(stokBarangTemp);
                    String id = GenerateId.generateNumberID(8);

                    DatabaseHelper dbh = new DatabaseHelper(getContext());

                    if(dbh.cekDataTabelBarang(namaBarangTemp,pcsBarangTemp)){
                        Toast.makeText(getContext(), "Data sudah ada di database", Toast.LENGTH_SHORT).show();
                    }else{
                        //insert database
                        dbh.entriDataTabelBarang(id,namaBarangTemp,pcsBarangTemp,harga,stok,1);
                        //update recycler
                        adaptor.updateArrayDataBarang(new ModulDataBarang(id,namaBarangTemp, pcsBarangTemp,stok,harga,1));
                        Toast.makeText(getContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
