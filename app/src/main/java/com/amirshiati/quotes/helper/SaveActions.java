package com.amirshiati.quotes.helper;

import android.app.Activity;
import android.content.Context;

import com.amirshiati.quotes.consts.Consts;
import com.amirshiati.quotes.models.Quotes;

import java.util.ArrayList;

public class SaveActions {
    private Activity activity;
    private Context context;

    private ArrayList<Quotes> savedQuotes = new ArrayList<Quotes>();

    private TinyDB tinyDB;

    public SaveActions(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;

        tinyDB = new TinyDB(context);
        getSaveQuotes();
    }

    public void saveQuote(Quotes toSave) {
        savedQuotes.add(toSave);
        tinyDB.putListObject(Consts.savedQuotesDBName, castToObject(savedQuotes));
    }

    private void getSaveQuotes() {
        ArrayList<Object> result;
        result = tinyDB.getListObject(Consts.savedQuotesDBName, Quotes.class);
        savedQuotes = castToQuotes(result);
    }

    private ArrayList<Object> castToObject(ArrayList<Quotes> quotesToSave) {
        ArrayList<Object> result = new ArrayList<>();
        for (Quotes quote : quotesToSave) {
            result.add((Object) quote);
        }
        return result;
    }

    private ArrayList<Quotes> castToQuotes(ArrayList<Object> objects) {
        ArrayList<Quotes> result = new ArrayList<>();
        for (Object object : objects) {
            result.add((Quotes) object);
        }
        return result;
    }

    public ArrayList<Quotes> getSavedQuotes() {
        return savedQuotes;
    }

    public void setSavedQuotes(ArrayList<Quotes> savedQuotes) {
        this.savedQuotes = savedQuotes;
    }
}
