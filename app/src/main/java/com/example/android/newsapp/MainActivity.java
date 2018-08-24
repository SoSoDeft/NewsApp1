package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    //URL for Article API
    public static final String NEWS_REQUEST_URL =
            "https://content.guardianapis.com/search?show-tags=contributor&show-fields=all&q=America&api-key=";
    //Tag for logcat
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Constant for Article Loader ( can be any number )
    private static final int ARTICLE_LOADER_ID = 10;
    //Personalized key to use API
    private static final String key = "398d194f-ae5a-4d52-9816-ee801a974c98";
    //Whole screen TextView displayed if list empty
    private TextView emptyStateTextView;
    //Reference to ArticleAdapter instance
    private ArticleAdapter mAdapter;

    @Override
    public android.content.Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        //Create new load for URL
        return new ArticleLoader(this, NEWS_REQUEST_URL + key);

    }


    @Override
    public void onLoadFinished(android.content.Loader<List<Article>> loader, List<Article> articles) {

        //Hide loading indicator, when data loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //When no articles available display "Oops, no articles"
        emptyStateTextView.setText(R.string.empty_state_text);

        //Clear adapter of prior earthquake data
        mAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get reference to ListView in layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        //Make ArrayAdapter of articles
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        //Populate ListView with data
        articleListView.setAdapter(mAdapter);


        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //Get current earthquake clicked
                Article currentArticle = mAdapter.getItem(pos);

                //Convert String to URI object
                Uri articleUri = Uri.parse(currentArticle.getUrl());


                //Create Intent to view article Uri
                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                //Send intent to launch new activity
                startActivity(webIntent);

            }
        });

        //Find resource id for empty view
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);

        //Get reference to ConnectivityManager to check network connectivity
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on current active default data network
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();


        //If there is a network connection, get data
        if (netInfo != null && netInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        } else {

            //Hide Loading indicator in order to show error message
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            emptyStateTextView.setText(R.string.no_internet_text);

        }


        articleListView.setEmptyView(emptyStateTextView);

    }


}
