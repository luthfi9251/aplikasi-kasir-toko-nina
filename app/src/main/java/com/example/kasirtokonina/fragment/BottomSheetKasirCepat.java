package com.example.kasirtokonina.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kasirtokonina.Adaptor.AdaptorBarang;
import com.example.kasirtokonina.Modul.ModulBarang;
import com.example.kasirtokonina.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetKasirCepat extends BottomSheetDialogFragment {
    AdaptorBarang adaptor;
    public BottomSheetKasirCepat(AdaptorBarang adaptor) {
        this.adaptor = adaptor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_kasir_cepat, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextInputLayout namaBarang, hargaBarang, jumlahBarang, pcsBarang;
        MaterialButton tambahBarangBtn;

        namaBarang = getView().findViewById(R.id.input_nama_barang);
        hargaBarang= getView().findViewById(R.id.input_harga_barang);
        jumlahBarang= getView().findViewById(R.id.input_jumlah_barang);
        pcsBarang = getView().findViewById(R.id.input_pcs_barang);
        tambahBarangBtn = getView().findViewById(R.id.btn_tambah_barang);

        String[] items = {"pcs", "dzn", "mtr", "pack", "roll", "cm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getView().getContext(), R.layout.list_item, items);
        pcsBarang.getEditText().setText(adapter.getItem(0));
        ((AutoCompleteTextView) pcsBarang.getEditText()).setAdapter(adapter);

        tambahBarangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                String namaBarangTemp = namaBarang.getEditText().getText().toString();
                String hargaBarangTemp = hargaBarang.getEditText().getText().toString();
                String jumlahBarangTemp = jumlahBarang.getEditText().getText().toString();
                String pcsBarangTemp = pcsBarang.getEditText().getText().toString();

                if(namaBarangTemp.isEmpty() || hargaBarangTemp.isEmpty() || jumlahBarangTemp.isEmpty()){
                    Toast.makeText(getContext(), "Pastikan tidak ada input yang kosong!", Toast.LENGTH_SHORT).show();
                }else {
                    Double total = Double.valueOf(hargaBarangTemp) * Double.valueOf(jumlahBarangTemp);
                    if(pcsBarangTemp.equalsIgnoreCase("cm")){
                        pcsBarangTemp = "mtr";
                        jumlahBarangTemp = (Double.valueOf(jumlahBarangTemp) / 100) + "";
                        total = Double.valueOf(hargaBarangTemp) * Double.valueOf(jumlahBarangTemp);
                    }

                    ModulBarang barangNew = new ModulBarang(namaBarangTemp, Double.valueOf(jumlahBarangTemp),Double.valueOf(hargaBarangTemp),total);
                    barangNew.setSatuan(pcsBarangTemp);
                    adaptor.updateDataBarang(barangNew);
                    namaBarang.getEditText().setText("");
                    hargaBarang.getEditText().setText("");
                    jumlahBarang.getEditText().setText("");
                }

            }
        });


    }
}
