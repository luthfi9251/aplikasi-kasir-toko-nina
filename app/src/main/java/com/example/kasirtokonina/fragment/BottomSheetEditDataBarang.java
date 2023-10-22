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
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirtokonina.Adaptor.AdaptorDataBarang;
import com.example.kasirtokonina.Modul.ModulDataBarang;
import com.example.kasirtokonina.R;
import com.example.kasirtokonina.Utils.GenerateId;
import com.example.kasirtokonina.Utils.Storage;
import com.example.kasirtokonina.database.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class BottomSheetEditDataBarang extends BottomSheetDialogFragment {
    ModulDataBarang data;
    AdaptorDataBarang adaptor;
    RecyclerView recycler;
    int index;
    public BottomSheetEditDataBarang(int index, ModulDataBarang data, RecyclerView recycler) {
        this.data = data;
        this.index = index;
        this.recycler = recycler;
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
        MaterialButton tambahBarangBtn, hapusBarangBtn;

        namaBarang = getView().findViewById(R.id.input_nama_barang);
        hargaBarang= getView().findViewById(R.id.input_harga_barang);
        stokBarang= getView().findViewById(R.id.input_stok_barang);
        pcsBarang = getView().findViewById(R.id.input_pcs_barang);
        tambahBarangBtn = getView().findViewById(R.id.btn_tambah_barang);
        tambahBarangBtn.setText("Simpan");
        hapusBarangBtn = getView().findViewById(R.id.btn_cancel_barang);
        hapusBarangBtn.setText("Hapus Barang");

        namaBarang.getEditText().setText(data.getNamaBarang());
        hargaBarang.getEditText().setText(data.getHarga().intValue() + "");
        stokBarang.getEditText().setText(data.getStok().toString());


        String[] items = {"pcs", "dzn", "mtr", "pack", "roll"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getView().getContext(), R.layout.list_item, items);
        pcsBarang.getEditText().setText(data.getSatuanBarang());
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

                    DatabaseHelper dbh = new DatabaseHelper(getContext());

                    //insert database
                    Boolean success = dbh.updateDataTabelBarang(data.getId(),namaBarangTemp,pcsBarangTemp,harga,stok,1);

                    if(success){
                        adaptor = (AdaptorDataBarang) recycler.getAdapter();
                        adaptor.updateDataBarang(new ModulDataBarang(data.getId(),namaBarangTemp, pcsBarangTemp,stok,harga,1),index);
                        for(int i = 0; i < Storage.arrayListDataBarang.size(); i++){
                            if(Storage.arrayListDataBarang.get(i).getId() == data.getId()){
                                Storage.arrayListDataBarang.set(i,new ModulDataBarang(data.getId(),namaBarangTemp, pcsBarangTemp,stok,harga,1));
                            }
                        }
                        Toast.makeText(getContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        Toast.makeText(getContext(), "Data Gagal Disimpan", Toast.LENGTH_SHORT).show();
                    }
                    //update recycler

                }

            }
        });

        hapusBarangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbh = new DatabaseHelper(getContext());
                Boolean success = dbh.deleteDataTabelBarang(data.getId());

                if(success){
                    adaptor = (AdaptorDataBarang) recycler.getAdapter();
                    adaptor.deleteDataBarang(index);
                    for(int i = 0; i < Storage.arrayListDataBarang.size(); i++){
                        if(Storage.arrayListDataBarang.get(i).getId() == data.getId()){
                            Storage.arrayListDataBarang.remove(i);
                        }
                    }
                    Toast.makeText(getContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
