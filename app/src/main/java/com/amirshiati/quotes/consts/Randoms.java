package com.amirshiati.quotes.consts;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.amirshiati.quotes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randoms {
    public static List<Integer> allColors = new ArrayList<>();
    public static List<Typeface> allTypeFaces = new ArrayList<>();
    public static int quotationColor;
    public static int writerNameColor;
    public static int tagColor;

    public static void setAllColors(Context context) {
        allColors.add(ContextCompat.getColor(context, R.color.text_color));
        allColors.add(ContextCompat.getColor(context, R.color.text_color_2));
        allColors.add(ContextCompat.getColor(context, R.color.text_color_3));
        allColors.add(ContextCompat.getColor(context, R.color.text_color_4));
        quotationColor = ContextCompat.getColor(context, R.color.quotation);
        writerNameColor = ContextCompat.getColor(context, R.color.writer_name);
        tagColor = ContextCompat.getColor(context, R.color.tag);
    }

    public static void setTypeFaces(Context context) {
        allTypeFaces.add(Typeface.createFromAsset(context.getAssets(), "fonts/Stoke-Light.ttf"));
        allTypeFaces.add(Typeface.createFromAsset(context.getAssets(), "fonts/Cinzel-Regular.ttf"));
        allTypeFaces.add(Typeface.createFromAsset(context.getAssets(), "fonts/Cocogoose-Classic-Medium-trial.ttf"));
    }

    public static int randomColorIndex() {
        return new Random().nextInt(((allColors.size() - 1) - 0) + 1) + 0;
    }

    public static int randomTypeFaceIndex() {
        return new Random().nextInt(((allTypeFaces.size() - 1) - 0) + 1) + 0;
    }
}
