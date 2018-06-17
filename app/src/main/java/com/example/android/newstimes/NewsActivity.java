package com.example.android.newstimes;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Loader;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * URL to query the GUARDIAN dataset for news information
     */
    private static final String GUARDIAN_JSON_RESPONSE =
            "http://content.guardianapis.com/search?&=";

    /**
     * Constant value for the news loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_news );

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById ( R.id.list );

        mEmptyStateTextView = (TextView) findViewById ( R.id.empty_view );
        listView.setEmptyView ( mEmptyStateTextView );

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter ( this, new ArrayList<News> () );

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter ( mAdapter );

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem ( position );

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse ( currentNews.getUrl () );

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent ( Intent.ACTION_VIEW, newsUri );

                // Send the intent to launch a new activity
                startActivity ( websiteIntent );
            }
        } );

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService ( Context.CONNECTIVITY_SERVICE );

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo ();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected ()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager ();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader ( NEWS_LOADER_ID, null, this );
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById ( R.id.loading_indicator );
            loadingIndicator.setVisibility ( View.GONE );

            // Update empty state with no connection error message
            mEmptyStateTextView.setText ( R.string.no_internet_connection );
        }
    }

    @Override
    // Instantiate and return a new Loader for the given ID
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences ( this );

        // Retrieve a String value from the preferences.
        // The second parameter is the default value for this preference.
        String orderBy = sharedPrefs.getString (
                getString ( R.string.settings_order_by_key ),
                getString ( R.string.settings_order_by_default )
        );

        // Break apart the URI string that's passed into the parse parameter
        Uri baseUri = Uri.parse ( GUARDIAN_JSON_RESPONSE );

        // Prepare the baseUri that we just parsed so we can add query parameters to uriBuilder
        Uri.Builder uriBuilder = baseUri.buildUpon ();

        // Append query parameter and its value
        uriBuilder.appendQueryParameter ( "section", orderBy );
        uriBuilder.appendQueryParameter ( "api-key", "ac9899fd-c9d5-4466-a9e2-905e19857895" );

        // Return the completed uri
        return new NewsLoader ( this, uriBuilder.toString () );
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById ( R.id.loading_indicator );
        loadingIndicator.setVisibility ( View.GONE );

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText ( R.string.no_news );
        // Clear the adapter of previous news data
        mAdapter.clear ();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty ()) {
            mAdapter.addAll ( news );
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent ( this, SettingsActivity.class );
            startActivity ( settingsIntent );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }
}
