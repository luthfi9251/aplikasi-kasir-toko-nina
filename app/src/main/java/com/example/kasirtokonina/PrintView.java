package com.example.kasirtokonina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.example.kasirtokonina.Adaptor.AdaptorBarang;
import com.example.kasirtokonina.Adaptor.AdaptorPrintView;
import com.example.kasirtokonina.Modul.ModulBarang;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PrintView extends AppCompatActivity {

    private static final int PERMISSION_BLUETOOTH = 2;
    ArrayList<ModulBarang> barangArrayList = new ArrayList<ModulBarang>();
    RecyclerView recyclerProduk;
    AdaptorPrintView adaptorPrintview;
    MaterialButton printButton;
    TextView totalBayar, kembaliBayar, bayar;
    TextInputEditText inputBayar;
    ImageView indikator;
    BluetoothConnection printer;

    int total, bayarInt, kembali = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_view);

        recyclerProduk = findViewById(R.id.recyclerBarang);
        totalBayar = findViewById(R.id.text_total);
        kembaliBayar = findViewById(R.id.text_kembali);
        bayar = findViewById(R.id.text_bayar);
        inputBayar = findViewById(R.id.input_bayar);
        printButton = findViewById(R.id.btn_bayar_dan_cetak);
        indikator = findViewById(R.id.indikator_printer);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initBluetooth();


        //ArrayList<ModulBarang> receivedList = getIntent().getParcelableExtra("listBarang");;
        //Log.d("test", receivedList.toString());
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("activeTransaction", null);
        Type type = new TypeToken<ArrayList<ModulBarang>>(){}.getType();
        ArrayList<ModulBarang> receivedList = gson.fromJson(json, type);

        ModulBarang mod = new ModulBarang("benang", 2, 2000, 4000);
        barangArrayList = receivedList;


        for(int i =0; i<barangArrayList.size(); i++){
            total = (int)(total + barangArrayList.get(i).getTotal());
        }
        totalBayar.setText(convertToCurrency(total));

        recyclerProduk.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return super.canScrollVertically();
            }
        });
        adaptorPrintview = new AdaptorPrintView(barangArrayList);
        recyclerProduk.suppressLayout(true);
        recyclerProduk.setAdapter(adaptorPrintview);


        inputBayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtBayar = s.toString();

                if(!txtBayar.isEmpty()){
                    bayarInt =(Integer.valueOf(txtBayar));
                    kembali = (Integer.valueOf(txtBayar)-total);
                    bayar.setText(convertToCurrency(bayarInt));
                    kembaliBayar.setText(convertToCurrency(kembali));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(inputBayar.getText().toString().isEmpty()){
                        Toast.makeText(PrintView.this,  "Harap isi jumlah bayar terlebih dahulu", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    printReceipt(barangArrayList,total,(bayarInt-total),bayarInt);
                } catch (EscPosConnectionException e) {
                    throw new RuntimeException(e);
                } catch (EscPosEncodingException e) {
                    throw new RuntimeException(e);
                } catch (EscPosBarcodeException e) {
                    throw new RuntimeException(e);
                } catch (EscPosParserException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    void initBluetooth(){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 2);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 3);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 4);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 5);
        } else {
        }


        printer = BluetoothPrintersConnections.selectFirstPaired();

        if(printer == null){
            indikator.setImageResource(R.drawable.circle_yellow);
        }else{
            indikator.setImageResource(R.drawable.circle_green);
        }
    }

    public static String convertToCurrency(int number) {
        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        return "Rp. " + currencyFormat.format(number);
    }

    public String generateTodayDate(){
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        // Menetapkan format yang diinginkan
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID"));

        // Mengonversi tanggal ke format yang diinginkan
        String formattedDate = dateFormat.format(today);
        return formattedDate;
    }

    void printReceipt(ArrayList<ModulBarang> dataBarang, int total, int kembali, int bayar) throws EscPosConnectionException, EscPosEncodingException, EscPosBarcodeException, EscPosParserException {
        EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 57f, 32);
        //String barang = "[L]<b>Benang</b>[R]4.000\n" + "[L]  @2.000[L]2 pcs\n";
        String dataToPrint = "";
        int pembulatan = 100 - (total % 100);

        for(int i = 0; i<dataBarang.size();i++){
            String namaBarangT = dataBarang.get(i).getNamaBarang();
            String satuanBarangT = dataBarang.get(i).getSatuan();
            String hargaBarangT = convertToCurrency((int) dataBarang.get(i).getHarga());
            String totalBarang = convertToCurrency((int) dataBarang.get(i).getTotal());
            String banyakBerangT = dataBarang.get(i).getJumlah()+"";
            //"[L]<b>Benang</b>[R]4.000\n" + "[L]  @2.000[L]2 pcs\n";
            dataToPrint += "[L]<b>"+namaBarangT+"</b>[R]"+totalBarang+"\n" + "[L]  @"+hargaBarangT+"[L]"+banyakBerangT+" "+satuanBarangT+"\n";
            dataToPrint += "[L]\n";
        }

        printer
                .printFormattedText(
                                "[L]\n" +
                                "[C]<b>Toko Perlengkapan Jahit</b>\n" +
                                        "[C]<font size='big'>NINA</font>\n" +
                                        "[C]<b>Jl. Raya No.21 Mranggen, Demak</b>\n" +
                                        "[C]<b>HP.085325748708</b>\n"+
                                        "[C]===============================\n" +
                                        "[L]\n" +
                                        dataToPrint +
                                        "[C]--------------------------------\n"+
                                        "[R]<b>Total Harga :[R]"+convertToCurrency(total)+"</b>\n" +
                                        "[R]Bayar :[R]"+convertToCurrency(bayar)+"\n" +
                                        "[R]Kembali :[R]"+convertToCurrency(kembali)+"\n" +
                                        "[L]\n" +
                                        "[C]===============================\n" +
                                        "[L]\n" +
                                        "[L]Terimakasih atas kunjungan Anda.\n"+
                                        "[L]"+generateTodayDate()+"\n" +
                                        "[L]INV/NINA/24102023/001\n"+
                                        "[L]\n" +
                                        "[C]<b>PERHATIAN</b>\n"+
                                        "[C]Barang yang sudah dibeli tidak dapat dikembalikan!\n"
                );
    }
}