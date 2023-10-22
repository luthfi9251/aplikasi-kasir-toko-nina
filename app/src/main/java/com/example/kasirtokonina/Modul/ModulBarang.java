package com.example.kasirtokonina.Modul;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ModulBarang implements Parcelable {
    String namaBarang, satuan;
    double jumlah, harga,total;
    Double pembulatan = 0.;

    public ModulBarang(String namaBarang, double jumlah, double harga, double total) {
        this.namaBarang = namaBarang;
        this.satuan = "pcs";
        this.jumlah = jumlah;
        this.harga = harga;
        if(total % 100 > 0){
            pembulatan = 100 - (total % 100);
        }

        this.total = total + pembulatan;
    }

    protected ModulBarang(Parcel in) {
        namaBarang = in.readString();
        satuan = in.readString();
        jumlah = in.readDouble();
        harga = in.readDouble();
        total = in.readDouble();
    }

    public static final Creator<ModulBarang> CREATOR = new Creator<ModulBarang>() {
        @Override
        public ModulBarang createFromParcel(Parcel in) {
            return new ModulBarang(in);
        }

        @Override
        public ModulBarang[] newArray(int size) {
            return new ModulBarang[size];
        }
    };

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(jumlah);
        dest.writeDouble(harga);
        dest.writeDouble(total);
        dest.writeString(namaBarang);
        dest.writeString(satuan);
    }

}
