package com.amirshiati.quotes.consts;

public class Consts {
    private static String baseUrl = "http://10.0.3.2:8000";

    public static String userName = "admin";
    public static String passWord = "changeme";

    public static String getAllQuotesURL = baseUrl + "/api/quotes/getall";
    public static String addLikeURL = baseUrl + "/api/quotes/liked/";

    public static String likeErr_mgs = "couldn't like the quote!";

    public static int requestTimeOutMilliSecondes = 5000;
}
