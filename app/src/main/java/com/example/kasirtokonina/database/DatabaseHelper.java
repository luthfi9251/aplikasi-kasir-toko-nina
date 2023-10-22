package com.example.kasirtokonina.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String NAMA_DATA_BASE="TOKO_NINA";
    public final static String NAMA_TABLE="barang";
    public final static String fiel01="id_barang";
    public final static String fiel02="nama_barang";
    public final static String fiel03="satuan";
    public final static String fiel04="harga";
    public final static String fiel05="stok";
    public final static String fiel06="status";

    public DatabaseHelper(Context context) {
        super(context, NAMA_DATA_BASE, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+NAMA_TABLE);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+NAMA_TABLE+"(id_barang text primary key," +
                "nama_barang text," +
                "satuan double," +
                "harga double,"+
                "stok double,"+
                "status INT" +
                ")");
    }

    public void entriDataTabelBarang(String kode, String nama, String satuan, Double harga, Double stok, int status) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(fiel01,kode);
        contentValues.put(fiel02,nama);
        contentValues.put(fiel03,satuan);
        contentValues.put(fiel04,harga);
        contentValues.put(fiel05,stok);
        contentValues.put(fiel06,status);
        db.insert(NAMA_TABLE,null,contentValues);
    }

    public boolean updateDataTabelBarang(String kode, String nama, String satuan, Double harga, Double stok, int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(fiel02,nama);
        contentValues.put(fiel03,satuan);
        contentValues.put(fiel04,harga);
        contentValues.put(fiel05,stok);
        contentValues.put(fiel06,status);
        int update = db.update(NAMA_TABLE,contentValues, fiel01+"=" + kode , null);
        Log.d("update", kode + " " + update);
        return update > 0;
    }

    public boolean cekDataTabelBarang(String nama, String satuan){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur = db.rawQuery("select * from "+NAMA_TABLE+" where nama_barang='"+nama+"' and satuan='"+satuan+"'",null);
        return cur.getCount() > 0;
    }

    public boolean deleteDataTabelBarang(String kode){
        SQLiteDatabase db=this.getWritableDatabase();
        int delete = db.delete(NAMA_TABLE,fiel01+"=" + kode,null);
        return delete > 0;
    }

    public Cursor getDataBarang() {
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from "+NAMA_TABLE,null);
        return cur;
    }
}
