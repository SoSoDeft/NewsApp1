package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    // Log Tag for Article Loader
    private static final String LOG_TAG = ArticleLoader.class.getSimpleName();

    //Field for query url
    private String mUrl;


    //Constructor
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    //Background Thread
    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        //Proceed with network request, parse response, and extract a list of articles
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }


}
