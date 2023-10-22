package com.example.kasirtokonina.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andremion.counterfab.CounterFab;
import com.example.kasirtokonina.Modul.ModulDataBarang;
import com.example.kasirtokonina.R;
import com.example.kasirtokonina.Utils.ConvertToCurrency;
import com.example.kasirtokonina.fragment.BottomSheetEditDataBarang;
import com.example.kasirtokonina.fragment.BottomSheetKasirMudah;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AdaptorKasirMudah extends RecyclerView.Adapter<AdaptorKasirMudah.myViewHolder> {
    ArrayList<ModulDataBarang> barangArrayList;
    FragmentManager fragmentManager;
    RecyclerView recycle;
    CounterFab counterBadge;

    public AdaptorKasirMudah(ArrayList<ModulDataBarang> barangArrayList, FragmentManager fragmentManager, CounterFab counterBadge) {
        this.barangArrayList = barangArrayList;
        this.fragmentManager = fragmentManager;
        this.counterBadge = counterBadge;
    }

    public void updateArrayDataBarang(ModulDataBarang barangNew){
        this.barangArrayList.add(barangNew);
        notifyDataSetChanged();
    }

    public void updateDataBarang(ModulDataBarang barangUpdate, int index){
        this.barangArrayList.set(index, barangUpdate);
        notifyDataSetChanged();
    }

    public void deleteDataBarang(int index){
        this.barangArrayList.remove(index);
        notifyDataSetChanged();
    }

    public void setRecycler(RecyclerView recycle){
        this.recycle = recycle;
    }

    public void deleteArrayList(){
        this.barangArrayList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void updateArrayListData(ArrayList<ModulDataBarang> barangArrayList){
        this.barangArrayList = barangArrayList;
        notifyDataSetChanged();
    }

    public void searchData(String s){

    }

    public ArrayList<ModulDataBarang> getArrayList(){
        return this.barangArrayList;
    }

    @NonNull
    @Override
    public AdaptorKasirMudah.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_kasir_mudah,parent,false);
        return new AdaptorKasirMudah.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorKasirMudah.myViewHolder holder, int position) {
        holder.namaProduk.setText(barangArrayList.get(position).getNamaBarang());
        holder.hargaProduk.setText(ConvertToCurrency.convert(barangArrayList.get(position).getHarga().intValue()));
        holder.satuanProduk.setText(barangArrayList.get(position).getSatuanBarang());
        holder.stok.setText(barangArrayList.get(position).getStok()+"");

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetKasirMudah bottomSheetMudah = new BottomSheetKasirMudah(barangArrayList.get(holder.getAdapterPosition()), counterBadge);
                bottomSheetMudah.show(fragmentManager, bottomSheetMudah.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return barangArrayList.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView namaProduk, hargaProduk, satuanProduk, stok;
        MaterialButton btnEdit;


        public myViewHolder(@NonNull View itemView){
            super(itemView);
            namaProduk = itemView.findViewById(R.id.nama_produk);
            hargaProduk = itemView.findViewById(R.id.harga_produk);
            satuanProduk = itemView.findViewById(R.id.satuan_produk);
            stok = itemView.findViewById(R.id.stok);
            btnEdit = itemView.findViewById(R.id.btn_tambah);
        }
    }
}