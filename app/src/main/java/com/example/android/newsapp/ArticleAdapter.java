package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        //Check to see if existing view is being reused, else, inflate view
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_list_item, parent, false);
        }

        // Get Word object located at this position in list
        Article currentArticle = getItem(position);

        //Find TextView in custom_list_item.xml layout with id title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        String title = currentArticle.getTitle();
        titleTextView.setText(title);

        //Find TextView in custom_list_item layout with id section
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        String section = currentArticle.getSection();
        sectionTextView.setText(section);

        //Find TextView in custom_list_item layout with id author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);

        // Use ternary operator to determine if field is null, if not save string, if so, save empty string
        String author = (currentArticle.getAuthor() != null) ? currentArticle.getAuthor() : "";

        if (author == null) { // If String is empty, hide view in UI
            authorTextView.setVisibility(View.GONE);
        } else {
            authorTextView.setText(author);
        }

        //Find TextView in custom_list_item layout with id date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);

        // Use ternary operator to determine if field is null, if not save string, if so, save empty string
        String date = (currentArticle.getDate() != null) ? currentArticle.getDate() : "";

        if (date == null) { // If String is empty, hide view in UI
            dateTextView.setVisibility(View.GONE);
        } else {
            dateTextView.setText(date);

        }


        return listItemView;

    }


}
