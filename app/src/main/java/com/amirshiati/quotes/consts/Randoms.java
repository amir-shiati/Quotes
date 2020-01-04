package com.amirshiati.quotes.consts;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.amirshiati.quotes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randoms {
    public static List<Integer> allColors = new ArrayList<>();
    public static int quotationColor;

    public static void setAllColors(Context context) {
        allColors.add(ContextCompat.getColor(context, R.color.text_color));
        allColors.add(ContextCompat.getColor(context, R.color.text_color_2));
        allColors.add(ContextCompat.getColor(context, R.color.text_color_3));
        allColors.add(ContextCompat.getColor(context, R.color.text_color_4));
        quotationColor = ContextCompat.getColor(context, R.color.quotation);
    }

    public static int randomColorIndex() {
        return new Random().nextInt(((allColors.size() - 1) - 0) + 1) + 0;
    }
}
