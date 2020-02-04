package com.amirshiati.quotes.models;

import android.graphics.Typeface;
import android.util.Log;

import com.amirshiati.quotes.consts.Randoms;

public class Quotes {
    private Long id;
    private String quote;
    private Long likes;
    private String subject;
    private String writerFirstName;
    private String writerLastName;

    private int assignedColor;
    private Typeface assignedTypeFace;
    private boolean liked = false;
    private boolean saved = false;

    public Quotes(Long id, String quote, Long likes, String subject, String writerFirstName, String writerLastName) {
        this.id = id;
        this.quote = quote;
        this.likes = likes;
        this.subject = subject;
        this.writerFirstName = writerFirstName;
        this.writerLastName = writerLastName;

        assignedColor = Randoms.allColors.get(Randoms.randomColorIndex());
        assignedTypeFace = Randoms.allTypeFaces.get(Randoms.randomTypeFaceIndex());
    }

    @Override
    public String toString() {
        Log.i("s", generateQuoteAsString());
        return generateQuoteAsString();
    }

    private String generateQuoteAsString() {
        return "<font color=" + Randoms.quotationColor + ">" + "<b>" + "<big>" + "\" " + "</big>" + "</b>" + "</font>" + "<font color=" + assignedColor + ">"
                + getQuote() + "</font>"
                + "<font color=" + Randoms.quotationColor + ">" + "<b>" + "<big>" + " \"" + "</big>" + "</b>" + "</font>";
    }

    public String firstAndLastName() {
        String result = "";
        if (!getWriterFirstName().equals("null")) {
            result += getWriterFirstName();
        }
        if (!getWriterLastName().equals("null")) {
            result += " " + getWriterLastName();
        }
        return "<font color=" + Randoms.writerNameColor + ">" + "<b>" + "<big>" + "~ " + "</big>" + "</b>" + result + "</font>";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getSubject() {
        return "<i> <font color=" + Randoms.tagColor + ">" + "#" + subject + "</font> </i>";
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWriterFirstName() {
        return writerFirstName;
    }

    public void setWriterFirstName(String writerFirstName) {
        this.writerFirstName = writerFirstName;
    }

    public String getWriterLastName() {
        return writerLastName;
    }

    public void setWriterLastName(String writerLastName) {
        this.writerLastName = writerLastName;
    }

    public Typeface getAssignedTypeFace() {
        return assignedTypeFace;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
