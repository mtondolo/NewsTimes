package com.example.android.newstimes;

/**
 * {@link News} represents a single news from a list of news.
 * Each news object has 3 properties: section, title and date.
 */

public class News {

    /**
     * Section of the news
     */
    public final String mSection;

    /**
     * Title of the news
     */
    public final String mTitle;

    /**
     * Date of news
     */
    public final String mDate;

    /**
     * Url link of news
     */
    private String mUrl;

    /**
     * Constructs a new {@News}.
     *
     * @param section is the section of the new item
     * @param title   is the title of the news item
     * @param date    is the time at which the news item was published
     */
    public News(String section, String title, String date, String url) {
        mSection = section;
        mTitle = title;
        mDate = date;
        mUrl = url;
    }

    /**
     * Get the section of the news
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Get the title of the news
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Get the title of the news
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Get the url link of the news
     */
    public String getUrl() {
        return mUrl;
    }

}

