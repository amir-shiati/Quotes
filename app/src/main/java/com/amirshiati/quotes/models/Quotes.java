package com.amirshiati.quotes.models;

public class Quotes {
    private Long id;
    private String quote;
    private Long likes;
    private String subject;
    private String writerFirstName;
    private String writerLastName;

    public Quotes(Long id, String quote, Long likes, String subject, String writerFirstName, String writerLastName) {
        this.id = id;
        this.quote = quote;
        this.likes = likes;
        this.subject = subject;
        this.writerFirstName = writerFirstName;
        this.writerLastName = writerLastName;
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
        return subject;
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
}
