package com.example.kasirtokonina.Utils;

import java.util.Random;

public class GenerateId {
    public static String generateNumberID(int length) {
        // Panjang ID yang diinginkan
        int panjangID = 8;

        // Membuat objek Random
        Random random = new Random();

        // Menghasilkan ID acak dengan panjang 8 digit
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Menghasilkan digit acak antara 0 hingga 9
            idBuilder.append(digit);
        }

        String id = idBuilder.toString();
        return String.valueOf(id);
    }
}
