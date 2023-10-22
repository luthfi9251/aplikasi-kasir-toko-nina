package com.example.kasirtokonina.Modul;

public class ModulDataBarang {
    String id,namaBarang,satuanBarang;
    Double stok, harga;
    int status;

    public ModulDataBarang(String id, String namaBarang, String satuanBarang, Double stok, Double harga, int status) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.satuanBarang = satuanBarang;
        this.stok = stok;
        this.harga = harga;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuanBarang() {
        return satuanBarang;
    }

    public void setSatuanBarang(String satuanBarang) {
        this.satuanBarang = satuanBarang;
    }

    public Double getStok() {
        return stok;
    }

    public void setStok(Double stok) {
        this.stok = stok;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
