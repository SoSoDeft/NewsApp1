package com.example.android.newsapp;

public class Article {

    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mDate;
    private String mUrl;


    // Default empty constructor
    public Article(){

    }

    //Object constructor
    public Article(String title, String section, String author, String date, String url){
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }


    //Getters
    public String getTitle(){
        return mTitle;
    }

    public String getSection(){
        return mSection;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getDate(){
        return mDate;
    }

    public String getUrl(){return mUrl;}
}
