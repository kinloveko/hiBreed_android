package com.example.hi_breed.classesFile;

import java.text.NumberFormat;
import java.util.Locale;

public class priceFormat {

    public String priceFormatString(String strNum){

        double num = Double.parseDouble(strNum);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        String formattedNum = nf.format(num);

        return formattedNum;
    }

}
