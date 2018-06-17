package com.example.android.newstimes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * The part of the date string from the GUARDIAN service that we use to determine
     * whether or not there is a  date offset present ("2018-03-13T19:53:59Z").
     */
    private static final String DATE_SEPARATOR = "T";

    /**
     * Constructs a new {@link News}.
     *
     * @param context   of the app
     * @param listitems is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Activity context, ArrayList<News> listitems) {
        super ( context, 0, listitems );
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from ( getContext () ).inflate (
                    R.layout.news_list_item, parent, false );
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem ( position );

        // Find the TextView in the news_list_item_item.xml layout with the ID section
        TextView sectionTextView = (TextView) listItemView.findViewById ( R.id.section );
        // Get the section from the current News object and
        // set this text on the section TextView
        sectionTextView.setText ( currentNews.getSection () );

        // Find the TextView in the news_list_item_item.xml layout with the ID title
        TextView titleTextView = (TextView) listItemView.findViewById ( R.id.title_text_view );
        // Get the title from the current News object and
        // set this text on the title TextView
        titleTextView.setText ( currentNews.getTitle () );

        // Get the original date string from the News object,
        // which can be in the format of "2018-03-13T19:53:59Z"
        String originalDate = currentNews.getDate ();

        // If the original date string (i.e. 2018-03-13T19:53:59Z") contains
        // a primary date (2018-03-13) and a date offset (19:53:59Z)
        // then store the primary date separately from the date offset in 2 Strings,
        // so they can be displayed in 2 TextViews.
        //but in this app we only display 1 TextView.
        String dateOffset;

        // Split the string into different parts (as an array of Strings)
        // based on the " T " text. We expect an array of 1 String, where
        // the String will be "2018-03-13".
        String[] parts = originalDate.split ( DATE_SEPARATOR );

        // date offset should be "2018-03-13 "
        dateOffset = parts[0];

        // Find the TextView with view ID location date_offset
        TextView locationOffsetView = (TextView) listItemView.findViewById ( R.id.date_offset );
        // Display the date offset of the current news in that TextView
        locationOffsetView.setText ( dateOffset );

        // Return the whole list item layout (containing 3 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
