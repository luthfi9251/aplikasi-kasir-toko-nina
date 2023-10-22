package com.example.kasirtokonina.Utils;

import java.text.DecimalFormat;

public class ConvertToCurrency {
    public static String convert(int number){
        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        return "Rp. " + currencyFormat.format(number);
    }
}
