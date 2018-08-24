package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    //Variable for info to include in logs
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    //Make constructor private, as object instance should never be used, but rather the class itsself
    private QueryUtils() {

    }


    //Make method to fetch data and return a list of Article objects
    //Udacity/GWG course 2018

    public static List<Article> fetchArticleData(String requestUrl) {

        try {

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create a URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to URL and receive JSON response
        String jsonResponse = null; //Make sure not to just definite it without initialize it
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //Retrieve relevant fields from JSON response and create list of articles
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        return articles;

    }

    // Method to create URL object from a string
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Issue with making URL", e);
        }
        return url;
    }


    // Method to create HTTP request from given URL and return response string
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if Url is empty, then return as-is
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000); // Timeout if unable to connect for 10 secs
            urlConnection.setConnectTimeout(15000); // Timeout if unable to connect for 15 secs
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Convert input stream into a string, using string builder te get JSON response
    private static String readFromStream(InputStream is) throws IOException {
        StringBuilder output = new StringBuilder();
        if (is != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine(); // recursion until line != null is no longer true
            }
        }

        return output.toString();
    }


    //Return list of article object created from parsed JSON response
    private static List<Article> extractFeatureFromJson(String articleJSON) {
        //If JSON string is empty or null, then return
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        //Create an ArrayList to hold article objects
        List<Article> articles = new ArrayList<>();


        // Parse JSON response. If there is issue, JSONException is thrown
        try {
            JSONObject generalJsonResponse = new JSONObject(articleJSON);
            JSONObject gjrResponse = generalJsonResponse.getJSONObject("response");
            JSONArray array = gjrResponse.getJSONArray("results"); // Api key for requested results

            for (int i = 0; i < array.length(); i++) { // For each array item...
                JSONObject listArticle = array.getJSONObject(i); // ...create JSONObject
                String title = listArticle.getJSONObject("fields").getString("headline");
                String section = listArticle.getString("sectionName");
                String author = listArticle.getJSONArray("tags").getJSONObject(0).getString("webTitle");
                String dateTime = listArticle.getString("webPublicationDate");

                //Create regex pattern to match date portion of web publication date
                String[] dateArray = dateTime.split("T");


                //Pattern match first part of webPublicationDate, before the first Text character (T)
                String date = dateArray[0];


                String webUrl = listArticle.getString("webUrl");

                Article article = new Article(title, section, author, date, webUrl);
                articles.add(article);
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing articles JSON results", e);
        }

        //Return list of articles

        return articles;

    }


}
