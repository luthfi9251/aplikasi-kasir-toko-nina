package com.example.kasirtokonina.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirtokonina.Modul.ModulBarang;
import com.example.kasirtokonina.R;
import com.example.kasirtokonina.Utils.ConvertToCurrency;

import java.util.ArrayList;

public class AdaptorPrintView extends RecyclerView.Adapter<AdaptorPrintView.myViewHolder> {
    ArrayList<ModulBarang> barangArrayList;

    public AdaptorPrintView(ArrayList<ModulBarang> barangArrayList) {
        this.barangArrayList = barangArrayList;
    }

    public void updateDataBarang(ModulBarang barangNew){
        this.barangArrayList.add(barangNew);
        notifyDataSetChanged();
    }

    public ArrayList<ModulBarang> getArrayList(){
        return this.barangArrayList;
    }

    @NonNull
    @Override
    public AdaptorPrintView.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_item_print,parent,false);
        return new AdaptorPrintView.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorPrintView.myViewHolder holder, int position) {
        holder.namaProduk.setText(barangArrayList.get(position).getNamaBarang());
        holder.hargaProduk.setText(ConvertToCurrency.convert((int)barangArrayList.get(position).getHarga()));
        holder.jumlahBarang.setText(barangArrayList.get(position).getJumlah() + " " + barangArrayList.get(position).getSatuan());
        holder.total.setText(ConvertToCurrency.convert((int)barangArrayList.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        return barangArrayList.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView namaProduk, hargaProduk, jumlahBarang, total;


        public myViewHolder(@NonNull View itemView){
            super(itemView);
            namaProduk = itemView.findViewById(R.id.nama_produk);
            hargaProduk = itemView.findViewById(R.id.harga_satuan);
            jumlahBarang = itemView.findViewById(R.id.jumlah);
            total = itemView.findViewById(R.id.total_harga);
        }
    }
}
